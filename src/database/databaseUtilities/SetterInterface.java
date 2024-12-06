package database.databaseUtilities;

public interface SetterInterface<O> {

    /**Calls all sets of all attributes to modify the sql instance
     *
     * @param object an entity
     */
    void setter(O object);
}
