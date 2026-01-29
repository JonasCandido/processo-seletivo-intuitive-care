package com.jonascandido.ansdespesas.application;

import com.jonascandido.ansdespesas.client.AnsApiClient;
import com.jonascandido.ansdespesas.downloader.ZipDownloader;
import com.jonascandido.ansdespesas.model.ZipFileInfo;
import com.jonascandido.ansdespesas.util.ZipSelector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesasJob {

    private final AnsApiClient apiClient;
    private final ZipDownloader downloader;
    private final ZipSelector selector;

    public DespesasJob(
            AnsApiClient apiClient,
            ZipDownloader downloader,
            ZipSelector selector
    ) {
        this.apiClient = apiClient;
        this.downloader = downloader;
        this.selector = selector;
    }

    public void execute() {
        List<ZipFileInfo> todos = apiClient.listarZips();
        List<ZipFileInfo> ultimos3 = selector.ultimosTres(todos);

        ultimos3.forEach(downloader::download);
    }
}
