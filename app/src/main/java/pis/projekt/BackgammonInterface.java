package pis.projekt;

public interface BackgammonInterface {

  void initializeBoard();

  void start();

  void round();

  int getPlayer();

  void resetGame();

  int[] getDices();

  Base[] getBoard();

  void selectPiece(int pos);

  void moveGamePiece(int destination);

  int getAvailableMoves();

  boolean isSelected();

  boolean clearable();

  boolean isGameOver();

  void setTestDices(int dice1, int dice2);
}
