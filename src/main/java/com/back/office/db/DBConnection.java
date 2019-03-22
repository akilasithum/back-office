package com.back.office.db;

import com.back.office.entity.*;
import com.back.office.persistence.HibernateUtil;
import com.back.office.ui.salesReports.CategorySalesView;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public void updateRecordStatus(int id,String className){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            Class clz = Class.forName(className);
            Object obj = session.get(Class.forName(className), id);
            Class[] paramStr = new Class[1];
            paramStr[0] = Integer.TYPE;
            Method status = clz.getDeclaredMethod("setRecordStatus",paramStr);
            status.invoke(obj,new Integer(1));
            session.close();
            updateObjectHBM(obj);


        } catch (Exception e) {
            session.close();
            e.printStackTrace();
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

    public boolean deleteObjectHBM(Object object){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(AircraftDetails.class);
            criteria.add(Restrictions.eq("recordStatus", 0));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List<CurrencyDetails> getAllCurrencies(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(CurrencyDetails.class);
            criteria.add(Restrictions.eq("recordStatus", 0));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List<ItemDetails> getAllItems(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(ItemDetails.class);
            criteria.add(Restrictions.eq("recordStatus", 0));
            criteria.addOrder(Order.asc("category"));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isLoginSuccessful(String userName,String password){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("staffId", userName));
            criteria.add(Restrictions.eq("password", password));
            List retList =  criteria.list();
            session.close();
            return retList != null && !retList.isEmpty();
        } catch (Exception e) {
            session.close();
            return false;
        }
    }

    public List<ItemDetails> getAllActiveItems(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(ItemDetails.class);
            criteria.add(Restrictions.eq("recordStatus", 0));
            criteria.add(Restrictions.eq("deListed", "No"));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List<KitCodes> getAllKitCodes(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(KitCodes.class);
            criteria.add(Restrictions.eq("recordStatus", 0));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List getAllPermissionCodes(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(PermissionCodes.class);
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List<?> getAllValues(String className){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(Class.forName(className));
            criteria.add(Restrictions.eq("recordStatus", 0));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List getFilterList(String filterName,String fieldName,Integer fieldValue,String className,String orderByFeild){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.enableFilter(filterName).setParameter(fieldName, fieldValue);
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Class.forName(className)).addOrder(Order.asc(orderByFeild));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List getUserRoleIds(String filterName,String fieldName,Integer fieldValue,String className){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.enableFilter(filterName).setParameter(fieldName, fieldValue);
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Class.forName(className)).setProjection(Projections.property("permissionCode"));
            List list = criteria.list();
            session.close();
            return list;

        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List getItemsFromServiceType(String cartName,String drawer){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.enableFilter("packTypeFilter").setParameter("packType", cartName);
            session.enableFilter("drawerFilter").setParameter("drawerName", drawer);
            session.beginTransaction();
            Criteria criteria = session.createCriteria(CartItems.class);
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List getSalesDetails(Date flightFromDate,Date flightToDate,String category,String serviceType,String flightFrom,
                                String flightTo,String sifNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(SalesDetails.class);
        criteria.add(Restrictions.ge("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.le("flightDate", tommorow(flightToDate)));
        if(category != null && !category.isEmpty()){
            criteria.add(Restrictions.eq("category", category));
        }
        if(serviceType != null && !serviceType.isEmpty() && !serviceType.equals("All")){
            criteria.add(Restrictions.eq("serviceType", serviceType));
        }
        if(flightFrom != null && !flightFrom.isEmpty()){
            criteria.add(Restrictions.eq("flightFrom", flightFrom));
        }
        if(flightTo != null && !flightTo.isEmpty()){
            criteria.add(Restrictions.eq("flightTo", flightTo));
        }
        if(sifNo != null && !sifNo.isEmpty()){
            criteria.add(Restrictions.eq("sifNo", Integer.parseInt(sifNo)));
        }
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getFlightPaymentDetails(Date flightFromDate,Date flightToDate,String serviceType,String flightNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(FlightPaymentDetails.class);
        criteria.add(Restrictions.ge("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.le("flightDate", tommorow(flightToDate)));
        if(serviceType != null && !serviceType.isEmpty() && !serviceType.equals("All")){
            criteria.add(Restrictions.eq("serviceType", serviceType));
        }
        if(flightNo != null && !flightNo.isEmpty()){
            criteria.add(Restrictions.eq("flightNo", flightNo));
        }
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getCategorySalesDetails(Date flightFromDate,Date flightToDate,String serviceType){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(CategorySalesDetails.class);
        criteria.add(Restrictions.ge("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.le("flightDate", tommorow(flightToDate)));
        if(serviceType != null && !serviceType.isEmpty()){
            criteria.add(Restrictions.eq("serviceType", serviceType));
        }
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.sum("quantity"),"quantity");
        projList.add(Projections.sum("price"),"price");
        projList.add(Projections.groupProperty("category"),"category");
        //criteria.setProjection(projList);
        criteria.setProjection(projList);
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getSifDetails(Date flightFromDate,Date flightToDate){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(SIFDetails.class);
        criteria.add(Restrictions.ge("downloaded", yesterday(flightFromDate)));
        criteria.add(Restrictions.le("downloaded", tommorow(flightToDate)));
        //criteria.setProjection(projList);
        List list = criteria.list();
        session.close();
        return list;
    }

    public User getCurrentUser(String userName){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("staffId", userName));
        List userList = criteria.list();
        session.close();
        if(userList != null && !userList.isEmpty()){
            return (User) userList.get(0);
        }
        else {
            return null;
        }
    }

    public String getRoleNameFromRoleId(int roleId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(UserRole.class);
        criteria.add(Restrictions.eq("roleId", roleId));
        List roleList = criteria.list();
        session.close();
        if(roleList != null && !roleList.isEmpty()){
            return ((UserRole) roleList.get(0)).getRoleName();
        }
        else {
            return null;
        }
    }

    public int getRoleIdFromStaffName(String staffName){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("staffName", staffName));
        List roleList = criteria.list();
        session.close();
        if(roleList != null && !roleList.isEmpty()){
            return ((User) roleList.get(0)).getUserRoleId();
        }
        else {
            return 0;
        }
    }

    public List getItemCodesList(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(ItemDetails.class);
        criteria.setProjection(Projections.distinct(Projections.property("itemCode")));
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getCurrencyCodesList(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(CurrencyDetails.class);
        criteria.setProjection(Projections.distinct(Projections.property("currencyCode")));
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getKeyFieldList(String className,String keyField){
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Criteria criteria = session.createCriteria(Class.forName(className));
            criteria.setProjection(Projections.distinct(Projections.property(keyField)));
            List list = criteria.list();
            session.close();
            return list;
        } catch (ClassNotFoundException e) {
            session.close();
            return null;
        }
    }

    public List getCategories(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(ItemDetails.class);
        criteria.setProjection(Projections.distinct(Projections.property("category")));
        List list = criteria.list();
        session.close();
        return list;
    }

    private Date yesterday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, - 1);
        return cal.getTime();
    }

    private Date tommorow(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, + 1);
        return cal.getTime();
    }
}


