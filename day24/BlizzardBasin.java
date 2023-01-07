package adventofcode;

import java.util.*;
import java.io.*;

public class BlizzardBasin {

  public static void main(String[] args) {
    Blizzard input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 24: Blizzard Basin ---
  With everything replanted for next year (and with elephants and monkeys to 
  tend the grove), you and the Elves leave for the extraction point.

  Partway up the mountain that shields the grove is a flat, open area that 
  serves as the extraction point. It's a bit of a climb, but nothing the 
  expedition can't handle.

  At least, that would normally be true; now that the mountain is covered in 
  snow, things have become more difficult than the Elves are used to.

  As the expedition reaches a valley that must be traversed to reach the 
  extraction site, you find that strong, turbulent winds are pushing small 
  blizzards of snow and sharp ice around the valley. It's a good thing 
  everyone packed warm clothes! To make it across safely, you'll need to find 
  a way to avoid them.

  Fortunately, it's easy to see all of this from the entrance to the valley, 
  so you make a map of the valley and the blizzards (your puzzle input). 
  For example:

  #.#####
  #.....#
  #>....#
  #.....#
  #...v.#
  #.....#
  #####.#
  
  The walls of the valley are drawn as #; everything else is ground. Clear 
  ground - where there is currently no blizzard - is drawn as .
  Otherwise, blizzards are drawn with an arrow indicating their direction of 
  motion: up (^), down (v), left (<), or right (>).

  The above map includes two blizzards, one moving right (>) and one moving 
  down (v). In one minute, each blizzard moves one position in the direction 
  it is pointing:

  #.#####
  #.....#
  #.>...#
  #.....#
  #.....#
  #...v.#
  #####.#
  
  Due to conservation of blizzard energy, as a blizzard reaches the wall of 
  the valley, a new blizzard forms on the opposite side of the valley moving 
  in the same direction. After another minute, the bottom downward-moving 
  blizzard has been replaced with a new downward-moving blizzard at the top 
  of the valley instead:

  #.#####
  #...v.#
  #..>..#
  #.....#
  #.....#
  #.....#
  #####.#
  
  Because blizzards are made of tiny snowflakes, they pass right through each 
  other. After another minute, both blizzards temporarily occupy the same 
  position, marked 2:

  #.#####
  #.....#
  #...2.#
  #.....#
  #.....#
  #.....#
  #####.#
  
  After another minute, the situation resolves itself, giving each blizzard 
  back its personal space:

  #.#####
  #.....#
  #....>#
  #...v.#
  #.....#
  #.....#
  #####.#
  
  Finally, after yet another minute, the rightward-facing blizzard on the 
  right is replaced with a new one on the left facing the same direction:

  #.#####
  #.....#
  #>....#
  #.....#
  #...v.#
  #.....#
  #####.#
  
  This process repeats at least as long as you are observing it, but probably 
  forever.

  Here is a more complex example:

  #.######
  #>>.<^<#
  #.<..<<#
  #>v.><>#
  #<^v^^>#
  ######.#
  
  Your expedition begins in the only non-wall position in the top row and 
  needs to reach the only non-wall position in the bottom row. On each 
  minute, you can move up, down, left, or right, or you can wait in place. 
  You and the blizzards act simultaneously, and you cannot share a position 
  with a blizzard.

  In the above example, the fastest way to reach your goal requires 18 steps. 
  Drawing the position of the expedition as E, one way to achieve this is:

  Initial state:
  #E######
  #>>.<^<#
  #.<..<<#
  #>v.><>#
  #<^v^^>#
  ######.#

  Minute 1, move down:
  #.######
  #E>3.<.#
  #<..<<.#
  #>2.22.#
  #>v..^<#
  ######.#

  Minute 2, move down:
  #.######
  #.2>2..#
  #E^22^<#
  #.>2.^>#
  #.>..<.#
  ######.#

  Minute 3, wait:
  #.######
  #<^<22.#
  #E2<.2.#
  #><2>..#
  #..><..#
  ######.#

  Minute 4, move up:
  #.######
  #E<..22#
  #<<.<..#
  #<2.>>.#
  #.^22^.#
  ######.#

  Minute 5, move right:
  #.######
  #2Ev.<>#
  #<.<..<#
  #.^>^22#
  #.2..2.#
  ######.#

  Minute 6, move right:
  #.######
  #>2E<.<#
  #.2v^2<#
  #>..>2>#
  #<....>#
  ######.#

  Minute 7, move down:
  #.######
  #.22^2.#
  #<vE<2.#
  #>>v<>.#
  #>....<#
  ######.#

  Minute 8, move left:
  #.######
  #.<>2^.#
  #.E<<.<#
  #.22..>#
  #.2v^2.#
  ######.#

  Minute 9, move up:
  #.######
  #<E2>>.#
  #.<<.<.#
  #>2>2^.#
  #.v><^.#
  ######.#

  Minute 10, move right:
  #.######
  #.2E.>2#
  #<2v2^.#
  #<>.>2.#
  #..<>..#
  ######.#

  Minute 11, wait:
  #.######
  #2^E^2>#
  #<v<.^<#
  #..2.>2#
  #.<..>.#
  ######.#

  Minute 12, move down:
  #.######
  #>>.<^<#
  #.<E.<<#
  #>v.><>#
  #<^v^^>#
  ######.#

  Minute 13, move down:
  #.######
  #.>3.<.#
  #<..<<.#
  #>2E22.#
  #>v..^<#
  ######.#

  Minute 14, move right:
  #.######
  #.2>2..#
  #.^22^<#
  #.>2E^>#
  #.>..<.#
  ######.#

  Minute 15, move right:
  #.######
  #<^<22.#
  #.2<.2.#
  #><2>E.#
  #..><..#
  ######.#

  Minute 16, move right:
  #.######
  #.<..22#
  #<<.<..#
  #<2.>>E#
  #.^22^.#
  ######.#

  Minute 17, move down:
  #.######
  #2.v.<>#
  #<.<..<#
  #.^>^22#
  #.2..2E#
  ######.#

  Minute 18, move down:
  #.######
  #>2.<.<#
  #.2v^2<#
  #>..>2>#
  #<....>#
  ######E#
  
  What is the fewest number of minutes required to avoid the blizzards and reach the goal?
  */

  public static int solve_part_1(Blizzard blizzard) {

    boolean[][] positions = new boolean[blizzard.height][blizzard.width];
    for(int i = 0; i < blizzard.height; i++) {
      for(int j = 0; j < blizzard.width; j++) {
        positions[i][j] = false;
      }
    }
    positions[0][1] = true; // start position
    int time = 0;
    // positions[i][j] stores true if at the current value of time, it is possible to be at position (i, j).
    
    while(true) {
      // print(positions);
      time++;
      boolean[][] next_positions = new boolean[blizzard.height][blizzard.width];
      next_positions[0][1] = true; // start position
      String[][] next_blizzard_positions = blizzard.blizzard_state_by_minute.get(time%blizzard.blizzard_state_by_minute.size());
      for(int i = 1; i < blizzard.height; i++) {
        for(int j = 1; j < blizzard.width-1; j++) {
          if(i == blizzard.height-1) {
            if(j == blizzard.width-2 && positions[i-1][j]) { // We reached the goal
              return time;
            }
          } else if(next_blizzard_positions[i][j].equals(".") && (positions[i][j] || positions[i-1][j] || positions[i+1][j] || positions[i][j-1] || positions[i][j+1])) {
            // There is no blizzard at the considered position and we were at a neighbor (or already here) just before
            next_positions[i][j] = true;
            
          } else {
            next_positions[i][j] = false;
          }
        }
      }
      positions = next_positions;
    }
  }

  public static void print(boolean[][] positions) {
    for(int i = 0; i < positions.length; i++) {
      for(int j = 0; j < positions[0].length; j++) {
        if(positions[i][j]) {
          System.out.print(1);
        } else {
          System.out.print(0); 
        }
      }
      System.out.println();
    }
    System.out.println();
  }

  /*
  --- Part Two ---
  
  */

  public static int solve_part_2(Blizzard blizzard) {
    return 0;
  }

  public static Blizzard read_input() {
    Blizzard blizzard = null;
    try {
      File input = new File("input.txt");
      // First, we want to determine the with and height of the blizzard:
      Scanner scanner = new Scanner(input);
      int width = 0;
      int height = 0;
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        width = Math.max(width, line.length());
        height++;
      }
      scanner.close();
      // Next, we actually read the input:
      blizzard = new Blizzard(width, height);
      scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        blizzard.add_row(line);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    blizzard.generate_all_blizzard_states();
    return blizzard;
  }
}

class Blizzard {
  public Map<Integer, String[][]> blizzard_state_by_minute;
  public int width;
  public int height;

  public Blizzard(int width, int height) {
    this.blizzard_state_by_minute = new HashMap<Integer, String[][]>();
    this.blizzard_state_by_minute.put(0, new String[height][width]);
    this.width = width;
    this.height = height;
  }

  private int current_line_number = 0;
  public void add_row(String line) {
    for(int i = 0; i < line.length(); i++) {
      this.blizzard_state_by_minute.get(0)[this.current_line_number][i] = line.substring(i, i+1);
    }
    this.current_line_number++;
  }

  public void generate_all_blizzard_states() {
    String[][] current_state = this.blizzard_state_by_minute.get(0);
    int next_state_number = 1;
    while(true) {
      String[][] next_state = this.add_new_blizzard_state(next_state_number);
      // Determine new positions of blizzards:
      for(int i = 0; i < this.height; i++) {
        for(int j = 0; j < this.width; j++) {
          if(next_state[i][j] == "#") {
            continue;
          }
          // Blizzard going up:
          if((i < height-2 && current_state[i+1][j].contains("^")) || (i == height-2 && current_state[1][j].contains("^"))) {
            next_state[i][j] += "^";
          }
          // Blizzard going right:
          if((j > 1 && current_state[i][j-1].contains(">")) || (j == 1 && current_state[i][width-2].contains(">"))) {
            next_state[i][j] += ">";
          }
          // Blizzard going down:
          if((i > 1 && current_state[i-1][j].contains("v")) || (i == 1 && current_state[height-2][j].contains("v"))) {
            next_state[i][j] += "v";
          }
          // Blizzard going left:
          if((j < width-2 && current_state[i][j+1].contains("<")) || (j == width-2 && current_state[i][1].contains("<"))) {
            next_state[i][j] += "<";
          }
        }
      }
      // Postprocess new blizzard state:
      for(int i = 0; i < this.height; i++) {
        for(int j = 0; j < this.width; j++) {
          next_state[i][j] = sort_direction_string(next_state[i][j]);
        }
      }
      // Check whether this state was already encountered:
      if(check_equal_states(0, next_state_number)) {
        break;
      }
      current_state = next_state;
      next_state_number++;
    }    
  }

  private String[][] add_new_blizzard_state(int state_number) {
    String[][] new_state = new String[height][width];
    String[][] init_state = this.blizzard_state_by_minute.get(0);
    // Initialize
    for(int i = 0; i < this.height; i++) {
      for(int j = 0; j < this.width; j++) {
        new_state[i][j] = init_state[i][j].equals("#")? "#" : ".";
      }
    }
    this.blizzard_state_by_minute.put(state_number, new_state);
    return new_state;
  }

  private String sort_direction_string(String s) {
    String new_s = "";
    for(String dir : new String[]{"<", ">", "^", "v"}) { // order in which we want them
      if(s.contains(dir)) {
        new_s += dir;
      }
    }
    if(new_s.length() == 0) {
      return s;
    }
    return new_s;
  }

  private boolean check_equal_states(int a, int b) {
    String[][] state_1 = this.blizzard_state_by_minute.get(a);
    String[][] state_2 = this.blizzard_state_by_minute.get(b);
    for(int i = 0; i < this.height; i++) {
      for(int j = 0; j < this.width; j++) {
        if(!state_1[i][j].equals(state_2[i][j])) {
          return false;
        }
      }
    }
    return true;
  }

  public void print_blizzard_state(int key) {
    String[][] state = this.blizzard_state_by_minute.get(key);
    for(int i = 0; i < this.height; i++) {
      for(int j = 0; j < this.width; j++) {
        System.out.print(state[i][j] + " ");
      }
      System.out.println();
    }
  }
}