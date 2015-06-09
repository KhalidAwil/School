/*
 * Players are created by Server each Round
 * A player is instantiated and associated with a client
 * 
 * @authors Christopher Sluyter & Khalid Awil
 */ 

public class Player {
 
  /*
   * ATTRIBUTES
   */ 
  
  //Used to communicate with the Server
  private Server server;
  //Used to communicate with the Round
  private Round round;
  //The amount of money a player has
  private int money;
  //The amount bet, used from the betting phase onward
  private int bet;
  //The player's score, calculated based on the value of cards
  private int score = 0;
  //This is a number representing which client the player is associated with; client 1, 2, 3 or 4?
  private int client;
  /*The number of Aces a player has in hand
   * Aces have either a value of 11 or 1. The assumed value is 11, and if that leads to a bust, then it's 1.
   * The numOfAces variable is used to keep track of how many times the player can reduce his/her score by 10
   */ 
  private int numOfAces = 0;
  //Flag used to differentiate the dealer and normal players
  private boolean isDealer;
  
  /*
   * CONTRUCTORS
   * The first is called by Server, so those are Players
   * The second is called by Round, which is the dealer, so the client/server/money info is not needed
   */ 
  public Player(Server server, int client, int money) {
    this.server = server;
    this.client = client;
    this.money = money;
    isDealer = false;
  }
  
  public Player() {
    isDealer = true;
  }
  
  
  /*
   * PUBLIC METHODS CALLED BY SERVER
   */ 
  
  //The players are created before the Round, so the Players must be sent the instance of the Round
  public void associateRound(Round round) {
   this.round = round; 
  }
  
  //Tells the player what choice the client has made
  public void playerChoice(boolean hit) {
    //If the choice was hit, call hit()
    if(hit) { 
      hit(); 
      //If after hitting, the player busts, send the "Busted" message and stay()
      if(bust()) { sendMsg("Busted"); stay(); }
      else { makeChoice(); }
    }   
    //If the choice was stay, call stay()
    else { stay(); }
  }
  
  //Tells the player how much the client bet
  public void playerBet(int bet) {
    this.bet = bet;
    money -= bet;
    //Set the new amount of money, this is done now, so that if a player disconnects during the round they lose their money
    server.setMoney(client, money);
    //Tells the Round that the player had bet
    round.playerBet(client);
  }

  
  /*
   * PUBLIC METHODS CALLED BY ROUND
   */ 

  //Tells the client it's time to bet
  public void bet() {
    server.requestBet(client);
  }
  
  //Tells the client it's time to choose hit or stay
  public void makeChoice() {
    server.requestChoice(client);
  }
  
  //The Round gives the player a card, for which the value is added to the score and sends the card to the client
  public void giveCard(int card) {
    addScore(calcValue(card));
    
    //Send as a dealer card (so everyone can see) if the player is a dealer
    if(isDealer) {
      server.sendDealerCard(card);
      server.sendDealerScore(score);
    }
    
    //Send as a player card (only to the player) if the player isn't the dealer
    else {
      server.sendCard(client,card);
      server.sendScore(client,score);
    }
  }
  
  //Getter method, returns the player's score
  public int getScore() {
   return(score); 
  }
  
  //This method is used to calculate the value of a card based on it's number representation
  public int calcValue(int card) {
    //The first 4 cards are aces, so the value is 11
    if(card<=4) { numOfAces++; return(11); }
    //Starting from 33 (10s and face cards) the cards have a value of 10
    else if(card>=33) { return(10); }
    //The remaining cards have a specific value using the following function:
    else {
     return((int)(card-1)/4+2); 
    }
  }
  
  //This method checks to see if the player busted
  public boolean bust() {
    //If the score is over 21, the player may have busted
    if(score>21) { 
      
      //They did if they have no aces
      if(numOfAces < 1) {
        return(true);
      }
      //If they do have aces, the value of one ace is turned to 1 instead of 11
      else {
        numOfAces--;
        addScore(-10);
        //Bust is then recalculated based on the new score with the change of value of the ace
        return(bust());
      } 
    }
    return(false);
  }
  
  //Round tells the player that the round is over, and if the player won; info is sent to server
  public void finish(boolean result) {
    server.winner(client,result);
  }
  
  
  /*
   * PRIVATE METHODS
   */ 
  
  //This method deducts the bet amount, and can also add the bet amount if the player wins
  private void addMoney(int amount) {
    money += amount;
    server.setMoney(client,money);
  }
  
  //Tells the Round that this player has stayed, so that when they all stay, the Round can continue
  private void stay() {
    round.playerStay(client);
  }

  //When the Player hits, draw a new card and add the cards value to the player's score
  private void hit() {
    int newCard = round.drawCard();
    giveCard(newCard);
    addScore(calcValue(newCard));
  }
  
  //This method adds a cards value to the score and sends the new score to the client
  private void addScore(int added) {
    score += added;
    server.sendScore(client, score);
  }
  
  //Used to send messages which appear in the GUI
  private void sendMsg(String msg) {
    server.sendMessage(client, msg);
  }
  
}