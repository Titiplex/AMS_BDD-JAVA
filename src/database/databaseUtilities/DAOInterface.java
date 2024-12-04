package database.databaseUtilities;

import java.util.List;

public interface DAOInterface<U> {

    /**Permet de créer une Liste d'un objet U en récupérant ses instances dans la base de données (Postgresql).
     *
     */
    public List<U> listAll();

    /**Permet d'insérer dans une table sql
     *
     */
    public void insertInTable(U entity);

    /**Modifier une entité
     *
     * @param entity
     */
    public void modifyEntity(U entity);

    /**Récupère un objet dans la bdd depuis son identifiant
     *
     * @param id
     * @return
     */
    public U getById(int id);

    /**Supprimer une entité
     *
     * @param entity
     */
    public void deleteEntity(U entity);
}
