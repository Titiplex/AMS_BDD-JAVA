package database.databaseUtilities;

import java.sql.SQLException;
import java.util.List;

public interface JoinDAOInterface<U, A> extends DAOInterface<U> {
    /**Returns a list of objects from a join query in sql
     *
     * @param joinEntity
     * @return
     */
    public List<U> listAllFromJoin(A joinEntity);
}
