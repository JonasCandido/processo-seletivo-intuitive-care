package com.jonascandido.ansdespesas.downloader;

import com.jonascandido.ansdespesas.model.ZipFileInfo;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

@Component
public class ZipDownloader {

    public void download(ZipFileInfo zip) {
        try {
            Path dir = Path.of("downloads");
            Files.createDirectories(dir);

            Path destino = dir.resolve(zip.nome());

            try (InputStream in = new URL(zip.url()).openStream()) {
                Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("Baixado: " + zip.nome());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar " + zip.nome(), e);
        }
    }
}