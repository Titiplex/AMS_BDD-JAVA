/**
 * This project generates a management app for a small store
 *
 * @author Titiplex
 * @author Qwantike
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