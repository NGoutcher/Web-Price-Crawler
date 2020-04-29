import java.util.*;

public class Spider {
    private Set<String> visited = new HashSet<String>();
    private List<String> found = new ArrayList<String>();
    private List<String> prices = new ArrayList<String>();
    private List<String> toVisit = new LinkedList<String>();

    public Spider(String url, String toSearch, int pages) {
        int MAX_PAGES = pages;
        search(url, toSearch, MAX_PAGES);
    }

    public String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.toVisit.remove(0);
        } while(this.visited.contains(nextUrl));
        this.visited.add(nextUrl);
        return nextUrl;
    }

    public void search(String url, String searchWord, int max_pages) {
        while(this.visited.size() < max_pages) {
            System.out.println(this.visited.size() + "/" + max_pages);
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();
            if(this.toVisit.isEmpty()) {
                currentUrl = url;
                this.visited.add(url);
            } else {
                currentUrl = this.nextUrl();
            }

            if(leg.crawl(currentUrl)) {
                boolean success = false;
                if (!currentUrl.equals(url) && leg.searchForWord(searchWord)) {
                    success = true;
                }
                if (success) {
                    if(!found.contains(currentUrl))
                        found.add(currentUrl);
                        prices.add(leg.searchForPrice());
                    //System.out.println(String.format("Success! Word %s found at %s", searchWord, currentUrl));
                    //break;
                }
                this.toVisit.addAll(leg.getLinks());
            }
        }
        System.out.println(String.format("Done! Visited %s web page(s)", this.visited.size()));

        if(found.size()> 0) {
            System.out.println(searchWord + " was found at: ");
            for (int i = 0; i < found.size(); i++) {
                System.out.println(found.get(i));
                System.out.println("Price: " + prices.get(i));
                System.out.println("- - - - - - - - - -");
            }
        } else {
            System.out.println("Couldn't find any results. Try increasing the number of pages to search.");
        }
    }
}
