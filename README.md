# GoL
Conway's Game of Life

- The game board is a 2D grid of square cells.
- Each cell is either 'alive' (white) or 'dead' (black) so the color of the cell indicates its state.
- The game proceeds in time steps, during which each cell interacts with its neighbors in the eight adjacent cells.

At each time step, the following rules are applied:
- A live cell with fewer than two live neighbors dies, as if by underpopulation.
- A live cell with more than three live neighbors dies, as if by overpopulation.
- A dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

You can set a custom number of rows, columns and cell size (Grid Settings).

Note: The following are the availabe numerical min/max ranges (inclusive) for each grid setting:
- Rows: 4 to 10 rows
- Columns: 3 to 20 columns
- Cell Size: 25 to 80
