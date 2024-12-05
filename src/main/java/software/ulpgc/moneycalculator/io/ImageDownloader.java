package software.ulpgc.moneycalculator.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageDownloader {

    public static void downloadImage(String urlImagen, String rutaDestino) {
        try {
            URL url = new URL(urlImagen);
            InputStream inputStream = url.openStream();
            Path path = Paths.get(rutaDestino);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
        } catch (IOException e) {
            System.err.println(STR."Error downloading image: \{e.getMessage()}");
        }
    }
}