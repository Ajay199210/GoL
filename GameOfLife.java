import java.awt.event.*;
import javax.swing.event.*;

public class GameOfLife {
  Grid grid;

  // CONSTRUCTOR
  public GameOfLife(int rowsSettingVal, int colsSettingVal, int sizeSettingVal) {

    grid = new Grid(rowsSettingVal, colsSettingVal, sizeSettingVal);

    // MOUSE LISTENER (ANONYMOUS NESTED INNER CLASS)
    grid.addMouseListener(new MouseInputListener(){

      @Override
      public void mouseClicked(MouseEvent ev) {
        // System.out.println("click on x: " + ev.getX() + " y: " + ev.getY());   // debugging
        // System.out.println("Cell Row #: " + (int) ev.getY() / sizeSettingVal); // debugging
        // System.out.println("Cell Col #: " + (int) ev.getX() / sizeSettingVal); // debugging
        int cellXPos = (int) ev.getY() / sizeSettingVal;    // cell row #
        int cellYPos = (int) ev.getX() / sizeSettingVal;    // cell col #
        Cell cell = grid.getCell(cellXPos, cellYPos); 
        // System.out.println(cell);          // debugging
        // System.out.println(cell.isOff());  // debugging
        if ( cell.isOff() ) cell.turnOn();
        else cell.turnOff();
        grid.repaint();
      }

      @Override
      public void mouseEntered(MouseEvent ev) { }

      @Override
      public void mouseExited(MouseEvent ev) { }

      @Override
      public void mousePressed(MouseEvent ev) { }

      @Override
      public void mouseReleased(MouseEvent ev) { }

      @Override
      public void mouseDragged(MouseEvent ev) { }

      @Override
      public void mouseMoved(MouseEvent ev) { }

    });

  }
  
  // COUNT LIVE CELLS IN THE GRID AROUND A GIVEN CELL (ONLY 1 CELL)
  public int countAlive(int r, int c) {
    int count = 0;
    count += grid.test(r - 1, c - 1);
    count += grid.test(r + 1, c + 1);
    count += grid.test(r - 1, c);
    count += grid.test(r, c - 1);
    count += grid.test(r + 1, c);
    count += grid.test(r, c + 1);
    count += grid.test(r + 1, c - 1);
    count += grid.test(r - 1, c + 1);
    return count;
  }

  // UDPATE THE GAME STATE
  public void update() {
    int[][] counts = countNeighbors();
    updateGrid(counts);
  }

  // COUNT LIVE NEIGHBORS FOR ALL CELLS IN THE GRID
  public int[][] countNeighbors() {
    int rows = grid.numRows();
    int cols = grid.numCols();

    int[][] counts = new int[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        counts[r][c] = countAlive(r, c);
      }
    }
    return counts;
  }

  // UPDATE THE GRID STATE
  public void updateGrid(int[][] counts) {
    int rows = grid.numRows();
    int cols = grid.numCols();

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        Cell cell = grid.getCell(r, c);
        updateCell(cell, counts[r][c]);
      }
    }
  }
  
  // UPDATE THE CELL STATE
  public static void updateCell(Cell cell, int count) { // it's marked static, because it does NOT depend
  // on the grid instance variable
    if (cell.isOn()) {
      if (count < 2 || count > 3) {
        cell.turnOff();
      }
    } else {
      if (count == 3) {
        cell.turnOn();
      }
    }
  }

  // GAME MAIN LOOP
  public void mainLoop() {
    while(true) {
      this.update();
      grid.repaint();
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) { }
    }
  }
}
