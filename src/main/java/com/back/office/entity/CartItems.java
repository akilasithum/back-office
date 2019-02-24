package com.back.office.entity;

import org.hibernate.annotations.*;

@FilterDefs({
        @FilterDef(name = "packDrawerFilter",
                parameters = {
                        @ParamDef(name="packType", type="java.lang.String"),
                        @ParamDef(name="drawerName", type="java.lang.String")
                }
        )
})
@Filters({
        @Filter(name = "packDrawerFilter",
                condition = "packType = :packType and drawerName = :drawerName")
})
public class CartItems {

    private int cartItemId;
    private String itemId;
    private String itemName;
    private String packType;
    private String drawerName;
    private int quantity;
    private int recordStatus;

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
