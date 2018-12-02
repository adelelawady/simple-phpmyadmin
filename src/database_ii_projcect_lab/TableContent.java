/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_ii_projcect_lab;

import java.awt.Color;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author adelali
 */
public class TableContent extends javax.swing.JFrame {

    /**
     * Creates new form TableContent
     */
    Statement stmt;
    ResultSet rs;
    String UpdateSql;
    ArrayList<String> Tables = new ArrayList<String>();
    ArrayList<String> Cols = new ArrayList<String>();
    ArrayList<ArrayList<Object>> ALLrows = new ArrayList<ArrayList<Object>>();

    DefaultTableModel model;
    Connection con;
String _DBName;
        String _SelectedTable;
    public TableContent(String DBName, String SelectedTable, boolean ISUPDATEMODE, boolean ISDELETEMODE) throws ClassNotFoundException, SQLException {
        initComponents();
        _DBName=DBName;
        _SelectedTable=SelectedTable;
        this.jLabel2.setVisible(false);
          this.jLabel3.setVisible(false);
           this.jTextField1.setVisible(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         this.setTitle("All Rows of Table : " + DBName + "."+ SelectedTable);
         
         
        if (ISDELETEMODE){
            this.setTitle("SELECT & DELETE ANY ROW :"  + DBName + "."+ SelectedTable);
               this.jLabel2.setText("DELETE MODE");
               jButton1.setVisible(true);
        }else{
             jButton1.setVisible(false);
        }
        
        
        Class.forName("com.mysql.jdbc.Driver");

        con = DriverManager.getConnection(
                MainFrame.ConnectionInfo,  MainFrame.User, MainFrame.password);

        stmt = con.createStatement();

        rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_type = 'base table' AND table_schema='" + DBName + "'");

        while (rs.next()) {

            Tables.add(rs.getString(1));

        }

        rs = stmt.executeQuery("SHOW COLUMNS FROM " + DBName + "." + SelectedTable);

        while (rs.next()) {

            Cols.add(rs.getString(1));

        }

        model = new DefaultTableModel(Cols.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return ISUPDATEMODE;
            }
        };

        rs = stmt.executeQuery("SELECT * FROM " + DBName + "." + SelectedTable);

        while (rs.next()) {
            ArrayList<Object> rows = new ArrayList<Object>();
            for (int i = 0; i < Cols.size(); i++) {
                rows.add(rs.getString(i + 1));
            }
            ALLrows.add(rows);
            model.addRow(rows.toArray());
        }

        this.jTable1.setModel(model);

        this.jLabel1.setText(DBName + " -> " + SelectedTable + " | {" + model.getRowCount() + "} item Found");

        //// Update Part
        if (ISUPDATEMODE) {
            this.setTitle("Click on Any Cell TO stat update :"  + DBName + "."+ SelectedTable);
            this.jTable1.setBorder(new LineBorder(Color.red, 3, true));
            this.jLabel2.setVisible(true);

            jTable1.getModel().addTableModelListener(
                    new TableModelListener() {
                public void tableChanged(TableModelEvent evt) {
                    String Sql = "UPDATE `" + DBName + "`.`" + SelectedTable + "` SET ";
                    for (int i = 0; i < Cols.size(); i++) {
                        Sql = Sql + "`" + Cols.get(i).toString() + "`='" + model.getValueAt(evt.getLastRow(), i).toString() + "'";
                        if (i != Cols.size() - 1) {
                            Sql = Sql + ",";
                        }

                    }
                    Sql = Sql + " WHERE ";
                    for (int i = 0; i < Cols.size(); i++) {
                        Sql = Sql + "`" + Cols.get(i).toString() + "`='" + ALLrows.get(evt.getLastRow()).get(i).toString() + "'";
                        if (i != Cols.size() - 1) {
                            Sql = Sql + " AND ";
                        } else {
                            Sql = Sql + " ; ";
                        }
                    }
                    jTextField1.setText(Sql);
                    try {
                        Connection conN = DriverManager.getConnection(
                                MainFrame.ConnectionInfo,  MainFrame.User, MainFrame.password);

                        stmt = conN.createStatement();
                       int UResult =(stmt.executeUpdate(Sql));
                       if (UResult==1){
                           jLabel3.setVisible(true);
                          
                           jTextField1.setVisible(true);
                       }else{
                             jLabel3.setText("Failed");
                             jLabel3.setForeground(Color.RED);
                       }
                               
                      //  setVisible(false); //you can't see me!
                      //  dispose();
                      
                      jTable1.setEnabled(false);
                        conN.close();
                    } catch (SQLException ex) {
                         jTextField1.setVisible(true);
                         jLabel3.setText("Failed");
                             jLabel3.setForeground(Color.RED);
                        Logger.getLogger(TableContent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            //UPDATE `DEPARTMENT` SET `Mgr_ssn` = '234567889' WHERE `DEPARTMENT`.`Dnumber` = 1;
        } else {

        }
        con.close();
    }

    public void updaterows(String DBName, String SelectedTable) throws SQLException {
        ALLrows.clear();

        rs = stmt.executeQuery("SELECT * FROM " + DBName + "." + SelectedTable);
        while (rs.next()) {
            ArrayList<Object> rows = new ArrayList<Object>();
            for (int i = 1; i < Cols.size(); i++) {
                rows.add(rs.getString(i));
            }
            ALLrows.add(rows);
            model.addRow(rows.toArray());
        }
        con.close();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel1.setText("jLabel1");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 102));
        jLabel2.setText("UPDATE MODE");

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 204, 102));
        jLabel3.setText("Success");

        jButton1.setText("Delete Row");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setText("SQL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3))
                            .addComponent(jButton1))
                        .addGap(0, 231, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      
        
        
          String Sql = "DELETE FROM `" + _DBName + "`.`" + _SelectedTable + "`  ";
                    
                    Sql = Sql + " WHERE ";
                    for (int i = 0; i < Cols.size(); i++) {
                        Sql = Sql + "`" + Cols.get(i) + "`='" + ALLrows.get(jTable1.getSelectedRow()).get(i).toString() + "'";
                        if (i != Cols.size() - 1) {
                            Sql = Sql + " AND ";
                        } else {
                            Sql = Sql + " ; ";
                        }
                    }

                     jTextField1.setText(Sql);
                    try {
                        Connection conN = DriverManager.getConnection(
                                 MainFrame.ConnectionInfo,  MainFrame.User, MainFrame.password);

                        stmt = conN.createStatement();
                       int UResult =(stmt.executeUpdate(Sql));
                       if (UResult==1){
                           jLabel3.setVisible(true);
                            jLabel2.setVisible(true);
                            jTextField1.setVisible(true);
                           ((DefaultTableModel)jTable1.getModel()).removeRow(jTable1.getSelectedRow());
                       }else{
                             jLabel3.setText("Failed");
                             jLabel3.setForeground(Color.RED);
                       }
                               
                      //  setVisible(false); //you can't see me!
                      //  dispose();
                      jButton1.setEnabled(false);
                      jTable1.setEnabled(false);
                        conN.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(TableContent.class.getName()).log(Level.SEVERE, null, ex);
                    }
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TableContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TableContent().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
