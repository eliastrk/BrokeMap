# BrokeMap

[English version](./README.md)

BrokeMap est une application Android conçue pour aider l’utilisateur à trouver des lieux et activités à proximité via une carte, avec un accent particulier sur des filtres orientés budget.

L’application récupère des données de lieux depuis une API métier, puis affiche les résultats sur Google Maps avec des marqueurs et des filtres adaptés à chaque type d’endroit.

## Portée Du Projet

Ce dépôt contient le client Android.

L’application mobile est reliée à :
- une API personnalisée développée avec FastAPI
- une base de données PostgreSQL 15
- un déploiement backend conteneurisé avec Docker
- le SDK Google Maps Android pour l’affichage cartographique

## Fonctionnalités Principales

- affichage de lieux proches sur une carte Google Maps interactive
- affichage de marqueurs pour plusieurs catégories de lieux
- filtrage par type de lieu
- filtrage par critères liés au budget
- affichage de détails enrichis récupérés depuis l’API
- utilisation de la localisation de l’utilisateur
- préchargement et mise en cache de certains détails pour fluidifier l’expérience

D’après le code, les principales catégories gérées incluent :
- bars
- restaurants
- fast-foods
- musées
- bancs / lieux publics peu coûteux

## Stack Technique

- Kotlin
- Jetpack Compose
- Google Maps Compose
- Retrofit + Gson
- Android ViewModels et state flows
- DataStore
- backend FastAPI
- PostgreSQL 15
- Docker

## Structure Du Dépôt

- `app/src/main/java/fr/nebulo9/brokemap/` : code source principal de l’application Android
- `app/src/main/java/fr/nebulo9/brokemap/api/` : client API et services Retrofit
- `app/src/main/java/fr/nebulo9/brokemap/models/` : ViewModels et gestion d’état
- `app/src/main/java/fr/nebulo9/brokemap/services/` : services Android, notamment la localisation
- `app/src/main/java/fr/nebulo9/brokemap/ui/` : interface Compose, écrans, filtres et thème
- `app/src/main/res/` : icônes, styles, ressources cartographiques et ressources Android
- `secrets.example.properties` : exemple de configuration pour les clés locales

## Fonctionnement Général

Le fonctionnement global est le suivant :

1. L’application initialise son client Retrofit et sa configuration cartographique.
2. Elle demande les permissions de localisation à l’utilisateur.
3. Elle charge les lieux depuis l’API backend.
4. Elle précharge certaines données détaillées selon les catégories.
5. Elle affiche les lieux sous forme de marqueurs sur la carte.
6. Elle applique des filtres côté client selon les critères sélectionnés.
7. Elle affiche une fiche détaillée lorsqu’un lieu est sélectionné.

Le projet illustre donc bien :
- la consommation d’API sur Android
- la conception d’une interface orientée carte
- le filtrage de données géolocalisées
- l’intégration entre un frontend mobile et un backend personnalisé

### Prérequis

- Android Studio
- un SDK Android récent
- une clé Google Maps Platform
- un accès à l’API backend utilisée par l’application

### Configuration Des Secrets

Le projet utilise un fichier `secrets.properties` pour les secrets locaux.

Utilise le modèle fourni :

```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```

## Remarques

- Ce dépôt se concentre sur la couche application Android.
- L’API backend, la base de données et le déploiement Docker font partie de l’architecture globale, mais ne sont pas inclus dans ce dépôt.

## Ce Que Le Projet Montre

- développement d’une application Android avec Jetpack Compose
- intégration de Google Maps dans une interface mobile personnalisée
- consommation d’une API REST avec Retrofit
- gestion de la localisation utilisateur et de la découverte géolocalisée
- mise en place de filtres métiers liés au type de lieu et au budget
- structuration d’un projet autour de l’UI, des services, de l’accès API et de la gestion d’état
