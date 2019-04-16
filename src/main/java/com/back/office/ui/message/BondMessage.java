package com.back.office.ui.message;

import java.util.List;
import java.util.Set;

import com.back.office.entity.User;
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

public class BondMessage extends VerticalLayout implements View{

    protected DBConnection connection;
    protected VerticalLayout hedderLayout;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout tableLayout;
    protected ComboBoxMultiselect<User> messageTo;
    protected TextArea message;
    protected Button sendButton;
    protected FilterGrid<Message> readMessage;
    protected FilterGrid<Message> newMessage;
    protected String userName;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if (userName == null || userName.toString().isEmpty()) {
            getUI().getNavigator().navigateTo("login");
        }
    }

    public BondMessage() {
        connection=DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
        userName =UI.getCurrent().getSession().getAttribute("userName").toString();
        setStyleName("backColorGrey");
        mssageread(userName);

    }

    protected void createMainLayout() {
        setSpacing(true);
        hedderLayout =new VerticalLayout();
        hedderLayout.setSizeFull();
        hedderLayout.setMargin(Constants.noMargin);
        Label h1 = new Label("Bond Messages");
        h1.addStyleName(ValoTheme.LABEL_H1);
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

        readMessage = new FilterGrid<>();
        readMessage.setColumnReorderingAllowed(false);
        readMessage.setWidth("60%");
        readMessage.setSizeFull();

        newMessage = new FilterGrid<>();
        newMessage.setColumnReorderingAllowed(false);
        newMessage.setWidth("60%");
        newMessage.setSizeFull();

        Label labelreadmessage=new Label("Read Messages");
        Label label_new_message=new Label("New Messages");

        sendButton.addClickListener(clickListener->messagdilivr());

        tableLayout.addComponent(label_new_message);
        tableLayout.addComponent(newMessage);
        tableLayout.addComponent(labelreadmessage);
        tableLayout.addComponent(readMessage);
        tableLayout.setWidth("70%");


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

        Window windowdatatable=new Window();
        VerticalLayout windowContent = new VerticalLayout();
        windowContent.setMargin(true);
        windowdatatable.setContent(windowContent);
        Button buttonclose=new Button("Ok");
        buttonclose.addClickListener((Button.ClickListener) clickEvent->closedatawindowh(windowdatatable));

        TextField message_from=new TextField("From");
        message_from.setValue(from_message_data);
        TextArea message_deta=new TextArea("Message");
        message_deta.setValue(recive_message_data);

        windowContent.addComponent(message_from);
        windowContent.addComponent(message_deta);

        windowdatatable.center();
        windowContent.addComponent(buttonclose);
        windowContent.setComponentAlignment(buttonclose,Alignment.BOTTOM_CENTER);

        windowdatatable.addStyleName("mywindowstyle");
        getUI().addWindow(windowdatatable);
    }

    public void messagdilivr() {

        Set<User> receiverList = messageTo.getSelectedItems();
        userName =UI.getCurrent().getSession().getAttribute("userName").toString();
        for(User user : receiverList){
            String messageContent = String.valueOf(message.getValue());
            Message messageDetails = new Message();
            messageDetails.setMessage_from(userName);
            messageDetails.setRead_un(false);
            messageDetails.setMessage(messageContent);
            messageDetails.setMessage_to(user.getStaffId());
            connection.insertObjectHBM(messageDetails);
        }

        Notification.show("Message sent successfully", Notification.Type.HUMANIZED_MESSAGE);
        this.messageTo.clear();
        message.clear();
    }

    public void mssageread(String user_name_data) {
        List<Message> lista = connection.getMessage(user_name_data,false);
        List<Message> listb = connection.getMessage(user_name_data,true);

        readMessage.setItems(listb);
        newMessage.setItems(lista);
    }

    private void closedatawindowh(Window windowdata) {
        windowdata.close();
    }

    public void showTableHeader() {
        readMessage.addColumn(Message::getMessage_from).setCaption("Message From").setWidth(200);
        readMessage.addColumn(Message::getMessage).setCaption("Message");
        newMessage.addColumn(Message::getMessage_from).setCaption("Message From").setWidth(200);
        newMessage.addColumn(Message::getMessage).setCaption("Message");
    }

}
