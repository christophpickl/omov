package at.ac.tuwien.e0525580.omov2.tools.webdata._old;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import at.ac.tuwien.e0525580.omov2.BusinessException;

public class WebPageDownloader {
    public static void main(String[] args) throws Exception {
//        System.out.println(WebDataFetcher.getPageContent("http://www.orf.at"));
        
//        final String searchString = "independence+day";
//        String htmlContent = WebDataFetcher.getPageContent("http://us.imdb.com/find?s=all&q="+searchString+"&x=0&y=0");
        
//        File file = new File("result.html");
//        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//        writer.write(htmlContent);
//        writer.close();
//        System.out.println("Written results.");
        
    }

    private static final String END_OF_INPUT = "\\Z";
    private static final String HTTP = "http";
    

    public static String getPageContent(String url) throws BusinessException {
        /*
        URL url = new URL("http://www.java-forums.org/faq.php");
        URLConnection conn = url.openConnection();
        DataInputStream in = new DataInputStream ( conn.getInputStream (  )  ) ;
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        while(d.ready()) {
            System.out.println( d.readLine());
        }
         */
        try {
            final URL fURL = new URL(url);
            if (!HTTP.equals(fURL.getProtocol())) {
                throw new IllegalArgumentException("URL is not for HTTP Protocol: " + fURL);
            }
            String result = null;
            URLConnection connection = null;
            connection = fURL.openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter(END_OF_INPUT);
            result = scanner.next();

            return result;
        } catch (IOException e) {
            throw new BusinessException("Could not get page content of: " + url, e);
        }
    }
}
