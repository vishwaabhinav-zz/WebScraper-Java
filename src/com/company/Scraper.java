package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav on 01/04/17.
 */
class Scraper {

    private static StringBuffer output;
    private static StringBuffer error;

    static void init(String urlString, Integer limit) throws Exception {
        output = new StringBuffer();
        error = new StringBuffer();
        LinkStore.init();
        connectAndGetDoc(urlString, limit, 0);
    }

    private static void connectAndGetDoc(String urlString, Integer limit, Integer current) throws Exception {
        if(current >= limit) {
            return;
        }
        Document doc = Jsoup.connect(urlString).get();
        List<String> urls = getLinks(doc);
        output.append("Links for " + doc.title() + ":" + doc.location() + "\n");

        output.append("----------------------------\n");
        for(String url: urls) {
            output.append(url + "\n");
        }
        output.append("----------------------------\n");

        for(String url: urls) {
            String cleanUrl = url.split("[?]")[0];
            if(!LinkStore.isPresent(cleanUrl)) {
                try {
                    connectAndGetDoc(url, limit, current + 1);
                } catch(IOException e) {
                    error.append("Failed for ").append(urlString).append("With " + e.getMessage()).append("\n");
                }
                LinkStore.addToStore(cleanUrl);
            }
        }
    }

    private static List<String> getLinks(Document doc) {
        Elements links = doc.select("a[href]");
        List<String> linkUrls = new ArrayList<>();
        String url = doc.location();
        for(Element link: links) {
            String linkUrl = link.attr("href");
            if(linkUrl.matches("^(http|https)://.*$")) {
                linkUrls.add(linkUrl);
            } else if(linkUrl.matches("^//.*$")) {
                String protocol = url.split("[:]")[0];
                linkUrls.add(protocol + ":" + linkUrl);
            } else if(linkUrl.matches("^/.*$")) {
                String domain = url.split("[?]")[0];
                linkUrls.add(domain + linkUrl);
            } else if(linkUrl.matches("^[a-zA-Z0-9].*$")) {
                if(!linkUrl.startsWith("javascript:") && !linkUrl.startsWith("tel:")) {
                    String cleanUrl = url.split("[?]")[0];
                    linkUrls.add(cleanUrl + "/" + linkUrl);
                }
            }
        }

        return linkUrls;
    }

    static String getOutput() {
        return output.toString();
    }
    static String getError() {
        return error.toString();
    }
}
