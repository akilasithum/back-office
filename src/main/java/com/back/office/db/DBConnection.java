package com.back.office.db;

import com.back.office.entity.*;
import com.back.office.persistence.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.lang.reflect.Method;
import java.util.*;

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
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String,ItemDetails> getItemNoItemDetailsMap(){
        Map<String,ItemDetails> itemDetailsMap = new HashMap<>();
        List<ItemDetails> itemDetails = getAllItems();
        for(ItemDetails item : itemDetails){
            itemDetailsMap.put(item.getItemCode(),item);
        }
        return itemDetailsMap;
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

    public List<?> getAllValuesNoRecrdStatus(String className){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(Class.forName(className));
            List list = criteria.list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public List<?> getSectors(String className){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Criteria criteria = session.createCriteria(Class.forName(className));
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

    public List getSectorsFromFlightType(int flightId,String flightType){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Sector.class);
        criteria.add(Restrictions.eq("flightId", flightId));
        criteria.add(Restrictions.eq("flightType", flightType));
        List list = criteria.list();
        session.close();
        return list;
    }

    public void updateFACommissionRecordStatus(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(FACommissionSetup.class);
        criteria.add(Restrictions.eq("recordStatus", 0));
        List list = criteria.list();
        session.close();
        if(list != null && !list.isEmpty()){
            FACommissionSetup setup = (FACommissionSetup) (list.get(0));
            setup.setRecordStatus(1);
            updateObjectHBM(setup);
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

    public List getSalesDetails(Date flightFromDate,Date flightToDate,String itemId,String serviceType){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(SalesDetails.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightToDate)));
        if(itemId != null && !itemId.isEmpty()){
            int itemIdInt = getItemIdFromItemCode(itemId);
            criteria.add(Restrictions.eq("itemId", itemIdInt));
        }
        if(serviceType != null && !serviceType.isEmpty() && !serviceType.equals("All")){
            criteria.add(Restrictions.eq("serviceType", serviceType));
        }

        List list = criteria.list();
        session.close();
        return list;
    }

    public int getItemIdFromItemCode(String itemCode){

        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(ItemDetails.class);
        criteria.add(Restrictions.eq("itemCode", itemCode));
        List list = criteria.list();
        if(list != null && list.size()> 0){
            ItemDetails item = (ItemDetails) list.get(0);
            return item.getItemId();
        }
        session.close();
        return 0;
    }

    public List getMonthlySales(Date flightFromDate,Date flightToDate,String flightNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(SalesDetails.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightToDate)));
        if(flightNo != null && !flightNo.isEmpty()){
            criteria.add(Restrictions.eq("flightNo", flightNo));
        }

        List list = criteria.list();
        session.close();
        return list;
    }

    public List getFlightPaymentDetails(Date flightFromDate,Date flightToDate,String flightNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(FlightPaymentDetails.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightToDate)));
        if(flightNo != null && !flightNo.isEmpty()){
            criteria.add(Restrictions.eq("flightNo", flightNo));
        }
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getTenderSummary(Date flightFromDate,Date flightToDate,String serviceType){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TenderSummaryObj.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightToDate)));
        if(serviceType != null && !serviceType.isEmpty() && !serviceType.equals("All")){
            criteria.add(Restrictions.eq("serviceType", serviceType));
        }

        List list = criteria.list();
        session.close();
        return list;
    }

    public List getFACommission(Date flightFromDate,Date flightToDate,String flightNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(FACommission.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightToDate)));
        if(flightNo != null && !flightNo.isEmpty() && !flightNo.equalsIgnoreCase("null")){
            criteria.add(Restrictions.eq("flightNo", flightNo));
        }

        List list = criteria.list();
        session.close();
        return list;
    }

    public List getRequestInventory(Date flightFromDate,Date flightToDate){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(RequestInventory.class);
        criteria.add(Restrictions.gt("requestedDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("requestedDate", tommorow(flightToDate)));
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getCCbyFlight(Date flightDate,String sifNo,Object flightNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(CCByFlightObj.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightDate)));
        if(sifNo != null && !sifNo.isEmpty()){
            criteria.add(Restrictions.eq("serviceType", sifNo));
        }
        if(flightNo != null && !flightNo.toString().isEmpty()){
            criteria.add(Restrictions.eq("flightNo", flightNo.toString()));
        }
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getCreditCardSummary(Date flightFromDate,Date flightToDate){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(CCSummaryObj.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightToDate)));
        List list = criteria.list();
        session.close();
        return list;
    }

    public List getCategorySalesDetails(Date flightFromDate,Date flightToDate,String serviceType){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(CategorySalesDetails.class);
        criteria.add(Restrictions.gt("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.lt("flightDate", tommorow(flightToDate)));
        if(serviceType != null && !serviceType.isEmpty()){
            criteria.add(Restrictions.eq("serviceType", serviceType));
        }
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.sum("quantity"),"quantity");
        projList.add(Projections.sum("price"),"price");
        projList.add(Projections.groupProperty("category"),"category");
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

    public List getSifDetailsForDailyFlights(Date flightFromDate,Date flightToDate){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(SIFDetails.class);
        criteria.add(Restrictions.ge("downloaded", yesterday(flightFromDate)));
        criteria.add(Restrictions.le("downloaded", tommorow(flightToDate)));
        criteria.add(Restrictions.isNotNull("packedFor"));
        //criteria.setProjection(projList);
        List list = criteria.list();
        session.close();
        return list;
    }

    public List<SealNo> getSealNumbersFromSifNo(String sifNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(SealNo.class);
        criteria.add(Restrictions.eq("sifNo", sifNo));
        List list = criteria.list();
        session.close();
        return list;
    }

    public List<Cart> getCartNoFromSifNo(String sifNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Cart.class);
        criteria.add(Restrictions.eq("sifNo", sifNo));
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

    public MonthEndInventory getMonthEndInventory(int year,String month){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(MonthEndInventory.class);
        criteria.add(Restrictions.eq("year", year));
        criteria.add(Restrictions.eq("month", month));
        List userList = criteria.list();
        session.close();
        if(userList != null && !userList.isEmpty()){
            return (MonthEndInventory) userList.get(0);
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

    public float getFACommissionPercentage(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(FACommissionSetup.class);
        criteria.add(Restrictions.eq("recordStatus", 0));
        List roleList = criteria.list();
        session.close();
        if(roleList != null && !roleList.isEmpty()){
            return ((FACommissionSetup) roleList.get(0)).getFaCommission();
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

    public List getRequestItems(int inventoryId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(RequestInventoryItem.class);
        criteria.add(Restrictions.eq("reqInventoryId", inventoryId));
        return criteria.list();
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

    public List getPreOrderDetails(Date flightFromDate,Date flightToDate,String serviceType){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(PreOrderDetails.class);
        criteria.add(Restrictions.ge("flightDate", flightFromDate));
        criteria.add(Restrictions.le("flightDate", flightToDate));
        if(serviceType != null && !serviceType.isEmpty() && !serviceType.equals("All")){
            criteria.add(Restrictions.eq("typeOfOrder", serviceType));
        }
        return criteria.list();
    }

    public List getPreOrderItemList(Date flightFromDate,Date flightToDate,String flightNo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(PreOrderDetails.class);
        criteria.add(Restrictions.ge("flightDate", yesterday(flightFromDate)));
        criteria.add(Restrictions.le("flightDate", tommorow(flightToDate)));
        if(flightNo != null && !flightNo.isEmpty() && !flightNo.equals("null")){
            criteria.add(Restrictions.eq("flightNumber", flightNo));
        }
        criteria.setProjection(Projections.property("preOrderId"));
        List<Integer> preOrderIdList = criteria.list();
        if(preOrderIdList == null || preOrderIdList.isEmpty()) return null;
        Criteria criteria1 = session.createCriteria(PreOrderItem.class);
        criteria1.add(Restrictions.in("preOrderId",preOrderIdList));

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.groupProperty("itemNo"));
        projectionList.add(Projections.sum("quantity"));
        criteria1.setProjection(projectionList);

        List<Object[]> results = criteria1.list();
        List<PreOrderItem> items = new ArrayList<>();
        for (Object[] obj : results) {
            PreOrderItem item = new PreOrderItem();
            item.setItemNo(String.valueOf(obj[0]));
            item.setQuantity(Integer.parseInt(String.valueOf(obj[1])));
            items.add(item);
        }

        return items;
    }

    public List getPreOrderItemDetails(int serviceType){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(PreOrderItem.class);
        if(serviceType != 0 ){
            criteria.add(Restrictions.eq("preOrderId", serviceType));
        }

        return criteria.list();
    }

    public List getMessage(String user_name_datamessage,boolean unreadmessage){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Message.class);
        criteria.add(Restrictions.eq("message_to", user_name_datamessage));
        criteria.add(Restrictions.eq("read_un", unreadmessage));
        return criteria.list();
    }

    public List getSentMessage(String user_name_datamessage){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Message.class);
        criteria.add(Restrictions.eq("message_from", user_name_datamessage));
        return criteria.list();
    }

    public List<String> getStaffIdUserNameMap(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("recordStatus", 0));
        criteria.add(Restrictions.eq("active", true));
        List<User> list = criteria.list();
        session.close();
        List<String> staffIdList = new ArrayList<>();
        for(User user : list){
            staffIdList.add(user.getStaffId());
        }
        return staffIdList;
    }

    public List<CurrencyDetails> getCurrencyDetail(String currencyType){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(CurrencyDetails.class);
            criteria.add(Restrictions.eq("currencyCode", currencyType));
            criteria.addOrder(Order.asc("lastUpdateDateTime"));

            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List getItemDetails(String itemNumber, String itemName){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(ItemDetails.class);
            if (itemName!=null&&!itemName.isEmpty()) {
                if(itemName=="itemName") {
                    criteria.add(Restrictions.eq("itemCode", itemNumber));

                }else if(itemNumber=="iteNumbe") {
                    criteria.add(Restrictions.eq("itemName", itemName));

                }else if(itemNumber=="detaSelectNumber"){
                    criteria.add(Restrictions.eq("recordStatus", 0));

                }else {
                    return null;
                }
            }
            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ItemDetails> getItemGross(String serviceTypeList,String category){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(ItemDetails.class);
            criteria.add(Restrictions.eq("serviceType", serviceTypeList));
            if(category != null && !category.isEmpty()) criteria.add(Restrictions.eq("category", category));

            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<OpeningInventory> getOpeningInventory(String sifNO){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(OpeningInventory.class);
            criteria.add(Restrictions.eq("sifNo", sifNO));

            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SIFSheet> getSigSheetList(int sifNO,String serviceType){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(SIFSheet.class);
            criteria.add(Restrictions.eq("sifNo", sifNO));
            criteria.add(Restrictions.eq("serviceType", serviceType));
            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<?> getBudgetDetails(String class1){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Class.forName(class1));

            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FlightSheduleDetail> getFlightShedule(Date fromDate,Date toDate,String baseStation){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(FlightSheduleDetail.class);
                criteria.add(Restrictions.ge("flightDateTime",yesterday(fromDate)));
                criteria.add(Restrictions.le("flightDateTime", tommorow(toDate)));
                if(baseStation != null && !baseStation.isEmpty()) {
                    criteria.add(Restrictions.eq("baseStation", baseStation));
                }
            return criteria.list();
        } catch (Exception e) {

            return null;
        }
    }

    public List<FlightSheduleDetail> getFlightShedule(String filterDate,Date dateListh,Date dateListhTo){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(FlightSheduleDetail.class);
            if(filterDate=="datefully") {
                criteria.add(Restrictions.ge("flightDateTime", dateListh));
                criteria.add(Restrictions.le("flightDateTime", dateExt(dateListh)));
            }else if(filterDate=="datethisgre") {
                criteria.add(Restrictions.ge("flightDateTime", dateListh));
                criteria.add(Restrictions.le("flightDateTime", dateListhTo));

            }else if(filterDate=="datethis") {
                criteria.add(Restrictions.ge("flightDateTime", dateListh));

            }
            else {

            }

            return criteria.list();
        } catch (Exception e) {
            System.out.print(e);

            return null;
        }
    }

    public List<BondMessageDetail> getBondMessageDetail(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(BondMessageDetail.class);
            return criteria.list();
        } catch (Exception e) {

            return null;
        }
    }

    public List<String> getFlightsNoList(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<String> flightsList = new ArrayList<>();
        try
        {
            Criteria criteria = session.createCriteria(Flight.class);
            criteria.add(Restrictions.eq("recordStatus", 0));
            List<Flight> list = criteria.list();
            session.close();
            if(list != null){
                for(Flight flight : list){
                    flightsList.add(flight.getIbFlightNo());
                    flightsList.add(flight.getObFlightNo());
                }
            }
            return flightsList;
        } catch (Exception e) {
            session.close();
            return null;
        }
    }

    public Map<String,ItemDetails> getItemCodeDetailsMap(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(ItemDetails.class);
            criteria.add(Restrictions.ge("recordStatus", 0));

            List<ItemDetails> itemDetails = criteria.list();
            Map<String,ItemDetails> map = new HashMap<>();
            itemDetails.stream().forEach((k)-> map.put(k.getItemCode(),k));
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<Integer,String> getItemIdCodeMap(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(ItemDetails.class);
            criteria.add(Restrictions.ge("recordStatus", 0));

            List<ItemDetails> itemDetails = criteria.list();
            Map<Integer,String> map = new HashMap<>();
            itemDetails.stream().forEach((k)-> map.put(k.getItemId(),k.getItemCode()));
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public List<CartNumber> getCartNumbersFromSIF(String sifNo){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(CartNumber.class);
            criteria.add(Restrictions.eq("sifNo", sifNo));
            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<MonthEndInventoryItem> getMonthEndItems(int inventoryId){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(MonthEndInventoryItem.class);
            criteria.add(Restrictions.eq("monthEndInventoryId", inventoryId));
            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<OpeningInventory> getOpeningInventoryFromSIF(int sifNo,String cartNo){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(OpeningInventory.class);
            criteria.add(Restrictions.eq("sifNo", String.valueOf(sifNo)));
            criteria.add(Restrictions.eq("cartNo", cartNo));
            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<EquipmentDetails> getEquipmentsFromType(List<String> packType){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(EquipmentDetails.class);
            criteria.add(Restrictions.in("packType", packType));
            return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserComment> getFaMessages(String flightNo,Date dateListh,Date dateListhTo){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(UserComment.class);
            criteria.add(Restrictions.ge("flightDate", dateListh));
            criteria.add(Restrictions.le("flightDate", dateListhTo));
            if(flightNo != null) {
                criteria.add(Restrictions.eq("flightNo", flightNo));
            }


            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<EquipmentMasterDetail> getEquipmentMasterDetails(Date dateFrom,Date dateTo){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(EquipmentMasterDetail.class);
            criteria.add(Restrictions.ge("lastUsedDate", dateFrom));
            criteria.add(Restrictions.le("lastUsedDate", dateTo));
            return criteria.list();
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    public List<HHCMaster> getHHCMasterDetails(Date dateFrom,Date dateTo){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(HHCMaster.class);
            criteria.add(Restrictions.ge("lastUsedDate", dateFrom));
            criteria.add(Restrictions.le("lastUsedDate", dateTo));
            return criteria.list();
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    public List<PassengerPurchases> getPassengerPurchase(String filterType,String fliterDate,Date dateFrom,Date dateTo){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(PassengerPurchases.class);
            if(filterType=="allType") {
                criteria.add(Restrictions.ge("deparureDate", dateFrom));
                criteria.add(Restrictions.le("deparureDate", dateTo));
                criteria.add(Restrictions.eq("flightNo", fliterDate));
                return criteria.list();
            }else if(filterType=="dateOnly") {

                criteria.add(Restrictions.ge("deparureDate", dateFrom));
                criteria.add(Restrictions.le("deparureDate", dateTo));
                return criteria.list();


            }else if(filterType=="typeOnly") {
                criteria.add(Restrictions.eq("flightNo", fliterDate));
                return criteria.list();

            }
            else {
                return criteria.list();

            }


        } catch (Exception e) {
            System.out.print(e);

            return null;
        }
    }

    public List<PosItemSaleDetail> getPosItemSale(String class1){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(PosItemSaleDetail.class);
            criteria.add(Restrictions.eq("orderId", class1));

            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ItemDetails> getItemId(int class1){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(ItemDetails.class);
            criteria.add(Restrictions.eq("itemCode", class1));
            return criteria.list();
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getAircraftRegNos(){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(AircraftDetails.class);
            criteria.add(Restrictions.eq("recordStatus", 0));
            List aircarftList = criteria.list();
            List<String> regNoList = new ArrayList<>();
            if(aircarftList != null && !aircarftList.isEmpty()){
                for(AircraftDetails aircraft : (List<AircraftDetails>)aircarftList){
                    regNoList.add(aircraft.getRegistrationNumber());
                }
            }
            return regNoList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<BuildTime> getBuildTimeList(Object flightName,Date fromPaked,Date toPaked){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(BuildTime.class);
        if(flightName != null && !String.valueOf(flightName).isEmpty()){
            criteria.add(Restrictions.eq("packedFor", flightName.toString()));
        }
        if(fromPaked != null && toPaked != null){
            criteria.add(Restrictions.ge("downloaded", fromPaked));
            criteria.add(Restrictions.le("downloaded", toPaked));
        }
        criteria.add(Restrictions.isNotNull("packedFor"));
        return criteria.list();
    }

    public List<WastageDetail> getSoldOut(Object flightName,Object sifList,Date fromPaked,Date toPaked){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(WastageDetail.class);
            criteria.add(Restrictions.eq("quantity",0));
            criteria.add(Restrictions.ge("flightDate", fromPaked));
            criteria.add(Restrictions.le("flightDate", toPaked));
            if(flightName!=null&&!flightName.toString().isEmpty()){
                criteria.add(Restrictions.eq("flightNo", flightName.toString()));
            }
            if(sifList!=null&&!sifList.toString().isEmpty()){
                criteria.add(Restrictions.eq("sifNo", sifList));
            }
            return criteria.list();

        } catch (Exception e) {
            System.out.print(e);

            return null;
        }
    }

    public List<WastageDetail> getWastage(Object flightName,Object sifList,Object fromList,Object toList,Date fromPaked,Date toPaked){
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(WastageDetail.class);
            if(flightName!=null&&!flightName.toString().isEmpty()&&sifList!=null&&!sifList.toString().isEmpty()&&fromList!=null&&!fromList.toString().isEmpty()&&toList!=null&&!toList.toString().isEmpty()) {
                criteria.add(Restrictions.ne("quantity",0));
                criteria.add(Restrictions.ge("flightDate", fromPaked));
                criteria.add(Restrictions.le("flightDate", toPaked));
                criteria.add(Restrictions.eq("sifNo", sifList));
                criteria.add(Restrictions.eq("flightNo", flightName.toString()));
                return criteria.list();
            }else if(sifList!=null&&!sifList.toString().isEmpty()&&fromList!=null&&!fromList.toString().isEmpty()&&toList!=null&&!toList.toString().isEmpty()) {

                criteria.add(Restrictions.ne("quantity",0));
                criteria.add(Restrictions.ge("flightDate", fromPaked));
                criteria.add(Restrictions.le("flightDate", toPaked));
                criteria.add(Restrictions.eq("sifNo", sifList));
                return criteria.list();


            }else if(flightName!=null&&!flightName.toString().isEmpty()&&fromList!=null&&!fromList.toString().isEmpty()&&toList!=null&&!toList.toString().isEmpty()) {
                criteria.add(Restrictions.ne("quantity",0));
                criteria.add(Restrictions.ge("flightDate", fromPaked));
                criteria.add(Restrictions.le("flightDate", toPaked));
                criteria.add(Restrictions.eq("flightNo", flightName.toString()));
                return criteria.list();

            }
            else if(flightName!=null&&!flightName.toString().isEmpty()&&sifList!=null&&!sifList.toString().isEmpty()){
                criteria.add(Restrictions.ne("quantity",0));

                criteria.add(Restrictions.eq("sifNo", sifList));
                criteria.add(Restrictions.eq("flightNo", flightName.toString()));
                return criteria.list();

            }else if(fromList!=null&&!fromList.toString().isEmpty()&&toList!=null&&!toList.toString().isEmpty()) {
                criteria.add(Restrictions.ne("quantity",0));
                criteria.add(Restrictions.ge("flightDate", fromPaked));
                criteria.add(Restrictions.le("flightDate", toPaked));
                return criteria.list();

            }else if(sifList!=null&&!sifList.toString().isEmpty()) {
                criteria.add(Restrictions.ne("quantity",0));

                criteria.add(Restrictions.eq("sifNo", sifList));
                return criteria.list();

            }else if(flightName!=null&&!flightName.toString().isEmpty()) {
                criteria.add(Restrictions.ne("quantity",0));
                criteria.add(Restrictions.eq("flightNo", flightName.toString()));
                return criteria.list();

            }else {
                criteria.add(Restrictions.ne("quantity",0));
                return criteria.list();
            }


        } catch (Exception e) {
            System.out.print(e);

            return null;
        }
    }


    private Date dateExt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, + 2);
        return cal.getTime();
    }
}


