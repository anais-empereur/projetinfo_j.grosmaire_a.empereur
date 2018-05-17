package projetinfo;

//Imports
import java.awt.BorderLayout;
import javax.swing.JFrame;
import projetinfo.Adresse;

public class Academie {
	
	public static void main(String[] args) {
		
		
		
		Adresse adresse1 = new Adresse(450.21f,54.165f,6,"rue du metagabro","Ekaterinbourg",78500);
    	Adresse adresse2 = new Adresse(562.52f,62.25f,5,"place de la sylphide","irkoutsk",95100);
    	Adresse adresse3 = new Adresse(5.24185f,4.2574f,5,"impasse vega","Sachaz",74500);
    	Adresse adresse4 = new Adresse(4.24f,0.25f,5,"lieu dit du gros caillou","Gouillac",24500);
    	
    	College collPrinc = new College(adresse1,"Ormesson", 63);
    	College collSec = new College(adresse2,"Les Eyzies",63);
    	College coll3 = new College(adresse4,"Le moustier",63);
    	
    	Etudiant etudiant1 = new Etudiant("rousette","larson",3,collPrinc, adresse1,false, 4, "hdfzev","0632589745");
    	Etudiant etudiant2 = new Etudiant("lavabo", "cyril", 5, collPrinc, adresse2, true, 3, "mdp1","0985741236");
    	Etudiant etudiant3 = new Etudiant("lavabo", "clement", 3, collPrinc, adresse2, false, 4, "mdp2","0147859623");
    	Etudiant etudiant4 = new Etudiant("triss", "charline", 2, collPrinc, adresse3, true, 6, "mdp3","0147859623");
    	
    	Departement departement1 = new Departement(Departementenum.Sciences, collPrinc);
    	Departement departement2 = new Departement(Departementenum.Sciences, collSec);

    	
    	Matiere matiere = new Matiere(Matiereenum.SVT, departement1);
    	Matiere matiere2 = new Matiere(Matiereenum.Mathématiques, departement1);
    	
    	
//    	Salle salle = new Salle(12,54,matiere);
//    	salle.changerMatiere(matiere2);

//    	Enseignant enseignant = new Enseignant("piolet", "yves",collPrinc,collSec,adresse1, matiere, "juin2019","0636259845");
    	
//    	Enseignant.changerCollege(collSec,Caractere.principal);
//    	enseignant.demenager(adresse4);
//    	departement.setRespo(enseignant);
    	
    	Note note = new Note(10,matiere,etudiant1);
    	Note note1 = new Note(9,matiere,etudiant1);
    	Note note2 = new Note(12,matiere,etudiant3);
    	Note note3 = new Note(10,matiere,etudiant2);
    	
    	Note note4 = new Note(15,matiere2,etudiant4);
    	Note note5 = new Note(4,matiere2,etudiant1);
    	Note note6 = new Note(14,matiere2,etudiant3);
    	Note note7 = new Note(10,matiere2,etudiant2);
//    	etudiant1.changerNote(note1);
    	
    	
    	System.out.println("La moyenne de SVT dans l'académie est : "+ matiere.moyenne());
    	System.out.println("La moyenne de mathématiques dans l'académie est : "+ matiere2.moyenne());
    	
//    	double moy2 = etudiant1.moyenne();
//    	etudiant2.gestion(moy2); 
//    	double moy3 = etudiant2.moyenne();
//    	etudiant3.gestion(moy3);
//    	double moy1 = etudiant1.moyenne();
//    	etudiant1.gestion(moy1);
    	
    	
//    	Site site = new Site("http://pamplemousse.eu");
//    	site.voirSalle();
//    	site.voirNote();
//    	site.voirMail(enseignant);
//    	enseignant.fiche();
    	
    	
//    	
//    	JFrame fenetre = new JFrame();
//      
//          fenetre.setTitle("Distance");
//          
//          fenetre.setSize(400, 100);
//        
//          fenetre.setLocationRelativeTo(null);
//
//          fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    
//          fenetre.setVisible(true);
//          AffichageGraphique a=new AffichageGraphique ("La distance entre les deux adresse est "+String.valueOf(enseignant.distance(Caractere.principal)+" m"));
//          fenetre.getContentPane().add(a, BorderLayout.CENTER);
	}

}