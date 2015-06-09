/*
 * Rounds are instantiated by Server, used to run the game
 * 
 * @authors Christopher Sluyter & Khalid Awil
 */ 

import java.util.Random;
import java.util.concurrent.*;

public class Round {
 
  /*
   * ATTRIBUTES
   * Made private since there is no need for them to be accessible by Server
   */ 
  
  //Array of players passed on by server
  private Player[] players;
  //A Special Player instance ued for the dealer
  private Player dealer = new Player();
  //Number of players calculated in the Constructor
  private int numOfPlayers;
  //Deck (Array of cards)
  private int[] cards = new int[52];
  
  //Indicates what position in the array the next card is in (for drawing cards)
  private int cardIndex = 0;
  
  //Tells the round it's OK to continue (used when players must make decisions)  
  private boolean[] bet = new boolean[numOfPlayers];  
  private boolean[] stay = new boolean[numOfPlayers];
  
  
  /*
   * CONSTRUCTOR
   * The server sends the players and an instance of itself for communication
   */ 
  public Round(Player[] players) {
    this.players = players;
    this.numOfPlayers = players.length;
    for(int i=0; i<numOfPlayers; i++) { bet[i] = false; stay[i] = false; }
  }
  
  
  /* 
   * "MAIN" PUBLIC METHOD
   * Run() runs the whole Round, the operation is subdivided into the private methods
   */ 
  public void run() {
    suffle();
    deal();
    bet();
    proceed(0);
    makeChoice();
    proceed(1);
    dealer();
    calcWinner();
  }
  
  
  /*
   * PRIVATE METHODS (Components of RUN)
   */
    
  //Adds the 52 cards and puts them in a random order
  private void suffle() {
    
    //Creates the deck of cards
    for(int i=0; i<52; i++) {
      cards[i] = i+1;
    }

    Random random = new Random(); //Random generator
 
    //This swaps each card with another random one
    for (int i=0; i<cards.length; i++) {
      
      int randomIndex = random.nextInt(cards.length);
      
      //Swapping the cards at indices i & randomIndex
      /*This is the equivalent of swapping using a temp int
       */
      cards[i] += cards[randomIndex];
      cards[randomIndex] = cards[i] - cards[randomIndex];
      cards[i] -= cards[randomIndex];
    }
  }
  
  //Gives each player 2 cards
  private void deal() {
    
    //For each players, give the next two cards in the deck, and increment the card index
    for(int i=0; i<numOfPlayers; i++) {
      players[i].giveCard(cards[cardIndex]);
      players[i].giveCard(cards[cardIndex+1]);
      cardIndex += 2;
    }
    //Give the dealer a card, only 1 since the players never know more than 1 dealer card
    dealer.giveCard(cards[cardIndex]);
    cardIndex++;
  }
  
  //Tells the player that it's time to bet
  private void bet() {
    //For each player, call bet(), which requests a bet amount through the server/client
    for(int i=0; i<numOfPlayers; i++) {
     players[i].bet();
    }
  }
  
  //Tells the players that it's time to make a choice (HIT or STAY)
  private void makeChoice() {
    //For each player, call makeChoice(), which requests a hit/stay choice through the server/client
    for(int i=0; i<numOfPlayers; i++) {
     players[i].makeChoice();
    }
  }
  
  //This method plays the dealer; continues drawing cards until the score is 17 or more
  private void dealer() {
    while(dealer.getScore() <= 17 || dealer.bust()) {
     dealer.giveCard(cards[cardIndex]); 
     cardIndex++;
     //Pauses the dealer for a couple seconds so the players can see the dealer make moves one by one
     pause();
    }
  }
  
  //Pauses the game for 2 seconds
  private void pause() {
    try { TimeUnit.SECONDS.sleep(2); }
    catch(InterruptedException e) {}
  }
  
  //For each player, this method compares his/her score with the dealer (and considers busts), then tells the player whether or not they won
  private void calcWinner() {
    for(int i=0; i<numOfPlayers; i++) {
      //If the player busts,or the dealer didn't bust and got an equal or higher score, the player lost
      if(players[i].bust() || (dealer.getScore() >= players[i].getScore() && !dealer.bust()) ) { players[i].finish(false); }
      //Otherwise (the dealer busted or they got a better score), the player wins
      else { players[i].finish(true); }
    }
  }
  
  //This allows the game to continue at certain points, to make sure the Round only goes once all players have bet/stayed
  private void proceed(int x) {
    
    //The first proceed causes the Round to hold until all players have bet
    if(x==0) {
      for(int i=0; i==0;) {
        
        //If one player hasn't bet, then re-enter the loop. If "everyoneBet", then exit the loop, so the next method in run() is called
        boolean everyoneBet = true;
        for(int j=0; j<numOfPlayers; j++) {
          if(bet[i] = false) { everyoneBet = false; }
        }
          if(everyoneBet) {
          i=1;
        }
      }
    }
    
    //If one player hasn't stayed, then re-enter the loop. If "everyoneStay", then exit the loop, so the next method in run() is called
    else if(x==1) {
      for(int i=0; i==0;) {
        
        boolean everyoneStay = true;
        for(int j=0; j<numOfPlayers; j++) {
          if(stay[i] = false) { everyoneStay = false; }
        }
          if(everyoneStay) {
          i=1;
        }
      }
    }
  }
  
  
  /*
   * OTHER PUBLIC METHODS
   */ 
  
  //This tells the Round that a player has finished betting
  public void playerBet(int player) {
    bet[player] = true;
  }
  
  //This tells the Round that a player has stayed
  public void playerStay(int player) {
    stay[player] = true;
  }
  
  //This method simply returns the next card in the deck and increments card index
  public int drawCard() {
    cardIndex++;
    return(cards[cardIndex-1]);
  }
  
}