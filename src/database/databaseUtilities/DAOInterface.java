package database.databaseUtilities;

import java.util.List;

public interface DAOInterface<U> {

    /**
     * Permet de créer une Liste d'un objet U en récupérant ses instances dans la base de données (Postgresql).
     */
    List<U> listAll();

    /**
     * Permet d'insérer dans une table sql
     * Modifie l'id de l'entité passée en paramètre avec l'id générée par sql
     */
    void insertInTable(U entity);

    /**
     * Modifier une entité
     *
     * @param entity
     */
    void modifyEntity(U entity);

    /**
     * Récupère un objet dans la bdd depuis son identifiant
     *
     * @param id
     * @return
     */
    U getById(int id);

    /**
     * Supprimer une entité
     *
     * @param entity
     */
    void deleteEntity(U entity);
}
