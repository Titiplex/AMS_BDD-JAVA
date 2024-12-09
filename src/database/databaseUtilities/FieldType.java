package database.databaseUtilities;

public enum FieldType {
    NUMERIC, VARCHAR, FLOAT8, INT4, DATE;

    public static FieldType getFieldType(String type) {
         type = type.toLowerCase();
        return switch (type) {
            case "int", "integer", "int4" -> FieldType.INT4;
            case "float", "float8" -> FieldType.FLOAT8;
            case "decimal", "numeric" -> FieldType.NUMERIC;
            case "varchar", "char", "text" -> FieldType.VARCHAR;
            case "date" -> FieldType.DATE;
            //Ajouter une exception
            default -> null;
        };
    }
}
