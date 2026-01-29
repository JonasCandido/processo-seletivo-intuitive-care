package com.jonascandido.ansdespesas.processor;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class ZipExtractor {

    public List<Path> extract(Path zipPath, Path targetDir) {
        List<Path> extracted = new ArrayList<>();
        try {
            Files.createDirectories(targetDir);

            try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.isDirectory()) continue;

                    Path outPath = targetDir.resolve(entry.getName());
                    Files.createDirectories(outPath.getParent());
                    Files.copy(zis, outPath, StandardCopyOption.REPLACE_EXISTING);
                    extracted.add(outPath);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao extrair ZIP: " + zipPath, e);
        }

        return extracted;
    }
}
