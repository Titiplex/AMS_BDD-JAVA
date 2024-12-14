package database.databaseUtilities;

import java.util.List;

public interface JoinDAOInterface<U, A> extends DAOInterface<U> {
    /**
     * Retrieves a list of all primary entities associated with the given join entity.
     * (comment generated with AI)
     *
     * @param joinEntity the join entity used to filter and retrieve the associated primary entities
     * @return a list of primary entities associated with the specified join entity
     */
    List<U> listAllFromParameter(A joinEntity);

    /**
     * Inserts a primary entity into a database table along with an associated join entity.
     * (comment generated with AI)
     *
     * @param entity     the primary entity to be inserted into the database table
     * @param joinEntity the associated entity that defines the join relationship with the primary entity
     */
    void insertInTable(U entity, A joinEntity);
}
