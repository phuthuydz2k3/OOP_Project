package com.oop.data;

import com.oop.model.ThoiKyModel;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.oop.util.UrlDecode.getCodeFromUrl;

public class ThoiKyCrawler implements ICrawler {
    @Override
    public void crawl() {
        String baseUrl = "https://nguoikesu.com/dong-lich-su";
        List<ThoiKyModel> thoiKyList = new ArrayList<>();
        Document doc;
        try {
            doc = (Document) Jsoup
                    .connect(baseUrl)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Lay tat cac link can scrape
        List<String> links = new ArrayList<>();
//        String[] skipLinks = {"bac-thuoc-lan-thu-nhat", "nha-ly-va-nha-trieu", "thoi-ky-xay-nen-tu-chu",
//                                "nam-bac-trieu", "trinh-nguyen-phan-tranh", "phap-thuoc", "viet-nam-dan-chu-cong-hoa"};
        Element linkElement = doc.selectFirst("div.ja-module#Mod88");
        Elements refElement = linkElement.select("a");
        for(Element element : refElement) {
            String refUrl = element.attr("href");
//            boolean check = true;
//            for (String skip : skipLinks) {
//                if (getCodeFromUrl(refUrl).equals(skip)) {
//                    check = false;
//                    break;
//                }
//            }
//            if(!check) continue;
            links.add("https://nguoikesu.com" + refUrl);
        }

        System.out.println(links);
//         Truy cap vao tung link
        for (String link : links) {
            try {
                doc = (Document) Jsoup
                        .connect(link)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                        .get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Element tenThoiKy = doc.selectFirst("h1");
            System.out.println(tenThoiKy.text());
            ThoiKyModel thoiKy = new ThoiKyModel(tenThoiKy.text());
            Elements des = doc.select("div.category-desc");
            thoiKy.setDescription(des.text());
            // Lay nhan vat lien quan
            Set<String> nhanVatLienQuan = new HashSet<>();

        }



    }

    // Testing
    public static void main(String[] args) {
        ThoiKyCrawler test = new ThoiKyCrawler();
        test.crawl();
    }

}

