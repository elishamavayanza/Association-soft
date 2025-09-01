# Association Management API

v1.0.0  
OAS 3.1

/v3/api-docs

API de gestion d'associations est l'application backend principale d'une plateforme dédiée à la gestion des associations, de leurs membres, projets et transactions financières.

## Description

Cette API permet de gérer de manière complète les associations et leurs éléments associés :

- Gestion des membres et de leur historique de rôles
- Gestion des projets et des membres de projets
- Gestion des activités associatives
- Gestion des documents
- Gestion des catégories financières et des transactions
- Gestion des prêts
- Gestion des cotisations

## Technologies

| Catégorie           | Technologie         |
|---------------------|---------------------|
| **Backend**         | Java + Spring Boot  |
| **Base de données** | SQLite              |
| **Documentation API** | Swagger/OpenAPI 3.1 |
| **Sécurité**        | Spring Security     |

## Fonctionnalités clés

- Gestion complète des associations et de leurs membres
- Suivi des projets et des responsables de projets
- Gestion des activités et des documents associatifs
- Module de gestion financière (catégories, transactions, cotisations)
- Gestion des prêts entre membres
- Suivi de l'historique des rôles des membres
- API REST documentée avec Swagger
- Sécurité basée sur JWT (si activée)

## Configuration

L'application utilise une base de données SQLite configurée dans le fichier `application.yml`. La documentation Swagger est accessible via l'endpoint `/v3/api-docs`.

## Accès à la documentation API

- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8080/v3/api-docs

## Prérequis

- Java 21
- Maven 3.6+