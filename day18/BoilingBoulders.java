package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class BoilingBoulders {

  public static void main(String[] args) {
    List<Droplet> input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println(solve_part_2(input));
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
      droplet_set.add(get_string(droplet.x, droplet.y, droplet.z));
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

  public static String get_string(int x, int y, int z) {
    return x + "," + y + "," + z;
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
    Set<String> droplets = new HashSet<String>();
    for(Droplet droplet : input) {
      droplets.add(get_string(droplet.x, droplet.y, droplet.z));
    }
    
    // Create border around droplet:
    min_x--;
    max_x++;
    min_y--;
    max_y++;
    min_z--;
    max_z++;

    Set<String> outside = new HashSet<String>();
    Droplet definitely_outside = new Droplet(max_x, max_y, max_z); // definitely filled with air
    outside.add(get_string(definitely_outside.x, definitely_outside.y, definitely_outside.z));    

    Stack<Droplet> stack = new Stack<Droplet>();
    stack.push(definitely_outside);
    Set<String> visited = new HashSet<String>();
    while(!stack.empty()) {
      Droplet current = stack.pop();
      int x = current.x;
      int y = current.y;
      int z = current.z;
      if(visited.contains(get_string(x, y, z))) {
        continue;
      }
      visited.add(get_string(x, y, z));
      if(x+1 <= max_x && !visited.contains(get_string(x+1, y, z)) && !droplets.contains(get_string(x+1, y, z))) { // air that is not visited
        outside.add(get_string(x+1, y, z));
        stack.push(new Droplet(x+1, y, z));
      }
      if(x-1 >= min_x && !visited.contains(get_string(x-1, y, z)) && !droplets.contains(get_string(x-1, y, z))) { // air that is not visited
        outside.add(get_string(x-1, y, z));
        stack.push(new Droplet(x-1, y, z));
      }
      if(y+1 <= max_y && !visited.contains(get_string(x, y+1, z)) && !droplets.contains(get_string(x, y+1, z))) { // air that is not visited
        outside.add(get_string(x, y+1, z));
        stack.push(new Droplet(x, y+1, z));
      }
      if(y-1 >= min_y && !visited.contains(get_string(x, y-1, z)) && !droplets.contains(get_string(x, y-1, z))) { // air that is not visited
        outside.add(get_string(x, y-1, z));
        stack.push(new Droplet(x, y-1, z));
      }
      if(z+1 <= max_z && !visited.contains(get_string(x, y, z+1)) && !droplets.contains(get_string(x, y, z+1))) { // air that is not visited
        outside.add(get_string(x, y, z+1));
        stack.push(new Droplet(x, y, z+1));
      }
      if(z-1 >= min_z && !visited.contains(get_string(x, y, z-1)) && !droplets.contains(get_string(x, y, z-1))) { // air that is not visited
        outside.add(get_string(x, y, z-1));
        stack.push(new Droplet(x, y, z-1));
      }
    }

    int exposed_sides = 0;
    for(Droplet droplet: input) {
      for(String neighbor : droplet.getNeighborStrings()) {
        if(outside.contains(neighbor)) {
          exposed_sides++;
        }
      }
    }
    return exposed_sides;
  }

  public static int min_x = Integer.MAX_VALUE;
  public static int max_x = Integer.MIN_VALUE;
  public static int min_y = Integer.MAX_VALUE;
  public static int max_y = Integer.MIN_VALUE;
  public static int min_z = Integer.MAX_VALUE;
  public static int max_z = Integer.MIN_VALUE;

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
          min_x = Math.min(min_x, x);
          max_x = Math.max(max_x, x);
        }
        if(number_matcher.find()) {
          y = Integer.valueOf(number_matcher.group());
          min_y = Math.min(min_y, y);
          max_y = Math.max(max_y, y);
        }
        if(number_matcher.find()) {
          z = Integer.valueOf(number_matcher.group());
        }
        input_list.add(new Droplet(x, y, z));
        min_z = Math.min(min_z, z);
        max_z = Math.max(max_z, z);
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

  public String[] getNeighborStrings() {
    String[] neighbors = new String[6];
    neighbors[0] = BoilingBoulders.get_string(x+1, y, z);
    neighbors[1] = BoilingBoulders.get_string(x-1, y, z);
    neighbors[2] = BoilingBoulders.get_string(x, y+1, z);
    neighbors[3] = BoilingBoulders.get_string(x, y-1, z);
    neighbors[4] = BoilingBoulders.get_string(x, y, z+1);
    neighbors[5] = BoilingBoulders.get_string(x, y, z-1);
    return neighbors;
  }

}