/* SEG 2105
 * 
 * Assignment 7 
 * @authors Khalid Awil(6763004) & Christopher Sluyter(6390470)
 * 
 * GUI
 *
 */ 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

import java.io.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame ; 
import javax.swing.JButton ;
import javax.swing.JLabel;
import javax.swing.JPanel ;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI extends JFrame implements ActionListener {

 /*
  *
  * Components of GUI
  * 
  */

 JLabel card1;
 JLabel card2;
 static JLabel[] dealerCards;
 JLabel card3; 
 JLabel card4;
 static JLabel[] playerCards;
 
 ////////////////////////////////////////////////////bet and choice flags
 boolean bet = false;
 boolean choice = false;

 JTextField playerCash;
 JTextField playerBet;
 JTextField plrScore;
 JTextField dlrScore;
 JTextField userName; 

 private String playerName;

 JButton increaseBet;
 JButton decreaseBet;
 JButton placeBet;
 JButton hit;
 JButton stay; 
  
 // Initializing the GridBagConstraints for the cards.
 GridBagConstraints gbc3 = new GridBagConstraints();
 GridBagConstraints gbc4 = new GridBagConstraints();
// Counter that points to the current card to be added.
 private int cardCount = -1;
 private int cardCountD = -1;
 private int gridx = 0 ;
 private int gridy = 0;
 
 
 JTextField msg1 = new JTextField(15); 
 JTextField msg2 = new JTextField(15); 
 JTextField msg3 = new JTextField(15); 
 JTextField msg4 = new JTextField(15); 
 JTextField msg5 = new JTextField(15); 
 JTextField msg6 = new JTextField(15); 
 
 // Initializing the player's and dealer's panel to hold their cards.
 JPanel player = new JPanel(new GridBagLayout());
 JPanel dealer = new JPanel(new GridBagLayout());
 
 
 JPanel scores;
 JPanel bettingArea;
 JPanel messages;
 JPanel textControlsPane;
 
 
 // Initializing the Message box to hold messages. 
 JTextField[] msgArray = new JTextField[]{msg1,msg2,msg3,msg4,msg5,msg6};

// Counter to point to next message.
 public int msgCount = 0 ;
 

 Client client;

 
 public GUI(){
 
  /*
   * Constructor for GUI
   */

  ///Frame/////////////////////////////////////////////
  super("BlackJack");  

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setBackground(Color.green);
  setLayout(new GridBagLayout());
 // setSize(1000,600);
  setResizable(true);
  GridBagConstraints gbc = new GridBagConstraints();
  GridBagConstraints gbc2 = new GridBagConstraints();

  gbc.insets = new Insets(6,6,6,6);
  gbc2.insets= new Insets(10,10,10,10);
  gbc3.insets = new Insets(2,2,2,2);
  gbc4.insets = new Insets(2,2,2,2);

  //Creating all the necessary panels.
  scores = new JPanel(new GridBagLayout()); 
  dealer = new JPanel(new GridBagLayout()); 
  player = new JPanel(new GridBagLayout());
  bettingArea = new JPanel(new GridBagLayout());
  messages = new JPanel();  
  messages.setLayout(new BoxLayout(messages,BoxLayout.PAGE_AXIS));



  //Adding the panels to the Frame.

  gbc2.fill=GridBagConstraints.BOTH;
  gbc2.weighty=1;
  gbc2.weightx=1;

  gbc2.gridx=2;
  gbc2.gridy=0;
  add(scores,gbc2);

  gbc2.gridx=2;
  gbc2.gridy=1;
  add(bettingArea,gbc2);

  gbc2.weighty=1;
  gbc2.weightx=1;
  gbc2.gridx=1;
  gbc2.gridy=0;
  add(dealer,gbc2);

  gbc2.weighty=1;
  gbc2.weightx=1;
  gbc2.gridx=1;
  gbc2.gridy=1;
  add(player,gbc2);

  gbc2.gridx=0;
  gbc2.gridy=0;
  gbc2.gridheight=2;
  add(messages,gbc2);



  // Setting borders for each Panel.
  scores.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
  bettingArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
  messages.setBorder(BorderFactory.createTitledBorder("Message Box"));
  dealer.setBorder(BorderFactory.createTitledBorder("Dealer's Hand"));
  player.setBorder(BorderFactory.createTitledBorder("Player's Hand"));

  ////////End of Frame/////////////////////////////////////////////////

  //score's Interface //////////////////////////////
  /////////////////////////////////////////////////

  hit = new JButton("Hit");
  stay = new JButton("Stay");
  plrScore = new JTextField();
  dlrScore = new JTextField();
  plrScore.setEditable(false);
  dlrScore.setEditable(false);
 
  //Adding ActionListeners 

  hit.addActionListener(this);
  stay.addActionListener(this);

  // Setting Different Border Colors for the Player and Dealer scores
  plrScore.setBorder(BorderFactory.createTitledBorder("Player's Score"));
  dlrScore.setBorder(BorderFactory.createTitledBorder("Dealers's Score"));

  // Setting up the GridBagConstraints.

  gbc.fill=GridBagConstraints.BOTH;
  gbc.weightx=1;
  gbc.weighty=1;
  gbc.gridx=0;
  gbc.gridy=0;
  scores.add(plrScore,gbc);

  gbc.gridx=1;
  gbc.gridy=0;
  scores.add(dlrScore,gbc);

  gbc.gridx=0;
  gbc.gridy=1;
  scores.add(hit,gbc);

  gbc.gridx=1;
  gbc.gridy=1;
  scores.add(stay,gbc); 


  //bettingArea's Interface //////////////////////////
  ///////////////////////////////////////////////////
  increaseBet = new JButton("Increase");
  userName = new JTextField(this.retrieveName()); 
  decreaseBet = new JButton("Decrease");
  placeBet = new JButton("Place Bet");
  playerBet = new JTextField("$0",6);
  playerCash = new JTextField("$1500",6);
  playerCash.setBorder(BorderFactory.createTitledBorder("Player Cash"));
  playerBet.setBorder(BorderFactory.createTitledBorder("Player Bet"));
  playerCash.setEditable(false);
  userName.setEditable(false); 
  playerBet.setEditable(false);
  playerCash.setHorizontalAlignment(JTextField.CENTER);
  playerBet.setHorizontalAlignment(JTextField.CENTER);



  //Adding ActionListeners 
  increaseBet.addActionListener(this);
  decreaseBet.addActionListener(this);
  placeBet.addActionListener(this);
 
  //Setting up the GridBagConstraints.
  gbc.gridx=0;
  gbc.gridy=0;
  bettingArea.add(increaseBet,gbc);

  gbc.gridx=1;
  gbc.gridy=0;
  bettingArea.add(playerBet,gbc);

  gbc.gridx=2;
  gbc.gridy=0;
  bettingArea.add(placeBet,gbc);

  gbc.gridx=0;
  gbc.gridy=1;
  bettingArea.add(decreaseBet,gbc);

  gbc.gridx=1;
  gbc.gridy=1;
  bettingArea.add(playerCash,gbc);
  
  gbc.gridx = 2;
  gbc.gridy = 0; 
  bettingArea.add(userName,gbc);

  ////messages Interface///////////////////// 

  
  msg1.setEditable(false);
  msg2.setEditable(false);
  msg3.setEditable(false);
  msg4.setEditable(false);
  msg5.setEditable(false);
  msg6.setEditable(false);
  
  messages.add(msg1); 
  messages.add(Box.createRigidArea( new Dimension(0,3)));
  messages.add(msg2);
  messages.add(Box.createRigidArea( new Dimension(0,3)));
  messages.add(msg3);
  messages.add(Box.createRigidArea( new Dimension(0,3)));
  messages.add(msg4);
  messages.add(Box.createRigidArea( new Dimension(0,3)));
  messages.add(msg5);
  messages.add(Box.createRigidArea( new Dimension(0,3)));
  messages.add(msg6);


  ////dealer's Interface/////////////////////////
  //////////////////////////////////////////////
  card1 = new JLabel();
  card2 = new JLabel();
  
  dealerCards = new JLabel[2];
  dealerCards[0] = card1;
  dealerCards[1] = card2;


  gbc4.fill=GridBagConstraints.BOTH;
  gbc.gridwidth = 5;
  gbc.gridheight = 5;
  gbc4.weightx=1;
  gbc4.weighty=1;

  gbc4.gridx=0;
  gbc4.gridy=0;
  dealer.add(card1,gbc4);

  gbc4.gridx=1;
  gbc4.gridy=0;
  dealer.add(card2,gbc4);

  //Visibility of Panels & JFrame.

  scores.setVisible(true);
  bettingArea.setVisible(true);
  messages.setVisible(true);
  dealer.setVisible(true);

  ////Player's Interface/////////////////////////
  //////////////////////////////////////////////

  card3 = new JLabel();
  card4 = new JLabel();
  playerCards = new JLabel[2];
  playerCards[0] = card3;
  playerCards[1] = card4;


  gbc3.fill=GridBagConstraints.BOTH;
  gbc.gridwidth = 5;
  gbc.gridheight = 5;
  gbc3.weightx=2;
  gbc3.weighty=1;

  gbc3.gridx=0;
  gbc3.gridy=0;
  player.add(card3,gbc3);

  gbc3.gridx=1;
  gbc3.gridy=0;
  player.add(card4,gbc3);

  //Visibility of Panels & JFrame.

  scores.setVisible(true);
  bettingArea.setVisible(true);
  messages.setVisible(true);
  dealer.setVisible(true);
  player.setVisible(true); 
  
 
 }

 //GUI Methods/////////////////////////////////////////
 /////////////////////////////////////////////////////
 /*
  ** Returns an ImageIcon, or null if the path was invalid.
  * 
  * @author Oracle.
  * 
  */

 protected ImageIcon createImageIcon(String path,
   String description) {
  java.net.URL imgURL = getClass().getResource(path);
  if (imgURL != null) {
   return new ImageIcon(imgURL, description);
  } else {
   System.err.println("Couldn't find file: " + path);
   return null;
  }
 }

 // Adds a card onto the hand of the Player.
//////////////////////////////////////////
  public void addPlayerCard(int card){
  
  Integer x = new Integer(card);
  ImageIcon icon = createImageIcon("Cards/"+x.toString()+".png","Card");

  addPlayerCard(icon);
  
 }
 
  // Adds a card onto the hand of the Dealer. 
 ////////////////////////////////////////////
  public void addDealerCard(int card){
  
  Integer x = new Integer(card);
  ImageIcon icon = createImageIcon("Cards/"+x.toString()+".png","Card");

  addDealerCard(icon);
  
 }
  


 // Makes a bet and sends it to the server.
 ///////////////////////////////////////////
 public void makeBet(){ 

  String x = playerBet.getText(); // Grabs the text in the JTextField..
  String y = x.substring(1);
  int bet = Integer.parseInt(y); //..and parses into an integer.
  String a = playerCash.getText();
  String b = a.substring(1);
  int cash = Integer.parseInt(b);
  Integer z = new Integer(cash-bet);

  if( bet>cash || bet <=0 /*|| client.getCash()-bet>=0*/){//--To be changed . If the bet input is invalid , send out a message 
   System.out.print("Invalid bet amount ("+ (bet-20)+"), please bet again."); 
  }
   if ( bet<cash && bet > 0){ // If it is a valid bet , send bet to the server.
     
     playerCash.setText("$"+z.toString());
     client.setCash(client.getCash()-bet); 
     client.bet(bet);
    
 
  }
 }
 
 //Makes a choice and sends it to the server.
 ////////////////////////////////////////////

 public void bet() {
   bet = true;
 }
 
 public void makeChoice(){ // If player chooses to hit (or not) , it will notify the server.  
   choice = true;
 }

 //Displays an action on the Message board.
 ///////////////////////////////////////////
 public void display(String message){

   msgArray[msgCount%msgArray.length].setText(message);
   msgCount++;
  
  }
 
//Adding a card to a hand , given the icon.
////////////////////////////////////////////
 private void addPlayerCard(ImageIcon icon){ 
   
   if(cardCount+1 < playerCards.length){
    cardCount++; // Set pointer to the current card.
    playerCards[cardCount].setIcon(icon); // Set icon of current card.
    playerCards[cardCount].setHorizontalAlignment(SwingConstants.CENTER);
    playerCards[cardCount].setVerticalAlignment(SwingConstants.CENTER); 
    System.out.println(cardCount); // -- For trouble-shooting
   }
   
   else if(cardCount+1 == playerCards.length){ // If next card is equal to the length of the array...
 
   playerCards=resizePlayer(playerCards);
   System.out.println(playerCards.length);
   System.out.println(cardCount);
   playerCards[cardCount] = new JLabel(icon); 
   if(cardCount == 3){
   gbc3.gridx = 0;
   gbc3.gridy = 1;
   player.add(playerCards[cardCount],gbc3);
   }
   else{
   gbc3.gridx = cardCount;
   gbc3.gridy = 0;
   player.add(playerCards[cardCount],gbc3);
   }
 }
 }
 
 
 // Resize method which increases the cardholder's hand by 1.
 ////////////////////////////////////////////////////////////
 public JLabel[] resizePlayer( JLabel[] cards){
 
  JLabel[] cards2 = new JLabel[cardCount+3];
  for( int i = 0 ; i < cards.length ; i++){
  cards2[i] = cards[i]; 
  }
  cardCount++;
  for(int i = cards.length ; i < cards2.length ; i++){
  cards2[i] = new JLabel();
  }
  cards = cards2; 
  return cards;
 }
 
//Adding a card to a hand , given the icon.
////////////////////////////////////////////
 
 private void addDealerCard(ImageIcon icon){ 
   if(cardCountD+1 < dealerCards.length){
    cardCountD++; // Set pointer to the current card.
    dealerCards[cardCountD].setIcon(icon); // Set icon of current card.
    dealerCards[cardCountD].setHorizontalAlignment(SwingConstants.CENTER);
    dealerCards[cardCountD].setVerticalAlignment(SwingConstants.CENTER); 
   }
   else if(cardCountD+1 == dealerCards.length){ // If next card is equal to the length of the array...

   dealerCards = resizeDealer(dealerCards);
   System.out.println(dealerCards.length);
   dealerCards[cardCountD] = new JLabel(icon); 
   gbc4.gridx = cardCountD;
   gbc4.gridy = 0;
   player.add(dealerCards[cardCountD],gbc4);
   }
   else{
   }

 }
// Resize method which increases the cardholder's hand by 1.
 ////////////////////////////////////////////////////////////
  public JLabel[] resizeDealer( JLabel[] cards){
 
  JLabel[] cards2 = new JLabel[cardCountD+3];
  for( int i = 0 ; i < cards.length ; i++){
  cards2[i] = cards[i]; 
  }
  cardCountD++;
  for(int i = cards.length ; i < cards2.length ; i++){
  cards2[i] = new JLabel();
  }
  cards = cards2; 
  return cards;
  }
 //Accesor Method that retrieves player name.
 ///////////////////////////////////////////
 public String getPlayerName(){
 
 return this.playerName;

 }

//Accesor method that sets player name.
///////////////////////////////////////
 public void setPlayerName(){
 this.playerName = retrieveName(); 
 }
 
 //Updates the Player's Score every round.
 ////////////////////////////////////////
  public void updatePlayerScore(int score){
  Integer x = new Integer(score);
  plrScore.setText(x.toString()); 
  plrScore.setHorizontalAlignment(JTextField.CENTER);
   
 }
 
 //Updates the Dealer's Score every round.
 /////////////////////////////////////////
  public void updateDealerScore(int score){
  Integer x = new Integer(score);
  dlrScore.setText(x.toString()); 
  dlrScore.setHorizontalAlignment(JTextField.CENTER);
 }
 
  //Retrieve User Name/////////////////////
 ///////////////////////////////////////// 
 
  public String retrieveName(){
  
  return( this.client.getUserName() );
  }
  
  
 //Displayed when user has won////////////////
 //////////////////////////////////////////////
 
  public void won(){
    display("You have won!!");
  }
  
//Action method to respond to certain button presses.
/////////////////////////////////////////////////////

 public void actionPerformed(ActionEvent e) {

   if(bet) {
   
     // If the button pressed is "Place Bet"...
     if(e.getSource()== placeBet){ 
       makeBet();
       display("Bet");
     }
 
     // If the button pressed is "Increase"(Bet)...
     else if(e.getSource()==increaseBet){
       
       String x = playerBet.getText();// Convert the text into an integer.
       String y = x.substring(1); 
       int bet = Integer.parseInt(y);
       bet = bet + 20; // Increment the bet.
       Integer z = new Integer(bet);
       playerBet.setText("$"+z.toString()); //Set the bet on the Bet label.
 
     }
 
// If the button pressed is "Decrease"(Bet)...
     else if(e.getSource()==decreaseBet){
   
       String x = playerBet.getText();// Convert the text into an integer.
       String y = x.substring(1);
       int bet = Integer.parseInt(y);
       if((bet-20)>0){
         bet = bet - 20;// Decrement the bet.
         Integer z = new Integer(bet);
         playerBet.setText("$"+z.toString()); //Set the bet on the Bet label.
       }   
     }
     
     bet = false;
   }
   
   if(choice) {
     //If the button clicked was hit..
     if(e.getSource()==hit){
       client.hit(true);
     }
     //If the button clicked was stay..
     else if(e.getSource()==stay){
       client.hit(false);
     }
     choice = false;
   }
 }
  
 // Main Method
 public static void main ( String args[]) {
   
   GUI game = new GUI();
   
   try { game.client = new Client(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]),args[3],game); }
   catch(IOException e) {}

  game.setVisible(true);
  game.addPlayerCard(13);
  game.addPlayerCard(17);
  game.addPlayerCard(52);
  game.addPlayerCard(51);
  game.pack();


}
} 


