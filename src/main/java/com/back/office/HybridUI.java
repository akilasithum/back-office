package com.back.office;

import com.back.office.db.DBConnection;
import com.back.office.entity.BlackListCC;
import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.RolePermission;
import com.back.office.ui.*;
import com.back.office.ui.authorization.ManageRolesView;
import com.back.office.ui.authorization.ViewAndEditCurrentUserDetailsView;
import com.back.office.ui.bondReports.FlightBondActivityReportView;
import com.back.office.ui.dashboard.*;
import com.back.office.ui.download.DownloadView;
import com.back.office.ui.finance.CurruncyDetailHistory;
import com.back.office.ui.finance.GrossMargine;
import com.back.office.ui.flightKitchen.DailyFlightsView;
import com.back.office.ui.flightKitchen.FlightScheduleView;
import com.back.office.ui.flightKitchen.RequestInventory;
import com.back.office.ui.message.BondMessage;
import com.back.office.ui.preOrder.PreOrders;
import com.back.office.ui.salesReports.*;
import com.back.office.ui.uploads.ErrorView;
import com.back.office.ui.uploads.UploadView;
import com.back.office.ui.wizard.AircraftDetailsView;
import com.back.office.ui.wizard.CreateFlightView;
import com.back.office.ui.wizard.CreateItemView;
import com.back.office.ui.wizard.EquipmentView;
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
import kaesdingeling.hybridmenu.components.*;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.design.DesignItem;
import org.vaadin.dialogs.ConfirmDialog;

import javax.servlet.annotation.WebServlet;
import java.util.*;

@SuppressWarnings("deprecation")

@Theme("mytheme")
@Widgetset("com.back.office.MyAppWidgetset")
@Title("Porter - Back Office")
@Push
@PushStateNavigation
public class HybridUI extends UI implements ClientConnector.DetachListener {

    private Navigator navigator;
    private List<String> menuItems;
    private DBConnection connection;
    private String previousPage;
    private List<String> noPermissionNeededViews;
    private TopMenu topMenu = new TopMenu();
    private VerticalLayout mainLayout;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = true, ui = HybridUI.class)
    public static class Servlet extends VaadinServlet {
        private static final long serialVersionUID = -2926441566643769901L;
    }

    private MainMenu hybridMenu = null;
    @Override
    protected void init(VaadinRequest request) {
        hybridMenu = MainMenu.get()
                .withNaviContent(new VerticalLayout())
                .withConfig(MenuConfig.get().withDesignItem(DesignItem.getWhiteDesign()))
                .build();
        connection = DBConnection.getInstance();
        setContent(hybridMenu);
        hybridMenu.setStyleName("hybridMenu");
        navigator = new Navigator(this, hybridMenu.getNaviContent());
        navigator.addView("login", LoginPage.class);
        navigator.addView("dashboard", MainDashboard.class);
        navigator.addView("AircraftType", AircraftDetailsView.class);
        navigator.addView("Currency", CurerncyDetailsView.class);
        navigator.addView("CreateItems", CreateItemView.class);
        navigator.addView("CreateKitCodes", KitCodesView.class);
        navigator.addView("EquipmentTypes", EquipmentView.class);
        navigator.addView("AssignItems", AssignItemView.class);
        navigator.addView("FlightDetails", CreateFlightView.class);
        navigator.addView("Staff", UserDetailsView.class);
        navigator.addView("CCBlackList", BlackListCCView.class);
        navigator.addView("Vouchers", VoucherView.class);
        navigator.addView("SalesDetails", SalesDetailsView.class);
        navigator.addView("SalesSummarybyFlight", FlightPaymentDetailsView.class);
        navigator.addView("SalebyCategory", CategorySalesView.class);
        navigator.addView("ManageUserRoles", ManageRolesView.class);
        navigator.addView("ManageUsers", UserDetailsView.class);
        navigator.addView("Uploads", UploadView.class);
        navigator.addView("Promotions", PromotionView.class);
        navigator.addView("ErrorView", ErrorView.class);
        navigator.addView("CCNumberRange", CCNumberRangeView.class);
        navigator.addView("Flight-BondActivity", FlightBondActivityReportView.class);
        navigator.addView("ViewandEditUser", ViewAndEditCurrentUserDetailsView.class);
        navigator.addView("CreditCardSummary", CreditCardSummaryView.class);
        navigator.addView("CreditCardbyFlight", CreditCardSummaryByFlightView.class);
        navigator.addView("TenderSummary",TenderSummaryView.class);
        navigator.addView("Downloads", DownloadView.class);
        navigator.addView("PreOrders", PreOrders.class);

        navigator.addView("setup", SetupDashboardView.class);
        navigator.addView("flightKitchen", FlightKitchenDashboardView.class);
        navigator.addView("preOrderMessenger", PreOrderMessangerDashboardView.class);
        navigator.addView("finance", FinanceDashboardView.class);
        navigator.addView("reports", ReportsDashboardView.class);
        navigator.addView("inventory",InventoryDashboardView.class);
        navigator.addView("crm",CRMDashboardView.class);
        navigator.addView("analyze",AnalyzeDashboardView.class);
        navigator.addView("BondMessages", BondMessage.class);
        navigator.addView("CurrencyHistory", CurruncyDetailHistory.class);
        navigator.addView("RequestInventory", RequestInventory.class);
        navigator.addView("GrossMargins", GrossMargine.class);
        navigator.addView("Budget", BudgetView.class);
        navigator.addView("FlightSchedule", FlightScheduleView.class);
        navigator.addView("DailyFlights", DailyFlightsView.class);

        String f = Page.getCurrent().getUriFragment();
        String query = Page.getCurrent().getLocation().getQuery();
        if(query != null && !query.isEmpty()){
            String[] params = query.split("=");
            if(params.length == 2 && params[0].equals("userName")){
                getSession().setAttribute("userName",params[1]);
                navigate();
                return;
            }
        }


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
                hybridMenu.addLogo();
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
        hybridMenu.addLogo();
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
        hybridMenu.getLogoLayout().removeAllComponents();

    }

    private void buildTopOnlyMenu() {
        TopMenu topMenu = hybridMenu.getTopMenu();

        topMenu.add(HMTextField.get(VaadinIcons.SEARCH, "Search ..."));

        topMenu.add(HMButton.get()
                .withIcon(VaadinIcons.HOME)
                .withDescription("Home")
                .withNavigateTo(MainDashboard.class));

        topMenu.add(HMButton.get()
                .withIcon(VaadinIcons.COMMENT_ELLIPSIS)
                .withDescription("Messages")
                .withNavigateTo(BondMessage.class));

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

    public MainMenu getHybridMenu() {
        return hybridMenu;
    }

    @Override
    public void detach(DetachEvent event) {
        getUI().close();
    }


}
