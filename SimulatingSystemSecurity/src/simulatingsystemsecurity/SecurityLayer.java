package simulatingsystemsecurity;

import simulatingsystemsecurity.database.Db_conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
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
       
        System.out.println("Code: " +authCode);
        showAuthCodeDialog();
    }
    
     private void showAuthCodeDialog() {
       
        String message = "Your authentication code is: \n\n" +
                        "** " + authCode + " **\n\n" +
                        "Click OK to copy this code to your clipboard.";
        
      
        int result = JOptionPane.showConfirmDialog(
            null, 
            message, 
            "Authentication Code", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        
      
        if (result == JOptionPane.OK_OPTION) {
            copyToClipboard(authCode);
            JOptionPane.showMessageDialog(null, 
                "Authentication code copied to clipboard!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void copyToClipboard(String text) {
        try {
            StringSelection stringSelection = new StringSelection(text);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Failed to copy to clipboard: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
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
