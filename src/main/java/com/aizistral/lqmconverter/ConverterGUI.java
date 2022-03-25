package com.aizistral.lqmconverter;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConverterGUI extends JFrame {
    public static final String PREFS_KEY_OPEN_LOC = "openLoc";
    public static final String PREFS_KEY_SAVE_LOC = "closeLoc";
    private static final long serialVersionUID = 7872646411017340237L;

    final Preferences prefs = Preferences.userNodeForPackage(ConverterGUI.class);
    private JPanel contentPane;
    private JTextField selectLQMFiles;
    private JTextField selectSaveFolder;
    private JButton browseLQMFiles;
    private JButton browseSaveFolder;
    private JButton btnConvert;
    private JProgressBar progressBar;

    public ConverterGUI() {
        this.setMinimumSize(new Dimension(650, 232));
        this.setTitle("LQM Converter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 650, 232);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.setFocusable(true);
        this.setLocationRelativeTo(null);

        this.selectLQMFiles = new JTextField();
        this.selectLQMFiles.setMargin(new Insets(0, 7, 0, 0));
        this.selectLQMFiles.setHorizontalAlignment(SwingConstants.LEFT);
        this.selectLQMFiles.setFont(new Font("Dialog", Font.PLAIN, 14));
        this.selectLQMFiles.setText("Select .lqm files...");
        this.selectLQMFiles.setColumns(10);

        this.browseLQMFiles = new JButton("Browse");
        this.browseLQMFiles.setFocusable(false);
        this.browseLQMFiles.setFont(new Font("Dialog", Font.BOLD, 14));

        this.browseLQMFiles.addMouseListener(new BrowseListener(this, true, this.selectLQMFiles));

        this.btnConvert = new JButton("Convert!");
        this.btnConvert.setFocusable(false);
        this.btnConvert.setFont(new Font("Dialog", Font.BOLD, 14));

        this.progressBar = new JProgressBar();
        this.progressBar.setFocusable(false);
        this.progressBar.setFont(new Font("Dialog", Font.BOLD, 14));
        this.progressBar.setStringPainted(true);
        this.progressBar.setString("Ready!");

        this.btnConvert.addActionListener(event -> {
            Thread thread = new Thread(() -> {
                try {
                    LQMConverter.convert(ConverterGUI.this.selectLQMFiles.getText(), ConverterGUI.this.selectSaveFolder.getText(), ConverterGUI.this, ConverterGUI.this.progressBar);
                } catch (Exception ex) {
                    LQMConverter.showException(ConverterGUI.this, "Fatal Error:", ex);
                }
            });
            thread.start();
        });

        this.selectSaveFolder = new JTextField();
        this.selectSaveFolder.setText("Select save folder...");
        this.selectSaveFolder.setMargin(new Insets(0, 7, 0, 0));
        this.selectSaveFolder.setHorizontalAlignment(SwingConstants.LEFT);
        this.selectSaveFolder.setFont(new Font("Dialog", Font.PLAIN, 14));
        this.selectSaveFolder.setColumns(10);

        this.browseSaveFolder = new JButton("Browse");
        this.browseSaveFolder.setFont(new Font("Dialog", Font.BOLD, 14));
        this.browseSaveFolder.setFocusable(false);

        this.browseSaveFolder.addMouseListener(new BrowseListener(this, false, this.selectSaveFolder));

        this.selectLQMFiles.setText(this.prefs.get(PREFS_KEY_OPEN_LOC, Paths.get("").toAbsolutePath().toString()));
        this.selectSaveFolder.setText(this.prefs.get(PREFS_KEY_SAVE_LOC, Paths.get("").toAbsolutePath().toString()));

        JLabel lblLqmFiles = new JLabel("LQM Files");
        lblLqmFiles.setHorizontalAlignment(SwingConstants.TRAILING);
        lblLqmFiles.setFont(new Font("Dialog", Font.BOLD, 14));

        JLabel lblSaveFolder = new JLabel("Save Folder");
        lblSaveFolder.setHorizontalAlignment(SwingConstants.TRAILING);
        lblSaveFolder.setFont(new Font("Dialog", Font.BOLD, 14));

        GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                        .addGap(3)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                .addComponent(this.progressBar, GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                                .addComponent(this.btnConvert, GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
                                                .addComponent(lblLqmFiles, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblSaveFolder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                        .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addComponent(this.selectLQMFiles, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                                        .addComponent(this.browseLQMFiles, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addComponent(this.selectSaveFolder, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                                        .addComponent(this.browseSaveFolder, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)))))
                        .addGap(3))
                );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(1)
                                                .addComponent(this.browseLQMFiles, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(1)
                                                .addComponent(this.selectLQMFiles, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                                .addComponent(lblLqmFiles, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addComponent(this.browseSaveFolder, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                        .addGap(1))
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(1)
                                                .addComponent(this.selectSaveFolder, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(1)
                                                .addComponent(lblSaveFolder, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))))
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(this.btnConvert, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(this.progressBar, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addGap(4))
                );

        this.contentPane.setLayout(gl_contentPane);
        this.setVisible(true);
    }

}
