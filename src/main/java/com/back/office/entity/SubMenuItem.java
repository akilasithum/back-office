package com.back.office.entity;

import java.util.Map;

public class SubMenuItem {

    private String menuName;
    private String menuImage;
    private Map<String,String> subMenuImageMap;


    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public Map<String, String> getSubMenuImageMap() {
        return subMenuImageMap;
    }

    public void setSubMenuImageMap(Map<String, String> subMenuImageMap) {
        this.subMenuImageMap = subMenuImageMap;
    }
}
