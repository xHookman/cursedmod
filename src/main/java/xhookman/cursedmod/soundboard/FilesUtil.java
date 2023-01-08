package xhookman.cursedmod.soundboard;

import xhookman.cursedmod.ModLauncher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static xhookman.cursedmod.soundboard.SoundJsonUtils.generateSoundsJson;

public class FilesUtil {
    private static File soundboardDir;
    private static String newJarName;

    public static void checkFilesName(File dir){
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if(file.getName().endsWith(".ogg")) {
                String soundFileName = file.getName().split(".ogg")[0];
                System.out.println("Ajout de " + soundFileName);
                if (!file.getName().matches("[a-z0-9_]")) {
                    file.renameTo(new File(dir, soundFileName.replaceAll("[^a-z0-9_]+", "")+".ogg"));
                }
            }
        }
    }

    public static void createFiles(){
        soundboardDir = new File("soundboard/");
        if (!soundboardDir.exists()) {
            soundboardDir.mkdir();
        }
    }
    public static File getDir(){
        return soundboardDir;
    }

    public static void addFile(JarOutputStream jos, String name, String path) throws IOException {
        jos.putNextEntry(new JarEntry(name));
        InputStream is = Files.newInputStream(Paths.get(path));
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            jos.write(buffer, 0, bytesRead);
        }
        is.close();
    }

    public static void generateFiles(File folder) {
        // Open the original .jar file for reading
        try {
            JarFile originalJar = new JarFile(getJarPath());

            Random r = new Random();

            // Create a new .jar file for writing
            newJarName = "soundboard" + r.nextLong() + ".jar";
            JarOutputStream jos = new JarOutputStream(new FileOutputStream(newJarName));
            // Iterate through all the entries in the original .jar file
            Enumeration<JarEntry> entries = originalJar.entries();

            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                // If the entry is the file you want to add, don't copy it
                // from the original .jar. Instead, add it to the new .jar
                // using the addFile() method
                if (!entry.getName().startsWith("assets/cursedmod/")) {
                    // copy the entry from the original .jar to the new .jar
                    jos.putNextEntry(new JarEntry(entry.getName()));
                    InputStream is = originalJar.getInputStream(entry);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        jos.write(buffer, 0, bytesRead);
                    }
                    is.close();
                }
            }
            File[] files = folder.listFiles();
            File[] soundsFiles = soundboardDir.listFiles();

            for(int i=0; i<files.length; i++)
                if(files[i].getName().endsWith(".ogg"))
                    addFile(jos, "assets/cursedmod/sounds/" + soundsFiles[i].getName(), soundboardDir.getPath() + "/" + soundsFiles[i].getName());

            SoundJsonUtils.addFile(jos, "assets/cursedmod/sounds.json", generateSoundsJson(folder));

            // Close the streams
            originalJar.close();
            jos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJarPath() {
        return ModLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    public static String getNewJarName() {
        return newJarName;
    }
}
