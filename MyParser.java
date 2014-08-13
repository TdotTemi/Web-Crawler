package a3;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class MyParser {
	
	public static String rawHTMLString = "";
	public static ArrayList<String> allCoAuthors = new ArrayList<String>();


	
    public static void main(String[] args) throws Exception 
    {		MyParser googleScholarParser=new MyParser();
            if (args.length > 2) {
            System.out.println("Please enter an appropriate input");
            }else{
            String URLS[]=args[0].split(",");

            if (args.length == 1) {
            	for (String url:URLS){
                    googleScholarParser.getHTML(url);

            System.out.println(googleScholarParser.getGoogleScholarInfo());
            }System.out.println(googleScholarParser.printTotalcoAuthors());

            }else if (args.length ==2) {
            	PrintStream outFile = new PrintStream(new FileOutputStream(args[1]));
            	System.setOut(outFile);
            	for (String url:URLS){
                    googleScholarParser.getHTML(url);
            

            } System.out.println(googleScholarParser.printTotalcoAuthors());
            }

            }

    }

    /**
     * This method gets the authors name from html
     * return authorsName
     */
    private String extractAuthorsName() {
    	    String authourName = "";
            String reForAuthorExtraction = "<span id=\"cit-name-display\" " +  
                    "class=\"cit-in-place-nohover\">(.*?)</span>";			 
            Pattern patternObject = Pattern.compile(reForAuthorExtraction);
            Matcher matcherObject = patternObject.matcher(rawHTMLString);
            while(matcherObject.find())
            {
                authourName = matcherObject.group(1);
            }
            
            return authourName;
    }

   public String getAuthorsName(){
	   return extractAuthorsName();
   }
  private int getAllCitations(int index) { 
    	return Integer.parseInt(citationsTables().get(index)) ;
  }

    private ArrayList<String> citationsTables() {
        ArrayList<String> citationsList = new ArrayList<String>();
        String reForCitationExtraction = "<td class=\"cit-borderleft cit-data\">(.*?)</td>";
        Pattern patternObject = Pattern.compile(reForCitationExtraction);
        Matcher matcherObject = patternObject.matcher(rawHTMLString);
        // Get all of the Citations
        while (matcherObject.find()) {
            String pubs = matcherObject.group(1);
            citationsList.add(pubs);
        }
        return citationsList;
    }
    
    private ArrayList<String> publicationTables() {
        ArrayList<String> publicationList = new ArrayList<String>();
        String reForCitationExtraction = "class=\"cit-dark-large-link\">(.*?)<";
        Pattern patternObject = Pattern.compile(reForCitationExtraction);
        Matcher matcherObject = patternObject.matcher(rawHTMLString);
        // Get all of the Citations
        while (matcherObject.find()) {
            String pubs = matcherObject.group(1);
            publicationList.add(pubs);
        }
        return publicationList;
    }
   
    private String getFirstThreePublications(int index) {
    	String Publication = "";
    	if (index < publicationTables().size()){
    	  Publication = publicationTables().get(index) ;
        }else{
        	;
        }
      return Publication;
      }

    
    private ArrayList<String> CitedByTables() {
        ArrayList<String> CitedByList = new ArrayList<String>();
        String reForCitationExtraction = "(\\>)([0-9]+)(\\<\\/a\\>)"; 

        Pattern patternObject = Pattern.compile(reForCitationExtraction);
        Matcher matcherObject = patternObject.matcher(rawHTMLString);

        while (matcherObject.find()) {
            String pubs = matcherObject.group(2);
            CitedByList.add(pubs);
   
        }
        return CitedByList;
    }
    
    private int getFirstFiveCitedPublications() { 
        int sum = 0;
    	for (int i = 0; i < 5; i++){ 
	    sum += Integer.parseInt(CitedByTables().get(i)) ;

    	}
    	return sum;
  }
    
    private ArrayList<String> coAuthorsTables() {
        ArrayList<String> coAuthorsList = new ArrayList<String>();
        String reForCitationExtraction = "\" title=\"(.*?)\">";  
        Pattern patternObject = Pattern.compile(reForCitationExtraction);
        Matcher matcherObject = patternObject.matcher(rawHTMLString);
        // Get all of the Citations
        while (matcherObject.find()) {
            String pubs = matcherObject.group(1);
            coAuthorsList.add(pubs);
            totalCoAuthorList(pubs);
        }Collections.sort(coAuthorsList);
        return coAuthorsList;
    }
    
    private int getNumberofCoAuthors() {
	return coAuthorsTables().size() ;
  }
    
    public void getHTML(String urlString) throws Exception 
    {
     try
     {
        // create object to store html source text as it is being collected
        StringBuilder html = new StringBuilder();
        // open connection to given url
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        // create BufferedReader to buffer the given url's HTML source
        BufferedReader htmlbr = new BufferedReader(new InputStreamReader(
                                                connection.getInputStream()));
        String line;
        // read each line of HTML code and store in StringBuilder
        while ((line = htmlbr.readLine()) != null) {
            html.append(line);
        }
        htmlbr.close();
        // convert StringBuilder into a String and return it
        rawHTMLString = html.toString();
     }
     catch(Exception e)
     {
         System.out.println("malformed URL or cannot open connection to " +
                 "given URL");
     }
    }
    
    public String getGoogleScholarInfo() {
    	String googleScholarInfo = "";
    	 googleScholarInfo = googleScholarInfo.concat(
                 "-------------------------------------------" + "\n"
                 + "1. Name of Author:" + "\n"
                 + "\t" + extractAuthorsName() + "\n"
                 + "2. Number of All Citations" + "\n"
                 + "\t" + getAllCitations(0) + "\n"
                 + "3. Number of i10-index after 2008" + "\n"
                 + "\t" + getAllCitations(5) + "\n"
                 + "4. Title of the first three publications" + "\n"
                 + "\t" + "1-" + "\t" + getFirstThreePublications(0) + "\n"
                 + "\t" + "2-" + "\t" + getFirstThreePublications(1) + "\n"
                 + "\t" + "3-" + "\t" + getFirstThreePublications(2) + "\n"
                 + "5. Total paper citation of the first five papers:" + "\n"
                 + "\t" + getFirstFiveCitedPublications() + "\n"
                 + "6. Total Co-authors:" + "\n"
                 + "\t" + getNumberofCoAuthors() + "\n");    
    	return googleScholarInfo;
    }
   public ArrayList<String> totalCoAuthorList(String pubs){//populates list
    	allCoAuthors.add(pubs);//add coauthors from all the athors u called to list
    	return allCoAuthors;
    }

    public String getTotalCoAuthorList(){
    Collections.sort(allCoAuthors);
    String totalCoAuthors = "";
    for (int i=0; i<allCoAuthors.size();i++){
		totalCoAuthors= totalCoAuthors.concat(allCoAuthors.get(i) +"\n");
    } return totalCoAuthors;

}

    public String printTotalcoAuthors(){
    	String googleScholarcoAuthors ="";
    	googleScholarcoAuthors =  googleScholarcoAuthors.concat(
                "-------------------------------------------" + "\n"
                + "7. Co-Author list sorted (Total: " + allCoAuthors.size() +":"+ "\n"
                + getTotalCoAuthorList() + "\n");
    	return googleScholarcoAuthors; 
    }
}
