package database.databaseUtilities;

import entities.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.sql.*;


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
        if (SqlEntity.tableName == null) {
            SqlEntity.tableName = tableName;
        }
        this.getStruct(SqlEntity.tableName);
    }

    /**
     * REMPLIE DANS LA CLASSE UNE HashMap<String, fieldType> LE NOM DES CHAMPS ET LE TYPE DE VARIABLE ET CREE LA CHAINE values ...
     * Il faut implémenter la string values dans les classes filles après un appel a super
     */
    protected void getStruct(String table) {
        if (!hashmapInitialized) {
            try {
                Connection conn = ConnectDatabase.getConnection();
                DatabaseMetaData databaseMetaData = conn.getMetaData();
                ResultSet columns = databaseMetaData.getColumns(null, null, table, null);

                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String datatype = columns.getString("DATA_TYPE");
                    struct.put(columnName, FieldType.getFieldType(datatype));
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
        return this.values;
    }

    /**
     * GETTER DE LA MAP CREE AVEC LA METHODE getStruct ...
     *
     * @return
     */
    public HashMap<String, FieldType> getMap() {
        return struct;
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

    protected <SqlEntity> void createValues(SqlEntity object) {
        Class<?> classe = object.getClass();
        //initialisation values
        this.values = "";
        this.values += "(";

        Field[] fields = classe.getDeclaredFields();
        //Parcourir chaque champ
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                //Récupérer la valeur du champ
                Object fieldValue = field.get(object);
                //Surement utiliser .equals("id")
                if (field.getName().equals("id")) {
                    continue;
                }
                //check le type du champ
                if (fieldValue == null) {
                    this.values += "NULL, ";
                } else if (fieldValue instanceof String) {
                    String standardValue = "'" + fieldValue.toString().replace("'", "''") + "'";
                    this.values += standardValue + ", ";
                } else if (fieldValue instanceof Number) {
                    this.values += fieldValue + ", ";
                }
            } catch (IllegalAccessException e) {
                System.out.println("Impossible d'accéder au champ : " + field.getName());
            }
            field.setAccessible(false);
        }
        // enlever ", " et ajouter ")"
        //on vérifie pour pas faire d'erreur mémoire ou un pb
        if (this.values.length() > 1) {
            this.values = this.values.substring(0, this.values.length() - 2);
        }
        this.values += ")";
    }

    public void modifyEntity(SqlEntity entity) {
        Class<?> classe = entity.getClass();
        String query = "UPDATE " + tableName + " SET ";

        //Récupération des fields
        Field[] fields = classe.getDeclaredFields();
        for (Field field : fields) {
            //On les rend public
            field.setAccessible(true);
            try {
                // Récupérer la valeur du champ
                Object fieldValue = field.get(entity);

                if (!field.getName().equals("id")) { // Ignorer le champ "id"
                    query += field.getName() + " = ";

                    if (fieldValue == null) {
                        query += "NULL, ";
                    } else if (fieldValue instanceof String) {
                        String standardValue = "'" + fieldValue.toString().replace("'", "''") + "'";
                        query += standardValue + ", ";
                    } else if (fieldValue instanceof Number) {
                        query += fieldValue + ", ";
                    } else if (fieldValue instanceof LocalDate) {
                        LocalDate dateValue = (LocalDate) fieldValue;
                        String dateString = "'" + dateValue.toString() + "'"; // Format SQL : 'yyyy-MM-dd'
                        query += dateString + ", ";
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Impossible d'accéder au champ : " + field.getName(), e);
            }
        }

        // Enlever la dernière virgule
        if (query.endsWith(", ")) {
            query = query.substring(0, query.length() - 2);
        }

        // Ajouter l'id via la fonction
        query += " WHERE id = " + getIdValue(entity);

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'entité : " + e.getMessage(), e);
        }
    }

    private String getIdValue(SqlEntity entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);

            // casting en int au cas ou
            int idValue = (int) idField.get(entity);

            // int to string
            String idString = String.valueOf(idValue);

            // remettre private
            idField.setAccessible(false);

            return idString; // Retourner l'ID sous forme de String
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Impossible de récupérer l'ID de l'entité.", e);
        }
    }

    public <T> T getById(int id, Class<T> entityClass) {
        String query = "SELECT * FROM " + entityClass.getSimpleName().toLowerCase() + " WHERE id = " + id;
        T entity = null;

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Créer une instance de l'entité avec un constructeur sans paramètre
                // Il faut donc créer un constructeur sans paramètre pour toutes les classes
                entity = entityClass.getDeclaredConstructor().newInstance();

                // Récupérer tous les champs de l'entité
                Field[] fields = entityClass.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = field.getName().toLowerCase();

                    // Récupérer la valeur du RS en fonction du type du champ
                    Object fieldValue = null;
                    if (field.getType() == int.class) {
                        fieldValue = rs.getInt(columnName);
                    } else if (field.getType() == String.class) {
                        fieldValue = rs.getString(columnName);
                    } else if (field.getType() == float.class) {
                        fieldValue = rs.getFloat(columnName);
                    } else if (field.getType() == double.class) {
                        fieldValue = rs.getDouble(columnName);
                    } else if (field.getType() == boolean.class) {
                        fieldValue = rs.getBoolean(columnName);
                    }else if (field.getType() == LocalDate.class) {
                        java.sql.Date sqlDate = rs.getDate(columnName);
                        if (sqlDate != null) {
                            // vers LocalDate
                            fieldValue = sqlDate.toLocalDate();
                        }
                    }
                    // attribution de la valeur trouvée
                    if (fieldValue != null) {
                        field.set(entity, fieldValue);
                    }
                }
            } else {
                System.out.println("Aucune entité trouvée avec l'ID " + id);
            }

        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return entity;
    }

    public void deleteEntity(SqlEntity entity) {
        String query = "DELETE FROM " + tableName + " WHERE ";

        try {
            // On récupère l'id
            String idValue = getIdValue(entity);

            // on l'ajoute à la requête
            query += "id = " + idValue;

            try (Connection conn = ConnectDatabase.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertInTable(SqlEntity entity) {
        // on check si l'entité existe déjà avant de tenter de l'insérer
        String checkQuery = "SELECT 1 FROM " + tableName + " WHERE id = " + getIdValue(entity);

        try (Connection conn = ConnectDatabase.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             ResultSet rs = checkStmt.executeQuery()) {

            //Si elle existe déjà, on fait rien
            if (rs.next()) {
                System.out.println("L'entité existe déjà avec cet ID.");
                return;
            }

            // sinon on continu
            String query = "INSERT INTO " + tableName + " (";

            // Créer les valeurs à insérer
            createValues(entity);
            String values = getValues();
            String fieldsQuery = "";

            // On construit la première partie de la requête
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.getName().equals("id")) { // Ignorer le champ "id"
                    fieldsQuery += field.getName() + ", ";
                }
            }
            // Retirer la dernière virgule et ajouter la parenthèse de fin
            if (fieldsQuery.length() > 1) {
                fieldsQuery = fieldsQuery.substring(0, fieldsQuery.length() - 2);
            }

            query += fieldsQuery + ") " + values;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet insertRs = stmt.executeQuery();
                if (!(entity instanceof Fournisseur)) {
                    if (insertRs.next()) {
                        // Récupérer l'ID généré et assigner à l'entité
                        entity.setId(insertRs.getInt("id"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public abstract void setId(int id);

}
