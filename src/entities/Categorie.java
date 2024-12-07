package entities;

import java.util.TreeSet;

import database.databaseUtilities.SqlEntity;

public class Categorie {

	private String categorie;
	
	
	public Categorie(String singleCategorie) {
		super();
		this.categorie = singleCategorie;
	}

	public String getCategorie() {
		return categorie;
	}
}
