Règles
------

Voici le fonctionnement de chaque batiment de Xebia-city.

Il existe plusieurs types de farms et de stores, chacun avec des propriétés différentes. Aux factories a s'adapter au mieux à leur environnement. 

**Une farm:**

* Produit des quantités de houblon à interval régulier
* Le stock maximal de houblon est limité par factory
* Chaque unité de houblon a un prix
* Lorsqu'une farm reçoie une demande de houblon, elle propose une offre en fonction de son stock disponible. Cette offre expire au bout d'un délai prédéfinit
* Lorsqu'une factory accepte une offre d'une farm, celle-ci envoie la facture à la bank


**Une factory:**

* Reçoie les demandes des stores en bières
* Demande la quantité de houblon correspondant aux factories
* Choisie une ou des offres renvoyées par les factories et lui notifie l'achat
* Envoie une offre au store (qu'il peut ne pas accepter)
* Si le stock d'une factory est trop important, la bank la pénalise
* La bank peut faire crédit à une factory, mais si la factory dépasse ce crédit, elle est pénalisée


**Un store:**

* Demande à toutes les factories une quantité de bière en échange d'un prix
* Une fois que les factories ont répondu, choisit une offre et informe la factory choisie ainsi que la bank


**La bank:**

* Maintient l'état des stocks et des comptes des factories. Elle est notifié à chaque achat / vente réalisé par une factory
* Lorsqu'une vente de houblons est conclue entre une factory et une farm, la bank notifie la factory. Sans cette notification, la transaction ne peux être considérée validée
* Lorsqu'une vente de bière est réalisée entre une factory et un store, la bank notifie la factory. Sans cette notification, la transaction ne peux être considérée validée
* Si une factory triche, la bank la sanctionne
* Si une factory a trop de stock, la bank applique des pénalités

Les factories doivent donc être très attentive aux notifications de la banque qui seules leurs donnent le droit de modifier leurs stocks.
Ne pas suivre cette rêgle expose la factory incriminée à des sanctions.

**Règles communes:**

* Lors qu'un batiment se connecte, il doit se faire connaitre
* Un batiment doit signaler sa présence toutes les 500 ms

