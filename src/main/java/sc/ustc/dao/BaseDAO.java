package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {

    public BaseDAO(String driver,String url,String userName,String password){
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    protected String driver;
    protected String url;
    protected String userName;
    protected String password;

    public Connection openDBConnection() throws ClassNotFoundException, SQLException {

        System.out.println(driver);

        Class.forName(driver);

        if (userName != null && !userName.isEmpty()) {

            return DriverManager.getConnection(url, userName, password);
        } else {

            return DriverManager.getConnection(url);
        }
    }

    public boolean closeDBConnection(Connection connection) throws SQLException {

        if (connection != null) {
            connection.close();
            return true;
        }
        return false;
    }

    public abstract boolean insert(String sql);

    public abstract boolean delete(String sql);

    public abstract boolean update(String sql);

    public abstract Object query(String sql, String[] args);

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}