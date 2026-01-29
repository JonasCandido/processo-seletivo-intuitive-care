package com.jonascandido.ansdespesas.parser;

import com.jonascandido.ansdespesas.model.Trimestre;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrimestreParser {

    // 1T2025, 2Q_2024, etc
    private static final Pattern PADRAO_COMPLETO =
            Pattern.compile("(?i)([1-4])[TQ].*?(20\\d{2})");

    // 3-Trimestre, 4_trimestre, etc
    private static final Pattern PADRAO_TRIMESTRE =
            Pattern.compile("(?i)([1-4]).*trimestre");

    // Ano em qualquer parte do caminho
    private static final Pattern PADRAO_ANO =
            Pattern.compile("(20\\d{2})");

    public static Trimestre from(String nomeArquivo, String caminhoCompleto) {

        // tentativa: tudo no nome
        Matcher completo = PADRAO_COMPLETO.matcher(nomeArquivo);
        if (completo.find()) {
            int trimestre = Integer.parseInt(completo.group(1));
            int ano = Integer.parseInt(completo.group(2));
            return new Trimestre(ano, trimestre);
        }

        // tentativa: trimestre no nome + ano no caminho
        Matcher trim = PADRAO_TRIMESTRE.matcher(nomeArquivo);
        Matcher ano = PADRAO_ANO.matcher(caminhoCompleto);

        if (trim.find() && ano.find()) {
            int trimestre = Integer.parseInt(trim.group(1));
            int anoExtraido = Integer.parseInt(ano.group(1));
            return new Trimestre(anoExtraido, trimestre);
        }

        return null;
    }
}
