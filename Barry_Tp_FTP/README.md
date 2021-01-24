# Serveur FTP
##Auteur
*Barry Mamadou*
## But
Il s'agit de réaliser un programme serveur Ftp. Ce serveur doit pouvoir être 
utilisé par le client ftp en ligne de commande
### Travail à réaliser
- implementer un scénario qui permet à l'utilisateur de s'authentifier en fournissant
un login et un mot de passe
- Implementer la commande quit pour se deconnecter
- Implementer les commandes dir, get, put et la commande cd
### Fonctionnement du serveur
- Lancer le serveur ftp via le fichier [Le fichier jar](out/artifacts/Barry_Tp_FTP_jar/Barry_Tp_FTP.jar) 
avec la commande **java -jar nom_fichier.jar**
- Depuis le terminal lancer le client ftp sur localhost et le numero du port donné par le serveur
- Ainsi vous pouvez utiliser les commandes disponibles sur le serveur
### Extension
Pour ajouter une commande, il s'agit de specifier dans la methode executeCommand le nom de la commande 
et ensuite d'implementer la methode qui gère cette commande.