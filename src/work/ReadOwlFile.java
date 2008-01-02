package work;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
/*
 *  Schemagen

When you're working with a model using Jena's API, it's useful to define constants for each property in the model's vocabulary. If you have an RDFS, a DAML, or an OWL representation of the vocabulary, Jena's Schemagen tool can make your life easier by generating these constants automatically.

Schemagen is run on the command line, and takes parameters including the location of the schema or ontology file, and the name and Java package of the class to output. The generated Java class can then be imported and its Property constants used to access the model.

It's also possible to run Schemagen as part of a build process with Ant, keeping the Java constants class in sync with an evolving vocabulary.
 
 

 */
public class ReadOwlFile {
	
	public void run(){
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
		
		//read in the ontology        
        FileManager.get().readModel(m, "./ontologies/photoOntologies.owl");
        listEntities(m, "City");
        
	}
	
	protected void listEntities(OntModel m, String keyword){
		//find the keyword in the ontology
		System.out.println(m.listClasses().toString());
//		Resource entity = m.getResource(keyword);	
//		System.out.println("entity: " +entity);
//		System.out.println("properties: " +entity.listProperties());
////		OntClass c = (OntClass)entity;
//		OntClass c = m.getOntClass("Self_Standing_Entity");
//		System.out.println("class: " + c);
//		while(c!=null){
//		OntClass d = c.getSubClass();
//		System.out.println("subclass: " + d);
//		c=d;
//		}
//		
	
	}
	
	public static void main(String[] args){
		new ReadOwlFile().run();
	}

}