package com.back.office.ui.flightKitchen;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.back.office.entity.ArrivalFlight;
import com.back.office.entity.DepartureFlight;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.back.office.utils.UserNotification;
import com.vaadin.ui.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.back.office.db.DBConnection;
import com.back.office.entity.FlightSheduleDetail;
import com.poiji.bind.Poiji;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Notification.Type;

public class FlightShedule extends UserEntryView implements View{

    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Grid<ArrivalFlight> arrivalFlightGrid;
    protected Grid<DepartureFlight> departureFlightGrid;
    protected File file=new File("Schedule.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);
    Upload uploadButton;
    Button rssFeedBtn;
    protected File fileData;
    List uploadedFlightList;
    ComboBox flightTypeCombo;
    private static final String FLIGHT_TIME = "Time";
    private static final String FLIGHT_NO = "Flight #";
    private static final String AIRLINE = "Airline";
    private static final String DESTINATION = "Destination";
    private static final String FROM = "From";
    private static final String CHECKIN = "Check-in";
    private static final String GATE = "Gate";
    private static final String BELT = "Belt";
    private static final String STATUS = "Status";

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public FlightShedule() {
        super();
        createMainLayout();
        setMargin(Constants.noMargin);
        connection=DBConnection.getInstance();
    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        createLayout.setMargin(Constants.noMargin);

        Label h1=new Label("Flight Schedule");

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        HorizontalLayout dataLayout=new HorizontalLayout();

        rssFeedBtn = new Button("RSS Feed");
        flightTypeCombo = new ComboBox("Flight Schedule Type");
        flightTypeCombo.setItems(Arrays.asList("Arrivals","Departures"));
        flightTypeCombo.setEmptySelectionAllowed(false);
        flightTypeCombo.setSelectedItem("Arrivals");

        Button processButton=new Button("Process");
        dataLayout.addComponent(processButton);

        processButton.setVisible(false);
        processButton.addClickListener((Button.ClickListener)ClickEvent->
                processFile());

        addComponent(createLayout);

        arrivalFlightGrid =new Grid("Arrival Flights");
        arrivalFlightGrid.setSizeFull();

        departureFlightGrid =new Grid("Departure Flights");
        departureFlightGrid.setSizeFull();
        departureFlightGrid.setVisible(false);

        arrivalFlightGrid.addColumn(ArrivalFlight::getFlightNo).setCaption(FLIGHT_NO);
        arrivalFlightGrid.addColumn(bean-> BackOfficeUtils.getDateTimeFromDate(bean.getFlightTime())).setCaption(FLIGHT_TIME);
        arrivalFlightGrid.addColumn(ArrivalFlight::getAirline).setCaption(AIRLINE);
        arrivalFlightGrid.addColumn(ArrivalFlight::getFrom).setCaption(FROM);
        arrivalFlightGrid.addColumn(ArrivalFlight::getBelt).setCaption(BELT);
        arrivalFlightGrid.addColumn(ArrivalFlight::getGate).setCaption(GATE);
        arrivalFlightGrid.addColumn(ArrivalFlight::getStatus).setCaption(STATUS);

        departureFlightGrid.addColumn(DepartureFlight::getFlightNo).setCaption(FLIGHT_NO);
        departureFlightGrid.addColumn(bean-> BackOfficeUtils.getDateTimeFromDate(bean.getFlightTime())).setCaption(FLIGHT_TIME);
        departureFlightGrid.addColumn(DepartureFlight::getAirline).setCaption(AIRLINE);
        departureFlightGrid.addColumn(DepartureFlight::getDestination).setCaption(DESTINATION);
        departureFlightGrid.addColumn(DepartureFlight::getCheckin).setCaption(CHECKIN);
        departureFlightGrid.addColumn(DepartureFlight::getGate).setCaption(GATE);
        departureFlightGrid.addColumn(DepartureFlight::getStatus).setCaption(STATUS);


        List<String> allowedMimeTypes = new ArrayList<>();
        allowedMimeTypes.add("text/xml");
        allowedMimeTypes.add("application/xlsx");
        allowedMimeTypes.add("application/xls");
        allowedMimeTypes.add("application/vnd.ms-excel");
        allowedMimeTypes.add("application/octet-stream");
        allowedMimeTypes.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        uploadButton = new Upload("", (Upload.Receiver) (filename, mimeType) -> {
            try {
                fileData = File.createTempFile("temp",".xls");
                return new FileOutputStream(fileData);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });

        uploadButton.addStartedListener((Upload.StartedListener) event -> {

            String contentType = event.getMIMEType();
            boolean allowed = false;
            for (int i = 0; i < allowedMimeTypes.size(); i++) {
                if (contentType.equalsIgnoreCase(allowedMimeTypes.get(i))) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed) {
                UserNotification.show("Error", "Not a valid file ", "error",UI.getCurrent());
                uploadButton.interruptUpload();
            }
        });

        uploadButton.addFinishedListener((Upload.FinishedListener) finishedEvent -> {
            dataList();
            processButton.setVisible(true);

        });

        VerticalLayout btnLayout = new VerticalLayout();
        btnLayout.setMargin(Constants.noMargin);

        HorizontalLayout rssFeedLayout = new HorizontalLayout();
        rssFeedLayout.setMargin(false);
        rssFeedLayout.addComponents(rssFeedBtn,new Label("Last Updated at : " + new Date()));
        btnLayout.addComponents(flightTypeCombo,uploadButton,rssFeedLayout);
        createLayout.addComponents(btnLayout);
        uploadButton.setButtonCaption("Upload Excel");
        createLayout.addComponent(arrivalFlightGrid);
        createLayout.addComponent(departureFlightGrid);
        createLayout.addComponent(dataLayout);
    }

    private void processFile(){
        try {

            for (Object object : uploadedFlightList) {
                connection.insertObjectHBM(object);
            }
            UserNotification.show("Success","Successfully updated.","success",UI.getCurrent());
            uploadedFlightList = new ArrayList();
            arrivalFlightGrid.setItems(uploadedFlightList);
            departureFlightGrid.setItems(uploadedFlightList);
            flightTypeCombo.setSelectedItem("Arrivals");
            departureFlightGrid.setVisible(false);
            arrivalFlightGrid.setVisible(true);
            fileData.delete();
        }
        catch (Exception e){

            UserNotification.show("Error", "Something wrong with the input file. Please check the file and upload again ",
                    "error",UI.getCurrent());
            fileData.delete();
        }
    }

    public void dataList() {
        try{
            if(flightTypeCombo.getValue().equals("Arrivals")){
                uploadedFlightList = Poiji.fromExcel(fileData, ArrivalFlight.class);
                departureFlightGrid.setVisible(false);
                arrivalFlightGrid.setVisible(true);
                arrivalFlightGrid.setItems(uploadedFlightList);
            }
            else{
                uploadedFlightList = Poiji.fromExcel(fileData, DepartureFlight.class);
                departureFlightGrid.setVisible(true);
                arrivalFlightGrid.setVisible(false);
                departureFlightGrid.setItems(uploadedFlightList);
            }
        }
        catch (Exception e){
            UserNotification.show("Error", "Something wrong with the input file. Please check the file and upload again",
                    "error",UI.getCurrent());
        }
    }

}

