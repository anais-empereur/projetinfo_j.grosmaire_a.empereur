package projetinfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JFrame;

public class Site {
	
	//attributs
	/**
	 * Adresse web du site du collège
	 */
	private final String adresse; 
	
	public Site(String ad){
		this.adresse = ad;
	}
	
	//accesseur et mutateurs
	private String getAdresse() {
		return adresse;
	}
	
	//Méthodes
	/**
	 * Récupère l'id de l'élève associé à un identifiant du site internet.
	 * @param id
	 * @return
	 */
    public int getetBDD(String id){
        Connection conn = null;
        try {
    String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
    //connecte la bdd
    conn = DriverManager.getConnection(url);
    String query = "SELECT * FROM Code WHERE identifiant='";
    query =query+id+"'";
    //System.out.println(query);

            Statement state = MyConnection.getinstance().createStatement();

            ResultSet result = state.executeQuery(query);
            int i = result.getInt("idUtilisateur");
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
     * Récupération dans la base de données le mot de passe associé à la clef de l'étudiant correspondant
     * @param ideleve, clef représentant l'élève/
     * @return
     */
    public String getmdpBDD(int ideleve){
        Connection conn = null;
        try {
        	String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
        	//connecte la bdd
        	conn = DriverManager.getConnection(url);
        	String query = "SELECT * FROM Code WHERE idutilisateur = ";
        	query =query+ideleve;
        	//System.out.println(query);

            Statement state = MyConnection.getinstance().createStatement();

            ResultSet result = state.executeQuery(query);
            String i = result.getString("mdp");
            state.close();
            return i;

        } catch (SQLException e1) {
        	System.out.println(e1.getMessage());
        	return "erreur";
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
	 * Methode permettant de consulter les notes d'une matière spécifique
	 */
	public void voirNote(){
		Scanner scan = new Scanner(System.in);
		System.out.print("identifiant : "); 
		String ide = scan.nextLine(); // on récupère la commande entrée par l'utilisateur
		System.out.print("mot de passe : "); 
		String mdp = scan.nextLine();
		mdp = mdp.toLowerCase(); // on passe tout en minuscule (réduit le risque de faute de frappes)
		
		
		int ideleve = getetBDD(ide);
		String password = getmdpBDD(ideleve);
		
		if (!mdp.equals(password)){
			System.out.println( "mot de passe incorrect");
			scan.close();
		}
		else{
		System.out.print("matière : "); 
		String matiere = scan.nextLine();
		scan.close();
		Connection conn = null;
		try {
		    String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
		    //connecte la bdd
		    conn = DriverManager.getConnection(url);
		    String query = "SELECT valeur FROM Note WHERE idEt = ";
		    query =query+ideleve+" AND idmatiere ="+getIdMatiere(matiere,getIdCollegeBDD(getetBDD(ide)));

		            Statement state = MyConnection.getinstance().createStatement();

		            ResultSet result = state.executeQuery(query);
		            String note = result.getString("valeur");
		            
		            state.close();result.close();
		            System.out.println("Note de"+ matiere + " : "+ note);

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
	
	/**
	 * Permet de voir la salle dans laquelle une certaine matière est enseignée dans un collège spécifique
	 */
	public void voirSalle(){
		
		Scanner scan = new Scanner(System.in);
		System.out.print("identifiant : "); 
		String ide = scan.nextLine(); // on récupère la commande entrée par l'utilisateur
		System.out.print("mot de passe : "); 
		String mdp = scan.nextLine();
		mdp = mdp.toLowerCase(); // on passe tout en minuscule (réduit le risque de faute de frappes)
		
		int ideleve = getetBDD(ide);
 		String password = getmdpBDD(ideleve);
 		
 		if (!mdp.equals(password)){
 			System.out.println("mot de passe incorrect");
 			
 		}
		
 		else{System.out.print("matière : "); 
		String matiere = scan.nextLine();
		
		int idmatiere = getIdMatiere(matiere,getIdCollegeBDD(getetBDD(ide)));
	
		scan.close();
		 Connection conn = null;
		        try {
		            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db"; //adresse de la base
		            // create a connection to the database
		            conn = DriverManager.getConnection(url);
		            
		           

		            String query = "SELECT numero FROM Salle WHERE idmatiere =";
		            query =query+idmatiere;
		            
		            //System.out.println(query);

		                    Statement state = MyConnection.getinstance().createStatement();

		                    ResultSet result = state.executeQuery(query);
		                    int i = result.getInt("numero");
		                    String salle = Integer.toString(i);
		                    state.close();result.close();
		                    System.out.println("La salle est "+ salle); 

		        } catch (SQLException e1) {
		            System.out.println(e1.getMessage());
		            System.out.println("erreur");
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
		                    



	/**
	 * Récupére l'identifiant d'une matiere à partir de son nom et l'identifiant du collège dans lequel elle est enseignée
	 * 
	 * @param matiere, nom de la matière
	 * @param idcollege, identifiant du collège dans lequel la matière est enseignée
	 * @return identifiant de la matière
	 */
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
   * Récupére l'identifiant du collège dans lequel est scolarisé un étudiant
   * @param id, identifiant de l'étudiant
   * @return identifiant du collège
   */
    public int getIdCollegeBDD(int id){
        Connection conn = null;
        try {
        	String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
        	//connecte la bdd
        	conn = DriverManager.getConnection(url);
        	String query = "SELECT idCol FROM Etudiant WHERE id=";
        	query =query+id;
        	//System.out.println(query);

            Statement state = MyConnection.getinstance().createStatement();

            ResultSet result = state.executeQuery(query);
            int i = result.getInt("idCol");
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
	 * Permet d'accèder à l'adresse mail d'un enseignant
	 * 
	 * @param enseignant, enseignant dont on souhaite le mail.
	 */
	public void voirMail(Enseignant enseignant){

		Scanner scan = new Scanner(System.in);
		System.out.print("identifiant : "); 
		String ide = scan.nextLine(); // on récupère la commande entrée par l'utilisateur
		System.out.print("mot de passe : "); 
		String mdp = scan.nextLine();
		mdp = mdp.toLowerCase(); // on passe tout en minuscule (réduit le risque de faute de frappes)
		int ideleve = getetBDD(ide);
		String password = getmdpBDD(ideleve);
		
		if (!mdp.equals(password)){
			System.out.println("mot de passe incorrect");
		}
		
		else{
			System.out.print("nom du professeur : "); // espérer sans faute et majuscule
		
		String prof = scan.nextLine();		
		
		scan.close();
		
		

		 Connection conn = null;
		        try {
		            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db"; //adresse de la base
		            conn = DriverManager.getConnection(url);
		            	    		 
		    		String query = "SELECT * FROM Enseignant WHERE nom ='";
		            query =query+prof+"' AND (idCollSec = (SELECT idCol FROM ETUDIANT WHERE id ="+ideleve;
		            query = query +") OR idCollPrinc = (SELECT idCol From ETUDIANT WHERE id ="+ideleve +"))";
		            

		                    Statement state = MyConnection.getinstance().createStatement();

		                    ResultSet result = state.executeQuery(query);
		                    String mail = result.getString("mail");
		                    state.close(); result.close();
		                    System.out.println(mail);

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

}