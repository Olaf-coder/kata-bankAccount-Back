# BankAcount-Backend

Ce projet de gestion de transaction est un exercice mandaté par un client afin de juger de mes compétence en developpement full-stack Java 17 & angular 19

Ce repository git contient le code back end, pour pouvoir lancer le projet, vous aurez besoin du front end (prochainement) ou bien du swagger mis en place (voir "Information Supplémentaire").

Ce projet doit être utilisé avec le projet front [BankAccount-Front](https://github.com/Olaf-coder/kata-phonebook-front)

## Sujet

Think of your personal bank account experience
When in doubt, go for the simplest solution

### Requirements
- Deposit and Withdrawal
- Account statement (date, amount, balance)
- Statement printing

### User Stories
#### US 1:
**In order** to save money\
**As a** bank client\
**I want to** make a deposit in my account

#### US 2:
**In order to** retrieve some or all of my savings\
**As a** bank client\
**I want to** make a withdrawal from my account

## Choix des technologies

La mission est fullstack, et dans un soucis de scallabilité, j'ai décidé de creer une API afin de fournir rapidement un BackEnd en premier lieu
Concernant les technologies j'étais libre, donc j'ai optés pour des technologies récentes

- Spring Boot 3.5.0
- Maven
- Java 21

C'est pour toutes ces raisons pour lesquelles j'ai les dépendances suivantes:

- Spring Web
- API JDBC
- Spring Data JPA
- H2 Database (pour sa facilité de configuration, propose un stockage local dans un fichier, et se gére comme une base de données classique (requêtes SQL, facilité d'accés via une API)

## Information supplémentaire
Le back-end qui contient l'API doit se lancer idéalement en premier. Dans tous les cas, il est nécessaire pour faire fonctionner le projet.
Sur intelliJ, il faut creer puis lancer une configurations de type Spring avec kata-bankAccount-back comme classPath

Pour la configuration, quand le projet se lance il est actuellement sur le port 8080. Pour changer le port d'écoute, il faut remplacer la ligne suivante dans src/main/resources/application.properties:

    server.port=8080

De la documentation API existe sur ce projet sous la forme d'une page Swagger. Une fois le backend lancé, vous pouvez acceder a Swagger sur le lien suivant:

    http://localhost:8080/swagger-ui/index.html#/

Lorsque des données sont enregistrées, vous les trouverez à la racine du dossier dans bankAccount.mv.db.
A savoir qu'il existe des solutions pour transformer ce fichier en Json.

## TODO
Il reste a faire un TU sur TransactionEntity.
J'ai décidé d'utiliser @PrePersist pour remplir automatiquement le champs date durant la création d'une transaction avec la date du jour.
Donc il faut voir comment faire ce test (piste possible: Persister l'entité dans le test ?)
