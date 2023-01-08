package xhookman.cursedmod.soundboard;

import java.io.*;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class SoundJsonUtils {
    private static int soundsCount;
    private static ArrayList<String> soundsName;

    public static void addFile(JarOutputStream jos, String name, String data) throws IOException {
        jos.putNextEntry(new JarEntry(name));
        InputStream is = new ByteArrayInputStream(data.getBytes());
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            jos.write(buffer, 0, bytesRead);
        }
        is.close();
    }
    public static String generateSoundsJson(File folder) {
        StringBuilder content = new StringBuilder("{");
        File[] files = folder.listFiles();
        for(int i=0; i<files.length; i++){
            if(files[i].getName().endsWith(".ogg")) {
                String fileName = files[i].getName().split(".ogg")[0];

                content.append("\"").append(fileName).append("\": {\n")
                        .append("    \"sounds\": [\n")
                        .append("      \"cursedmod:").append(fileName).append("\"\n")
                        .append("    ]\n");

                if (i + 1 < files.length)
                    content.append("  },\n");
                else
                    content.append("  }\n");
            }
        }
        content.append("}");
        return content.toString();
    }

    public static int getSoundsCount(){
        return soundsCount;
    }

    public static ArrayList<String> getSoundsName(){
        return soundsName;
    }

    public static void readSoundsJson() throws IOException {
        soundsName = new ArrayList<>();
        InputStream inputStream = SoundboardServer.class.getResourceAsStream("/assets/"+ MOD_ID + "/sounds.json");
        InputStreamReader inputStreamReader;
        if (inputStream != null) {
            inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(MOD_ID + ":")){
                    soundsCount++;
                    soundsName.add(line.split(":")[1].split("\"")[0]);
                }
            }

            reader.close();
            inputStreamReader.close();
            inputStream.close();
        }
    }
}
