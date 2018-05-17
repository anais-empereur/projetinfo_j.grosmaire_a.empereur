package projetinfo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Permet l'accès à la base de données
 * 
 *
 */

public class MyConnection {
	
	static final String DB_URL = "jdbc:sqlite:/home/prof/workspace/bdd.db";
	static final String DB_USER = "dbuse";
	static final String DB_PASS = "pass";
	
	static Connection connection;
	
	/**
	 * Constructeur
	 * 
	 */
	
	private MyConnection (){
		try{
			Class.forName("org.sqlite.JDBC");
			MyConnection.connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Récupére ou construit si elle n'existe pas l'instance connexion
	 * @return
	 */
	public static Connection getinstance(){
		if (MyConnection.connection == null){
			new MyConnection();	
		}
		return MyConnection.connection;
	}
}