package generalise;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;

public class GenerateSearchWords {

	private Statement statement;
	private ResultSet resultSet;
	
	private String searchWord;
	private Vector<String> generatedSearchWords;
	private Vector<Instance> searchList;
	
	private int instanceID;
	private int owlClassID;
	private int containedInInstanceID;
	private String owlClass;
	private boolean subRegionProperty;
	private boolean inverseOf_subRegionProperty;
	private boolean is_aProperty;
	
	
	public GenerateSearchWords() throws ClassNotFoundException, SQLException, IOException{
		Database database = new Database("individuals");
		statement = database.getStatement();
		generatedSearchWords = new Vector<String>();
	}
	
	
	protected void processSearchWord(String search) throws SQLException, IOException{
		this.searchWord = search;
		
		searchList = new Vector<Instance>();
		//take care of strings that contain the ' character
		//SHOULD CHANGE INSTANCES IN DATABASE TO NOT CONTAIN ' CHARACTER
//		String word = instanceList.elementAt(index);
		if(searchWord.contains("'")){
			searchWord = searchWord.replace("'", " ");
		}
		//perform query on database (select searchword from instances table)
		resultSet = statement.executeQuery("SELECT * " + "from instances " +"WHERE name=\'"+searchWord+"\'");
		
			//print out the results of query to the console
//			System.out.println("display results from intances table .... search word found: ");
			
			while(resultSet.next()){
				instanceID = resultSet.getInt("instanceID");
				owlClassID = resultSet.getInt("classID");
				containedInInstanceID = resultSet.getInt("containedIn_instanceID");
				String str = resultSet.getString("name");
				searchList.add(new Instance(str, instanceID, owlClassID, containedInInstanceID));
//				System.out.println("\tinstanceID= " + instanceID
//						+ "\tname= " + str + "\tclassID= " + owlClassID);
			}//end while loop
//			System.out.println();
			
			if(searchList.size()>1){
				int id = handleMultiple();
				//perform query on database (select searchword from instances table)
				resultSet = statement.executeQuery("SELECT * " + "from instances " +"WHERE name=\'"+searchWord+"\'" + "AND containedIn_instanceID="+id);
				
				//print out the results of query to the console
//				System.out.println("display new results from intances table .... search word found: ");
				
				while(resultSet.next()){
					instanceID = resultSet.getInt("instanceID");
					owlClassID = resultSet.getInt("classID");
					containedInInstanceID = resultSet.getInt("containedIn_instanceID");
					String str = resultSet.getString("name");
					searchList.add(new Instance(str, instanceID, owlClassID, containedInInstanceID));
//					System.out.println("\tinstanceID= " + instanceID
//							+ "\tname= " + str + "\tclassID= " + owlClassID);
				}//end while loop
//				System.out.println();
			}
			
			//if the word is not in the database we don't need to do any work
			else if(searchList.size()==0){
				generatedSearchWords.add(searchWord);
				return;
			}

			//perform query on database (select class id from owlClass table)
			resultSet = statement.executeQuery("SELECT * " + "from owlClass " +"WHERE owlclassID="+owlClassID);

//			System.out.println("display results from owlClass table .... owl class that the search word belongs to: ");
			while(resultSet.next()){
				subRegionProperty = resultSet.getBoolean("subRegion");
				inverseOf_subRegionProperty = resultSet.getBoolean("inverseOf_subregion");
				owlClassID = resultSet.getInt("owlclassID");
				owlClass = resultSet.getString("name");
//				System.out.println("\towlClassID= " + owlClassID
//						+ "\towlClass name= " + owlClass+ "\thas subregion= " +subRegionProperty);
			}
//			System.out.println();
			
			if(subRegionProperty){
				SubRegions test = new SubRegions();
				test.findAllSubRegions(statement, searchWord, instanceID, owlClassID);
				generatedSearchWords = test.getGeneratedKeywords();
			}
			//if it doesnt have a subregion but is a sub region of something else
			//it is as specific as can be... should offer chance to generalise!?
			else if(!subRegionProperty && inverseOf_subRegionProperty){
				generatedSearchWords.add(searchWord);
				return;
			}
	}
	
	public int handleMultiple() throws SQLException, IOException{
		
		boolean sub = false;
		System.out.println("enter the number of the search you require");
		for(int i=0; i<searchList.size(); i++){
			int id = searchList.elementAt(i).getClassID();
//			System.out.println("classid: " +id);
			resultSet = statement.executeQuery("SELECT * " + "from owlClass " +"WHERE owlclassID="+searchList.elementAt(i).getClassID());
			if(resultSet.next()){	
			 sub = resultSet.getBoolean("subRegion");
			 
			}
			if(sub){
				resultSet = statement.executeQuery("SELECT * " + "from instances " + "WHERE instanceID=" +searchList.elementAt(i).getContainedInClassID());
				String n;
				int d;
				if(resultSet.next()){
					n = resultSet.getString("name");
					System.out.println(i + " = " +searchList.elementAt(i).getName()+", "+n );
				}
				
			}
		}
		/*
		 * InputStreamReader isr = new InputStreamReader ( System.in );
			BufferedReader br = new BufferedReader ( isr );
			String s = null;
			try {
   				while ( (s = br.readLine ()) != null ) {
      			// do something
   				}
			}catch ( IOException ioe ) {
   			// won't happen too often from the keyboard
			}

		 */
		
		InputStreamReader cin = new InputStreamReader(System.in);

	 
		
		int temp = cin.read()-48;
		
		
		return searchList.elementAt(temp).getContainedInClassID();
		
	}
	
	public Vector<String> getGeneratedSearchWords(){
		return generatedSearchWords;
	}
	


}
