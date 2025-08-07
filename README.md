# 📦 Association-Soft

Application Java Spring Boot permettant la gestion complète d’associations, communautés ou groupes divers, avec un système flexible de gestion des utilisateurs, rôles, associations et activités.

## 🔧 Technologies utilisées
- Java 17+
- Spring Boot 3+
- Spring Data JPA
- Base de données H2 (persistante)
- Lombok
- Hibernate

## 🗃️ Structure

 ### 1. Base de données (modèle)
- **User** : utilisateur avec email, mot de passe, rôles, etc.
- **Role** : rôles attribués aux utilisateurs (ex. ADMIN, MEMBER)
- **Association** : entités associatives ou communautés
- **Activity** : événements ou activités créés et suivis par les utilisateurs

### 2. Project
```
src/
├── main/                                      
│   ├── java/com/yourassociation/              
│   │   ├── AssociationApplication.java        # Classe principale qui démarre l'application Spring Boot (@SpringBootApplication)
│   │   ├── config/                            # Configuration générale (CORS, Swagger, Datasource, Beans, etc.)
│   │   ├── controllers/                       # Contrôleurs REST (endpoints de l’API)
│   │   ├── dto/                               # Objets de transfert de données (Data Transfer Objects)
│   │   ├── exceptions/                        # Gestion centralisée des erreurs et exceptions personnalisées
│   │   ├── models/                            # Entités JPA représentant les tables de la base de données
│   │   ├── repositories/                      # Interfaces JPA pour l'accès aux données (DAO)
│   │   ├── services/                          # Logique métier (services appelés par les contrôleurs)
│   │   ├── security/                          # Configuration de la sécurité (JWT, Auth, filtres, services utilisateur)
│   │   └── utils/                             # Classes utilitaires (helpers, formatters, générateurs, constantes)
│
│   └── resources/                             # Ressources de l'application (non compilées)
│       ├── application.properties             # Fichier de configuration par défaut
│       ├── application-dev.properties         # Configuration spécifique à l'environnement de développement
│       ├── application-prod.properties        # Configuration pour l’environnement de production
│       ├── static/                            # Fichiers statiques (CSS, JS, images pour une app web)
│       └── templates/                         # Templates HTML (si utilisation de Thymeleaf, FreeMarker...)
│
└── test/                                      # Tests unitaires et d'intégration

````

## 🛠️ Configuration H2
Le projet utilise une base de données H2 persistante accessible via la console intégrée :
````
spring.datasource.url=jdbc:h2:~/associationsoft
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
````