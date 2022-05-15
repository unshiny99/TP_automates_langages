# TP_automates_langages
TP d'automates portant sur l'étude de données de transport, réalisé en Java et en monôme.

## Fichier `InterCites.txt`
- Dans ce fichier, on fait l'hypothèse que le bus repart dès qu'il arrive, il n'y donc **pas de temps de pause**.
- On suppose aussi que le trajet prend autant de temps dans un sens que dans l'autre.
- Les liaisons doivent être insérées dans l'ordre chronologique de leur heure de départ.
On peut donc directement retrouver les durées de trajet en faisant la différence entre les 2 heures de départs des stations consécutives.

**Remarque** : Pour le moment, la dernière laison n'est pas prise en compte. 
Cela est dû au fait que nous n'avons pas l'information sur son heure d'arrivée puisqu'il n'y a pas de prochaine ligne.
On pourra peut-être aller chercher l'info avant les `//` (à voir).

## Fichier `metro.txt`

Le coeur du programme est fonctionnel. On instancie autant de liaisons qu'il y a de lignes de définition dans le fichier.

Les 4 lignes de commentaires en bas sont très importantes. 


## Fichier `bus.json`

Dans ce fichier, il est important de respecter le nom des clés données. En effet, si le nom n'est plus le même, l'incorporation ne se fera plus correctement.
On doit bien entendu respecter la syntaxe JSON. Enfin, le nom de ligne est obligatoire.
L'implémentation de ce type de fichier est entièrement fonctionnelle.

**[IMPORTANT] : Ajouter les clés nécessaires**

## Fichier `train.xml`

Les balises XML doivent être dans un ordre précis. On doit avoir la balise racine `horaires`, qui contient des `line` (lignes), qui sont composées de ``junction`` (jonctions/liaisons), qui elles-mêmes contiennent 4 attributs :
- ``start-station`` : station de départ
- ``arrival-station`` : station d'arrivée
- ``start-hour`` : heure de départ
- ``arrival-hour`` : heure d'arrivée

## Fichier `tram.xml`

Les balises XML doivent être dans un ordre précis. On doit avoir la balise racine `reseau`, qui contient un élément `lignes`, qui est composé de `ligne`, qui elles-mêmes contiennent des ``stations`` et des `heures-passage` (heures de passage).

Ces heures ont la particularité de pouvoir être à plusieurs sur une même balise, puisqu'on doit avoir autant d'heures que de stations.

Le programme est tel qu'on ne regarde pas la liste des stations définie au début, donc si une station n'est pas comprise dans une des ``ligne``, elle ne sera pas instanciée.