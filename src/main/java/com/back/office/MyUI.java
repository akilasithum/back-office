package com.back.office;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.back.office.db.DBConnection;
import com.back.office.entity.PermissionCodes;
import com.back.office.entity.RolePermission;
import com.back.office.ui.*;
import com.back.office.ui.authorization.ManageRolesView;
import com.back.office.ui.salesReports.CategorySalesView;
import com.back.office.ui.salesReports.FlightPaymentDetailsView;
import com.back.office.ui.salesReports.SalesDetailsView;
import com.back.office.ui.uploads.UploadView;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("tests-valo-facebook")
@Widgetset("com.back.office.MyAppWidgetset")
@StyleSheet("valo-theme-ui.css")
public class MyUI extends UI {

    protected DBConnection connection;
    private static LinkedHashMap<String, String> themeVariants = new LinkedHashMap<String, String>();
    static {
        themeVariants.put(ValoTheme.THEME_NAME, "Valo");
        themeVariants.put("midsummer-night", "Midsummer Night");
        themeVariants.put("tests-valo-blueprint", "Blueprint");
        themeVariants.put("tests-valo-dark", "Dark");
        themeVariants.put("tests-valo-facebook", "Facebook");
        themeVariants.put("tests-valo-flatdark", "Flat dark");
        themeVariants.put("tests-valo-flat", "Flat");
        themeVariants.put("tests-valo-light", "Light");
        themeVariants.put("tests-valo-metro", "Metro");
        themeVariants.put("tests-valo-reindeer", "Migrate Reindeer");
    }
    ValoMenuLayout root = new ValoMenuLayout();
    ComponentContainer viewDisplay = root.getContentContainer();
    CssLayout menu = new CssLayout();
    CssLayout menuItemsLayout = new CssLayout();
    {
        menu.setId("testMenu");
    }
    private Navigator navigator;
    private LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().addSessionInitListener(
                    new ValoThemeSessionInitListener());
        }
    }

    @Override
    protected void init(VaadinRequest request) {

        connection = DBConnection.getInstance();
        if (request.getParameter("test") != null) {

            if (browserCantRenderFontsConsistently()) {
                getPage().getStyles().add(
                        ".v-app.v-app.v-app {font-family: Sans-Serif;}");
            }
        }

        if (getPage().getWebBrowser().isIE()
                && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
            menu.setWidth("320px");
        }

        Responsive.makeResponsive(this);

        getPage().setTitle("Porter Airlines - Back Office");
        setContent(root);
        root.setWidth("100%");
        navigator = new Navigator(this, viewDisplay);
        navigator.addView("dashboard", Dashboard.class);
        navigator.addView("AircraftType", AirCraftTypeView.class);
        navigator.addView("Currency", CurerncyDetailsView.class);
        navigator.addView("CreateItems", ItemView.class);
        navigator.addView("login", LoginPage.class);
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
        /*navigator.addView("accordions", Accordions.class);
        navigator.addView("colorpickers", ColorPickers.class);
        navigator.addView("selects", NativeSelects.class);
        navigator.addView("calendar", CalendarTest.class);
        navigator.addView("forms", Forms.class);
        navigator.addView("popupviews", PopupViews.class);
        navigator.addView("dragging", Dragging.class);*/
        /*if(VaadinSession.getCurrent().getAttribute("user") == null){
            navigator.navigateTo("login");
            return;
        }*/

        root.addMenu(buildMenu());
        addStyleName(ValoTheme.UI_WITH_MENU);

        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                for (Iterator<Component> it = menuItemsLayout.iterator(); it
                        .hasNext();) {
                    it.next().removeStyleName("selected");
                }
                for (Map.Entry<String, String> item : menuItems.entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
                        for (Iterator<Component> it = menuItemsLayout
                                .iterator(); it.hasNext();) {
                            Component c = it.next();
                            if (c.getCaption() != null
                                    && c.getCaption().startsWith(
                                    item.getValue())) {
                                c.addStyleName("selected");
                                break;
                            }
                        }
                        break;
                    }
                }
                menu.removeStyleName("valo-menu-visible");
            }
        });

        String f = Page.getCurrent().getUriFragment();

        if(getSession().getAttribute("userName") == null || getSession().getAttribute("userName").toString().isEmpty()){
            root.removeComponent(root.getComponent(0));
            getUI().getNavigator().navigateTo("login");
        }
        else {
            if (f == null || f.equals("") || f.equals("!login")) {
                navigator.navigateTo("dashboard");
            }
        }
        navigator.setErrorView(Dashboard.class);

    }

    private void getPermissionCodes(){
        if(getSession().getAttribute("permissionCodes") == null) {
            getSession().setAttribute("permissionCodes", BackOfficeUtils.getPermissionCodes());
        }
    }

    public void navigate(){
        if(root.getComponentCount() == 1){
            getPermissionCodes();
            root.addComponent(buildMenu(),0);
        }
    }

    public boolean isLoggedIn(){
        return getSession() != null && getSession().getAttribute("userName") != null;
    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so
        // disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592

        // IE8 also has randomness in its font rendering...

        return getPage().getWebBrowser().getBrowserApplication()
                .contains("PhantomJS")
                || (getPage().getWebBrowser().isIE() && getPage()
                .getWebBrowser().getBrowserMajorVersion() <= 9);
    }

    Tree buitTreeMenu(){
        Tree tree = new Tree();
        tree.setImmediate(true);
        tree.setContainerDataSource(getContainer());
        tree.setItemCaptionPropertyId("displayName");
        tree.setNullSelectionAllowed(false);
        tree.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                if (value instanceof String && !Arrays.asList(GROUP_WITH_CHILD).contains(value)) {
                   navigator.navigateTo(value.toString());
                } else {
                    for(int i=0;i<GROUP_ORDER.length;i++){
                        tree.collapseItem(GROUP_ORDER[i]);
                    }
                    tree.expandItemsRecursively(value);
                }
            }
        });

        for(Map.Entry<String,FontAwesome> icons : BackOfficeUtils.getIconMap().entrySet()){
            tree.setItemIcon(icons.getKey(),icons.getValue());
        }
        return tree;
    }

    private static final String[] GROUP_CAPTIONS= {"Dashboard", "Authorization",
            "Setup", "Uploads", "Generate XML", "Bond Reports", "Sales Report",
            "Analysis", "Special Reports", "Pre Order Management", "CRM" };
    private static final String[] GROUP_ORDER = {"dashboard", "authorization",
            "setup", "uploads", "generateXML", "bondReports", "salesReport",
            "analysis", "specialReports", "preOrderManagement", "CRM" };
    private static final String[] GROUP_WITH_CHILD = { "authorization",
            "setup", "generateXML", "bondReports", "salesReport",
            "analysis", "specialReports", "preOrderManagement" };

    private HierarchicalContainer getContainer() {

        HierarchicalContainer hierarchicalContainer = new HierarchicalContainer();
        hierarchicalContainer.addContainerProperty("displayName", String.class,
                "");
        hierarchicalContainer.addContainerProperty("searchName", String.class,
                "");
        String userName = "";
        if(getSession().getAttribute("userName") != null){
            userName = getSession().getAttribute("userName").toString();
        }
        List<RolePermission> rolePermissions = connection.getFilterList("roleIdFilter","roleId",3,
                "com.back.office.entity.RolePermission");
        Map<String, Map<Integer, String>> funcAreasCodesMap = (Map<String, Map<Integer, String>>)getSession().getAttribute("permissionCodes");
        if(funcAreasCodesMap == null){
            return hierarchicalContainer;
        }
        List<Integer> userPermissions = new ArrayList<>();
        for(RolePermission rolePermission : rolePermissions){
            userPermissions.add(rolePermission.getPermissionCode());
        }

        for (int i = 0; i < GROUP_CAPTIONS.length; i++) {
            String group = GROUP_ORDER[i];
            String groupName = GROUP_CAPTIONS[i];
            Item groupItem = hierarchicalContainer.addItem(group);
            groupItem.getItemProperty("displayName").setValue(groupName);
            groupItem.getItemProperty("searchName").setValue(groupName);
            List<String> list = new ArrayList<>();
            if(groupName.equals("Setup")){
                list = BackOfficeUtils.getSetupMap();
            }
            else if(groupName.equals("Sales Report")){
                list = BackOfficeUtils.getSellsReportsMap();
            }
            else if(groupName.equals("Authorization")){
                list = BackOfficeUtils.getAuthorizationMap();
            }
            if(groupName.equals("Dashboard") || groupName.equals("CRM") || groupName.equals("Uploads")){
                hierarchicalContainer.setChildrenAllowed(group, false);
            }
            Map<Integer,String> permissionMap = funcAreasCodesMap.get(groupName);
            Map<String,Integer> reverseMap = new HashMap<>();
            if(permissionMap != null) {
                for (Map.Entry<Integer, String> map : permissionMap.entrySet()) {
                    reverseMap.put(map.getValue(), map.getKey());
                }
            }
            for (String itemName : list) {
                String name = itemName.replace(" ","");
                if(reverseMap.isEmpty() || userPermissions.contains(reverseMap.get(name))) {
                    Item testItem = hierarchicalContainer.addItem(itemName.replace(" ",""));
                    testItem.getItemProperty("displayName").setValue(itemName);
                    testItem.getItemProperty("searchName").setValue(
                            groupName + " " + name);
                    hierarchicalContainer.setParent(name, group);
                    hierarchicalContainer.setChildrenAllowed(name, false);
                }
            }
        }

        return hierarchicalContainer;
    }

    CssLayout buildMenu() {
        // Add items
        menuItems.put("common", "Dashboard");
        menuItems.put("aircraft-type", "Aircraft Type");
        menuItems.put("flight-number", "Flight Number");
        menuItems.put("currency", "Currency");
        menuItems.put("create-items", "Create Items");
        menuItems.put("create-kit-codes", "Create Kit Codes");
        menuItems.put("equipment-types", "Equipment Types");
        menuItems.put("assign-items", "Assign Items");
        menuItems.put("staff", "Staff");
        menuItems.put("cc-black-list", "CC Black List");
        menuItems.put("cc-number-range", "CC Number Range");
        menuItems.put("promotions", "Promotions");
        menuItems.put("update-inventory", "Update Inventory");

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(ValoTheme.MENU_TITLE);
        menu.addComponent(top);
        //menu.addComponent(createThemeSelect());

        /*Button showMenu = new Button("Menu", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (menu.getStyleName().contains("valo-menu-visible")) {
                    menu.removeStyleName("valo-menu-visible");
                } else {
                    menu.addStyleName("valo-menu-visible");
                }
            }
        });
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName("valo-menu-toggle");
        showMenu.setIcon(FontAwesome.LIST);
        menu.addComponent(showMenu);*/

        Label title = new Label("<h3><strong>Porter AirLines</strong></h3>",
                ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);

        MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        MenuBar.MenuItem settingsItem = settings.addItem("Akila Sithum",
                new ClassResource("profile-pic-300px.jpg"),
                null);
        settingsItem.addItem("Edit Profile", null);
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", null);
        menu.addComponent(settings);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(buitTreeMenu());
        return menu;
    }

    private Component createThemeSelect() {
        final NativeSelect ns = new NativeSelect();
        ns.setNullSelectionAllowed(false);
        ns.setId("themeSelect");
        ns.addContainerProperty("caption", String.class, "");
        ns.setItemCaptionPropertyId("caption");
        for (String identifier : themeVariants.keySet()) {
            ns.addItem(identifier).getItemProperty("caption")
                    .setValue(themeVariants.get(identifier));
        }

        ns.setValue(ValoTheme.THEME_NAME);
        ns.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setTheme((String) ns.getValue());
            }
        });
        return ns;
    }
}
