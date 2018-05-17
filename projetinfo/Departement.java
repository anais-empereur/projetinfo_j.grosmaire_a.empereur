package projetinfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Departement implements IFonctionsBDD {
	/**
	 * Les noms de départements sont déterminés dans l'énumération Departementenum
	 */
	private Departementenum nom;
	/** 
	 * Un département peut avoir un professeur responsable
	 */
	private Enseignant respo;
	private final College college;
	
	/**
	 * Constructeur du département
	 * @param nom, nom du département à partir de l'énumération
	 * @param college dans lequel se trouve le département
	 */
	public Departement(Departementenum nom,College college){
		super();
		this.nom = nom;
		this.college = college;
		// Si le département n'est pas dans la base de données
		if (!this.presenceBDD()){Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Création de la connection à la base de données
            conn = DriverManager.getConnection(url);
    		
            Statement st = conn.createStatement(); 
            
            st.executeUpdate("INSERT INTO Departement " + 
                "VALUES (1+(SELECT MAX (id) FROM Departement),NULL,"+this.college.getidBDD()+",'"+this.nom.name()+"')"); 

        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            
        } }}
	}
	
	//Accesseurs
	public String getNom() {
		return nom.name();
	}
	public Enseignant getRespo() {
		return respo;
	}
	
	/**
	 * Changement ou ajout de responsable à un département
	 * @param respo, le professeur responsable du département
	 */
	public void setRespo(Enseignant respo) {
		this.respo = respo;
		Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement(); 
            // Requête SQL
            String query = "UPDATE Departement SET idresponsable = "+respo.getidBDD()+" WHERE id ="+this.getidBDD();
			st.executeUpdate(query);st.close();


        } catch (SQLException e1) {
        	System.out.println(e1.getMessage());
        } finally {
        	try {
        		if (conn != null) {
        conn.close();}
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
            
            // Requête SQL
            String query = "SELECT * FROM Departement WHERE idCollege=";
            query =query+this.college.getidBDD()+" AND nom = '"+this.getNom()+"'";
            //System.out.println(query);
            

            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("id");result.close();
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
	
	
	public College getCollege() {
		return college;
	}
	
	/** 
	 * Calcul de la moyenne d'un département d'un collège
	 * @return La moyenne des notes du département
	 */
	public double  moyenne() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            // Requête SQL
            String query = "SELECT * FROM Note n JOIN Matiere m ON m.id = n.idmatiere JOIN Departement d ON m.iddepartement = d.id WHERE m.departement=";
            query =query+"'"+this.nom.toString()+"'";
            query =query+"AND d.idcollege ="+this.college.getidBDD();
        
            PreparedStatement prepare = MyConnection.getinstance().prepareStatement(query);
            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		
    		double moy=0;
    		int nb = 0;
    		while(result.next()){
    			moy=moy+result.getFloat("valeur");
    			nb++;
    			}
    		
    		moy = moy/nb;
    		prepare.close(); state.close();
    		return(moy);       
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return(0.1);
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
            

            String query = "SELECT COUNT(*) as total FROM Departement WHERE idCollege ='"+this.getCollege().getidBDD()+"' AND nom='"+this.getNom()+"'";
            
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
            }        }
	}

}