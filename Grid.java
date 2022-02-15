import java.awt.*;

public class Grid extends Canvas {

  private Cell[][] grid;

  public Grid(int rows, int cols, int size) {
    grid = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      int y = r * size;
      for (int c = 0; c < cols; c++) {
        int x = c * size;
        grid[r][c] = new Cell(x, y, size);
      }
    }
    // Set canvas size
    setSize(cols * size, rows * size);
  }
  
  public void draw(Graphics g) {
    for (Cell[] row : grid) { 
      for (Cell cell : row) {
        cell.draw(g);
      }
    }
  }

  public void paint(Graphics g) {
    draw(g);
  }

  public int numRows() {
    return grid.length;
  }

  public int numCols() {
    return grid[0].length;
  }

  public Cell getCell(int r, int c) {
   return grid[r][c];
  }

  public void turnOn(int r, int c) {
    grid[r][c].turnOn();
  }

  public void turnOff(int r, int c) {
    grid[r][c].turnOff();
  }

  public int test(int r, int c) {
    try {
      if (grid[r][c].isOn()) return 1;
    } catch (ArrayIndexOutOfBoundsException e) {
      // cell doesn't exist
    }
    return 0;
  }
}
