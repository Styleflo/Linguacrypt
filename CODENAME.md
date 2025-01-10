# Document d'analyse du code  

Le code fonctionne de la façon suivante : 

Une partie qui gère la logique du jeu, avec une classe principale : Jeu, à partir de laquelle on a accès à tout ce qui 
concerne la logique du jeu : 

![img.png](images/img.png)

La logique de chaque partie est enregistrée dans une classe Partie que l'on peut construire avec un builder. 

![img_1.png](images/img_1.png)

Pour chaque partie, on fait un Plateau qui peut être ou un plateau avec des mots ou un plateau avec des images. 

![img_3.png](images/img_3.png)

Idem pour les objets Carte image et Carte mots.

![img_4.png](images/img_4.png)

Enfin, une clef unique est créer pour chaque partie. 

![img_5.png](images/img_5.png)

L'ensemble du diagramme avec les dépendences entre les classes est visible dans l'image package.png.


Les differentes Vue sont implémentées avec fxml et javafx, elles implémentent toute l'interface Observer. 
Le passage d'une vue à une autre se fait grâce à la classe MainController.
