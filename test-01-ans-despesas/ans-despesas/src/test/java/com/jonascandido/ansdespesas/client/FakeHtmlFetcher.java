package com.jonascandido.ansdespesas.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class FakeHtmlFetcher implements HtmlFetcher {

    private final Map<String, String> responses = new HashMap<>();

    public void whenGet(String url, String html) {
        responses.put(url, html);
    }

    @Override
    public Document get(String url) {
        String html = responses.get(url);
        if (html == null) {
            throw new RuntimeException("URL não mockada: " + url);
        }
        return Jsoup.parse(html);
    }
}
