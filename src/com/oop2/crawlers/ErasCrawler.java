package com.oop2.crawlers;

import com.oop2.interfaces.ICrawler;
import com.oop2.util.Config;
import org.jsoup.Jsoup;
import java.io.FileWriter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.oop2.models.Model;
import java.util.HashSet;
import java.util.Set;
import com.oop2.models.EraModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ErasCrawler implements ICrawler {
    public List<Model> crawlPages(String page) {
        String baseUrl = page;
        String thoiKyUrl = "/dong-lich-su";
        List<Model> eraList = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(baseUrl+thoiKyUrl).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //  Lấy các elements trong sidebar
        Elements liElements = doc.selectFirst("ul.mod-articlescategories.categories-module.mod-list").select("li");

        int id = 0;
        for (Element liElement : liElements) {
            thoiKyUrl = liElement.selectFirst("a").attr("href");
            try {
                doc = Jsoup.connect(baseUrl+thoiKyUrl).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(baseUrl+thoiKyUrl);  // Console log

            //  Get eraName
            String eraName = doc.selectFirst("h1").text();

            //  Get eraCode
            String eraCode = thoiKyUrl.substring(thoiKyUrl.lastIndexOf("/") + 1);

            //  Get description
            Elements dElements = doc.selectFirst("div.category-desc.clearfix").select("p");
            ArrayList<String> description = new ArrayList<>();
            for (Element dElement : dElements) {
                description.add(dElement.wholeText());
            }

            //  Get all links related to thoi ky
            ArrayList<String> links = new ArrayList<>();
            String nextPageUrl = "";
            while (nextPageUrl != null) {
                Elements h2Elements = doc.select("div.page-header h2");
                for (Element h2Element : h2Elements) {
                    links.add(h2Element.selectFirst("a").attr("href"));
                }
                Element nextPage = doc.selectFirst("a[aria-label=Đi tới tiếp tục trang].page-link");
                if (nextPage != null) nextPageUrl=nextPage.attr("href");
                else {
                    nextPageUrl = null;
                    continue;
                }
                try {
                    doc = Jsoup.connect(baseUrl+nextPageUrl).get();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Set<String> historicalFiguresLinked = new HashSet<>();
            Set<String> historicalDestinationsLinked = new HashSet<>();
            //  Traverse all links to find relative figures
            for (String link : links) {
                System.out.println(baseUrl+link);
                ArrayList<String> hrefs = getRelatives(baseUrl+link);

                for (String href : hrefs) {
                    String code = href.substring(href.lastIndexOf("/") + 1);
                    if (href.contains("nhan-vat")) {
                        historicalFiguresLinked.add(code);
                    } else if (href.contains("dia-danh")) {
                        historicalDestinationsLinked.add(code);
                    }
                }
            }

            Model era = new EraModel(eraName, description, eraCode
                    , historicalFiguresLinked, historicalDestinationsLinked);
            era.setId(++id);
            eraList.add(era);
        }

        return eraList;
    }

    // Lay thong tin nhan vat tu trang gom nhieu trang con
    private ArrayList<String> getRelatives(String url) {
        ArrayList<String> hrefs = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //  Find in article body
        Element articleBody = doc.selectFirst("div[itemprop=articleBody]");
        if (articleBody != null) {
            Elements aTags = doc.selectFirst("div[itemprop=articleBody]").select("a");
            for (Element aTag : aTags) {
                String href = aTag.attr("href");
                if (href.isEmpty() || href.startsWith("#")) continue;
                hrefs.add(href);
            }
        }

        //  Find in article sidebar
        Element isNhanVat = doc.selectFirst("div.caption > h3 > a");
        if (isNhanVat != null)
            hrefs.add(isNhanVat.attr("href"));
        return hrefs;
    }

    public void writeModel(String fileName, List<Model> models)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(models);
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Testing
    public static void main(String[] args) {
        ErasCrawler test = new ErasCrawler();
        List<Model> models = test.crawlPages(Config.ERA_WEBPAGE);
        test.writeModel(Config.ERA_FILENAME, models);
    }

}