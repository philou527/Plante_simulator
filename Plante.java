
public class Plante {

    boolean game; // boolean pour savoir si le jeu est fini ou non
    int life; // pv de la plante
    String name; // nom de la plante
    int level; // level de le plante
    int nextLevelIn; // point d'exp de la plante à gagner pour atteindre le prochain niveau
    int expTotal = 0; // exp total
    int type; // type de la plante
    Lieu lieu; // lieu dans lequel se trouve la plante 
    String planteImgURL; // url png de la plante

    int masse; // la masse va nous servir à faire les calculs de co2 et O2 (en gramme)
    int masseFinale; // masse que la plante va atteindre à la fin
    int besoinEnEau; // besoin en eau quotidien : pourcentage
    int besoinLum; // besoin en lumière : pourcentage
    int besoinTemp; // température nécessaire
    int humidite; // humidité de la plante : pourcentage

    int facteurHum;
    int facteurLum;
    int facteurTemp; // facteurs utiles aux calculs d'xp et de vie
    int calculHum;

    int levelMax = 4;
    int ageEnJour; // l'age de la plante

    public Plante(int pv, String n, Lieu pos, int[] infos) {
        this.game = true;
        this.life = pv;
        this.name = n;
        this.type = infos[0]; // on stocke le type de la plante pour savoir quelle image pick pour la plante
        this.level = 0; // normalement fixé à 0
        this.nextLevelIn = 100; // lié à son level de départ
        this.lieu = pos;
        this.masse = 0; // au départ masse nulle
        this.ageEnJour = 0;

        /* -- Importation des données de la plante en fonction de la plante choisie -- //
            -- Les informations seront stockées dans un tableau 2D qui pourra souvent être mis à jour -- */
        this.besoinEnEau = infos[1];
        this.humidite = infos[2]; // la plante commence avec son humidité au bon niveau
        this.besoinLum = infos[3];
        this.facteurHum = infos[4];
        this.facteurLum = infos[5];
        this.facteurTemp = infos[6];
        this.masseFinale = infos[7];
        this.calculHum = infos[8];
        this.besoinTemp = infos[9];

        this.planteImgURL = "png\\plante_" + Integer.toString(type) + "_" + "0.png"; 
        // png de forme plante_nomDeLaPlante_niveauPlante, ici définie à la base au niveau 0
    }


    // -- Fonction pour gérer les gains d'exp (prochain palier, nouvelles caractéristiques, etc) -- //
    public void gainExp(int nbExpGagne) {

        expTotal += nbExpGagne; // on ajoute l'exp gagné

        if (nbExpGagne <= nextLevelIn) { // verifier si on peut tjr gagner en exp
            nextLevelIn -= nbExpGagne; // on soustrait au nombre d'exp à gagner

        } else { // on a atteint le nb d'exp à obtenir, on a un surplus d'exp
            nbExpGagne -= nextLevelIn;  // on soustrait le nombre d'exp qu'on a gagné par le nb d'exp restant à atteindre
            switch (level) {

                case 0:
                    level = 1; // rang 1 atteint
                    nextLevelIn = 200; // on refixe le nouveau nombre d'exp à obtenir
                    besoinEnEau -= 2; // on considère que plus la plante grandit plus ses besoins seront moindre
                    besoinLum -= 2;
                    break;
                case 1:
                    level = 2;
                    nextLevelIn = 500;
                    besoinEnEau -= 2;
                    besoinLum -= 2;
                    break;
                case 2:
                    level = 3;
                    nextLevelIn = 1000;
                    besoinEnEau -= 3;
                    besoinLum -= 3;
                    break;
                case 3:
                    level = 4; // phase adulte
                    nextLevelIn = 10000;
                    besoinEnEau -= 3;
                    besoinLum -= 3;
                    break;
            }
            miseAJourImgPlante(); // maj du png de notre plante
            masse = (masseFinale * level) / levelMax; // nouvelle masse à chaque niveau
            nextLevelIn -= nbExpGagne; // on soustrait le surplus au nouveau nombre d'exp à gagner
        }
    }

    // -- Arrosage de la plante -- //
    public void arroser() {
        if(humidite<100){
           humidite += (int) (30 / calculHum); 
        }else{
            humidite=100;
        }
        
    }
    // -- Compostage -- //
    public void compost() {
        
        if(life<100&&humidite<100){
            humidite += (int) (10 / calculHum);
            life += (int) (20 / calculHum);
        }else{
            humidite=100;
            life=100;
        }
    }

    // -- Etat physique de notre plante -- // 
    public String etat() {

        if (life >= 70) {
            return "Votre plante se porte plutôt bien !";
        } else if (life > 40 && life < 70) {
            return "Votre plante n'est pas au top de sa forme :c";
        } else {
            return "Votre plante va bientôt mourir...";
        }

    }

    // -- Caractéristiques de la plante -- //
    public String caracteristiques() {

        String texte = "Voici les caracteristiques de votre plante: \n";
        texte += "Nom : " + name + "\nType : " + String.valueOf(type) + "\nSon besoin en eau : "
                + String.valueOf(besoinEnEau) + "\nL'expostion nécessaire en lumière : " + String.valueOf(besoinLum);

        return texte;

    }

    // -- Calcul du CO2 absorbé par la plante en fonction de paramètres -- //
    public double co2Absorbe() {
        double co2Absorbe = 0;
        // - pour avoir une bonne absorption, il faut avoir de la luminosité
        // - pour une plante d'intérieur, il y a absorption de CO2 entre 0,37 et
        // 3,7kg/an ou de 0,04 à 0,4g/heure

        int lum = lieu.luminosite; // on recupere la luminosité de la piece

        co2Absorbe = (masse / (360 * 24)) * (life / 100); // co2 (en grammes) absorbé par heure en fonction de la masse
                                                          // de la plante!
       
        if(lum>=60&&masse!=0){ // facteur lumière et de la masse
            co2Absorbe+=0.05;
         } 
         else if(lum<60&&co2Absorbe>=0.05){
            co2Absorbe-=0.05;
         }
       // facteur lumiere plus la lumiere est presente + il y aura de co2 absorbé

        return co2Absorbe;
    }

    // -- Calcul du dioxygene produit -- // 
    public double prodDioxyg() {
        double prodO2 = 0; // une feuille moyenne dégage environ 5 millilitres d’oxygène par heure, soit
                           // 1.43mg/heure

        prodO2 = ((masse / 4) * 1.43) * (life / 100);
        return prodO2; // en mg/heure !
    }

    // -- Changement de piece maj à faire dans la plante -- // 
    public void miseAJourLieu(Lieu newLieu) { 
        lieu = newLieu;
    }

    // -- Changement de png sur la plante -- // 
    public void miseAJourImgPlante() {

        planteImgURL = "png\\plante_" + String.valueOf(type) + "_" + String.valueOf(level) + ".png"; // patern

    }

    // cette fonction permet au joueur de faire un saut dans le temps et voir le
    // nouvel état de sa plante!
    public void maJTemporel() {
        // on fixe des seuils de paramètres à ne pas dépasser durant un laps de temps
        if(humidite<=100){
            humidite += (int) ((masse / 1000) * lieu.humidite / calculHum);
            humidite -= (int) (100/calculHum)+(20/level);
        }else{
            humidite = 100;
        }
        // -- On regarde si nos arrosages, compostes ont été utiles ou pas ! --
        if(life>0){ // tant que pv n'est pas plus petit ou égale à 0, on ne détruit pas la plante
            lvlETlife();
            
        }
        else{
            game = destructionPlante();
        }
        
    }

    public void lvlETlife() {
        int ecartH = Math.abs(besoinEnEau - humidite);
        int ecartL = Math.abs(besoinLum - lieu.luminosite);
        int ecartT = Math.abs(besoinTemp - lieu.temperature);

        if(ecartH==0){
            ecartH=1;
        } else if(ecartL==0){
            ecartL=1;
        } else if(ecartT==0) {
            ecartT=1;
        }
        


        if (ecartH < facteurHum) { // on répartit le gain d'xp et la perte de vie en 3 caractéristiques (donc
                                   // "xptotal" = somme des xp et pareil pour life)
            int xp = (int) ((level + 1) * (facteurHum / ecartH) * (5 / 3));
            gainExp(xp);
        } else {
            if (besoinEnEau <= 50 && life>0) {
                life -= (int) ((ecartH * 25) / (3 * (100 - besoinEnEau) - facteurHum));
            } else {
                life -= (int) ((ecartH * 25) / (3 * besoinEnEau - facteurHum));
            }
        }

        if (ecartL < facteurLum) {
            int xp = (int) ((level + 1) * (facteurLum / ecartL) * (5 / 3));
            gainExp(xp);
        } else {
            if (besoinLum <= 50 && life>0) {
                life -= (int) ((ecartL * 25) / (3 * (100 - besoinLum) - facteurLum));
            } else {
                life -= (int) ((ecartH * 25) / (3 * besoinLum - facteurLum));
            }
        }

        if (ecartT < facteurTemp) {
            int xp = (int) ((level + 1) * (facteurTemp / ecartT) * (5 / 3));
            gainExp(xp);
        } else {
            if (besoinTemp <= 20 && life>0) {
                life -= (int) ((ecartT * 25) / (3 * (100 - besoinTemp) - facteurTemp));
            } else {
                life -= (int) ((ecartT * 25) / (3 * besoinTemp - facteurTemp));
            }
        }

    }

    // -- fonction pour détruire la plante et signaler au jeu que c'est terminé -- //
    public boolean destructionPlante(){

        return false;
    }

}
