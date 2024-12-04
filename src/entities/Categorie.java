package entities;

import java.util.TreeSet;

import database.databaseUtilities.SqlEntity;

public class Categorie extends SqlEntity {
	
	private static final TreeSet<String> categories = new TreeSet<>(); // liste ordonnée des catégories à utiliser pour affichage
	private String singleCategorie;
	
	
	public Categorie(String singleCategorie) {
		super();
		this.singleCategorie = singleCategorie;
		Categorie.categories.add(singleCategorie);
	}

	@Override
	public void getStruct() {
		// TODO Auto-generated method stub
		super.getStruct("Categorie");
		
		this.values = "('" + 
				this.singleCategorie.replace("'", "''") + "'" + 
				")";
	}
}
