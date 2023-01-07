package xhookman.cursedmod;

import xhookman.cursedmod.soundboard.FilesUtil;
import javax.swing.*;
import java.io.File;

import static xhookman.cursedmod.soundboard.FilesUtil.*;

public class ModLauncher {
        public static void main(String[] args) {
            FilesUtil.createFiles();
            File dir = FilesUtil.getDir();
            System.out.println("Checking files name...");
            checkFilesName(dir);
            System.out.println("Generating new jar...");
            FilesUtil.generateFiles(dir);
            System.out.println("Mod updated! Please delete this jar after you closed it.");
            //show a message box
            String message = "Mod updated! You can delete this jar after you closed it.";
            String title = "Soundboard Updater";
            JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);}
}
