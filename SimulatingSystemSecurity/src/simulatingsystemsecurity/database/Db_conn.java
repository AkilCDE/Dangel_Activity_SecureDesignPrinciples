package simulatingsystemsecurity.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db_conn {
    public static Connection con(){
        try{
             Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SSS", "root", "");
            return con;
        }catch (SQLException e){
            System.out.println(e);
        }
        catch (ClassNotFoundException e){
            System.out.println(e);
        }
    return null;
    }       
}
