package model.connection;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnection{

	private static final String RESOURCES = "java:comp/env/jdbc/Erika";
	
	private DatabaseConnection() {
		
	}
	
	public static Connection getConnection() throws SQLException{
	
		try {
			InitialContext initial = new InitialContext();
			DataSource data = (DataSource) initial.lookup(RESOURCES);
			return data.getConnection();			
			
		}catch(NamingException n) {
			n.printStackTrace();
		}
		return null;
	}
}