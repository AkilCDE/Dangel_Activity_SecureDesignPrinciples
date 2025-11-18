package simulatingsystemsecurity;

import simulatingsystemsecurity.database.Db_conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.JOptionPane;

public class SecurityLayer {
    Connection conn = new Db_conn().con();
    
    private String password;
    private String authCode = "";
    public int authTryCount = 0;
    
    public void generateAuthCode(){
        Random rng = new Random();
        for(int i = 0; i <= 4; i++){
            authCode += rng.nextInt(10);
        }
        
        System.out.println("Auth Code: " +authCode);
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public String getAuthCode(){
        return this.authCode;
    }
    
    public boolean checkIntrution(){
        boolean status = false;
        
        try{
            String query = "SELECT Role FROM users WHERE Username = ?";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, Main.getUser().getUsername());
            ResultSet rs = pst.executeQuery();
            rs.next();
            
            if(!Main.getUser().getRole().equals(rs.getString(1))){
                JOptionPane.showMessageDialog(null, "Suspicious Actions Detected! Aborting", "Intrusion Alert", JOptionPane.ERROR_MESSAGE);
                status = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return status;
    }
}
