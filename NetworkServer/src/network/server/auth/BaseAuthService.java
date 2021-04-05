package network.server.auth;

import network.server.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAuthService implements AuthServise {

//    private static final Map<String, String> USERS = new HashMap() {{
//        put("login1", "pass1");
//        put("login2", "pass2");
//        put("login3", "pass3");
//    }};

//    private static final List<User> USERS = new ArrayList<User>(){{
//        add(new User("login1", "pass1", "user1"));
//        add(new User("login2", "pass2", "user2"));
//        add(new User("login3", "pass3", "user3"));
//    }};

    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;





    @Override
    public void start() {
        try {
            setConnection();
            createDb();
            writeDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Auth service has been started ");
    }

    @Override
    public void stop() {
        try {
            closeDB();
        } catch (SQLException throwables) {
            System.out.println("Close error");
            throwables.printStackTrace();
        }
        System.out.println("Auth service has been finished ");
    }


    public static void setConnection() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:TEST2.s2db");
    }

    public static void createDb() throws SQLException {
        statement = connection.createStatement();
        statement.execute(
                "CREATE TABLE if not exists 'users'" +
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'login' text, 'password' text, 'name' text);");
    }

    public static void writeDB() throws SQLException {
        statement.execute("INSERT INTO 'users' ('login', 'password','name') VALUES ('login1', 'pass1', 'user1')");
        statement.execute("INSERT INTO 'users' ('login', 'password','name') VALUES ('login2', 'pass2', 'user2')");
        statement.execute("INSERT INTO 'users' ('login', 'password','name') VALUES ('login3', 'pass3', 'user3')");
        statement.execute("INSERT INTO 'users' ('login', 'password','name') VALUES ('login4', 'pass4', 'user4')");


    }


    public static void closeDB() throws SQLException {
        resultSet.close();
        statement.close();
        connection.close();
    }



    @Override
    public String getUsernameByLoginAndPassword(String login, String password)  {
        try {
            resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                String login2 = resultSet.getString("login");
                String password2 = resultSet.getString("password");
                String name = resultSet.getString("name");

                if(login2.equals(login) && password2.equals(password)){
                    return name;
                }
            }
        } catch (SQLException throwables) {
            System.out.println("getUsernameByLoginAndPassword error");
            throwables.printStackTrace();
        }
        return null;
    }
}
