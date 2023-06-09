package com.oop2.crawlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.Normalizer;
import com.oop2.interfaces.ICrawler;
import com.oop2.models.Model;
import com.oop2.models.FestivalModel;
import com.oop2.util.Config;

public class FestivalsCrawler implements ICrawler
{
    private Map<String, List<String>> map;

    public FestivalsCrawler()
    {
        map = new HashMap<>();
    }

    public List<Model> crawlPages(String page)
    {
        List<Model> festivals;
        festivals = scrapeTableFestivals(page);
        festivals.addAll(scrapeTinhFestivals(page));

        return festivals;
    }

    private List<Model> scrapeTinhFestivals(String page)
    {
        List<Model> festivals = new ArrayList<>();
        Document doc;

        try
        {
            doc = Jsoup.connect(page)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                            " (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .get();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        Element div = doc.selectFirst("div.mw-parser-output");
        Element ul = div.select("ul").get(10);
        Elements lis = ul.select("li");

        for (Element li : lis)
        {
            String text = li.text();
            String[] texts = text.split(":", 2);
            String location = texts[0];
            String[] provinceFestivals;

            if (location.equals("Hà Tĩnh"))
            {
                provinceFestivals = texts[1].split("\\b((?<!khai\\s)(?<!kết\\s)(?<!chính\\s))(hội đền|đền|hội)\\b");
            }
            else if (location.equals("Hưng Yên"))
            {
                provinceFestivals = texts[1].split("(?<!khai\\s)(lễ hội đình - đền - chùa và hội|Hội|đền|hội|lễ hội)");
            }
            else if (location.equals("Lâm Đồng"))
            {
                provinceFestivals = new String[1];
                provinceFestivals[0] = texts[1].replaceFirst("lễ", "").trim();
            }
            else if (texts[1].contains("khai hội") || texts[1].contains("chính hội")
                    || texts[1].contains("kết hội"))
            {
                provinceFestivals = texts[1].split("\\b((?<!khai\\s)(?<!kết\\s)(?<!chính\\s))(lễ hội|Hội|hội)\\b");
            }
            else
            {
                provinceFestivals = texts[1].split("(Lễ hội|lễ hội|hội|Hội|lễ|Lễ|kỷ niệm|Kỷ niệm|giỗ|Giỗ|ngày giỗ|tết|Tết|ngày|lê hội)");
            }
            for (String provinceFestival : provinceFestivals)
            {
                provinceFestival = provinceFestival.trim();
                if (provinceFestival.equals(""))
                {
                    continue;
                }
                provinceFestival = provinceFestival.replaceAll("[,;]$", "");
                String[] parts;
                parts = provinceFestival.split("\\s+(?=năm|tháng|\\d)", 2);
                String name = parts[0];
                if (map.containsKey(location))
                {
                    if (map.get(location).contains(name))
                    {
                        continue;
                    }
                }
                String time = "không rõ";
                if (parts.length == 2)
                {
                    time = parts[1];
                }
                List<String> description = new ArrayList<>();
                description.add("không rõ");
                String locationCode = convertToCode(location);
                Model festival = new FestivalModel(name, time, location, "", "", description, locationCode);
                festivals.add(festival);
            }
        }

        return festivals;
    }

    private List<Model> scrapeTableFestivals(String page)
    {
        List<Model> festivals = new ArrayList<>();
        Document doc;

        try
        {
            doc = Jsoup.connect(page)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                            " (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .get();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        Elements trs = doc.select("table.prettytable tbody tr");
        trs.remove(0);

        for (Element tr : trs)
        {
            Elements tds = tr.select("td");

            String time = tds.get(0).text();
            String location = tds.get(1).text();

            String name = tds.get(2).text();
            name = name.replaceAll("(Lễ hội|Hội|Tết|Đại hội)", "");
            name = name.trim();

            if (!location.equals("Nhiều quốc gia"))
            {
                String[] parts = location.split(",", 2);
                String province = parts[0];
                if (province.equals(""))
                {
                    break;
                }
                else
                {
                    if (!map.containsKey(province))
                    {
                        ArrayList<String> newA = new ArrayList<>();
                        newA.add(name);
                        map.put(province, newA);
                    }
                    else
                    {
                        map.get(province).add(name);
                    }
                }
            }
            String fistTimeHolding = tds.get(3).text();
            String historicalFigureLinked = tds.get(4).text();
            String description = tds.get(5).text().trim();
            ArrayList<String> others = new ArrayList<>();
            if (!description.equals(""))
            {
                others.add(description);
            }

            String href = "https://vi.wikipedia.org/" + tds.get(2).selectFirst("a").attr("href");
            Document doc2;

            try
            {
                doc2 = Jsoup.connect(href)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                                " (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                        .get();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }

            Element div = doc2.selectFirst("div.mw-parser-output");
            if (div != null)
            {
                Element p = div.selectFirst("p");
                if (p != null)
                {
                    Elements sups = p.select("sup");
                    for (Element sup : sups)
                    {
                        sup.remove();
                    }
                    others.add(p.text().replaceAll("\"", "'"));
                }
            }

            String locationCode = convertToCode(location.split(",")[0]);
            Model festival = new FestivalModel(name, time, location, historicalFigureLinked, fistTimeHolding, others, locationCode);
            festivals.add(festival);
        }

        return festivals;
    }

    private String convertToCode(String str)
    {
        str = str.toLowerCase();
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return str.replaceAll(" ", "-");
    }

    public void writeModel(String fileName, List<Model> models)
    {
        for (Model model : models)
        {
            model.setId(models.indexOf(model) + 1);
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("[");
            for (Model model : models)
            {
                if (models.indexOf(model) == models.size() - 1)
                {
                    writer.write(model.toString());
                }
                else
                {
                    writer.write(model.toString() + ",\n");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        FestivalsCrawler festivalsScraper = new FestivalsCrawler();
        String page = Config.FESTIVAL_WEBPAGE;
        String festivalFilename = Config.FESTIVAL_FILENAME;
        List<Model> festivals;

        festivals = festivalsScraper.crawlPages(page);
        festivalsScraper.writeModel(festivalFilename, festivals);
    }
}
