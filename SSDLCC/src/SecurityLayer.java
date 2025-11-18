import java.util.Random;

public class SecurityLayer {

    public boolean checkPassword(user user, String enteredPassword) {
        return user.getPassword().equals(enteredPassword);
    }

    public String generate2FACode() {
        Random r = new Random();
        return String.valueOf(100000 + r.nextInt(900000));
    }

    public boolean check2FA(String real, String entered) {
        return real.equals(entered);
    }

    public boolean intrusionDetected(String action, String role) {
        if (action.equalsIgnoreCase("DELETE") && !role.equals("admin"))
            return true;
        return false;
    }
}

