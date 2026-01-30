package com.jonascandido.ansdespesas.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OperadoraApiClientTest {

    private OperadoraApiClient apiClient;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setup() {
        apiClient = new OperadoraApiClient();

        // usa reflexão para injetar o RestTemplate mock
        restTemplateMock = Mockito.mock(RestTemplate.class);
        try {
            var field = OperadoraApiClient.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(apiClient, restTemplateMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetRazaoSocial_normal() {
        OperadoraApiClient.OperadoraResponse resp = new OperadoraApiClient.OperadoraResponse();
        resp.setRazaoSocial("Operadora X");
        resp.setCnpj("12345678000199");

        when(restTemplateMock.getForObject(anyString(), eq(OperadoraApiClient.OperadoraResponse.class)))
                .thenReturn(resp);

        String razao = apiClient.getRazaoSocial("0001");
        assertEquals("Operadora X", razao);
    }

    @Test
    void testGetRazaoSocial_nullOuVazio() {
        OperadoraApiClient.OperadoraResponse resp = new OperadoraApiClient.OperadoraResponse();
        resp.setRazaoSocial("");
        resp.setCnpj("12345678000199");

        when(restTemplateMock.getForObject(anyString(), eq(OperadoraApiClient.OperadoraResponse.class)))
                .thenReturn(resp);

        String razao = apiClient.getRazaoSocial("0001");
        assertEquals("DESCONHECIDO", razao);
    }

    @Test
    void testGetRazaoSocial_excecao() {
        when(restTemplateMock.getForObject(anyString(), eq(OperadoraApiClient.OperadoraResponse.class)))
                .thenThrow(new RuntimeException("API falhou"));

        String razao = apiClient.getRazaoSocial("0001");
        assertEquals("DESCONHECIDO", razao);
    }

    @Test
    void testGetCnpj_normal() {
        OperadoraApiClient.OperadoraResponse resp = new OperadoraApiClient.OperadoraResponse();
        resp.setRazaoSocial("Operadora Y");
        resp.setCnpj("98765432000100");

        when(restTemplateMock.getForObject(anyString(), eq(OperadoraApiClient.OperadoraResponse.class)))
                .thenReturn(resp);

        String cnpj = apiClient.getCnpj("0002");
        assertEquals("98765432000100", cnpj);
    }

    @Test
    void testGetCnpj_nullOuVazio() {
        OperadoraApiClient.OperadoraResponse resp = new OperadoraApiClient.OperadoraResponse();
        resp.setRazaoSocial("Operadora Y");
        resp.setCnpj("");

        when(restTemplateMock.getForObject(anyString(), eq(OperadoraApiClient.OperadoraResponse.class)))
                .thenReturn(resp);

        String cnpj = apiClient.getCnpj("0002");
        assertEquals("00000000000000", cnpj);
    }

    @Test
    void testGetCnpj_excecao() {
        when(restTemplateMock.getForObject(anyString(), eq(OperadoraApiClient.OperadoraResponse.class)))
                .thenThrow(new RuntimeException("API falhou"));

        String cnpj = apiClient.getCnpj("0002");
        assertEquals("00000000000000", cnpj);
    }
}
