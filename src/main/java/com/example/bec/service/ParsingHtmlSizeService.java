package com.example.bec.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ParsingHtmlSizeService {
    public Document connectSite(String url, String agent, String referrer) throws IOException {
       return Jsoup.connect(url)
                .userAgent(agent)
                .referrer(referrer)
                .get();
    }

    public Document connectSite(String url) throws IOException {
        return connectSite(url, "Chrome/4.0.249.0 Safari/532.5", "http://www.google.com");
    }

    public Elements selectElements(String select, Element element){
       return element.select(select);
    }

    public String selectElementText(String select, Element element){
        return element.select(select).text();
    }

    public String selectElementAttr(String select, Element element, String attr){
        return element.select(select).attr(attr);
    }

    public void parsingConfig(){

    }
}
