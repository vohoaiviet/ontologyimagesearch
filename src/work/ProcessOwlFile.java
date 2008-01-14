package work;

import java.io.InputStream;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class ProcessOwlFile {

	private OntModel model;

	public void run(){

		readFile();
		queryOntology();

	}

	public void readFile(){
		//create a new ontology model
		model = ModelFactory.createOntologyModel();
		//use filemanager to find file
		InputStream in = FileManager.get().open("./ontologies/imageSearchOntology.owl");
		//read in file
		model.read(in,"");
		//write file
//		model.write(System.out);
	}

	protected void queryOntology(){

		//store the query to be performed in a string

		//returns all owl classes in the ontology
		String queryString = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
		"SELECT ?class " +
		"WHERE { " +
		"?class rdf:type owl:Class " +
		" }";

		//returns all subclass relationships between owl classes 
		// *** each class is shown as a subclass of itself as well as its superclass!?!?!
		String queryString2 = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
		"SELECT ?classA ?classB " +
		"WHERE { " +
		"?classA rdf:type owl:Class " +
		"{ " +
		"?classB rdf:type owl:Class " +
		"} " +
		"{ " +
		"?classA rdfs:subClassOf ?classB " +
		"} " +
		"}";

		//DOESNT WORK: ** WANT TO -> return all functional properties for owl classes in the ontology
		//the "subRegion" property is a functional property
		String queryString3 = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
		"SELECT ?class ?property " +
		"WHERE { " +
		"{ " +
		"?property rdf:type owl:FunctionalProperty ." +
		"} " +
		"UNION " +
		"{ " +
		"?class rdf:type owl:Class ." +
		"} "+
		" }";

		//TRYING to return the property associated with the "Country" owl class
		// -- as is, returns the subRegion property and null for the class, no matter what is put in place of country
		String queryString4 = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
		"PREFIX ucd: <http://www.srg.ucd.ie/photo_ontologies.owl#>" +
		"SELECT ?property ?class " +
		"WHERE { " +
		"{ " +
		"?property rdf:type owl:FunctionalProperty . " +
		"} " +
		"UNION " +
		"{ "+ 
		"?class owl:Class ucd:Country . " +
		"} " +
		"} ";


		//test query that just returns all the types in the ontology
		String queryStringTest = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"SELECT ?type " +
		"WHERE { " +
		"?x rdf:type ?type " +
		"}";

		//create a query object using the query string
		Query query = QueryFactory.create(queryString4);

		//execute the query and obtain the results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		for(; results.hasNext();){
			QuerySolution solution = results.nextSolution();
			System.out.println("solution: " +solution.toString());
		}



		//output query results
//		ResultSetFormatter.out(System.out, results, query);

		//free up resources used running the query
		qe.close();

	}




	public static void main(String[] args){
		new ProcessOwlFile().run();
	}

}
