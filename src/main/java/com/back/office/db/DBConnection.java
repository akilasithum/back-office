package com.back.office.db;

import com.back.office.entity.AircraftDetails;
import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.ItemDetails;
import com.back.office.persistence.HibernateUtil;
import org.hibernate.classic.Session;

import java.lang.reflect.Type;
import java.util.List;

public class DBConnection {

    private static final DBConnection dbConnection = new DBConnection();

    private DBConnection(){
        try{

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public int insertObjectHBM(Object details){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        int id = (Integer)session.save(details);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public void updateObjectHBM(Object details){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(details);
        session.getTransaction().commit();
        session.close();
    }

    public boolean deleteAircraftDetails(int id){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            AircraftDetails aircraft = (AircraftDetails) session.get(AircraftDetails.class, id);
            session.delete(aircraft);
            session.getTransaction().commit();
            session.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean deleteCurrencyDetails(int id){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            CurrencyDetails aircraft = (CurrencyDetails) session.get(CurrencyDetails.class, id);
            session.delete(aircraft);
            session.getTransaction().commit();
            session.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean deleteObjectHBM(int id, String type){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Object object =  session.get(Class.forName(type), id);
            session.delete(object);
            session.getTransaction().commit();
            session.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public static DBConnection getInstance(){
        return dbConnection;
    }

 /*   public boolean isLoginSuccess(String userName,String password){
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user where userName = '" +userName +"' and " +
                    "password = '" + password +"'");

            while (rs.next())
                return true;
            con.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }*/

    public List<AircraftDetails> getAllFlights(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            return session.createCriteria(AircraftDetails.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CurrencyDetails> getAllCurrencies(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            return session.createCriteria(CurrencyDetails.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ItemDetails> getAllItems(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            return session.createCriteria(ItemDetails.class).list();
        } catch (Exception e) {
            return null;
        }
    }
}


