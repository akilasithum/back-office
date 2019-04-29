package com.back.office.ui.finance;

import java.util.List;

import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;

import com.back.office.db.DBConnection;
import com.back.office.entity.CurrencyDetails;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class CurruncyDetailHistory extends VerticalLayout implements View {
    protected DBConnection connection;
    ComboBox currencyDetail;
    FilterGrid<CurrencyDetails> currencyGrid;
    private Button button;
    FormLayout layoutVertical;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public CurruncyDetailHistory() {
        createMainLayout();
        connection=DBConnection.getInstance();
    }

    public void createMainLayout() {
        layoutVertical=new FormLayout();
        Label h1=new Label("Currency History");
        h1.addStyleName(ValoTheme.LABEL_H1);
        layoutVertical.addComponent(h1);
        currencyDetail=new ComboBox("Currency");
        currencyDetail.setItems(BackOfficeUtils.getCurrencyDropDownValues(false));
        currencyDetail.setRequiredIndicatorVisible(true);
        layoutVertical.addComponent(currencyDetail);
        currencyGrid=new FilterGrid();
        currencyGrid.setVisible(false);
        button=new Button("Submit");
        layoutVertical.addComponent(button);
        button.addClickListener((Button.ClickListener) ClickEvent->process());
        currencyGrid.setWidth("50%");
        layoutVertical.addComponent(currencyGrid);
        addComponent(layoutVertical);



    }
    public void process() {

        currencyGrid.removeAllColumns();


        if(currencyDetail.getValue()!=null&&!currencyDetail.getValue().toString().isEmpty()) {
            List<CurrencyDetails> currecyDetailsList=connection.getCurrencyDetail(currencyDetail.getValue().toString());

            currencyGrid.addColumn(CurrencyDetails::getCurrencyCode).setCaption("Currency Code");
            currencyGrid.addColumn(CurrencyDetails::getCurrencyDesc).setCaption("Currency Desc");
            currencyGrid.addColumn(CurrencyDetails::getCurrencyRate).setCaption("Curruncy Rate");
            currencyGrid.addColumn(CurrencyDetails::getLastUpdateDateTime).setCaption("Last Update Date Time");
            currencyGrid.setItems(currecyDetailsList);
            currencyGrid.setVisible(true);


        }else {

            Notification.show("Error","Pleas input Currency Code",Type.WARNING_MESSAGE);


        }
    }
}

