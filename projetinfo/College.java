
package projetinfo;

//imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class College implements IFonctionsBDD{
	//Attributs
	private final String nom;
	/**
	 * Cet attribut correspond au numéro de l'académie
	 */
	private final int num_ac;
	private final Adresse adresse;
	
	//Accesseurs
	public String getNom() {
		return nom;
	}
	public int getNum_ac() {
		return num_ac;
	}
	public Adresse getAdresse(){
		return adresse;
	}
	
	
	/**
	 * Constructeur du collège
	 * @param adresse où se situe le collège
	 * @param nom
	 * @param num_ac, numéro de l'académie du collège
	 */
	public College(Adresse adresse, String nom, int num_ac){
		super();
		this.nom = nom;
		this.num_ac = num_ac;
		this.adresse = adresse;
		
		int idAd = adresse.getidBDD();
		if (!this.presenceBDD()){Connection conn = null;
	    try {
	        String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
	        // connecte la bdd
	        conn = DriverManager.getConnection(url);
	        Statement st = conn.createStatement(); 
	        // Ecriture de la requête SQL
	        st.executeUpdate("INSERT INTO College " + 
	          "VALUES (1+(SELECT MAX (id) FROM College ),"+idAd+",'"+nom+"',"+num_ac+")"); 
	       
	    } catch (SQLException e1) {
	        //System.out.println(e1.getMessage());
	    } finally {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException ex) {
	            //System.out.println(ex.getMessage());
	        }  } 
	    }
	}
	
	/**
	 * Récupère l'identifiant dans la base de données du collège en question
	 * Implémente l'interface IFonctionsBDD
	 * 
	 * @return L'identifiant dans la base de données, un entier
	 * @throws SQLException
	 */
	public int getidBDD(){
        Connection conn = null;
        try {
    String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
    //connect la bdd
    conn = DriverManager.getConnection(url);
    String query = "SELECT * FROM College WHERE nom='";
    query =query+this.nom +"'";

            Statement state = MyConnection.getinstance().createStatement();

            ResultSet result = state.executeQuery(query);
            int i = result.getInt("id");
            state.close();result.close();
            return i;

        } catch (SQLException e1) {
        	//System.out.println(e1.getMessage());
        	return (0);
        } finally {
        	try {
        		if (conn != null) {
        			conn.close();
        		}
        	} catch (SQLException ex) {
        //System.out.println(ex.getMessage());
        	}
}

}
	
	
	/**
	 * Détermine si le collège est dans la base de données, on suppose que le nom de collège est unique dans le sein de l'académie
	 * Implémente l'interface IFonctionsBDD
	 * 
	 * @return Le boolean true ou false si l'élement est dans la base de données ou non
	 * @throws SQLException
	 */
	public boolean presenceBDD(){
		Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //connection à la base de données
            conn = DriverManager.getConnection(url);
            
            // Requête SQL : compte les différents collège avec ce nom
            String query = "SELECT COUNT(*) as total FROM College WHERE nom ="+this.getNom();
            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("total");
    		state.close();result.close();
    		if (i==0){// Si il n'est pas dans la base de données
    			return false;
    		} else{// Si il est dans la base de données
    			return true;
    		}
        } catch (SQLException e1) {
            //System.out.println(e1.getMessage());
            return false ;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                //System.out.println(ex.getMessage());
            }  
	}
	

	}
	

}