Sim-service-factories
=====================

Sim-service est un jeu permettant de mettre en place des micro-services communiquant à travers un bus d'évènements.

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


Guide de développement
----------------------

Afin de développer la factory, il suffit de suivre ce guide développement. Chaque étape est agrémentée de la documentation vertx correspondate.

#### Vertx

Vertx est un framework polyglotte qui permet de faire communiquer facilement des services à travers [un bus d'évènements](http://vertx.io/core_manual_java.html#the-event-bus) (entre autres). Dans vertx, chaque service est appelé [Verticle](http://vertx.io/manual.html#verticle).
Les langages exécutable dans vertx sont le Java, le javascript, le ruby, le groovy, et le python. Nous vous proposons un squelette de Verticle pour bien démarrer dans trois de ces langages, [Java](https://github.com/xebia-france/sim-service-factories/tree/java), [Javascript](https://github.com/xebia-france/sim-service-factories/tree/javascript) ou [Groovy](https://github.com/xebia-france/sim-service-factories/tree/java).

Pour démarrer, clonez la branche de votre choix et suivez le guide. Consultez les règles du jeu ou le diagramme de communication pour savoir quel service parle à quel autre service.

L'ensemble des évènements publiés dans le bus seront des évènements JSON.

#### Dites 'Hello'

Chaque batiment doit se faire connaitre à l'ensemble de la ville lorsqu'il est créé. Par la suite, toute les 500ms, il doit indiquer qu'il est toujours en vie.

Pour se faire, la première chose à faire dans votre Verticle est de [publier un évènement](http://vertx.io/core_manual_java.html#publishing-messages) **hello** dans le format suivant:

```
{
  "action": "hello",
  "team": "choose your team name and stick to it",
  "from": "unique instance id",
  "type": "factory",
  "version": "version"
}
```

Ceci sur l'adresse: /city

En Java, vertx propose un [objet JSON](http://vertx.io/core_manual_java.html#json) pour faciliter la manipulation de ce format.

Choisissez un nom de team. Le squelette choisie contient déjà un identifiant prédéfini. Enfin indiquez la version de votre factory (1.0 au départ).

Maintenant, il vous faut indiquer toutes les 500 ms que votre factory est en vie. Heureusement, vertx propose un [ensemble d'outils](http://vertx.io/core_manual_java.html#periodic-timers) permettant de faire cela facilement.



#### Recevez les demandes

Maintenant que votre factory existe et le dit, il vous faut prendre en compte les demandes des stores.
Les stores communiquent de la façon suivante:

```
{
   "action": "request",
   "from": "store id",
   "quantity": 10,
   "cost" : 1000
}
```

Il vous indique ainsi la quantité de bière voulue ainsi que le prix qu'il est pret à payer pour ça.
Le store publie le message sur /city/factory, ainsi, **toutes** les factories de Xebia-city vont recevoir le message. A vous d'être le plus rapide pour répondre le premier.

Pour recevoir les messages, il vous faut [écouter](http://vertx.io/core_manual_java.html#registering-and-unregistering-handlers) sur l'adresse /city/factory

Si vous avez le stock suffisant, vous pouvez répondre immédiatement, sinon, il va falloir aller chercher du houblon.


#### Achetez du houblon

Pour faire de la bière, il faut du houblon, et c'est le rôle des farms.

Pour l'acquérir, vous pouvez publier un nouveau message sur l'adresse /city/farm dans ce format:

```
{
   "action": "request",
   "from": "factory id",
   "quantity": 10
}
```

Chaque factory vous retournera alors une offre comme celle ci sur votre adresse privée /city/factory/votre-id:

```
{
    "action": "response",
    "from": "farm id",
    "quantity": 3,
    "cost": 30
}
```

Ce message est avec [timeout](http://vertx.io/core_manual_java.html#specifying-timeouts-for-replies) vous devez donc [répondre](http://vertx.io/core_manual_java.html#replying-to-messages) assez rapidement que vous êtes intéressé:

```{
    "action": "acquittement",
    "from": "factory id",
    "quantity": 9
}```

