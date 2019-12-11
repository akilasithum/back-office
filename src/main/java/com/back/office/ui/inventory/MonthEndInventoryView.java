package com.back.office.ui.inventory;

import com.back.office.db.DBConnection;
import com.back.office.entity.*;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.Constants;
import com.back.office.utils.UserNotification;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;

import java.time.LocalDate;
import java.util.*;
import java.text.DateFormatSymbols;

public class MonthEndInventoryView extends UserEntryView implements View {

    TextField baseStationFld;
    DateField monthFld;
    TextField userFld;
    ComboBox typeCombo;
    protected VerticalLayout headerLayout;
    protected VerticalLayout userFormLayout;
    protected DBConnection connection;
    protected HorizontalLayout tableLayout;
    private Grid<MonthEndInventory> monthEndInventoryGrid;
    List<MonthEndInventory> monthEndInventoryList;
    Window inventoryWindow;

    public MonthEndInventoryView() {
        super();
        connection = DBConnection.getInstance();
        createLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userNameObj = UI.getCurrent().getSession().getAttribute("userName");
        if (userNameObj == null || userNameObj.toString().isEmpty()) {
            getUI().getNavigator().navigateTo("login");
        }
    }

    private void createLayout() {

        inventoryWindow = new Window("View Inventory");
        inventoryWindow.setWidth("600px");

        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        Label h1 = new Label("Month End Inventory");
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);

        baseStationFld = new TextField("Base Station");
        baseStationFld.setSizeFull();
        baseStationFld.setValue("YYZ");
        baseStationFld.setEnabled(false);

        monthFld = new DateField("Date");
        monthFld.setSizeFull();
        monthFld.setDescription("Select any date of the required month");
        monthFld.setStyleName("datePickerStyle");

        userFld = new TextField("User Name");
        userFld.setSizeFull();
        Object userNameObj = UI.getCurrent().getSession().getAttribute("userName");
        userFld.setValue(String.valueOf(userNameObj));
        userFld.setEnabled(false);

        typeCombo = new ComboBox("Type");
        typeCombo.setSizeFull();
        typeCombo.setItems(Arrays.asList("Shelf", "Cart"));

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("80%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");
        addComponent(firstRow);
        firstRow.addComponents(baseStationFld, monthFld, userFld, typeCombo);
        firstRow.setMargin(true);

        VerticalLayout shelfLayout = new VerticalLayout();
        shelfLayout.setCaption("Shelf");
        shelfLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        shelfLayout.setSpacing(true);
        shelfLayout.setSizeFull();
        shelfLayout.setWidth("80%");
        shelfLayout.setMargin(Constants.noMargin);
        shelfLayout.addStyleName("report-filter-panel");
        TextField shelfNoFld = new TextField("Shelf No");
        shelfLayout.addComponent(shelfNoFld);
        HorizontalLayout shelfMainLayout = new HorizontalLayout();
        shelfMainLayout.setSizeFull();
        shelfLayout.addComponent(shelfMainLayout);
        shelfMainLayout.setWidth("90%");
        addComponent(shelfLayout);

        ComboBox itemNoCombo = new ComboBox("Item No");
        List<ItemDetails> itemsDetail = connection.getItemDetails("detaSelectNumber", "*");
        List<String> arrayListDetailNumber = new ArrayList<>();
        Map<String,String> itemNoDescMap = new HashMap<>();
        for (ItemDetails item : itemsDetail) {
            arrayListDetailNumber.add(item.getItemCode());
            itemNoDescMap.put(item.getItemCode(),item.getItemName());
        }
        itemNoCombo.setItems(arrayListDetailNumber);
        itemNoCombo.setSizeFull();


        TextField itemDescFld = new TextField("Item Description");
        itemDescFld.setSizeFull();

        itemNoCombo.addSelectionListener((event->itemDescFld.setValue(itemNoDescMap.get(itemNoCombo.getValue()))));

        TextField qtyFld = new TextField("Qty");
        qtyFld.setSizeFull();

        HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setStyleName("searchButton");
        buttonRow.setSpacing(true);

        Button searchButton = new Button("Add");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> insertInventoryItem("Shelf",
                String.valueOf(itemNoCombo.getValue()), itemDescFld.getValue(), qtyFld.getValue(),shelfNoFld.getValue()));
        buttonRow.addComponent(searchButton);

        shelfMainLayout.addComponents(itemNoCombo, itemDescFld, qtyFld, buttonRow);

        VerticalLayout cartLayout = new VerticalLayout();
        cartLayout.setCaption("Cart");
        cartLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        cartLayout.setSpacing(true);
        cartLayout.setSizeFull();
        cartLayout.setWidth("80%");
        cartLayout.setMargin(Constants.noMargin);
        cartLayout.addStyleName("report-filter-panel");
        TextField cartNoFld = new TextField("Cart No");
        cartLayout.addComponent(cartNoFld);
        HorizontalLayout cartMainLayout = new HorizontalLayout();
        cartMainLayout.setSizeFull();
        cartLayout.addComponent(cartMainLayout);
        cartMainLayout.setWidth("90%");
        addComponent(cartLayout);

        ComboBox itemNoComboCart = new ComboBox("Item No");
        itemNoComboCart.setItems(arrayListDetailNumber);
        itemNoComboCart.setSizeFull();

        TextField itemDescFldCart = new TextField("Item Description");
        itemDescFldCart.setSizeFull();

        itemNoComboCart.addSelectionListener((event->itemDescFldCart.setValue(itemNoDescMap.get(itemNoComboCart.getValue()))));

        TextField qtyFldCart = new TextField("Qty");
        qtyFldCart.setSizeFull();

        HorizontalLayout buttonRowCart = new HorizontalLayout();
        buttonRowCart.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRowCart.setStyleName("searchButton");
        buttonRowCart.setSpacing(true);

        Button searchButtonCart = new Button("Add");
        searchButtonCart.addClickListener((Button.ClickListener) clickEvent -> insertInventoryItem("Cart",
                String.valueOf(itemNoComboCart.getValue()), itemDescFldCart.getValue(), qtyFldCart.getValue(),cartNoFld.getValue()));
        buttonRowCart.addComponent(searchButtonCart);

        cartMainLayout.addComponents(itemNoComboCart, itemDescFldCart, qtyFldCart, buttonRowCart);

        cartLayout.setVisible(false);
        shelfLayout.setVisible(false);
        typeCombo.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                if (event.getValue().toString().equals("Cart")) {
                    cartLayout.setVisible(true);
                    shelfLayout.setVisible(false);
                } else {
                    cartLayout.setVisible(false);
                    shelfLayout.setVisible(true);
                }
            } else {
                cartLayout.setVisible(false);
                shelfLayout.setVisible(false);
            }
        });

        tableLayout = new HorizontalLayout();
        tableLayout.setMargin(Constants.topMarginInfo);
        tableLayout.setSizeFull();
        addComponent(tableLayout);

        monthEndInventoryList = new ArrayList<>();
        monthEndInventoryList = (List<MonthEndInventory>)connection.getAllValuesNoRecrdStatus("com.back.office.entity.MonthEndInventory");
        monthEndInventoryGrid = new Grid<>();
        monthEndInventoryGrid.setColumnReorderingAllowed(true);
        monthEndInventoryGrid.setSizeFull();
        monthEndInventoryGrid.addColumn(MonthEndInventory::getMonth).setCaption("Month");
        monthEndInventoryGrid.addColumn(MonthEndInventory::getYear).setCaption("Year");
        monthEndInventoryGrid.addColumn(MonthEndInventory::getStation).setCaption("Station");
        monthEndInventoryGrid.addColumn(MonthEndInventory::getTypes).setCaption("Types");
        monthEndInventoryGrid.addColumn(MonthEndInventory::getUser).setCaption("User");

        monthEndInventoryGrid.setItems(monthEndInventoryList);
        monthEndInventoryGrid.addItemClickListener(event -> showCartShelfInventory(event.getItem()));

        tableLayout.addComponent(monthEndInventoryGrid);
    }

    private void showCartShelfInventory(MonthEndInventory inventory){

        List<MonthEndInventoryItem> items = connection.getMonthEndItems(inventory.getMonthEndInventoryId());
        List<MonthEndInventoryItem> cartItems = new ArrayList<>();
        List<MonthEndInventoryItem> shelfItems = new ArrayList<>();

        for(MonthEndInventoryItem item : items){
            if(item.getType().equals("Cart")){
                cartItems.add(item);
            }
            else {
                shelfItems.add(item);
            }
        }

        if(!inventoryWindow.isAttached()){
            VerticalLayout windowContent = new VerticalLayout();
            windowContent.setMargin(true);
            inventoryWindow.setContent(windowContent);
            inventoryWindow.setWidth("750px");

            TabSheet tabSheet = new TabSheet();

            VerticalLayout cartLayout = new VerticalLayout();
            cartLayout.setSizeFull();

            VerticalLayout shelfLayout = new VerticalLayout();
            shelfLayout.setSizeFull();

            tabSheet.addTab(cartLayout,"Carts Inventory", VaadinIcons.CART_O);
            tabSheet.addTab(shelfLayout,"Shelf Inventory", VaadinIcons.SCALE);

            VerticalLayout mainLayout = new VerticalLayout();
            Button downloadPDFBtn = new Button("Download PDF");
            downloadPDFBtn.setIcon(VaadinIcons.DOWNLOAD);
            mainLayout.addComponents(tabSheet,downloadPDFBtn);

            inventoryWindow.setContent(mainLayout);

            Grid<MonthEndInventoryItem> cartItemGrid = new Grid<>();
            cartItemGrid.setSizeFull();
            cartItemGrid.setWidth("90%");
            cartItemGrid.addColumn(MonthEndInventoryItem::getItemCode).setCaption("Item Code");
            cartItemGrid.addColumn(MonthEndInventoryItem::getItemDesc).setCaption("Item Desc");
            cartItemGrid.addColumn(MonthEndInventoryItem::getQty).setCaption("Qty");
            cartItemGrid.setItems(cartItems);
            cartLayout.addComponent(cartItemGrid);

            Grid<MonthEndInventoryItem> shelfItemGrid = new Grid<>();
            shelfItemGrid.setSizeFull();
            shelfItemGrid.setWidth("90%");
            shelfItemGrid.addColumn(MonthEndInventoryItem::getItemCode).setCaption("Item Code");
            shelfItemGrid.addColumn(MonthEndInventoryItem::getItemDesc).setCaption("Item Desc");
            shelfItemGrid.addColumn(MonthEndInventoryItem::getQty).setCaption("Qty");
            shelfItemGrid.setItems(shelfItems);
            shelfLayout.addComponent(shelfItemGrid);

            inventoryWindow.center();
            inventoryWindow.addStyleName("mywindowstyle");
            getUI().addWindow(inventoryWindow);
        }
    }

    private void insertInventoryItem(String type, String itemCode, String itemDesc, String qty,String shelfCartNo) {

        if (itemCode != null && !itemCode.isEmpty() && itemDesc != null && !itemDesc.isEmpty() &&
                qty != null && !qty.isEmpty() && shelfCartNo != null && !shelfCartNo.isEmpty()) {

            if (monthFld.getValue() != null) {
                LocalDate localDate = monthFld.getValue();
                String month = new DateFormatSymbols().getMonths()[localDate.getMonthValue() - 1];
                int year = localDate.getYear();
                String baseStation = baseStationFld.getValue();
                String userName = userFld.getValue();

                MonthEndInventoryItem item = new MonthEndInventoryItem();
                item.setItemCode(itemCode);
                item.setItemDesc(itemDesc);
                item.setQty(Integer.parseInt(qty));
                item.setType(type);
                item.setShelfCartNo(shelfCartNo);

                MonthEndInventory inventory = connection.getMonthEndInventory(year, month);
                if (inventory == null) {
                    inventory = new MonthEndInventory();
                    inventory.setMonth(month);
                    inventory.setYear(year);
                    inventory.setStation(baseStation);
                    inventory.setTypes(type);
                    inventory.setUser(userName);

                    int id = connection.insertObjectHBM(inventory);
                    item.setMonthEndInventoryId(id);
                    connection.insertObjectHBM(item);
                    monthEndInventoryList.add(inventory);
                }
                else {

                    if(!inventory.getTypes().contains(type)){
                        monthEndInventoryList.remove(inventory);
                        inventory.setTypes(inventory.getTypes() + "," + type);
                        connection.updateObjectHBM(inventory);
                        monthEndInventoryList.add(inventory);
                    }

                    item.setMonthEndInventoryId(inventory.getMonthEndInventoryId());
                    connection.insertObjectHBM(item);
                }
                monthEndInventoryGrid.setItems(monthEndInventoryList);
            }
            else {
                UserNotification.show("Error","Please fill all values","warning",UI.getCurrent());
            }
        }
        else{
            UserNotification.show("Error","Please fill all values","warning",UI.getCurrent());
        }
    }
}
