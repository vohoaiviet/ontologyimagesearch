package work;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class SubRegions {

	private Vector<String> generatedKeywords;
	private Vector<String> instanceList;
	private Vector<Integer> containedByInstanceID;
	private ResultSet resultSet;
	private int instanceID;
	private int classID;
	private int subClassID;
	private boolean subRegionProperty = true;
	private boolean inverseOf_subRegionProperty;
	private int index = 1;

	protected void findAllSubRegions(Statement statement, String keyword, int instID, int clsID){
		instanceID = instID;
		classID = clsID;
		generatedKeywords = new Vector<String>();
		generatedKeywords.add(keyword);
		instanceList = new Vector<String>();
		instanceList.add(keyword);
		containedByInstanceID = new Vector<Integer>();
		containedByInstanceID.add(null);

		
		try {
			do{
			if(subRegionProperty){
				//perform query on database (find out what class the subregion of the given class is)
				resultSet = statement.executeQuery("SELECT ClassID_B " + "from subRegion " + "WHERE ClassID_A="+classID);

//				System.out.println("display results from subregion table");
				while(resultSet.next()){
					subClassID = resultSet.getInt("ClassID_B");
//					System.out.println("\tsubClassID= " +subClassID);
				}
//				System.out.println();

				//perform query on database (select all instances that are subregions of the previous instance
				resultSet = statement.executeQuery("SELECT * " + "from instances " +"WHERE ContainedIn_instanceID="+instanceID+" AND ClassID="+subClassID);

//				System.out.println("display results from instances table");
				while(resultSet.next()){
					String name = resultSet.getString("name");
					generatedKeywords.add(name);
					instanceList.add(name);
					int id = resultSet.getInt("ClassID");
					int id2 = resultSet.getInt("ContainedIn_instanceID");
					containedByInstanceID.add(id2);

//					System.out.println("\tname= " +name+" \tfrom classID= " +id+" \tcontained in instanceID= "+id2);
				}
//				System.out.println();
			}
			
			if(instanceList.size()!=index){
			//take care of strings that contain the ' character
			//SHOULD CHANGE INSTANCES IN DATABASE TO NOT CONTAIN ' CHARACTER
			String word = instanceList.elementAt(index);
			if(word.contains("'")){
				word = word.replace("'", " ");
			}
			//perform query on database (select searchword from instances table)
			resultSet = statement.executeQuery("SELECT * " + "from instances " +"WHERE name=\'"+word+"\'" + "AND containedIn_instanceID=" + containedByInstanceID.elementAt(index));
			
			//print out the results of query to the console
//			System.out.println("display results from intances table .... search word found: ");
			while(resultSet.next()){
				instanceID = resultSet.getInt("instanceID");
				classID = resultSet.getInt("classID");
				String str = resultSet.getString("name");
//				System.out.println("\tinstanceID= " + instanceID
//						+ "\tname= " + str + "\tclassID= " + classID);
			}//end while loop
//			System.out.println();
			
			//perform query on database (select class id from owlClass table)
			resultSet = statement.executeQuery("SELECT * " + "from owlClass " +"WHERE owlclassID="+classID);

//			System.out.println("display results from owlClass table .... owl class that the search word belongs to: ");
			while(resultSet.next()){
				subRegionProperty = resultSet.getBoolean("subRegion");
				inverseOf_subRegionProperty = resultSet.getBoolean("inverseOf_subregion");
				classID = resultSet.getInt("owlclassID");
				String owlClass = resultSet.getString("name");
//				System.out.println("\towlClassID= " + classID
//						+ "\towlClass name= " + owlClass+ "\thas subregion= " +subRegionProperty);
			}
//			System.out.println();
			index++;
			}
			}while(instanceList.size()!=index);
		

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Vector<String> getGeneratedKeywords(){
		return generatedKeywords;
	}

}
