package generalise;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

public class ProcessKeywords {

	private BufferedReader reader = null;
	private int numRequests = 0;
	private Vector<MediaItem> mediaItems;
	
	public ProcessKeywords() throws ClassNotFoundException, SQLException, IOException{
		try{
			this.reader = new BufferedReader(new FileReader("./Info.txt"));
			numRequests = Integer.parseInt(reader.readLine());
			this.mediaItems = new Vector<MediaItem>();
		
			for(int i = 0; i< numRequests; i++){
				String infoString = reader.readLine();
				StringTokenizer tokenizer = new StringTokenizer(infoString);
				String itemName = tokenizer.nextToken();
				Vector<String> itemKeywords = new Vector<String>();
				while(tokenizer.hasMoreTokens()){
					itemKeywords.add(tokenizer.nextToken());
				}
				
				mediaItems.add(new MediaItem(itemName, itemKeywords));	
			}
			
			}catch (Exception e) {
			System.out.println("no data file");
			System.exit(-1);
		}
			
			individualProcesses();

	}

	private void individualProcesses() throws ClassNotFoundException, SQLException, IOException{
		
		GenerateSearchWords search = new GenerateSearchWords(); 
		
		if(mediaItems!=null){
			for (int i = 0; i < mediaItems.size(); i++) {
				for(int j = 0; j<mediaItems.elementAt(i).getKeywords().size(); j++){
					search.processSearchWord(mediaItems.elementAt(i).getKeywords().elementAt(j)); //generate additional words for each word provided with each image
				}
				
			}
		}else{
			System.out.println("no media items");
			return;
		}
		
	}
}
