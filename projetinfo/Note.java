package projetinfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Note implements IFonctionsBDD {
	private int valeur;
	final private Matiere matiere;
	final private Etudiant etudiant;
	
	
	public Note(int v,Matiere m, Etudiant e){
		valeur = v ; matiere = m ; etudiant = e;
		
	
		if (!this.presenceBDD()){Connection conn = null;
        try {
            // paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Connection à la base de données
            conn = DriverManager.getConnection(url);

            int i = m.getidBDD();
            int nb = e.getidBDD();
    		
            Statement st = conn.createStatement(); 
            
            st.executeUpdate("INSERT INTO Note " + 
                "VALUES (1+(SELECT MAX (id) FROM Note ),"+v+","+nb+","+ i +")"); 

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
        
			
		}}
		
	}


	public int getValeur() {
		return valeur;
	}
	public Etudiant getEtudiant(){
		return etudiant;
	}
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	public Matiere getMatiere() {
		return matiere;
	}
	
	public int getIdMatiere(String matiere, int idcollege){
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //connecte la bdd
            conn = DriverManager.getConnection(url);
            String query = "SELECT M.id FROM Matiere M JOIN Departement D ON D.id = M.iddepartement WHERE idCollege = ";
            query =query+idcollege+" AND M.nom = '" + matiere+"'";
            //System.out.println(query);

                    Statement state = MyConnection.getinstance().createStatement();

                    ResultSet result = state.executeQuery(query);
                    int i = result.getInt("id");
                    state.close();
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
	 * Retourne d'identifiant de la note dans la base de données
	 * Implemente l'interface IFonctionsBDD
	 * 
	 * @return L'identifiant de la note dans la base de données
	 */
	public int getidBDD(){
		Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //create a connection to the database
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT * FROM Note WHERE idEt=";
            query =query+this.etudiant.getidBDD()+" AND idmatiere = "+getIdMatiere(this.matiere.getnomstring(),this.etudiant.getCollege().getidBDD());
            //System.out.println(query);
            

            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("id");
    		state.close();
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
	 * Détermine si l'élément est dans la base de données
	 * Implemente l'interface IFonctionsBDD
	 * 
	 * @return Le boolean true ou false si l'élement est dans la base de données ou non
	 * @throws SQLException
	 */
	public boolean presenceBDD (){
		Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //connection à la base de données
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT COUNT(*) as total FROM Note WHERE idEt ='"+this.getEtudiant().getidBDD() +"' AND idMatiere='"+this.getMatiere().getidBDD()+"'";
            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("total");
    		state.close();
    		if (i==0){// Si il n'y a aucune note de cet étudiant dans cette matière
    			return false;
    		} else{// Si il n'y en a pas
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
            }        }
	}


}