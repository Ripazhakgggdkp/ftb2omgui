/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feelthebeats.ftb2omgui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    public void createOSZ(String mp3Path, String difficultyPath) {
        try {
            FileOutputStream fos = new FileOutputStream(difficultyPath + ".osz");
            ZipOutputStream zos = new ZipOutputStream(fos);

            addToZip(mp3Path, "Audio.mp3", zos);
            addToZip(difficultyPath, "Difficulty.osu", zos);

            zos.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println(ex.getCause());
        }
    }

    private void addToZip(String path, String name, ZipOutputStream zos) throws FileNotFoundException, IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(name);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

}
