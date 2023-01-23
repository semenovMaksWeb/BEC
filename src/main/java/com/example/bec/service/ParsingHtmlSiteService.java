package com.example.bec.service;

import com.example.bec.enums.ParsingHtmlTypeEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.store.StoreCommandModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class ParsingHtmlSiteService {
    public Element connectSite(String url, String agent, String referrer) throws IOException {
       return Jsoup.connect(url)
                .userAgent(agent)
                .referrer(referrer)
                .get().body();
    }

    public Element connectSite(String url) throws IOException {
        return connectSite(url, "Chrome/4.0.249.0 Safari/532.5", "http://www.google.com");
    }

    public Element selectElement(String select, Element element){
       return element.select(select).get(0);
    }
    public Elements selectElements(String select, Element element){
        return element.select(select);
    }

    public String selectElementText(Element element){
        return element.text();
    }

    public String selectElementAttr(Element element, String attr){
        return element.attr(attr);
    }

    public Object parsingConfig(CommandModel commandModel, StoreCommandModel storeCommandModel) throws IOException {
        Object res = null;
        /* получить весь сайт */
        if (Objects.equals(commandModel.getParsingSite().getType(), ParsingHtmlTypeEnum.connectSite.getTitle())) {
            res = this.connectSite(
                    storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("url")).toString()
            );
        }
        /* получить элементы */
        else if (Objects.equals(commandModel.getParsingSite().getType(), ParsingHtmlTypeEnum.selectElements.getTitle())) {
            res = this.selectElements(
                    storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("select")).toString(),
                    (Element) storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("element"))
            );
        }
        /* получить элемент */
        else if (Objects.equals(commandModel.getParsingSite().getType(), ParsingHtmlTypeEnum.selectElement.getTitle())) {
            res = this.selectElement(
                    storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("select")).toString(),
                    (Element) storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("element"))
            );
        }
        /* получить атрибут элемента */
        else if (Objects.equals(commandModel.getParsingSite().getType(), ParsingHtmlTypeEnum.selectElementAttr.getTitle())) {
            res = this.selectElementAttr(
                    (Element) storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("element")),
                    storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("attr")).toString()
            );
        }
        /* получить текст элемента */
        if (Objects.equals(commandModel.getParsingSite().getType(), ParsingHtmlTypeEnum.selectElementText.getTitle())) {
            res = this.selectElementText(
                    (Element) storeCommandModel.storeGetData(commandModel.getParsingSite().getParams().get("element"))
            );
        }
        return res;
    }
}
