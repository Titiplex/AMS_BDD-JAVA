package database.databaseUtilities;

public interface SetterInterface<O> {

    /**Calls all sets of all attributs to modify the sql instance
     *
     * @param object
     */
    void setter(O object);
}
