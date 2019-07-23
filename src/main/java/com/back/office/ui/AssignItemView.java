package com.back.office.ui;

import com.back.office.db.DBConnection;
import com.back.office.entity.*;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.Query;
import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssignItemView extends UserEntryView implements View {

    ComboBox packTypeComboBox;
    ComboBox drawerComboBox;
    ComboBox itemNameComboBox;
    ComboBox itemCodeComboBox;
    TextField quantityFld;
    TextField cartItemId;

    Button addButton;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout tableLayout;
    DBConnection connection;
    protected Object editObj;
    boolean isOneDropDownSelected = false;

    private final String PACK_TYPE = "Pack Type";
    private final String DRAWER_NAME = "Drawer";
    private final String ITEM_NAME = "Item Name";
    private final String ITEM_CODE = "Item Code";
    private final String QUANTITY = "Quantity";
    private final String EQUIPMENT_DETAILS_CLASS_NAME = "com.back.office.entity.EquipmentDetails";

    FilterGrid<CartItems> cartItemsGrid;
    List<CartItems> cartItems;
    Map<String,EquipmentDetails> equipmentDetailsMap = new HashMap<>();
    List<ItemDetails> itemDetailsList;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public AssignItemView(){
        super();
        connection = DBConnection.getInstance();
        createMainLayout();
    }

    private void createMainLayout(){

        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        headerLayout.setWidth("55%");
        headerLayout.setMargin(Constants.noMargin);
        userFormLayout = new VerticalLayout();
        addComponent(userFormLayout);
        userFormLayout.setMargin(Constants.bottomMarginInfo);
        tableLayout = new VerticalLayout();
        tableLayout.setSizeFull();
        addComponent(tableLayout);
        tableLayout.setMargin(Constants.noMargin);
        Label h1 = new Label("Assign Items");
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth(66.67f,Unit.PERCENTAGE);
        firstRow.setMargin(Constants.noMargin);
        userFormLayout.addComponent(firstRow);

        packTypeComboBox = new ComboBox(PACK_TYPE);
        packTypeComboBox.setItems(getPackTypesObjects());
        packTypeComboBox.setEmptySelectionAllowed(false);
        packTypeComboBox.setRequiredIndicatorVisible(true);
        firstRow.addComponent(packTypeComboBox);
        packTypeComboBox.addValueChangeListener(valueChangeEvent -> {
            if(packTypeComboBox.getValue() != null){
                fillDrawerNames((EquipmentDetails)packTypeComboBox.getValue());
                fillItemNames((EquipmentDetails)packTypeComboBox.getValue());
            }
        });


        drawerComboBox = new ComboBox(DRAWER_NAME);
        drawerComboBox.setEmptySelectionAllowed(false);
        drawerComboBox.setRequiredIndicatorVisible(true);
        firstRow.addComponent(drawerComboBox);
        drawerComboBox.addValueChangeListener(valueChangeEvent -> {
            if(packTypeComboBox.getValue() != null && drawerComboBox.getValue() != null){
                setDataInGrid((EquipmentDetails)packTypeComboBox.getValue(),drawerComboBox.getValue().toString());
            }
        });

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setMargin(Constants.noMargin);
        secondRow.setSizeFull();

        itemCodeComboBox = new ComboBox(ITEM_CODE);
        itemCodeComboBox.setEmptySelectionAllowed(false);
        itemCodeComboBox.setRequiredIndicatorVisible(true);
        secondRow.addComponent(itemCodeComboBox);
        itemCodeComboBox.addValueChangeListener((HasValue.ValueChangeListener) valueChangeEvent -> {
            if(!isOneDropDownSelected) {
                isOneDropDownSelected = true;
                itemNameComboBox.setValue(getItemNameFromItemCode(String.valueOf(valueChangeEvent.getValue())));
            }
            else {
                isOneDropDownSelected = false;
            }
        });

        itemNameComboBox = new ComboBox(ITEM_NAME);
        itemNameComboBox.setEmptySelectionAllowed(false);
        itemNameComboBox.setRequiredIndicatorVisible(true);
        secondRow.addComponent(itemNameComboBox);
        itemNameComboBox.addValueChangeListener(valueChangeEvent -> {
            if(!isOneDropDownSelected) {
                isOneDropDownSelected = true;
                if (valueChangeEvent.getValue() != null) {
                    ItemDetails item = (ItemDetails) valueChangeEvent.getValue();
                    itemCodeComboBox.setValue(item.getItemCode());
                }
            }
            else {
                isOneDropDownSelected = false;
            }
        });

        quantityFld = new TextField(QUANTITY);
        quantityFld.setDescription(QUANTITY);
        quantityFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(quantityFld);

        cartItemId = new TextField("Cart Item Id");
        cartItemId.setVisible(false);
        secondRow.addComponent(cartItemId);

        userFormLayout.addComponent(secondRow);
        userFormLayout.setWidth("50%");

        Button showAll = new Button("Show all cart items");
        showAll.addClickListener((Button.ClickListener) event -> {
            setDataInGrid(null,null);
        });

        addButton = new Button("Add");
        addButton.setStyleName("add-button-margin");
        addButton.addClickListener((Button.ClickListener) clickEvent -> insertCartItem());
        HorizontalLayout rowFilter = new HorizontalLayout();
        rowFilter.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        rowFilter.setSpacing(true);
        rowFilter.addComponent(addButton);
        rowFilter.addComponent(showAll);
        rowFilter.setMargin(Constants.noMargin);
        tableLayout.addComponent(rowFilter);

        cartItemsGrid = new FilterGrid<>();
        cartItemsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        cartItemsGrid.setSizeFull();
        tableLayout.addComponent(cartItemsGrid);
        cartItemsGrid.addColumn(CartItems::getItemName).setCaption(ITEM_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        cartItemsGrid.addColumn(CartItems::getPackType).setCaption(PACK_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        cartItemsGrid.addColumn(CartItems::getDrawerName).setCaption(DRAWER_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        cartItemsGrid.addColumn(CartItems::getQuantity).setCaption(QUANTITY).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        GridContextMenu<CartItems> gridMenu = new GridContextMenu<>(cartItemsGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);


        tableLayout.setWidth("75%");
        setComponentAlignment(tableLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(userFormLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_LEFT);
    }
    protected TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;
    }

    private void updateGridBodyMenu(GridContextMenu.GridContextMenuOpenListener.GridContextMenuOpenEvent<?> event) {
        event.getContextMenu().removeItems();
        if (event.getItem() != null) {
            event.getContextMenu().addItem("Edit item", VaadinIcons.EDIT, selectedItem -> {
                fillEditDetails(event.getItem());
            });
            event.getContextMenu().addItem("Delete item", VaadinIcons.FOLDER_REMOVE, selectedItem -> {
                deleteItemConfirmation(event.getItem());
            });
        }
    }

    private void deleteItemConfirmation(Object target){
        ConfirmDialog.show(getUI(), "Delete row", "Are you sure you want to delete this row?",
                "Yes", "No", new ConfirmDialog.Listener() {
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            deleteItem(target);
                        }
                    }
                });
    }

    private void deleteItem(Object target) {
        if (target != null) {
            CartItems cartItem = (CartItems) target;

            boolean success = connection.deleteObjectHBM(cartItem.getCartItemId(),
                    "com.back.office.entity.CartItems");
            if (success) {
                BackOfficeUtils.showNotification("Success", "Item delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                cartItems.remove(target);
                cartItemsGrid.setItems(cartItems);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }

        }
    }

    private void fillEditDetails(Object target){
        if(target != null) {
            CartItems cartItem = (CartItems) target;
            packTypeComboBox.setValue( equipmentDetailsMap.get(cartItem.getPackType()));
            drawerComboBox.setValue(cartItem.getDrawerName());
            List<ItemDetails> items = (List<ItemDetails>)itemNameComboBox.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
            for(ItemDetails itemDetail : items){
                if(itemDetail.getItemName().equals(cartItem.getItemName())){
                    itemNameComboBox.setValue(itemDetail);
                }
            }
            quantityFld.setValue(String.valueOf(cartItem.getQuantity()));
            cartItemId.setValue(String.valueOf(cartItem.getCartItemId()));
            addButton.setCaption("Save");
            editObj = cartItem;
        }
    }

    private void setDataInGrid(EquipmentDetails equipmentDetails,String drawer){
        cartItems = new ArrayList<>();
        cartItemsGrid.setItems(cartItems);
        if(equipmentDetails == null && drawer == null){
            cartItems = (List<CartItems>)connection.getAllValues("com.back.office.entity.CartItems");
        }
        else {
            cartItems = connection.getItemsFromServiceType(equipmentDetails.getPackType(), drawer);
        }
        cartItemsGrid.setItems(cartItems);
    }



    private void insertCartItem(){

        int quantityInt = 0;
        Object packType = packTypeComboBox.getValue();
        if(packType == null){
            Notification.show("Enter Pack Type", Notification.Type.WARNING_MESSAGE);
            packTypeComboBox.focus();
            return;
        }
        Object drawerName = drawerComboBox.getValue();
        if(drawerName == null){
            Notification.show("Enter drawer", Notification.Type.WARNING_MESSAGE);
            drawerComboBox.focus();
            return;
        }
        Object itemName = itemNameComboBox.getValue();
        if(itemName == null){
            Notification.show("Enter Item Name", Notification.Type.WARNING_MESSAGE);
            itemNameComboBox.focus();
            return;
        }
        String quantity = quantityFld.getValue();
        if(quantity == null || quantity.isEmpty()){
            Notification.show("Enter Quantity", Notification.Type.WARNING_MESSAGE);
            quantityFld.focus();
            return;
        }
        else{
            if(!BackOfficeUtils.isInteger(quantity)) {
                Notification.show("Quantity should be a number", Notification.Type.WARNING_MESSAGE);
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
        cartItem.setItemId(((ItemDetails)itemName).getItemCode());
        cartItem.setItemName(itemName.toString());
        cartItem.setQuantity(quantityInt);
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(cartItem);
            if (newId != 0) {
                BackOfficeUtils.showNotification("Success","Cart item added successfully",VaadinIcons.CHECK_CIRCLE_O);
                updateCartItems(false,cartItem);
                resetFields();
            } else {
                BackOfficeUtils.showNotification("Error","Something wrong, please try again",VaadinIcons.CLOSE);
            }
        }
        else{
            connection.updateObjectHBM(cartItem);
            BackOfficeUtils.showNotification("Success","Cart item updated successfully",VaadinIcons.CHECK_CIRCLE_O);
            updateCartItems(true,cartItem);
            addButton.setCaption("Add");
            resetFields();
        }

    }

    private void updateCartItems(boolean isEdit , CartItems details){
        if(isEdit){
            int index = cartItems.indexOf(editObj);
            cartItems.remove(editObj);
            cartItems.add(index,details);
        }
        else{
            cartItems.add(details);
        }
        cartItemsGrid.setItems(cartItems);
    }

    private List<EquipmentDetails> getPackTypesObjects(){
         List<EquipmentDetails> details = (List<EquipmentDetails>)connection.getAllValues(EQUIPMENT_DETAILS_CLASS_NAME);
         for(EquipmentDetails equipmentDetails : details){
             equipmentDetailsMap.put(equipmentDetails.getPackType(),equipmentDetails);
         }
         return details;
    }

    private void fillDrawerNames(EquipmentDetails equipmentDetails){
        drawerComboBox.setItems("");
        String prefix;
        if(equipmentDetails.getEquipmentType().equals("Containers")){
            prefix = "Container";
        }
        else{
            prefix = "Drawer";
        }
        List<String> list = new ArrayList<>();
        for(int i = 1; i<=equipmentDetails.getNoOfDrawers();i++){
            list.add(prefix + "-" + i);
        }
        drawerComboBox.setItems(list);
    }

    private void fillItemNames(EquipmentDetails equipmentDetails){
        itemNameComboBox.setItems("");
        itemCodeComboBox.setItems("");
        //String kitCode = equipmentDetails.getKitCode();
        //List<KitCodes> kitCodesList = connection.getServiceTypeFromKitCode(kitCode);
        //if(kitCodesList != null && !kitCodesList.isEmpty()){
            //itemDetailsList = connection.getItemsFromServiceType(kitCodesList.get(0).getServiceType());
            itemDetailsList = connection.getAllItems();
            itemNameComboBox.setItems(itemDetailsList);
            List<String> itemCodes = new ArrayList<>();
            for(ItemDetails item : itemDetailsList){
                itemCodes.add(item.getItemCode());
            }
            itemCodeComboBox.setItems(itemCodes);
       // }
    }

    private ItemDetails getItemNameFromItemCode(String itemCode){
        for(ItemDetails details : itemDetailsList){
            if(details.getItemCode().equals(itemCode)) return details;
        }
        return null;
    }

    private void resetFields(){
        itemNameComboBox.clear();
        itemCodeComboBox.clear();
        quantityFld.clear();
    }
}
