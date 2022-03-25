package com.aizistral.lqmconverter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class BrowseListener extends MouseAdapter {
    private ConverterGUI parent;
    private boolean isOpen;
    private JTextField field;
    private JFileChooser fileChooser;

    public BrowseListener(ConverterGUI parent, boolean isOpen, JTextField field) {
        this.parent = parent;
        this.isOpen = isOpen;
        this.field = field;
        this.fileChooser = new JFileChooser();

        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.fileChooser.setFileHidingEnabled(false);

        this.fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String fileName = f.getName();

                if (BrowseListener.this.isOpen)
                    return f.isDirectory() || fileName.endsWith(".lqm");
                else
                    return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return ".lqm files";
            }
        });

        String key = isOpen ? ConverterGUI.PREFS_KEY_OPEN_LOC : ConverterGUI.PREFS_KEY_SAVE_LOC;
        String savedDir = parent.prefs.get(key, Paths.get("").toAbsolutePath().toString());
        File currentDir = new File(savedDir);

        if (!Paths.get(savedDir).getRoot().toFile().exists()) {
            currentDir = Paths.get("").toAbsolutePath().toFile();
        }

        while (!currentDir.isDirectory()) {
            currentDir = currentDir.getParentFile();
        }

        this.fileChooser.setCurrentDirectory(currentDir);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int returnState;
        if (this.isOpen) {
            returnState = this.fileChooser.showOpenDialog(this.parent);
        } else {
            returnState = this.fileChooser.showSaveDialog(this.parent);
        }
        if (returnState == JFileChooser.APPROVE_OPTION) {
            File file = this.fileChooser.getSelectedFile();
            String path = null;
            try {
                path = file.getCanonicalPath();
            } catch (IOException ex) {
                path = file.getAbsolutePath();
            }

            this.field.setText(path);

            String parentFolder = null;

            if (file.isDirectory()) {
                parentFolder = file.getAbsolutePath();
            } else {
                parentFolder = file.getParentFile() != null ? file.getParentFile().getAbsolutePath() : "";
            }

            if (this.isOpen) {
                this.parent.prefs.put(ConverterGUI.PREFS_KEY_OPEN_LOC, parentFolder);
            } else {
                this.parent.prefs.put(ConverterGUI.PREFS_KEY_SAVE_LOC, parentFolder);
            }

        }
    }

}
