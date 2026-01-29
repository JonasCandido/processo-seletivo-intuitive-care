package com.jonascandido.ansdespesas.client;

import com.jonascandido.ansdespesas.model.Trimestre;
import com.jonascandido.ansdespesas.model.ZipFileInfo;
import com.jonascandido.ansdespesas.parser.TrimestreParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AnsApiClient {

    private static final String BASE_URL =
            "https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/";

    public List<ZipFileInfo> listarZips() {
        try {
            List<ZipFileInfo> resultado = new ArrayList<>();

            Document root = Jsoup.connect(BASE_URL).get();

            for (Element link : root.select("a[href]")) {
                String href = link.attr("href");

                if (!href.matches("\\d{4}/")) {
                    continue;
                }

                String anoUrl = BASE_URL + href;
                Document anoDoc = Jsoup.connect(anoUrl).get();

                for (Element zip : anoDoc.select("a[href$=.zip]")) {
                    ZipFileInfo info = criarInfo(zip.attr("href"), anoUrl);
                    if (info != null) {
                        resultado.add(info);
                    }
                }
            }

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar ANS", e);
        }
    }

    private ZipFileInfo criarInfo(String nomeArquivo, String anoUrl) {

        String caminhoCompleto = anoUrl + nomeArquivo;

        Trimestre trimestre =
                TrimestreParser.from(nomeArquivo, caminhoCompleto);

        if (trimestre == null) {
            return null;
        }

        return new ZipFileInfo(
                nomeArquivo,
                caminhoCompleto,
                trimestre
        );
    }
}
