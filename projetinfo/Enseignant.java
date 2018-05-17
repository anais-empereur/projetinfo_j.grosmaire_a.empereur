
package projetinfo;

//imports 
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Enseignant implements IFonctionsBDD{
	//attributs
	/**
	 * L'attribut nom est le nom de famille de l'enseignant
	 * On considère qu'il ne peut pas changer
	 */
	private final String nom;
	/**
	 * L'attribut prenom est le prenom de l'enseignant
	 * On considère que le prénom ne peut pas changer
	 */
	private final String prenom;	
	private Adresse adresse;
	private final String mail;	//on considère que le mail ne peut pas changer
	private final String date;
	private Matiere matiere;	//matière que le professeur enseigne
	private College collPrinc;	//college principal d'affectaton du professeur
	private College collSec;	//college secondaire d'affectaton du professeur (optionnel)
	private String telephone;
	
	//Accesseurs
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public Adresse getAdresse() {
		return adresse;
	}

	public String getMail() {
		return mail;
	}
	public String getDate() {
		return date;
	}
	public Matiere getMatiere() {
		return matiere;
	}

	public College getCollPrinc() {
		return collPrinc;
	}
	public College getCollSec() {
		return collSec;
	}

	

	/**
	 * Constructeur de l'enseignant si il travaille dans deux collèges
	 * @param nom
	 * @param prenom
	 * @param CollPrinc, le collège principale de l'enseignant
	 * @param CollSec, le collège secondaire de l'enseignant
	 * @param adresse
	 * @param matiere
	 * @param date
	 */
	
	public Enseignant(String nom, String prenom,College CollPrinc, College CollSec, Adresse adresse, Matiere matiere, String date,String tel) {
		// Affectation des attributs
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.telephone=tel;
		String[] val = CollPrinc.getNom().split(" "); 
		if (val.length == 2){
			this.mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"."+matiere.getnomstring()+"@"+CollPrinc.getNom().split(" ")[0].toLowerCase()+CollPrinc.getNom().split(" ")[1].toLowerCase()+".fr";//Il n'y a que deux mots 
		
		}else{
			this.mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"."+matiere.getnomstring()+"@"+CollPrinc.getNom().toLowerCase()+".fr";
		}
		this.date = date;//????
		
		this.matiere = matiere;
		this.collPrinc = CollPrinc;
		this.collSec = CollSec;
		
		int idCollPrinc = CollPrinc.getidBDD();
		int idCollSec = CollSec.getidBDD();
		int idAd = adresse.getidBDD();
		
		//constructeur particulier : crée le nouvel enseignant dans la base de donnée
	
		if (!this.presenceBDD()){Connection conn = null;
	    try {
	        String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
	        // connecte la bdd
	        conn = DriverManager.getConnection(url);
	        Statement st = conn.createStatement(); 
	        
	        st.executeUpdate("INSERT INTO Enseignant " + 
	        "VALUES (1+(SELECT MAX (id) FROM Enseignant),'"+nom+"','"+prenom+"',"+idCollPrinc+","+idCollSec+","+idAd+",'"+mail+"','"+matiere.getnomstring()+"','"+this.telephone+"')"); 

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
	 * Constructeur de l'enseignant si il travaille dans un seul collège
	 * @param nom
	 * @param prenom
	 * @param CollPrinc, le collège principale de l'enseignant
	 * @param CollSec, le collège secondaire de l'enseignant
	 * @param adresse
	 * @param matiere
	 * @param date
	 */
	public Enseignant(String nom, String prenom,College CollPrinc, Adresse adresse, Matiere matiere, String date,String tel) {
		// Affectation des attributs
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.telephone=tel;
		String[] val = CollPrinc.getNom().split(" "); 
		if (val.length == 2){
			this.mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"."+matiere.getnomstring()+"@"+CollPrinc.getNom().split(" ")[0].toLowerCase()+CollPrinc.getNom().split(" ")[1].toLowerCase()+".fr";//Il n'y a que deux mots 
		
		}else{
			this.mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"."+matiere.getnomstring()+"@"+CollPrinc.getNom().toLowerCase()+".fr";
		}
		this.date = date;//????
		
		this.matiere = matiere;
		this.collPrinc = CollPrinc;
		
		int idCollPrinc = CollPrinc.getidBDD();
		int idAd = adresse.getidBDD();
		
		//constructeur particulier : crée le nouvel enseignant dans la base de donnée
	
		if (!this.presenceBDD()){Connection conn = null;
	    try {
	        String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
	        // connecte la bdd
	        conn = DriverManager.getConnection(url);
	        Statement st = conn.createStatement(); 
	        
	        st.executeUpdate("INSERT INTO Enseignant " + 
	        "VALUES (1+(SELECT MAX (id) FROM Enseignant),'"+nom+"','"+prenom+"',"+idCollPrinc+",NULL,"+idAd+",'"+mail+"','"+matiere.getnomstring()+"','"+this.telephone+"')"); 

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
	
	
	
 //Methodes
	/**
	 * Récupère l'identifiant de l'enseignant dans la base de données
	 * Implémentation de l'interface IFonctionsBDD
	 * 
	 * @return L'identifiant dans la base de données de l'enseignant, un entier
	 * @throws SQLException
	 */
	public int getidBDD (){
        Connection conn = null;
        try {
        	// db parameters
        	String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
        	//create a connection to the database
        	conn = DriverManager.getConnection(url);

        	int col = this.getCollPrinc().getidBDD();
        	// Requête SQL
        	String query = "SELECT * FROM Enseignant WHERE nom ='";
        	query =query+this.getNom()+"' AND prenom ='"+this.getPrenom()+"' AND idCollPrinc ="+ col;
       


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
	 * Déménagement d'un professeur vers une autre adresse
	 * @param adresse, nouvelle adresse du professeur
	 */
	public void demenager(Adresse adresse){
		this.adresse = adresse;
		Connection conn = null;
        try {
            // paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Connexion
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement(); 

			String query = "UPDATE Enseignant SET idAd = "+ adresse.getidBDD()+" WHERE id ="+this.getidBDD();
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
	 * écrit les éléments constitutifs de la fiche de l'enseignant dans un fichier
	 */
	public void fiche(){
        String filePath = "//home/prof/Images/"+this.prenom.substring(0,1).toLowerCase()+this.nom.toLowerCase()+this.collPrinc.getNom().toLowerCase()+".txt";
        Path logFile = Paths.get(filePath);
        if (!Files.exists(logFile)) {
                try {
                        Files.createFile(logFile);
        }
                catch (IOException e) {
                        e.printStackTrace();
        } }

        try (BufferedWriter writer =
Files.newBufferedWriter(logFile,StandardCharsets.UTF_8,
StandardOpenOption.WRITE)) {
                writer.write(this.nom+" "+this.prenom+" / "+this.mail+"  /  "+ this.telephone);
                        writer.close();
                }
        catch (IOException e) {
                        e.printStackTrace();
}


}
	
	/**
	 * Calcul de la distance entre le domicile d'un professeur et un collège où il enseigne
	 * 
	 * @caractere détermine si le collège est le collège principal ou secondaire.
	 * @return La distance entre le domicile d'un professeur et le collège choisi
	 */
	public double distance(Caractere caractere){
		if (caractere.toString() == "principal"){
			return this.getAdresse().calculeDist(this.getCollPrinc().getAdresse());
		} else{
			return this.getAdresse().calculeDist(this.getCollSec().getAdresse());
		}
		
	}
	
	/**
	 * permet de changer le collège d'affectation d'un professeur 
	 * @param college, le nouveau collège de l'enseignant
	 * @param caractere, détermine si l'enseignant change de collège principal ou secondaire
	 */
	public void changerCollege(College college, Caractere caractere){
		// Récupération des identifiants nécessaires dans la base de données
		int id = this.getidBDD();
		int id2 = college.getidBDD();
		Connection conn = null;
        try {
            // paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Connexion
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement(); 
		if (caractere.toString() == "principal"){
			this.collPrinc = college;
			String[] val = this.getCollPrinc().getNom().split(" ");
			String mail = "";
			if (val.length == 2){
				mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"."+this.getMatiere().getnomstring()+"@"+this.getCollPrinc().getNom().split(" ")[0].toLowerCase()+this.getCollPrinc().getNom().split(" ")[1].toLowerCase()+".fr";//Il n'y a que deux mots 
			
			}else{
				mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"."+this.getMatiere().getnomstring()+"@"+this.getCollPrinc().getNom().toLowerCase()+".fr";
			}
			String query = "UPDATE Enseignant SET idCollPrinc = "+ id2 +", mail='"+mail+"' WHERE id ="+id;
			st.executeUpdate(query);st.close();
		}else{
			this.collSec = college;
			String query = "UPDATE Enseignant SET idCollSec = "+ id2 +" WHERE id ="+id;
			st.executeUpdate(query);st.close();
		}
		
	
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
	 * Détermine si l'enseignant est dans la base de données
	 * Implémentation de l'interface IFonctionsBDD
	 * 
	 * @return Le boolean true ou false si l'enseignant est dans la base de données ou non
	 * @throws SQLException
	 */
    public boolean presenceBDD(){
		Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //connection à la base de données
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT COUNT(*) as total FROM Enseignant WHERE nom ='"+this.getNom() +"' AND idCollPrinc ="+this.getCollPrinc().getidBDD();
            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("total");
    		state.close();result.close();
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