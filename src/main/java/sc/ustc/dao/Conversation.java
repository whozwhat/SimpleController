package sc.ustc.dao;

import sc.ustc.bean.OR_class;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class Conversation {

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
                if(field.getType().getName().equals("Integer") )
                //method.invoke(ro,Integer.parseInt(fieldString));
                    field.set(ro,Integer.parseInt(fieldString));
                // 构造查询sql
                for(int i =1;i<propertyList.size();i++) {
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

            // 通过反射机制得到object的id属性
            Class<?> cla = o.getClass();
            Field id = cla.getDeclaredField("id");
            id.setAccessible(true);
            String idString = (String) id.get(o);

            // 解析xml文件得到对应的数据库表
            OR_class orC = Configuration.class_config(cla.getName());
            String table = orC.getTable();
            List<List<String>> propertyList = orC.getPropertyList();
            String tableId = null;
            for (int i = 0; i < propertyList.size(); i++) {
                if (propertyList.get(i).get(0).equals("id")) {
                    tableId = propertyList.get(i).get(1);
                }
            }

            // 连接DB
            Connection conn = getConnection();

            // delete
            String sql = "delete from " + table + " where " + tableId + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idString);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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