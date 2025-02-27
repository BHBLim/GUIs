/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wooppy.gui.model;

import java.awt.event.ActionListener;
import java.lang.String;
import javax.swing.event.ChangeListener;

/**
 *
 * @author lisab
 */
public class ManualTaggerFrame extends javax.swing.JFrame implements ActionListener, ChangeListener {

    /**
     * Creates new form ManualTaggerFrame
     */
    public ManualTaggerFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jFileChooser1 = new javax.swing.JFileChooser();
        tagChooserInternalFrame = new javax.swing.JInternalFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        tagChooserList = new javax.swing.JList<>();
        tagListLabel = new javax.swing.JLabel();
        queryInternalFrame = new javax.swing.JInternalFrame();
        prevQueryButton = new javax.swing.JButton();
        nextQueryButton = new javax.swing.JButton();
        editQueryToggleButton = new javax.swing.JToggleButton();
        queryTextScrollPane = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        queryNumberLabel = new javax.swing.JLabel();
        retokenizeButton = new javax.swing.JButton();
        saveEditsButton = new javax.swing.JButton();
        tokenInternalFrame = new javax.swing.JInternalFrame();
        editLineToggleButton = new javax.swing.JToggleButton();
        tokenTableScrollPane = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        topMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openQueriesMenuItem = new javax.swing.JMenuItem();
        openEntityTagMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manual Tagger GUI");

        tagChooserInternalFrame.setVisible(true);

        tagChooserList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "1: Preference", "2: Item", "3: Price", "4: Brand" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        tagChooserList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jScrollPane3.setViewportView(tagChooserList);

        tagListLabel.setText("Tag Chooser");

        javax.swing.GroupLayout tagChooserInternalFrameLayout = new javax.swing.GroupLayout(tagChooserInternalFrame.getContentPane());
        tagChooserInternalFrame.getContentPane().setLayout(tagChooserInternalFrameLayout);
        tagChooserInternalFrameLayout.setHorizontalGroup(
            tagChooserInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tagChooserInternalFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tagChooserInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tagListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tagChooserInternalFrameLayout.setVerticalGroup(
            tagChooserInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tagChooserInternalFrameLayout.createSequentialGroup()
                .addComponent(tagListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        queryInternalFrame.setVisible(true);

        prevQueryButton.setText("Previous Query");
        prevQueryButton.addActionListener(this);

        nextQueryButton.setText("Next Query");
        nextQueryButton.addActionListener(this);

        editQueryToggleButton.setText("Toggle Edit");
        editQueryToggleButton.addChangeListener(this);
        editQueryToggleButton.addActionListener(this);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, editQueryToggleButton, org.jdesktop.beansbinding.ELProperty.create("${selected}"), jTextPane1, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        queryTextScrollPane.setViewportView(jTextPane1);

        queryNumberLabel.setText("Query #" + currentQueryNumber + " out of " + numberOfQueries);

        retokenizeButton.setText("Retokenize");

        saveEditsButton.setText("Save Edits");

        javax.swing.GroupLayout queryInternalFrameLayout = new javax.swing.GroupLayout(queryInternalFrame.getContentPane());
        queryInternalFrame.getContentPane().setLayout(queryInternalFrameLayout);
        queryInternalFrameLayout.setHorizontalGroup(
            queryInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(queryInternalFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(queryInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(queryNumberLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prevQueryButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextQueryButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editQueryToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retokenizeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveEditsButton)
                .addContainerGap(84, Short.MAX_VALUE))
            .addComponent(queryTextScrollPane)
        );
        queryInternalFrameLayout.setVerticalGroup(
            queryInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(queryInternalFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(queryNumberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(queryInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prevQueryButton)
                    .addComponent(nextQueryButton)
                    .addComponent(editQueryToggleButton)
                    .addComponent(retokenizeButton)
                    .addComponent(saveEditsButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(queryTextScrollPane)
                .addGap(18, 18, 18))
        );

        tokenInternalFrame.setVisible(true);

        editLineToggleButton.setText("Toggle Edit");
        editLineToggleButton.addActionListener(this);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
            }
        ));
        tokenTableScrollPane.setViewportView(jTable1);

        jLabel1.setText("Line #" + currentLineNumber);

        javax.swing.GroupLayout tokenInternalFrameLayout = new javax.swing.GroupLayout(tokenInternalFrame.getContentPane());
        tokenInternalFrame.getContentPane().setLayout(tokenInternalFrameLayout);
        tokenInternalFrameLayout.setHorizontalGroup(
            tokenInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tokenInternalFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tokenInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tokenInternalFrameLayout.createSequentialGroup()
                        .addComponent(editLineToggleButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(tokenTableScrollPane))
                .addContainerGap())
            .addGroup(tokenInternalFrameLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tokenInternalFrameLayout.setVerticalGroup(
            tokenInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tokenInternalFrameLayout.createSequentialGroup()
                .addComponent(editLineToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(tokenTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        fileMenu.setText("File");

        openQueriesMenuItem.setText("Open Queries");
        openQueriesMenuItem.addActionListener(this);
        fileMenu.add(openQueriesMenuItem);

        openEntityTagMenuItem.setText("Open Entity Tags");
        openEntityTagMenuItem.addActionListener(this);
        fileMenu.add(openEntityTagMenuItem);

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(this);
        fileMenu.add(saveMenuItem);

        topMenuBar.add(fileMenu);

        editMenu.setText("Edit");

        jMenuItem1.setText("Undo");
        jMenuItem1.addActionListener(this);
        editMenu.add(jMenuItem1);

        topMenuBar.add(editMenu);

        helpMenu.setText("Help");
        topMenuBar.add(helpMenu);

        setJMenuBar(topMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tokenInternalFrame)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(queryInternalFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tagChooserInternalFrame))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tagChooserInternalFrame)
                    .addComponent(queryInternalFrame))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tokenInternalFrame)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == prevQueryButton) {
            ManualTaggerFrame.this.prevQueryButtonActionPerformed(evt);
        }
        else if (evt.getSource() == nextQueryButton) {
            ManualTaggerFrame.this.nextQueryButtonActionPerformed(evt);
        }
        else if (evt.getSource() == editQueryToggleButton) {
            ManualTaggerFrame.this.editQueryToggleButtonActionPerformed(evt);
        }
        else if (evt.getSource() == editLineToggleButton) {
            ManualTaggerFrame.this.editLineToggleButtonActionPerformed(evt);
        }
        else if (evt.getSource() == openQueriesMenuItem) {
            ManualTaggerFrame.this.openQueriesMenuItemActionPerformed(evt);
            ManualTaggerFrame.this.openQueriesMenuItemActionPerformed1(evt);
            ManualTaggerFrame.this.openQueriesMenuItemActionPerformed2(evt);
        }
        else if (evt.getSource() == openEntityTagMenuItem) {
            ManualTaggerFrame.this.openEntityTagMenuItemActionPerformed(evt);
        }
        else if (evt.getSource() == saveMenuItem) {
            ManualTaggerFrame.this.saveMenuItemActionPerformed(evt);
            ManualTaggerFrame.this.saveMenuItemActionPerformed1(evt);
        }
        else if (evt.getSource() == jMenuItem1) {
            ManualTaggerFrame.this.jMenuItem1ActionPerformed(evt);
        }
    }

    public void stateChanged(javax.swing.event.ChangeEvent evt) {
        if (evt.getSource() == editQueryToggleButton) {
            ManualTaggerFrame.this.editQueryToggleButtonStateChanged(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void openQueriesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openQueriesMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_openQueriesMenuItemActionPerformed

    private void prevQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevQueryButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prevQueryButtonActionPerformed

    private void nextQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextQueryButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nextQueryButtonActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void openEntityTagMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openEntityTagMenuItemActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        
        
    }//GEN-LAST:event_openEntityTagMenuItemActionPerformed

    private void editLineToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editLineToggleButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editLineToggleButtonActionPerformed

    private void openQueriesMenuItemActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openQueriesMenuItemActionPerformed1
    }//GEN-LAST:event_openQueriesMenuItemActionPerformed1

    private void openQueriesMenuItemActionPerformed2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openQueriesMenuItemActionPerformed2
        try {
            jFileChooser1.showOpenDialog(jFileChooser1);
        } catch (java.awt.HeadlessException e1) {
            e1.printStackTrace();
        }
    }//GEN-LAST:event_openQueriesMenuItemActionPerformed2

    private void saveMenuItemActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed1
        try {
            jFileChooser1.showSaveDialog(jFileChooser1);
        } catch (java.awt.HeadlessException e1) {
            e1.printStackTrace();
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed1

    private void editQueryToggleButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_editQueryToggleButtonStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_editQueryToggleButtonStateChanged

    private void editQueryToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editQueryToggleButtonActionPerformed
    }//GEN-LAST:event_editQueryToggleButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ManualTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManualTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManualTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManualTaggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManualTaggerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JToggleButton editLineToggleButton;
    javax.swing.JMenu editMenu;
    javax.swing.JToggleButton editQueryToggleButton;
    javax.swing.JMenu fileMenu;
    javax.swing.JMenu helpMenu;
    javax.swing.JFileChooser jFileChooser1;
    javax.swing.JLabel jLabel1;
    javax.swing.JMenuItem jMenuItem1;
    javax.swing.JScrollPane jScrollPane3;
    javax.swing.JTable jTable1;
    javax.swing.JTextPane jTextPane1;
    javax.swing.JButton nextQueryButton;
    javax.swing.JMenuItem openEntityTagMenuItem;
    javax.swing.JMenuItem openQueriesMenuItem;
    javax.swing.JButton prevQueryButton;
    javax.swing.JInternalFrame queryInternalFrame;
    javax.swing.JLabel queryNumberLabel;
    javax.swing.JScrollPane queryTextScrollPane;
    javax.swing.JButton retokenizeButton;
    javax.swing.JButton saveEditsButton;
    javax.swing.JMenuItem saveMenuItem;
    javax.swing.JInternalFrame tagChooserInternalFrame;
    javax.swing.JList<String> tagChooserList;
    javax.swing.JLabel tagListLabel;
    javax.swing.JInternalFrame tokenInternalFrame;
    javax.swing.JScrollPane tokenTableScrollPane;
    javax.swing.JMenuBar topMenuBar;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    
    //My own variables
    private int numberOfQueries;
    private int currentQueryNumber;
    private int numberOfLines;
    private int currentLineNumber;

    //Loaded file options
    private String firstLine;
}
