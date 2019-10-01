package com.back.office.utils;

import com.vaadin.ui.UI;
import org.vaadin.alump.fancylayouts.FancyNotifications;

public class UserNotification {

    public static void show(String title, String desc, String type, UI ui){
        FancyNotifications notifications = (FancyNotifications)ui.getSession().getAttribute("notification");
        notifications.showNotification(null, title,
                desc, null,
                "notification-"+type);
    }
}
