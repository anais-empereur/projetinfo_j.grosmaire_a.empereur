package projetinfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Etudiant qui regroupe les étudiants de l'académie
 * @author formation
 *
 */
public class Etudiant implements IFonctionsBDD {
	private final String nom;
	private final String prenom;
	private String mail;
	private Adresse adresse;
	private String telephone;
	/**
	 * L'attribut entree indique depuis combien d'années l'étudiant est à l'école
	 */
	private int entree; //Depuis quand l'étudiant est à l'école en années
	/**
	 * L'attribut mdp est le mot de passe de l'élève nécessaire pour accéder au site internet de l'école.
	 */
	private String mdp; //Mot de passe pour le site internet de l'école
	/**
	 * Le booléen redoublement détermine si l'élève a déjà redoublé ou non.
	 */
	private boolean redoublement; //Est-ce que l'élève a déjà redoublé ?
	private int classe;
	private College college;
	/**
	 * L'identifient de l'élève est normalisé et non modifiable, il permet d'accéder au site internet de l'école.
	 */
	private String identifiant;
	
	/**
	 * Construction d'un étudiant
	 * Il est également ajouté à la base de données si il n'y est pas déjà.
	 * 
	 * @param n, correspond à un string, nom de l'élève
	 * @param p correspond au prénom de l'élève
	 * @param m correspond au mail de l'élève
	 * @param e correspond au nombre d'années depuis lesquelles l'étudiant est à son collège.
	 * @param col, collège dans lequel l'étudiant fait sa scolarité
	 * @param a, est l'adresse de l'élève
	 * @param bool est false si l'élève n'a jamais redoublé, il est true si il y a déjà redoublé
	 * @param clas est le niveau dans lequel est l'élève
	 * @param mdp0 est le mot de passe de l'élève pour accéder au site.
	 * @param tel est le numéro de téléphone de l'élève
	 */
	public Etudiant(String n,String p, int e, College col, Adresse a, boolean bool, int clas, String mdp0, String tel){
		nom = n; prenom=p;entree =e; college = col;redoublement = bool; classe = clas; mdp = mdp0.toLowerCase(); telephone = tel;
		String[] val = col.getNom().split(" "); 
		if (val.length == 2){
			mail = p+"."+n+"@"+col.getNom().split(" ")[0]+col.getNom().split(" ")[1]+".fr";
		} else{mail = p+"."+n+"@"+col.getNom()+".fr";}
		
		
		int valeur =0;
		if (bool){valeur = 1;}
		
		 // Si l'étudiant n'est pas déjà présent dans la base de données
		if (!this.presenceBDD()){Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Création de la connection à la base de données
            conn = DriverManager.getConnection(url);
    		
            Statement st = conn.createStatement(); 
            
            st.executeUpdate("INSERT INTO Etudiant " + 
                "VALUES (1+(SELECT MAX (id) FROM Etudiant),"+col.getidBDD()+","+a.getidBDD()+",'"+n+"','"+p+"',"+valeur+","+classe+",'"+tel+"')"); 

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
        
		this.identifiant();
		this.ajoutBDD(this.identifiant, mdp0);
		}
	}}
	
// Accesseurs et mutateurs
	
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getMail() {
		return mail;
	}
	public int getEntree() {
		return entree;
	}

	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
		Connection conn = null;
        try {
            // paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Connexion
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement(); 

			String query = "UPDATE Code SET mdp = '"+ mdp+"' WHERE id ="+this.getidBDD();
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
	public boolean isRedoublement() {
		return redoublement;
	}

	public int getClasse() {
		return classe;
	}
	public College getCollege() {
		return college;
	}
	
	public String getidentifiant (){
		return identifiant;
	}
	public Adresse getadresse(){
		return this.adresse;
	}

	
	/** 
	 * Calcul de la moyenne d'un élève
	 * @return La moyenne de l'élève, un flottant
	 * 
	 * @throws Si l'élève n'a aucune note, il retourne 0.
	 */
	public double moyenne(){
		
		Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT * FROM Note WHERE idEt ="+this.getidBDD();
        
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
    		prepare.close(); state.close();result.close();
    		return(moy);       
            
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            return(0.1);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
               // System.out.println(ex.getMessage());
            }
        }

	}

	/**
	 * Génération de l'identifiant de l'élève si il n'en a pas
	 * 
	 * @throws SQLException si problème de forme de la base de données.
	 */
	public void identifiant(){
		String id = this.getPrenom().substring(0, 1) + this.getNom();
		Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //create a connection to the database
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT COUNT(*) as total FROM Code c JOIN Etudiant e ON c.idUtilisateur = e.id WHERE identifiant LIKE '"+id+"%' AND e.idCol=" + this.getCollege().getidBDD();

            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		int i = result.getInt("total");
    		state.close();result.close();
    		if (i==0){
    			this.identifiant=id;
    			
    		} else{
    			this.identifiant = id+String.valueOf(i+1).toLowerCase();
    		}
    		
 

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
        }
		
		
	}
	
		/**
	 * Récupère l'identifiant dans la base de données de l'étudiant
	 * Implemente l'interface IFonctionsBDD
	 * 
	 * @return L'identifiant dans la base de données de l'étudiant, un entier
	 * @throws SQLException
	 */
	public int getidBDD (){
		Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //create a connection to the database
            conn = DriverManager.getConnection(url);
            
            int col = this.getCollege().getidBDD();
            String query = "SELECT * FROM Etudiant WHERE nom = '";
            query =query+this.getNom()+"' AND prenom = '"+this.getPrenom()+"' AND idCol ="+ col;
            //System.out.println(query);
            
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
            

            String query = "SELECT COUNT(*) as total FROM Etudiant WHERE nom ='"+this.getNom()+"' AND prenom='"+this.getPrenom()+"' AND idCol =" + this.getCollege().getidBDD();

            
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

	/**
	 * Création de la fiche de renseignement de l'élève
 	 * Enregistre la fiche dans les documents.
 	 */
	public void fiche(){
		String filePath = "//home/prof/Images/"+this.identifiant+".txt"; 
		Path logFile = Paths.get(filePath);
		if (!Files.exists(logFile)) { 
			try {
				Files.createFile(logFile);
		}
			catch (IOException e) {
				e.printStackTrace();
		} }
		
		try (BufferedWriter writer = Files.newBufferedWriter(logFile,StandardCharsets.UTF_8, StandardOpenOption.WRITE)) { 
			writer.write(this.nom+" "+this.prenom+" / "+this.mail+"  /  " +this.telephone);
				writer.close();
			} 
		catch (IOException e) {
				e.printStackTrace();
 }

		
	}
	
	/**
	 * Gestion de la base de donnée basée sur la diagramme d'activité
	 * Il assure la gestion des élèves
	 */
	public void gestion(double moy){
		Connection conn = null;
        try {
            // paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Connexion
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement(); 

    		
    		if (moy>10){ // Si l'élève a la moyenne
    			if (this.getClasse()==3){
    				// Si l'élève est en troisième, il quitte le collège
    			String query = "DELETE FROM Etudiant WHERE id ="+this.getidBDD()+" ; DELETE FROM Note WHERE idEt ="+this.getidBDD()+" ; DELETE FROM Code Where idutilisateur ="+this.getidBDD();
    			st.executeUpdate(query); st.close();
    			conn.close();
    			
    			
    			} else{ // Sinon il passe à la classe supérieure
    				int cl = this.getClasse()-1;
    				this.classe = this.getClasse()-1;
    				String query = "UPDATE Etudiant SET classe = "+ cl +" WHERE id ="+this.getidBDD();
    				st.executeUpdate(query);st.close();
    				conn.close();
    			}
    			}
    		else{// Si l'élève n'a pas la moyenne
    			if (redoublement == false ){// Si l'élève n'a jamais redoublé, il a le droit de redoubler
    				String query = "UPDATE Etudiant SET redoublement= 1 WHERE id ="+this.getidBDD();
    				redoublement = true;
    				st.executeUpdate(query);st.close();
    				conn.close();
    				
    			}
    			else{// Si il a déjà redoublé
    				if (this.getClasse()==3){
        				// Si l'élève est en troisième, il quitte le collège
    					String query = "DELETE FROM Etudiant WHERE id ="+this.getidBDD()+" ; DELETE FROM Note WHERE idEt ="+this.getidBDD()+" ; DELETE FROM Code Where idutilisateur ="+this.getidBDD();
    	    			st.executeUpdate(query); 
    	    			st.close();
    	    			conn.close();
    				} else{
    					// Si l'élève n'est pas en troisième 
    					int cl = this.getClasse()-1;
    					this.classe = this.getClasse()-1;
        				String query = "UPDATE Etudiant SET classe = "+ cl +" WHERE id ="+this.getidBDD();
        				st.executeUpdate(query);st.close();
        				conn.close();
    				}
    				
    			}
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
	 * Détermine si l'élève a une note ou non dans une certaine matière.
	 * @param On rentre en entrée un nom de matière
	 * @return Un booléen déterminant si l'élève a une note ou non.
	 */
	public boolean aUneNote (Matiereenum m){

		Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            //connection à la base de données
            conn = DriverManager.getConnection(url);
            

            String query = "SELECT COUNT(*) as total FROM Note n JOIN Matiere m ON n.idmatiere = m.id WHERE n.idEt ="+this.getidBDD()+" AND m.nom='"+m.name()+"'";
            
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
            }        }
	}
	
	/**
	 * Détermination des matières non suivies par un élève
	 * @return Renvoie la liste du nom des matières qui ne sont pas suivies par un élèves
	 */
	public List<String> pasDeNotes(){
		List<String> mat = new ArrayList<String>();
		 for(Matiereenum m : Matiereenum.values()){
			 if (! this.aUneNote(m)){
				 mat.add(m.toString());
			 }
		 }
		 return mat; 
	}
	
	/** 
	 * Détermination des matières suivies par un élève
	 * @return Retourne la liste des matières suivies par l'élève
	 */
	public List<String> suiviMatiere(){
		List<String> mat = new ArrayList<String>();
		 for(Matiereenum m : Matiereenum.values()){
			 if (this.aUneNote(m)){
				 mat.add(m.toString());
			 }
		 }
		 return mat; 
	}

	
	/**
	 * Changement de la note associée à une matière
	 * @param no, la nouvelle note de l'étudiant
	 */
	public void changerNote(Note no){
		Connection conn = null;
		try {
		            // paramètres de la base de données
		            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
		            // Connection à la base de données
		            conn = DriverManager.getConnection(url);

		    		
		            Statement st = conn.createStatement(); 
		            
		            st.executeUpdate("UPDATE Note " + 
		                "SET valeur ="+no.getValeur()+" WHERE id="+no.getidBDD()); 

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
	 * Ajout de l'identifiant d'un étudiant à la table Code de la base de données
	 */
	public void ajoutBDD(String identifiant, String mdp){
		Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Création de la connection à la base de données
            conn = DriverManager.getConnection(url);
    		
            Statement st = conn.createStatement(); 
            
            st.executeUpdate("INSERT INTO Code " + 
                "VALUES (1+(SELECT MAX (id) FROM Code ),'"+identifiant.toLowerCase()+"','"+mdp.toLowerCase()+"',(SELECT MAX (id) FROM Etudiant ))"); 

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