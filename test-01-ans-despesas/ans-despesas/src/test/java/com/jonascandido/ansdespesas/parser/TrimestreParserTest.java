package com.jonascandido.ansdespesas.parser;

import com.jonascandido.ansdespesas.model.Trimestre;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrimestreParserTest {

    @Test
    void deveExtrairAnoETrimestreDoNomeCompleto() {
        Trimestre t = TrimestreParser.from(
                "1T2025.zip",
                "/demonstracoes_contabeis/2025/1T2025.zip"
        );

        assertThat(t).isNotNull();
        assertThat(t.ano()).isEqualTo(2025);
        assertThat(t.trimestre()).isEqualTo(1);
    }

    @Test
    void deveExtrairTrimestreDoNomeEAnoDoCaminho() {
        Trimestre t = TrimestreParser.from(
                "3-Trimestre.zip",
                "/demonstracoes_contabeis/2023/3-Trimestre.zip"
        );

        assertThat(t).isNotNull();
        assertThat(t.ano()).isEqualTo(2023);
        assertThat(t.trimestre()).isEqualTo(3);
    }

    @Test
    void deveAceitarVariacoesDeFormato() {
        Trimestre t = TrimestreParser.from(
                "2Q_2024_dados.zip",
                "/demonstracoes_contabeis/2024/2Q_2024_dados.zip"
        );

        assertThat(t).isNotNull();
        assertThat(t.ano()).isEqualTo(2024);
        assertThat(t.trimestre()).isEqualTo(2);
    }

    @Test
    void deveRetornarNullQuandoNaoForPossivelExtrair() {
        Trimestre t = TrimestreParser.from(
                "arquivo_qualquer.zip",
                "/demonstracoes_contabeis/arquivo_qualquer.zip"
        );

        assertThat(t).isNull();
    }
}
