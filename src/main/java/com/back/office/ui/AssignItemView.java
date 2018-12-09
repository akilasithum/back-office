package com.back.office.ui;

import com.back.office.db.DBConnection;
import com.back.office.entity.*;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tepi.filtertable.FilterTable;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

public class AssignItemView extends VerticalLayout implements View {

    ComboBox packTypeComboBox;
    ComboBox drawerComboBox;
    ComboBox itemNameComboBox;
    TextField quantityFld;
    TextField cartItemId;
    Button addButton;
    FilterTable cartItemsTable;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout tableLayout;
    DBConnection connection;

    private final String PACK_TYPE = "Pack Type";
    private final String DRAWER_NAME = "Drawer";
    private final String ITEM_NAME = "Item Name";
    private final String QUANTITY = "Quantity";
    private final String EQUIPMENT_DETAILS_CLASS_NAME = "com.back.office.entity.EquipmentDetails";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public AssignItemView(){
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    private void createMainLayout(){

        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        headerLayout.setWidth("55%");
        userFormLayout = new VerticalLayout();
        addComponent(userFormLayout);
        userFormLayout.setStyleName("layout-with-border");
        tableLayout = new VerticalLayout();
        tableLayout.setSizeFull();
        addComponent(tableLayout);



        setSpacing(true);
        Label h1 = new Label("Assign Items");
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        userFormLayout.addComponent(firstRow);

        packTypeComboBox = new ComboBox(PACK_TYPE);
        packTypeComboBox.addItems(getPackTypesObjects());
        packTypeComboBox.setNullSelectionAllowed(false);
        packTypeComboBox.setRequired(true);
        firstRow.addComponent(packTypeComboBox);
        packTypeComboBox.addValueChangeListener((Property.ValueChangeListener) valueChangeEvent -> {
            if(packTypeComboBox.getValue() != null){
                fillDrawerNames((EquipmentDetails)packTypeComboBox.getValue());
                fillItemNames((EquipmentDetails)packTypeComboBox.getValue());
            }
        });


        drawerComboBox = new ComboBox(DRAWER_NAME);
        drawerComboBox.setNullSelectionAllowed(false);
        drawerComboBox.setRequired(true);
        firstRow.addComponent(drawerComboBox);
        drawerComboBox.addValueChangeListener((Property.ValueChangeListener) valueChangeEvent -> {
            if(packTypeComboBox.getValue() != null && drawerComboBox.getValue() != null){
                loadTable((EquipmentDetails)packTypeComboBox.getValue(),drawerComboBox.getValue().toString());
            }
        });

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        MarginInfo marginInfo = new MarginInfo(true,false,true,false);
        secondRow.setMargin(marginInfo);
        secondRow.setSizeFull();

        itemNameComboBox = new ComboBox(ITEM_NAME);
        itemNameComboBox.setNullSelectionAllowed(false);
        itemNameComboBox.setRequired(true);
        secondRow.addComponent(itemNameComboBox);

        quantityFld = new TextField(QUANTITY);
        quantityFld.setInputPrompt(QUANTITY);
        quantityFld.setRequired(true);
        secondRow.addComponent(quantityFld);

        addButton = new Button("Add");
        addButton.setStyleName("add-button-margin");
        addButton.addClickListener((Button.ClickListener) clickEvent -> insertCartItem());
        secondRow.addComponent(addButton);

        cartItemId = new TextField("Cart Item Id");
        cartItemId.setVisible(false);
        secondRow.addComponent(cartItemId);

        userFormLayout.addComponent(secondRow);
        userFormLayout.setWidth("50%");

        cartItemsTable = new FilterTable();
        cartItemsTable.setSelectable(true);
        cartItemsTable.setFilterBarVisible(true);
        cartItemsTable.setMultiSelect(false);
        cartItemsTable.setSortEnabled(true);
        cartItemsTable.setColumnCollapsingAllowed(true);
        cartItemsTable.setColumnReorderingAllowed(true);
        cartItemsTable.setPageLength(10);

        IndexedContainer normalContainer = generateContainer();
        cartItemsTable.setContainerDataSource(normalContainer);
        cartItemsTable.setSizeFull();
        cartItemsTable.addActionHandler(actionHandler);

        HorizontalLayout rowFilter = new HorizontalLayout();
        rowFilter.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        rowFilter.setSpacing(true);

        TextField filterFiled = new TextField();
        filterFiled.setInputPrompt("Filter by name");
        rowFilter.addComponent(filterFiled);

        Button filterBtn = new Button("Filter");
        filterBtn.addClickListener((Button.ClickListener) clickEvent -> {
            IndexedContainer container = (IndexedContainer)cartItemsTable.getContainerDataSource();
            if(filterFiled.getValue() == null || filterFiled.getValue().isEmpty()) {
                container.removeContainerFilters(ITEM_NAME);
            }
            else{
                container.addContainerFilter(ITEM_NAME, filterFiled.getValue(), true, false);
            }
        });
        Button showAll = new Button("Show all cart items");
        showAll.addClickListener((Button.ClickListener) event -> {
            loadTable(null,null);
        });
        rowFilter.addComponent(filterBtn);
        rowFilter.addComponent(showAll);
        tableLayout.addComponent(rowFilter);

        tableLayout.addComponent(cartItemsTable);
        tableLayout.setWidth("55%");
        tableLayout.setStyleName("layout-with-border");
        setComponentAlignment(tableLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(userFormLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_CENTER);
    }

    Action.Handler actionHandler = new Action.Handler() {
        private final Action editItem = new Action("Edit Cart Item" , FontAwesome.EDIT);
        private final Action deleteItem = new Action("Delete Cart Item" , FontAwesome.REMOVE);
        private final Action[] ACTIONS = new Action[] {editItem, deleteItem};

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if(action.getCaption().equals("Edit Cart Item")){
                fillEditDetails(target);
            }
            else if(action.getCaption().equals("Delete Cart Item")){
                ConfirmDialog.show(getUI(), "Delete Cart Item", "Are you sure you want to delete Cart Item?",
                        "Yes", "No", (ConfirmDialog.Listener) dialog -> {
                            if(dialog.isConfirmed()){
                                deleteItem(target);
                            }
                        });
            }
        }

        @Override
        public Action[] getActions(Object target, Object sender) {
            return ACTIONS;
        }
    };

    private void deleteItem(Object target){
        if(target != null){
            boolean success = connection.deleteObjectHBM(Integer.parseInt(target.toString()),EQUIPMENT_DETAILS_CLASS_NAME);
            if(success){
                Notification.show("Cart item delete successfully");
                IndexedContainer container = (IndexedContainer) cartItemsTable.getContainerDataSource();
                container.removeItem(target);
            }
            else {
                Notification.show("Something wrong, please try again");
            }
        }
    }

    private void fillEditDetails(Object target){
        if(target != null) {
            IndexedContainer container = (IndexedContainer) cartItemsTable.getContainerDataSource();
            Item item = container.getItem(target);
            packTypeComboBox.setValue(item.getItemProperty(PACK_TYPE).getValue().toString());
            drawerComboBox.setValue(item.getItemProperty(DRAWER_NAME).getValue());
            List<ItemDetails> items = (List<ItemDetails>)itemNameComboBox.getItemIds();
            for(ItemDetails itemDetail : items){
                if(itemDetail.getItemName().equals(item.getItemProperty(ITEM_NAME).getValue())){
                    itemNameComboBox.setValue(itemDetail);
                }
            }
            //itemNameComboBox.setValue(item.getItemProperty(ITEM_NAME).getValue());
            quantityFld.setValue(item.getItemProperty(QUANTITY).getValue().toString());
            cartItemId.setValue(target.toString());
            addButton.setCaption("Edit");
        }
    }

    private IndexedContainer generateContainer(){
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(ITEM_NAME, String.class, null);
        container.addContainerProperty(PACK_TYPE, String.class, null);
        container.addContainerProperty(DRAWER_NAME, String.class, null);
        container.addContainerProperty(QUANTITY, Integer.class, null);
        return container;
    }

    private void loadTable(EquipmentDetails equipmentDetails,String drawer){
        IndexedContainer container = (IndexedContainer) cartItemsTable.getContainerDataSource();
        container.removeAllItems();
        List<CartItems> cartItems;
        if(equipmentDetails == null && drawer == null){
            cartItems = (List<CartItems>)connection.getAllValues("com.back.office.entity.CartItems");
        }
        else {
            cartItems = connection.getItemsFromServiceType(equipmentDetails.getPackType(), drawer);
        }
        for(CartItems details : cartItems){
            Item item = container.addItem(details.getCartItemId());
            item.getItemProperty(ITEM_NAME).setValue(details.getItemName());
            item.getItemProperty(PACK_TYPE).setValue(details.getPackType());
            item.getItemProperty(DRAWER_NAME).setValue(details.getDrawerName());
            item.getItemProperty(QUANTITY).setValue(details.getQuantity());
        }
    }

    private void insertCartItem(){

        int quantityInt = 0;
        Object packType = packTypeComboBox.getValue();
        if(packType == null){
            Notification.show("Enter Pack Type");
            packTypeComboBox.focus();
            return;
        }
        Object drawerName = drawerComboBox.getValue();
        if(drawerName == null){
            Notification.show("Enter drawer");
            drawerComboBox.focus();
            return;
        }
        Object itemName = itemNameComboBox.getValue();
        if(itemName == null){
            Notification.show("Enter Item Name");
            itemNameComboBox.focus();
            return;
        }
        String quantity = quantityFld.getValue();
        if(quantity == null || quantity.isEmpty()){
            Notification.show("Enter Quantity");
            quantityFld.focus();
            return;
        }
        else{
            if(!BackOfficeUtils.isInteger(quantity)) {
                Notification.show("Quantity should be a number");
                quantityFld.focus();
                return;
            }
            else{
                quantityInt = Integer.parseInt(quantity);
            }
        }

        String val = cartItemId.getValue();
        int cartITemIdVal = (val != null && !val.isEmpty())? Integer.parseInt(val) : 0;
        CartItems cartItem = new CartItems();
        cartItem.setCartItemId(cartITemIdVal);
        cartItem.setPackType(packType.toString());
        cartItem.setDrawerName(drawerName.toString());
        cartItem.setItemId(((ItemDetails)itemName).getItemId());
        cartItem.setItemName(itemName.toString());
        cartItem.setQuantity(quantityInt);
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(cartItem);
            if (newId != 0) {
                Notification.show("Cart item added successfully");
                updateCartItems(false,cartItem,newId);
                resetFields();
            } else {
                Notification.show("Something wrong, please try again");
            }
        }
        else{
            connection.updateObjectHBM(cartItem);
            Notification.show("Cart item updated successfully");
            updateCartItems(true,cartItem,0);
            addButton.setCaption("Add");
            resetFields();
        }

    }

    private void updateCartItems(boolean isEdit , CartItems cartItems, int newId){
        IndexedContainer container = (IndexedContainer) cartItemsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(cartItems.getCartItemId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(PACK_TYPE).setValue(cartItems.getPackType());
        item.getItemProperty(DRAWER_NAME).setValue(cartItems.getDrawerName());
        item.getItemProperty(ITEM_NAME).setValue(cartItems.getItemName());
        item.getItemProperty(QUANTITY).setValue(cartItems.getQuantity());
    }

    private List<EquipmentDetails> getPackTypesObjects(){
        return (List<EquipmentDetails>)connection.getAllValues(EQUIPMENT_DETAILS_CLASS_NAME);
    }

    private void fillDrawerNames(EquipmentDetails equipmentDetails){
        drawerComboBox.removeAllItems();
        String prefix;
        if(equipmentDetails.getEquipmentType().equals("Containers")){
            prefix = "Container";
        }
        else{
            prefix = "Drawer";
        }
        for(int i = 1; i<=equipmentDetails.getNoOfDrawers();i++){
            drawerComboBox.addItem(prefix + "-" + i);
        }
    }

    private void fillItemNames(EquipmentDetails equipmentDetails){
        itemNameComboBox.removeAllItems();
        String kitCode = equipmentDetails.getKitCode();
        List<KitCodes> kitCodesList = connection.getServiceTypeFromKitCode(kitCode);
        if(kitCodesList != null && !kitCodesList.isEmpty()){
            List<ItemDetails> items = connection.getItemsFromServiceType(kitCodesList.get(0).getServiceType());
            itemNameComboBox.addItems(items);
        }
    }

    private void resetFields(){
        itemNameComboBox.clear();
        quantityFld.clear();
    }
}
