// -- importation des bibliothèques --

import javax.swing.*; // -- library IHM --
import java.awt.event.*;
import java.awt.*;

import java.io.*; // -- library data --
import java.nio.*;


public class Jeu extends JFrame implements ActionListener {

    // -- tableau utilisé au moment des changements de lieu --
    int[][] infosLieu = { { 1, 20, 50, 70, 0 }, { 2, 20, 60, 60, 0 }, { 3, 22, 70, 25, 0 }, { 4, 13, 30, 30, 0 } };


    // -- variables de départ pour le setup de notre fenetre --
    
    static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static int w = (int)size.getWidth()*3/7;
    static int h = (int)size.getHeight()-50; // le -50 pour enlever la petite bordure de fin
    private Plante plante;
    private Lieu lieu;
    Lieu newLieuu;
    boolean menuVisb = false; // -- ces booleans servent à faire un système de "bouton poussoir" pour nos "popups" --
    boolean menuLieu = false;
    boolean menuCarac = false;

    // -- boutons des actions --
    JButton bArroser,bComposter, bChangerPiece, bSautTempor, bEtat; // on rajoutera d'autres actions?
    JButton bFenetre,bChauffer;
    // -- boutons pour les lieus --
    JButton lieu1,lieu2,lieu3,lieu4;
    // -- affichage des caractéristiques/stats --
    JLabel pv, exp, nextLvl, hum, level, etat;
    JLabel co2, o2, mass, compteurJour, tempLab, saisonLab; 
    // -- les conteneurs principaux --
    JPanel gameScreen;
    JPanel toolBar;
    JPanel menuPause;
    // -- boutons secondaires --
    JButton sauver;
    JButton quitter;

    // -- conteneurs secondaires --
    JPanel actions;
    JPanel actions2;
    JButton resume;
    JButton prof; // bouton pour le prof pour simuler plus vite l'évolution de la plante
    JPanel canvasLieu;
    JPanel canvasCaract;

    // -- images et bgs --
    JLabel backg;
    ImageIcon bgLieu;
    JLabel pltImage;
    ImageIcon plt;

    public Jeu(String nom,int width,int height, Plante p, Lieu l){
        super(nom);
		setSize(width, height);
		setLayout(null);
		setBackground(Color.GRAY);
		setLocationRelativeTo(null);
		setResizable(false);

        // -- setup des fonts et couleurs --
        Color transparent = new Color(0, 0, 0, 1);

        // -- on récupère la plante soit nouvelle soit loaded de notre sauvegarde --
        plante = p;
        lieu = l;

        // -- barre d'outils du jeu --
        toolBar = new JPanel();
		toolBar.setBounds(0,0,width, height/10);
		toolBar.setLayout(null);
        toolBar.setBackground(new Color(167,193,172));
		this.add(toolBar);
        
        // -- menu/"popup" pause caché au départ --
        menuPause = new JPanel();
        menuPause.setBounds(0,0,width,h/3);
        menuPause.setLayout(null);
        menuPause.setBackground(new Color(132,151,135));
        menuPause.setVisible(false);
        this.add(menuPause);

        // -- Nos 2 boutons sauvegarder et quitter de notre menu --
        sauver = new JButton("Sauvegarder");
        sauver.setBounds(w/15,(h/3)/2,w/5,w/7);
        sauver.setOpaque(false);
        sauver.setFocusPainted(false);
        sauver.addActionListener(this);
        menuPause.add(sauver);

        quitter = new JButton("Quitter");
        quitter.setBounds(w*2/3,(h/3)/2,w/5,w/7);
        quitter.setOpaque(false);
        quitter.setFocusPainted(false);
        quitter.addActionListener(this);
        menuPause.add(quitter);

        // -- Notre bouton Menu pour afficher notre "popup" caché juste en haut --
        resume = new JButton("Menu");
        resume.setBounds(width*9/10,(h/10)/5,width/15,width/15);
        resume.setOpaque(false);
        resume.setFocusPainted(false);
        resume.setBorder(null);
        //resume.setBorderPainted(false);
        //resume.setContentAreaFilled(false);
        resume.addActionListener(this);
        toolBar.add(resume);

        prof = new JButton("Prof");
        prof.setBounds(width*1/20,(h/10)/5,width/15,width/15);
        prof.setOpaque(false);
        prof.setFocusPainted(false);
        prof.setBorder(null);
        prof.addActionListener(this);
        toolBar.add(prof);

        gameScreen = new JPanel();
		gameScreen.setBounds(0,height/10,width, height-(height/10));
		gameScreen.setLayout(null);
        //gameScreen.setBackground(Color.green);
		this.add(gameScreen);


        actions = new JPanel();
        actions.setBounds(0,height/3,width/3,height/3);
        //actions.setLayout(null);
        actions.setBackground(transparent);
        gameScreen.add(actions);

        pltImage = new JLabel(plt);
        pltImage.setLayout(null);
        //plante.setFont(new Font("Arial", Font.PLAIN, 18));
        pltImage.setBounds(w/3,h/2,width/3,height/3);
        //plante.setIcon(plt);
        gameScreen.add(pltImage);



        // -- Grid layout pour les boutons d'actions --
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLocation(0, 0);
        buttonPanel.setBackground(transparent);
        buttonPanel.setLayout(new GridLayout(5,1));
        buttonPanel.add(bArroser = new JButton("Arroser"));
        buttonPanel.add(bComposter = new JButton("Composter"));
        buttonPanel.add(bChangerPiece = new JButton("Changer lieu"));
        buttonPanel.add(bSautTempor = new JButton("Passer le temps"));
        buttonPanel.add(bEtat = new JButton("Caracteristiques"));
        buttonPanel.setPreferredSize(new Dimension(width/3, height/3));
        actions.add(buttonPanel);

        
        bArroser.addActionListener(this);
        bComposter.addActionListener(this);
        bChangerPiece.addActionListener(this);
        bSautTempor.addActionListener(this);
        bEtat.addActionListener(this);
       

        // -- Grid layout pour afficher les infos (états de la plante et les stats créées) --
        JPanel infos = new JPanel();
        infos.setBounds(0,0,width*2/3,height/6);
        infos.setBackground(transparent);
        gameScreen.add(infos); 

        JPanel infos2 = new JPanel();
        infos2.setBounds(width*2/3,0,width/3,height/6);
        infos2.setBackground(transparent);
        gameScreen.add(infos2);
        
        JPanel textPanel = new JPanel();
        textPanel.setLocation(0,0);
        textPanel.setBackground(new Color(172,179,234));
        textPanel.setLayout(new GridLayout(6,1));
        textPanel.add(pv = new JLabel("PV: "));
        textPanel.add(exp = new JLabel("Exp total: "));
        textPanel.add(nextLvl = new JLabel("Prochain niveau: "));
        textPanel.add(hum = new JLabel("Humidite plante: "));
        textPanel.add(level = new JLabel("Level: "));
        textPanel.add(etat = new JLabel("Etat: "));
        textPanel.setPreferredSize(new Dimension(width*2/3,height/6));
        infos.add(textPanel);
           
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(new Color(234,172,172));
        statsPanel.setLayout(new GridLayout(6,1));
        statsPanel.add(co2 = new JLabel("CO2 absorbe: "));
        statsPanel.add(o2 = new JLabel("Dioxygène produit: "));
        statsPanel.add(mass = new JLabel("Masse: "));
        statsPanel.add(compteurJour = new JLabel("Compteur jour: "));
        // rajouter température et saison
        statsPanel.add(tempLab = new JLabel("Température salle: "));
        statsPanel.add(saisonLab = new JLabel("Saison: "));
        statsPanel.setPreferredSize(new Dimension(width/3,height/6));
        infos2.add(statsPanel);


        // -- Conteneur/"Pop-up" pour le choix des lieux (invisible au départ) --
        canvasLieu = new JPanel();
        canvasLieu.setBounds(0,h*2/3,width/3, height/7);
        gameScreen.add(canvasLieu);
        JPanel choixLieu = new JPanel();
        choixLieu.setLayout(new GridLayout(2,2));
        choixLieu.add(lieu1 = new JButton("salon"));
        choixLieu.add(lieu2 = new JButton("cuisine"));
        choixLieu.add(lieu3 = new JButton("toilette"));
        choixLieu.add(lieu4 = new JButton("grenier"));
        choixLieu.setPreferredSize(new Dimension(width/3, height/7));
        canvasLieu.add(choixLieu);
        canvasLieu.setVisible(false);
        lieu1.addActionListener(this);
        lieu2.addActionListener(this);
        lieu3.addActionListener(this);
        lieu4.addActionListener(this);

        canvasCaract = new JPanel();
        canvasCaract.setBounds(0,h*2/3,width*4/10, height/7);
        gameScreen.add(canvasCaract);
        JPanel caract = new JPanel();
        caract.setLayout(new GridLayout(1,1));
        JTextArea txtCarac = new JTextArea(plante.caracteristiques());
        caract.add(txtCarac);
        caract.setPreferredSize(new Dimension(width*4/10, height/7));
        canvasCaract.add(caract);
        canvasCaract.setVisible(false);


        // gros canvas pour changer saison, mettre le chauffage et ouvrir la porte
        actions2 = new JPanel();
        actions2.setBounds(width*2/3,height/3,width/3,height/6);
        //actions.setLayout(null);
        actions2.setBackground(transparent);
        gameScreen.add(actions2);

        JPanel button2Panel = new JPanel();
        button2Panel.setLocation(0, 0);
        button2Panel.setBackground(transparent);
        button2Panel.setLayout(new GridLayout(2,1));
        button2Panel.add(bChauffer = new JButton("Chauffage"));
        button2Panel.add(bFenetre = new JButton("Fenetre"));
        button2Panel.setPreferredSize(new Dimension(width/3, height/6));
        actions2.add(button2Panel);
        

        
        bChauffer.addActionListener(this);
        bFenetre.addActionListener(this);


        // -- Resize notre image importé à chaque fois au conteneur gameScreen ! --
        bgLieu=new ImageIcon(new ImageIcon(lieu.urlBg).getImage().getScaledInstance(w, (h-(h/10)), Image.SCALE_DEFAULT));
        backg = new JLabel(bgLieu);
		backg.setLayout(null);
		backg.setBounds(0, 0, w, h);
		gameScreen.add(backg);
      

        setupPlante(plante); // --- on met en place la plante en fonction de son niveau ---
        updateText(); // --- on setup les infos de départ ou sauvegardé de la plante ---
        setVisible(true);
 
    }  
    
    // -- Ecouteur de nos boutons -- 
    public void actionPerformed(ActionEvent e){
        //actions.repaint();
        // -- systeme bouton poussoir pour notre menu (menu servant à quitter ou sauvegarder) -- 
        if(e.getSource()==resume){
            if(menuVisb){
                menuPause.setVisible(false);
                menuVisb = false;
            }else{
                menuPause.setVisible(true);
                menuVisb = true;
            }
            
        }

        // -- Boutons Actions -- 
        if(e.getSource()==bArroser){
            plante.arroser();
        }
        if(e.getSource()==bComposter){
            plante.compost();
        }
        if(e.getSource()==bChangerPiece){
            // -- Systeme bouton poussoir avec la popup du choix des lieux -- 
            if(menuLieu){
                canvasLieu.setVisible(false);
                menuLieu=false;
            }
            else{
                canvasLieu.setVisible(true);
                menuLieu=true;
            }

        }
        if(e.getSource()==bSautTempor){
            plante.maJTemporel();
            lieu.TempsQuiPasse();
            //System.out.println(lieu.temps);
            bgLieu=new ImageIcon(new ImageIcon(lieu.urlBg).getImage().getScaledInstance(w, (h-(h/10)), Image.SCALE_DEFAULT)); // on rescale les images et on l'applique
            backg.setIcon(bgLieu);
            plante.ageEnJour++;
        }
        if(e.getSource()==bEtat){
        // -- Systeme bouton poussoir avec la popup des caractéristiques -- 
            if(menuCarac){
                canvasCaract.setVisible(false);
                menuCarac=false;
            }
            else{
                canvasCaract.setVisible(true);
                menuCarac=true;
            }
        }
        // -- bouton destiné au prof pour simulé plus vite l'évolution de la plante
        if(e.getSource()==prof){
            plante.gainExp(100);
        }

        updateText(); // -- on re-update le texte à chaque appui de nos boutons actions --
        
        if(e.getSource()==bChauffer){
            if(!lieu.chauffage){
                lieu.chauffage=true;
                bChauffer.setBackground(Color.green);
                lieu.testChauffage();
            }
            else{
                lieu.chauffage=false;
                bChauffer.setBackground(new JButton().getBackground()); // pour reprendre la couleur par défault
                lieu.testChauffage();
            }
        }

        if(e.getSource()==bFenetre){
            if(!lieu.fenetreOuverte){
                lieu.fenetreOuverte=true;
                bFenetre.setBackground(Color.green);
                lieu.testFenetreOuverte();
                plante.humidite-=6;
            }else{
                lieu.fenetreOuverte=false;
                bFenetre.setBackground(new JButton().getBackground());
                lieu.testFenetreOuverte();
                plante.humidite+=2;
            }
        }
        // -- Ecouteur pour le choix des lieux
        if(e.getSource()==lieu1){
           newLieuu = new Lieu(infosLieu[0],1); // on définit notre nouveau lieu 
           plante.miseAJourLieu(newLieuu); // on le met à jour dans notre plante
           canvasLieu.setVisible(false); // on ferme notre popup une fois le lieu choisi
           bgLieu=new ImageIcon(new ImageIcon(newLieuu.urlBg).getImage().getScaledInstance(w, (h-(h/10)), Image.SCALE_DEFAULT)); // on rescale les images et on l'applique
           backg.setIcon(bgLieu); // background de notre jeu
           lieu = newLieuu; // affecter le nouveau lieu au jeu

        }
        else if(e.getSource()==lieu2){
           newLieuu = new Lieu(infosLieu[1],1);
           plante.miseAJourLieu(newLieuu);
           canvasLieu.setVisible(false);
           bgLieu=new ImageIcon(new ImageIcon(newLieuu.urlBg).getImage().getScaledInstance(w,(h-(h/10)), Image.SCALE_DEFAULT));
           backg.setIcon(bgLieu);
           lieu = newLieuu;
        }
        else if(e.getSource()==lieu3){
            newLieuu = new Lieu(infosLieu[2],1);
            plante.miseAJourLieu(newLieuu);
            canvasLieu.setVisible(false);
            bgLieu=new ImageIcon(new ImageIcon(newLieuu.urlBg).getImage().getScaledInstance(w, (h-(h/10)), Image.SCALE_DEFAULT));
            backg.setIcon(bgLieu);
            lieu = newLieuu;
        }
        else if(e.getSource()==lieu4){
            newLieuu = new Lieu(infosLieu[3],1);
            plante.miseAJourLieu(newLieuu);
            canvasLieu.setVisible(false);
            bgLieu=new ImageIcon(new ImageIcon(newLieuu.urlBg).getImage().getScaledInstance(w, (h-(h/10)), Image.SCALE_DEFAULT));
            backg.setIcon(bgLieu);
            lieu = newLieuu;
        }

        // -- Sauvegarde de notre jeu --
        if(e.getSource()==sauver){
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter("player_data.txt")); // Sauvegarde dans un fichier texte

                bw.write(""+plante.type);bw.newLine(); // -- on écrit en Str dans notre fichier texte puis on saute une ligne 
                bw.write(plante.name);bw.newLine();     // à chaque fois --
                bw.write(""+plante.life);bw.newLine();
                bw.write(""+lieu.piece);bw.newLine();
                bw.write(""+plante.level);bw.newLine();
                bw.write(""+plante.expTotal);bw.newLine();
                bw.write(""+plante.nextLevelIn);bw.newLine();
                bw.write(""+plante.masse);bw.newLine();
                bw.write(""+plante.humidite);bw.newLine();
                bw.write(""+plante.ageEnJour);bw.newLine();
                bw.write(""+lieu.temperature);bw.newLine();
                bw.write(""+lieu.saison);bw.newLine();
                bw.close();
                dispose(); // -- fermeture de la fenêtre -- 
            }
            catch(Exception ex){ // -- Erreur attrapée --
                System.out.println("Erreur dans la sauvegarde!");
                JFrame popup = new JFrame();
                JLabel textErr = new JLabel("Erreur");
                popup.add(textErr);
            }
        }
        else if(e.getSource()==quitter){ // on quitte directement
            dispose();
        }
        
 
        repaint();
        revalidate();

    }

    // -- Mise en oeuvre de la plante (resize de son png) --
    public void setupPlante(Plante p){
        
        int l=0; // variables pour la longueur et l'hauteur de l'image qui grandissent en fonction de son level
        int ha=0; // on a préféré faire le resize dans le programme et pas sur le logiciel de retouches car ici bien plus modulable
        switch(p.level){
            case 0:
                l=w/7;
                ha=h/10;
                break;
            case 1:
                l=w/6;
                ha=h/7;
                break;
            case 2:
                l=w/4;
                ha=h/4;
                break;
            case 3:
                l=w/4;
                ha=h/4;
                break;
            case 4:
                l=w/3;
                ha=h/3;
                break;
        }
        plt = new ImageIcon(new ImageIcon(p.planteImgURL).getImage().getScaledInstance(l, ha, Image.SCALE_DEFAULT));
        pltImage.setIcon(plt); // on ajoute à la plante son image

    }

    // -- Mise à jour des caractéristiques et stats de la plante ! --

    public void updateText(){

        pv.setText("PV : "+String.valueOf(plante.life));
        exp.setText("Exp total : "+String.valueOf(plante.expTotal));
        nextLvl.setText("Prochain niveau : "+String.valueOf(plante.nextLevelIn));
        hum.setText("Humidite plante: "+String.valueOf(plante.humidite));
        level.setText("Level : "+String.valueOf(plante.level));
        etat.setText("Etat : "+plante.etat());
        plante.miseAJourImgPlante();

        mass.setText("Masse : "+String.valueOf(plante.masse)+" grammes");
        co2.setText("CO2 absorbe : "+String.valueOf(plante.co2Absorbe())+" g/heure");
        o2.setText("Dioxygene produit : "+String.valueOf(plante.prodDioxyg())+" mg/heure");
        compteurJour.setText("Compteur jour : "+String.valueOf((plante.ageEnJour)/3));
        tempLab.setText("Température salle: "+String.valueOf(lieu.temperature)+" °C");

        String saison = (lieu.saison == 1) ? "Printemps" : (lieu.saison == 2) ? "Ete" : (lieu.saison == 3) ? "Automne" : (lieu.saison == 4) ? "Hiver" : "Printemps";
        saisonLab.setText("Saison: "+saison); // opérateur ternaire pour transformer la saison int en string
        setupPlante(plante);

        if(!plante.game){ // si la plante est morte on arrête le jeu !
            System.out.println("GAME OVER");
            // on remet tout à 0 !
            try{
                PrintWriter writer = new PrintWriter("player_data.txt"); 
                writer.print("");
                writer.close();
            }
            catch(Exception ex){
                System.out.println("Erreur dans la création du nouveau fichier!");
            }
            dispose(); // on ferme la fenetre
            InterfaceFenetre newGame = new InterfaceFenetre("Plante Simulator", w, h);
        }
    }

   
}
