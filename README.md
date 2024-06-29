# Projet de GLA
Version 2024

## Description
Ceci est l'archetype de projet de Génie Logiciel Avancé (GLA).

Il s'agit d'un projet Java. Ce dépôt définit un système de build et une application simple. Il est nécéssaire de consulter le fichier [CONTRIBUTING.md](CONTRIBUTING.md) pour utiliser ce dépôt.

## Lancement du programme
Ce projet utilise [maven](https://maven.apache.org/) de Apache pour la gestion de construction.

Afin de compiler et lancer les tests, éxecutez simplement
```
mvn verify
```

Dans sa version initiale, le programme fournit est un simple code qui se lance en terminal ou en application graphique.

Une fois le programme compilé, vous trouverez un jar executable dans le dossier target. Au nom de jar près (version changeante), vous pourrez l'exécuter avec:
```
java -jar project-2024.1.0.0-SNAPSHOT-jar-with-dependencies
```
L'option de lancement `--info` causera l'affichage dans la console d'informations de l'application.

L'option de lancement `--term` causera le lancement de lapplication dans le terminal.

L'option de lancement `--gui` causera le lancement de l'interface graphique.

L'option de lancement `--map` causera l'affichage du réseau de métro parisien, il sera posssible de cliquer sur les arrets pour obtenir les infos des stations.

