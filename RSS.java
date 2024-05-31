import org.jsoup.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RSS{
    static String fileName= "data.txt";
    static List<RssFeed> feeds = new ArrayList<>();
    static Scanner sc= new Scanner(System.in);
    static int maxItems=5;
    public static void main(String[] args) {
        
        

        // Load feeds from data.txt
        loadFeeds();



        // Display menu options
            while (true) {
                System.out.println("\nType a valid number for your desired action:");
                System.out.println("1. Show updates");
                System.out.println("2. Add new RSS feed");
                System.out.println("3. Remove RSS feed");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();





        // Handle user input for adding/removing feeds and displaying posts

                switch (choice) {
                    case 1:
                    ShowUpdates();
                        break;
                    case 2:
                        addFeed();
                        saveFeeds();
                        break;
                    case 3:
                        removeFeed();
                        saveFeeds();
                        break;
            
                    case 4:
                        saveFeeds();
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }    
            
            
        }
    
    // Other methods here (e.g., extractPageTitle, fetchPageSource, extractRssUrl, retrieveRssContent)



        private static void loadFeeds() {

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        feeds.add(new RssFeed(parts[0], parts[1], parts[2]));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading data file: " + e.getMessage());
            }

        }



    private static void addFeed() {
        System.out.print("Enter the website URL to add: ");
        String websiteUrl = sc.nextLine();
        for(RssFeed feed : feeds){
            if(feed.getWebsiteUrl().equals(websiteUrl)){
                System.out.print(websiteUrl+" already exists.");
                return;
            }
        }
        try {
            String rssUrl = extractRssUrl(websiteUrl);
            if (rssUrl.isEmpty()) {
                System.out.println("No RSS feed found for this website.");
                return;
            }
            
            String name = extractPageTitle(websiteUrl);
            feeds.add(new RssFeed(name, websiteUrl, rssUrl));
            System.out.println("Feed added successfully.");
        } catch (IOException e) {
            System.out.println("Error fetching RSS URL: " + e.getMessage());
        }
        saveFeeds();
    }


    private static void removeFeed() {
        boolean flag=false;
        System.out.print("Enter the website URL to remove: ");
        String websiteUrl = sc.nextLine();
    
        for(RssFeed feed : feeds){
            if(feed.getWebsiteUrl().equals(websiteUrl)){
                feeds.remove(feed);
                flag=true;
                break;
            }
        }
        if(flag==false){
            System.out.print("Coudn't find "+websiteUrl);
        }
            
        System.out.println("Feed removed successfully.");
        

/* 
        System.out.println("Available feeds:");

        System.out.println((0) + ". " + "Remove all feeds.");
        for (int i = 1; i < feeds.size()+1; i++) {
            System.out.println(i+ ". " + feeds.get(i).getName());
        }
        System.out.println("Enter the number of the feed to remove:");
        System.out.println("Enter -1 to return.");
        int index = sc.nextInt();
        if (index==-1){      
            return;
        }else if(index==0){
            for (RssFeed feed : feeds) {
                feeds.remove(feed);
            }
        }else if(index >= 1 && index < feeds.size()+1) {
            feeds.remove(index);
            System.out.println("Feed removed successfully.");
        } else {
            System.out.println("Invalid index. Please try again.");
        }
        saveFeeds();*/

    }


    private static void ShowUpdates() {
        System.out.println("Available feeds:");
        System.out.println((0) + ". "+"show all updates");
        for (int i = 0; i < feeds.size(); i++) {
            System.out.println((i + 1) + ". " + feeds.get(i).getName());
        }
        System.out.print("Enter the number of the feed to show updates: ");
        System.out.println("Enter -1 to return.");
        int index = sc.nextInt();
        if (index==-1){      
            return;
        }else if(index==0){
            for (RssFeed feed : feeds) {
                retrieveRssContent(feed.getRssUrl());
            }
        }else if(index >= 1 && index < feeds.size()+1) {
            retrieveRssContent(feeds.get(index).getRssUrl());
        } else {
            System.out.println("Invalid index. Please try again.");
        }
    }
        

    private static void saveFeeds() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
        for (RssFeed feed : feeds) {
                bw.write(feed.getName() + ";" + feed.getWebsiteUrl() + ";" + feed.getRssUrl());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing data file: " + e.getMessage());
        }
    }
        
    
    public static String extractPageTitle(String html) {
        try {
            org.jsoup.nodes.Document doc = Jsoup.parse(html);
            return doc.select("title").first().text();
        } catch (Exception e) {
            return "Error: no title tag found in page source!";
        }
    }
    
    public static String fetchPageSource(String urlString) throws Exception {
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
        return toString(urlConnection.getInputStream());
    }
    
    private static String toString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null)
            stringBuilder.append(inputLine);
        return stringBuilder.toString();
    }
    public static String extractRssUrl(String url) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        return doc.select("[type='application/rss+xml']").attr("abs:href");
    }
    public static void retrieveRssContent(String rssUrl) {
        try {
            String rssXml = fetchPageSource(rssUrl);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append(rssXml);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(input);
            NodeList itemNodes = doc.getElementsByTagName("item");
    
            for (int i = 0; i < itemNodes.getLength(); ++i) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) itemNode;
                    System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent());
                    System.out.println("Link: " + element.getElementsByTagName("link").item(0).getTextContent());
                    System.out.println("Description: " + element.getElementsByTagName("description").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            System.out.println("Error in retrieving RSS content for " + rssUrl + ": " + e.getMessage());
        }
    }
    
    
}



class RssFeed{
    private static int counter=0;
    private final String name;
    private final String websiteUrl;
    private final String rssUrl;

    public RssFeed(String name, String websiteUrl, String rssUrl) {
        counter++;
        this.name = name;
        this.websiteUrl = websiteUrl;
        this.rssUrl = rssUrl;
    }
    public int getCounter(){
        return counter;
    }
    public String getName(){
        return name;
    }
    public String getWebsiteUrl(){
        return websiteUrl;
    }
    public String getRssUrl(){
        return rssUrl;
    }

}