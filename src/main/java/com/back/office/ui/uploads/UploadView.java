package com.back.office.ui.uploads;

import com.back.office.db.DBConnection;
import com.back.office.xml.Currencies;
import com.google.gson.reflect.TypeToken;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
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

public class UploadView extends VerticalLayout implements View {

    protected DBConnection connection;
    protected VerticalLayout headerLayout;
    private VerticalLayout userFormLayout;
    private VerticalLayout tableLayout;
    private Button processButton;
    protected ComboBox uploadTypeComboBox;
    protected File tempFile;
    Upload uploadComponent;
    Table detailsTable;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public UploadView(){
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    protected void createMainLayout() {

        setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        Label h1 = new Label("Upload Files");
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        userFormLayout.setSpacing(true);
        addComponent(userFormLayout);
        tableLayout = new VerticalLayout();
        addComponent(tableLayout);
        processButton = new Button("Process");
        processButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                processFile();
            }
        });
        tableLayout.setVisible(false);
        tableLayout.setSpacing(true);

        uploadTypeComboBox = new ComboBox("Entity Type");
        uploadTypeComboBox.setInputPrompt("Entity Type");
        uploadTypeComboBox.addItem("Aircraft Type");
        uploadTypeComboBox.addItem("Currency");
        uploadTypeComboBox.addItem("Create Items");
        uploadTypeComboBox.addItem("Create Kit Codes");
        uploadTypeComboBox.addItem("Equipment Types");
        uploadTypeComboBox.addItem("CC Black List");
        uploadTypeComboBox.addItem("Vouchers");
        uploadTypeComboBox.setNullSelectionAllowed(false);
        userFormLayout.addComponent(uploadTypeComboBox);


        List<String> allowedMimeTypes = new ArrayList<>();
        allowedMimeTypes.add("text/xml");
        uploadComponent = new Upload("choose XML file",new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                try {
                    tempFile = File.createTempFile("temp", ".xml");
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
            detailsTable.setContainerDataSource(generateContainer());

        });

        userFormLayout.addComponent(uploadComponent);
        uploadComponent.setButtonCaption("Upload File");

        detailsTable = new Table();
        detailsTable.setSelectable(true);
        detailsTable.setMultiSelect(false);
        detailsTable.setSortEnabled(true);
        detailsTable.setColumnCollapsingAllowed(true);
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setPageLength(10);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        tableLayout.addComponent(processButton);
    }

    private void processFile(){
        try {
            String className = getEntityTypeClassNameMap().get(uploadTypeComboBox.getValue().toString());
            if(className != null && !className.isEmpty()) {
                Class clz = Class.forName(className);
                JAXBContext jc = JAXBContext.newInstance(Currencies.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                Object sc = unmarshaller.unmarshal(tempFile);
                Method getList = clz.getDeclaredMethod("getList");
                List list = (List) getList.invoke(sc);
                for (Object object : list) {
                    connection.insertObjectHBM(object);
                }
            }
            else{
                Notification.show("Not Implemented", "This file upload feature is not implemented.", Type.WARNING_MESSAGE);
            }
            tempFile.delete();
        }
        catch (Exception e){
            Notification.show("Error", "Something wrong with the input file. Please check the file and upload again ", Type.WARNING_MESSAGE);
            tempFile.delete();
        }
    }

    private IndexedContainer generateContainer(){
        try {
            boolean isHeaderAdded = false;
            IndexedContainer container = new IndexedContainer();
            SAXReader reader = new SAXReader();
            Document document = reader.read(tempFile);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            int i = 1;
            for(Element element : elements){
                List<Element> attributes = element.elements();
                Item item = container.addItem(i);
                if(!isHeaderAdded) {
                    for (Element attr : attributes) {
                        container.addContainerProperty(attr.getName(), String.class, null);
                        item.getItemProperty(attr.getName()).setValue(attr.getStringValue());
                    }
                    isHeaderAdded = true;
                }
                else{
                    for (Element attr : attributes) {
                        item.getItemProperty(attr.getName()).setValue(attr.getStringValue());
                    }
                }
                i++;
            }

        return container;
        }
        catch (Exception e){
            return null;
        }
    }

    public  String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
            int c = 0;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
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
}
