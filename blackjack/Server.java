// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 * 
 * @author Christopher Sluyter & Khalid Awil
 * Used for Blackjack game
 */
public class Server extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  private static Thread[] clientThreadList;
  private static Player[] players = new Player[1];
  private static int numOfPlayers;
  
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public Server(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  
  public void handleMessageFromClient (Object msg, ConnectionToClient client) {
    int i = getIndex(client);
    
    
    if(msg instanceof String) {
      ((ConnectionToClient)clientThreadList[i]).setInfo("username",msg.toString());
    }
    else if(msg instanceof Integer) {
      Integer intMsg = new Integer(msg.toString());
      if(intMsg<=0) { int money = -intMsg; ((ConnectionToClient)clientThreadList[i]).setInfo("money",money);
      }
      else {
        playerBet(i, intMsg);
      }
    }
    else if(msg instanceof Boolean) {
     Boolean boolMsg = new Boolean(msg.toString());
     playerChoice(i, boolMsg);
    }
  }
  
  //Gets the client's "number" or index
  private int getIndex(ConnectionToClient client) {
    for (int i=0; i<numOfPlayers; i++) {
      if(client == (ConnectionToClient)clientThreadList[i]) { return(i); }
    }
    return(-1);
  }
  
  
  //Sends a message to a particular client
  public void sendToClient(int client, Object msg) {
    Thread[] clientThreadList = getClientConnections();

    try
    {
      ((ConnectionToClient)clientThreadList[client]).sendToClient(msg);
    }
    catch (Exception ex) {}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
 
    Server server = new Server(port);
    
    try 
    {
      server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    
    /*
     * GAME
     */

    int i = 0;
    while(numOfPlayers<1) { /*System.out.println("test "+i); i++;*/} 
      //Start a new Round
      server.setPlayers();

      Round round = new Round(players);

      for(int j=0; j<numOfPlayers; j++) {
        players[0].associateRound(round);
      }

      round.run();
  }
  
  //Resets the players, associates the clients to players
  private void setPlayers() {

    clientThreadList = getClientConnections();
  
    for(int i=0; i<clientThreadList.length; i++) {
      //Casting an Object as an int by first converting to String then to Integer
      int money = Integer.parseInt( (((ConnectionToClient)clientThreadList[i]).getInfo("money")).toString() );
      players[i] = new Player(this,i,money);
    }
  }
  
  //Check to see if the server should stop listening (since there may be 4 players)
   protected void clientConnected(ConnectionToClient client) {
     numOfPlayers++;
     try { check(); }
     catch(IOException e) {}
   }

   //Check to see if the server can start listening again
   synchronized protected void clientDisconnected(ConnectionToClient client) {
     try { check(); }
     catch(IOException e) {}
   }
  
  //If there are 4 clients, stop listening. If less than four, begin listening again
  private void check() throws IOException {
    if(getNumberOfClients()==4) {
     stopListening(); 
    }
    
    if(getNumberOfClients()<4) {
     listen(); 
    }
  }
  
  
  
  /*
   * METHODS THAT SEND MESSAGES TO THE CLIENT
   */ 
  
  //Set's a Connection's money amount
  public void setMoney(int i, int money) {
    ((ConnectionToClient)clientThreadList[i]).setInfo("money",money);
  }
  
  //Sends the player's card
  public void sendCard(int client, int card) {
    Integer msg = new Integer(card);
    sendToClient(client, msg);
  }
  
  //Sends the dealer's card
  public void sendDealerCard(int card) {
    Integer msg = new Integer(card+100);
    for(int i=0; i<numOfPlayers; i++) { sendToClient(i, msg); }
  }
  
  //Sends the player score
  public void sendScore(int client, int score) {
    Integer msg = new Integer(score+200);
    sendToClient(client, msg);
  }
  
  //Sends the dealer score
  public void sendDealerScore(int score) {
    Integer msg = new Integer(score+300);
    for(int i=0; i<numOfPlayers; i++) { sendToClient(i, msg); }
  }
  
  //Tells the player that it's the betting phase
  public void requestBet(int client) {
    sendToClient(client,"bet");
  }
  
  //Tells the client that it's the hit/stay phase
  public void requestChoice(int client) {
   sendToClient(client,"hit"); 
  }
  
  //Sends the client a message to be displayed in the GUI
  public void sendMessage(int client, String msg) {
   sendToClient(client, msg); 
  }
  
  //Tells the client if he/she won
  public void winner(int client, boolean won) {
   Boolean msg = new Boolean(won);
   sendToClient(client, msg);
  }
  
  /*
   * PRIVATE METHODS CALLED AS A RESULT OF MESSAGES FROM CLIENT
   */ 
  
  //Tells the player what the client has bet
  private void playerBet(int i, int bet) {
   players[i].playerBet(bet); 
  }
  
  //Tells the player if the player hit/stayed
  private void playerChoice(int i, boolean choice) {
   players[i].playerChoice(choice);
  }
  
}
