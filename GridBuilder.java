import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.regex.*;

public class GridBuilder {
  private JFrame frame;
  private JTextField rowsField;
  private JTextField colsField;
  private JTextField sizeField;
  private GameOfLife game;
  private Runnable GameLoop;
  private Thread GameThread;

  public static void main(String[] args) {
    new GridBuilder().go();
  }
  
  // BUILD GUI
  public void go() {
    // Create components
    frame = new JFrame("Conway's Game of Life Grid Setup");
    JPanel mainPanel = new JPanel();
    GridLayout grid = new GridLayout(3,2);
    JPanel inPanel = new JPanel(grid);
    JTextArea rulesBox = new JTextArea(getRules(), 10, 30);
    JButton drawButton = new JButton("DRAW");
    drawButton.addActionListener( new DrawButtonListener() );
    
    JScrollPane scroller = new JScrollPane(rulesBox);
    scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    rowsField = new JTextField();
    rowsField.addFocusListener( new FieldFocusListener() );
    colsField = new JTextField();
    colsField.addFocusListener( new FieldFocusListener() );
    sizeField = new JTextField();
    sizeField.addFocusListener( new FieldFocusListener() );
    
    // Style
    Font f1 = new Font("sansserif", Font.PLAIN, 25);
    Font f2 = new Font("sansserif", Font.BOLD, 25);
    
    rulesBox.setFont(f1);
    drawButton.setFont(new Font("serif", Font.BOLD, 28));
    
    Border outerBorder = BorderFactory.createEtchedBorder(Color.BLUE, Color.WHITE);
    Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    CompoundBorder textFieldsBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
    
    rowsField.setFont(f2);
    rowsField.setBorder(textFieldsBorder);
    rowsField.setText("9"); // default value
    rowsField.setCaretPosition(rowsField.getText().strip().length());
    rowsField.setHorizontalAlignment(0);

    colsField.setFont(f2);
    colsField.setBorder(textFieldsBorder);
    colsField.setText("9"); // default value
    colsField.setCaretPosition(colsField.getText().strip().length());
    colsField.setHorizontalAlignment(0);

    sizeField.setFont(f2);
    sizeField.setBorder(textFieldsBorder);
    sizeField.setText("50"); // default value
    sizeField.setCaretPosition(sizeField.getText().strip().length());
    sizeField.setHorizontalAlignment(0);
    
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    Border border = BorderFactory.createEmptyBorder(50, 30, 10, 10);
    TitledBorder inPanelBorder = BorderFactory.createTitledBorder(border, "Grid Settings");
    inPanelBorder.setTitleFont(f2);
    inPanelBorder.setTitleJustification(2);
    inPanelBorder.setTitleColor(Color.BLUE);
    inPanelBorder.setTitlePosition(1);
    inPanel.setBorder(inPanelBorder);
    
    Border rulesBoxOutsideBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    Border rulesBoxInsideBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    CompoundBorder rulesBoxBorder = BorderFactory.createCompoundBorder (
      rulesBoxOutsideBorder, rulesBoxInsideBorder
      );
    rulesBox.setBorder(rulesBoxBorder);
    
    grid.setHgap(30);
    grid.setVgap(30);
    
    rulesBox.setBackground(new Color(208, 194, 193));
    rulesBox.setForeground(Color.BLACK);
    rulesBox.setEditable(false);
    rulesBox.setLineWrap(true);
    rulesBox.setWrapStyleWord(true);
    
    // Add contents
    mainPanel.add(BorderLayout.WEST, scroller);
    inPanel.add(new JLabel("Rows (4 min - 10 max)")).setFont(f2);
    inPanel.add(rowsField);
    inPanel.add(new JLabel("Cols (3 min - 20 max)")).setFont(f2);
    inPanel.add(colsField);
    inPanel.add(new JLabel("Cell Size (25 min - 80 max)")).setFont(f2);
    inPanel.add(sizeField);
    mainPanel.add(inPanel);
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(BorderLayout.WEST, mainPanel);
    frame.getContentPane().add(BorderLayout.SOUTH, drawButton);
    // frame.setSize(1850, 600);
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
  }
  
  // PRINT GAME DESCRIPTION & RULES
  private String getRules() {
    String rules = null;
    rules = "Conway's Game of Life\n\n";
    rules += "- The game board is a two-dimensional grid of square cells.\n\n";
    rules += "- Each cell is either 'alive' or 'dead'; the color of the cell indicates its state.\n\n";
    rules += "- The game proceeds in time steps, during which each cell interacts with its ";
    rules += "neighbors in the eight adjacent cells.\n\n"; 
    rules += "At each time step, the following rules are applied:\n\n";
    rules += "1. A live cell with fewer than two live neighbors dies, as if by underpopulation.\n\n";
    rules += "2. A live cell with more than three live neighbors dies, as if by overpopulation.\n\n";
    rules += "3. A dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.\n\n";
    rules += "You can set a custom number of rows, columns and cell size (Grid Settings).\n\n";
    rules += "NOTE: The following are the availabe numerical range (Min & Max) (inclusive) for each grid setting:\n\n";
    rules += "> Rows: 4 to 10 rows\n";
    rules += "> Columns: 3 to 20 columns\n";
    rules += "> Cell Size: 25 to 80\n";
    return rules;
  }

  // HELPER METHOD FOR THE FIELD FOCUS LISTENER (?PRIVATE ACCESS MODIFIER)
  public void warn(JTextField inField) {
    if (inField.getText().isBlank()) inField.setBackground(Color.YELLOW);
    else if (!Pattern.compile("^\\d+$").matcher(inField.getText().strip()).find())
      inField.setBackground(Color.RED);
    else inField.setBackground(Color.WHITE);
  }

  // HELPER METHOD FOR THE DRAW BUTTON LISTENER (?PRIVATE ACCESS MODIFIER?)
  public int getFieldValue(JTextField inField) {
      return Integer.parseInt(inField.getText().strip());
  }

  // HELPER METHOD FOR VALIDATING USER INPUT
  public boolean validate(JTextField inField, int min, int max) {
    boolean proceed = false;
    // check that only integers are accepted
    Pattern p = Pattern.compile("^\\d\\d?$");
    Matcher m = p.matcher(inField.getText().strip());

    if (m.find()) {
      int fieldVal = getFieldValue(inField);
      // check that that integers falls within appropriate ranges 
      if (fieldVal >= min && fieldVal <= max) {
        proceed = true;
        // System.out.println("proceed"); // debugging
      } 
    }
    return proceed;
  }

  // HELPER METHOD FOR DRAWING THE NEW SIM. GRID 
  public void drawGrid() {
    int rowsNum = getFieldValue(rowsField);
    int colsNum = getFieldValue(colsField);
    int cellSize = getFieldValue(sizeField);

    // System.out.printf("Grid Dimensions: %d x %d | Cell Size: %d\n",
    // rowsNum, colsNum, cellSize); // debugging

    // Build a new frame for the simulation and start it
    JFrame frame = new JFrame("Conway's Game of Life Simulation");

    // New frame start button
    JButton startButton = new JButton("Start SIM");
    startButton.setFont(new Font("sansserif", Font.BOLD, 20));
    startButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        // Implements the threading process
        GameLoop = new GameLoop();
        GameThread = new Thread(GameLoop);
        // System.out.println("Starting Simulation..."); // debugging
        GameThread.start();
      }
    }); // close anonymous inner class

    // New frame stop button
    JButton stopButton = new JButton("Stop SIM");
    stopButton.setFont(new Font("sansserif", Font.BOLD, 20));
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        // System.out.println("Stopping Simulation..."); // debugging
        GameThread.stop(); // to re-write
      }
    }); // close anonymous inner class

    game = new GameOfLife(rowsNum, colsNum, cellSize);
    frame.setResizable(false);
    frame.add(game.grid); // you can also `frame.getContentPane().add(game.grid);`
    frame.getContentPane().add(BorderLayout.WEST, startButton);
    frame.getContentPane().add(BorderLayout.EAST, stopButton);
    frame.pack();
    frame.setVisible(true);
  
  } // close method 
  
  // HELPER METHOD TO SHOW A NOTE FOR INCORRECT GRID SETTING VALUE
  public void showNote() {
    JTextArea canDraw = new JTextArea();
    String note = "";
    note += "Please make sure that you enter correct grid settings values.\n";
    note += "See NOTE at the end of the text below.";
    canDraw.setText(note);
    canDraw.setEditable(false);
    canDraw.setFocusable(false);
    canDraw.setFont(new Font("sansserif", Font.ITALIC, 28));
    canDraw.setBackground(new Color(238, 238, 236));
    canDraw.setForeground(Color.RED);
    canDraw.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
    frame.getContentPane().add(BorderLayout.NORTH, canDraw);
    frame.pack();
    frame.setVisible(true);
  }

  
  // ========= //
  // LISTENERS //
  // ========= //

  // FIELD FOCUS LISTENER
  class FieldFocusListener implements FocusListener {
    
    @Override
    public void focusGained(FocusEvent ev) { }

    @Override
    public void focusLost(FocusEvent ev) { warn((JTextField) ev.getSource()); }
  
  }

  // DRAW BUTTON LISTENER
  class DrawButtonListener implements ActionListener {
    
    @Override
    public void actionPerformed(ActionEvent ev) {
      // Use regex & numerical RANGES to validate input
      if ( validate(rowsField, 4, 10) &&
       validate(colsField, 3, 20) &&
        validate(sizeField, 25, 80) ) {
        
        drawGrid();
      
      } else { // validation fails, so tell user which ranges are available

        showNote();
        
      } // close else
    
    } // close method

  } // close class


  // ========== //
  // THREAD JOB //
  // ========== //

  // GAME MAIN LOOP 
  public class GameLoop implements Runnable {
    public void run() {
      game.mainLoop(); // start simulation loop
    }
  }
}
