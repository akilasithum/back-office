package com.back.office.ui;

import com.back.office.entity.BudgetDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import java.util.List;

public class BudgetView extends CommonPageDetails {

    private final String YEAR_LIST = "Year";
    private final String MONTH_LIST = "Month";
    private final String BUDGET_ESTIMATE = "Estimate Budget";
    private final String ESTIMATED_PAX = "Estimated Pax";
    private final String ESTIMATED_FOR = "Estimated Budget For Pax";

    TextField paxestimateText;
    TextField budgetText;
    TextField estimateText;
    ComboBox yearListC;
    ComboBox monthListC;

    FilterGrid<BudgetDetails> budgetListGrid;
    List<BudgetDetails> budgetListDetail;

    public BudgetView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        yearListC = new ComboBox(YEAR_LIST);
        yearListC.setItems("2019","2020","2021","2022","2023","2024","2025");
        yearListC.setRequiredIndicatorVisible(true);
        yearListC.setEmptySelectionAllowed(false);
        yearListC.setSelectedItem("2019");
        yearListC.setSizeFull();
        yearListC.setWidth("70%");
        firstRow.addComponent(yearListC);

        monthListC = new ComboBox(MONTH_LIST);
        monthListC.setItems("January","February","March","April","May","June","July","August","September","October","November","December");
        monthListC.setRequiredIndicatorVisible(true);
        monthListC.setEmptySelectionAllowed(false);
        monthListC.setSelectedItem("January");
        monthListC.setRequiredIndicatorVisible(true);
        monthListC.setSizeFull();
        monthListC.setWidth("70%");
        firstRow.addComponent(monthListC);

        budgetText = new TextField(BUDGET_ESTIMATE);
        budgetText.setDescription(BUDGET_ESTIMATE);
        budgetText.setRequiredIndicatorVisible(true);
        budgetText.setSizeFull();
        budgetText.setWidth("70%");
        firstRow.addComponent(budgetText);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setWidth("66.67%");
        mainUserInputLayout.addComponent(secondRow);
        mainUserInputLayout.setWidth("70%");

        paxestimateText = new TextField(ESTIMATED_PAX);
        paxestimateText.setDescription(ESTIMATED_PAX);
        paxestimateText.setSizeFull();
        paxestimateText.setWidth("70%");
        secondRow.addComponent(paxestimateText);

        estimateText = new TextField(ESTIMATED_FOR);
        estimateText.setDescription(ESTIMATED_FOR);
        estimateText.setRequiredIndicatorVisible(true);
        estimateText.setSizeFull();
        estimateText.setWidth("70%");
        secondRow.addComponent(estimateText);


        budgetListGrid = new FilterGrid<>();
        budgetListGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        budgetListGrid.setSizeFull();
        tableLayout.addComponent(budgetListGrid);
        setDataInGrid();
        GridContextMenu<BudgetDetails> gridMenu = new GridContextMenu<>(budgetListGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        userFormLayout.setWidth("100%");
        mainTableLayout.setWidth("85%");
        headerLayout.setWidth("70%");
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            BudgetDetails aircraftDetail = (BudgetDetails) target;
            boolean success = connection.deleteObjectHBM(aircraftDetail.getestimateId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Budget entry delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                budgetListDetail.remove(target);
                budgetListGrid.setItems(budgetListDetail);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        budgetListDetail = (List<BudgetDetails>)connection.getBudgetDetails(className);
        budgetListGrid.setItems(budgetListDetail);
        budgetListGrid.addColumn(BudgetDetails::getyear).setCaption(YEAR_LIST).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        budgetListGrid.addColumn(BudgetDetails::getmonth).setCaption(MONTH_LIST).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        budgetListGrid.addColumn(BudgetDetails::getbudgetEstimate).setCaption(BUDGET_ESTIMATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        budgetListGrid.addColumn(BudgetDetails::getestimatedPax).setCaption(ESTIMATED_PAX).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        budgetListGrid.addColumn(BudgetDetails::getestimatedBudgetForPax).setCaption(ESTIMATED_FOR).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }


    @Override
    protected void insertDetails() {

        try {
            Float.parseFloat(budgetText.getValue());
            Float.parseFloat(paxestimateText.getValue());
            Float.parseFloat(estimateText.getValue());

            String isValidated = validateFields();
            if(isValidated != null){
                Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
            }
            else{

                String val = idField.getValue();
                int currencyCodeIdVal = (val != null && !val.isEmpty())? Integer.parseInt(val) : 0;


                BudgetDetails details = new BudgetDetails();

                details.setyear(Integer.parseInt(yearListC.getValue().toString()));
                details.setmonth(monthListC.getValue().toString());
                details.setbudgetEstimate(Float.parseFloat(budgetText.getValue()));
                details.setestimatedPax(Integer.parseInt(paxestimateText.getValue()));
                details.setestimatedBudgetForPax(Float.parseFloat(estimateText.getValue()));
                addOrUpdateDetails(details,currencyCodeIdVal);
            }
        }catch(Exception ex) {
            Notification.show("Error","Something wrong with the submitted data. Please check",Notification.Type.WARNING_MESSAGE);
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            BudgetDetails budgetDetails = (BudgetDetails) target;
            yearListC.setValue(budgetDetails.getyear());
            monthListC.setValue(budgetDetails.getmonth());
            budgetText.setValue(String.valueOf(budgetDetails.getbudgetEstimate()));
            paxestimateText.setValue(String.valueOf(budgetDetails.getestimatedPax()));
            estimateText.setValue(String.valueOf(budgetDetails.getestimatedBudgetForPax()));

            idField.setValue(String.valueOf(budgetDetails.getestimateId()));
            addButton.setCaption("Save");
            editObj = budgetDetails;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Estimated Budget";
        this.className = "com.back.office.entity.BudgetDetails";
        this.keyFieldDBName = "year";
    }

    @Override
    protected void updateTable(boolean isEdit, Object detail, int newId) {
        BudgetDetails details = (BudgetDetails) detail;
        if(isEdit){
            int index = budgetListDetail.indexOf(editObj);
            budgetListDetail.remove(editObj);
            budgetListDetail.add(index,details);
        }
        else{
            budgetListDetail.add(details);
        }
        budgetListGrid.setItems(budgetListDetail);
    }

    @Override
    protected TextField getKeyField() {
        return budgetText;
    }
}
