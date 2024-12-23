package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tabs.*;

import java.util.HashMap;
import java.util.Map;

// Notre utilisation de javafx est issue de tutoriels présents sur internet

public class Main extends Application {

    private static final Main INSTANCE = new Main();
    private static TabPane tabPane;
    private static Map<String, Tab> tabs;

    public Main() {
        super();
        tabPane = new TabPane();
        tabs = new HashMap<>();
    }

    public static Main getInstance() {
        return INSTANCE;
    }

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
        MenuItem menuItemRefresh = new MenuItem("Rafraîchir les données");
        menuItemRefresh.setOnAction(e -> setTabPane()); // TabPane est une variable globale ou passée au contexte
        menuFile.getItems().add(menuItemRefresh);
        menuBar.getMenus().add(menuFile);

        setTabPane();

        // on ajoute à la scène finale
        VBox vbox = new VBox(menuBar, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        Scene scene = new Scene(vbox, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setTabPane() {
        // on récupère le Tab qui sur lequel on était avant reload
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        String selectedTabName = (selectedTab != null) ? selectedTab.getText() : null;

        // on supprime tout ce qui a déjà été fait
        tabPane.getTabs().clear();
        tabs.clear();

        Tab resultats = new Tab("Resultats", new TabResultats().createTab());
        Tab commandes = new Tab("Commandes", new TabCommandes().createTab());
        Tab produits = new Tab("Produits", new TabProduits().createTab());
        Tab stock = new Tab("Stock", new TabStock().createTab());
        Tab gestion = new Tab("Gestion", new TabGestion().createTab());
        Tab ventes = new Tab("Ventes", new TabVentes().createTab());

        addTab("Resultats", resultats);
        addTab("Commandes", commandes);
        addTab("Produits", produits);
        addTab("Stock", stock);
        addTab("Gestion", gestion);
        addTab("Ventes", ventes);

        // on replace sur le bon tab
        if (selectedTabName != null && tabs.containsKey(selectedTabName)) {
            tabPane.getSelectionModel().select(tabs.get(selectedTabName));
        }
    }

    private void addTab(String name, Tab tab) {
        tabPane.getTabs().add(tab);
        tabs.put(name, tab);
    }

    /**
     * Recreates a specified tab in the associated tab pane. The tab is replaced with a new instance based on its name.
     * This method updates both the tab pane and the internal map containing the tabs.
     * (comment generated automatically)
     *
     * @param tabName the name of the tab to recreate. Supported names are:
     *                "Resultats", "Commandes", "Produits", "Stock", "Gestion", and "Ventes".
     *                If the specified name is not found in the internal map, no action is taken.
     */
    public void recreateTab(String tabName) {
        if (!tabs.containsKey(tabName)) return; // on verif si le tab est supporté

        // on récupère le tab sur lequel on était
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        String selectedTabName = (selectedTab != null) ? selectedTab.getText() : null;

        int index = tabPane.getTabs().indexOf(tabs.get(tabName)); // index du tab
        Tab newTab = switch (tabName) {
            case "Resultats" -> new Tab("Resultats", new TabResultats().createTab());
            case "Commandes" -> new Tab("Commandes", new TabCommandes().createTab());
            case "Produits" -> new Tab("Produits", new TabProduits().createTab());
            case "Stock" -> new Tab("Stock", new TabStock().createTab());
            case "Gestion" -> new Tab("Gestion", new TabGestion().createTab());
            case "Ventes" -> new Tab("Ventes", new TabVentes().createTab());
            default -> null;
        };

        if (newTab != null) {
            // remplace et maj
            tabPane.getTabs().set(index, newTab);
            tabs.put(tabName, newTab);

            // on replace le curseur sur le bon tab
            if (selectedTabName != null && selectedTabName.equals(tabName)) {
                tabPane.getSelectionModel().select(newTab);
            }
        }
    }
}