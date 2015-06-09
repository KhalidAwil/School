// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 * 
 * @authors Christopher Sluyter & Khalid Awil
 */
public class Client extends AbstractClient
{
  //Instance variables **********************************************
  
  GUI window;
  
  private int cash; // Initializes the Client's cash amount.
  
  private String userName; // Initializes the Client's user name.
  
  //Accessor method for retrieving the user name.
  public String getUserName(){
  return this.userName;
  }
  

  
  //Set cash value.
  public void setCash(int x){
  this.cash = cash;
  }
  //Get cash value.
  public int getCash(){
  return this.cash;
  }

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public Client(String host, int port, int cash ,String userName,GUI window) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.window = window;
    this.cash = cash;
    this.userName = userName;
    openConnection();
    
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  
  public void handleMessageFromServer(Object msg) {
    
    if(msg instanceof Integer) { //If the message is a number, treat as a card
      Integer intMsg = new Integer(msg.toString());
      
      if(intMsg<100) { addCard(intMsg); }
      else if(intMsg>=100 && intMsg<200) { addDealerCard(intMsg-100); }
      else if(intMsg>=200 && intMsg<300) { playerScore(intMsg-200); }
      else { dealerScore(intMsg-300); }
    }
    
    else if(msg instanceof String) { //If the message is a String...
     
      if(msg == "bet") { //...and it's "bet" then request a bet from the player
        makeBet(); 
      }
      
      else if(msg == "hit") { //...and it's "hit" then request a hit/stay choice from the player
        makeChoice();
      }
      
      else {
        addMessage(msg.toString()); //...and it's anything else, then post as message
      }
    }
    
    else if(msg instanceof Boolean) {
      Boolean booleanMsg = new Boolean(msg.toString());
      won(booleanMsg);
    }

  }
  
  /*
   * Methods from Client to GUI
   */ 
  
  private void addCard(int card) {
    window.addPlayerCard(card);
  }
  
  private void addDealerCard(int card) {
    window.addDealerCard(card); 
  }
  
  private void makeBet() {
  
    window.makeBet();  
 
  }
  
  private void makeChoice() {
    window.makeChoice();
  }
  
  private void addMessage(String msg) {
    window.display(msg);
  }
  
  private void won(boolean result) {
    window.won();
  }
  
  private void playerScore(int score) {
    window.updatePlayerScore(score);
  }
  
  private void dealerScore(int score) {
    window.updateDealerScore(score);
  }
  
  /*
   *Messages sent to Server for the game
   */
  public void bet(int x) { //Sends the bet
    try{ sendToServer(x); }
    catch(IOException e) {}
  }
  
  public void hit(boolean hit) { //Sends true for hit, false for stay
    try{ sendToServer(hit); }
    catch(IOException e) {}
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of Client class
