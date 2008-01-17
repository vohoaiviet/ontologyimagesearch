package work;

import java.io.FileInputStream;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

/*
 * To get the protege ontology into the Jena/MySQL database I simply read
in the Protege owl file into a memory based model and then add that
model to my Jena database model:
 */

public class UsingJenaDatabaseBackend {

	private final String databaseURL = "jdbc:mysql://localhost/JenaOntology";
	private final String databaseUserName = "root";
	private final String databasePassword = "";
	private final String databaseEngineName = "MySQL";
	private final String JDBCdriver = "com.mysql.jdbc.Driver";
	
	protected void factoryMethods(OntModel model){
		//load the driver class
		try {
			Class.forName(JDBCdriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create a database connection
		IDBConnection connection = new DBConnection(databaseURL, databaseUserName, databasePassword, databaseEngineName);
		
		//create a model maker with the given connection paramaters
		ModelMaker modelMaker = ModelFactory.createModelRDBMaker(connection);
	//   Create a named model
		Model namedModel = modelMaker.getModel("imageSearchOntologyModel");
		
		 
		
		   
		
		//create a default model
//		Model defaultModel = modelMaker.createDefaultModel();
		
		//or open existing default model
//		Model existingDefaultModel = modelMaker.openModel();
		
		//or create a named model
//		Model namedModel = modelMaker.createModel("namedModel");
		
		//or open a previously created named model
//		Model prevCreatedNamedModel = modelMaker.openModel("an existing model");
	}
	
	private void modelRDBmethod(){
		
		//load the driver class
		try {
			Class.forName(JDBCdriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create a database connection
		IDBConnection connection = new DBConnection(databaseURL, databaseUserName, databasePassword, databaseEngineName);
		
		//create a default model
		ModelRDB defaultModel = ModelRDB.createModel(connection);
		
		//or open an existing model
		ModelRDB existingModel = ModelRDB.open(connection);
		
		//or create a named model
		ModelRDB namedModel = ModelRDB.createModel(connection, "modelName");
		
		//or open a named model
		ModelRDB namedmodel2 = ModelRDB.open(connection, "existingModelName");
	}
	
	
}
