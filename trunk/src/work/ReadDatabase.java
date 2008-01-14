package work;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;



public class ReadDatabase {

	private int id;
	private int location_entity_id;
	private String location_entity;

	public void loadDatabase(){

		try{

			Statement statement;
			ResultSet resultSet;

			//load and register the JDBC driver classes for MySql
			Class.forName("com.mysql.jdbc.Driver");

			//URL of the individuals database on localhost with default port 3306
			String url = "jdbc:mysql://localhost:3306/individuals";
			System.out.println("url: " +url);

			//connect to the database at url using root user and password (none at the mo)
			Connection connection = DriverManager.getConnection(url, "root", "");
			System.out.println("connection: " +connection);

			//statement object for manipulating the database
			statement = connection.createStatement();

			//create a new database
//			statement.executeUpdate("CREATE DATABASE testDatabase");

			//perform query on database (select all info)
			resultSet = statement.executeQuery("SELECT * " + "from Location_Entity ORDER BY LOC_ENT_ID");

			//print out the results of query to the console
			System.out.println("Display all results:");
			while(resultSet.next()){
				int theInt= resultSet.getInt("LOC_ENT_ID");
				String str = resultSet.getString("name");
				System.out.println("\tloc_ent_ID= " + theInt
						+ "\tname = " + str);
			}//end while loop

			//print results from row 2
//			System.out.println(
//			"Display row number 2:");
//			if( resultSet.absolute(2) ){
//			int theInt= resultSet.getInt("LOC_ENT_ID");
//			String str = resultSet.getString("name");
//			System.out.println("\tent_ID= " + theInt
//			+ "\tname = " + str);
//			}//end if

			//perform query on database (select Ireland from locations table)
			resultSet = statement.executeQuery("SELECT * " + "from Locations " +"WHERE name=\'Europe\'");

			//print out the results of query to the console
			System.out.println("display results from locations table: ");
			while(resultSet.next()){

				int theInt= resultSet.getInt("LOC_ID");
				id = theInt;
				String str = resultSet.getString("name");
				System.out.println("\tloc_ID= " + theInt
						+ "\tname = " + str);
			}//end while loop

			//perform query on database (select loc_ent id from location_mapping table)
			resultSet = statement.executeQuery("SELECT * " + "from location_mapping " +"WHERE LOC_ID="+id);

			System.out.println("display results from location_mapping table: ");
			while(resultSet.next()){
				location_entity_id = resultSet.getInt("LOC_ENT_ID");
				int loc_id = resultSet.getInt("LOC_ID");
				int parent_loc_id = resultSet.getInt("PARENT_LOC_ID");
				System.out.println("\tloc_ID= " + loc_id
						+ "\tloc_ent_id = " + location_entity_id + "\tparent_loc_id= " +parent_loc_id);
			}

			//perform query on database (select the name of the location entity from the location_entity table)
			resultSet = statement.executeQuery("SELECT * " + "from location_entity " +"WHERE LOC_ENT_ID="+location_entity_id);

			System.out.println("display result from location_entity table: ");
			while(resultSet.next()){
				int loc_id = resultSet.getInt("LOC_ENT_ID");
				location_entity = resultSet.getString("name");
				System.out.println("\tloc_ent_id= " + loc_id + "\tlocation_entity= " +location_entity);
			}

			//close the connection to the database
			connection.close();

		}catch(Exception e){
			e.printStackTrace();
		}


	}

	public static void main(String[] args){
		ReadDatabase test = new ReadDatabase();
		test.loadDatabase();
	}

}
