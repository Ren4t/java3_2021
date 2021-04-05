package network.server.auth;

import java.sql.SQLException;

public interface AuthServise {


    default  void  start(){}

    String getUsernameByLoginAndPassword(String login,String password) throws SQLException;



    default void stop(){}
}
