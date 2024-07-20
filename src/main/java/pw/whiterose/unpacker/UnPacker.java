package pw.whiterose.unpacker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;

public class UnPacker {
    public static void main(String[] args) {
        if (args.length != 1) {
            new JFXPanel();
            Platform.runLater(() -> {
                FileChooser d = new FileChooser();
                File file = d.showOpenDialog(null);
                if (file == null) {
                    System.exit(0);
                    return;
                }
                unpackWithException(file.getPath());
            });
            return;
        }
        String path = args[0];
        unpackWithException(path);
    }

    public static void unpackWithException(String path) {
        try {
            unpack(path);
        } catch (Exception e) {
            System.err.println("Error on unpack: " + e);
        }
    }

    public static void unpack(String path) throws IOException {
        File file = new File(path);
        File dir = new File(file.getParentFile(), file.getName() + "_unpacked");

        dir.mkdirs();

        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

        byte[] buffer = new byte[1024];

        while (enumeration.hasMoreElements()) {
            ZipEntry zipentry = enumeration.nextElement();
            File zipEntryFile = new File(dir, zipentry.getName());

            zipEntryFile.getParentFile().mkdirs();
            zipEntryFile.createNewFile();

            InputStream inputStream = zipFile.getInputStream(zipentry);
            FileOutputStream fos = new FileOutputStream(zipEntryFile);

            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
        }
    }
}
