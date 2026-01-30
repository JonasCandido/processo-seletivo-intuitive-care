package com.jonascandido.ansdespesas.application;

import com.jonascandido.ansdespesas.client.AnsApiClient;
import com.jonascandido.ansdespesas.downloader.ZipDownloader;
import com.jonascandido.ansdespesas.model.ZipFileInfo;
import com.jonascandido.ansdespesas.processor.Consolidator;
import com.jonascandido.ansdespesas.processor.DespesaFileProcessor;
import com.jonascandido.ansdespesas.processor.ZipExtractor;
import com.jonascandido.ansdespesas.util.ZipSelector;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class DespesasJob {

    private final AnsApiClient apiClient;
    private final ZipDownloader downloader;
    private final ZipSelector selector;
    private final ZipExtractor extractor;
    private final DespesaFileProcessor processor;
    private final Consolidator consolidator;

    public DespesasJob(
            AnsApiClient apiClient,
            ZipDownloader downloader,
            ZipSelector selector,
            ZipExtractor extractor,
            DespesaFileProcessor processor,
            Consolidator consolidator
    ) {
        this.apiClient = apiClient;
        this.downloader = downloader;
        this.selector = selector;
        this.extractor = extractor;
        this.processor = processor;
        this.consolidator = consolidator;
    }

    public void execute() throws Exception {
        List<ZipFileInfo> todos = apiClient.listarZips();
        List<ZipFileInfo> ultimos3 = selector.ultimosTres(todos);

        List<Path> allDespesaFiles = new ArrayList<>();

        for (ZipFileInfo zip : ultimos3) {
            downloader.download(zip);

            Path targetDir = Path.of("temp", zip.nome().replace(".zip", ""));
            List<Path> extracted = extractor.extract(Path.of("downloads").resolve(zip.nome()), targetDir);

            List<Path> despesaFiles = processor.filterDespesaFiles(extracted);

            allDespesaFiles.addAll(despesaFiles);
        }

        if (!allDespesaFiles.isEmpty()) {
            consolidator.consolidate(allDespesaFiles);
        }
    }
}
