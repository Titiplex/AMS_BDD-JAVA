package database.databaseUtilities;

import java.util.List;

public interface JoinDAOInterface<U, A> extends DAOInterface<U> {
    /**
     * Lister toutes les entités depuis un paramètre (ex : les join en sql)
     *
     * @param joinEntity
     * @return
     */
    List<U> listAllFromParameter(A joinEntity);

    /**
     * Insérer une entité en join
     *
     * @param entity
     * @param joinEntity
     */
    void insertInTable(U entity, A joinEntity);
}
