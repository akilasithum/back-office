package com.back.office.ui.setup;

import com.back.office.entity.FACommissionSetup;
import com.back.office.ui.CommonPageDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import java.util.Date;
import java.util.List;

public class FACommissionSetupView extends CommonPageDetails {

    private final String FA_COMMISSION = "FA Commission percentage";
    private final String UPDATED_DATE = "Updated Date";
    private final String CURRENT_VALUE = "Current Value";

    TextField faCommission;
    FilterGrid<FACommissionSetup> faCommissionSetupFilterGrid;
    List<FACommissionSetup> faCommissions;

    public FACommissionSetupView(){
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

        faCommission = new TextField(FA_COMMISSION);
        faCommission.setDescription(FA_COMMISSION);
        faCommission.setRequiredIndicatorVisible(true);
        firstRow.addComponent(faCommission);
        faCommission.addValueChangeListener(valueChangeEvent -> {
            isKeyFieldDirty = true;
        });

        faCommissionSetupFilterGrid = new FilterGrid<>();
        faCommissionSetupFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        faCommissionSetupFilterGrid.setSizeFull();
        tableLayout.addComponent(faCommissionSetupFilterGrid);
        setDataInGrid();

        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("85%");
        headerLayout.setWidth("70%");
    }

    private void setDataInGrid(){
        faCommissions = (List<FACommissionSetup>)connection.getAllValuesNoRecrdStatus(className);
        faCommissionSetupFilterGrid.setItems(faCommissions);
        faCommissionSetupFilterGrid.addColumn(FACommissionSetup::getFaCommission).setCaption(FA_COMMISSION).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        faCommissionSetupFilterGrid.addColumn(bean->BackOfficeUtils.getDateTimeFromDateTime(bean.getUpdatedDateTime())).setCaption(UPDATED_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        faCommissionSetupFilterGrid.addColumn(bean -> isCurrentValue(bean.getRecordStatus())).setCaption(CURRENT_VALUE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }

    private String isCurrentValue(int val){
        if(val == 0) return "Yes";
        else return "no";
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
        }
        else{
            String val = idField.getValue();
            int currencyCodeIdVal = (val != null && !val.isEmpty())? Integer.parseInt(val) : 0;
            FACommissionSetup details = new FACommissionSetup();
            String faCommissionVal = faCommission.getValue();
            if(BackOfficeUtils.isFloat(faCommissionVal)){
                connection.updateFACommissionRecordStatus();
                details.setFaCommission(Float.parseFloat(faCommission.getValue()));
                details.setRecordStatus(0);
                details.setUpdatedDateTime(new Date());
                addOrUpdateDetails(details,currencyCodeIdVal);
            }
            else{
                Notification.show("FA Commission should be a decimal value", Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    @Override
    protected void fillEditDetails(Object target) {

    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = FA_COMMISSION;
        this.pageHeader = "FA Commission Table";
        this.className = "com.back.office.entity.FACommissionSetup";
        this.keyFieldDBName = "faCommisionId";
    }

    @Override
    protected void updateTable(boolean isEdit, Object detail, int newId) {
        faCommissions = (List<FACommissionSetup>)connection.getAllValuesNoRecrdStatus(className);
        faCommissionSetupFilterGrid.setItems(faCommissions);
    }

    @Override
    protected TextField getKeyField() {
        return idField;
    }

    @Override
    protected void deleteItem(Object item) {

    }
}
