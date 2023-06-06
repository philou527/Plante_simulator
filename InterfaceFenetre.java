import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.nio.*;
import java.util.concurrent.ExecutionException;

public class InterfaceFenetre extends JFrame implements ActionListener {

	int[][] infosPlante = { { 1, 20, 22, 60, 20, 10,6, 650, 8, 22 }, { 2, 50, 52, 20, 10, 20,5, 1600, 6, 17},
			{ 3, 65, 67, 70, 8, 5,3, 450, 7,13 }, { 4, 50, 52, 65, 10, 6,4, 1200, 6, 20 } }; 
	// données setup de : Pippoaetus, Calathéa, Dionée, Desmodium gyrans
	// int type, int bEau, int hum, int besoinLum, int fH, int fL, int fT, int mFinale, int
	// calculHumidite, int besoinTemp

	int[][] infosLieu = { { 1, 20, 50, 70, 0 }, { 2, 20, 60, 60, 0 }, { 3, 22, 70, 25, 0 }, { 4, 13, 30, 30, 0 } };

	static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static int w = (int)size.getWidth()*3/7;
    static int h = (int)size.getHeight()-50;

	Font MochiyPopOne;
	Font titre1;
	Font titre2;
	Font petitMochi;

	Color col;

	JPanel MenuP;

	JButton nvlP;
	JButton cont;
	JButton quit;
	ImageIcon imgMenu = new ImageIcon(
			"png\\SizedGif.gif");

	JPanel MenuC;
	JPanel plante1;
	ImageIcon Pippo = new ImageIcon(
			"font\\SmallShelf.jpeg");
	JPanel plante2;
	JPanel plante3;
	JPanel plante4;
	JButton Retour;
	ImageIcon imgChoix = new ImageIcon("MenuChoix.png");

	JButton Pippoaetus;
	ImageIcon pippoImg = new ImageIcon("png\\plante_1_4.png");
	JButton Calathea;
	ImageIcon calatheaImg = new ImageIcon("png\\plante_2_4.png");
	JButton Dionee;
	ImageIcon dioneeImg = new ImageIcon("png\\plante_3_4.png");
	JButton Desmodium;
	ImageIcon desmoImg = new ImageIcon("png\\plante_4_4.png");


	JPanel Valid;
	JButton validerPlante;
	JButton RetourC;
	JLabel imgPlante;
	JTextArea stats;
	JButton chNom;
	JPanel canvasSaison;

	JButton s1,s2,s3,s4;

	JTextField nomPlante;
	int numPlante = 0;
	int numSaison = 0;


	Plante laPlante;

	public InterfaceFenetre(String nom, int width, int height) {

		super(nom);
		setSize(width, height);
		setLayout(null);
		setBackground(Color.GRAY);
		setLocationRelativeTo(null);
		setResizable(false);

		// font des textes du jeu
		try {
			// create the font to use. Specify the size!
			MochiyPopOne = Font.createFont(Font.TRUETYPE_FONT, new File(
					"font\\MochiyPopOne-Regular.ttf"))
					.deriveFont(24f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(MochiyPopOne);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		titre1 = MochiyPopOne.deriveFont(MochiyPopOne.getSize() * 2.7F);
		titre2 = MochiyPopOne.deriveFont(MochiyPopOne.getSize() * 2F);
		petitMochi = MochiyPopOne.deriveFont(MochiyPopOne.getSize() * 0.8F);

		// Couleur des boutons
		col = new Color(223, 213, 209);
		// Couleur transparente
		Color transparent = new Color(0, 0, 0, 1);

		// 1er conteneur : Ecran d'accueil *********************************************
		MenuP = new JPanel();
		MenuP.setSize(width, height);
		MenuP.setLayout(null);
		this.add(MenuP);
		// Dimension SizeMenu = MenuP.getSize();

		// Titre du jeu (Ce serait mieux de le mettre dans un conteneur les 2 Label mais
		// pr l'instant flemme)
		JLabel TitreJeu1 = new JLabel("WAKAGITCHI");
		TitreJeu1.setFont(titre1);
		TitreJeu1.setBounds(width / 20, height / 5, w, h/8);
		MenuP.add(TitreJeu1);
		JLabel TitreJeu2 = new JLabel("わかぎち");
		TitreJeu2.setFont(titre2);
		TitreJeu2.setBounds(width / 2, height / (7 / 2), w/2, h/8);
		MenuP.add(TitreJeu2);

		// Bouton nouvelle partie
		nvlP = new JButton("Nouvelle Partie");
		nvlP.setBackground(col);
		nvlP.setFont(MochiyPopOne);
		nvlP.setBounds(w/6, h*5/10, h/2, h/11);
		nvlP.addActionListener(this);
		nvlP.setBorder(null);
		nvlP.setFocusPainted(false);
		MenuP.add(nvlP);

		// Bouton Continuer
		cont = new JButton("Continuer");
		cont.setBounds(w/6, h*6/10, h/2, h/11);
		cont.addActionListener(this);
		cont.setFont(MochiyPopOne);
		cont.setBackground(col);
		cont.setBorder(null);
		cont.setFocusPainted(false);
		MenuP.add(cont);

		// Bouton Quitter
		quit = new JButton("Quitter le jeu");
		quit.setBounds(w/6, h*7/10, h/2, h/11);
		quit.addActionListener(this);
		quit.setFont(MochiyPopOne);
		quit.setBackground(col);
		quit.setBorder(null);
		quit.setFocusPainted(false);
		MenuP.add(quit);

		// image de fond
		/*
		 * Image temp = imgMenu.getImage(); // transform it
		 * Image newimg = temp.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
		 * // scale it the smooth way
		 * imgMenu = new ImageIcon(newimg); // transform it back
		 */
		JLabel backP = new JLabel(imgMenu);
		backP.setLayout(null);
		backP.setBounds(0, -35, width, height);
		MenuP.add(backP);

		// 2e conteneur : Menu du choix de la plante *********************************

		MenuC = new JPanel();
		MenuC.setSize(width, height);
		MenuC.setLayout(null);
		this.add(MenuC);

		JLabel CH = new JLabel("Choisissez votre plante");
		CH.setLayout(null);
		CH.setFont(MochiyPopOne);
		CH.setBounds(w*3/100, h/30, w*2/3, h/25);
		MenuC.add(CH);

		// Bouton de retour au menu Principal **************************
		Retour = new JButton("Retour");
		Retour.setFocusPainted(false);
		Retour.setBackground(transparent);
		Retour.setContentAreaFilled(false);
		Retour.setBorder(null);
		Retour.addActionListener(this);
		Retour.setBounds(w*80/100, 2, 50, 20);
		MenuC.add(Retour);

		// Pippoaetus **************************
		plante1 = new JPanel();
		plante1.setBounds(w/9, h*2/10, w*2/3, h/8);
		plante1.setBackground(transparent);
		plante1.setLayout(null);
		
		Pippoaetus = new JButton(pippoImg); // image de la plante (bouton)
		Pippoaetus.setBounds(0, 0, w/3, h/8);
		// Pippoaetus.setBorder(null);
		Pippoaetus.setFocusPainted(false);
		Pippoaetus.addActionListener(this);
		plante1.add(Pippoaetus);
		JLabel nomPip = new JLabel("Pippoaetus");
		nomPip.setFont(MochiyPopOne);
		nomPip.setBounds((w*2/3)*6/10, 0, w/3, h/9); // nom de la plante
		plante1.add(nomPip);
		MenuC.add(plante1);

		// Calathea **************************
		plante2 = new JPanel();
		plante2.setBounds(w/8, h*41/100, w*2/3, h/8);
		plante2.setBackground(transparent);
		plante2.setLayout(null);
		
		Calathea = new JButton(calatheaImg); // image de la plante (bouton)
		Calathea.setBounds((w*2/3)*5/10, 0, w/3, h/8);
		// Calathea.setBorder(null);
		Calathea.setFocusPainted(false);
		Calathea.addActionListener(this);
		plante2.add(Calathea);
		JLabel nomCal = new JLabel("Calathea");
		nomCal.setFont(MochiyPopOne);
		nomCal.setBounds(w/15, 0, w/3, h/8); // nom de la plante
		plante2.add(nomCal);
		MenuC.add(plante2);

		// Dionée **************************
		plante3 = new JPanel();
		plante3.setBounds(w/9, h*63/100, w*2/3, h/8);
		plante3.setBackground(transparent);
		plante3.setLayout(null);
		
		Dionee = new JButton(dioneeImg); // image de la plante (bouton)
		Dionee.setBounds(0, 0, w/3, h/8);
		// Dionee.setBorder(null);
		Dionee.setFocusPainted(false);
		Dionee.addActionListener(this);
		plante3.add(Dionee);
		JLabel nomDio = new JLabel("Dionee");
		nomDio.setFont(MochiyPopOne);
		nomDio.setBounds((w*2/3)*6/10, 0, w/3, h/9); // nom de la plante
		plante3.add(nomDio);
		MenuC.add(plante3);

		// Desmodium gyrans **************************
		plante4 = new JPanel();
		plante4.setBounds(w/50, h*83/100, w*5/6, h/8);
		plante4.setBackground(transparent);
		plante4.setLayout(null);
		Desmodium = new JButton(desmoImg); // image de la plante (bouton)
		Desmodium.setBounds((w*2/3)*75/100, 0, w/3, h/8);
		// Desmodium.setBorder(null);
		Desmodium.setFocusPainted(false);
		Desmodium.addActionListener(this);
		plante4.add(Desmodium);
		JLabel nomDes = new JLabel("Desmodium gyrans");
		nomDes.setFont(MochiyPopOne);
		nomDes.setBounds(20, 0, w/2, h/8); // nom de la plante
		plante4.add(nomDes);
		MenuC.add(plante4);

		// Background Menu choix **************************
		JLabel backC = new JLabel(imgChoix);
		backC.setLayout(null);
		backC.setBounds(-15, -15, width, height);
		MenuC.add(backC);

		MenuC.setVisible(false);

		// Panel de validation de la plante
		Valid = new JPanel();
		Valid.setSize(width, height);
		Valid.setLayout(null);
		this.add(Valid);

		RetourC = new JButton("Retour"); // Retour au menu choix
		RetourC.setFocusPainted(false);
		RetourC.setBackground(transparent);
		RetourC.setContentAreaFilled(false);
		RetourC.setBorder(null);
		RetourC.addActionListener(this);
		RetourC.setBounds(w*8/10, h/50,width/4, h/20);
		Valid.add(RetourC);

		imgPlante = new JLabel(dioneeImg); // le dionee Img je l'ai juste reprise de l'imageIcon créé à la ligne 220
		imgPlante.setBounds(w/5, h/8, w/2, h/4);
		Valid.add(imgPlante);

		nomPlante = new JTextField(20); // pour rentrer le nom de sa plante
		nomPlante.setFont(petitMochi);
		nomPlante.setBounds(w/7, h/2, w/3, h/20);
		nomPlante.addActionListener(this);
		Valid.add(nomPlante);

		canvasSaison = new JPanel();
        canvasSaison.setBounds(w/2,h*37/100,width/3, h/10);
        Valid.add(canvasSaison);
        JPanel choixSaison = new JPanel();
        choixSaison.setLayout(new GridLayout(2,2));
        choixSaison.add(s1 = new JButton("Printemps"));
        choixSaison.add(s2 = new JButton("Ete"));
        choixSaison.add(s3 = new JButton("Automne"));
        choixSaison.add(s4 = new JButton("Hiver"));
        choixSaison.setPreferredSize(new Dimension(w/3, h/10));
        canvasSaison.add(choixSaison);
        s1.addActionListener(this);
        s2.addActionListener(this);
        s3.addActionListener(this);
        s4.addActionListener(this);

		stats = new JTextArea();
		stats.setFont(petitMochi);
		stats.setBounds(w/10, h*2/3, w*2/3, w/3);
		stats.setBackground(transparent);
		stats.setEnabled(false);
		Valid.add(stats);

		chNom = new JButton("Valider Nom");
		chNom.setFont(petitMochi);
		chNom.setBounds(w/2, h/2, w/3, h/20);
		chNom.addActionListener(this);
		Valid.add(chNom);

		validerPlante = new JButton("Valider choix"); // pour valider tout ses choix et creer la nouvelle partie
		validerPlante.setBounds(width*7/10, height/9, w/4, height/20);
		validerPlante.addActionListener(this);
		validerPlante.setFocusPainted(false);
		Valid.add(validerPlante);

		Valid.setVisible(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// pack();
		// setLocationByPlatform(true);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nvlP) {// choix de commencer une nouvelle partie -> on écrase l'ancienne sauvegarde
			MenuP.setVisible(false);
			MenuC.setVisible(true);
			repaint();
			// revalidate();

		} else if (e.getSource() == cont) { // choix de continuer on prend le fichier sauvegardé (verifier si le joueur
											// a
											// déjà un fichier data)
			dispose();
			// ouvrir/lire le fichier data
			loadData();
		} else if (e.getSource() == Retour) { // retourner au menu principal
			MenuC.setVisible(false);
			MenuP.setVisible(true);
		} else if (e.getSource() == RetourC) { // retourner au menu choix
			Valid.setVisible(false);
			MenuC.setVisible(true);

		}
		if(e.getSource()==quit){
			dispose();
		}

		// on regarde sur quel plante il a cliqué
		numPlante = (e.getSource() == Pippoaetus) ? 1
				: (e.getSource() == Calathea) ? 2
						: (e.getSource() == Dionee) ? 3 : (e.getSource() == Desmodium) ? 4 : numPlante;
		if (numPlante != 0) {
			MenuC.setVisible(false);
			Valid.setVisible(true);
			switch (numPlante) { // change image et nom plante en fonction du choix
				case 1:
					nomPlante.setText("Pippoaetus");
					imgPlante.setIcon(pippoImg);// on set up la plante à afficher pour montrer la forme finale au choix de la plante
					break;
				case 2:
					nomPlante.setText("Calathea");
					imgPlante.setIcon(calatheaImg);
					break;
				case 3:
					nomPlante.setText("Dionée");
					imgPlante.setIcon(dioneeImg);
					break;
				case 4:
					nomPlante.setText("Desmodium gyrans");
					imgPlante.setIcon(desmoImg);
					break;
			}
			laPlante = creePlante(numPlante - 1);
			stats.setText(laPlante.caracteristiques());
			repaint();
		}
		if (e.getSource() == chNom) {
			laPlante.name = nomPlante.getText();

			stats.setText(laPlante.caracteristiques());
			repaint();
			Valid.validate(); // refresh la page
		}
		numSaison = (e.getSource() == s1) ? 1
				: (e.getSource() == s2) ? 2
						: (e.getSource() == s3) ? 3 : (e.getSource() == s4) ? 4 : numSaison;
		if (e.getSource() == validerPlante) {
			dispose(); // fermer la fenetre
			laPlante = creePlante(numPlante-1); // on recrée la plante pour ajouter la saison
			
			Jeu gameOn = new Jeu("game loaded", w, h,laPlante, laPlante.lieu);
			// creer un nouveau fichier de sauvegarde et ecraser le fichier d'avant
			try{
				PrintWriter writer = new PrintWriter("player_data.txt"); 
				writer.print("");
				writer.close();
			}
			catch(Exception ex){
				System.out.println("Erreur dans la création du nouveau fichier!");
			}
		}

	}

	// ---_ Lancement du jeu dans cette classe _---//
	public static void main(String[] args) {

		InterfaceFenetre fen = new InterfaceFenetre("Plante Simulator", w, h);

	}

	public Plante creePlante(int numP) {
		if(numSaison==0){
			numSaison=1; //Num saison est fixé à 1 s'il ne sélectionne pas la saison
		}
		Lieu lieuChoisi = new Lieu(infosLieu[0], numSaison); // on commence par le salon
		Plante laPlante = new Plante(100, nomPlante.getText(), lieuChoisi, infosPlante[numP]);
		System.out.println(numSaison);

		return laPlante;

		// on crée un fichier texte et on sauvegarde dedans
	}

	public void loadData() {

		/* -- On récupère le fichier texte de notre sauvegarde --
		   ---> en stockant dans différentes variables nos données 
		   ---> puis en remplacant ses nouvelles variables à la "recréation" de notre plante

		*/
		try {
			BufferedReader br = new BufferedReader(new FileReader("player_data.txt"));
			int typeSaved = Integer.parseInt(br.readLine());
			String nomSaved = br.readLine();
			int pvSaved = Integer.parseInt(br.readLine());
			int numLieuSaved = Integer.parseInt(br.readLine()); // numero de la piece
			int lvlSaved = Integer.parseInt(br.readLine());
			int expSaved = Integer.parseInt(br.readLine());
			int nxtLvlInSaved = Integer.parseInt(br.readLine());
			int masseSaved = Integer.parseInt(br.readLine());
			int humSaved = Integer.parseInt(br.readLine());
			int ageSaved = Integer.parseInt(br.readLine());
			int tempSaved = Integer.parseInt(br.readLine());
			int saisonSaved = Integer.parseInt(br.readLine());
			Lieu lieuSaved = new Lieu(infosLieu[numLieuSaved - 1], 1);
			Plante pltSaved = new Plante(pvSaved, nomSaved, lieuSaved, infosPlante[typeSaved - 1]);
			pltSaved.level = lvlSaved;
			pltSaved.expTotal = expSaved;
			pltSaved.nextLevelIn = nxtLvlInSaved;
			pltSaved.masse = masseSaved;
			pltSaved.humidite = humSaved;
			lieuSaved.temperature = tempSaved;
			lieuSaved.saison = saisonSaved;
			pltSaved.miseAJourLieu(lieuSaved);
			pltSaved.ageEnJour = ageSaved;
			

			Jeu gameOn = new Jeu("Plante Simulator - game loaded", w, h,pltSaved,lieuSaved); // on lance le jeu
			br.close();

		} catch (Exception e) {
			System.out.println("Vous ne possédez pas de sauvegarde !");
			dispose();
			InterfaceFenetre fen = new InterfaceFenetre("Plante Simulator", w, h); // on remet au menu
		}

	}


}
