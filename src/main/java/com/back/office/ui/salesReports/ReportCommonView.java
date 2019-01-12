package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public abstract class ReportCommonView extends VerticalLayout implements View {
    protected DBConnection connection;
    protected String pageHeader = "";
    protected String reportExcelHeader = "";
    protected VerticalLayout headerLayout;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout mainTableLayout;
    protected HorizontalLayout tableLayout;
    protected VerticalLayout mainUserInputLayout;
    protected Button searchButton;
    protected HorizontalLayout buttonRow;
    protected Table detailsTable;
    protected Label filterCriteriaText;
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public ReportCommonView(){
        connection = DBConnection.getInstance();
        setMargin(true);
        defineStringFields();
        createMainLayout();
    }

    protected void createMainLayout() {

        setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        addComponent(userFormLayout);
        mainTableLayout = new VerticalLayout();
        addComponent(mainTableLayout);
        mainTableLayout.setVisible(false);
        tableLayout = new HorizontalLayout();
        tableLayout.setSizeFull();

        mainUserInputLayout = new VerticalLayout();
        userFormLayout.addComponent(mainUserInputLayout);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setSpacing(true);
        userFormLayout.addComponent(buttonRow);

        searchButton = new Button("Search");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());
        buttonRow.addComponent(searchButton);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);

        Button printBtn = new Button("Print");
        Button downloadExcelBtn = new Button("Download as Excel");
        downloadExcelBtn.addClickListener((Button.ClickListener) clickEvent -> {
            ExcelExport excelExport = new ExcelExport(detailsTable);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle(reportExcelHeader);
            excelExport.export();
        });
        optionButtonRow.addComponents(printBtn,downloadExcelBtn);
        filterCriteriaText = new Label("");
        filterCriteriaText.addStyleName(ValoTheme.LABEL_H4);
        mainTableLayout.addComponent(optionButtonRow);
        mainTableLayout.addComponent(filterCriteriaText);
        mainTableLayout.addComponent(tableLayout);

        detailsTable = new Table();
        detailsTable.setSelectable(true);
        detailsTable.setMultiSelect(false);
        detailsTable.setSortEnabled(true);
        detailsTable.setColumnCollapsingAllowed(true);
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setPageLength(10);
        detailsTable.setSizeFull();
        detailsTable.setContainerDataSource(generateContainer());
        tableLayout.addComponent(detailsTable);
        setComponentAlignment(mainTableLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(userFormLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_CENTER);
    }

    protected abstract IndexedContainer generateContainer();

    protected abstract void defineStringFields();

    protected abstract void showFilterData();

}
