import java.awt.*;
import javax.swing.*;

public class MainGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    private SecurityLayer security = new SecurityLayer();
    private PermissionManager perm = new PermissionManager();

    public LoginFrame() {
        super("Secure System Login");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(18);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password (testing: 12345):"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        roleBox = new JComboBox<>(new String[]{"student", "teacher", "admin"});
        panel.add(roleBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginBtn = new JButton("Login");
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> handleLogin());

        setContentPane(panel);
        pack();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String role = roleBox.getSelectedItem().toString();

        user u = new user(user, role, "12345"); 

        if (!security.checkPassword(u, pass)) {
            JOptionPane.showMessageDialog(this, "Incorrect Password!");
            return;
        }

       
        String code = security.generate2FACode();
        JOptionPane.showMessageDialog(this, "Your 2FA Code: " + code);

        String entered = JOptionPane.showInputDialog(this, "Enter 2FA Code:");

        if (!security.check2FA(code, entered)) {
            JOptionPane.showMessageDialog(this, "2FA Failed!");
            return;
        }

     
        new SystemWindow(u, perm, security);
        dispose();
    }
}

class SystemWindow extends JFrame {

    public SystemWindow(user user, PermissionManager perm, SecurityLayer security) {
        super("Secure System Access");

        setLayout(new FlowLayout());

        add(new JLabel("Welcome: " + user.getUsername()));
        add(new JLabel("Role: " + user.getRole()));
        add(new JLabel("Permissions: " + perm.getPermissions(user.getRole())));

        JButton deleteBtn = new JButton("Attempt DELETE Action");
        add(deleteBtn);

        deleteBtn.addActionListener(e -> {
            if (security.intrusionDetected("DELETE", user.getRole())) {
                JOptionPane.showMessageDialog(this,
                        "Intrusion Detected!\nYou are NOT allowed to DELETE.");
            } else {
                JOptionPane.showMessageDialog(this, "Delete action allowed.");
            }
        });

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

