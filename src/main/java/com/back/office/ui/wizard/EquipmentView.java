package com.back.office.ui.wizard;

import com.back.office.entity.EquipmentDetails;
import com.back.office.ui.wizard.steps.equipment.EquipmentCommonSteps;
import com.back.office.ui.wizard.steps.equipment.EquipmentDrawerStep;
import com.back.office.ui.wizard.steps.equipment.EquipmentFirstStep;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

public class EquipmentView extends WizardCommonView {

    private final String PACK_TYPE = "Pack Type";
    private final String PACK_DESC = "Pack Description";
    private final String EQUIPMENT_TYPE = "Equipment Type";
    private final String NO_OF_DRAWERS = "No of Drawers";
    private final String NO_OF_SEALS = "No of Seals";
    private final String CART_MANUFACTURER = "Cart Manufacturer";
    private final String CART_WEIGHT = "Cart Weight";
    private final String DRAWER_MANUFACTURER = "Drawer Manufacturer";
    private final String DRAWER_WEIGHT = "Drawer Weight";

    ComboBox equipmentTypeFld;
    TextField packTypeFld;
    TextField packDescFld;
    TextField cartManufacturerFld;
    TextField cartWeightFld;
    TextField noOfDrawersFld;
    TextField drawerManufacturerFld;
    TextField drawerWeightFld;
    TextField noOfSeals;

    FilterGrid<EquipmentDetails> equipmentDetailsGrid;
    List<EquipmentDetails> equipmentDetailsList;

    public EquipmentView(){
        super();
    }

    @Override
    protected void createMainLayout(){
        super.createMainLayout();
        equipmentDetailsGrid = new FilterGrid<>();
        equipmentDetailsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        equipmentDetailsGrid.setSizeFull();
        tableLayout.addComponent(equipmentDetailsGrid);
        setDataInGrid();
        equipmentDetailsGrid.addItemClickListener(itemClick -> {
            openViewEditWindow(itemClick.getItem());
        });
    }

    private void openViewEditWindow(EquipmentDetails item) {
        Window detailsWindow = new Window("View Item");
        FormLayout formLayout = new FormLayout();
        editObj = item;

        TextField idFld = new TextField();
        idFld.setValue(String.valueOf(item.getEquipmentId()));
        formLayout.addComponents(idFld);
        idFld.setVisible(false);

        equipmentTypeFld = new ComboBox(EQUIPMENT_TYPE);
        equipmentTypeFld.setItems("Half Cart","Full Cart","Containers");
        equipmentTypeFld.setEmptySelectionAllowed(false);
        equipmentTypeFld.setRequiredIndicatorVisible(true);
        equipmentTypeFld.setValue(item.getEquipmentType());
        formLayout.addComponent(equipmentTypeFld);

        cartManufacturerFld = new TextField("Equipment Manufacturer");
        cartManufacturerFld.setValue(item.getCartManufacturer() == null ? "" : item.getCartManufacturer());
        formLayout.addComponent(cartManufacturerFld);

        cartWeightFld = new TextField("Equipment Weight");
        cartWeightFld.setValue(String.valueOf(item.getCartWeight()));
        formLayout.addComponent(cartWeightFld);

        formLayout.addComponent(new Label("<hr />", ContentMode.HTML));

        noOfDrawersFld = new TextField(NO_OF_DRAWERS);
        noOfDrawersFld.setRequiredIndicatorVisible(true);
        noOfDrawersFld.setValue(String.valueOf(item.getNoOfDrawers()));
        formLayout.addComponent(noOfDrawersFld);

        drawerManufacturerFld = new TextField("Drawer Manufacturer");
        drawerManufacturerFld.setValue(item.getDrawerManufacturer() == null ? "" : item.getDrawerManufacturer());
        formLayout.addComponent(drawerManufacturerFld);

        drawerWeightFld = new TextField("Drawer Weight");
        drawerWeightFld.setValue(String.valueOf(item.getDraweWeight()));
        formLayout.addComponent(drawerWeightFld);

        formLayout.addComponent(new Label("<hr />", ContentMode.HTML));

        packTypeFld = new TextField(PACK_TYPE);
        packTypeFld.setValue(item.getPackType());
        formLayout.addComponent(packTypeFld);

        packDescFld = new TextField(PACK_DESC);
        packDescFld.setValue(item.getPackDescription());
        formLayout.addComponent(packDescFld);

        noOfSeals = new TextField(NO_OF_SEALS);
        noOfSeals.setValue(String.valueOf(item.getNoOfSeals()));
        formLayout.addComponent(noOfSeals);

        detailsWindow.setWidth("40%");
        detailsWindow.setHeight(500,Unit.PIXELS);
        detailsWindow.center();
        formLayout.setMargin(true);

        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(editButton,deleteButton);

        editButton.addClickListener(clickEvent -> {

            if(editButton.getCaption().equals("Edit")){
                enableDisableAllComponents(formLayout,true);
                editButton.setCaption("Save");
            }
            else{
                try {
                    EquipmentDetails cart = new EquipmentDetails();
                    cart.setEquipmentType(equipmentTypeFld.getValue().toString());
                    cart.setCartManufacturer(cartManufacturerFld.getValue());
                    cart.setCartWeight(Float.parseFloat(cartWeightFld.getValue()));
                    cart.setNoOfDrawers(Integer.parseInt(noOfDrawersFld.getValue()));
                    cart.setDrawerManufacturer(drawerManufacturerFld.getValue());
                    cart.setDraweWeight(Float.parseFloat(drawerWeightFld.getValue()));
                    cart.setPackType(packTypeFld.getValue());
                    cart.setPackDescription(packDescFld.getValue());
                    cart.setNoOfSeals(Integer.parseInt(noOfSeals.getValue()));
                    connection.updateRecordStatus(Integer.parseInt(idFld.getValue()),
                            "com.back.office.entity.EquipmentDetails");
                    connection.insertObjectHBM(cart);
                    updateTable(true,cart);
                    detailsWindow.close();
                }
                catch (Exception e){
                    Notification.show("Some fields are not specified or contain wrong values. Please check.", Notification.Type.WARNING_MESSAGE);
                }
            }

        });

        deleteButton.addClickListener(clickEvent -> {
            ConfirmDialog.show(getUI(), "Delete", "Are you sure you want to delete this item?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                connection.deleteObjectHBM(item);
                                detailsWindow.close();
                                equipmentDetailsList.remove(editObj);
                                equipmentDetailsGrid.setItems(equipmentDetailsList);
                            }
                        }
                    });
        });

        formLayout.addComponents(btnLayout);
        detailsWindow.setContent(formLayout);
        enableDisableAllComponents(formLayout,false);
        getUI().addWindow(detailsWindow);
    }

    private void setDataInGrid(){
        equipmentDetailsList = (List<EquipmentDetails>)connection.getAllValues(className);
        equipmentDetailsGrid.setItems(equipmentDetailsList);
        equipmentDetailsGrid.addColumn(EquipmentDetails::getEquipmentType).setCaption(EQUIPMENT_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getPackType).setCaption(PACK_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getPackDescription).setCaption(PACK_DESC).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getNoOfDrawers).setCaption(NO_OF_DRAWERS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getNoOfSeals).setCaption(NO_OF_SEALS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getCartManufacturer).setCaption(CART_MANUFACTURER).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getCartWeight).setCaption(CART_WEIGHT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getDrawerManufacturer).setCaption(DRAWER_MANUFACTURER).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getDraweWeight).setCaption(DRAWER_WEIGHT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }

    @Override
    protected void defineStringFields() {
        headerName = "Equipment Types";
        objectName = "equipment";
        wizardName = "Create Equipment";
        this.className = "com.back.office.entity.EquipmentDetails";
    }

    @Override
    protected void createItemWizard() {
        super.createWizard();
        wizard.addStep(new EquipmentFirstStep());
        wizard.addStep(new EquipmentDrawerStep());
        wizard.addStep(new EquipmentCommonSteps());
        getUI().getUI().addWindow(window);
    }

    @Override
    protected void updateTable(boolean isEdit, Object object) {
        EquipmentDetails equipment = (EquipmentDetails) object;
        if(isEdit){
            int index = equipmentDetailsList.indexOf(editObj);
            equipmentDetailsList.remove(editObj);
            equipmentDetailsList.add(index,equipment);
        }
        else{
            equipmentDetailsList.add(equipment);
        }
        equipmentDetailsGrid.setItems(equipmentDetailsList);
    }
}
