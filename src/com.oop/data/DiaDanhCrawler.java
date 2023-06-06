package com.oop.data;

import com.oop.model.DiaDanhModel;
import com.oop.util.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static com.oop.util.UrlDecode.getCodeFromUrl;

public class DiaDanhCrawler implements ICrawler {
    @Override
    public void crawl() {
        String baseUrl = "https://nguoikesu.com/dia-danh/bien-dong";
        // List
        List<DiaDanhModel> diaDanhList = new ArrayList<>();
        Document doc;
        try {
            doc =  Jsoup
                    .connect(baseUrl)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                    .get();
        } catch (IOException e ){
            throw new RuntimeException(e);
        }

        Elements nextElements = doc.select("a.btn.btn-sm.btn-secondary.next");
        String completeUrl = "";

        // Looking for the "Next"
        while (baseUrl.compareTo(completeUrl) != 0 && (!nextElements.isEmpty())) {
            // Getting the "Next"
            Element nextElement = nextElements.first();
            // Extracting the relative URL of the next page
            String relativeUrl = nextElement.attr("href");
            completeUrl = "https://nguoikesu.com" + relativeUrl;
            // Debugger
            System.out.println(completeUrl);
            int maxRetries = 3;
            int retryCount = 0;
            boolean success = false;

            while (!success && retryCount < maxRetries) {
                try {
                    doc = Jsoup
                            .connect(completeUrl)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                            .get();

                    success = true; // Request succeeded
                } catch (IOException e) {
                    System.out.println("Request failed. Retrying...");
                    retryCount++;

                    // Wait for a short duration before retrying
                    try {
                        Thread.sleep(1000); // Adjust the delay as needed
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (!success) {
                throw new RuntimeException("Exceeded maximum retries. Request failed.");
            }


            // Collect data
            // Get title
            String title = null;
            Element titleElement = doc.selectFirst("h2");
            if (titleElement != null) {
                 title = titleElement.text();
            }
            DiaDanhModel diaDanh = new DiaDanhModel(title);

            // Get dia danh code
            diaDanh.setDiaDanhCode(getCodeFromUrl(completeUrl));
            System.out.println(diaDanh.getDiaDanhCode());
            // Get description
            Elements desElements = doc.select("div.com-content-article__body > p");
            for(Element element : desElements) {
                String text = element.text();
                if (text.length() > 50) {
                    diaDanh.setDescription(text);
                    break;
                }
            }

            // Relative person
            Set<String> nhanVatLienQuan = new HashSet<>();
            Elements refElements = desElements.select("a[href*=/nhan-vat/]");
            for (Element refElement : refElements) {
                String name = refElement.attr("href");
                nhanVatLienQuan.add(getCodeFromUrl(name));
            }
            diaDanh.setNhanVatLienQuan(nhanVatLienQuan);
            diaDanhList.add(diaDanh);
            nextElements = doc.select("a.btn.btn-sm.btn-secondary.next");
        }

        // Print all data in json file
        try (FileWriter writer = new FileWriter(Config.DIA_DANH_FILENAME)) {
            writer.write(diaDanhList.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Testing
    public static void main(String[] args) {
        DiaDanhCrawler test = new DiaDanhCrawler();
        test.crawl();
    }
}
