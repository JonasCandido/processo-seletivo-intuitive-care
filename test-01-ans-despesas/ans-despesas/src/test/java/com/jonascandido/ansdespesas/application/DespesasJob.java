package com.jonascandido.ansdespesas.application;

import com.jonascandido.ansdespesas.client.AnsApiClient;
import com.jonascandido.ansdespesas.downloader.ZipDownloader;
import com.jonascandido.ansdespesas.model.ZipFileInfo;
import com.jonascandido.ansdespesas.model.Trimestre;
import com.jonascandido.ansdespesas.processor.DespesaFileProcessor;
import com.jonascandido.ansdespesas.processor.ZipExtractor;
import com.jonascandido.ansdespesas.util.ZipSelector;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.*;

class DespesasJobTest {

    @Test
    void execute_deveBaixarExtrairEFiltrarUltimos3Zips() throws Exception {

        AnsApiClient client = mock(AnsApiClient.class);
        ZipDownloader downloader = mock(ZipDownloader.class);
        ZipSelector selector = mock(ZipSelector.class);
        ZipExtractor extractor = mock(ZipExtractor.class);
        DespesaFileProcessor processor = mock(DespesaFileProcessor.class);

        ZipFileInfo zip1 = new ZipFileInfo("1T2024.zip", "url1", new Trimestre(2024,1));
        ZipFileInfo zip2 = new ZipFileInfo("2T2024.zip", "url2", new Trimestre(2024,2));
        ZipFileInfo zip3 = new ZipFileInfo("3T2024.zip", "url3", new Trimestre(2024,3));
        ZipFileInfo zip4 = new ZipFileInfo("4T2024.zip", "url4", new Trimestre(2024,4));

        List<ZipFileInfo> todos = List.of(zip1, zip2, zip3, zip4);
        List<ZipFileInfo> ultimos3 = List.of(zip2, zip3, zip4);

        when(client.listarZips()).thenReturn(todos);
        when(selector.ultimosTres(todos)).thenReturn(ultimos3);

        Path dummyExtractedFile = Path.of("temp/file1.csv");
        when(extractor.extract(any(), any())).thenReturn(List.of(dummyExtractedFile));
        when(processor.filterDespesaFiles(List.of(dummyExtractedFile))).thenReturn(List.of(dummyExtractedFile));

        DespesasJob job = new DespesasJob(client, downloader, selector, extractor, processor);

        job.execute();

        verify(downloader).download(zip2);
        verify(downloader).download(zip3);
        verify(downloader).download(zip4);
        verify(downloader, times(3)).download(any());

        for (ZipFileInfo zip : ultimos3) {
            verify(extractor).extract(
                    Path.of("downloads").resolve(zip.nome()),
                    Path.of("temp", zip.nome().replace(".zip", ""))
            );
        }

        verify(processor, times(3)).filterDespesaFiles(List.of(dummyExtractedFile));
    }
}
