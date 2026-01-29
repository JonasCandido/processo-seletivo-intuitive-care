package com.jonascandido.ansdespesas.util;

import com.jonascandido.ansdespesas.model.ZipFileInfo;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ZipSelector {

    public List<ZipFileInfo> ultimosTres(List<ZipFileInfo> arquivos) {
        return arquivos.stream()
                .sorted(Comparator.comparing(ZipFileInfo::trimestre).reversed())
                .limit(3)
                .toList();
    }
}
