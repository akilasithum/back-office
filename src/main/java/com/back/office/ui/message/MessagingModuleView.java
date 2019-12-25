package com.back.office.ui.message;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.back.office.entity.User;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.UserNotification;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.addons.ComboBoxMultiselect;
import org.vaadin.addons.filteringgrid.FilterGrid;

import com.back.office.db.DBConnection;
import com.back.office.entity.Message;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.wizards.Wizard;

import javax.jws.soap.SOAPBinding;

public class MessagingModuleView extends UserEntryView implements View{

    protected DBConnection connection;
    protected VerticalLayout hedderLayout;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout tableLayout;
    protected ComboBoxMultiselect<String> messageTo;
    protected TextArea message;
    protected Button sendButton;
    protected FilterGrid<Message> readMessage;
    protected FilterGrid<Message> newMessage;
    protected FilterGrid<Message> sentMessage;
    protected String userName;
    protected TabSheet messageTab;
    private Window createMsgWindow;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if (userName == null || userName.toString().isEmpty()) {
            getUI().getNavigator().navigateTo("login");
        }
    }

    public MessagingModuleView() {
        super(true);
        connection=DBConnection.getInstance();
        createMainLayout();
        userName =UI.getCurrent().getSession().getAttribute("userName").toString();
        mssageread(userName);
        createMsgWindow = new Window("Create New Message");
    }

    protected void createMainLayout() {
        setSpacing(true);
        hedderLayout =new VerticalLayout();
        hedderLayout.setSizeFull();
        hedderLayout.setMargin(Constants.noMargin);
        Label h1 = new Label("Messaging");
        h1.addStyleName("headerText");
        hedderLayout.addComponent(h1);
        addComponent(hedderLayout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        MarginInfo marginInfo = new MarginInfo(false,false,false,false);
        buttonLayout.setMargin(marginInfo);
        buttonLayout.setSizeFull();
        addComponent(buttonLayout);

        Button addNewButton = new Button();
        addNewButton.setIcon(FontAwesome.PLUS);
        addNewButton.setStyleName("add_button");
        addNewButton.setSizeFull();
        addNewButton.addClickListener((Button.ClickListener) clickEvent -> openCreateMsgWindow());

        buttonLayout.addComponents(addNewButton);

        tableLayout =new VerticalLayout();
        tableLayout.setSpacing(true);
        tableLayout.setMargin(Constants.noMargin);
        tableLayout.setSizeFull();
        addComponent(tableLayout);

        messageTab = new TabSheet();

        readMessage = new FilterGrid<>();
        readMessage.setColumnReorderingAllowed(false);
        readMessage.setWidth("60%");
        readMessage.setSizeFull();

        newMessage = new FilterGrid<>();
        newMessage.setColumnReorderingAllowed(false);
        newMessage.setWidth("60%");
        newMessage.setSizeFull();

        sentMessage = new FilterGrid<>();
        sentMessage.setColumnReorderingAllowed(false);
        sentMessage.setWidth("60%");
        sentMessage.setSizeFull();

        messageTab.addStyleName(ValoTheme.TABSHEET_FRAMED);
        messageTab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        messageTab.addTab(newMessage,"New Messages", VaadinIcons.ENVELOPE);
        messageTab.addTab(readMessage,"Read Messages",VaadinIcons.ENVELOPE_OPEN);
        messageTab.addTab(sentMessage,"Sent Messages",VaadinIcons.ENVELOPE_OPEN_O);
        messageTab.setStyleName("blackFont");

        tableLayout.setWidth("70%");
        tableLayout.addComponents(messageTab);

        showTableHeader();

        readMessage.addItemClickListener((ItemClickListener<Message>) itemClick -> {
            itemClick.getRowIndex();
            String from_message_data=itemClick.getItem().getMessage_from();
            String recive_message_dat=itemClick.getItem().getMessage();

            selectDetailsh(from_message_data,recive_message_dat);


        });

        newMessage.addItemClickListener((ItemClickListener<Message>) itemClick -> {
            itemClick.getRowIndex();
            int selectdata=itemClick.getItem().getMessage_id();
            userName =UI.getCurrent().getSession().getAttribute("userName").toString();
            String from_message_data=itemClick.getItem().getMessage_from();
            String recive_message_dat=itemClick.getItem().getMessage();
            String message_fromdata=itemClick.getItem().getMessage_from();

            selectDetailsh(from_message_data,recive_message_dat);

            Message messagedatedith=new Message();
            messagedatedith.setMessage_id(selectdata);
            messagedatedith.setRead_un(true);
            messagedatedith.setMessage_from(message_fromdata);
            messagedatedith.setMessage_to(userName);
            messagedatedith.setMessage(recive_message_dat);


            connection.updateObjectHBM(messagedatedith);


        });
    }

    public void openCreateMsgWindow(){

        if(!createMsgWindow.isAttached()) {
            createMsgWindow.setWidth("40%");
            createMsgWindow.setHeight(350, Unit.PIXELS);
            final FormLayout content = new FormLayout();
            content.setMargin(true);

            userFormLayout = new VerticalLayout();
            userFormLayout.setSpacing(true);
            userFormLayout.setMargin(true);
            addComponent(userFormLayout);

            messageTo = new ComboBoxMultiselect("Message To");
            messageTo.showSelectAllButton(true);
            messageTo.setItems(connection.getStaffIdUserNameMap());
            messageTo.setRequiredIndicatorVisible(true);
            messageTo.setWidth("70%");
            messageTo.setSizeFull();

            message = new TextArea("Message");
            message.setCaption("Message");
            message.setSizeFull();
            message.setRequiredIndicatorVisible(true);

            sendButton = new Button("Send");
            sendButton.setCaption("Send");
            sendButton.addClickListener(clickListener->messagdilivr());

            userFormLayout.setWidth("80%");
            userFormLayout.addComponent(messageTo);
            userFormLayout.addComponent(message);
            userFormLayout.addComponent(sendButton);

            createMsgWindow.center();
            createMsgWindow.setContent(userFormLayout);
            createMsgWindow.addStyleName("mywindowstyle");
            createMsgWindow.setModal(true);
            getUI().addWindow(createMsgWindow);
        }

    }


    public void selectDetailsh(String from_message_data,String recive_message_data) {

        Window messageWindow = new Window("Message");
        messageWindow.setSizeFull();
        messageWindow.setWidth("40%");
        messageWindow.setHeight("40%");
        VerticalLayout windowContent = new VerticalLayout();
        windowContent.setMargin(true);
        messageWindow.setContent(windowContent);
        Button replyBtn=new Button("Reply");
        Button buttonclose=new Button("Cancel");
        buttonclose.addClickListener((Button.ClickListener) clickEvent->closedatawindowh(messageWindow));
        replyBtn.addClickListener((Button.ClickListener) clickEvent -> {
            setUserNameForReply(from_message_data,messageWindow);
        });

        TextField message_from=new TextField("From");
        message_from.setEnabled(false);
        message_from.setSizeFull();
        message_from.setWidth("30%");
        message_from.setValue(from_message_data);
        TextArea messageText = new TextArea("Message");
        messageText.setEnabled(false);
        messageText.setSizeFull();
        messageText.setWidth("70%");
        messageText.setValue(recive_message_data);

        windowContent.addComponent(message_from);
        windowContent.addComponent(messageText);

        messageWindow.center();
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(replyBtn,buttonclose);
        windowContent.addComponent(btnLayout);

        messageWindow.addStyleName("mywindowstyle");
        getUI().addWindow(messageWindow);
    }

    public void messagdilivr() {

        Set<String> receiverList = messageTo.getSelectedItems();
        if(receiverList != null && message.getValue() != null && !message.getValue().isEmpty()){
            userName =UI.getCurrent().getSession().getAttribute("userName").toString();
            for(String user : receiverList){
                String messageContent = String.valueOf(message.getValue());
                Message messageDetails = new Message();
                messageDetails.setMessage_from(userName);
                messageDetails.setRead_un(false);
                messageDetails.setMessage(messageContent);
                messageDetails.setMessage_to(user);
                messageDetails.setSentTime(new Date());
                connection.insertObjectHBM(messageDetails);
            }
            UserNotification.show("Success","Message sent successfully","success", UI.getCurrent());
            this.messageTo.clear();
            message.clear();
            createMsgWindow.close();
        }
        else{
            UserNotification.show("Warning","Please add sender and message details","warning", UI.getCurrent());
        }
    }

    public void mssageread(String user_name_data) {
        List<Message> newMessages = connection.getMessage(user_name_data,false);
        List<Message> readMessages = connection.getMessage(user_name_data,true);
        List<Message> sentMessages = connection.getSentMessage(user_name_data);

        readMessage.setItems(readMessages);
        newMessage.setItems(newMessages);
        sentMessage.setItems(sentMessages);
    }

    private void closedatawindowh(Window windowdata) {
        windowdata.close();
    }

    private void setUserNameForReply(String userName,Window window){
        window.close();
        messageTo.clear();
        messageTo.select(userName);

    }

    public void showTableHeader() {
        readMessage.addColumn(Message::getMessage_from).setCaption("Message From").setWidth(200);
        readMessage.addColumn(Message::getMessage).setCaption("Message");
        readMessage.addColumn(bean -> BackOfficeUtils.getDateTimeFromDateTime(bean.getSentTime())).setCaption("Sent Time").setWidth(200);
        newMessage.addColumn(Message::getMessage_from).setCaption("Message From").setWidth(200);
        newMessage.addColumn(Message::getMessage).setCaption("Message");
        newMessage.addColumn(bean -> BackOfficeUtils.getDateTimeFromDateTime(bean.getSentTime())).setCaption("Sent Time").setWidth(200);
        sentMessage.addColumn(Message::getMessage_to).setCaption("Message To").setWidth(200);
        sentMessage.addColumn(Message::getMessage).setCaption("Message");
        sentMessage.addColumn(bean -> BackOfficeUtils.getDateTimeFromDateTime(bean.getSentTime())).setCaption("Sent Time").setWidth(200);
    }

}
