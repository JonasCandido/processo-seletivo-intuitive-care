package com.jonascandido.ansdespesas.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OperadoraApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getRazaoSocial(String regAns) {
        try {
            String url = "https://www.ans.gov.br/operadoras-entity/v1/operadoras/" + regAns;
            OperadoraResponse resp = restTemplate.getForObject(url, OperadoraResponse.class);
            String razao = (resp != null ? resp.getRazaoSocial() : null);
            return (razao != null && !razao.isEmpty()) ? razao : "DESCONHECIDO";
        } catch (Exception e) {
            System.err.println("Erro ao buscar razão social para REG_ANS " + regAns + ": " + e.getMessage());
            return "DESCONHECIDO";
        }
    }

    public String getCnpj(String regAns) {
        try {
            String url = "https://www.ans.gov.br/operadoras-entity/v1/operadoras/" + regAns;
            OperadoraResponse resp = restTemplate.getForObject(url, OperadoraResponse.class);
            String cnpj = (resp != null ? resp.getCnpj() : null);
            return (cnpj != null && !cnpj.isEmpty()) ? cnpj : "00000000000000";
        } catch (Exception e) {
            System.err.println("Erro ao buscar CNPJ para REG_ANS " + regAns + ": " + e.getMessage());
            return "00000000000000";
        }
    }

    /**
     * Classe interna para mapear o JSON da API
     */
    public static class OperadoraResponse {
        @JsonProperty("razao_social")
        private String razaoSocial;

        @JsonProperty("cnpj")
        private String cnpj;

        public String getRazaoSocial() { return razaoSocial; }
        public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

        public String getCnpj() { return cnpj; }
        public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    }
}
