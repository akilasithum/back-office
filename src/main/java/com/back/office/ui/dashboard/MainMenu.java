package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import kaesdingeling.hybridmenu.components.BreadCrumbs;
import kaesdingeling.hybridmenu.components.LeftMenu;
import kaesdingeling.hybridmenu.components.TopMenu;
import kaesdingeling.hybridmenu.data.DefaultViewChangeManager;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.data.interfaces.ViewChangeManager;
import kaesdingeling.hybridmenu.design.DesignItem;
import kaesdingeling.hybridmenu.page.DefaultPage;


public class MainMenu extends VerticalLayout {

    private static final long serialVersionUID = -4055770717384786366L;

    public static final String CLASS_NAME = "hybridMenu";

    private ViewChangeManager viewChangeManager = new DefaultViewChangeManager();
    private MenuConfig config = null;
    private boolean buildRunning = false;
    private boolean initNavigator = true;
    private LeftMenu leftMenu = new LeftMenu();

    /* Components */
    private HorizontalLayout content = new HorizontalLayout();

    private BreadCrumbs breadcrumbs = null;
    private Layout naviRootContent = null;
    private VerticalLayout rootContent = new VerticalLayout();
    private TopMenu topMenu = new TopMenu();
    private VerticalLayout logoLayout = new VerticalLayout();

    private Label css = new Label("", ContentMode.HTML);

    public static MainMenu get() {
        return new MainMenu();
    }

    public MainMenu() {
        super();
        setSizeFull();
        setMargin(false);
        setSpacing(false);
    }

    public MainMenu build() {
        if (!buildRunning) {
            UI ui = UI.getCurrent();

            if (config == null) {
                config = new MenuConfig();
            }
            if (naviRootContent == null) {
                naviRootContent = new VerticalLayout();
            }

            naviRootContent.setWidth(100, Unit.PERCENTAGE);
            naviRootContent.setStyleName("contentBox");

            if (initNavigator) {
                new Navigator(ui, naviRootContent);
                ui.getNavigator().setErrorView(DefaultPage.class);
            }


            HorizontalLayout layout = new HorizontalLayout();
            layout.setMargin(Constants.leftMargin);
            addComponent(layout);
            layout.addComponent(logoLayout);
            logoLayout.setMargin(Constants.noMargin);
            layout.addComponent(topMenu);

            content.setSizeFull();
            content.setMargin(false);
            content.setSpacing(false);
            content.setStyleName("myRootContent");
            addComponent(content);
            setExpandRatio(content, 1f);

            css.setHeight(0, Unit.PIXELS);
            css.setStyleName("customCss");

            content.addComponents(rootContent,css);
            content.setExpandRatio(rootContent, 1f);

            rootContent.setMargin(false);
            rootContent.setSpacing(false);
            rootContent.setSizeFull();

            if (config.isBreadcrumbs()) {
                breadcrumbs = new BreadCrumbs();
                rootContent.addComponent(breadcrumbs);
            }

            rootContent.addComponent(naviRootContent);
            if (config.isBreadcrumbs()) {
                rootContent.setExpandRatio(naviRootContent, 1f);
            }

            switchTheme(config.getDesignItem());
            VaadinSession.getCurrent().setAttribute(MenuConfig.class, config);
            VaadinSession.getCurrent().setAttribute(MainMenu.class, this);
            buildRunning = true;
        }
        return this;
    }

    public void addLogo(){
        if(logoLayout.getComponentCount() == 0) {
            Image logo = new Image();
            logo.setSource(new ClassResource("logo.png"));
            logo.setWidth(170, Unit.PIXELS);
            logo.setHeight(50, Unit.PIXELS);
            logo.setStyleName("imageMargin");
            logoLayout.addComponent(logo);
        }
    }

    public VerticalLayout getRootContent() {
        return rootContent;
    }

    public TopMenu getTopMenu() {
        return topMenu;
    }

    public VerticalLayout getLogoLayout() {
        return logoLayout;
    }

    public BreadCrumbs getBreadCrumbs() {
        return breadcrumbs;
    }

    public LeftMenu getLeftMenu() {
        return leftMenu;
    };

    public Layout getNaviContent() {
        return naviRootContent;
    }

    public MainMenu withNaviContent(Layout naviRootContent) {
        this.naviRootContent = naviRootContent;
        return this;
    }

    public MainMenu withInitNavigator(boolean initNavigator) {
        this.initNavigator = initNavigator;
        return this;
    }

    public MenuConfig getConfig() {
        return config;
    }

    public MainMenu withConfig(MenuConfig config) {
        this.config = config;
        return this;
    }

    public void switchTheme(DesignItem designItem) {
        if (designItem != null) {
            if (designItem.getMenuDesign() == null) {
                designItem.setMenuDesign(config.getDesignItem().getMenuDesign());
            } else {
                setStyleName(CLASS_NAME);
                addStyleName(designItem.getMenuDesign().getName());
            }
            config.withDesignItem(designItem);
            css.setValue("<style type=\"text/css\">" + designItem.convertToStyle() + "</style>");
        } else {
            css.setValue("");
        }
    }

    public void setViewChangeManager(ViewChangeManager viewChangeManager) {
        this.viewChangeManager = viewChangeManager;
    }
}
