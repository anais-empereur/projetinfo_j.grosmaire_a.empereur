package projetinfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Matiere implements IFonctionsBDD {
	/**
	 * Le nom de la matière fait partie de l'énumération Matiereenum, l'énumération des différentes matières proposées dans l'académie
	 */
	private final Matiereenum nom;
	private Departement departement;
		
	public Matiere(Matiereenum nom, Departement d) {
		this.nom = nom;
		this.departement = d;
	
		if (!this.presenceBDD()){Connection conn = null;
        try {
            // Paramètres de la base de données
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // Création de la connection à la base de données
            conn = DriverManager.getConnection(url);
    		
            Statement st = conn.createStatement(); 
            
            st.executeUpdate("INSERT INTO Matiere " + 
                "VALUES (1+(SELECT MAX (id) FROM Matiere ),'"+this.getnomstring()+"','"+this.getDepartement().getNom()+"',"+this.getDepartement().getidBDD()+")"); 

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
	
	public String getnomstring (){
		return(this.nom.name());
	}
	
	
	
	public Matiereenum getNom() {
		return nom;
	}
	public Departement getDepartement() {
		return departement;
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
	 * Récupération de l'indice de la matière dans la base de donnée
	 * Implemente l'interface IFonctionsBDD
	 * 
	 * @return Indice de la matière dans la base de données, un entier
	 */
	public int getidBDD (){
		return(getIdMatiere(this.getnomstring(),this.getDepartement().getCollege().getidBDD()));
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
            

            String query = "SELECT COUNT(*) as total FROM Matiere WHERE iddepartement ='"+this.getDepartement().getidBDD()+"' AND nom='"+this.getNom()+"'";
            
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
	
	/**
	 * Calcul la moyenne d'une matière au niveau de l'académie
	 * @return La moyenne
	 */
    public double  moyenne() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/home/prof/workspace/bdd.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            // Récupération des notes de la matière
            String query = "SELECT * FROM Note n JOIN Matiere m ON m.id = n.idmatiere WHERE m.nom =";
            query =query+"'"+this.getnomstring()+"'";
            PreparedStatement prepare = MyConnection.getinstance().prepareStatement(query);
            
    		Statement state = MyConnection.getinstance().createStatement();
    				
    		ResultSet result = state.executeQuery(query);
    		
    		// Calcul de la moyenne
    		double moy=0;
    		int nb = 0;
    		while(result.next()){
    			moy=moy+result.getFloat("valeur");
    			nb ++;}
    		moy = moy/nb;
    		prepare.close(); state.close();
    		 //System.out.println(moy);
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

}