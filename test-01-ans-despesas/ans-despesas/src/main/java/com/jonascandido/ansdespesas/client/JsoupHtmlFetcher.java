package com.jonascandido.ansdespesas.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class JsoupHtmlFetcher implements HtmlFetcher {

    @Override
    public Document get(String url) throws Exception {
        return Jsoup.connect(url).get();
    }
}
