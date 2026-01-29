package com.jonascandido.ansdespesas.client;

import com.jonascandido.ansdespesas.model.ZipFileInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnsApiClientTest {

    private static final String BASE =
            "https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/";

    @Test
    void deveListarZipsDeTodosOsAnosDisponiveis() {

        FakeHtmlFetcher fetcher = new FakeHtmlFetcher();

        fetcher.whenGet(BASE, """
            <html>
              <body>
                <a href="2024/">2024</a>
                <a href="2025/">2025</a>
              </body>
            </html>
        """);

        fetcher.whenGet(BASE + "2024/", """
            <html>
              <body>
                <a href="1T2024.zip">1T2024.zip</a>
                <a href="arquivo_ignorado.txt">arquivo.txt</a>
              </body>
            </html>
        """);

        fetcher.whenGet(BASE + "2025/", """
            <html>
              <body>
                <a href="3-Trimestre.zip">3-Trimestre.zip</a>
              </body>
            </html>
        """);

        AnsApiClient client = new AnsApiClient(fetcher);

        List<ZipFileInfo> zips = client.listarZips();

        assertThat(zips).hasSize(2);

        assertThat(zips)
                .extracting(z -> z.trimestre().ano())
                .containsExactlyInAnyOrder(2024, 2025);

        assertThat(zips)
                .extracting(z -> z.trimestre().trimestre())
                .containsExactlyInAnyOrder(1, 3);
    }
}
