package pis.projekt;

import processing.core.PApplet;
import processing.core.PImage;

public class Draw extends PApplet {
  private final int firstQuarterPosX = 113;
  private final int secondQuarterPosX = 438;
  private final int firstHalfPosY = 55;
  private final int secondHalfPosY = 659;
  private final int pYardX = 50;
  BackgammonInterface backEnd = new GameEngine();

  public static void main(String[] args) {
    PApplet.runSketch(new String[] {""}, new Draw());
  }

  private void showSidebar(int[] dices) {
    fill(90);
    rect(width - 300, 0, 300, height);

    fill(0);
    rect(width - 275, 25, 250, 100);

    fill(0);
    rect(width - 175, 160, 50, 50);

    textSize(15);
    fill(255);
    text("moves left", width - 185, 150);

    textSize(40);
    fill(255);
    text(backEnd.getAvailableMoves(), width - 161, 198);

    textSize(15);
    fill(255);
    text("Player's turn", width - 195, 340);

    fill(0);
    rect(width - 225, height - 100, 150, 50);
    textSize(32);
    fill(255);
    text("Exit", width - 185, height - 62);

    fill(0);
    rect(width - 225, height - 160, 150, 50);
    textSize(32);
    fill(255);
    text("Reset", width - 185, height - 122);

    PImage[] dicesAr =
        new PImage[] {
          loadImage("1.png"),
          loadImage("2.png"),
          loadImage("3.png"),
          loadImage("4.png"),
          loadImage("5.png"),
          loadImage("6.png")
        };

    image(dicesAr[dices[0] - 1], width - 250, 37.5F, 75, 75);
    image(dicesAr[dices[1] - 1], width - 125, 37.5F, 75, 75);
  }

  private void showPieces(Base[] board) {

    int pieceOffset = 47;
    int pOutOffset = 16;
    int midX = 392;
    int midY = 357;

    PImage[] pieceAr =
        new PImage[] {
          loadImage("pieceBlack.png"),
          loadImage("pieceWhite.png"),
          loadImage("pOutBlack.png"),
          loadImage("pOutWhite.png"),
        };

    image(pieceAr[backEnd.getPlayer() - 1], width - 200, 350, 100, 100);

    for (int i = 0; i < 6; i++) {
      buildPieceStack(
          firstQuarterPosX + (i * pieceOffset),
          firstHalfPosY,
          board[i].count,
          board[i].color,
          pieceOffset,
          pieceAr);

      buildPieceStack(
          secondQuarterPosX + (i * pieceOffset),
          firstHalfPosY,
          board[6 + i].count,
          board[6 + i].color,
          pieceOffset,
          pieceAr);

      buildPieceStack(
          (secondQuarterPosX + (5 * pieceOffset)) - (i * pieceOffset),
          secondHalfPosY - (board[(12 + i)].count) * pieceOffset,
          board[12 + i].count,
          board[12 + i].color,
          pieceOffset,
          pieceAr);

      buildPieceStack(
          (firstQuarterPosX + (5 * pieceOffset)) - (i * pieceOffset),
          secondHalfPosY - (board[(18 + i)].count) * pieceOffset,
          board[18 + i].count,
          board[18 + i].color,
          pieceOffset,
          pieceAr);
    }

    buildPieceStack(
        pYardX, firstHalfPosY, board[25].count, board[25].color + 2, pOutOffset, pieceAr);
    buildPieceStack(
        pYardX,
        secondHalfPosY - (board[24].count * pOutOffset),
        board[24].count,
        board[24].color + 2,
        pOutOffset,
        pieceAr);

    buildPieceStack(midX, midY, board[26].count, board[26].color, pieceOffset, pieceAr);
    buildPieceStack(
        midX,
        midY - (board[27].count * pieceOffset),
        board[27].count,
        board[27].color,
        pieceOffset,
        pieceAr);
  }

  private void buildPieceStack(
      int posX, int posY, int count, int color, int offset, PImage[] pieces) {
    int height = 45;
    if (color > 2) {height = 15;}
    for (int i = 0; i < count; i++) {
      image(pieces[(color - 1)], posX, posY + (i * offset), 45, height);
    }
  }

  public void mouseClicked() {
    int middleGap = 50;
    int outGap = 45;

    if ((width - 225 < mouseX && mouseX < width - 75)
        && (height - 100 < mouseY && mouseY < height - 50)) {exit();}

    if ((width - 225 < mouseX && mouseX < width - 75)
        && (height - 160 < mouseY && mouseY < height - 110)) {backEnd.resetGame();}

    if ((112 < mouseX && mouseX < 716) && (54 < mouseY && mouseY < 660)) {
      int startPosX = 113;
      int startPosY = 55;

      float offsetX = 46.16F;
      int offsetY = 302;
      int pos;
      for (int i = 0; i < 4; i++) {
        if (i % 3 == 1) startPosX += middleGap + (offsetX * 6);
        if (i % 3 == 2) startPosY += offsetY;
        if (i % 7 == 3) startPosX -= middleGap + (offsetX * 6);

        for (int j = 0; j < 6; j++) {

          if (startPosX + (j * offsetX) < mouseX
              && mouseX < (startPosX + offsetX) + (j * offsetX)
              && (startPosY < mouseY && mouseY < (startPosY + offsetY))) {
            if (i < 2) {pos = (i * 6) + j;}
            else {pos = ((i + 1) * 6) + ((j + 1) * (-1));}
            markPos(startPosX + (j * offsetX), startPosY, 1);

            if (backEnd.isSelected()) {backEnd.moveGamePiece(pos);}
            else {backEnd.selectPiece(pos);}
          }
        }
      }
    }

    if (((secondQuarterPosX - middleGap + 5) < mouseX && mouseX < secondQuarterPosX)
        && (firstHalfPosY < mouseY && mouseY < secondHalfPosY)) {

      markPos(secondQuarterPosX - middleGap + 5, firstHalfPosY, 2);
      int pos;

      if (backEnd.getPlayer() == 1) {pos = 26;}
      else {pos = 27;}

      if (backEnd.isSelected()) {backEnd.moveGamePiece(pos);}
      else {backEnd.selectPiece(pos);}
    }

    if ((pYardX < mouseX && mouseX < pYardX + outGap)
        && (firstHalfPosY < mouseY && mouseY < secondHalfPosY)
        && backEnd.clearable()) {

      int pos;
      if (backEnd.getPlayer() == 1) {pos = 24;}
      else {pos = 25;}
      
      if (backEnd.isSelected()) {backEnd.moveGamePiece(pos);}
    }
  }

  private void markPos(float x, float y, int factor) {
    int sizeY = 302;
    if (backEnd.isSelected()) {fill(0, 255, 0, 30);}
    else {fill(0, 0, 255, 30);}

    rect(x, y, 45.16F, sizeY * factor);
  }

  private void run() {

    if (backEnd.isGameOver()) {
      fill(90);
      rect(firstQuarterPosX, ((secondHalfPosY - firstHalfPosY) / 2F + firstHalfPosY), 230, 50);
      textSize(30);
      fill(255);
      text(
          "Winner: Player" + backEnd.getPlayer(),
          firstQuarterPosX,
          ((secondHalfPosY - firstHalfPosY) / 2F + 85));
    }

    image(loadImage("BGBoard.png"), 30, 30, (float) (1920 * 0.4), (float) (1629 * 0.4));
    backEnd.round();
    showSidebar(backEnd.getDices());
    showPieces(backEnd.getBoard());
  }

  public void settings() {
    width = 1280;
    height = 720;
  }

  public void setup() {
    backEnd.initializeBoard();
    backEnd.start();
    background(0);
  }

  public void draw() {
    run();
  }
}
