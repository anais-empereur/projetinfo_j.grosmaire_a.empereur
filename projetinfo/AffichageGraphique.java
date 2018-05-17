package projetinfo;

//Imports
import java.awt.Graphics;
import javax.swing.JPanel;
 
public class AffichageGraphique extends JPanel {
	/**
	 * Texte à afficher dans la fenêtre générée
	 */
	String b;
	
	/**
	 * Constructeur
	 * @param s, texte à afficher dans la fenêtre
	 */
	public AffichageGraphique(String s){
		b = s;
	}
	
	/**
	 * Permettant d'écrire dans la fenêtre
	 */
	public void paintComponent(Graphics g){
	    g.drawString(b, 10, 20);
	  }  
}