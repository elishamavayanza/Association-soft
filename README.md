#📦 Association-Soft

Application Java Spring Boot permettant la gestion complète d’associations, communautés ou groupes divers, avec un système flexible de gestion des utilisateurs, rôles, associations et activités.

## 🔧 Technologies utilisées
- Java 17+
- Spring Boot 3+
-Spring Data JPA
- Base de données SQLite (persistante)
- Lombok
- Hibernate

## 🗃️ Structure

 ### 1. Base de données (modèle)
- **User** : utilisateur avec email, mot de passe, rôles, etc.
- **Role** :rôlesattribués aux utilisateurs (ex. ADMIN, MEMBER)
- **Association** : entités associatives ou communautés
- **Activity** : événements ou activités créés et suivis par les utilisateurs

### 2. Project
```
src/
├── main/                                │   ├──java/com/yourassociation/              
│   │   ├── AssociationApplication.java        # Classe principale qui démarre l'application Spring Boot (@SpringBootApplication)
│   │   ├── config/                            # Configuration générale (CORS, Swagger, Datasource, Beans, etc.)
│  │   ├── controllers/                       # Contrôleurs REST (endpoints de l’API)
│   │   ├── dto/                               # Objets de transfert de données (Data Transfer Objects)
│   │   ├── exceptions/                        # Gestion centralisée des erreurs et exceptions personnalisées
│   │├── models/#Entités JPA représentant les tables de la base de données
│   │   ├── repositories/                      # Interfaces JPA pour l'accès aux données (DAO)
│   │   ├── services/                          # Logique métier (services appelés par les contrôleurs)
│   │├── security/# Configuration dela sécurité (JWT, Auth, filtres, services utilisateur)
│   │   └── utils/                             # Classes utilitaires (helpers, formatters, générateurs, constantes)
│
│   └── resources/                             # Ressources de l'application (noncompilées)
│       ├── application.properties             # Fichier de configuration par défaut
│       ├── application-dev.properties         # Configuration spécifique à l'environnement de développement
│       ├── application-prod.properties        # Configuration pour l’environnement de production
│       ├── static/                            #Fichiers statiques (CSS, JS, images pour une app web)
│       └── templates/                         # Templates HTML (si utilisation de Thymeleaf, FreeMarker...)
│
└── test/                                      # Tests unitaires et d'intégration

````

## 🔄Système Likelemba (rotation financière)

### Description
Module complet de gestion du système rotatif Likelemba, permettant la création de groupes de tontine, la gestion des tours, des contributions, et des pénalités. L'objectif est d'automatiserla rotation financière entremembresselon l'ordre défini et d'assurer le suivi transparent des montants versés et reçus.

### Structure
- **RotatingGroup** : Entité représentant un groupe de rotation financière
- **Round** : Entité représentant un tour de rotationau sein d'un groupe-**Contribution** : Entité représentant une contribution d'un membre à un tour
- **Penalty** : Entité représentant une pénalité appliquée à un membre

### Fonctionnalités
- Création et gestion des groupes rotatifs(Likelemba)
- Gestion des tours de rotation et des contributions des membres
- Calcul des montants reçus, restants et des pénalités en cas de retard
- API REST complète pour l'intégration avec l'interface utilisateur

### Endpoints API
- `/api/rotating/groups`- Gestion des groupes de rotation
- `/api/rotating/rounds` - Gestion des tours
- `/api/rotating/contributions` - Gestion des contributions
- `/api/rotating/penalties` - Gestion des pénalités

Tous les endpoints supportent lesopérations CRUD (Create, Read, Update, Delete) ainsi que des opérations spécifiques comme le calcul des totaux et des montants restants.

##🛠️ Configuration H2
Le projet utilise une base de données H2 persistante accessible via la console intégrée :
````
spring.datasource.url=jdbc:h2:~/associationsoft
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=truespring.h2.console.path=/h2-console
````

## 📊 Domaines d'application

### 1.Associations et organisations non lucratives
- **Gestion des membres** : Suivi des adhésions, renouvellements et historique des rôles
- **Gestion financière** : Suivi des cotisations, des transactions et des catégories de dépenses/revenus
- **Organisation d'événements** : Planification et suivi des activités organisées par l'association
- **Gestion de projets** : Coordination des projets associatifs avec affectation des membres
- **Système de tontine Likelemba** : Gestion des groupes de rotation financière entre membres

### 2. Clubs et organisations communautaires
- **Clubs sportifs** : Gestion des membres, des cotisations et des événements sportifs
- **Groupes de hobby** : Organisation d'activités et gestion des participants
- **Associations de quartier** : Coordination des initiatives communautaires
- **Groupes de tontine communautaires** : Mise en place de systèmes de rotation financière

### 3. Organisations professionnelles
- **Ordres professionnels** : Gestion des membres, des cotisations obligatoires et des formations
- **Chambres de commerce**: Suivi des membres et organisation d'événements professionnels
- **Groupes d'investissement** : Mise en place de systèmes de rotation d'investissement

##⚙️ Fonctionnalités clés pour une utilisation réelle
### Gestion complètedes membres
- Base de données centralisée des membres avec historique
- Suivi des rôles et permissions (admin, modérateur, membre, invité)
-Gestion des cotisationset vérification du statut à jour

### Système financier intégré
- Suivi des paiements et des cotisations
- Gestion des prêts entre membres avec calcul des intérêts
- Système de rotation financière Likelemba (tontine)
- Catégorisation des transactions financières
- Rapports financiers automatiques

###Communication et notification
- Système de notifications par email, SMS et push
- Annonces et mises à jour aux membres
- Confirmation automatique pour les événements

### Organisation d'activités
- Planification d'événements
- Gestion des inscriptions
- Suivides participants
---

## 💼 Contextes d'utilisation concrets
### Pour une association locale
1. Inscription de nouveaux membres via l'interface d'authentification
2. Paiement des cotisations annuelles avec suivi automatique
3. Organisation d'événements (assemblées générales, activités sociales)
4. Gestion des finances avec rapport mensuel des entrées/sorties
5. Communication avec les membres via notifications

### Pour une organisation professionnelle
1. Vérification du statut des membres (à jour dans leurs cotisations)
2. Gestion desprêtsentre membres ou à l'organisation
3. Suivi des projets professionnels ou communautaires
4. Planification de formations et événements réseautage
___

## 🌟 Avantages dans un contexte réel- **Centralisation des données** : Toutes lesinformations dansun seul système
- **Automatisation** : Moins de tâches manuelles pour l'administration
- **Transparence** : Accès contrôlé aux informations financières et de gestion
- **Communication efficace** :Notifications multi-canaux aux membres
- **Historique complet** :Suivi de toutes les activités et modifications

Cette application serait particulièrement utile pour les associations qui ont besoin d'un système de gestion intégré mais n'ont pas les ressources pour développer une solution personnalisée. Elle peut être déployée sur un serveur local ou cloud selon les besoinsde l'organisation.
