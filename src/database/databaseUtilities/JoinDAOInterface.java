package database.databaseUtilities;

import java.util.List;

public interface JoinDAOInterface<U, A> extends DAOInterface<U> {
    /**
     * Returns a list of objects from a join query in sql or where parameter
     *
     * @param joinEntity
     * @return
     */
    List<U> listAllFromParameter(A joinEntity);
}
