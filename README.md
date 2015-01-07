Sim-service-factories
=====================

Sim-service est un jeu permettant de mettre en place des micro-services communiquant à travers un bus d'évènements.

Dans ce jeu vous avez le choix des armes: [Java](https://github.com/xebia-france/sim-service-factories/tree/java), [Javascript](https://github.com/xebia-france/sim-service-factories/tree/javascript) ou [Groovy](https://github.com/xebia-france/sim-service-factories/tree/groovy)

Le tout s'articule autour du framework [vertx](http://vertx.io/) qui va nous permettre de faire communiquer nos services.


L'histoire
----------

Sim-service se déroule à Xebia-city. L'occupation principale de la population => **Boire de la bière**

Pour ce faire, il y a 4 types de batiments (nos types de services) dans Xebia-city:

* Les **stores**, où les gens commandent leurs bières
* Les **factories**, où les stores se fournissent en bières.
* Pour faire de la bière, il faut du houblon que les factories commandent aux **farms**
* Une **bank** est également présente pour superviser les productions et empêcher les fraudes

Votre but sera de créer la **factory** la plus efficace possible. C'est à dire celle qui fournira le plus de bières aux stores tout en ayant un stock minimal.

Communication
-------------

Voici le diagramme de communication de l'ensemble des services.

![Diagramme](https://github.com/xebia-france/sim-service-factories/blob/master/com.png)

1 Le store demande des bières aux factories

2 La factory demande du houblon aux farms

3 Les farms proposent du houblon à la factory

4 La factory accepte une ou des offres de houblon. La/les farms previennent la bank des transactions

5 La bank prévient la factory que la transaction est acceptée

6 La factory fournit une offre de bières au store

7 Le store accepte l'offre et le signale à la factory et à la bank

8 La bank prévient la factory que la transaction est finalisée

Pour plus de détails sur les règles du jeu, [c'est par ici](https://github.com/xebia-france/sim-service-factories/blob/master/regles.md)

Guide de développement
----------------------

Afin de développer la factory, il suffit de suivre ce guide développement. Chaque étape est agrémentée de la documentation vertx correspondante.

Pour vous aider dans votre développement, chaque squelette contient déjà:
* Le hello périodique indiquant que votre application est en vie
* Un (et un seul) cycle d'achat complet, sans la gestion du stock et des coûts

#### Vertx

Vertx est un framework polyglotte qui permet de faire communiquer facilement des services à travers [un bus d'évènements](http://vertx.io/core_manual_groovy.html#the-event-bus) (entre autres). Dans vertx, chaque service est appelé [Verticle](http://vertx.io/manual.html#verticle).
Les langages exécutable dans vertx sont Java, Javascript, Ruby, Groovy, Python, Scala, Clojure et Ceylon. Nous vous proposons un squelette de Verticle pour bien démarrer dans trois de ces langages, [Java](https://github.com/xebia-france/sim-service-factories/tree/java), [Javascript](https://github.com/xebia-france/sim-service-factories/tree/javascript) ou [Groovy](https://github.com/xebia-france/sim-service-factories/tree/groovy).

Pour démarrer, clonez la branche de votre choix et suivez le guide. Consultez les règles du jeu ou le diagramme de communication pour savoir quel service parle à quel autre service.

L'ensemble des évènements publiés dans le bus seront des évènements JSON.

#### Dites 'Hello'

Chaque batiment doit se faire connaitre à l'ensemble de la ville lorsqu'il est créé. Par la suite, toutes les 2s, il doit indiquer qu'il est toujours en vie.

Pour ce faire, la première chose à faire dans votre Verticle est de [publier un évènement](http://vertx.io/core_manual_groovy.html#publishing-messages) **hello** dans le format suivant:

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

Choisissez un nom de team. Le squelette choisi contient déjà un identifiant prédéfini. Enfin, indiquez la version de votre factory (1.0 au départ).

Maintenant, il vous faut indiquer toutes les 500 ms que votre factory est en vie. Heureusement, vertx propose un [ensemble d'outils](http://vertx.io/core_manual_groovy.html#periodic-timers) permettant de faire cela facilement.

#### Déployez votre factory

Maintenant que votre factory peut se signaler, il faut la déployer pour qu'elle s'installe en ville.
Un script factory.sh est disponible dans votre workspace. Celui ci permet de déployer à distance sur une instance amazon votre factory et de la démarrer.
Il s'exécute comme suit:

```
./factory.sh 'start|status|list|stop|restart|logs' [config]
```

N'hésitez pas à demander le nom de votre machine pendant l'event.

Pour faciler la connection au machine, il vous faut une clé ssh valide.
Si vous n'en possédez pas, exécuter la commande suivante:

```
ssh-keygen -t dsa
```

Dans tous les cas, exécutez la commande suivante (en indiquant le bon nom de machine):

```
cat ~/.ssh/id_dsa.pub | ssh admin@nom-machine "cat - >> ~/.ssh/authorized_keys"
```

Enfin, dans votre ~/.ssh/config, ajoutez les lignes suivantes:

```
Host sim-factory
        Hostname nom-machine
        User admin
```

Les méthodes start et restart du script vont vous permettre de synchroniser votre code et de (re)démarrer votre factory autant de fois que vous le souhaitez. 
La méthode logs vous permets de voir vos logs d'éxécution.

Le fichier de configuration factory.json est utilisé par défaut avec votre factory. Si vous souhaitez variabiliser des élements de votre application, [vous pouvez l'utiliser facilement](http://vertx.io/core_manual_groovy.html#getting-configuration-in-a-verticle)

Une fois votre factory déployée, vous devriez la voir apparaitre [ici](http://sim-core.aws.xebiatechevent.info:8080/):

#### Achetez du houblon

Maintenant que votre factory existe et le dit, il lui faut de la bière à vendre.
Pour faire de la bière, il faut du houblon, et c'est le rôle des farms

Lors de votre premier lancement, vous avez un stock de 0 et une trésorerie de 0. Il vous est possible d'acheter du houblon à crédit. Attention au delà d'un certain montant de dette, la banque appliquera des pénalités.

Pour acquérir le houblon, vous pouvez publier un nouveau message sur l'adresse /city/farm dans ce format:

```
{
   "action": "request",
   "from": "factory id",
   "quantity": 10
}
```

Chaque farm vous retournera alors une offre comme celle-ci sur votre adresse privée /city/factory/votre-id:

```
{
    "action": "response",
    "from": "farm id",
    "quantity": 3,
    "cost": 30
}
```

Pour recevoir ces messages, il vous faut [écouter](http://vertx.io/core_manual_groovy.html#registering-and-unregistering-handlers) sur l'adresse /city/factory/votre-id

Une farm peut vous faire une offre avec un stock inférieur à votre demande si elle ne peux pas y répondre intégralement.

Ce message est avec [timeout](http://vertx.io/core_manual_groovy.html#request-timeouts) vous devez donc [répondre](http://vertx.io/core_manual_groovy.html#replying-to-messages) assez rapidement que vous êtes intéressé:

```
{
    "action": "acquittement",
    "from": "factory id",
    "quantity": 9
}
```

Une réponse ne veux pas dire que vous pouvez augmenter votre stock. En effet c'est la bank qui va vous signaler que la transaction s'est bien passée en vous contactant directement avec ce message:

```
{
    "action": "purchase",
    "from": "bank",
    "quantity": 9,
    "cost": 100
}
```

#### Vendre de la bière

Le but des factories est de vendre la bière aux stores.

Un store communique de la façon suivante avec vous:

```
{
   "action": "request",
   "from": "store id",
   "orderId": "order id",
   "quantity": 10,
   "cost" : 1000
}
```

Il vous indique ainsi la quantité de bière voulue ainsi que le prix qu'il est pret à payer pour ça.
Le store publie le message sur /city/factory, ainsi, **toutes** les factories de Xebia-city vont recevoir le message. A vous d'être le plus rapide pour répondre le premier.

Pour recevoir les messages, il vous faut écouter sur l'adresse /city/factory

Pour répondre aux demandes des stores, vous devez [envoyer](http://vertx.io/core_manual_groovy.html#sending-messages) une offre au store sur son adresse privée /city/store/id-store:

```
{
    "action": "response",
    "from": "factory id",
    "orderId": "order id",
    "quantity": 10
}
```

Celui ci va vous répondre (ou pas) qu'il accepte votre demande comme suit:

```
{
    "action": "acquittement",
    "from": "store id",
    "quantity": 10
}
```

Ne débitez pas votre stock immédiatement, comme pour l'achat de houblon, seule la bank validera cette transaction en vous envoyant le message suivant sur votre adresse privée:

```
{
    "action": "sale",
    "from": "bank",
    "quantity": 10,
    "cost": 1000
}
```

#### Récupérer les métriques de votre factory

A intervalle régulier la banque envoie des métriques concernant votre factory sur /city/factory/votre-id

```json
{
    "action": "status",
    "from": "bank",
    "purchases": 100,
    "sales": 100,
    "costs": 100,
    "stocks": 100
}
```

Voilà, vous avez toutes les informations nécessaires pour faire votre factory. A vous d'optimiser tout ça pour être la factory la plus rentable.
