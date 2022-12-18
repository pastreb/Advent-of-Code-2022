package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class BoilingBoulders {

  public static void main(String[] args) {
    List<Droplet> input = read_input();
    System.out.println(solve_part_1(input));
  }

  /*
  --- Day 18: Boiling Boulders ---
  You and the elephants finally reach fresh air. You've emerged near the base 
  of a large volcano that seems to be actively erupting! Fortunately, the 
  lava seems to be flowing away from you and toward the ocean.

  Bits of lava are still being ejected toward you, so you're sheltering in 
  the cavern exit a little longer. Outside the cave, you can see the lava 
  landing in a pond and hear it loudly hissing as it solidifies.

  Depending on the specific compounds in the lava and speed at which it 
  cools, it might be forming obsidian! The cooling rate should be based on 
  the surface area of the lava droplets, so you take a quick scan of a 
  droplet as it flies past you (your puzzle input).

  Because of how quickly the lava is moving, the scan isn't very good; its 
  resolution is quite low and, as a result, it approximates the shape of the 
  lava droplet with 1x1x1 cubes on a 3D grid, each given as its x,y,z position.

  To approximate the surface area, count the number of sides of each cube 
  that are not immediately connected to another cube. So, if your scan were 
  only two adjacent cubes like 1,1,1 and 2,1,1, each cube would have a single 
  side covered and five sides exposed, a total surface area of 10 sides.

  Here's a larger example:

  2,2,2
  1,2,2
  3,2,2
  2,1,2
  2,3,2
  2,2,1
  2,2,3
  2,2,4
  2,2,6
  1,2,5
  3,2,5
  2,1,5
  2,3,5
  
  In the above example, after counting up all the sides that aren't connected 
  to another cube, the total surface area is 64.

  What is the surface area of your scanned lava droplet?
  */

  public static int solve_part_1(List<Droplet> input) {
    Set<String> droplet_set = new HashSet<String>();
    for(Droplet droplet : input) {
      droplet_set.add(droplet.getString());
    }
    int exposed_sides = 0;
    for(Droplet droplet: input) {
      for(String neighbor : droplet.getNeighborStrings()) {
        if(!droplet_set.contains(neighbor)) {
          exposed_sides++;
        }
      }
    }
    return exposed_sides;
  }

  /*
  --- Part Two ---
  Something seems off about your calculation. The cooling rate depends on 
  exterior surface area, but your calculation also included the surface area 
  of air pockets trapped in the lava droplet.

  Instead, consider only cube sides that could be reached by the water and 
  steam as the lava droplet tumbles into the pond. The steam will expand to 
  reach as much as possible, completely displacing any air on the outside of 
  the lava droplet but never expanding diagonally.

  In the larger example above, exactly one cube of air is trapped within the 
  lava droplet (at 2,2,5), so the exterior surface area of the lava droplet 
  is 58.

  What is the exterior surface area of your scanned lava droplet?
  */

  public static int solve_part_2(List<Droplet> input) {
    return 0;
  }

  public static List<Droplet> read_input() {
    List<Droplet> input_list = new ArrayList<Droplet>();
    Pattern number_pattern = Pattern.compile("\\d+");
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        Matcher number_matcher = number_pattern.matcher(scanner.nextLine());
        int x = -1;
        int y = -1;
        int z = -1;
        if(number_matcher.find()) {
          x = Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          y = Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          z = Integer.valueOf(number_matcher.group());
        }
        input_list.add(new Droplet(x, y, z));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class Droplet {
  public int x;
  public int y;
  public int z;

  public Droplet(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public String getString() {
    return "" + x + "," + y + "," + z;
  }

  public String[] getNeighborStrings() {
    String[] neighbors = new String[6];
    neighbors[0] = "" + (x+1) + "," + y + "," + z;
    neighbors[1] = "" + (x-1) + "," + y + "," + z;
    neighbors[2] = "" + x + "," + (y+1) + "," + z;
    neighbors[3] = "" + x + "," + (y-1) + "," + z;
    neighbors[4] = "" + x + "," + y + "," + (z+1);
    neighbors[5] = "" + x + "," + y + "," + (z-1);
    return neighbors;
  }
}