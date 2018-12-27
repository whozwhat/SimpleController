package sc.ustc.dao;

import sc.ustc.bean.OR_class;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class Conversation {

    public  static  boolean insertObject (Object o){
        Class<?> cla = o.getClass();
        OR_class orC = Configuration.class_config(cla.getName());
        String table = orC.getTable();
        List<List<String>> propertyList = orC.getPropertyList();

        StringBuilder sqlb = new StringBuilder();
        sqlb.append("insert into "+table+" values (");

        for(int i = 0;i<propertyList.size();i++){
                sqlb.append("?,");
            }
        sqlb.deleteCharAt(sqlb.length()-1);
        sqlb.append(");");
        String sql = sqlb.toString();
        System.out.println("sql:"+sql);
        try{
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for(int i = 0;i<propertyList.size();i++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyList.get(i).get(0),cla);
                Method method = propertyDescriptor.getReadMethod();
                String value = (String) method.invoke(o);
                preparedStatement.setString(i+1,value);
            }
                return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object getObject(Object o) {

        Class<?> cla = o.getClass();
        OR_class orC = Configuration.class_config(cla.getName());
        System.out.println(cla.getName());
        System.out.println(orC);
        String table = orC.getTable();
        System.out.println(table);
        List<List<String>> propertyList = orC.getPropertyList();

        // 得到object的主属性
            try {
                Field field = cla.getDeclaredField(propertyList.get(0).get(0));
                field.setAccessible(true);
                String fieldString = (String) field.get(o);
                Object ro = cla.newInstance();
                //Method method = cla.getMethod("set"+field.getName(),field.getType());
                System.out.println("field.getType().getName()"+field.getType().getName());
                //method.invoke(ro,Integer.parseInt(fieldString));
                //field.set(ro,fieldString);
                // 构造查询sql
                for(int i =0;i<propertyList.size();i++) {
                    String sql = ("select " + propertyList.get(i).get(1) + " from " + table + " where " + propertyList.get(0).get(1) + "=" + fieldString);
                    Connection conn = getConnection();
                    // select
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        //Method method1 = cla.getMethod("set" + propertyList.get(i).get(0), Class.forName("java.lang." + propertyList.get(i).get(2) + ".class"));
                        //method1.invoke(ro, rs.getString(1));
                        Field field1 = cla.getDeclaredField(propertyList.get(i).get(0));
                        field1.setAccessible(true);
                        field1.set(ro,rs.getString(1));
                    }
                }

                return ro;

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        return null;
    }

    public static boolean deleteObjById(Object o) {

        try {

            Class<?> cla = o.getClass();
            OR_class orC = Configuration.class_config(cla.getName());
            String table = orC.getTable();
            List<List<String>> propertyList = orC.getPropertyList();
            String tableUserID = null;

            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyList.get(0).get(0),cla);
            Method method = propertyDescriptor.getReadMethod();
            //if(propertyDescriptor.getPropertyType().getName().equals("Integer"))
            String userID = (String) method.invoke(o);
            tableUserID = propertyList.get(0).get(1);


            // 连接DB
            Connection conn = getConnection();

            // delete
            String sql = "delete from " + table + " where " + tableUserID + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userID);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }  catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Map<String, String> jdbcMap = Configuration.jdbc_config();
        Class.forName(jdbcMap.get("driver"));
        System.out.println(jdbcMap.get("password"));
        Connection conn = DriverManager.getConnection(jdbcMap.get("url"), jdbcMap.get("userName"), jdbcMap.get("password"));
        return conn;
    }
}