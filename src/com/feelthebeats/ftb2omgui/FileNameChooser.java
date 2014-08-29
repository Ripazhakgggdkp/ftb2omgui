/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feelthebeats.ftb2omgui;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Jamil
 */
public class FileNameChooser extends JFileChooser {
    
    private static JFileChooser filechooser;
        
    public void FileNameOpener(JTextField textField) {
        filechooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("FTB Difficulty", "txt");
        filechooser.setFileFilter(filter);
        int status = filechooser.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            textField.setText(filechooser.getSelectedFile().getAbsoluteFile().toString());
        }
    }

    public void FileNameSaver(JTextField textField) {
        filechooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("FTB Difficulty", "txt");

        filechooser.setFileFilter(filter);
        int status = filechooser.showSaveDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            textField.setText(filechooser.getSelectedFile().getAbsoluteFile().toString());
        }
    }
}
