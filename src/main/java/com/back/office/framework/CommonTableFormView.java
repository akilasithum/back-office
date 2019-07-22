package com.back.office.framework;

import com.back.office.db.DBConnection;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.formula.functions.T;
import org.vaadin.viritin.label.Header;

import java.awt.*;

public abstract class CommonTableFormView extends VerticalLayout implements View {

    protected VerticalLayout mainLayout;
    protected VerticalLayout gridLayout;
    protected VerticalLayout formLayout;
    protected Button exportToExcelBtn;
    protected Button downloadPdfBtn;
    protected Button addButton;
    protected Label header;
    protected DBConnection connection;
    protected Grid<?> mainGrid;

    /*public CommonTableFormView(){

        createLayout();
    }*/

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    protected void createLayout(){
        connection=DBConnection.getInstance();
        mainLayout = new VerticalLayout();
        mainLayout.setSizeUndefined();
        mainLayout.setWidth("85%");
        addComponent(mainLayout);
        setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName("mainLayoutBackground");

        header = new Label();
        header.setStyleName("headerLabel");
        gridLayout = new VerticalLayout();
        gridLayout.setSizeFull();
        exportToExcelBtn = new Button();
        downloadPdfBtn = new Button();
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(exportToExcelBtn,downloadPdfBtn);
        exportToExcelBtn.setIcon(FontAwesome.FILE_EXCEL_O);
        exportToExcelBtn.setDescription("Export to Excel");
        downloadPdfBtn.setIcon(FontAwesome.FILE_PDF_O);
        downloadPdfBtn.setDescription("Download PDF");
        gridLayout.addComponent(btnLayout);
        gridLayout.setComponentAlignment(btnLayout,Alignment.TOP_RIGHT);
        mainGrid = getGrid();
        mainGrid.setStyleName("mainGrid");
        gridLayout.addComponent(mainGrid);

        formLayout = new VerticalLayout();
        formLayout.setSizeFull();
        addButton = new Button(getAddBtnName());
        addButton.setIcon(FontAwesome.NEWSPAPER_O);
        addButton.setStyleName("btn-two");
        formLayout.addComponents(addButton);
        formLayout.setMargin(false);
        gridLayout.setMargin(false);
        mainLayout.addComponents(header,formLayout,gridLayout);

    }

    protected abstract String getAddBtnName();

    protected abstract Grid<?> getGrid();
}
