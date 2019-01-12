package com.back.office.db;

import java.sql.*;

public class SQLConnection {

    private Connection getSQLConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/flight_pos","root","");
            return con;
        }catch(Exception e){
            return null;
        }
    }

    public void getSalesDetails(){
        Connection con = getSQLConnection();
        try {
        Statement stmt=con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT i.itemName,i.category, i.itemId, isd.quantity , i.costPrice , isd.price, " +
                "f.flightDate,f.flightNo, f.flightFrom, f.flightTo, o.serviceType " +
                "FROM itemDetails AS i " +
                "INNER JOIN pos_item_sale_details AS isd ON i.itemId = isd.itemId " +
                "INNER JOIN pos_order_flight_details AS f ON isd.orderId = f.orderId " +
                "INNER JOIN pos_order_main_details AS o ON isd.orderId = o.orderId");
            while(rs.next()){
                String itemName = rs.getString("itemName");

            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

