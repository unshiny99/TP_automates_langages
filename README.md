# TP_automates_langages
TP d'automates portant sur l'étude de données de transport, réalisé en Java.

## Fichier `InterCites.txt`
- Dans ce fichier, on fait l'hypothèse que le bus repart dès qu'il arrive, il n'y donc **pas de temps de pause**.
- On suppose aussi que le trajet prend autant de temps dans un sens que dans l'autre.
- Les liaisons doivent être insérées dans l'ordre chronologique de leur heure de départ.
On peut donc directement retrouver les durées de trajet en faisant la différence entre les 2 heures de départs des stations consécutives.

*Remarque* : Pour le moment, la dernière laison n'est pas prise en compte. 
Cela est dû au fait que nous n'avons pas l'information sur son heure d'arrivée puisqu'il n'y a pas de prochaine ligne.
On pourra peut-être aller chercher l'info avant les '//' (à voir).

## Fichier `metro.txt`

Il reste à interpréter les commentaires en bas du fichier pour dynamiser la création d'instance (ne pas faire qu'une liaison mais tous les tant de temps, et respecter les horaires).
Le coeur du programme est fonctionnel.

## Fichier `bus.json`

Dans ce fichier, il est important de respecter le nom des clés données. En effet, si le nom n'est plus le même, l'incorporation ne se fera plus correctement.
On doit bien entendu respecter la syntaxe JSON. Enfin, le nom de ligne est obligatoire.
L'implémentation de ce type de fichier est entièrement fonctionnelle.

## Fichier `train.xml`



## Fichier `tram.xml`

