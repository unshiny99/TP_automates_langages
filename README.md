# TP_automates_langages
TP d'automates portant sur l'étude de données de transport, réalisé en Java et en monôme.

**5 fichiers sur 5** sont entièrement fonctionnels et génèrent les données attendues.

Librairies externes utilisées : ```json-simple-1.1``` pour le JSON, ``dom-2.3.0-jaxb-1.0.6`` pour le XML.

## Fichier `InterCites.txt`
- Dans ce fichier, on fait l'hypothèse que le bus repart dès qu'il arrive, il n'y donc **pas de temps de pause**.
- On suppose aussi que le trajet prend autant de temps dans un sens que dans l'autre.
- Les liaisons sont insérées dans l'ordre chronologique de leur heure de départ par convention (mais fonctionne quand même si on ne respecte pas ça).

**Remarque** : 
On doit préciser les jonctions et leur durée avant les `//`, et les liaisons avec l'heure de départ après ces derniers.

## Fichier `metro.txt`

Le programme est fonctionnel. On instancie autant de liaisons qu'il y a de départs de la station initiale * (nombre total de stations) - 1.

Les 4 lignes de commentaires en bas sont très importantes. Elles permettent de définir les paramètres de génération des différentes liaisons.

Elles doivent commencer par ``%``, et doivent respecter le format correspondant (lettres, espaces et nombres dans un ordre précis).
Les accents sont tolérés par l'expression régulière du programme.

**Remarque** :
On prend en compte que la station de départ est toujours "Gare" dans cette version pour la génération des départs dynamiques de métro (donnée statique).


## Fichier `bus.json`

Dans ce fichier, il est important de respecter le nom des clés données. En effet, si le nom n'est plus le même, l'incorporation ne se fera plus correctement.
On doit bien entendu respecter la syntaxe JSON. Enfin, le nom de ligne est obligatoire.
L'implémentation de ce type de fichier est entièrement fonctionnelle.

**[IMPORTANT] : Ajouter les clés nécessaires ICI**

## Fichier `train.xml`

Les balises XML doivent être dans un ordre précis. On doit avoir la balise racine `horaires`, qui contient des `line` (lignes), qui sont composées de ``junction`` (jonctions/liaisons), qui elles-mêmes contiennent 4 attributs :
- ``start-station`` : station de départ
- ``arrival-station`` : station d'arrivée
- ``start-hour`` : heure de départ
- ``arrival-hour`` : heure d'arrivée

## Fichier `tram.xml`

Les balises XML doivent être dans un ordre précis. On doit avoir la balise racine `reseau`, qui contient un élément `lignes`, qui est composé de `ligne`, qui elles-mêmes contiennent des ``stations`` et des `heures-passage` (heures de passage).
Bien entendu, le langage impose qu'elles soient imbriquées et refermées (pas de possibilité de "croiser" 2 balises).

Ces heures ont la particularité de pouvoir être à plusieurs sur une même balise, puisqu'on doit avoir autant d'heures que de stations.

Le programme est tel qu'on ne regarde pas la liste des stations définie au début, donc si une station n'est pas comprise dans une des ``ligne``, elle ne sera simplement pas instanciée.