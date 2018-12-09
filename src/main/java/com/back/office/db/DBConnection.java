package com.back.office.db;

import com.back.office.entity.*;
import com.back.office.persistence.HibernateUtil;
import org.hibernate.Filter;
import org.hibernate.classic.Session;

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

    public List<KitCodes> getAllKitCodes(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            return session.createCriteria(KitCodes.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<?> getAllValues(String className){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            return session.createCriteria(Class.forName(className)).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List getServiceTypeFromKitCode(String kitCode){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.enableFilter("kitCodeFilter").setParameter("kitCode", kitCode);
            session.beginTransaction();
            return session.createCriteria(KitCodes.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List getItemsFromServiceType(String serviceType){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.enableFilter("serviceTypeFilter").setParameter("serviceType", serviceType);
            session.beginTransaction();
            return session.createCriteria(ItemDetails.class).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List getFilterList(String filterName,String fieldName,Integer fieldValue,String className){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.enableFilter(filterName).setParameter(fieldName, fieldValue);
            session.beginTransaction();
            return session.createCriteria(Class.forName(className)).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List getItemsFromServiceType(String cartName,String drawer){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.enableFilter("packTypeFilter").setParameter("packType", cartName);
            session.enableFilter("drawerFilter").setParameter("drawerName", drawer);
            session.beginTransaction();
            return session.createCriteria(CartItems.class).list();
        } catch (Exception e) {
            return null;
        }
    }
}


