package database.databaseUtilities;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * LES CLASSES IMPLEMENTANT CETTE INTERFACE DOIVENT DISPOSEES DES ATTRIBUTS SUPPLEMENTAIRES :
 * private String values ; private HashMap<String, fieldType> map;
 */
public abstract class SqlEntity {

    protected String values;
    protected final static HashMap<String, FieldType> struct = new HashMap<>();
    protected static String tableName = null;
    private static boolean hashmapInitialized = false;

    protected SqlEntity(String tableName) {
        if(SqlEntity.tableName == null) {SqlEntity.tableName = tableName;}
        this.getStruct(SqlEntity.tableName);
    }

    /**
     * REMPLIE DANS LA CLASSE UNE HashMap<String, fieldType> LE NOM DES CHAMPS ET LE TYPE DE VARIABLE ET CREE LA CHAINE values ...
     * Il faut implémenter la string values dans les classes filles après un appel a super
     */
    protected void getStruct(String table) {
        if(!hashmapInitialized) {
            try {
                Connection conn = ConnectDatabase.getConnection();
                DatabaseMetaData databaseMetaData = conn.getMetaData();
                ResultSet columns = databaseMetaData.getColumns(null, null, table, null);

                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String datatype = columns.getString("DATA_TYPE");
                    this.struct.put(columnName, FieldType.getFieldType(datatype));
                }
                conn.close();
                hashmapInitialized = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * RETOURNE UNE CHAINE DE CARACTERE PRE-REMPLIE PERMETTANT DE COMPOSER LA REQUETE INSERT ...
     *
     * @return
     */
    public String getValues() {
        this.getStruct();
        return this.values;
    }

    /**
     * GETTER DE LA MAP CREE AVEC LA METHODE getStruct ...
     *
     * @return
     */
    public HashMap<String, FieldType> getMap() {
        return this.struct;
    }

    /**
     * METHODE PERMETTANT DE VERIFIER QUE LA TABLE ET L'INSTANCE PARTAGE LES MEMES ATTRIBUTS ET MEMES TYPES
     * PREND EN PARAMETRE LA MAP ATTRIBUT/TYPE DE LA TABLE ...
     *
     * @param tableStruct
     * @return
     */
    public boolean check(HashMap<String, FieldType> tableStruct) {

        for (String key : tableStruct.keySet()) {
            if (tableStruct.get(key) != struct.get(key))
                return false;
        }
        return true;
    }

    /**
     * Implémentation dans les classes filles pour générer les values et la hashmap
     */
    public abstract void getStruct();
}
