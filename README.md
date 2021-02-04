# Projet - Application de gestion d’inscriptions à une conférence

## Prérequis
* **mysql**: compte: 'root', mot de passse: 'root'
* Une base de données intitulée 'conf_dbase'

**Remarque** : vous avez la possiblité de changer/préciser le mot de passe du compte root du serveur mysql et/ou le nom de la base de données dans le fichier _application.properties_

## Création de la base de données

```
mysql> CREATE DATABASE conf_dbase;
```

## Lancemement de l'application 
```
$ mvn spring-boot:run
```

## Déploiement de l'application


### A partir d'un .jar
```
$ mvn clean install
```
```
$ java -jar target/ConferenceApp-0.0.1-SNAPSHOT.jar
```

Application : http://localhost:8080


### Simple Docker (--net=host)
```
$ mvn clean install
```
```
$ sudo docker build --tag conference-app .
```
```
$ sudo docker run --net=host conference-app
```


### Docker + mySQL network

```
$ sudo docker network create conferenceapp-mysql
```

```
$ sudo docker network ls
NETWORK ID     NAME                  DRIVER    SCOPE
63eef7b2c977   bridge                bridge    local
d2d98d771473   conferenceapp-mysql   bridge    local
bc246a41f6be   host                  host      local
502768a86f7b   none                  null      local
```

```
$ sudo docker container run --name mysqldb --network conferenceapp-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=conf_dbase -d mysql:8
```
```
$ sudo docker build -t conferenceapp .
```
```
$ docker container run --network conferenceapp-mysql --name conferenceapp-jdbc-container -p 8080:8080 -d conferenceapp
```
```
sudo docker restart 17a7a1fc6ab4
```
