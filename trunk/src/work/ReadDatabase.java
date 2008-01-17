package work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class ReadDatabase {

	private Statement statement;
	private ResultSet resultSet;
	private int instanceID;
	private int owlClassID;
	private int containedInInstanceID;
	private String owlClass;
	private boolean subRegionProperty;
	private boolean inverseOf_subRegionProperty;
	private boolean is_aProperty;
	private Vector<String> keyWordList;
	private Vector<Instance> searchList;
	public String searchWord = "world";
	

	public void loadDatabase(){
		
		keyWordList = new Vector<String>();

		try{
			//load and register the JDBC driver classes for MySql
			Class.forName("com.mysql.jdbc.Driver");

			//URL of the individuals database on localhost with default port 3306
			String url = "jdbc:mysql://localhost:3306/individuals";
//			System.out.println("url: " +url);

			//connect to the database at url using root user and password (none at the mo)
			Connection connection = DriverManager.getConnection(url, "root", "");
//			System.out.println("connection: " +connection);

			//statement object for manipulating the database
			statement = connection.createStatement();
			
			processSearchWord(searchWord);
			
//			System.out.println("Generated search words: ");
//			
//			for(int i = 0; i< keyWordList.size(); i++){
//				System.out.println(keyWordList.elementAt(i));
//			}
//			

			//close the connection to the database
			connection.close();

		}catch(Exception e){
			e.printStackTrace();
		}


	}
	
	private Vector<String> processSearchWord(String searchWord) throws SQLException, IOException{
		
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
			
			else if(searchList.size()==0){
				keyWordList.add(searchWord);
				return keyWordList;
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
				keyWordList = test.getGeneratedKeywords();
			}
	
			return keyWordList;

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
		
		InputStreamReader cin = new InputStreamReader(System.in);

	 
		
		int temp = cin.read()-48;
		
		
		return searchList.elementAt(temp).getContainedInClassID();
		
	}
	
	public Vector<String> getKeywordList(){
		return keyWordList;
	}

	public static void main(String[] args){
		ReadDatabase test = new ReadDatabase();
		test.loadDatabase();
		ImageSearcher searcher = new ImageSearcher(test.getKeywordList());
		
	}

}
