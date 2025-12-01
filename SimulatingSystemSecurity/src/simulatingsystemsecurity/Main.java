
package simulatingsystemsecurity;
import simulatingsystemsecurity.homePanels.Login;
/**
 *
 * @author Akil Cyrylle
 */
public class Main {
private static User user;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Login login = new Login();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }
    
    public static void setUser(User user){
        Main.user = user;
        
        new PermissionManager();
    }
    
    public static User getUser(){
        return Main.user;
    }
    
    public static void resetUser(){
        Main.user = null;
    
    }
    
}
