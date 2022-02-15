import java.awt.*;

public class Cell {
  private final int x;
  private final int y;
  private final int size;
  private int state;
  public static final Color[] COLORS = {Color.WHITE, Color.BLACK};

  public Cell(int x, int y, int size) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.state = 0;
  }

  public void draw(Graphics g) {
    g.setColor(Color.LIGHT_GRAY);
    g.drawRect(x, y, size, size);
    g.setColor(COLORS[state]);
    g.fillRect(x + 1, y + 1, size - 1, size - 1);
  }

  public boolean isOff() {
    return this.state == 0;
  }

  public boolean isOn() {
    return this.state == 1;
  }

  public void turnOn() {
    state = 1;
  }

  public void turnOff() {
    state = 0;
  }
}
