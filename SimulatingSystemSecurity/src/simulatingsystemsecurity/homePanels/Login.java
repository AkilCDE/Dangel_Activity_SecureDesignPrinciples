package simulatingsystemsecurity.homePanels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import simulatingsystemsecurity.database.Db_conn;
import simulatingsystemsecurity.Main;

import simulatingsystemsecurity.SecurityLayer;
import simulatingsystemsecurity.User;

public class Login extends javax.swing.JFrame {
    Connection conn = new Db_conn().con();
    private SecurityLayer securityLayer = new SecurityLayer();
    
    public Login() {
        initComponents();
       // setUndecorated(true);
    //setUndecorated(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        submitBtn = new javax.swing.JButton();
        pwd = new javax.swing.JPasswordField();
        showPass = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 0, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LOGIN");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Username");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Password");

        submitBtn.setBackground(new java.awt.Color(0, 0, 204));
        submitBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submitBtn.setForeground(new java.awt.Color(255, 255, 255));
        submitBtn.setText("Login");
        submitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBtnActionPerformed(evt);
            }
        });

        showPass.setBackground(new java.awt.Color(255, 0, 102));
        showPass.setText("Show Pass");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(showPass)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(submitBtn)
                .addGap(87, 87, 87))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(48, 48, 48)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(showPass)
                .addGap(27, 27, 27)
                .addComponent(submitBtn)
                .addGap(23, 23, 23))
        );

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBtnActionPerformed
        String usrname = username.getText();
        String password = pwd.getText();
        
        
        if(usrname.isEmpty() || password.isEmpty()){
            if(usrname.isEmpty()) username.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0x990000)));
            if(password.isEmpty()) pwd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0x990000)));
            JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            String query = "SELECT * FROM users WHERE Username = ?";
            
            try{
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, usrname);
                ResultSet rs = pst.executeQuery();
                
         if(rs.next()){
                    String username = rs.getString(2);
                    securityLayer.setPassword(rs.getString(3));
                    String role = rs.getString(4);
                    
           if(pwd.getText().equals(securityLayer.getPassword())){
                        securityLayer.generateAuthCode();
                        String auth;
                        
              do {
                      if(securityLayer.authTryCount == 3){
                                JOptionPane.showMessageDialog(this, " Abort!", "Retry Count Reached", JOptionPane.ERROR_MESSAGE);
                                System.exit(0);
                            }
                            
                            auth = JOptionPane.showInputDialog(this, "Enter Code", "Authorization Reuired [ Retry Left: " +(4 - securityLayer.authTryCount) +" ]", JOptionPane.INFORMATION_MESSAGE);
                            securityLayer.authTryCount++;
     } while (!auth.equals(securityLayer.getAuthCode()));
                       
                        User user = new User(username, role);
                        Main.setUser(user);
                        this.dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Password is Incorrect!", "Password Error!",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "No Account Found ", "Invalid Account", JOptionPane.INFORMATION_MESSAGE);
                }
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, "copy code");
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_submitBtnActionPerformed

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
      if (showPass.isSelected()) {
        pwd.setEchoChar((char) 0); 
    } else {
        pwd.setEchoChar('•');
    }
    }//GEN-LAST:event_showPassActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField pwd;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JButton submitBtn;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
