/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pis.projekt;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppTest {
  /**
   * Es wird eine Partie Simuliert und im Laufe dieser werden unterschiedliche Szenarien
   * durchgespielt, die die Funktionalität des Projekts testen. Es wird ein Testboard erzeugt um in
   * Verlauf der Simulation die korrekte Anzahl, Farbe und Position zu überprüfen. Mit //preparation
   * markierte codeblöcke stellen die Vorbereitung einer bestimmten Spielsituation dar.
   */
  @Test
  public void test() {
    BackgammonInterface backend = new GameEngine();
    backend.initializeBoard();
    backend.start();

    Base[] testBoard = new Base[28];
    Base[] board = backend.getBoard();

    for (int i = 0; i < testBoard.length; i++) {
      testBoard[i] = new Base(0, 0, false);
    }

    testBoard[0].color = 1;
    testBoard[0].count = 2;

    testBoard[5].color = 2;
    testBoard[5].count = 5;

    testBoard[7].color = 2;
    testBoard[7].count = 3;

    testBoard[11].color = 1;
    testBoard[11].count = 5;

    testBoard[12].color = 2;
    testBoard[12].count = 5;

    testBoard[16].color = 1;
    testBoard[16].count = 3;

    testBoard[18].color = 1;
    testBoard[18].count = 5;

    testBoard[23].color = 2;
    testBoard[23].count = 2;

    testBoard[24].color = 1;
    testBoard[26].color = 1;

    testBoard[25].color = 2;
    testBoard[27].color = 2;

    // Test#1: Ist die Anzahl und Farbe der aufgestellten Spielsteine Korrekt?
    for (int i = 0; i < testBoard.length; i++) {
      assertEquals(testBoard[i].count, board[i].count);
      assertEquals(testBoard[i].color, board[i].color);
    }
    System.out.println("\ntest1: testboard identical to gameboard setup\n");

    // Test#2: Ist die Anzahl und Farbe der aufgestellten Spielsteine nach dem Reset immer noch
    // Korrekt?
    backend.resetGame();
    for (int i = 0; i < testBoard.length; i++) {
      assertEquals(testBoard[i].count, board[i].count);
      assertEquals(testBoard[i].color, board[i].color);
    }
    System.out.println("test2: reset works properly\n");

    // Test#3: Der Spieler der Anfängt darf nicht den Wert 0 haben, da dem Spieler eine Zahl
    // zugeordnet wird
    assertNotEquals(0, backend.getPlayer());
    System.out.println("test3: a starting player is selected\n");

    // Test#4: Die Würfelwerte dürfen nach spielbeginn nicht 0 sein
    int[] testAr = new int[] {0, 0};
    assertNotEquals(testAr, backend.getDices());
    System.out.println("test4: dices are rolled\n");

    // Test#5: sowohl der Yard, als auch das Aus müssen im Backend zu beginn des Spiels als out
    // markiert sein
    for (int i = 24; i < 28; i++) {
      testBoard[i].out = true;
    }
    for (int i = 24; i < 28; i++) {
      assertEquals(testBoard[i].out, board[i].out);
    }
    System.out.println("test5: yard and out have to be declared as outer positions\n");

    // Test#6: Wenn ein Pasch geworfen wurde, stehen 4 Züge zur Verfügung. Andernfalls stehen 2 Züge
    // zur verfügung
    int[] testDicesAreDifferent = new int[] {1, 2};
    int assumedMovesForDifferentDices = 2;
    backend.setTestDices(1, 2);
    backend.round();

    for (int i = 0; i < 2; i++) {
      assertEquals(testDicesAreDifferent[i], backend.getDices()[i]);
    }
    assertEquals(assumedMovesForDifferentDices, backend.getAvailableMoves());

    int[] testDicesAreEqual = new int[] {2, 2};
    int assumedMovesForEqualDices = 4;
    backend.resetGame();
    backend.setTestDices(2, 2);
    backend.round();

    for (int i = 0; i < 2; i++) {
      assertEquals(testDicesAreEqual[i], backend.getDices()[i]);
    }
    assertEquals(assumedMovesForEqualDices, backend.getAvailableMoves());
    System.out.println(
        "test6: available moves are set correctly\n\tsidenote: testmode works properly to\n");

    // Test#7: Es wird überprüft ob selektion durchgeführt werden kann
    backend.setTestDices(2, 1);
    backend.resetGame();
    backend.round();
    backend.selectPiece(0);

    assertTrue(backend.isSelected());
    System.out.println("test7: gamepiece selection is possible\n");

    // preparation
    testBoard[0].popPiece();
    testBoard[0].popPiece();
    testBoard[1].pushPiece(1);
    testBoard[2].pushPiece(1);
    // preparation finished

    // test#8: Es wird überprüft ob zwei spielsteine bewegt werden können
    backend.moveGamePiece(1);
    backend.selectPiece(0);
    backend.moveGamePiece(2);

    for (int i = 0; i < 3; i++) {
      assertEquals(testBoard[i].count, board[i].count);
    }
    System.out.println("test8: movement possible\n");

    // test#9: Es wird überprüft ob die Anzahl der Züge nach 2 Bewegungen von 2 auf 0 fällt
    assertEquals(0, backend.getAvailableMoves());
    System.out.println("test9: count of possible moves drops from 2 to 0 after two moves\n");

    // test#10: Es wird überprüft ob die Selektion aufgehoben wird, wenn als Ziel die Startposition
    // des Spielsteins gewählt wird. Ebenfalls wird überprüft ob die Anzahl der möglichen Züge
    // unverändert bleibt.
    backend.round();
    backend.setTestDices(1, 3);
    backend.round();
    backend.selectPiece(23);

    assertTrue(backend.isSelected());

    backend.moveGamePiece(23);

    assertFalse(backend.isSelected());
    assertEquals(2, backend.getAvailableMoves());
    System.out.println(
        "test10: selection is canceled, after selecting destination on the same position as starting position.\n"
            + "\t\tAvailable moves are not affected.\n");

    // preparation
    testBoard[1].popPiece();
    testBoard[2].popPiece();
    testBoard[26].pushPiece(1);
    testBoard[26].pushPiece(1);
    testBoard[5].popPiece();
    testBoard[1].pushPiece(2);
    backend.selectPiece(5);
    backend.moveGamePiece(2);
    backend.selectPiece(2);
    backend.moveGamePiece(1);
    // preparation finished

    // test#11: spieler 1 verliert 2 Spielsteine. Es wird überprüft ob beide Steine ins Aus
    // verschoben werden.
    assertEquals(2, backend.getBoard()[26].count);
    for (int i = 0; i < testBoard.length; i++) {
      assertEquals(testBoard[i].count, board[i].count);
      assertEquals(testBoard[i].color, board[i].color);
    }
    System.out.println("test11: 2 gamepieces successfully pushed into out\n");

    // preparation
    testBoard[26].popPiece();
    testBoard[26].popPiece();
    testBoard[0].pushPiece(1);
    testBoard[2].pushPiece(1);

    backend.round();
    backend.setTestDices(1, 3);
    backend.round();

    backend.selectPiece(26);
    backend.moveGamePiece(0);
    backend.selectPiece(26);
    backend.moveGamePiece(2);
    // preparation finished

    // test#12: spieler 1 bringt die Spielsteine zurück auf's Feld
    for (int i = 0; i < testBoard.length; i++) {
      assertEquals(testBoard[i].count, board[i].count);
      assertEquals(testBoard[i].color, board[i].color);
    }
    System.out.println("test12: pieces successfully brought back in the game\n");

    testBoard[23].popPiece();
    testBoard[23].popPiece();
    testBoard[12].popPiece();
    testBoard[12].popPiece();
    testBoard[19].pushPiece(2);
    testBoard[19].pushPiece(2);
    testBoard[8].pushPiece(2);
    testBoard[8].pushPiece(2);

    backend.round();
    backend.setTestDices(4, 4);
    backend.round();

    // test#13: Spieler 2 kann mit einem Pasch 4 züge tätigen
    assertEquals(4, backend.getAvailableMoves());
    backend.selectPiece(23);
    backend.moveGamePiece(19);
    backend.selectPiece(23);
    backend.moveGamePiece(19);
    backend.selectPiece(12);
    backend.moveGamePiece(8);
    backend.selectPiece(12);
    backend.moveGamePiece(8);

    for (int i = 0; i < testBoard.length; i++) {
      assertEquals(testBoard[i].count, board[i].count);
      assertEquals(testBoard[i].color, board[i].color);
    }
    System.out.println("test13: movement with 4 available moves is possible\n");

    // test#14: Züge gegen die Laufrichtung resultieren in aufhebung der Selektion
    backend.round();
    backend.setTestDices(2, 1);
    backend.round();

    backend.selectPiece(11);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(10);

    assertFalse(backend.isSelected());
    System.out.println("test14: movement backwards results in cancellation of selection\n");

    // test#15: Verschieben der Spielsteine ins Aus resultieren in aufhebung der Selektion
    backend.selectPiece(11);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(26);

    assertFalse(backend.isSelected());
    System.out.println("test15: movement to out results in cancellation of selection\n");

    // test#16: Verschieben der Spielsteine in den Endbereich ohne alle Steine vieher in die Base
    // gebracht zu haben resultieren in aufhebung der Selektion
    backend.selectPiece(11);
    assertTrue(backend.isSelected());
    assertFalse(backend.clearable());
    backend.moveGamePiece(24);

    assertFalse(backend.isSelected());
    System.out.println(
        "test16: movement to yard without bringing all pieces to players base results in cancellation of selection\n");


    // test#17: Der Versuch einen Spielstein einer anderen Farbe auf ein Feld mit mehr als einem
    // Stein zu bringen resultiert in aufhebung der Selektion
    backend.selectPiece(11);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(12);
    assertFalse(backend.isSelected());
    System.out.println(
        "test17: are 2 and more pieces Stacked, results attempt to move a piece of"
                + "different color to this position\n in cancellation of selection\n");

    // test#18: Das doppelte Gehen von augenzahlen, wenn 2 züge zur verfügung stehen, resutliert
    // beim zweiten Zug in aufhebung der selektion, verfügbare Züge werden nicht beeinflusst
    testBoard[16].popPiece();
    testBoard[17].pushPiece(1);
    backend.selectPiece(16);
    backend.moveGamePiece(17);
    backend.selectPiece(16);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(17);
    assertFalse(backend.isSelected());

    for (int i = 0; i < testBoard.length; i++) {
      assertEquals(testBoard[i].count, board[i].count);
      assertEquals(testBoard[i].color, board[i].color);
    }

    assertEquals(1, backend.getAvailableMoves());
    System.out.println(
        "test18: moving twice the same step with 2 available steps results in the second attempt in cancellation\n"
            + "\t\tof selection without affecting the remaining available move\n");

    // test#19: das auswählen der gegnerischen Spielsteine resultiert nicht in selektion
    backend.selectPiece(7);

    assertFalse(backend.isSelected());
    System.out.println("test19: opponents gamepieces can't be selected\n");

    // test#20: der Versuch mehr als 5 Spielsteine aufeinander zu setzten resultiert in aufhebung
    // der selektion
    backend.selectPiece(16);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(18);

    assertFalse(backend.isSelected());
    assertEquals(1, backend.getAvailableMoves());

    System.out.println(
        "test20: more than 5 pieces can't be stacked during start or midgame, attempt results in cancellation\n"
            + "\t\tof selection without affecting the remaining available move\n");

    // test#21: der Versuch eine nicht erlaubte Anzahl von Schritten zu gehen resultiert in
    // aufhebung der Selektion
    backend.selectPiece(16);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(19);

    assertFalse(backend.isSelected());
    assertEquals(1, backend.getAvailableMoves());

    System.out.println(
        "test21: unallowed steps result in cancellation of selection without affecting the remaining available move\n");

    // test#22: Wenn die Anzahl der verbliebenen Züge auf 0 fällt muss der Spieler gewechselt werden
    assertEquals(1, backend.getAvailableMoves());
    assertEquals(1, backend.getPlayer());

    testBoard[11].popPiece();
    testBoard[13].pushPiece(1);
    backend.selectPiece(11);
    backend.moveGamePiece(13);

    assertNotEquals(1, backend.getAvailableMoves());
    backend.round();

    assertNotEquals(1, backend.getPlayer());
    assertEquals(2, backend.getPlayer());
    System.out.println(
        "test22: the current player should be switched, if the count of available moves reaches 0\n");

    // test#23: ein Sprung um die Summe der Augenzahlen resultiert in aufhebung der selektion
    backend.setTestDices(3, 2);
    backend.round();

    backend.selectPiece(12);
    backend.moveGamePiece(7);

    assertFalse(backend.isSelected());
    System.out.println(
        "test23: the sum of possible steps can't be used in one move. Attempt results in cancellation of selection\n");

    // preparation
    testBoard[8].popPiece();
    testBoard[3].pushPiece(2);
    backend.selectPiece(8);
    backend.moveGamePiece(6);
    backend.selectPiece(6);
    backend.moveGamePiece(3);

    backend.round();
    backend.setTestDices(1, 5);
    backend.round();

    backend.selectPiece(0);
    backend.moveGamePiece(1);
    backend.selectPiece(1);
    backend.moveGamePiece(6);

    backend.round();
    backend.setTestDices(4, 5);
    backend.round();
    // preparation finished

    // test#24: das Zurückbringen der Spielsteine auf ein Feld außerhalb der Zielbase ist nicht
    // möglich
    backend.selectPiece(27);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(10);
    assertNotEquals(1, board[10].count);
    System.out.println(
        "test24: gamepiece can't be moved from out to a field wich is not in the opponents base\n");

    // test#25: das bewegen aus dem Aus in den Endbereich eines Spielers resultiert in aufhebung der
    // Selektion
    backend.selectPiece(27);
    assertTrue(backend.isSelected());
    backend.moveGamePiece(25);
    assertFalse(backend.isSelected());
    System.out.println(
        "test25: attempt of movement form players out to players Yard results in results in cancellation of selection\n");

    // preparation
    backend.selectPiece(27);
    backend.moveGamePiece(19);
    backend.selectPiece(7);
    backend.moveGamePiece(3);
    backend.round();
    backend.setTestDices(5, 6);
    backend.round();
    backend.selectPiece(17);
    backend.moveGamePiece(22);
    backend.selectPiece(16);
    backend.moveGamePiece(22);
    backend.round();
    backend.setTestDices(4, 4);
    backend.round();
    backend.selectPiece(19);
    backend.moveGamePiece(15);
    backend.selectPiece(19);
    backend.moveGamePiece(15);
    backend.selectPiece(19);
    backend.moveGamePiece(15);
    backend.selectPiece(8);
    backend.moveGamePiece(4);
    backend.round();
    backend.setTestDices(4, 6);
    backend.round();
    backend.selectPiece(2);
    backend.moveGamePiece(6);
    backend.selectPiece(13);
    backend.moveGamePiece(19);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(15);
    backend.moveGamePiece(9);
    backend.selectPiece(15);
    backend.moveGamePiece(9);
    backend.selectPiece(15);
    backend.moveGamePiece(9);
    backend.selectPiece(7);
    backend.moveGamePiece(1);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(11);
    backend.moveGamePiece(17);
    backend.selectPiece(11);
    backend.moveGamePiece(17);
    backend.selectPiece(11);
    backend.moveGamePiece(17);
    backend.selectPiece(11);
    backend.moveGamePiece(17);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(9);
    backend.moveGamePiece(3);
    backend.selectPiece(9);
    backend.moveGamePiece(3);
    backend.selectPiece(9);
    backend.moveGamePiece(3);
    backend.selectPiece(7);
    backend.moveGamePiece(1);
    backend.round();
    backend.setTestDices(5, 5);
    backend.round();
    backend.selectPiece(6);
    backend.moveGamePiece(11);
    backend.selectPiece(6);
    backend.moveGamePiece(11);
    backend.selectPiece(17);
    backend.moveGamePiece(22);
    backend.selectPiece(17);
    backend.moveGamePiece(22);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(12);
    backend.moveGamePiece(6);
    backend.selectPiece(12);
    backend.moveGamePiece(6);
    backend.selectPiece(12);
    backend.moveGamePiece(6);
    backend.selectPiece(6);
    backend.moveGamePiece(0);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(11);
    backend.moveGamePiece(17);
    backend.selectPiece(11);
    backend.moveGamePiece(17);
    backend.selectPiece(17);
    backend.moveGamePiece(23);
    backend.selectPiece(17);
    backend.moveGamePiece(23);
    backend.round();
    backend.setTestDices(2, 6);
    backend.round();
    backend.selectPiece(6);
    backend.moveGamePiece(4);
    backend.selectPiece(6);
    backend.moveGamePiece(0);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(16);
    backend.moveGamePiece(22);
    backend.selectPiece(17);
    backend.moveGamePiece(23);
    backend.selectPiece(17);
    backend.moveGamePiece(23);
    backend.selectPiece(18);
    backend.moveGamePiece(24);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(5);
    backend.moveGamePiece(25);
    backend.selectPiece(5);
    backend.moveGamePiece(25);
    backend.selectPiece(5);
    backend.moveGamePiece(25);
    backend.selectPiece(5);
    backend.moveGamePiece(25);
    backend.round();
    backend.setTestDices(6, 6);
    backend.round();
    backend.selectPiece(18);
    backend.moveGamePiece(24);
    backend.selectPiece(18);
    backend.moveGamePiece(24);
    backend.selectPiece(18);
    backend.moveGamePiece(24);
    backend.selectPiece(18);
    backend.moveGamePiece(24);
    backend.round();
    backend.setTestDices(4, 5);
    backend.round();
    backend.selectPiece(4);
    backend.moveGamePiece(25);
    backend.selectPiece(3);
    backend.moveGamePiece(25);
    backend.round();
    backend.setTestDices(2, 5);
    backend.round();
    backend.selectPiece(19);
    backend.moveGamePiece(24);
    backend.selectPiece(22);
    backend.moveGamePiece(24);
    backend.round();
    backend.setTestDices(4, 5);
    backend.round();
    backend.selectPiece(4);
    backend.moveGamePiece(25);
    backend.selectPiece(3);
    backend.moveGamePiece(25);
    backend.round();
    backend.setTestDices(2, 2);
    backend.round();
    backend.selectPiece(22);
    backend.moveGamePiece(24);
    backend.selectPiece(22);
    backend.moveGamePiece(24);
    backend.selectPiece(22);
    backend.moveGamePiece(24);
    backend.selectPiece(22);
    backend.moveGamePiece(24);
    backend.round();
    backend.setTestDices(2, 4);
    backend.round();
    backend.selectPiece(3);
    backend.moveGamePiece(25);
    backend.selectPiece(1);
    backend.moveGamePiece(25);
    backend.round();
    backend.setTestDices(1, 1);
    backend.round();
    backend.selectPiece(23);
    backend.moveGamePiece(24);
    backend.selectPiece(23);
    backend.moveGamePiece(24);
    backend.selectPiece(23);
    backend.moveGamePiece(24);
    backend.selectPiece(23);
    backend.moveGamePiece(24);
    // preparation finished

    // test#26: wenn das Spiel zu Ende ist kann ein Spielerwechsel nicht mehr stattfinden
    assertTrue(backend.isGameOver());
    assertEquals(1, backend.getPlayer());
    backend.round();
    assertNotEquals(2, backend.getPlayer());
    System.out.println("test26: switch of players is impossible after the game ist over\n");

  }
}
