package adventofcode;

import java.util.*;
import java.io.*;

public class PyroclasticFlow {

  public static void main(String[] args) {
    List<Character> input = read_input();
    System.out.println(solve_part_1(input));
  }

  /*
  --- Day 17: Pyroclastic Flow ---
  Your handheld device has located an alternative exit from the cave for you 
  and the elephants. The ground is rumbling almost continuously now, but the 
  strange valves bought you some time. It's definitely getting warmer in 
  here, though.

  The tunnels eventually open into a very tall, narrow chamber. Large, oddly-
  shaped rocks are falling into the chamber from above, presumably due to all 
  the rumbling. If you can't work out where the rocks will fall next, you 
  might be crushed!

  The five types of rocks have the following peculiar shapes, where # is rock 
  and . is empty space:

  ####

  .#.
  ###
  .#.

  ..#
  ..#
  ###

  #
  #
  #
  #

  ##
  ##
  
  The rocks fall in the order shown above: first the - shape, then the + 
  shape, and so on. Once the end of the list is reached, the same order 
  repeats: the - shape falls first, sixth, 11th, 16th, etc.

  The rocks don't spin, but they do get pushed around by jets of hot gas 
  coming out of the walls themselves. A quick scan reveals the effect the 
  jets of hot gas will have on the rocks as they fall (your puzzle input).

  For example, suppose this was the jet pattern in your cave:

  >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
  
  In jet patterns, < means a push to the left, while > means a push to the 
  right. The pattern above means that the jets will push a falling rock 
  right, then right, then right, then left, then left, then right, and so on. 
  If the end of the list is reached, it repeats.

  The tall, vertical chamber is exactly seven units wide. Each rock appears 
  so that its left edge is two units away from the left wall and its bottom 
  edge is three units above the highest rock in the room (or the floor, if 
  there isn't one).

  After a rock appears, it alternates between being pushed by a jet of hot 
  gas one unit (in the direction indicated by the next symbol in the jet 
  pattern) and then falling one unit down. If any movement would cause any 
  part of the rock to move into the walls, floor, or a stopped rock, the 
  movement instead does not occur. If a downward movement would have caused a 
  falling rock to move into the floor or an already-fallen rock, the falling 
  rock stops where it is (having landed on something) and a new rock 
  immediately begins falling.

  Drawing falling rocks with @ and stopped rocks with #, the jet pattern in 
  the example above manifests as follows:

  The first rock begins falling:
  |..@@@@.|
  |.......|
  |.......|
  |.......|
  +-------+

  Jet of gas pushes rock right:
  |...@@@@|
  |.......|
  |.......|
  |.......|
  +-------+

  Rock falls 1 unit:
  |...@@@@|
  |.......|
  |.......|
  +-------+

  Jet of gas pushes rock right, but nothing happens:
  |...@@@@|
  |.......|
  |.......|
  +-------+

  Rock falls 1 unit:
  |...@@@@|
  |.......|
  +-------+

  Jet of gas pushes rock right, but nothing happens:
  |...@@@@|
  |.......|
  +-------+

  Rock falls 1 unit:
  |...@@@@|
  +-------+

  Jet of gas pushes rock left:
  |..@@@@.|
  +-------+

  Rock falls 1 unit, causing it to come to rest:
  |..####.|
  +-------+

  A new rock begins falling:
  |...@...|
  |..@@@..|
  |...@...|
  |.......|
  |.......|
  |.......|
  |..####.|
  +-------+

  Jet of gas pushes rock left:
  |..@....|
  |.@@@...|
  |..@....|
  |.......|
  |.......|
  |.......|
  |..####.|
  +-------+

  Rock falls 1 unit:
  |..@....|
  |.@@@...|
  |..@....|
  |.......|
  |.......|
  |..####.|
  +-------+

  Jet of gas pushes rock right:
  |...@...|
  |..@@@..|
  |...@...|
  |.......|
  |.......|
  |..####.|
  +-------+

  Rock falls 1 unit:
  |...@...|
  |..@@@..|
  |...@...|
  |.......|
  |..####.|
  +-------+

  Jet of gas pushes rock left:
  |..@....|
  |.@@@...|
  |..@....|
  |.......|
  |..####.|
  +-------+

  Rock falls 1 unit:
  |..@....|
  |.@@@...|
  |..@....|
  |..####.|
  +-------+

  Jet of gas pushes rock right:
  |...@...|
  |..@@@..|
  |...@...|
  |..####.|
  +-------+

  Rock falls 1 unit, causing it to come to rest:
  |...#...|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  A new rock begins falling:
  |....@..|
  |....@..|
  |..@@@..|
  |.......|
  |.......|
  |.......|
  |...#...|
  |..###..|
  |...#...|
  |..####.|
  +-------+
  The moment each of the next few rocks begins falling, you would see this:

  |..@....|
  |..@....|
  |..@....|
  |..@....|
  |.......|
  |.......|
  |.......|
  |..#....|
  |..#....|
  |####...|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  |..@@...|
  |..@@...|
  |.......|
  |.......|
  |.......|
  |....#..|
  |..#.#..|
  |..#.#..|
  |#####..|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  |..@@@@.|
  |.......|
  |.......|
  |.......|
  |....##.|
  |....##.|
  |....#..|
  |..#.#..|
  |..#.#..|
  |#####..|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  |...@...|
  |..@@@..|
  |...@...|
  |.......|
  |.......|
  |.......|
  |.####..|
  |....##.|
  |....##.|
  |....#..|
  |..#.#..|
  |..#.#..|
  |#####..|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  |....@..|
  |....@..|
  |..@@@..|
  |.......|
  |.......|
  |.......|
  |..#....|
  |.###...|
  |..#....|
  |.####..|
  |....##.|
  |....##.|
  |....#..|
  |..#.#..|
  |..#.#..|
  |#####..|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  |..@....|
  |..@....|
  |..@....|
  |..@....|
  |.......|
  |.......|
  |.......|
  |.....#.|
  |.....#.|
  |..####.|
  |.###...|
  |..#....|
  |.####..|
  |....##.|
  |....##.|
  |....#..|
  |..#.#..|
  |..#.#..|
  |#####..|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  |..@@...|
  |..@@...|
  |.......|
  |.......|
  |.......|
  |....#..|
  |....#..|
  |....##.|
  |....##.|
  |..####.|
  |.###...|
  |..#....|
  |.####..|
  |....##.|
  |....##.|
  |....#..|
  |..#.#..|
  |..#.#..|
  |#####..|
  |..###..|
  |...#...|
  |..####.|
  +-------+

  |..@@@@.|
  |.......|
  |.......|
  |.......|
  |....#..|
  |....#..|
  |....##.|
  |##..##.|
  |######.|
  |.###...|
  |..#....|
  |.####..|
  |....##.|
  |....##.|
  |....#..|
  |..#.#..|
  |..#.#..|
  |#####..|
  |..###..|
  |...#...|
  |..####.|
  +-------+
  
  To prove to the elephants your simulation is accurate, they want to know 
  how tall the tower will get after 2022 rocks have stopped (but before the 
  2023rd rock begins falling). In this example, the tower of rocks will be 
  3068 units tall.

  How many units tall will the tower of rocks be after 2022 rocks have 
  stopped falling?
  */

  public static int solve_part_1(List<Character> input) {
    int n_rocks = 2022;
    int height = n_rocks * 5 + 50;
    int width = 7;
    String[][] grid = new String[height][width];
    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        grid[i][j] = ".";
      }
    }
    
    int pattern_i = 0;
    int max_y = -1;
    for(int i = 0; i < n_rocks; i++) {
      Rock rock = new Rock(((int) i%5), max_y + 4);
      while(true) {
        // Consider Jet of Gas:
        if(input.get(pattern_i) == '<') {
          rock.move_left(grid);
        } else {
          rock.move_right(grid);
        }
        pattern_i = (pattern_i+1)%input.size();
        // Consider falling:
        if(!rock.move_down(grid)) {
          // Store positions:
          for(Coord coord : rock.positions) {
            grid[coord.y][coord.x] = "#";
            max_y = Math.max(max_y, coord.y);
          }
          break;
        }
      }
    }
    return max_y+1;
  }

  public static void print_grid(String[][] grid) {
    for(int i = grid.length-1; i >= 0; i--) {
      for(int j = 0; j < grid[i].length; j++) {
        System.out.print(grid[i][j]);
      }
      System.out.println();
    }
    System.out.println();
  }

  public static int cleanup_grid(String[][] grid, int normalized_max_y) {
    int height = grid.length;
    int width = grid[0].length;
    // Find the highest row that is full:
    int row = height-1;
    while(row >= 0) {
      boolean row_blocked = true;
      for(int x = 0; x < width; x++) {
        if(grid[row][x].equals(".")) {
          row_blocked = false;
        }
      }
      if(row_blocked) {
        break;
      }
      row--;
    }
    if(row == -1) {
      return normalized_max_y;
    }
    System.out.println("row: " + row);
    // Only keep stuff that is higher than row
    for(int i = 0; i < height-row; i++) {
      for(int j = 0; j < width; j++) {
        grid[i][j] = grid[row+i][j];
      }
    }
    for(int i = height-row; i < height; i++) {
      for(int j = 0; j < width; j++) {
        grid[i][j] = ".";
      }
    }
    return normalized_max_y = height-row-1;
  }

  /*
  --- Part Two ---
  The elephants are not impressed by your simulation. They demand to know how 
  tall the tower will be after 1000000000000 rocks have stopped! Only then 
  will they feel confident enough to proceed through the cave.

  In the example above, the tower would be 1514285714288 units tall!

  How tall will the tower be after 1000000000000 rocks have stopped?

  -> The key insight here is that we cannot keep the whole thing.
     Instead, we can discard the rows that "cannot be reached" anymore
     (due to blocking Rocks) and use a smaller matrix.
  */

  public static List<Character> read_input() {
    List<Character> input_list = new ArrayList<Character>();
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        for(int i = 0; i < line.length(); i++) {
          input_list.add(line.charAt(i));
        }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class Rock {
  
  public Coord[] positions;
  
  public Rock(int type, int start_y) {
    switch(type) {
      case 0: 
        // ####
        this.positions = new Coord[4];
        for(int i = 0; i < 4; i++) {
          this.positions[i] = new Coord(2 + i, start_y);
        }
        return;
      
      case 1: 
        // .#.
        // ###
        // .#.
        this.positions = new Coord[5];
        this.positions[0] = new Coord(3, start_y + 2);
        this.positions[1] = new Coord(2, start_y + 1);
        this.positions[2] = new Coord(3, start_y + 1);
        this.positions[3] = new Coord(4, start_y + 1);
        this.positions[4] = new Coord(3, start_y);
        return;
      
      case 2:
        // ..#
        // ..#
        // ###
        this.positions = new Coord[5];
        this.positions[0] = new Coord(4, start_y + 2);
        this.positions[1] = new Coord(4, start_y + 1);
        this.positions[2] = new Coord(4, start_y);
        this.positions[3] = new Coord(3, start_y);
        this.positions[4] = new Coord(2, start_y);
        return;
      
      case 3:
        // #
        // #
        // #
        // #
        this.positions = new Coord[4];
        for(int i = 0; i < 4; i++) {
          this.positions[i] = new Coord(2, start_y + i);
        }
        return;
      
      case 4:
        // ##
        // ##
        this.positions = new Coord[4];
        this.positions[0] = new Coord(2, start_y + 1);
        this.positions[1] = new Coord(3, start_y + 1);
        this.positions[2] = new Coord(2, start_y);
        this.positions[3] = new Coord(3, start_y);
        return;

      default:
        // do nothing
    }
  }

  public void move_left(String[][] grid) {
    for(Coord position : this.positions) {
      if(position.x - 1 < 0 || grid[position.y][position.x - 1] != ".") {
        return;
      }
    }
    // Did not return, so movement must be safe
    for(Coord position : this.positions) {
      position.x--;
    }
  }

  public void move_right(String[][] grid) {
    for(Coord position : this.positions) {
      if(position.x + 1 >= grid[0].length || grid[position.y][position.x + 1] != ".") {
        return;
      }
    }
    // Did not return, so movement must be safe
    for(Coord position : this.positions) {
      position.x++;
    }
  }

  public boolean move_down(String[][] grid) {
    for(Coord position : this.positions) {
      if(position.y - 1 < 0 || grid[position.y - 1][position.x] != ".") {
        return false;
      }
    }
    // Did not return, so movement must be safe
    for(Coord position : this.positions) {
      position.y--;
    }
    return true;
  }
}

class Coord {
  public int x;
  public int y;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }
}