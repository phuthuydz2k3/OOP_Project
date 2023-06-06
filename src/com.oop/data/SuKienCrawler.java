package com.oop.data;

import com.oop.model.SuKienModel;
import com.oop.util.Config;
//import org.json.JSONArray;
//import org.json.JSONObject;
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

public class SuKienCrawler implements ICrawler {
    @Override
    public void crawl() {
        // the URL of the target website's home page
        String baseUrl = "https://nguoikesu.com/tu-lieu/quan-su/tran-ngoc-hoi-dong-da-nam-1789";

        // initializing the list of SuKienModel` data objects
        // that will contain the scraped data
        List<SuKienModel> cacSuKienLichSu = new ArrayList<>();

        // downloading the target website with an HTTP GET request
        Document doc;
        try {
            doc = Jsoup
                    .connect(baseUrl)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements nextElements = doc.select("a.btn.btn-sm.btn-secondary.next");
        String completeUrl = "";
        // looking for the "Next →" HTML element
        while (baseUrl.compareTo(completeUrl)!=0 && (!nextElements.isEmpty())) {
            // getting the "Next →" HTML element
            Element nextElement = nextElements.first();
            // extracting the relative URL of the next page
            String relativeUrl = nextElement.attr("href");
            // building the complete URL of the next page
            completeUrl =  "https://nguoikesu.com" + relativeUrl;
            System.out.println(completeUrl);

            try {
                doc = Jsoup
                        .connect(completeUrl)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                        .get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // retrieving the list of product HTML elements
            // selecting all quote HTML elements
            //Title
            String tenSuKien = doc.selectXpath("//*[@id=\"content\"]/div[2]/div[1]").text();
            tenSuKien = tenSuKien.replace("\u2013", "-");
            SuKienModel skls = new SuKienModel(tenSuKien);

            //Time, Place, Outcomes
            Elements elements = doc.select("table[cellpadding='0'] tr");
            String[] attributeValue = new String[elements.size()];
            int i = 0;
            for (Element element: elements) {
                attributeValue[i] = element.select("td:nth-child(2)").text();
                i++;
            }
            String thoiGian = "";
            String diaDiem = "";
            String ketQua = "";
            if (attributeValue.length!=0){
                thoiGian = attributeValue[0];
//                thoiGian = thoiGian.replace("\u2013", "-");
                diaDiem = attributeValue[1];
//                diaDiem = diaDiem.replace("\u2032", "'");
//                diaDiem = diaDiem.replace("\u2033", "\"");
                ketQua = attributeValue[2];
//                ketQua = ketQua.replace("\u2032", "'");
//                ketQua = ketQua.replace("\u2033", "\"");
                ketQua = ketQua.replaceAll("\"", "″");
                skls.setThoiGian(thoiGian);
                skls.setDiaDiem(diaDiem);
                skls.setKetQua(ketQua);
            }
            skls.setThoiGian(thoiGian);
            skls.setDiaDiem(diaDiem);
            skls.setKetQua(ketQua);

            //Historical Figures
            Set<String> nhanVatLienQuan = new HashSet<>();
            Elements bodyElements = doc.select("div.com-content-article__body");
            Elements refElements = bodyElements.select("a[href*=/nhan-vat/]");
            for (Element refElement : refElements) {
                String name = refElement.attr("href");
                nhanVatLienQuan.add(getCodeFromUrl(name));
            }
            skls.setNhanVatLienQuan(nhanVatLienQuan);

            // Các địa điểm liên quan
            Set<String> diaDiemLienQuan = new HashSet<>();
            Elements cacDiaDiem = bodyElements.select("a[href*=/dia-danh/]");
            for (Element element : cacDiaDiem) {
                String name = element.attr("href");
                diaDiemLienQuan.add(getCodeFromUrl(name));
            }
            skls.setDiaDiemLienQuan(diaDiemLienQuan);
            cacSuKienLichSu.add(skls);
            // looking for the "Next →" HTML element in the new page
            nextElements = doc.select("a.btn.btn-sm.btn-secondary.next");
        }

        // Convert the list of objects to a JSON array
//        JSONArray jsonArray = new JSONArray();
//        for (SuKienModel suKienLichSu : cacSuKienLichSu) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("Tên", suKienLichSu.getTen());
//            jsonObject.put("Thời gian", suKienLichSu.getThoiGian());
//            jsonObject.put("Địa điểm", suKienLichSu.getDiaDiem());
//            jsonObject.put("Kết quả", suKienLichSu.getKetQua());
//            jsonObject.put("Các nhân vật lịch sử liên quan", JSONArray.toJSONString(List.of(suKienLichSu.getNhanVatLienQuan())) );
//            jsonObject.put("Các địa điểm liên quan", JSONArray.toJSONString(List.of(suKienLichSu.getDiaDiemLienQuan())));
//            jsonArray.put(jsonObject);
//        }

        //Testing jsonutils
//        String historyActionsJson = JsonUtils.pojoToJson(cacSuKienLichSu);
//        try (FileWriter fileWriter = new FileWriter("test.json")) {
//            fileWriter.write(historyActionsJson);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Write the JSON array to a file
        try (FileWriter fileWriter = new FileWriter(Config.SU_KIEN_FILENAME)) {
            fileWriter.write(cacSuKienLichSu.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // testing
    public static void main(String[] args) {
        SuKienCrawler test = new SuKienCrawler();
        test.crawl();
    }
}
