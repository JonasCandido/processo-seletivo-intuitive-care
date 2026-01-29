package com.jonascandido.ansdespesas.client;

import org.jsoup.nodes.Document;

public interface HtmlFetcher {
    Document get(String url) throws Exception;
}
