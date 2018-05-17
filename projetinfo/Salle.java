package projetinfo;

//Impots
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Salle implements IFonctionsBDD{
	/**
	 * Numéro attribué à la salle au sein du collère
	 */
	private final int numero;
	/**
	 * L'attribut capacité est le nom d'étudiants qui peuvent étudier dans la salle
	 */
	private int capacite;
	private  Matiere matiere;
	
	public int getCapacite() {
		return capacite;
	}
	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}
	public Matiere getMatiere() {
		return matiere;
	}

	public int getNumero(){
		return this.numero;
	}
	
	
	/**
	 * Constructeur de la salle
	 * @param numer, numéro de la salle
	 * @param capacite, le nombre d'étudiants qui peuvent rentrer dans la salle
	 * @param matiere, la matière qui est enseignée dans la salle
	 */
	public Salle(int numer, int capacite, Matiere matiere) {
		super();
		this.numero = numer;
		this.capacite = capacite;
		this.matiere = matiere;
		
		
		if (!this.presenceBDD()){Connection conn = null;
        try {
            // paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Connection à la base de données
            conn = DriverManager.getConnection(url);

    		
            Statement st = conn.createStatement(); 
            
            st.executeUpdate("INSERT INTO Salle " + 
                "VALUES (1+(SELECT MAX (id) FROM Salle ),"+this.getMatiere().getDepartement().getCollege().getidBDD()+","+this.getMatiere().getidBDD()+","+numer+")"); 

        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
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
	}

	/**
	 * Récupère l'identifiant de la salle dans la base de données
	 * Implemente l'interface IFonctionsBDD
	 * @return L'identifiant dans la base de données, un entier
	 * @throws SQLException
	 */
	public int getidBDD(){
		Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //create a connection to the database
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT * FROM Salle WHERE idcollege=";
            query =query+this.getMatiere().getDepartement().getCollege().getidBDD()+" AND idmatiere = "+this.getMatiere().getidBDD();
            //System.out.println(query);
            

            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("id");result.close();
    		state.close(); conn.close();
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
	 * Changement de matière enseignant dans la salle
	 * @param m, la nouvelle matière qui sera enseignée dans la salle
	 */
	public void changerMatiere (Matiere m){
		this.matiere = m;
		Connection conn = null;
		int a = m.getidBDD();
		int b = 2;
		
        try {
            // paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Connexion
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement(); 

			String query = "UPDATE Salle SET idmatiere ="+a+" WHERE id ="+b;
			st.executeUpdate(query);st.close();
		
	
    } catch (SQLException e1) {
        System.out.println(e1.getMessage());
        
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
	 * Détermine si la salle est dans la base de données
	 * Implemente l'interface IFonctionsBDD
	 * @return Le boolean true ou false si la salle est dans la base de données ou non
	 * @throws SQLException
	 */
	public boolean presenceBDD(){
		Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //connection à la base de données
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT COUNT(*) as total FROM Salle WHERE idcollege ='"+this.getMatiere().getDepartement().getCollege().getidBDD() +"' AND idMatiere='"+this.getMatiere().getidBDD()+"'";
            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("total");
    		state.close();
    		if (i==0){
    			return false;
    		} else{
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
                System.out.println(ex.getMessage());
            }  
	}
	

	}
}