/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.seu.iws.gspan;

import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Limited
 */
public class SingleGraphFrame extends javax.swing.JFrame {

    private File myfile;
    private ArrayList<ChildGraph> Graphs;
    private Integer indexGraphs;
    private ArrayList<String> relationships;
    private ArrayList<String> names;
    private ArrayList<String> pictures;

    /**
     * Creates new form SingleGraphFrame
     */
    public SingleGraphFrame() {
        initComponents();
    }

    public SingleGraphFrame(File file) {
        initComponents();
        myfile = file;
        Graphs = new ArrayList<ChildGraph>();
        indexGraphs = -1;
        relationships = new ArrayList<String>();
        names = new ArrayList<String>();
        pictures = new ArrayList<String>();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnNext = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblIndex = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Đồ thị riêng");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNext.setText("Next graph");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Đồ thị:");

        lblIndex.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblIndex.setForeground(new java.awt.Color(0, 102, 102));
        lblIndex.setText("0");

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(204, 0, 0));
        lblTotal.setText("/0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(lblIndex)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNext)
                    .addComponent(jLabel1)
                    .addComponent(lblIndex)
                    .addComponent(lblTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        /// Show first graph
        indexGraphs++;

        if (indexGraphs == Graphs.size()) {
            indexGraphs = 0;
            JOptionPane.showMessageDialog(null, "Trở lại Đồ thị đầu tiên", "Mở đồ thị", JOptionPane.INFORMATION_MESSAGE);
            lblIndex.setText("" + (indexGraphs + 1));
        }

        if (indexGraphs < Graphs.size()) {
            lblIndex.setText("" + (indexGraphs + 1));
            Graphics g;
            g = jPanel1.getGraphics();
            g.clearRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
            jPanel1.setOpaque(true);
            MyGraphics g2d = new MyGraphics(g);

            ArrayList<String> result = new ArrayList<String>();

            List<Integer> diem;
            diem = new ArrayList<Integer>();
            Point[] luudiem;
            luudiem = new Point[50];
            for (int i = 0; i < luudiem.length; i++) {
                luudiem[i] = new Point(0, 0);
            }

            for (String Point : Graphs.get(indexGraphs).Points) {
                result.clear();
                String[] splitread = Point.split(" ");
                for (String aa : splitread) {
                    result.add(aa);
                }

                int id = Integer.parseInt(result.get(1));
                int label = Integer.parseInt(result.get(2));
                diem.add(id);
                ///g2d.VeDiem(luudiem, id, label);
                g2d.DrawPoint(luudiem, id, names.get(id), pictures.get(id));
            }

            for (String Vector : Graphs.get(indexGraphs).Vectors) {
                result.clear();
                String[] splitread = Vector.split(" ");
                for (String aa : splitread) {
                    result.add(aa);
                }

                int from = Integer.parseInt(result.get(1));
                int to = Integer.parseInt(result.get(2));
                int elabel = Integer.parseInt(result.get(3));
                String strlabel = relationships.get(elabel - 1);
                g2d.VeCanh(luudiem[from], luudiem[to], strlabel);
            }
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (myfile != null) {
            try {
                // TODO add your handling code here:

                FileReader reader = new FileReader(myfile);
                BufferedReader buffer = new BufferedReader(reader);

                ArrayList<String> lines = new ArrayList<String>();
                String readline;

                while ((readline = buffer.readLine()) != null) {
                    lines.add(readline);
                }

                /// Read Relationships
                int index = 0;
                while (!("rrr".equals(lines.get(index)))) {
                    relationships.add(lines.get(index));
                    index++;
                }
                index++;

                /// Read Names
                while (!("nnn".equals(lines.get(index)))) {
                    names.add(lines.get(index));
                    index++;
                }

                index++;

                /// Read Pictures' Links
                while (!("ppp".equals(lines.get(index)))) {
                    pictures.add(lines.get(index));
                    index++;
                }
                index++;

                /// Save to Graphs
                ///index = 0;

                while (index < lines.size()) {
                    String line = lines.get(index);

                    if (line.charAt(0) != 't') {

                    } else /// line[0] == 't'
                    {
                        ChildGraph childGraph = new ChildGraph();
                        childGraph.t = line;
                        index++;

                        while ((index < lines.size()) && (lines.get(index).charAt(0) != 't')) {
                            if (lines.get(index).charAt(0) == 'v') {
                                childGraph.Points.add(lines.get(index));
                            }

                            if (lines.get(index).charAt(0) == 'e') {
                                childGraph.Vectors.add(lines.get(index));
                            }

                            index++;
                        }

                        if (!Graphs.contains(childGraph)) {
                            Graphs.add(childGraph);
                        }

                        index--;
                    }

                    index++;
                }

                /// Show first graph
                if (Graphs.size() >= 1) {
                    indexGraphs = 0;
                    lblIndex.setText("" + (indexGraphs + 1));
                    lblTotal.setText("/" + Graphs.size());

                    Graphics g;
                    g = jPanel1.getGraphics();
                    MyGraphics g2d = new MyGraphics(g);

                    ArrayList<String> result = new ArrayList<String>();

                    List<Integer> diem;
                    diem = new ArrayList<Integer>();
                    Point[] luudiem;
                    luudiem = new Point[50];
                    for (int i = 0; i < luudiem.length; i++) {
                        luudiem[i] = new Point(0, 0);
                    }

                    for (String Point : Graphs.get(0).Points) {
                        result.clear();
                        String[] splitread = Point.split(" ");
                        for (String aa : splitread) {
                            result.add(aa);
                        }

                        int id = Integer.parseInt(result.get(1));
                        int label = Integer.parseInt(result.get(2));
                        diem.add(id);
                        ///g2d.VeDiem(luudiem, id, label);
                        g2d.DrawPoint(luudiem, id, names.get(id), pictures.get(id));
                    }

                    for (String Vector : Graphs.get(0).Vectors) {
                        result.clear();
                        String[] splitread = Vector.split(" ");
                        for (String aa : splitread) {
                            result.add(aa);
                        }

                        int from = Integer.parseInt(result.get(1));
                        int to = Integer.parseInt(result.get(2));
                        int elabel = Integer.parseInt(result.get(3));
                        String strlabel = relationships.get(elabel - 1);
                        g2d.VeCanh(luudiem[from], luudiem[to], strlabel);
                    }
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(SingleGraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SingleGraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(SingleGraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SingleGraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SingleGraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SingleGraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SingleGraphFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblIndex;
    private javax.swing.JLabel lblTotal;
    // End of variables declaration//GEN-END:variables
}
