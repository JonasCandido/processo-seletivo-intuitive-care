package com.jonascandido.ansdespesas.application;

import com.jonascandido.ansdespesas.client.AnsApiClient;
import com.jonascandido.ansdespesas.downloader.ZipDownloader;
import com.jonascandido.ansdespesas.model.ZipFileInfo;
import com.jonascandido.ansdespesas.model.Trimestre;
import com.jonascandido.ansdespesas.util.ZipSelector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

class DespesasJobTest {

    @Test
    void execute_deveBaixarApenasUltimos3Zips() {

        AnsApiClient client = mock(AnsApiClient.class);
        ZipDownloader downloader = mock(ZipDownloader.class);
        ZipSelector selector = mock(ZipSelector.class);

        ZipFileInfo zip1 = new ZipFileInfo("1T2024.zip", "url1", new Trimestre(2024,1));
        ZipFileInfo zip2 = new ZipFileInfo("2T2024.zip", "url2", new Trimestre(2024,2));
        ZipFileInfo zip3 = new ZipFileInfo("3T2024.zip", "url3", new Trimestre(2024,3));
        ZipFileInfo zip4 = new ZipFileInfo("4T2024.zip", "url4", new Trimestre(2024,4));

        List<ZipFileInfo> todos = List.of(zip1, zip2, zip3, zip4);
        List<ZipFileInfo> ultimos3 = List.of(zip2, zip3, zip4);

        when(client.listarZips()).thenReturn(todos);
        when(selector.ultimosTres(todos)).thenReturn(ultimos3);

        DespesasJob job = new DespesasJob(client, downloader, selector);

        job.execute();

        verify(downloader).download(zip2);
        verify(downloader).download(zip3);
        verify(downloader).download(zip4);

        verify(downloader, times(3)).download(any());
    }
}
