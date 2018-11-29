package com.back.office;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.back.office.ui.AirCraftTypeView;
import com.back.office.ui.CurrencyView;
import com.back.office.ui.Dashboard;
import com.back.office.ui.ItemView;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("valo")
@Widgetset("com.back.office.MyAppWidgetset")
@StyleSheet("valo-theme-ui.css")
public class MyUI extends UI {

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
        navigator.addView("common", Dashboard.class);

        navigator.addView("aircraft-type", AirCraftTypeView.class);
        navigator.addView("currency", CurrencyView.class);
        navigator.addView("create-items", ItemView.class);
        /*navigator.addView("login", LoginPage.class);
        navigator.addView("flight-number", ButtonsAndLinks.class);
        navigator.addView("equipment-types", ComboBoxes.class);
        navigator.addView("assign-items", CheckBoxes.class);
        navigator.addView("create-kit-codes", Sliders.class);
        navigator.addView("staff", MenuBars.class);
        navigator.addView("cc-black-list", Panels.class);
        navigator.addView("cc-number-range", Trees.class);
        navigator.addView("promotions", Tables.class);
        navigator.addView("update-inventory", SplitPanels.class);*/
        /*navigator.addView("tabs", Tabsheets.class);
        navigator.addView("accordions", Accordions.class);
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

        String f = Page.getCurrent().getUriFragment();
        if (f == null || f.equals("")) {
            navigator.navigateTo("common");
        }

        navigator.setErrorView(Dashboard.class);

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


    CssLayout buildMenu() {
        // Add items
        menuItems.put("common", "Dashboard");
        menuItems.put("aircraft-type", "Aircraft Type");
        menuItems.put("flight-number", "Flight Number");
        menuItems.put("currency", "Currency");
        menuItems.put("create-items", "Create Items");
        menuItems.put("equipment-types", "Equipment Types");
        menuItems.put("assign-items", "Assign Items");
        menuItems.put("create-kit-codes", "Create Kit Codes");
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
        menu.addComponent(createThemeSelect());

        Button showMenu = new Button("Menu", new Button.ClickListener() {
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
        menu.addComponent(showMenu);

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
        settingsItem.addItem("Preferences", null);
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", null);
        menu.addComponent(settings);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        Label label = null;
        int count = -1;
        for (final Map.Entry<String, String> item : menuItems.entrySet()) {
            if (item.getKey().equals("aircraft-type")) {
                label = new Label("Setup", ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("panels")) {
                label.setValue(label.getValue()
                        + " <span class=\"valo-menu-badge\">" + count
                        + "</span>");
                count = 0;
                label = new Label("Containers", ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("calendar")) {
                label.setValue(label.getValue()
                        + " <span class=\"valo-menu-badge\">" + count
                        + "</span>");
                count = 0;
                label = new Label("Other", ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            Button b = new Button(item.getValue(), new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    navigator.navigateTo(item.getKey());
                }
            });
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName(ValoTheme.MENU_ITEM);
            b.setIcon(BackOfficeUtils.getIconMap().get(item.getKey()));
            menuItemsLayout.addComponent(b);
            count++;
        }
        /*label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">"
                + count + "</span>");*/

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
