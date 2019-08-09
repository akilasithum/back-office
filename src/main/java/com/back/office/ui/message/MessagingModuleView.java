package com.back.office.ui.message;

import java.util.List;
import java.util.Set;

import com.back.office.entity.User;
import com.back.office.framework.UserEntryView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
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

    }

    protected void createMainLayout() {
        setSpacing(true);
        hedderLayout =new VerticalLayout();
        hedderLayout.setSizeFull();
        hedderLayout.setMargin(Constants.noMargin);
        Label h1 = new Label("Bond Messages");
        h1.addStyleName("headerText");
        hedderLayout.addComponent(h1);
        addComponent(hedderLayout);

        userFormLayout = new VerticalLayout();
        userFormLayout.setSpacing(true);
        userFormLayout.setMargin(Constants.noMargin);
        addComponent(userFormLayout);

        messageTo =new ComboBoxMultiselect ("Message To");
        messageTo.showSelectAllButton(true);
        messageTo.setItems(connection.getStaffIdUserNameMap());
        messageTo.setRequiredIndicatorVisible(true);
        messageTo.setWidth("70%");
        messageTo.setSizeFull();

        message=new TextArea("Message");
        message.setCaption("Message");
        message.setSizeFull();
        message.setRequiredIndicatorVisible(true);

        tableLayout =new VerticalLayout();
        tableLayout.setSpacing(true);
        tableLayout.setMargin(Constants.noMargin);
        tableLayout.setSizeFull();
        addComponent(tableLayout);


        sendButton =new Button("Send");
        sendButton.setCaption("Send");

        userFormLayout.setWidth("50%");
        userFormLayout.addComponent(messageTo);
        userFormLayout.addComponent(message);
        userFormLayout.addComponent(sendButton);

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

        sendButton.addClickListener(clickListener->messagdilivr());

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


    public void selectDetailsh(String from_message_data,String recive_message_data) {

        Window messageWindow = new Window();
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
        userName =UI.getCurrent().getSession().getAttribute("userName").toString();
        for(String user : receiverList){
            String messageContent = String.valueOf(message.getValue());
            Message messageDetails = new Message();
            messageDetails.setMessage_from(userName);
            messageDetails.setRead_un(false);
            messageDetails.setMessage(messageContent);
            messageDetails.setMessage_to(user);
            connection.insertObjectHBM(messageDetails);
        }

        Notification.show("Message sent successfully", Notification.Type.HUMANIZED_MESSAGE);
        this.messageTo.clear();
        message.clear();
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
        newMessage.addColumn(Message::getMessage_from).setCaption("Message From").setWidth(200);
        newMessage.addColumn(Message::getMessage).setCaption("Message");
        sentMessage.addColumn(Message::getMessage_to).setCaption("Message To").setWidth(200);
        sentMessage.addColumn(Message::getMessage).setCaption("Message");
    }

}
