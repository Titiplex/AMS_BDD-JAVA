/**This project generates a management app for a small store
 * @author Johanny Titouan
 * @author Moinereau Paul
 */
module AMS.BDD.JAVA {

    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    requires org.postgresql.jdbc;
    requires javafx.base;
    exports main;
}