package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class BeaconExclusionZone {

  public static void main(String[] args) {
    List<Sensor> input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 15: Beacon Exclusion Zone ---
  You feel the ground rumble again as the distress signal leads you to a 
  large network of subterranean tunnels. You don't have time to search them 
  all, but you don't need to: your pack contains a set of deployable sensors 
  that you imagine were originally built to locate lost Elves.

  The sensors aren't very powerful, but that's okay; your handheld device 
  indicates that you're close enough to the source of the distress signal to 
  use them. You pull the emergency sensor system out of your pack, hit the 
  big button on top, and the sensors zoom off down the tunnels.

  Once a sensor finds a spot it thinks will give it a good reading, it 
  attaches itself to a hard surface and begins monitoring for the nearest 
  signal source beacon. Sensors and beacons always exist at integer 
  coordinates. Each sensor knows its own position and can determine the 
  position of a beacon precisely; however, sensors can only lock on to the 
  one beacon closest to the sensor as measured by the Manhattan distance. 
  (There is never a tie where two beacons are the same distance to a sensor.)

  It doesn't take long for the sensors to report back their positions and 
  closest beacons (your puzzle input). For example:

  Sensor at x=2, y=18: closest beacon is at x=-2, y=15
  Sensor at x=9, y=16: closest beacon is at x=10, y=16
  Sensor at x=13, y=2: closest beacon is at x=15, y=3
  Sensor at x=12, y=14: closest beacon is at x=10, y=16
  Sensor at x=10, y=20: closest beacon is at x=10, y=16
  Sensor at x=14, y=17: closest beacon is at x=10, y=16
  Sensor at x=8, y=7: closest beacon is at x=2, y=10
  Sensor at x=2, y=0: closest beacon is at x=2, y=10
  Sensor at x=0, y=11: closest beacon is at x=2, y=10
  Sensor at x=20, y=14: closest beacon is at x=25, y=17
  Sensor at x=17, y=20: closest beacon is at x=21, y=22
  Sensor at x=16, y=7: closest beacon is at x=15, y=3
  Sensor at x=14, y=3: closest beacon is at x=15, y=3
  Sensor at x=20, y=1: closest beacon is at x=15, y=3
  
  So, consider the sensor at 2,18; the closest beacon to it is at -2,15. For 
  the sensor at 9,16, the closest beacon to it is at 10,16.

  Drawing sensors as S and beacons as B, the above arrangement of sensors 
  and beacons looks like this:

                1    1    2    2
      0    5    0    5    0    5
  0 ....S.......................
  1 ......................S.....
  2 ...............S............
  3 ................SB..........
  4 ............................
  5 ............................
  6 ............................
  7 ..........S.......S.........
  8 ............................
  9 ............................
  10 ....B.......................
  11 ..S.........................
  12 ............................
  13 ............................
  14 ..............S.......S.....
  15 B...........................
  16 ...........SB...............
  17 ................S..........B
  18 ....S.......................
  19 ............................
  20 ............S......S........
  21 ............................
  22 .......................B....
  
  This isn't necessarily a comprehensive map of all beacons in the area, 
  though. Because each sensor only identifies its closest beacon, if a sensor 
  detects a beacon, you know there are no other beacons that close or closer 
  to that sensor. There could still be beacons that just happen to not be the 
  closest beacon to any sensor. Consider the sensor at 8,7:

                1    1    2    2
      0    5    0    5    0    5
  -2 ..........#.................
  -1 .........###................
  0 ....S...#####...............
  1 .......#######........S.....
  2 ......#########S............
  3 .....###########SB..........
  4 ....#############...........
  5 ...###############..........
  6 ..#################.........
  7 .#########S#######S#........
  8 ..#################.........
  9 ...###############..........
  10 ....B############...........
  11 ..S..###########............
  12 ......#########.............
  13 .......#######..............
  14 ........#####.S.......S.....
  15 B........###................
  16 ..........#SB...............
  17 ................S..........B
  18 ....S.......................
  19 ............................
  20 ............S......S........
  21 ............................
  22 .......................B....
  
  This sensor's closest beacon is at 2,10, and so you know there are no 
  beacons that close or closer (in any positions marked #).

  None of the detected beacons seem to be producing the distress signal, so 
  you'll need to work out where the distress beacon is by working out where 
  it isn't. For now, keep things simple by counting the positions where a 
  beacon cannot possibly be along just a single row.

  So, suppose you have an arrangement of beacons and sensors like in the 
  example above and, just in the row where y=10, you'd like to count the 
  number of positions a beacon cannot possibly exist. The coverage from all 
  sensors near that row looks like this:

                  1    1    2    2
        0    5    0    5    0    5
  9 ...#########################...
  10 ..####B######################..
  11 .###S#############.###########.
  
  In this example, in the row where y=10, there are 26 positions where a 
  beacon cannot be present.

  Consult the report from the sensors you just deployed. In the row where 
  y=2000000, how many positions cannot contain a beacon?
  */

  public static int solve_part_1(List<Sensor> input) {
    int relevant_row = 2000000;
    Range covered_range = new Range(input.size());
    for(Sensor sensor : input) {
      Range next = sensor.get_covered_range(relevant_row);
      if(next != null) {
        covered_range.add(next.starts[0], next.ends[0]);
      }
    }
    int n_covered = covered_range.get_n_values_in_range();
    // Remove Beacons that may already be in that row:
    Set<Integer> beacons = new HashSet<Integer>();
    for(Sensor sensor : input) {
      if(sensor.beacon_y == relevant_row) {
        beacons.add(sensor.beacon_x);
      }
    }
    return n_covered - beacons.size();
  }

  /*
  --- Part Two ---
  Your handheld device indicates that the distress signal is coming from a 
  beacon nearby. The distress beacon is not detected by any sensor, but the 
  distress beacon must have x and y coordinates each no lower than 0 and no 
  larger than 4000000.

  To isolate the distress beacon's signal, you need to determine its tuning 
  frequency, which can be found by multiplying its x coordinate by 4000000 
  and then adding its y coordinate.

  In the example above, the search space is smaller: instead, the x and y 
  coordinates can each be at most 20. With this reduced search area, there 
  is only a single position that could have a beacon: x=14, y=11. The tuning 
  frequency for this distress beacon is 56000011.

  Find the only possible position for the distress beacon. What is its tuning 
  frequency?
  */

  public static long solve_part_2(List<Sensor> input) {
    int min = 0;
    int max = 4000000;

    for(int y = min; y <= max; y++) {
      Range range = new Range(input.size());
      for(Sensor sensor : input) {
        Range next = sensor.get_covered_range(y);
        if(next != null) {
          range.add(next.starts[0], next.ends[0]);
        }
      }
      if(range.current_size > 1) {
        int x = range.ends[0]+1;
        System.out.println("x=" + x + ", y=" + y);
        return ((long)x)*4000000+y;
      }
    }
    return -1;
  }

  public static List<Sensor> read_input() {
    List<Sensor> input_list = new ArrayList<Sensor>();
    Pattern pattern = Pattern.compile("-?\\d+");
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
          Matcher matcher = pattern.matcher(scanner.nextLine());
          int sensor_x = -1;
          int sensor_y = -1;
          int beacon_x = -1;
          int beacon_y = -1;
          if(matcher.find()) {
            sensor_x = Integer.valueOf(matcher.group());
          }
          if(matcher.find()) {
            sensor_y = Integer.valueOf(matcher.group());
          }
          if(matcher.find()) {
            beacon_x = Integer.valueOf(matcher.group());
          }
          if(matcher.find()) {
            beacon_y = Integer.valueOf(matcher.group());
          }
          input_list.add(new Sensor(sensor_x, sensor_y, beacon_x, beacon_y));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class Sensor {
  int x;
  int y;
  int beacon_x;
  int beacon_y;
  int covered_distance;
  
  public Sensor(int x, int y, int beacon_x, int beacon_y) {
    this.x = x;
    this.y = y;
    this.beacon_x = beacon_x;
    this.beacon_y = beacon_y;
    this.covered_distance = Math.abs(x-beacon_x)+Math.abs(y-beacon_y);
    // System.out.println("Sensor at " + x + "," + y + " with Beacon at " + beacon_x + "," + beacon_y + " with cover distance " + covered_distance);
  }

  public Range get_covered_range(int y_row) {
    int y_dist = Math.abs(this.y - y_row);
    int max_x_dist = covered_distance - y_dist;
    // System.out.println("Sensor at " + x + "," + y + " with Beacon at " + beacon_x + "," + beacon_y + " with max_x_distance " + max_x_dist);
    if(max_x_dist < 0) {
      return null;
    } else {
      return new Range(this.x - max_x_dist, this.x + max_x_dist, 1);
    }
  }
}

class Range {
  public int[] starts;
  public int[] ends;
  int current_size;

  public Range(int max_size) {
    this.starts = new int[max_size];
    this.ends = new int[max_size];
  }

  public Range(int start, int end, int max_size) {
    this(max_size);
    this.starts[0] = start;
    this.ends[0] = end;
    this.current_size = 1;
  }

  public int get_n_values_in_range() {
    int x = 0;
    for(int i = 0; i < current_size; i++) {
      x += Math.abs(ends[i] - starts[i]) + 1;
    }
    return x;
  }

  public void add(int start, int end) {
    starts[current_size] = start;
    ends[current_size] = end;
    current_size++;
    // System.out.println("[BEFORE CLEANUP] Start: " + Arrays.toString(starts) + ", End: " + Arrays.toString(ends));
    cleanup();
    // System.out.println("[AFTER CLEANUP] Start: " + Arrays.toString(starts) + ", End: " + Arrays.toString(ends));
  }

  public void cleanup() {
    // Try to merge intervals:
    for(int i = 0; i < current_size; i++) {
      for(int j = i+1; j < current_size; j++) {
        // System.out.println("Comparing interval [" + starts[i] + "," + ends[i] + "] with interval [" + starts[j] + "," + ends[j] + "]");
        if(starts[i] <= starts[j] && ends[j] <= ends[i]) { // [ { } ]
          starts[j] = Integer.MIN_VALUE;
          ends[j] = Integer.MIN_VALUE;          
        } else if(starts[i] <= starts[j] && starts[j] <= ends[i]) { // [ { ] }
          ends[i] = ends[j];
          starts[j] = Integer.MIN_VALUE;
          ends[j] = Integer.MIN_VALUE; 
          cleanup();
        } else if(starts[i] <= ends[j] && ends[j] <= ends[i]) { // { [ } ]
          starts[i] = starts[j];
          starts[j] = Integer.MIN_VALUE;
          ends[j] = Integer.MIN_VALUE;
          cleanup();
        } else if(starts[j] <= starts[i] && ends[i] <= ends[j]) {  // { [ ] }
          starts[i] = starts[j];
          ends[i] = ends[j];
          starts[j] = Integer.MIN_VALUE;
          ends[j] = Integer.MIN_VALUE;
          cleanup();
        } else if(ends[i] + 1 == starts[j]) { // []{}
          ends[i] = ends[j];
          starts[j] = Integer.MIN_VALUE;
          ends[j] = Integer.MIN_VALUE; 
          cleanup();
        } else if(ends[j] + 1 == starts[i]) { // {}[]
          starts[i] = starts[j];
          starts[j] = Integer.MIN_VALUE;
          ends[j] = Integer.MIN_VALUE; 
          cleanup();
        } else { // { } [ ] or [ ] { }
          continue;
        }
      }
    }
    // Sort:
    for(int i = 0; i < current_size; i++) {
      for(int j = i; j < current_size-1; j++) {
        if(starts[j] == Integer.MIN_VALUE && ends[j] == Integer.MIN_VALUE) {
          starts[j] = starts[j+1];
          ends[j] = ends[j+1];
          starts[j+1] = Integer.MIN_VALUE;
          ends[j+1] = Integer.MIN_VALUE;
        }
      }
    }
    for(int i = 0; i < current_size; i++) {
      if(starts[i] == Integer.MIN_VALUE && ends[i] == Integer.MIN_VALUE) {
        int j = i;
        while(j < current_size) {
          starts[j] = 0;
          ends[j] = 0;
          j++;
        }
        current_size = i;
        return;
      }
    }
  }

}