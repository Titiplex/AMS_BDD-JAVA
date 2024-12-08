package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tabs.*;

// Mon utilisation de javafx est issue de tutoriels présents sur internet

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Application AMS");
        primaryStage.setOnCloseRequest(e -> Platform.exit());

        // menu
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Application de l'entreprise de détail");
        MenuItem menuItemExit = new MenuItem("Quitter");
        menuItemExit.setOnAction(e -> Platform.exit());
        menuFile.getItems().add(menuItemExit);
        menuBar.getMenus().add(menuFile);

        // onglets
        TabPane tabPane = new TabPane();

        Tab resultats = new Tab("Resultats", new TabResultats().createTab());
        Tab commandes = new Tab("Commandes", new TabCommandes().createTab());
        Tab produits = new Tab("Produits", new TabProduits().createTab());
        Tab stock = new Tab("Stock", new TabStock().createTab());
        Tab gestion = new Tab("Gestion", new TabGestion().createTab());
        Tab ventes = new Tab("Ventes", new TabVentes().createTab());


        tabPane.getTabs().addAll(resultats, commandes, produits, stock, gestion, ventes);

        // on ajoute à la scène finale
        VBox vbox = new VBox(menuBar, tabPane);
        Scene scene = new Scene(vbox, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}