package projetinfo;

import java.sql.SQLException;

public interface IFonctionsBDD {
	/**
	 * Récupère l'identifiant dans la base de données
	 * 
	 * @return L'identifiant dans la base de données, un entier
	 * @throws SQLException
	 */
	public int getidBDD();
	/**
	 * Détermine si l'élément est dans la base de données
	 * 
	 * @return Le boolean true ou false si l'élement est dans la base de données ou non
	 * @throws SQLException
	 */
	public boolean presenceBDD();

}