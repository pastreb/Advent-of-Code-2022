package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class RegolithReservoir {

  public static void main(String[] args) {
    List<List<Point>> input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println();
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 14: Regolith Reservoir ---
  The distress signal leads you to a giant waterfall! Actually, hang on - the 
  signal seems like it's coming from the waterfall itself, and that doesn't 
  make any sense. However, you do notice a little path that leads behind the 
  waterfall.

  Correction: the distress signal leads you behind a giant waterfall! There 
  seems to be a large cave system here, and the signal definitely leads 
  further inside.

  As you begin to make your way deeper underground, you feel the ground 
  rumble for a moment. Sand begins pouring into the cave! If you don't 
  quickly figure out where the sand is going, you could quickly become 
  trapped!

  Fortunately, your familiarity with analyzing the path of falling material 
  will come in handy here. You scan a two-dimensional vertical slice of the 
  cave above you (your puzzle input) and discover that it is mostly air with 
  structures made of rock.

  Your scan traces the path of each solid rock structure and reports the x,y 
  coordinates that form the shape of the path, where x represents distance 
  to the right and y represents distance down. Each path appears as a single 
  line of text in your scan. After the first point of each path, each point 
  indicates the end of a straight horizontal or vertical line to be drawn 
  from the previous point. For example:

  498,4 -> 498,6 -> 496,6
  503,4 -> 502,4 -> 502,9 -> 494,9
  
  This scan means that there are two paths of rock; the first path consists 
  of two straight lines, and the second path consists of three straight 
  lines. (Specifically, the first path consists of a line of rock from 498,4 
  through 498,6 and another line of rock from 498,6 through 496,6.)

  The sand is pouring into the cave from point 500,0.

  Drawing rock as #, air as ., and the source of the sand as +, this 
  becomes:


    4     5  5
    9     0  0
    4     0  3
  0 ......+...
  1 ..........
  2 ..........
  3 ..........
  4 ....#...##
  5 ....#...#.
  6 ..###...#.
  7 ........#.
  8 ........#.
  9 #########.
  
  Sand is produced one unit at a time, and the next unit of sand is not 
  produced until the previous unit of sand comes to rest. A unit of sand is 
  large enough to fill one tile of air in your scan.

  A unit of sand always falls down one step if possible. If the tile 
  immediately below is blocked (by rock or sand), the unit of sand attempts 
  to instead move diagonally one step down and to the left. If that tile is 
  blocked, the unit of sand attempts to instead move diagonally one step down 
  and to the right. Sand keeps moving as long as it is able to do so, at each 
  step trying to move down, then down-left, then down-right. If all three 
  possible destinations are blocked, the unit of sand comes to rest and no 
  longer moves, at which point the next unit of sand is created back at the 
  source.

  So, drawing sand that has come to rest as o, the first unit of sand simply 
  falls straight down and then stops:

  ......+...
  ..........
  ..........
  ..........
  ....#...##
  ....#...#.
  ..###...#.
  ........#.
  ......o.#.
  #########.
  
  The second unit of sand then falls straight down, lands on the first one, 
  and then comes to rest to its left:

  ......+...
  ..........
  ..........
  ..........
  ....#...##
  ....#...#.
  ..###...#.
  ........#.
  .....oo.#.
  #########.
  
  After a total of five units of sand have come to rest, they form this pattern:

  ......+...
  ..........
  ..........
  ..........
  ....#...##
  ....#...#.
  ..###...#.
  ......o.#.
  ....oooo#.
  #########.
  
  After a total of 22 units of sand:

  ......+...
  ..........
  ......o...
  .....ooo..
  ....#ooo##
  ....#ooo#.
  ..###ooo#.
  ....oooo#.
  ...ooooo#.
  #########.
  
  Finally, only two more units of sand can possibly come to rest:

  ......+...
  ..........
  ......o...
  .....ooo..
  ....#ooo##
  ...o#ooo#.
  ..###ooo#.
  ....oooo#.
  .o.ooooo#.
  #########.
  
  Once all 24 units of sand shown above have come to rest, all further sand 
  flows out the bottom, falling into the endless void. Just for fun, the path 
  any new sand takes before falling forever is shown here with ~:

  .......+...
  .......~...
  ......~o...
  .....~ooo..
  ....~#ooo##
  ...~o#ooo#.
  ..~###ooo#.
  ..~..oooo#.
  .~o.ooooo#.
  ~#########.
  ~..........
  ~..........
  ~..........
  
  Using your scan, simulate the falling sand. How many units of sand come to 
  rest before sand starts flowing into the abyss below?
  */

  public static int solve_part_1(List<List<Point>> input) {
    min_x -= 5; // border
    max_x += 5; // border
    int width = max_x - min_x + 1;
    int height = max_y + 1; // min_y is always 0
    String[][] cave = get_cave(input, width, height);
    int sand_count = 0;
    // Start dropping sand until no longer possible:
    while(true) {
      int sand_x = 500-min_x;
      int sand_y = 0;
      while(true) { // sand falls down while it can
        if(sand_y < height-1 && (cave[sand_y+1][sand_x] == "." || cave[sand_y+1][sand_x] == "~")) {
          cave[sand_y][sand_x] = "~";
          sand_y++;
        } else if(sand_y < height-1 && sand_x > 0 && (cave[sand_y+1][sand_x-1] == "." || cave[sand_y+1][sand_x-1] == "~")) {
          cave[sand_y][sand_x] = "~";
          sand_x--;
          sand_y++;
        } else if(sand_y < height-1 && sand_x < width-1 && (cave[sand_y+1][sand_x+1] == "." || cave[sand_y+1][sand_x+1] == "~")) {
          cave[sand_y][sand_x] = "~";
          sand_x++;
          sand_y++;
        } else {
          break;
        }
      }
      if(sand_x > 0 && sand_x < width-1 && sand_y < height-1){
        cave[sand_y][sand_x] = "O";
        sand_count++;
      } else {
        cave[sand_y][sand_x] = "~";
        break;
      }
    }
    cave[0][500-min_x] = "+";
    print_cave(cave);
    return sand_count;
  }

  public static String[][] get_cave(List<List<Point>> input, int width, int height) {
    String[][] cave = new String[height][width];
    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        cave[i][j] = ".";
      }
    }
    for(List<Point> path : input) {
      Point current = path.get(0);
      for(int i = 1; i < path.size(); i++) {
        Point next = path.get(i);
        if(current.x < next.x) { // horizontal line (left to right)
          for(int x = current.x; x <= next.x; x++) {
            cave[current.y-min_y][x-min_x] = "#"; // normalize coordinates
          }
        } else if(current.x > next.x) { // horizontal line (right to left)
          for(int x = current.x; x >= next.x; x--) {
            cave[current.y-min_y][x-min_x] = "#"; // normalize coordinates
          }
        } else if(current.y < next.y) { // vertical line (top down)
          for(int y = current.y; y <= next.y; y++) {
            cave[y-min_y][current.x-min_x] = "#"; // normalize coordinates
          }
        } else { // vertical line (bottom up)
          for(int y = current.y; y >= next.y; y--) {
            cave[y-min_y][current.x-min_x] = "#"; // normalize coordinates
          }
        }
        current = next;
      }
    }
    cave[0][500-min_x] = "+";
    return cave;
  }

  public static void print_cave(String[][] cave) {
    String out = "";
    for(int i = 0; i < cave.length; i++) {
      for(int j = 0; j < cave[0].length; j++) {
        out += cave[i][j];
      }
      out+="\n";
    }
    System.out.println(out);    
  }

  /*
  --- Part Two ---
  You realize you misread the scan. There isn't an endless void at the bottom 
  of the scan - there's floor, and you're standing on it!

  You don't have time to scan the floor, so assume the floor is an infinite 
  horizontal line with a y coordinate equal to two plus the highest y 
  coordinate of any point in your scan.

  In the example above, the highest y coordinate of any point is 9, and so 
  the floor is at y=11. (This is as if your scan contained one extra rock 
  path like -infinity,11 -> infinity,11.) With the added floor, the example 
  above now looks like this:

          ...........+........
          ....................
          ....................
          ....................
          .........#...##.....
          .........#...#......
          .......###...#......
          .............#......
          .............#......
          .....#########......
          ....................
  <-- etc #################### etc -->
  
  To find somewhere safe to stand, you'll need to simulate falling sand until 
  a unit of sand comes to rest at 500,0, blocking the source entirely and 
  stopping the flow of sand into the cave. In the example above, the 
  situation finally looks like this after 93 units of sand come to rest:

  ............o............
  ...........ooo...........
  ..........ooooo..........
  .........ooooooo.........
  ........oo#ooo##o........
  .......ooo#ooo#ooo.......
  ......oo###ooo#oooo......
  .....oooo.oooo#ooooo.....
  ....oooooooooo#oooooo....
  ...ooo#########ooooooo...
  ..ooooo.......ooooooooo..
  #########################
  
  Using your scan, simulate the falling sand until the source of the sand 
  becomes blocked. How many units of sand come to rest? 
  */

  public static int solve_part_2(List<List<Point>> input) {
    min_x -= 500;
    max_x += 500;
    max_y += 2;
    int width = max_x - min_x + 1;
    int height = max_y + 1; // min_y is always 0
    String[][] cave = get_cave(input, width, height);
    for(int x = 0; x < width; x++) { // draw the floor
      cave[height-1][x] = "#"; // normalize coordinates
    }
    int sand_count = 0;
    // Start dropping sand until no longer possible:
    while(true) {
      int sand_x = 500-min_x;
      int sand_y = 0;
      while(true) { // sand falls down while it can
        if(sand_y < height-1 && (cave[sand_y+1][sand_x] == "." || cave[sand_y+1][sand_x] == "~")) {
          cave[sand_y][sand_x] = "~";
          sand_y++;
        } else if(sand_y < height-1 && sand_x > 0 && (cave[sand_y+1][sand_x-1] == "." || cave[sand_y+1][sand_x-1] == "~")) {
          cave[sand_y][sand_x] = "~";
          sand_x--;
          sand_y++;
        } else if(sand_y < height-1 && sand_x < width-1 && (cave[sand_y+1][sand_x+1] == "." || cave[sand_y+1][sand_x+1] == "~")) {
          cave[sand_y][sand_x] = "~";
          sand_x++;
          sand_y++;
        } else {
          break;
        }
      }
      if(sand_x > 0 && sand_x < width-1 && sand_y > 0 && sand_y < height-1){
        cave[sand_y][sand_x] = "O";
        sand_count++;
      } else {
        cave[sand_y][sand_x] = "~";
        break;
      }
    }
    return sand_count + 1; // + 1 for the source
  }

  public static int min_x = Integer.MAX_VALUE;
  public static int max_x = Integer.MIN_VALUE;
  public static int min_y = 0;
  public static int max_y = 0;

  public static List<List<Point>> read_input() {
    List<List<Point>> input_list = new ArrayList<List<Point>>();
    Pattern line_pattern = Pattern.compile("\\d+,\\d+(\s|$)");
    Pattern point_pattern = Pattern.compile("\\d+");
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
          List<Point> path = new ArrayList<Point>();
          Matcher line_matcher = line_pattern.matcher(scanner.nextLine());
          while(line_matcher.find()) {
            Matcher point_matcher = point_pattern.matcher(line_matcher.group());
            int x = -1;
            int y = -1;
            if(point_matcher.find()) {
              x = Integer.valueOf(point_matcher.group());
              min_x = Math.min(min_x, x);
              max_x = Math.max(max_x, x);
            }
            if(point_matcher.find()) {
              y = Integer.valueOf(point_matcher.group());
              max_y = Math.max(max_y, y);
            }
            path.add(new Point(x, y));
          }
          input_list.add(path);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class Point {
  int x;
  int y;
  
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}