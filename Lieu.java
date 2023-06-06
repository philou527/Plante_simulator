public class Lieu {

    int piece;// 1=salon, 2=cuisine, 3=salle de bain, 4=cave
    int temperature; //de -10 à +30
    int humidite; // de 0 à 100
    int luminosite; // de 0 à 100
    int temperatureFixe; //de -10 à +30
    int humiditeFixe; // de 0 à 100
    int luminositeFixe; // de 0 à 100
    int temps;// 1=matin, 2=midi, 3=soir
    String urlBg; // l'url de l'image png pour le fond
    int saison;// 1=printemps, 2=été, 3=automne, 4=hiver
    boolean fenetreOuverte;
    boolean chauffage;


    public Lieu(/*int p, int temp, int h, int l, int t, String bg, int s*/ int[] infos, int saisonChoisie){
        // on a stocké ici nos différentes valeurs de lieu dans un tableau!
        piece = infos[0];
        temperature = infos[1];
        humidite = infos[2];
        luminosite = infos[3];
        temperatureFixe = infos[1];
        humiditeFixe = infos[2];
        luminositeFixe = infos[3];
        temps = infos[4];
        urlBg = "png\\bg"+String.valueOf(infos[0])+"_1.png"; // patern sur l'url de notre bg
        saison = saisonChoisie; //stat -20 ou +20 selon la saison
        chauffage = false;
        fenetreOuverte = false;

    }

    public void TempsQuiPasse(){

        if(temps==1){
            temps=2;
            temperature+=2+(int)(Math.random()*4);
            luminosite+=15+(int)(Math.random()*10);
            testFenetreOuverte();
            testChauffage();
        }else if(temps==2){
            temps=3;
            temperature-=(int)(Math.random()*4);
            luminosite-=25+(int)(Math.random()*10);
            testFenetreOuverte();
            testChauffage();
        }else if(temps==3){
            temps=1;
            temperature=temperatureFixe-(2+(int)(Math.random()*4)); //retour aux stats de base après la nuit
            luminosite=luminositeFixe-(18+(int)(Math.random()*5));
            testFenetreOuverte();
            testChauffage();
        }else{//initialisation (le matin)
            temps=1;
            temperature = temperatureFixe -2+(int)(Math.random()*4);
            luminosite= luminositeFixe -18+(int)(Math.random()*5);
            humidite = humiditeFixe;
        }
        urlBg="png\\bg"+String.valueOf(piece)+"_"+String.valueOf(temps)+".png"; // nouveau bg
    }
    
    public void ActualisationSaison(){
        if(saison==2){ //augmentation stats en été
            temperatureFixe+=10; 
            luminositeFixe+=20;
            humiditeFixe+=20;
        }
        if(saison==4){ //augmentation stats en hiver
            temperatureFixe-=5;
            luminositeFixe-=15;
            humiditeFixe-=15;

        }else{ //retour aux stats de base au printemps et à l'automne
            temperature=temperatureFixe;
            luminosite=luminositeFixe;
            humidite=humiditeFixe;
        }
    }

    public void testFenetreOuverte(){
        if(fenetreOuverte){
            if(saison==2){ //augmentation stats en été
                temperature-=5;
                humidite+=10;
            }
            if(saison==4){ //diminution stats en hiver
                temperature-=10;
                humidite-=10;
    
            }else{ //au printemps et à l'automne
                temperature-=5;
                humidite+=5;
            }
        }

    }

    public void testChauffage(){
        if(chauffage && fenetreOuverte){
            temperature+=2;
        }else if(chauffage){
            temperature+=5;
        }
    }
}
