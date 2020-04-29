import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

import static jdk.internal.net.http.HttpRequestImpl.USER_AGENT;

public class SpiderLeg {
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;

    public boolean crawl(String nextUrl){
        try {
            Connection connection = Jsoup.connect(nextUrl).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-UK; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com");
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;

            System.out.println("Received web page at " + nextUrl);

            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
        } catch (IOException e) {
            System.out.println("Error in HTTP request " + e);
            return false;
        }
        return true;
    }

    public boolean searchForWord(String word) {
        System.out.println("Searching for '" + word + "'...");
        String[] words = breakSentence(word);
        boolean[] booleans = new boolean[words.length];

        String bodyText = this.htmlDocument.body().text();

        for(int i = 0; i < words.length; i++) {
            if(bodyText.toLowerCase().contains(words[i].toLowerCase())) {
                booleans[i] = true;
            } else {
                booleans[i] = false;
            }
        }

        for(int j = 0; j < booleans.length; j++) {
            if(!booleans[j]) {
                return false;
            }
        }

        return true;
    }

    public String searchForPrice() {
        String bodyText = this.htmlDocument.body().text();
        char[] textArr = bodyText.toCharArray();
        /*if(bodyText.contains("£") || bodyText.contains("$") || bodyText.contains("€")) {
            int psign = -1;
            int dsign = -1;
            int esign = -1;

            psign = bodyText.indexOf("£");
            dsign = bodyText.indexOf("$");
            esign = bodyText.indexOf("€");
        }*/

        for(int i = 0; i < textArr.length; i++) {
            if(textArr[i] == '£' || textArr[i] == '$' || textArr[i] == '€') {
                int count = 0;
                for(int j = i; j < textArr.length; j++) {
                    if(textArr[j] != ' '){
                        count++;
                    } else if(textArr[j] == ' '){
                        char[] price = new char[count + 1];
                        for(int k = 0; k < count; k++) {
                            price[k] = textArr[i+k];
                        }
                        return new String(price);
                    }

                }
            }
        }
        return "Price not found. Check manually.";
    }

    public List<String> getLinks() {
        return this.links;
    }

    public String[] breakSentence(String sentence) {
        String[] words = sentence.toLowerCase().split("\\s+");
        for(int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "");
        }
        return words;
    }
}
