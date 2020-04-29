import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.*;

public class Driver {

    public static void main(String[] args) throws IOException {
        System.out.println("Enter the item to search for:");
        Scanner input = new Scanner(System.in);
        String search = input.nextLine();
        String urlSearch = search.replaceAll("\\s+", "+");
        System.out.println("You are searching for: " + search);

        System.out.println("How many pages would you like to search? The higher the number of pages, the more results but will also take longer.");
        int pages = input.nextInt();
        Spider spider = new Spider(String.format("https://www.google.com/search?q=%s&oq=n&aqs=chrome.0.69i59j69i57j35i39j0l3.1730j0j8&sourceid=chrome&ie=UTF-8", urlSearch),
                search, pages);
    }


}
