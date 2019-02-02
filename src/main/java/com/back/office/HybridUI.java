package com.back.office;

import com.back.office.db.DBConnection;
import com.back.office.entity.BlackListCC;
import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.RolePermission;
import com.back.office.ui.*;
import com.back.office.ui.authorization.ManageRolesView;
import com.back.office.ui.bondReports.FlightBondActivityReportView;
import com.back.office.ui.salesReports.CategorySalesView;
import com.back.office.ui.salesReports.FlightPaymentDetailsView;
import com.back.office.ui.salesReports.SalesDetailsView;
import com.back.office.ui.uploads.ErrorView;
import com.back.office.ui.uploads.UploadView;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.annotations.*;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.JavaScript;
import elemental.json.JsonArray;
import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.components.*;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.design.DesignItem;
import org.vaadin.dialogs.ConfirmDialog;

import javax.servlet.annotation.WebServlet;
import java.util.*;

@SuppressWarnings("deprecation")

@Theme("demo")
@Widgetset("com.back.office.MyAppWidgetset")
@Title("Porter - Back Office")
@Push
@PushStateNavigation
public class HybridUI extends UI implements ClientConnector.DetachListener {

    private Navigator navigator;
    private List<String> menuItems;
    private DBConnection connection;
    private String previousPage;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = true, ui = HybridUI.class)
    public static class Servlet extends VaadinServlet {
        private static final long serialVersionUID = -2926441566643769901L;
    }

    private HybridMenu hybridMenu = null;
    @Override
    protected void init(VaadinRequest request) {
        hybridMenu = HybridMenu.get()
                .withNaviContent(new VerticalLayout())
                .withConfig(MenuConfig.get().withDesignItem(DesignItem.getWhiteDesign()))
                .build();
        connection = DBConnection.getInstance();
        setContent(hybridMenu);
        navigator = new Navigator(this, hybridMenu.getNaviContent());
        navigator.addView("login", LoginPage.class);
        navigator.addView("dashboard", Dashboard.class);
        navigator.addView("AircraftType", AirCraftTypeView.class);
        navigator.addView("Currency", CurerncyDetailsView.class);
        navigator.addView("CreateItems", ItemView.class);
        navigator.addView("CreateKitCodes", KitCodesView.class);
        navigator.addView("EquipmentTypes", EquipmentTypeView.class);
        navigator.addView("AssignItems", AssignItemView.class);
        navigator.addView("FlightDetails", FlightDetailsView.class);
        navigator.addView("Staff", UserDetailsView.class);
        navigator.addView("CCBlackList", BlackListCCView.class);
        navigator.addView("Vouchers", VoucherView.class);
        navigator.addView("SalesDetails", SalesDetailsView.class);
        navigator.addView("SalesSummarybyFlight", FlightPaymentDetailsView.class);
        navigator.addView("SalebyCategory", CategorySalesView.class);
        navigator.addView("ManageUserRoles", ManageRolesView.class);
        navigator.addView("ManageUsers", UserDetailsView.class);
        navigator.addView("uploads", UploadView.class);
        navigator.addView("Promotions", PromotionView.class);
        navigator.addView("ErrorView", ErrorView.class);
        navigator.addView("CCNumberRange", CCNumberRangeView.class);
        navigator.addView("Flight-BondActivity", FlightBondActivityReportView.class);

        String f = Page.getCurrent().getUriFragment();

        getPage().addUriFragmentChangedListener(uriFragmentChangedEvent -> {
            String currentPage = uriFragmentChangedEvent.getUriFragment();
             if(previousPage != null && previousPage.equals("login")){
                getUI().getNavigator().navigateTo("login");
            }
            else {
                previousPage = currentPage;
            }
        });

        if(getSession().getAttribute("userName") == null || getSession().getAttribute("userName").toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
        else {
            if (f == null || f.equals("") || !f.equals("login")) {
                buildTopOnlyMenu();
                buildLeftMenu();
            }
        }
        getUI().getNavigator().setErrorView(ErrorView.class);

        JavaScript.getCurrent().addFunction("aboutToClose", new JavaScriptFunction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void call(JsonArray arguments) {
                detach();
            }
        });

        Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");
    }

    public void navigate(){
        getPermissionCodes();
        buildTopOnlyMenu();
        buildLeftMenu();
        previousPage = "login ok";
        navigator.navigateTo("dashboard");
    }

    private void getPermissionCodes(){
        if(getSession().getAttribute("permissionCodes") == null) {
            getSession().setAttribute("permissionCodes", BackOfficeUtils.getPermissionCodes());
        }
    }

    private void removeMenus(){
        hybridMenu.getTopMenu().removeAllComponents();
        hybridMenu.getLeftMenu().removeAllComponents();

    }

    private void buildTopOnlyMenu() {
        TopMenu topMenu = hybridMenu.getTopMenu();

        topMenu.add(HMTextField.get(VaadinIcons.SEARCH, "Search ..."));

        topMenu.add(HMButton.get()
                .withIcon(VaadinIcons.HOME)
                .withDescription("Home")
                .withNavigateTo(Dashboard.class));

        hybridMenu.getNotificationCenter()
                .setNotiButton(topMenu.add(HMButton.get()
                        .withDescription("Notifications")));

        topMenu.add(HMButton.get()
                .withIcon(VaadinIcons.POWER_OFF)
                .withDescription("Logout")
                .withClickListener((Button.ClickListener) clickEvent -> {
                    logoutUser();
                }));
    }

    private void logoutUser(){
        ConfirmDialog.show(getUI(), "Logout", "Are you sure you want to log out from the system?",
                "Yes", "No", new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            getSession().setAttribute("userName","");
                            removeMenus();
                            getUI().getNavigator().navigateTo("login");
                        }
                    }
                });
    }

    private void buildLeftMenu() {

        menuItems = new ArrayList<>();
        menuItems.add("Authorization");
        menuItems.add("Setup");
        menuItems.add("Uploads");
        menuItems.add("Generate XML");
        menuItems.add("Bond Reports");
        menuItems.add("Sales Report");
        menuItems.add("Analysis");
        menuItems.add("Special Reports");
        menuItems.add("Pre Order Management");
        menuItems.add("CRM");

        List<String> noChildMenus = new ArrayList<>();
        noChildMenus.add("Uploads");
        noChildMenus.add("CRM");

        LeftMenu leftMenu = hybridMenu.getLeftMenu();

        HMLabel hmLabel = HMLabel.get()
                .withCaption("<h3><strong>Porter AirLines</strong></h3>");
        hmLabel.setStyleName("logo");
        leftMenu.add(hmLabel);
                //.withIcon(new ClassResource("logo.png")));
        //leftMenu.setStyleName("logo");

        List<RolePermission> rolePermissions = connection.getFilterList("roleIdFilter","roleId",3,
                "com.back.office.entity.RolePermission","permissionCode");
        Map<String, Map<Integer, String>> funcAreasCodesMap = (Map<String, Map<Integer, String>>)getSession().getAttribute("permissionCodes");
        List<Integer> userPermissions = new ArrayList<>();
        for(RolePermission rolePermission : rolePermissions){
            userPermissions.add(rolePermission.getPermissionCode());
        }

        /*hybridMenu.getBreadCrumbs().setRoot(leftMenu.add(HMButton.get()
                .withCaption("Dashboard")
                .withIcon(VaadinIcons.HOME)
                .withNavigateTo(Dashboard.class)));*/
        Map<String,VaadinIcons> iconsMap = BackOfficeUtils.getIconMap();
        for(String menu : menuItems){
            if(noChildMenus.contains(menu)){
                if(menu.equalsIgnoreCase("Uploads")) {
                    leftMenu.add(HMButton.get()
                            .withCaption(menu).withIcon(iconsMap.get(menu))
                            .withNavigateTo(UploadView.class));
                }
                else if(menu.equalsIgnoreCase("CRM")){
                    leftMenu.add(HMButton.get()
                            .withCaption(menu).withIcon(iconsMap.get(menu))
                            .withNavigateTo(ErrorView.class));
                }
            }
            else{
                HMSubMenu mainMenu = leftMenu.add(HMSubMenu.get()
                        .withCaption(menu)
                        .withIcon(iconsMap.get(menu)));
                for(String subMeu : BackOfficeUtils.getSubMeuList(menu)){
                    Map<Integer,String> permissionMap = funcAreasCodesMap.get(menu);
                    Map<String,Integer> reverseMap = new HashMap<>();
                    if(permissionMap != null) {
                        for (Map.Entry<Integer, String> map : permissionMap.entrySet()) {
                            reverseMap.put(map.getValue(), map.getKey());
                        }
                    }
                    String name = subMeu.replace(" ","");
                    if(reverseMap.isEmpty() || userPermissions.contains(reverseMap.get(name))) {
                        mainMenu.add(HMButton.get()
                                .withCaption(subMeu).withNavigateTo(subMeu.replace(" ","")));
                    }

                }
            }
        }

        HMSubMenu demoSettings = leftMenu.add(HMSubMenu.get()
                .withCaption("Settings")
                .withIcon(VaadinIcons.COGS));

        demoSettings.add(HMButton.get()
                .withCaption("White Theme")
                .withIcon(VaadinIcons.PALETE)
                .withClickListener(e -> hybridMenu.switchTheme(DesignItem.getWhiteDesign())));

        demoSettings.add(HMButton.get()
                .withCaption("Dark Theme")
                .withIcon(VaadinIcons.PALETE)
                .withClickListener(e -> hybridMenu.switchTheme(DesignItem.getDarkDesign())));

        demoSettings.add(HMButton.get()
                .withCaption("Minimal")
                .withIcon(VaadinIcons.COG)
                .withClickListener(e -> hybridMenu.getLeftMenu().toggleSize()));
    }

    public HybridMenu getHybridMenu() {
        return hybridMenu;
    }

    @Override
    public void detach(DetachEvent event) {
        getUI().close();
    }


}
