package adventofcode;

import java.util.*;
import java.io.*;

public class MonkeyMap {

  public static void main(String[] args) {
    Board input = read_input();
    System.out.println(solve_part_1(input));
    // System.out.println(solve_part_2(input));
  }

  /*
  --- Day 22: Monkey Map ---
  The monkeys take you on a surprisingly easy trail through the jungle. 
  They're even going in roughly the right direction according to your 
  handheld device's Grove Positioning System.

  As you walk, the monkeys explain that the grove is protected by a force 
  field. To pass through the force field, you have to enter a password; doing 
  so involves tracing a specific path on a strangely-shaped board.

  At least, you're pretty sure that's what you have to do; the elephants 
  aren't exactly fluent in monkey.

  The monkeys give you notes that they took when they last saw the password 
  entered (your puzzle input).

  For example:

          ...#
          .#..
          #...
          ....
  ...#.......#
  ........#...
  ..#....#....
  ..........#.
          ...#....
          .....#..
          .#......
          ......#.

  10R5L5R10L4R5L5

  The first half of the monkeys' notes is a map of the board. It is comprised 
  of a set of open tiles (on which you can move, drawn .) and solid walls 
  (tiles which you cannot enter, drawn #).

  The second half is a description of the path you must follow. It consists 
  of alternating numbers and letters:

    - A number indicates the number of tiles to move in the direction you 
      are facing. If you run into a wall, you stop moving forward and 
      continue with the next instruction.
    - A letter indicates whether to turn 90 degrees clockwise (R) or 
      counterclockwise (L). Turning happens in-place; it does not change 
      your current tile.
  
      So, a path like 10R5 means "go forward 10 tiles, then turn clockwise 90 
      degrees, then go forward 5 tiles".

  You begin the path in the leftmost open tile of the top row of tiles. 
  Initially, you are facing to the right (from the perspective of how the map 
  is drawn).

  If a movement instruction would take you off of the map, you wrap around to 
  the other side of the board. In other words, if your next tile is off of 
  the board, you should instead look in the direction opposite of your current 
  facing as far as you can until you find the opposite edge of the board, then 
  reappear there.

  For example, if you are at A and facing to the right, the tile in front of 
  you is marked B; if you are at C and facing down, the tile in front of you 
  is marked D:

          ...#
          .#..
          #...
          ....
  ...#.D.....#
  ........#...
  B.#....#...A
  .....C....#.
          ...#....
          .....#..
          .#......
          ......#.
  
  It is possible for the next tile (after wrapping around) to be a wall; this 
  still counts as there being a wall in front of you, and so movement stops 
  before you actually wrap to the other side of the board.

  By drawing the last facing you had with an arrow on each tile you visit, 
  the full path taken by the above example looks like this:

          >>v#    
          .#v.    
          #.v.    
          ..v.    
  ...#...v..v#    
  >>>v...>#.>>    
  ..#v...#....    
  ...>>>>v..#.    
          ...#....
          .....#..
          .#......
          ......#.
  
  To finish providing the password to this strange input device, you need to 
  determine numbers for your final row, column, and facing as your final 
  position appears from the perspective of the original map. Rows start from 
  1 at the top and count downward; columns start from 1 at the left and count 
  rightward. (In the above example, row 1, column 1 refers to the empty space 
  with no tile on it in the top-left corner.) Facing is 0 for right (>), 1 
  for down (v), 2 for left (<), and 3 for up (^). The final password is the 
  sum of 1000 times the row, 4 times the column, and the facing.

  In the above example, the final row is 6, the final column is 8, and the 
  final facing is 0. So, the final password is 1000 * 6 + 4 * 8 + 0: 6032.

  Follow the path given in the monkeys' notes. What is the final password?
  */

  public static int solve_part_1(Board board) {
    while(board.index_in_path < board.path.length()) {
      board.step();
    }
    System.out.println("Final Row: " + (board.y_pos + 1));
    System.out.println("Final Column: " + (board.x_pos + 1));
    System.out.println("Final Direction: " + board.direction);
    return 1000 * (board.y_pos + 1) + 4 * (board.x_pos + 1) + board.direction;
  }

  /*
  --- Part Two ---
  */

  public static int solve_part_2(Board board) {
    return 0;
  }

  public static Board read_input() {
    Board board = null;
    try {
      File input = new File("input.txt");
      // First, we want to determine the grid with and height:
      Scanner scanner = new Scanner(input);
      int grid_width = 0;
      int grid_height = 0;
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if(!(line.contains(".") || line.contains("#"))) {
          break;
        }
        grid_width = Math.max(grid_width, line.length());
        grid_height++;
      }
      scanner.close();
      // Next, we actually read the input:
      board = new Board(grid_width, grid_height);
      scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if(line.contains(".") || line.contains("#")) {
          board.add_row(line);
        } else if(line.contains("L") || line.contains("R")) {
          board.set_path(line);
        }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return board;
  }
}

class Board {
  
  public Character[][] grid;
  public int grid_width;
  public int grid_height;
  public int x_pos = -1;
  public int y_pos = -1;
  public int direction = 0; // 0 for right, 1 for down, 2 for left, 3 for up

  public String path;

  public Board(int grid_width, int grid_height) {
    this.grid_width = grid_width;
    this.grid_height = grid_height;
    this.grid = new Character[grid_height][grid_width];
  }

  private int current_line = 0;
  public void add_row(String line) {
    for(int i = 0; i < this.grid[current_line].length; i++) {
      if(i < line.length()) {
        this.grid[current_line][i] = line.charAt(i);
        if(this.x_pos == -1 && this.y_pos == -1 && line.charAt(i) == '.') { // find starting position
          this.x_pos = i;
          this.y_pos = 0;
        }
      } else {
        this.grid[current_line][i] = ' ';
      }
    }
    this.current_line++;
  }

  public void set_path(String path) {
    this.path = path;
  }

  public int index_in_path = 0;
  // Reads one instruction from path and performs it
  public void step() {
    if(this.path.charAt(this.index_in_path) == 'R') {
      this.direction = (this.direction + 1);
      this.direction = this.direction > 3? this.direction - 4 : this.direction;
      this.index_in_path++;
    } else if(this.path.charAt(this.index_in_path) == 'L') {
      this.direction = (this.direction - 1);
      this.direction = this.direction < 0? this.direction + 4 : this.direction;
      this.index_in_path++;
    } else {
      int number_len = 0;
      while(this.index_in_path + number_len < this.path.length() && isParsable(this.path.substring(this.index_in_path, this.index_in_path + number_len + 1))) {
        number_len++;
      }
      if(number_len > 0) {
        move(Integer.parseInt(this.path.substring(this.index_in_path, this.index_in_path + number_len)));
      }
      this.index_in_path += number_len;
    }
  }

  // Moves step_size steps in the current direction
  private void move(int step_size) {
    if(this.direction == 0) { // right
      while(step_size > 0 && this.x_pos < this.grid_width - 1 && this.grid[this.y_pos][this.x_pos+1] == '.') {
        this.x_pos++;
        step_size--;
      }
      int old_x_pos = this.x_pos;
      if((this.x_pos == this.grid_width-1 || this.grid[this.y_pos][this.x_pos+1] == ' ') && step_size > 0) { // possibly warp around
        this.x_pos = 0;
        while(this.grid[this.y_pos][this.x_pos] == ' ' && this.x_pos < this.grid_width - 1) {
          this.x_pos++;
        }
        if(this.x_pos < this.grid_width && this.grid[this.y_pos][this.x_pos] == '.') {
          step_size--;
          move(step_size);
        } else if(this.grid[this.y_pos][this.x_pos] == '#') { // blocked
          this.x_pos = old_x_pos;
          // do nothing
        }
      }
    } else if(this.direction == 1) { // down
      while(step_size > 0 && this.y_pos < this.grid_height - 1 && this.grid[this.y_pos+1][this.x_pos] == '.') {
        this.y_pos++;
        step_size--;
      }
      int old_y_pos = this.y_pos;
      if((this.y_pos == this.grid_height-1 || this.grid[this.y_pos+1][this.x_pos] == ' ') && step_size > 0) { // possibly warp around
        this.y_pos = 0;
        while(this.grid[this.y_pos][this.x_pos] == ' ' && this.y_pos < this.grid_height - 1) {
          this.y_pos++;
        }
        if(this.y_pos < this.grid_height && this.grid[this.y_pos][this.x_pos] == '.') {
          step_size--;
          move(step_size);
        } else if(this.grid[this.y_pos][this.x_pos] == '#') { // blocked
          this.y_pos = old_y_pos;
          // do nothing
        }
      }
    } else if(this.direction == 2) { // left
      while(step_size > 0 && this.x_pos > 0 && this.grid[this.y_pos][this.x_pos-1] == '.') {
        this.x_pos--;
        step_size--;
      }
      int old_x_pos = this.x_pos;
      if((this.x_pos == 0 || this.grid[this.y_pos][this.x_pos-1] == ' ') && step_size > 0) { // possibly warp around
        this.x_pos = this.grid_width - 1;
        while(this.grid[this.y_pos][this.x_pos] == ' ' && this.x_pos > 1) {
          this.x_pos--;
        }
        if(this.x_pos >= 0 && this.grid[this.y_pos][this.x_pos] == '.') {
          step_size--;
          move(step_size);
        } else if(this.grid[this.y_pos][this.x_pos] == '#') { // blocked
          this.x_pos = old_x_pos;
          // do nothing
        }
      }
    } else if(this.direction == 3) { // up
      while(step_size > 0 && this.y_pos > 0 && this.grid[this.y_pos-1][this.x_pos] == '.') {
        this.y_pos--;
        step_size--;
      }
      int old_y_pos = this.y_pos;
      if((this.y_pos == 0 || this.grid[this.y_pos-1][this.x_pos] == ' ') && step_size > 0) { // possibly warp around
        this.y_pos = this.grid_height - 1;
        while(this.grid[this.y_pos][this.x_pos] == ' ' && this.y_pos > 1) {
          this.y_pos--;
        }
        if(this.y_pos >= 0 && this.grid[this.y_pos][this.x_pos] == '.') {
          step_size--;
          move(step_size);
        } else if(this.grid[this.y_pos][this.x_pos] == '#') { // blocked
          this.y_pos = old_y_pos;
          // do nothing
        }
      }
    }
  }

  public void print_grid() {
    this.grid[this.y_pos][this.x_pos] = 'X';
    for(int i = 0; i < this.grid_height; i++) {
      for(int j = 0; j < this.grid_width; j++) {
        System.out.print(this.grid[i][j]);
      }
      System.out.println();
    }
    this.grid[this.y_pos][this.x_pos] = '.';
  }

  public Boolean isParsable(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch(NumberFormatException e) {
      return false;
    }
  }
  
}