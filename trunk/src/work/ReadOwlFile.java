package work;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import com.hp.hpl.jena.query.*;

public class ReadOwlFile {

	private OntModel model;
	private Vector resultsVector;

	public void run(){

		readFile();
		UsingJenaDatabaseBackend test = new UsingJenaDatabaseBackend();
		test.factoryMethods(model);
		
//		listEntities(model, "Location_entity");
//		queryOntology();
//		process();

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

		Query query = QueryFactory.create(queryString4);

		//execute the query and obtain the results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		for(; results.hasNext();){
			QuerySolution solution = results.nextSolution();
			System.out.println("solution: " +solution.toString());
		}

		//print out results
//		ResultSetFormatter.out(System.out, results, query);
		/*	
		//iterate through results and store in resultsVector
		resultsVector = new Vector();
		for(; results.hasNext();){
			QuerySolution solution = results.nextSolution();
			//access variables
			RDFNode n = solution.get("x");

			if(n.isLiteral()){
				((Literal)n).getLexicalForm();
				resultsVector.add(n);
			}
			else if(n.isResource()){
				Resource r = (Resource)n;
				resultsVector.add(r);

			}
			else{
				resultsVector.add(n);
			}

		}

		for(int i = 0; i<resultsVector.size(); i++){
			System.out.println("i : " +resultsVector.elementAt(i));
		}


		//output query results
//		ResultSetFormatter.out(System.out, results, query);
		 */
		//free up resources used running the query
		qe.close();

	}

	/*protected void listEntities(OntModel m, String keyword){
		//find the keyword in the ontology


		OntClass c = m.getOntClass(keyword);
		System.out.println("C: " +c);

		Resource entity = m.getResource(keyword);
		System.out.println("entity: " +entity);

		StmtIterator iterator = entity.listProperties();
		System.out.println("size of list: "+iterator.toList().size());

		while(iterator.hasNext()){
			System.out.println(iterator.next().toString());
		}


	}*/

	protected void process(){
		Resource resource = (Resource) resultsVector.elementAt(0);
		StmtIterator iterator = resource.listProperties();

		while(iterator.hasNext()){
			System.out.println("property: "+iterator.next());
		}





	}

	public static void main(String[] args){
		new ReadOwlFile().run();
		
	}

}
