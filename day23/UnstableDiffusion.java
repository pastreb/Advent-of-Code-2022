package adventofcode;

import java.util.*;
import java.io.*;

public class UnstableDiffusion {

  public static void main(String[] args) {
    Grove input = read_input(11);
    System.out.println(solve_part_1(input));
    input = read_input(100);
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 23: Unstable Diffusion ---
  You enter a large crater of gray dirt where the grove is supposed to be. 
  All around you, plants you imagine were expected to be full of fruit are 
  instead withered and broken. A large group of Elves has formed in the 
  middle of the grove.

  "...but this volcano has been dormant for months. Without ash, the fruit 
  can't grow!"

  You look up to see a massive, snow-capped mountain towering above you.

  "It's not like there are other active volcanoes here; we've looked 
  everywhere."

  "But our scanners show active magma flows; clearly it's going somewhere."

  They finally notice you at the edge of the grove, your pack almost 
  overflowing from the random star fruit you've been collecting. Behind you, 
  elephants and monkeys explore the grove, looking concerned. Then, the Elves 
  recognize the ash cloud slowly spreading above your recent detour.

  "Why do you--" "How is--" "Did you just--"

  Before any of them can form a complete question, another Elf speaks up: 
  "Okay, new plan. We have almost enough fruit already, and ash from the 
  plume should spread here eventually. If we quickly plant new seedlings now, 
  we can still make it to the extraction point. Spread out!"

  The Elves each reach into their pack and pull out a tiny plant. The plants 
  rely on important nutrients from the ash, so they can't be planted too 
  close together.

  There isn't enough time to let the Elves figure out where to plant the 
  seedlings themselves; you quickly scan the grove (your puzzle input) and 
  note their positions.

  For example:

  ....#..
  ..###.#
  #...#.#
  .#...##
  #.###..
  ##.#.##
  .#..#..
  
  The scan shows Elves # and empty ground .; outside your scan, more empty 
  ground extends a long way in every direction. The scan is oriented so that 
  north is up; orthogonal directions are written N (north), S (south), W (west), 
  and E (east), while diagonal directions are written NE, NW, SE, SW.

  The Elves follow a time-consuming process to figure out where they should 
  each go; you can speed up this process considerably. The process consists 
  of some number of rounds during which Elves alternate between considering 
  where to move and actually moving.

  During the first half of each round, each Elf considers the eight positions 
  adjacent to themself. If no other Elves are in one of those eight 
  positions, the Elf does not do anything during this round. Otherwise, the 
  Elf looks in each of four directions in the following order and proposes 
  moving one step in the first valid direction:

  - If there is no Elf in the N, NE, or NW adjacent positions, the Elf 
    proposes moving north one step.
  - If there is no Elf in the S, SE, or SW adjacent positions, the Elf 
    proposes moving south one step.
  - If there is no Elf in the W, NW, or SW adjacent positions, the Elf 
    proposes moving west one step.
  - If there is no Elf in the E, NE, or SE adjacent positions, the Elf 
    proposes moving east one step.
  
  After each Elf has had a chance to propose a move, the second half of the 
  round can begin. Simultaneously, each Elf moves to their proposed 
  destination tile if they were the only Elf to propose moving to that 
  position. If two or more Elves propose moving to the same position, 
  none of those Elves move.

  Finally, at the end of the round, the first direction the Elves considered 
  is moved to the end of the list of directions. For example, during the 
  second round, the Elves would try proposing a move to the south first, then 
  west, then east, then north. On the third round, the Elves would first 
  consider west, then east, then north, then south.

  As a smaller example, consider just these five Elves:

  .....
  ..##.
  ..#..
  .....
  ..##.
  .....
  
  The northernmost two Elves and southernmost two Elves all propose moving 
  north, while the middle Elf cannot move north and proposes moving south. 
  The middle Elf proposes the same destination as the southwest Elf, so 
  neither of them move, but the other three do:

  ..##.
  .....
  ..#..
  ...#.
  ..#..
  .....
  
  Next, the northernmost two Elves and the southernmost Elf all propose 
  moving south. Of the remaining middle two Elves, the west one cannot move 
  south and proposes moving west, while the east one cannot move south or 
  west and proposes moving east. All five Elves succeed in moving to their 
  proposed positions:

  .....
  ..##.
  .#...
  ....#
  .....
  ..#..
  
  Finally, the southernmost two Elves choose not to move at all. Of the 
  remaining three Elves, the west one proposes moving west, the east one 
  proposes moving east, and the middle one proposes moving north; all three 
  succeed in moving:

  ..#..
  ....#
  #....
  ....#
  .....
  ..#..
  
  At this point, no Elves need to move, and so the process ends.

  The larger example above proceeds as follows:

  == Initial State ==
  ..............
  ..............
  .......#......
  .....###.#....
  ...#...#.#....
  ....#...##....
  ...#.###......
  ...##.#.##....
  ....#..#......
  ..............
  ..............
  ..............

  == End of Round 1 ==
  ..............
  .......#......
  .....#...#....
  ...#..#.#.....
  .......#..#...
  ....#.#.##....
  ..#..#.#......
  ..#.#.#.##....
  ..............
  ....#..#......
  ..............
  ..............

  == End of Round 2 ==
  ..............
  .......#......
  ....#.....#...
  ...#..#.#.....
  .......#...#..
  ...#..#.#.....
  .#...#.#.#....
  ..............
  ..#.#.#.##....
  ....#..#......
  ..............
  ..............

  == End of Round 3 ==
  ..............
  .......#......
  .....#....#...
  ..#..#...#....
  .......#...#..
  ...#..#.#.....
  .#..#.....#...
  .......##.....
  ..##.#....#...
  ...#..........
  .......#......
  ..............

  == End of Round 4 ==
  ..............
  .......#......
  ......#....#..
  ..#...##......
  ...#.....#.#..
  .........#....
  .#...###..#...
  ..#......#....
  ....##....#...
  ....#.........
  .......#......
  ..............

  == End of Round 5 ==
  .......#......
  ..............
  ..#..#.....#..
  .........#....
  ......##...#..
  .#.#.####.....
  ...........#..
  ....##..#.....
  ..#...........
  ..........#...
  ....#..#......
  ..............
  After a few more rounds...

  == End of Round 10 ==
  .......#......
  ...........#..
  ..#.#..#......
  ......#.......
  ...#.....#..#.
  .#......##....
  .....##.......
  ..#........#..
  ....#.#..#....
  ..............
  ....#..#..#...
  ..............
  
  To make sure they're on the right track, the Elves like to check after 
  round 10 that they're making good progress toward covering enough ground. 
  To do this, count the number of empty ground tiles contained by the 
  smallest rectangle that contains every Elf. (The edges of the rectangle 
  should be aligned to the N/S/E/W directions; the Elves do not have the 
  patience to calculate arbitrary rectangles.) In the above example, that 
  rectangle is:

  ......#.....
  ..........#.
  .#.#..#.....
  .....#......
  ..#.....#..#
  #......##...
  ....##......
  .#........#.
  ...#.#..#...
  ............
  ...#..#..#..
  
  In this region, the number of empty ground tiles is 110.

  Simulate the Elves' process and find the smallest rectangle that contains 
  the Elves after 10 rounds. How many empty ground tiles does that rectangle 
  contain?
  */

  public static int solve_part_1(Grove grove) {
    for(int i = 0; i < 10; i++) {
      grove.perform_round();
    }
    return grove.get_empty_ground_tiles_in_min_rectangle();
  }

  /*
  --- Part Two ---
  It seems you're on the right track. Finish simulating the process and 
  figure out where the Elves need to go. How many rounds did you save them?

  In the example above, the first round where no Elf moved was round 20:

  .......#......
  ....#......#..
  ..#.....#.....
  ......#.......
  ...#....#.#..#
  #.............
  ....#.....#...
  ..#.....#.....
  ....#.#....#..
  .........#....
  ....#......#..
  .......#......
  
  Figure out where the Elves need to go. What is the number of the first 
  round where no Elf moves?
  */

  public static int solve_part_2(Grove grove) {
    int round_count = 0;
    while(!grove.perform_round()) {
      round_count++;
    }
    return round_count + 1;
  }

  public static Grove read_input(int offset) {
    Grove grove = null;
    try {
      File input = new File("input.txt");
      // First, we want to determine the with and height of the grove:
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
      grove = new Grove(width, height, offset);
      scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        grove.add_row(line);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return grove;
  }
}

public class Grove {
  Character[][] grove;
  private int offset; // space around grove; prevents going out of bounds
  List<Position_Offset_Triple> directions_to_consider = new LinkedList<Position_Offset_Triple>();

  public Grove(int width, int height, int offset) {
    this.grove = new Character[height + 2*offset][width + 2*offset];
    this.offset = offset;
    this.next_row = offset;
    for(int i = 0; i < grove.length; i++) {
      for(int j = 0; j < grove[0].length; j++) {
        grove[i][j] = '.';
      }
    }
    this.directions_to_consider.add(new Position_Offset_Triple(-1, 0, -1, -1, -1, 1)); // North
    this.directions_to_consider.add(new Position_Offset_Triple(+1, 0, +1, -1, +1, 1)); // South
    this.directions_to_consider.add(new Position_Offset_Triple(0, -1, 1, -1, -1, -1)); // West
    this.directions_to_consider.add(new Position_Offset_Triple(0, 1, 1, 1, -1, 1)); // East
  }

  private int next_row;
  public void add_row(String line) {
    for(int i = 0; i < line.length(); i++) {
      if(line.charAt(i) == '#') {
        this.grove[this.next_row][i+this.offset] = '#';
      }
    }
    this.next_row++;
  }

  public void print_grove() {
    for(int i = 0; i < this.grove.length; i++) {
      for(int j = 0; j < this.grove[0].length; j++) {
        System.out.print(this.grove[i][j]);
      }
      System.out.println();
    }
    System.out.println();
  }

  private int round_number = 0;
  // Returns true if no elf moved in this round
  public boolean perform_round() {
    // Allocate space to remember the count the number of times each position is proposed:
    int[][] proposals = new int[this.grove.length][this.grove[0].length];
    for(int i = 0; i < proposals.length; i++) {
      for(int j = 0; j < proposals[0].length; j++) {
        proposals[i][j] = 0;
      }
    }
    // Also, we must remember which position is proposed by which elf:
    Map<Integer, My_Pair> proposed_positions_by_elf = new HashMap<Integer, My_Pair>();
    // First half of the round:
    for(int i = 0; i < this.grove.length; i++) {
      for(int j = 0; j < this.grove[0].length; j++) {
        if(this.grove[i][j] == '#') {
          proposed_positions_by_elf.put(i*this.grove.length+j, null);
          // First, we check whether we are currently at a position where we don't have to move:
          boolean safe = true;
          for(int k = 0; k < 4; k++) { // Propose a direction
            if(grove[i+this.directions_to_consider.get(k).y1][j+this.directions_to_consider.get(k).x1] == '#' 
             || grove[i+this.directions_to_consider.get(k).y2][j+this.directions_to_consider.get(k).x2] == '#' 
             || grove[i+this.directions_to_consider.get(k).y3][j+this.directions_to_consider.get(k).x3] == '#') {
              safe = false;
              break;
            }
          }
          if(safe) {
            continue;
          }
          // If not, we next check whether we can actually move:
          for(int k = 0; k < 4; k++) { // Propose a direction
            if(grove[i+this.directions_to_consider.get(k).y1][j+this.directions_to_consider.get(k).x1] == '.' 
             && grove[i+this.directions_to_consider.get(k).y2][j+this.directions_to_consider.get(k).x2] == '.' 
             && grove[i+this.directions_to_consider.get(k).y3][j+this.directions_to_consider.get(k).x3] == '.') {
              proposals[i+this.directions_to_consider.get(k).y1][j+this.directions_to_consider.get(k).x1]++;
              proposed_positions_by_elf.put(i*this.grove.length+j, new My_Pair(i+this.directions_to_consider.get(k).y1, j+this.directions_to_consider.get(k).x1));
              break;
            }
          }
        }
      }
    }
    // We cannot update the grove in place, thus we need a copy:
    Character[][] updated_grove = new Character[this.grove.length][this.grove[0].length];
    for(int i = 0; i < this.grove.length; i++) {
      for(int j = 0; j < this.grove[0].length; j++) {
        updated_grove[i][j] = '.';
      }
    }
    // Second half of the round:
    boolean no_elf_moved = true;
    for(int i = 0; i < this.grove.length; i++) {
      for(int j = 0; j < this.grove[0].length; j++) {
        if(this.grove[i][j] == '#') { // there is an elf that can possibly move
          My_Pair proposed_position = proposed_positions_by_elf.get(i*this.grove.length+j);
          if(proposed_position != null && proposals[proposed_position.first][proposed_position.second] == 1) { // move
            updated_grove[proposed_position.first][proposed_position.second] = '#';
            no_elf_moved = false;
          } else {
            updated_grove[i][j] = '#';
          }
        }
      }
    }
    // Update grove:
    this.grove = updated_grove;
    // Rotate directions to consider:
    this.directions_to_consider.add(3, this.directions_to_consider.remove(0));
    return no_elf_moved;
  }

  public int get_empty_ground_tiles_in_min_rectangle() {
    int min_x = Integer.MAX_VALUE;
    int max_x = Integer.MIN_VALUE;
    int min_y = Integer.MAX_VALUE;
    int max_y = Integer.MIN_VALUE;
    // Find min rectangle:
    for(int i = 0; i < this.grove.length; i++) {
      for(int j = 0; j < this.grove[0].length; j++) {
        if(this.grove[i][j] == '#') {
          min_x = Math.min(min_x, j);
          max_x = Math.max(max_x, j);
          min_y = Math.min(min_y, i);
          max_y = Math.max(max_y, i);
        }
      }
    }
    // Count empty tiles:
    int count = 0;
    for(int i = min_y; i <= max_y; i++) {
      for(int j = min_x; j <= max_x; j++) {
        count += this.grove[i][j] == '.' ? 1 : 0;
      }
    }
    return count;
  }
}

class Position_Offset_Triple {
  public int x1;
  public int y1;
  public int x2;
  public int y2;
  public int x3;
  public int y3;

  public Position_Offset_Triple(int y1, int x1, int y2, int x2, int y3, int x3) {
    this.y1 = y1;
    this.x1 = x1;
    this.y2 = y2;
    this.x2 = x2;
    this.y3 = y3;
    this.x3 = x3;
  }
}

class My_Pair {
  public int first;
  public int second;

  public My_Pair(int first, int second) {
    this.first = first;
    this.second = second;
  }
}