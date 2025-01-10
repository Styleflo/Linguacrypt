# CodingWeek 2025
**TELECOM Nancy - 06/01 au 10/01**


Les instructions sur l'organisation et les attendus de la semaine se trouvent dans le fichier [INSTRUCTIONS.md](./INSTRUCTIONS.md).

# Guide de configuration et d'exécution du projet

## Prérequis
Avant d'exécuter le projet, assurez-vous d'avoir les éléments suivants installés sur votre système :

- **Java JDK** (version 21.0.5 ou supérieure) installé et ajouté à la variable PATH de votre système.
- Pas besoin d'installer Gradle séparément, car le projet inclut le Gradle Wrapper.

## Étapes pour construire et exécuter le projet

### 1. Placez-vous dans le répertoire racine du projet
Ouvrez un terminal ou une invite de commande et placez-vous dans le répertoire racine du projet (là où se trouve le fichier `gradlew`) :

```bash
cd /chemin/vers/le/projet
```

### 2. Construisez et exécutez le projet avec Gradle Wrapper
Utilisez les commandes suivantes en fonction de votre système d'exploitation :

#### Sous Windows :

```cmd
gradlew.bat build
gradlew.bat run
```

#### Sous macOS ou Linux :

```bash
./gradlew build
./gradlew run
```


### 3. Si vous souhaitez utiliser le JAR

Le fichier JAR est disponible dans le répertoire `out/REALEASE_FINAL.jar`.

Exemple :
```bash
/chemin/vers/le/projet/out/REALEASE_FINAL/REALEASE_FINAL.jar
```

### 4. Exécutez le fichier JAR
Pour exécuter l'application, utilisez la commande suivante :


#### Sous macOS ou Linux :

```bash
java --module-path $PATH_TO_FX --add-modules javafx.base,javafx.controls,javafx.fxml -jar REALEASE_FINAL.jar
```
Généralement, le nom de la variable d'environnement est JAVAFX_HOME, la commande est alors :

```bash
java --module-path $JAVAFX_HOME --add-modules javafx.base,javafx.controls,javafx.fxml -jar REALEASE_FINAL.jar
```

## Dépannage

1. **Problèmes de permissions sous macOS/Linux :**
   Si vous rencontrez une erreur "Permission denied" lors de l'exécution de `./gradlew`, accordez les permissions d'exécution avec :
   ```bash
   chmod +x gradlew
   ```

2. **JAVA_HOME non défini :**
   Assurez-vous que votre variable d'environnement `JAVA_HOME` pointe vers votre installation JDK.

3. **Dépendances obsolètes :**
   Si le projet échoue à se construire à cause de dépendances manquantes ou obsolètes, essayez de lancer :
   ```bash
   ./gradlew clean build
   ```

Pour toute assistance supplémentaire, contactez l'équipe de développement :) 

## COMPTES RENDU JOURNALIER DU CODAGE DE L'APPLICATION


Compte Rendu Jour 1 : 

- Avancés sur la construction des Classes Jeu, Plateau, Cartes qui construisent la logique du jeu. 

- Avancés sur les différentes vues utilisateur, l'utilisateur peut : 

        - Ouvrir l'application, il arrive sur un Menu avec le nom du jeu.
        - Du Menu, il peut choisir de cliquer sur 2 boutons : Cartes et Jouer. 
        - S'il appuie sur Cartes, il arrive sur une page pour visualiser les cartes, quelques cartes exemple sont codée de façon temporaire.
        - S'il appuie sur Jouer, il visualise sur un plateau de 5*5 cartes.

- Avancés sur la carte clef : 

        - un QRcode est formé dans Ressources/assets, les espions peuvent, en le scannant, obtenir la répartition des agents


Compte Rendu Jour 2 : 

- Avancés sur la construction d'une nouvelle classe Plateau pour pouvoir faire un plateau avec des images. 

- Avancés sur les vues utilisateur, l'utilisateur peut : 

        - Ouvrir l'application, il arrive sur un Menu avec le nom du jeu.
        - Du Menu, il peut choisir de cliquer sur 3 boutons : Cartes, Jouer et Quitter. 
        - S'il appuie sur Cartes, il arrive sur une page pour visualiser les cartes, il peut naviguer entre les différentes catégories pour regarder toutes les cartes avec les boutons "Thème précédent" et "Thème suivant". 
        Il peut également choisir d'ajouter une carte à une catégorie en appuyant sur : "Ajouter un mot à la Collection". Enfin, il peut revenir au menu principal. 
        - S'il appuie sur Jouer, il arrive sur une superbe page de jeu ou il voit un plateau de 5*5 cartes et où il peut jouer ! Quand il clique sur les cartes, leur couleur se révèle ce qui met à jour des petits compteurs. Si les joueurs se trompent de carte, le tour passe directement. 
        Il peut appuyer sur "Nouvelle Partie" pour faire une nouvelle partie. Il peut appuyer sur "Menu Principal" pour retourner sur celui-ci. Il peut enfin cliquer sur "Tour Suivant" pour laisser l'équipe suivante jouer.  
        - S'il appuie sur Quitter, il quitte l'application.

- Quelques tests mis en place. 


Compte Rendu Jour 3 : 

- Avancés sur les fonctions de sauvegarde d'une partie, désormais, quand le joueur veut quitter la partie, une pop s'affiche pour demander à l'utilisateur s'il souhaite que sa partie soit enregistrée avant de revenir sur le menu. La partie enregistrement n'est en pratique pas encore tout à fait aboutie. 

- Avancés sur l'obtention de la clef indice : une application Android permettant de lire le QR code et de montrer une image de la solution a été implémentée. 

- Les vues utilisateur, ont été améliorées du point de vue design. 

- Avancée sur les vues utilisateur, l'utilisateur peut : 

        - Ouvrir l'application, il arrive sur un Menu avec le nom du jeu.
        - Du Menu, il peut choisir de cliquer sur 3 boutons : Cartes, Jouer et Quitter. 
        - S'il appuie sur Cartes, il arrive sur une page. 
        - S'il appuie sur Jouer, il arrive sur une page de paramètres ou il peut choisir les paramètres de sa partie avant de jouer : choisir le nombre de colonnes, de lignes, de prendre des thèmes aléatoires ou de choisir son thème. 
        - Quand il est sur le plateau de jeu, il peut couvrir les cartes en cliquant dessus. (Pour l'instant, les images pout couvrir les cartes sont aléatoires.)


Compte Rendu Jour 4 : 

- Amélioration des designs.

- Travail sur l'enregistrement et l'ouverture de parties archivées. 

- Avancée sur les vues utilisateur, l'utilisateur peut : 

        - L'utilisateur peut désormais choisir de jouer avec des images. 
        - Il peut également voir la collection de toutes les cartes image en appuyant sur l'icone image en bas à droite de sa page. 
        - Dans le menu Paramètre, il peut choisir de jouer en mode image en cochant son choix dans la case qu'il souhaite. 
        - Du menu Paramètre, il peut aussi load une de ses anciennes parties en appuyant sur le bouton en bas à droite. 


Compte Rendu Jour 5 : 

- Amélioration des designs.

- Il est désormais possible d'ajouter des images dans le deck image depuis l'ordinateur de l'utilisateur dans le menu image. 

- Le compteur fonctionne correctement, les joueurs peuvent selectionner le temps que va durer leur partie. 






