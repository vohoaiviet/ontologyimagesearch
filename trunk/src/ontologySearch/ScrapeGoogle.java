package ontologySearch;

import org.apache.log4j.PropertyConfigurator;
import org.omg.CORBA_2_3.portable.InputStream;

import com.apsquared.jscrape.PageScraper;

public class ScrapeGoogle {
	
	private String URL = "";
	PageScraper scraper = new PageScraper();
	
	public ScrapeGoogle(String keyWord){
		URL = "http://images.google.ie/images?hl=en&q="+keyWord+"&btnG=Search+Images&gbv=2";
	}
	
	public void processPage(){
//		System.out.println("TEST : "+scraper.getStreamOnly(URL));
//		java.io.InputStream in = scraper.getStreamOnly(URL);
		
		//Query to find the stock price.
		String q = "declare namespace xhtml=\"http://www.w3.org/1999/xhtml\"; \n" +
					"for $d in //xhtml:td \n"+
				   " where contains($d/text()[1], \"color\") \n"+
				   " return    {data($d/following-sibling::xhtml:td)}   ";
		try
		{
			String word = scraper.scrapePageForString(URL, q);
			System.out.println("TEST WORD: "+word);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	   
	}
	
	public static void main(String[] args){

		PropertyConfigurator.configure("src/ontologySearch/log4j.properties");
		ScrapeGoogle scrape = new ScrapeGoogle("car");
		scrape.processPage();
	}

}
