
package projetinfo;

//Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Adresse implements IFonctionsBDD{
	/**
	 * Coordonnées en x d'une adresse
	 */
	private final float coordX;	
	/**
	 * Coordonnées en y d'une adresse 
	 */
	private final float coordY;	
	private final int numero;
	private final String rue;
	private final String ville;
	/**
	 * Code postal de l'adresse
	 */
	private final int codePost;
	
//Accesseurs permettant d'obtenir les attributs privés
	public float getCoordX() {
		return coordX;
	}
	public float getCoordY() {
		return coordY;
	}
	public int getNumero() {
		return numero;
	}
	public String getRue() {
		return rue;
	}
	public String getVille() {
		return ville;
	}
	public int getCodePost() {
		return codePost;
	}


	/**
	 * Constructeur de l'adresse
	 * @param coordX, coordonnées de l'adresse en X
	 * @param coordY, coordonnées de l'adresse en Y
	 * @param numero, numéro du batiment
	 * @param rue, rue du batiment
	 * @param ville, ville où se situe le batiment
	 * @param codePost, code postal de l'adresse
	 */
	public Adresse(float coordX, float coordY, int numero, String rue, String ville, int codePost) {
		super();
		this.coordX = coordX;
		this.coordY = coordY;
		this.numero = numero;
		this.rue = rue;
		this.ville = ville;
		this.codePost = codePost;
		
		//Ajout dans la BDD si pas déjà présence
		if (!this.presenceBDD()){Connection conn = null;
	    try {
	        String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
	        // connecte la bdd
	        conn = DriverManager.getConnection(url);		
	        Statement st = conn.createStatement(); 	        
	        st.executeUpdate("INSERT INTO Adresse " + 
	            "VALUES (1+(SELECT MAX (id) FROM Adresse),"+coordX+","+coordY+","+numero+",'"+ville+"','"+rue+"',"+codePost+")"); 

	    } catch (SQLException e1) {
	        //System.out.println(e1.getMessage());
	    } finally {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException ex) {
	            //System.out.println(ex.getMessage());
	        }
	        
	    }}
	
	}
	
	//Methodes
	/**
	 * Méthode renvoyant la distance entre deux adresses quelconque.
	 * @param ad
	 * @return double 
	 */
	public double calculeDist(Adresse ad){
		return Math.sqrt(Math.pow(this.coordX-ad.coordX, 2.0)+Math.pow(this.coordY-ad.coordY, 2.0));
	}
	
	
	/**
	 * Récupère l'identifiant de l'adresse dans la base de données
	 * Implémentation de l'interface IFonctionsBDD
	 * 
	 * @return L'identifiant de l'adresse dans la base de données, un entier
	 * @throws SQLException
	 */
	public int getidBDD(){
        Connection conn = null;
        try {

    String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
    //connecte la bdd
    conn = DriverManager.getConnection(url);
    //Création de la requêtre SQL
    String query = "SELECT * FROM Adresse WHERE coordX = ";
    query =query+this.coordX+" AND coordY = "+this.coordY;

            Statement state = MyConnection.getinstance().createStatement();

            ResultSet result = state.executeQuery(query);
            int i = result.getInt("id");
            state.close();result.close();
            return i;

        } catch (SQLException e1) {
        	System.out.println(e1.getMessage());
        	return (0);
        } finally {
        	try {
        		if (conn != null) {
            conn.close();
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
	}

	
}
	
	/**
	 * Détermine si l'adresse est dans la base de données
	 * Implémentation de l'interface IFonctionsBDD
	 * 
	 * @return Le boolean true ou false si l'adresse est déjà dans la base de données ou non
	 * @throws SQLException
	 */
	public boolean presenceBDD(){
		Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //connection à la base de données
            conn = DriverManager.getConnection(url);
            
            // Ecriture de la reqrête SQL
            String query = "SELECT COUNT(*) as total FROM Adresse WHERE coordX ="+this.getCoordX() +" AND coordY ="+this.getCoordY();
            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("total");
    		state.close();result.close();
    		if (i==0){// Si il n'y a aucun élement à cette adresse
    			return false;
    		} else{// Si il y en a.
    			return true;
    		}
        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
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