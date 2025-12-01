package simulatingsystemsecurity;

import simulatingsystemsecurity.homePanels.Home;

import javax.swing.JOptionPane;

public class PermissionManager {
    public PermissionManager(){
        String role = Main.getUser().getRole();
        Home home;
        
        boolean intrusionDetected = new SecurityLayer().checkIntrution();
        
        if(intrusionDetected) System.exit(0);
        
        switch(role){
            case "student":
                home = new Home("STUDENT: VIEW ONLY");
                home.setVisible(true);
                break;
            case "teacher":
                home = new Home("TEACHER: VIEW + EDIT");
                home.setVisible(true);
                break;
            case "admin":
                home = new Home("ADMIN: FULL ACCESS");
                home.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid Role", "An error occured", JOptionPane.ERROR_MESSAGE);
        }
    }
}
