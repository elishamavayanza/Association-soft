# Système de Notification Amélioré pour Likelemba

Ce document décrit le système de notification amélioré qui a été mis en œuvre pour Likelemba afin de fournir une meilleure engagement utilisateur et une sensibilisation aux événements importants.

## Fonctionnalités Ajoutées

1. **Rappels de Tour à Venir**
    - Notifications automatiques envoyées 3 jours avant le début d'un tour
    - Inclut le numéro du tour, le nom du groupe, la date de début et le montant de la contribution

2. **Notifications d'Échéance de Paiement**
    - Notifications automatiques envoyées 2 jours avant l'échéance d'une contribution
    - Inclut le montant, le numéro du tour, le nom du groupe et la date d'échéance

3. **Annonces de Distribution**
    - Notifications automatiques envoyées lorsque les fonds sont distribués
    - Inclut le numéro du tour, le nom du groupe et le montant total distribué

4. **Avertissements de Pénalité**
    - Notifications automatiques envoyées lorsqu'une pénalité est encourue
    - Inclut le montant de la pénalité, le numéro du tour, le nom du groupe et la raison

## Détails de Mise en Œuvre

### Types de Notification

Le système prend en charge 4 types de notification spécialisés :

1. `UPCOMING_ROUND_REMINDER` - Rappels pour les tours à venir
2. `PAYMENT_DUE_NOTIFICATION` - Notifications d'échéance de paiement
3. `DISTRIBUTION_ANNOUNCEMENT` - Annonces de distribution de fonds
4. `PENALTY_WARNING` - Avertissements de pénalité

### Planification

Les notifications sont automatiquement envoyées selon le calendrier suivant :
- Rappels de tour à venir : Quotidien à 9h00
- Notifications d'échéance de paiement : Quotidien à 10h00
- Annonces de distribution : Quotidien à 11h00
- Avertissements de pénalité : Quotidien à 12h00

### Points de Terminaison API

Le système de notification expose les points de terminaison API suivants :

1. `POST /api/notifications` - Envoyer une notification basique à un utilisateur
2. `POST /api/notifications/broadcast` - Diffuser une notification à tous les utilisateurs
3. `POST /api/notifications/role/{roleName}` - Envoyer une notification aux utilisateurs ayant un rôle spécifique
4. `POST /api/notifications/enhanced` - Envoyer une notification améliorée avec un type spécifique

## Configuration

Pour activer la planification des notifications, assurez-vous que l'annotation `@EnableScheduling` est présente sur la classe principale de l'application.

## Extension du Système

Pour ajouter de nouveaux types de notification :
1. Ajoutez de nouvelles méthodes à l'interface [NotificationService](src/main/java/com/org/testApi/services/NotificationService.java)
2. Implémentez les méthodes dans [NotificationServiceImpl](src/main/java/com/org/testApi/services/NotificationServiceImpl.java)
3. Ajoutez une logique de planification dans [NotificationSchedulerService](src/main/java/com/org/testApi/services/NotificationSchedulerService.java) si nécessaire
4. Mettez à jour les repositories avec les nouvelles méthodes de requête requises

## Tests

Le système peut être testé en :
1. Créant des données de test (tours, contributions, pénalités)
2. Déclenchant manuellement les méthodes planifiées dans NotificationSchedulerService
3. Vérifiant que les notifications sont envoyées aux utilisateurs appropriés