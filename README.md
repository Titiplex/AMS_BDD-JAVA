# **Projet d'ERP Retail (Enterprise Resource Planning)**

## **Description**

Cette application permet de gérer des produits (achats, ventes, stock) des fournisseurs et leurs contacts et de stocker ces informations dans une base de données.

Elle a été designée pour essayer de répondre aux besoins d'une supérette.

### **Langages utilisés**

Nous avons développé cette application en JAVA pour la logique, JAVAFX pour l'interface client et postgreSQL (JDBC) pour la base de données.

## **Fonctionnalités**

L'interface client est présentée sous forme de plusieurs onglets avec une fonction et utilité différente pour chaque onglet.

### **Onglet 1 : Résultats**

Cet onglet permettra d'afficher deux colonnes distinctes, pour le jour et le mois en cours, y sera afficher les dix meilleurs ventes, triables par quantité ou par bénéfice sur la période donnée.

### **Onglet 2 : Commandes**

Ici est affiché la liste des commandes pour le lendemain avec possibilité de valider, supprimer et reporter à la journée suivante.

Les commandes de la journée sont automatiquement passées à minuit chaque jour.

Concernant la commande, il est possible de modifier la quantité du produit (supérieure ou égale au montant minimal du contrat).

Vous pouvez également ajouter une commande pour un produit d’un fournisseur (avec une quantité supérieur ou égale au montant minimal du contrat), pour une date immédiate ou future.

### **Onglet 3 : Produits**

Dans cet onglet, vous trouverez la liste complète des produits avec le prix d’achat moyen (calculé en fonction des lots acquis en cours de validité et non encore vendus), le prix de vente (par défaut 134% du prix d’achat soit une marge de 34%, modifiable par l’utilisateur) et la quantité disponible.

C'est aussi ici que le client pourra ajouter un nouveau produit dans la base de donnée.

La description détaillée et la catégorie sont affichées dans un modal au clique sur le produit.

Il est aussi possible de modifier ces dernières.

### **Onglet 4 : Stock**

Il est affiché ici la liste des lots en stock (i.e. les commandes passées en stock et effectives) groupés par produit et triés par date avec les lots arrivant à échéance en tête. Il est précisé le nombre de produits restants par lot. Cela permet une gestion plus fine du stock.

### **Onglet 5 : Gestion**

Cet onglet permet de gérer les fournisseurs, leurs contrats et leurs contacts. Vous pouvez choisir un fournisseur ce qui affiche ses contacts associés, les produits proposés et les contrats passés avec ledit fournisseur. Il est possible d’ajouter ou de modifier un fournisseur (nom de la société, numéro SIRET, adresse, e-mail principale, produits proposés) ou d’un contact associé (nom, prénom, fonction, email, téléphone).

Il est également possible d'ajouter un contrat pour un produit avec une quantité minimale de commande, une date de début et de fin ainsi qu’un prix fixe d'achat effectif pour la durée du contrat.

Les produits associés au fournisseur sont récupérés automatiquement en fonction des contrats passés avec ledit fournisseur.

### **Onglet 6 : Ventes**

Ajout d’une vente et liste des ventes de la journée. L’ajout d’une vente se fait via l'entrée du produit et de la quantité (le lot d’origine est selectionné automatiquement en fonction du lot avec la date de péremption la plus proche).

Le prix sera celui du prix effectif en cours et la date celle du jour où la vente est entrée.

## **Spécifications**

Il a été décidé de ne pas associer de numéro de lot d’achat pour un produit dans la base de données. Les raisons sont : par simplicité pour créer des produits associables avec différents fournisseurs et lots, ce qui génère plus de simplicité et de cohérence lors de la création des lots/fournisseurs; ainsi que par soucis de redondance étant donné que les achats (i.e. les lots achetés) enregistrent les produits.

Il est nécessaire de posséder un contrat avec un fournisseur pour un produit afin de passer une commande. Dans le cas d’une offre spéciale et “urgente”, l’utilisateur pourra créer un contrat d’une durée très limitée (ex: une journée) afin de passer une commande spécifique.

Une commande génère directement un lot acheté disponible en stock, c'est-à-dire qu'il est livré directement.

Un produit ne peut être défini comme n’étant vendable que par unité ou masse ou volume.

Un fournisseur ne peut pas être désactivé par souci de contradiction avec les consignes données par nos professeurs, puisque cela nécessiterait une modification de la structure de la base de données.

Un lot pour la journée arrivant est commandé automatiquement à minuit. La raison est : une gestion plus simple du temps en java et pour éviter de contredire les consignes données par nos professeurs puisque cela nécessiterait une modification de la base de données.

## **Installation**

Télécharger le dépot github,

Créer la base de donnée avec le fichier .sql,

Vérifier les dépendances du projet (javafx, jdbc/postgresql),

Ajouter dans le dossier parent un fichier .env avec les trois variables suivantes :
```
USER="uapvXXXXXXX"
URL="jdbc:postgresql://pedago.univ-avignon.fr:5432/etd"
PASSWORD="Votre Mot de Passe"
```

## **Utilisation**

Pour lancer l'application :

&nbsp;&nbsp;&nbsp;Après compilation, il faut lancer le fichier jar Main appartenant au package main.


