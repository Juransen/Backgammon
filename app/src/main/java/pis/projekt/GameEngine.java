package pis.projekt;

import java.util.*;

public class GameEngine implements BackgammonInterface {
  private final Base[] board = new Base[28];
  private final int player2 = 2; // white
  private final int player1 = 1; // black
  private final int p1Out = 26;
  private final int p2Out = 27;
  private final int[] diceVal = new int[2];
  private boolean isSelected, clearable, roundStarted, gameIsOver, testMode = false;
  private int availableMoves, playedDice, currentPlayer = 0;
  private int piecePos;

  /*
  |   white base     |                   |                   |     black base    | yard | out |
  | 00 01 02 03 04 05| 06 07 08 09 10 11 | 12 13 14 15 16 17 | 18 19 20 21 22 23 | 24 25|26 27|
  | 2              5 |     3          5  |  5          3     | 5              2  |      |     |
  | p1             p2|     p2         p1 |  p2         p1    | p1             p2 | p1 p2|p1 p2|

  15 weiße Steine (Player 2) läuft vowärst
  15 schwarze Steine (Player 1) läuft rückwärts
  */

  @Override
  public void start(){
    dicesRoll();

    if (diceVal[0] == diceVal[1]) {start();}

    if (diceVal[0] > diceVal[1]) {currentPlayer = 1;}
      else if (diceVal[0] < diceVal[1]) {currentPlayer = 2;}

    fillBoard();
  }

  public void round(){
    if (isGameOver()) {return;}

    if (!roundStarted){
      dicesRoll();
      roundStarted = true;
      availableMoves(diceVal[0], diceVal[1]);
    }

    isClearable();

    if (availableMoves <= 0){
      playedDice = 0;
      if (currentPlayer == player1) {currentPlayer = player2;}
      else {currentPlayer = player1;}
      roundStarted = false;
    }
  }

  @Override
  public boolean isGameOver() {
    int yard1 = board[24].count;
    int yard2 = board[25].count;
    if(yard1 == 15 || yard2 == 15) {gameIsOver = true;}
    return gameIsOver;
  }

  private void isClearable(){
    int counter = 0;
    switch (currentPlayer){

      case (player1) ->{
        for(int i = 0; i < 18; i++) {
          if (board[i].color == player1) {counter += 1;}
        }
        if (counter == 0) {clearable = true;}
      }
      case (player2) ->{
        for(int i = 6; i < 24; i++) {
          if (board[i].color == player2) {counter += 1;}
        }
        if (counter == 0) {clearable = true;}
      }
    }
  }

  @Override
  public void initializeBoard(){
    for (int i = 0; i < board.length; i++) {board[i] = new Base(0,0, false);}
  }

  private void fillBoard() {

    for (int i = 0; i < ((board.length - 4) / 2); i++) {
      switch (i) {
        case 0 -> {
          board[i].color = player1;
          board[i].count = 2;
          board[board.length-5].color = player2;
          board[board.length-5].count = 2;
        }
        case 5 -> {
          board[i].color = player2;
          board[i].count = 5;
          board[(board.length-5)-i].color = player1;
          board[(board.length-5)-i].count = 5;
        }
        case 7 -> {
          board[i].color = player2;
          board[i].count = 3;
          board[(board.length-5)-i].color = player1;
          board[(board.length-5)-i].count = 3;
        }
        case 11 -> {
          board[i].color = player1;
          board[i].count = 5;
          board[(board.length-5)-i].color = player2;
          board[(board.length-5)-i].count = 5;
        }
      }
    }
    for(int i = 24; i < 26; i++) {
      int playercolor = player1;
      if ((i % 2) == 1) {playercolor++;}
      board[i].color = playercolor;
      board[i].out = true;
      board[i+2].color = playercolor;
      board[i+2].out = true;
    }
  }

  private void dicesRoll(){
    if (testMode) {return;}
    else{
      diceVal[0] = (new Random().nextInt(5) + 1);
      diceVal[1] = (new Random().nextInt(5) + 1);
    }
  }

  private void availableMoves(int dice1, int dice2){
    if (dice1 == dice2) {availableMoves = 4;}
      else {availableMoves = 2;}
    }

  @Override
  public void selectPiece(int pos) {
    if (board[pos].color == currentPlayer && board[pos].count != 0){
      isSelected = true;
      piecePos = pos;
    }
  }

  @Override
  public void moveGamePiece(int destination) {
    int out = p1Out;
    int ownOut = p2Out;
    int movement = -1;

    if (currentPlayer == player1){
      out = p2Out;
      ownOut = p1Out;
      movement = 1;
      if ((destination < piecePos && board[ownOut].count < 1) || (destination > 5 && board[ownOut].count > 0)){
        isSelected = false;
        return;
      }
    }
    else if ((destination > piecePos && !clearable) || (destination < 18 && board[ownOut].count > 0)) { return; }

    if (board[ownOut].count != 0 && !possibleStep(destination, movement)) {availableMoves = 0;}

    if (piecePos == destination
            || (board[destination].color != currentPlayer && board[destination].count > 1)
            || (board[destination].count == 5 && !clearable)
            || ((destination == 24 || destination == 25) && !clearable)
            || (destination == p1Out || destination == p2Out)
            || (!possibleStep(destination, movement)) && board[ownOut].count == 0){
      isSelected = false;
      return;
    }

    if ((currentPlayer == board[destination].color) || board[destination].color == 0){
      board[piecePos].popPiece();
      board[destination].pushPiece(currentPlayer);
    }

    if((board[destination].color != currentPlayer) && (board[destination].count < 2)){
      board[destination].popPiece();
      board[out].pushPiece(board[out].color);
      board[piecePos].popPiece();
      board[destination].pushPiece(currentPlayer);
    }

    playedDice = (destination - piecePos) * movement;
    isSelected = false;
    availableMoves -= 1;
  }

  private boolean possibleStep(int destination, int movement){
    boolean isPossible = false;
    int out = p1Out;
    int baseBorder = -1;

    if (currentPlayer == player2){
      out = p2Out;
      baseBorder = 24;
      if (destination == 25 && clearable) {isPossible = true;}
    }
    if (board[out].count != 0
            && ((destination == baseBorder + (diceVal[0] * movement)) || (destination == baseBorder + (diceVal[1] * movement)))){
      isPossible = true;
    }

    if ((diceVal[0] == diceVal[1] && (destination == piecePos + (diceVal[0] * movement)))) {isPossible = true;}

    else if ((destination == piecePos + (diceVal[0] * movement) || (destination == piecePos + (diceVal[1] * movement)))
              && playedDice != (destination - piecePos) * movement){
      isPossible = true;
    }

    return isPossible;
  }

  @Override
  public Base[] getBoard() {
    Base[] boardCopy = new Base[board.length];
    System.arraycopy(board, 0, boardCopy, 0, board.length);
    return boardCopy;
  }

  @Override
  public int[] getDices() {
    int[] diceValCopy = new int[diceVal.length];
    System.arraycopy(diceVal, 0, diceValCopy, 0, diceVal.length);
    return diceValCopy;
  }

  @Override
  public void setTestDices(int dice1, int dice2){
    testMode = true;
    diceVal[0] = dice1;
    diceVal[1] = dice2;
  }

  @Override
  public void resetGame() {
    currentPlayer = 0;
    isSelected = false;
    availableMoves = 0;
    gameIsOver = false;
    roundStarted = false;
    Arrays.stream(board).forEach(Base::reset);
    Arrays.stream(diceVal).forEach(i -> i = 0);
    start();
  }

  @Override
  public int getPlayer() {
    return currentPlayer;
  }

  @Override
  public int getAvailableMoves() {
    return availableMoves;
  }

  @Override
  public boolean clearable(){
    return clearable;
  }

  @Override
  public boolean isSelected(){
    return isSelected;
  }
}
