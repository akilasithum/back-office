package com.back.office.ui.uploads;

import com.back.office.db.DBConnection;
import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.ItemDetails;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.Constants;
import com.back.office.xml.Currencies;
import com.poiji.bind.Poiji;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadView extends UserEntryView implements View {

    protected DBConnection connection;
    protected VerticalLayout headerLayout;
    private VerticalLayout userFormLayout;
    private VerticalLayout tableLayout;
    private Button processButton;
    protected ComboBox uploadTypeComboBox;
    protected ComboBox fileTypeComboBox;
    protected File tempFile;
    Upload uploadComponent;
    Grid<Object> detailsTable;
    List uploadDataList;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public UploadView(){
        super();
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    protected void createMainLayout() {

        setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        Label h1 = new Label("Upload Files");
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        userFormLayout.setSpacing(true);
        userFormLayout.setMargin(Constants.noMargin);
        addComponent(userFormLayout);
        tableLayout = new VerticalLayout();
        tableLayout.setMargin(Constants.noMargin);
        addComponent(tableLayout);
        processButton = new Button("Process");
        processButton.addClickListener((Button.ClickListener) clickEvent -> processFile());
        tableLayout.setVisible(false);
        tableLayout.setSpacing(true);

        fileTypeComboBox = new ComboBox("File Type");
        fileTypeComboBox.setDescription("File Type");
        fileTypeComboBox.setItems("XML","Excel");
        fileTypeComboBox.setEmptySelectionAllowed(false);
        fileTypeComboBox.setRequiredIndicatorVisible(true);
        userFormLayout.addComponent(fileTypeComboBox);

        uploadTypeComboBox = new ComboBox("Entity Type");
        uploadTypeComboBox.setDescription("Entity Type");
        uploadTypeComboBox.setItems("Aircraft Type","Currency","Create Items","Create Kit Codes","Equipment Types","CC Black List","Vouchers");
        uploadTypeComboBox.setEmptySelectionAllowed(false);
        uploadTypeComboBox.setRequiredIndicatorVisible(true);
        userFormLayout.addComponent(uploadTypeComboBox);


        List<String> allowedMimeTypes = new ArrayList<>();
        allowedMimeTypes.add("text/xml");
        allowedMimeTypes.add("application/xls");
        allowedMimeTypes.add("application/vnd.ms-excel");
        allowedMimeTypes.add("application/octet-stream");

        uploadComponent = new Upload("choose file",new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                try {
                    String fileType = String.valueOf(fileTypeComboBox.getValue());
                    if("XML".equals(fileType))
                    tempFile = File.createTempFile("temp", ".xml");
                    else if("Excel".equals(fileType)) tempFile = File.createTempFile("temp", ".xlsx");
                    return new FileOutputStream(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
        uploadComponent.addStartedListener((Upload.StartedListener) event -> {

            if(uploadTypeComboBox.getValue() != null && !uploadTypeComboBox.getValue().toString().isEmpty()) {
                String contentType = event.getMIMEType();
                boolean allowed = false;
                for (int i = 0; i < allowedMimeTypes.size(); i++) {
                    if (contentType.equalsIgnoreCase(allowedMimeTypes.get(i))) {
                        allowed = true;
                        break;
                    }
                }
                if (allowed) {
                    Notification.show("Upload started: ", Type.HUMANIZED_MESSAGE);
                } else {
                    Notification.show("Error", "Not a valid file ", Type.WARNING_MESSAGE);
                    uploadComponent.interruptUpload();
                }
            }
            else{
                Notification.show("Error", "Please specify entity type first.", Type.WARNING_MESSAGE);
                uploadComponent.interruptUpload();
            }
        });
        uploadComponent.addFinishedListener((Upload.FinishedListener) finishedEvent -> {
            tableLayout.setVisible(true);
            showData();

        });
        userFormLayout.addComponent(uploadComponent);
        uploadComponent.setButtonCaption("Upload File");


    }

    private void processFile(){
        try {
            String className = getEntityTypeClassNameMap().get(uploadTypeComboBox.getValue().toString());
            if(className != null && !className.isEmpty()) {
                for (Object object : uploadDataList) {
                    connection.insertObjectHBM(object);
                }
                Notification.show("Successfully updated.");
                tableLayout.setVisible(false);
                uploadTypeComboBox.clear();
            }
            else{
                Notification.show("Not Implemented", "This file upload feature is not implemented.", Type.WARNING_MESSAGE);
            }
            uploadDataList = null;
            tempFile.delete();
        }
        catch (Exception e){
            Notification.show("Error", "Something wrong with the input file. Please check the file and upload again ", Type.WARNING_MESSAGE);
            tempFile.delete();
        }
    }

    private void showData(){
        String fileType = String.valueOf(fileTypeComboBox.getValue());
        if("XML".equals(fileType)) {
                try {
                    String className = getEntityTypeClassNameMap().get(uploadTypeComboBox.getValue().toString());
                    if (className != null && !className.isEmpty()) {
                        Class clz = Class.forName(className);
                        JAXBContext jc = JAXBContext.newInstance(clz);
                        Unmarshaller unmarshaller = jc.createUnmarshaller();
                        Object sc = unmarshaller.unmarshal(tempFile);
                        Method getList = clz.getDeclaredMethod("getList");
                        uploadDataList = (List) getList.invoke(sc);

                        Class curClz = Class.forName(getEntityClassNameMap().get(uploadTypeComboBox.getValue().toString()));
                        tableLayout.removeAllComponents();
                        detailsTable = new Grid(curClz);
                        detailsTable.setItems(uploadDataList);
                        detailsTable.setSelectionMode(Grid.SelectionMode.SINGLE);
                        detailsTable.setColumnReorderingAllowed(true);
                        detailsTable.setSizeFull();
                        tableLayout.addComponent(detailsTable);
                        tableLayout.addComponent(processButton);
                    } else {
                        Notification.show("Not Implemented", "This file upload feature is not implemented.", Type.WARNING_MESSAGE);
                    }
                    //tempFile.delete();
                } catch (Exception e) {
                    Notification.show("Error", "Something wrong with the input file. Please check the file and upload again ", Type.WARNING_MESSAGE);
                    tempFile.delete();
                }
        }
        else if("Excel".equals(fileType)){
            showExcelData();
        }
    }
    private void showExcelData(){
        try {
            Class clz = Class.forName(getEntityClassNameMap().get(uploadTypeComboBox.getValue().toString()));
            uploadDataList = Poiji.fromExcel(tempFile, clz);
            tableLayout.removeAllComponents();
            detailsTable = new Grid(clz);
            detailsTable.setItems(uploadDataList);
            detailsTable.setSelectionMode(Grid.SelectionMode.SINGLE);
            detailsTable.setColumnReorderingAllowed(true);
            detailsTable.setSizeFull();
            tableLayout.addComponent(detailsTable);
            tableLayout.addComponent(processButton);

        } catch (ClassNotFoundException e) {
            Notification.show("Something wrong with file. Please check and upload again",Type.WARNING_MESSAGE);
            tempFile.delete();
        }

    }

    private Map<String,String> getEntityTypeClassNameMap(){
        Map<String,String> map = new HashMap<>();
        //map.put("Aircraft Type","com.back.office.entity.AircraftDetails");
        map.put("Currency","com.back.office.xml.Currencies");
        map.put("Create Items","com.back.office.xml.Items");
        map.put("Create Kit Codes","com.back.office.xml.KitCodes");
        map.put("Equipment Types","com.back.office.xml.Equipments");
        //map.put("CC Black List","com.back.office.entity.BlackListCC");
        map.put("Vouchers","com.back.office.xml.Vouchers");

        return map;
    }

    private Map<String,String> getEntityClassNameMap(){
        Map<String,String> map = new HashMap<>();
        //map.put("Aircraft Type","com.back.office.entity.AircraftDetails");
        map.put("Currency","com.back.office.entity.CurrencyDetails");
        map.put("Create Items","com.back.office.entity.ItemDetails");
        map.put("Create Kit Codes","com.back.office.entity.KitCodes");
        map.put("Equipment Types","com.back.office.entity.EquipmentDetails");
        //map.put("CC Black List","com.back.office.entity.BlackListCC");
        map.put("Vouchers","com.back.office.entity.Voucher");

        return map;
    }
}
