package generalise;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	private Statement statement;
	private String database;
	
	public Database(String datab) throws ClassNotFoundException, SQLException{
		this.database = datab;
		loadDatabase();
	}

	private void loadDatabase() throws ClassNotFoundException, SQLException{
		
			//load and register the JDBC driver classes for MySql
			Class.forName("com.mysql.jdbc.Driver");
			//URL of the individuals database on localhost with default port 3306
			String url = "jdbc:mysql://localhost:3306/"+database;
			//connect to the database at url using root user and password (none at the mo)
			Connection connection = DriverManager.getConnection(url, "root", "");
			//statement object for manipulating the database
			this.statement = connection.createStatement();
				
	}
	
	public Statement getStatement(){
		return statement;
	}
}
