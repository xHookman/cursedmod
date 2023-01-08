package xhookman.cursedmod;

import xhookman.cursedmod.soundboard.FilesUtil;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import static xhookman.cursedmod.soundboard.FilesUtil.*;

public class ModLauncher {

    private static void removeJar(){ // The batch is launched after 2 seconds
        // Create a bat file to remove the jar
        try {
            File batFile = new File("deleteJar.bat");
            String batContent =
                    "del \"" + getJarPath().substring(getJarPath().lastIndexOf("/")+1) + "\"\n" +
                            "rename " + getNewJarName() + " " + getJarPath().substring(getJarPath().lastIndexOf("/")+1) + "\n" +
                            "del \"deleteJar.bat\"";
            InputStream is = new ByteArrayInputStream(batContent.getBytes());
            OutputStream os = new java.io.FileOutputStream(batFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Executing batch
        try {
            ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", "Start-Sleep -Seconds 2; Start-Process -FilePath 'deleteJar.bat'"); // Obliged because Windows 11 is trash and doing it with a cmd does not work
            System.out.println("Deleting jar...");
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public static void main(String[] args) {
            FilesUtil.createFiles();
            File dir = FilesUtil.getDir();
            System.out.println("Checking files name...");
            checkFilesName(dir);
            System.out.println("Generating new jar...");
            FilesUtil.generateFiles(dir);
            System.out.println("Mod updated! Please delete this jar after you closed it.");
            //show a message box

            String message = "Mod updated! Please delete this jar after you closed it, otherwise Fabric will not load the right mod";
            String title = "Soundboard Updater";
            JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);

            removeJar();
        }
}
