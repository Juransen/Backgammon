package pis.projekt;

public class Base {
  boolean out;
  int count;
  int color;

  public Base(int count, int color, boolean out) {
    this.out = out;
    this.color = color;
    this.count = count;
  }

  public void pushPiece(int player) {
    if (count == 0) {
      color = player;
    }
    count += 1;
  }

  public void popPiece() {
    if (count > 0) count -= 1;
    if (count == 0 && !out) color = 0;
  }

  public void reset() {
    color = 0;
    count = 0;
  }
}
