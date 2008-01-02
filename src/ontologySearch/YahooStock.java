package ontologySearch;

import com.apsquared.jscrape.PageScraper;
import org.apache.log4j.PropertyConfigurator;


public class YahooStock {

	String theURL;
	PageScraper ps = new PageScraper();
	
	public YahooStock(String stock)
	{
		theURL = "http://finance.yahoo.com/q?s="+stock;
	}
	
	public void processPage()
	{
		//Query to find the stock price.
		String q = "declare namespace xhtml=\"http://www.w3.org/1999/xhtml\"; \n" +
					"for $d in //xhtml:td \n"+
				   " where contains($d/text()[1], \"Last Trade\") \n"+
				   " return  {  data($d/following-sibling::xhtml:td) }   ";
		try
		{
			String price = ps.scrapePageForString(theURL, q);
			System.out.println("PRICE: $"+price);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String []args)
	{
		PropertyConfigurator.configure("src/ontologySearch/log4j.properties");
		YahooStock d = new YahooStock("MSFT");
		d.processPage();
	}
}