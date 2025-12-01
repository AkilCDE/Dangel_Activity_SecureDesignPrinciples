package simulatingsystemsecurity.homePanels;

//import com.sun.jdi.connect.spi.Connection;
import java.awt.Frame;
import simulatingsystemsecurity.Main;
import simulatingsystemsecurity.database.Db_conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Home extends javax.swing.JFrame {
    public Home(String privilege) {
        initComponents();
          //setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        
        this.setLocationRelativeTo(null);
        AdminprivilegeLabel.setText("" +privilege +"");
        TeacherprivilegeLabel.setText("" + privilege + "");
        StudentprivilegeLabel.setText("" + privilege + "");
         setPanelVisibility(privilege);
          loadUserData();
    }
    
   private void setPanelVisibility(String privilege) {
    switch(privilege) {
        case "STUDENT: VIEW ONLY":
            // Student can only see student panel
            adminPanel.setVisible(false);
            teacherPanel.setVisible(false);
            studentPanel.setVisible(true);
            break;
        case "TEACHER: VIEW + EDIT":
            // Teacher can see teacher and student panels
            adminPanel.setVisible(false);
            teacherPanel.setVisible(true);
            studentPanel.setVisible(true);
            break;
        case "ADMIN: FULL ACCESS":
            // Admin can see all panels
            adminPanel.setVisible(true);
            teacherPanel.setVisible(true);
            studentPanel.setVisible(true);
            break;
    }
}
   
   private void deleteUser(int id) {
    try {
        Connection conn = new Db_conn().con();
        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id);
        
        int rowsAffected = pst.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserData(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete user!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        pst.close();
        conn.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
 private void loadUserData() {
    try {
        Connection conn = new Db_conn().con();
        String currentUser = Main.getUser().getUsername();
        String currentRole = Main.getUser().getRole();
        
        // Create table models for each table
        javax.swing.table.DefaultTableModel adminModel = (javax.swing.table.DefaultTableModel) AdminTable.getModel();
        javax.swing.table.DefaultTableModel teacherModel = (javax.swing.table.DefaultTableModel) TeacherTable.getModel();
        javax.swing.table.DefaultTableModel studentModel = (javax.swing.table.DefaultTableModel) StudentTable.getModel();
        
        // Clear existing data
        adminModel.setRowCount(0);
        teacherModel.setRowCount(0);
        studentModel.setRowCount(0);
        
        // Load data based on user role
        switch (currentRole) {
            case "admin":
                // Admin can see ALL users in ALL tables
                loadAllUsers(adminModel, teacherModel, studentModel, conn);
                break;
            case "teacher":
                // Teacher table shows student accounts + teacher's own account
                // Student table shows only teacher's own account (or can be empty/hidden)
                loadStudentsAndCurrentTeacher(teacherModel, conn);
                loadCurrentTeacherForStudentTable(studentModel, conn, currentUser);
                break;
            case "student":
                // Student can see only themselves in student table
                loadCurrentStudent(studentModel, conn, currentUser);
                break;
        }
        
        conn.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void loadAllUsers(javax.swing.table.DefaultTableModel adminModel, 
                         javax.swing.table.DefaultTableModel teacherModel, 
                         javax.swing.table.DefaultTableModel studentModel, 
                         Connection conn) throws SQLException {
    String query = "SELECT * FROM users";
    PreparedStatement pst = conn.prepareStatement(query);
    ResultSet rs = pst.executeQuery();
    
    while (rs.next()) {
        Object[] row = {
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"), 
            rs.getString("role")
        };
        
        // Add to all tables (admin sees everything in all tables)
        adminModel.addRow(row);
        teacherModel.addRow(row);
        studentModel.addRow(row);
    }
    
    rs.close();
    pst.close();
}

private void loadStudentsAndCurrentTeacher(javax.swing.table.DefaultTableModel teacherModel, 
                                         Connection conn) throws SQLException {
    // Load all student accounts
    String studentQuery = "SELECT * FROM users WHERE role = 'student'";
    PreparedStatement studentPst = conn.prepareStatement(studentQuery);
    ResultSet studentRs = studentPst.executeQuery();
    
    while (studentRs.next()) {
        Object[] studentRow = {
            studentRs.getInt("id"),
            studentRs.getString("username"),
            studentRs.getString("password"), 
            studentRs.getString("role")
        };
        teacherModel.addRow(studentRow);
    }
    studentRs.close();
    studentPst.close();
    
   
}

private void loadCurrentTeacherForStudentTable(javax.swing.table.DefaultTableModel studentModel, 
                                             Connection conn, String currentUser) throws SQLException {
    // Load only current teacher's account for student table
    String query = "SELECT * FROM users WHERE username = ? AND role = 'teacher'";
    PreparedStatement pst = conn.prepareStatement(query);
    pst.setString(1, currentUser);
    ResultSet rs = pst.executeQuery();
    
    if (rs.next()) {
        Object[] row = {
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"), 
            rs.getString("role")
        };
        studentModel.addRow(row);
    }
    
    rs.close();
    pst.close();
}

private void loadCurrentStudent(javax.swing.table.DefaultTableModel studentModel, 
                               Connection conn, String currentUser) throws SQLException {
    // Student can only see their own account in student table
    String query = "SELECT * FROM users WHERE username = ?";
    PreparedStatement pst = conn.prepareStatement(query);
    pst.setString(1, currentUser);
    ResultSet rs = pst.executeQuery();
    
    if (rs.next()) {
        Object[] row = {
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"), 
            rs.getString("role")
        };
        studentModel.addRow(row);
    }
    
    rs.close();
    pst.close();
}
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        adminPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        AdminprivilegeLabel = new javax.swing.JLabel();
        AdminlogoutBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        AdminTable = new javax.swing.JTable();
        AdminAddBtn = new javax.swing.JButton();
        AdminEditBtn = new javax.swing.JButton();
        AdminDeleteBtn = new javax.swing.JButton();
        teacherPanel = new javax.swing.JPanel();
        TeacherDeleteBtn = new javax.swing.JButton();
        TeacherEditBtn = new javax.swing.JButton();
        TeacherAddBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TeacherTable = new javax.swing.JTable();
        TeacherprivilegeLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        TeacherlogoutBtn = new javax.swing.JButton();
        Homebtn = new javax.swing.JButton();
        studentPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        StudentTable = new javax.swing.JTable();
        StudentlogoutBtn = new javax.swing.JButton();
        StudentprivilegeLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        HomeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        adminPanel.setBackground(new java.awt.Color(255, 0, 102));
        adminPanel.setForeground(new java.awt.Color(18, 52, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Least Privilege");

        AdminprivilegeLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AdminprivilegeLabel.setForeground(new java.awt.Color(255, 255, 255));
        AdminprivilegeLabel.setText("-");

        AdminlogoutBtn.setBackground(new java.awt.Color(51, 51, 51));
        AdminlogoutBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AdminlogoutBtn.setForeground(new java.awt.Color(255, 0, 51));
        AdminlogoutBtn.setText("Logout");
        AdminlogoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminlogoutBtnActionPerformed(evt);
            }
        });

        AdminTable.setBackground(new java.awt.Color(255, 0, 102));
        AdminTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AdminTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Username", "Password", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(AdminTable);

        AdminAddBtn.setBackground(new java.awt.Color(51, 204, 0));
        AdminAddBtn.setText("Add");
        AdminAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminAddBtnActionPerformed(evt);
            }
        });

        AdminEditBtn.setBackground(new java.awt.Color(0, 0, 204));
        AdminEditBtn.setText("Edit");
        AdminEditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminEditBtnActionPerformed(evt);
            }
        });

        AdminDeleteBtn.setBackground(new java.awt.Color(255, 0, 0));
        AdminDeleteBtn.setText("Delete");
        AdminDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminDeleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout adminPanelLayout = new javax.swing.GroupLayout(adminPanel);
        adminPanel.setLayout(adminPanelLayout);
        adminPanelLayout.setHorizontalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addGap(234, 234, 234)
                        .addComponent(AdminprivilegeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(adminPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminPanelLayout.createSequentialGroup()
                                .addComponent(AdminAddBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AdminEditBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AdminDeleteBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(AdminlogoutBtn)))))
                .addContainerGap())
        );
        adminPanelLayout.setVerticalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AdminprivilegeLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AdminlogoutBtn)
                    .addComponent(AdminAddBtn)
                    .addComponent(AdminEditBtn)
                    .addComponent(AdminDeleteBtn))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        getContentPane().add(adminPanel, "card2");

        teacherPanel.setBackground(new java.awt.Color(255, 0, 102));

        TeacherDeleteBtn.setBackground(new java.awt.Color(255, 0, 0));
        TeacherDeleteBtn.setText("Delete");
        TeacherDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeacherDeleteBtnActionPerformed(evt);
            }
        });

        TeacherEditBtn.setBackground(new java.awt.Color(0, 0, 204));
        TeacherEditBtn.setText("Edit");
        TeacherEditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeacherEditBtnActionPerformed(evt);
            }
        });

        TeacherAddBtn.setBackground(new java.awt.Color(51, 204, 0));
        TeacherAddBtn.setText("Add");
        TeacherAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeacherAddBtnActionPerformed(evt);
            }
        });

        TeacherTable.setBackground(new java.awt.Color(255, 0, 102));
        TeacherTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TeacherTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Username", "Password", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TeacherTable);

        TeacherprivilegeLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TeacherprivilegeLabel.setForeground(new java.awt.Color(255, 255, 255));
        TeacherprivilegeLabel.setText("-");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Least Privilege");

        TeacherlogoutBtn.setBackground(new java.awt.Color(51, 51, 51));
        TeacherlogoutBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TeacherlogoutBtn.setForeground(new java.awt.Color(255, 0, 51));
        TeacherlogoutBtn.setText("Logout");
        TeacherlogoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeacherlogoutBtnActionPerformed(evt);
            }
        });

        Homebtn.setText("Home");
        Homebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout teacherPanelLayout = new javax.swing.GroupLayout(teacherPanel);
        teacherPanel.setLayout(teacherPanelLayout);
        teacherPanelLayout.setHorizontalGroup(
            teacherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teacherPanelLayout.createSequentialGroup()
                .addGroup(teacherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(teacherPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Homebtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE))
                    .addGroup(teacherPanelLayout.createSequentialGroup()
                        .addGap(234, 234, 234)
                        .addComponent(TeacherprivilegeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(teacherPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(teacherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, teacherPanelLayout.createSequentialGroup()
                                .addComponent(TeacherAddBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TeacherEditBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TeacherDeleteBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TeacherlogoutBtn)))))
                .addContainerGap())
        );
        teacherPanelLayout.setVerticalGroup(
            teacherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teacherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(teacherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(Homebtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TeacherprivilegeLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(teacherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TeacherlogoutBtn)
                    .addComponent(TeacherAddBtn)
                    .addComponent(TeacherEditBtn)
                    .addComponent(TeacherDeleteBtn))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        getContentPane().add(teacherPanel, "card3");

        studentPanel.setBackground(new java.awt.Color(255, 0, 102));

        StudentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Username", "Password", "Role"
            }
        ));
        jScrollPane4.setViewportView(StudentTable);

        StudentlogoutBtn.setBackground(new java.awt.Color(51, 51, 51));
        StudentlogoutBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        StudentlogoutBtn.setForeground(new java.awt.Color(255, 0, 51));
        StudentlogoutBtn.setText("Logout");
        StudentlogoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentlogoutBtnActionPerformed(evt);
            }
        });

        StudentprivilegeLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        StudentprivilegeLabel.setForeground(new java.awt.Color(255, 255, 255));
        StudentprivilegeLabel.setText("-");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Least Privilege");

        HomeBtn.setText("Home");
        HomeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout studentPanelLayout = new javax.swing.GroupLayout(studentPanel);
        studentPanel.setLayout(studentPanelLayout);
        studentPanelLayout.setHorizontalGroup(
            studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentPanelLayout.createSequentialGroup()
                .addGroup(studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(studentPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(HomeBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE))
                    .addGroup(studentPanelLayout.createSequentialGroup()
                        .addGap(234, 234, 234)
                        .addComponent(StudentprivilegeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(studentPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studentPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(StudentlogoutBtn)))))
                .addContainerGap())
        );
        studentPanelLayout.setVerticalGroup(
            studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(HomeBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StudentprivilegeLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StudentlogoutBtn)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        getContentPane().add(studentPanel, "card4");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AdminlogoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminlogoutBtnActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to logout?",
        "Confirm Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
       
        this.dispose();
        
        
        Login login = new Login();
        login.setVisible(true);
         login.setLocationRelativeTo(null);
    }
        //login.setLocationRelativeTo(null);
    }//GEN-LAST:event_AdminlogoutBtnActionPerformed

    private void TeacherlogoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeacherlogoutBtnActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to logout?",
        "Confirm Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
       
        this.dispose();
        
        
        Login login = new Login();
        login.setVisible(true);
         login.setLocationRelativeTo(null);
    }
    }//GEN-LAST:event_TeacherlogoutBtnActionPerformed

    private void StudentlogoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentlogoutBtnActionPerformed
       int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to logout?",
        "Confirm Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
       
        this.dispose();
        
        
        Login login = new Login();
        login.setVisible(true);
         login.setLocationRelativeTo(null);
    }
    }//GEN-LAST:event_StudentlogoutBtnActionPerformed

    private void HomebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomebtnActionPerformed
      setPanelVisibility(AdminprivilegeLabel.getText());
    }//GEN-LAST:event_HomebtnActionPerformed

    private void HomeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeBtnActionPerformed
          setPanelVisibility(StudentprivilegeLabel.getText());
    }//GEN-LAST:event_HomeBtnActionPerformed

    private void TeacherAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeacherAddBtnActionPerformed
    Add_Student addStudent = new Add_Student((Frame) this.getParent(), true);
    addStudent.setVisible(true);
    addStudent.setLocationRelativeTo(this);
    loadUserData();
        
      
    }//GEN-LAST:event_TeacherAddBtnActionPerformed

    private void AdminAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminAddBtnActionPerformed
    Add_User addUser = new Add_User((Frame) this.getParent(), true);
    addUser.setVisible(true);
    addUser.setLocationRelativeTo(this);
    loadUserData();
    }//GEN-LAST:event_AdminAddBtnActionPerformed

    private void AdminEditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminEditBtnActionPerformed
int selectedRow = AdminTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Get data from selected row
    int id = (int) AdminTable.getValueAt(selectedRow, 0);
    String username = (String) AdminTable.getValueAt(selectedRow, 1);
    String password = (String) AdminTable.getValueAt(selectedRow, 2);
    String role = (String) AdminTable.getValueAt(selectedRow, 3);
    
    // Open admin edit dialog
    admin_Edit adminEdit = new admin_Edit((Frame) this.getParent(), true, id, username, password, role);
    adminEdit.setVisible(true);
    adminEdit.setLocationRelativeTo(this);
    
    // Refresh data after editing
    loadUserData();       
    }//GEN-LAST:event_AdminEditBtnActionPerformed

    private void AdminDeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminDeleteBtnActionPerformed
   int selectedRow = AdminTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int id = (int) AdminTable.getValueAt(selectedRow, 0);
    String username = (String) AdminTable.getValueAt(selectedRow, 1);
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete user: " + username + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        deleteUser(id);
    }      
    }//GEN-LAST:event_AdminDeleteBtnActionPerformed

    private void TeacherEditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeacherEditBtnActionPerformed
   int selectedRow = TeacherTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a student to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Get data from selected row
    int id = (int) TeacherTable.getValueAt(selectedRow, 0);
    String username = (String) TeacherTable.getValueAt(selectedRow, 1);
    String password = (String) TeacherTable.getValueAt(selectedRow, 2);
    String role = (String) TeacherTable.getValueAt(selectedRow, 3);
    
    // Teachers can only edit students
    if (!"student".equals(role)) {
        JOptionPane.showMessageDialog(this, "Teachers can only edit student accounts!", "Permission Denied", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Open teacher edit dialog
    teacher_Edit teacherEdit = new teacher_Edit((Frame) this.getParent(), true, id, username, password);
    teacherEdit.setVisible(true);
    teacherEdit.setLocationRelativeTo(this);
    
    // Refresh data after editing
    loadUserData();       
    }//GEN-LAST:event_TeacherEditBtnActionPerformed

    private void TeacherDeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeacherDeleteBtnActionPerformed
        int selectedRow = TeacherTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a student to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int id = (int) TeacherTable.getValueAt(selectedRow, 0);
    String username = (String) TeacherTable.getValueAt(selectedRow, 1);
    String role = (String) TeacherTable.getValueAt(selectedRow, 3);
    
    // Teachers can only delete students
    if (!"student".equals(role)) {
        JOptionPane.showMessageDialog(this, "Teachers can only delete student accounts!", "Permission Denied", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete student: " + username + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        deleteUser(id);
    }
    }//GEN-LAST:event_TeacherDeleteBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AdminAddBtn;
    private javax.swing.JButton AdminDeleteBtn;
    private javax.swing.JButton AdminEditBtn;
    private javax.swing.JTable AdminTable;
    private javax.swing.JButton AdminlogoutBtn;
    private javax.swing.JLabel AdminprivilegeLabel;
    private javax.swing.JButton HomeBtn;
    private javax.swing.JButton Homebtn;
    private javax.swing.JTable StudentTable;
    private javax.swing.JButton StudentlogoutBtn;
    private javax.swing.JLabel StudentprivilegeLabel;
    private javax.swing.JButton TeacherAddBtn;
    private javax.swing.JButton TeacherDeleteBtn;
    private javax.swing.JButton TeacherEditBtn;
    private javax.swing.JTable TeacherTable;
    private javax.swing.JButton TeacherlogoutBtn;
    private javax.swing.JLabel TeacherprivilegeLabel;
    private javax.swing.JPanel adminPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel studentPanel;
    private javax.swing.JPanel teacherPanel;
    // End of variables declaration//GEN-END:variables
}
