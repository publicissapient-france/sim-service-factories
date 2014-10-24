Sim-service-factories
=====================

Sim-service est un jeu permettant de mettre en place des micro-services communiquant à travers un bus de messages.

Dans ce jeu vous avez le choix des armes: [Java](https://github.com/xebia-france/sim-service-factories/tree/java), [Javascript](https://github.com/xebia-france/sim-service-factories/tree/javascript) ou [Groovy](https://github.com/xebia-france/sim-service-factories/tree/java)

Le tout s'articule autour du framework [vertx](http://vertx.io/) qui va nous permettre de faire communiquer nos services.


L'histoire
----------

Sim-service ce déroule à Xebia-city. L'occupation principale de la population => **Boire de la bière**

Pour se faire, il y a 4 types de batiments (nos types de services) dans Xebia-city:

* Les **stores**, où les gens commandent leurs bières
* Les **factories**, où les stores se fournissent en bières.
* Pour faire de la bière, il faut du houblon que les factories commandent aux **farms**
* Une **bank** est également présente pour superviser les productions et empêcher les fraudes

Votre est but sera de créer la **factory** la plus efficace possible. C'est à dire celle qui fournira le plus de bières aux stores tout en ayant un stock minimal.


Règles
------

Voici le fonctionnement de chaque batiment de Xebia-city.

Il existe plusieurs types de farms et de stores, chacun avec des propriétés différentes.

**Une farm:**

* Produit des quantités de houblons à interval régulier
* Le stock maximal de houblons est limité par factory
* Chaque unité de houblon a un prix
* Lorsqu'une farm reçoie une demande de houblon, elle propose une offre en fonction de son stock disponible. Cette offre expire au bout d'un délai prédéfinit
* Lorsqu'une factory accepte une offre d'une farm, celle ci envoi la facture à la bank


**Une factory:**

* Reçoie les demandes des stores en bières
* Demande la quantité de houblons correspondant aux factories
* Choisie une ou des offres renvoyées par les factories et lui notifie l'achat
* Envoie une offre au store (qu'il peut ne pas accepter)
* Si le stock d'une factory est trop important, la bank la pénalise
* Si une factory répond à des offres sans avoir le stock nécessaire, la bank la pénalise


**Un store:**

* Demande à toutes les factories une quantité de bière en échange d'un prix
* Une fois que les factories ont répondu, choisit une offre et informe la factory choisie ainsi que la bank


**La bank:**

* Maintient l'état des stocks et des comptes des factories. Elle est notifié à chaque achat / vente réalisé par une factory
* Lorsqu'une vente de houblons est conclut entre une factory et une farm, la bank notifie la factory. Sans cette notification, la transaction est concidéré comme annulée
* Lorsqu'une vente de bière est réalisée entre une factory et un store, la bank notifie la factory. Sans cette notification, la transaction est concidérée comme annulée
* Si une factory triche, la bank la sanctionne

**Règles communes:**

* Lors qu'un batiment se connecte, il doit se faire connaitre
* Un batiment doit signaler sa présence toutes les 500 ms


Communication
-------------
