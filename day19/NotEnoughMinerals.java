package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class NotEnoughMinerals {

  public static void main(String[] args) {
    List<Blueprint> input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 19: Not Enough Minerals ---
  Your scans show that the lava did indeed form obsidian!

  The wind has changed direction enough to stop sending lava droplets toward 
  you, so you and the elephants exit the cave. As you do, you notice a 
  collection of geodes around the pond. Perhaps you could use the obsidian to 
  create some geode-cracking robots and break them open?

  To collect the obsidian from the bottom of the pond, you'll need waterproof 
  obsidian-collecting robots. Fortunately, there is an abundant amount of 
  clay nearby that you can use to make them waterproof.

  In order to harvest the clay, you'll need special-purpose clay-collecting 
  robots. To make any type of robot, you'll need ore, which is also plentiful 
  but in the opposite direction from the clay.

  Collecting ore requires ore-collecting robots with big drills. Fortunately, 
  you have exactly one ore-collecting robot in your pack that you can use to 
  kickstart the whole operation.

  Each robot can collect 1 of its resource type per minute. It also takes one 
  minute for the robot factory (also conveniently from your pack) to construct 
  any type of robot, although it consumes the necessary resources available 
  when construction begins.

  The robot factory has many blueprints (your puzzle input) you can choose 
  from, but once you've configured it with a blueprint, you can't change it. 
  You'll need to work out which blueprint is best.

  For example:

  Blueprint 1:
    Each ore robot costs 4 ore.
    Each clay robot costs 2 ore.
    Each obsidian robot costs 3 ore and 14 clay.
    Each geode robot costs 2 ore and 7 obsidian.

  Blueprint 2:
    Each ore robot costs 2 ore.
    Each clay robot costs 3 ore.
    Each obsidian robot costs 3 ore and 8 clay.
    Each geode robot costs 3 ore and 12 obsidian.
  
  (Blueprints have been line-wrapped here for legibility. The robot factory's 
  actual assortment of blueprints are provided one blueprint per line.)

  The elephants are starting to look hungry, so you shouldn't take too long; 
  you need to figure out which blueprint would maximize the number of opened 
  geodes after 24 minutes by figuring out which robots to build and when to 
  build them.

  Using blueprint 1 in the example above, the largest number of geodes you 
  could open in 24 minutes is 9. One way to achieve that is:

  == Minute 1 ==
  1 ore-collecting robot collects 1 ore; you now have 1 ore.

  == Minute 2 ==
  1 ore-collecting robot collects 1 ore; you now have 2 ore.

  == Minute 3 ==
  Spend 2 ore to start building a clay-collecting robot.
  1 ore-collecting robot collects 1 ore; you now have 1 ore.
  The new clay-collecting robot is ready; you now have 1 of them.

  == Minute 4 ==
  1 ore-collecting robot collects 1 ore; you now have 2 ore.
  1 clay-collecting robot collects 1 clay; you now have 1 clay.

  == Minute 5 ==
  Spend 2 ore to start building a clay-collecting robot.
  1 ore-collecting robot collects 1 ore; you now have 1 ore.
  1 clay-collecting robot collects 1 clay; you now have 2 clay.
  The new clay-collecting robot is ready; you now have 2 of them.

  == Minute 6 ==
  1 ore-collecting robot collects 1 ore; you now have 2 ore.
  2 clay-collecting robots collect 2 clay; you now have 4 clay.

  == Minute 7 ==
  Spend 2 ore to start building a clay-collecting robot.
  1 ore-collecting robot collects 1 ore; you now have 1 ore.
  2 clay-collecting robots collect 2 clay; you now have 6 clay.
  The new clay-collecting robot is ready; you now have 3 of them.

  == Minute 8 ==
  1 ore-collecting robot collects 1 ore; you now have 2 ore.
  3 clay-collecting robots collect 3 clay; you now have 9 clay.

  == Minute 9 ==
  1 ore-collecting robot collects 1 ore; you now have 3 ore.
  3 clay-collecting robots collect 3 clay; you now have 12 clay.

  == Minute 10 ==
  1 ore-collecting robot collects 1 ore; you now have 4 ore.
  3 clay-collecting robots collect 3 clay; you now have 15 clay.

  == Minute 11 ==
  Spend 3 ore and 14 clay to start building an obsidian-collecting robot.
  1 ore-collecting robot collects 1 ore; you now have 2 ore.
  3 clay-collecting robots collect 3 clay; you now have 4 clay.
  The new obsidian-collecting robot is ready; you now have 1 of them.

  == Minute 12 ==
  Spend 2 ore to start building a clay-collecting robot.
  1 ore-collecting robot collects 1 ore; you now have 1 ore.
  3 clay-collecting robots collect 3 clay; you now have 7 clay.
  1 obsidian-collecting robot collects 1 obsidian; you now have 1 obsidian.
  The new clay-collecting robot is ready; you now have 4 of them.

  == Minute 13 ==
  1 ore-collecting robot collects 1 ore; you now have 2 ore.
  4 clay-collecting robots collect 4 clay; you now have 11 clay.
  1 obsidian-collecting robot collects 1 obsidian; you now have 2 obsidian.

  == Minute 14 ==
  1 ore-collecting robot collects 1 ore; you now have 3 ore.
  4 clay-collecting robots collect 4 clay; you now have 15 clay.
  1 obsidian-collecting robot collects 1 obsidian; you now have 3 obsidian.

  == Minute 15 ==
  Spend 3 ore and 14 clay to start building an obsidian-collecting robot.
  1 ore-collecting robot collects 1 ore; you now have 1 ore.
  4 clay-collecting robots collect 4 clay; you now have 5 clay.
  1 obsidian-collecting robot collects 1 obsidian; you now have 4 obsidian.
  The new obsidian-collecting robot is ready; you now have 2 of them.

  == Minute 16 ==
  1 ore-collecting robot collects 1 ore; you now have 2 ore.
  4 clay-collecting robots collect 4 clay; you now have 9 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 6 obsidian.

  == Minute 17 ==
  1 ore-collecting robot collects 1 ore; you now have 3 ore.
  4 clay-collecting robots collect 4 clay; you now have 13 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 8 obsidian.

  == Minute 18 ==
  Spend 2 ore and 7 obsidian to start building a geode-cracking robot.
  1 ore-collecting robot collects 1 ore; you now have 2 ore.
  4 clay-collecting robots collect 4 clay; you now have 17 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 3 obsidian.
  The new geode-cracking robot is ready; you now have 1 of them.

  == Minute 19 ==
  1 ore-collecting robot collects 1 ore; you now have 3 ore.
  4 clay-collecting robots collect 4 clay; you now have 21 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 5 obsidian.
  1 geode-cracking robot cracks 1 geode; you now have 1 open geode.

  == Minute 20 ==
  1 ore-collecting robot collects 1 ore; you now have 4 ore.
  4 clay-collecting robots collect 4 clay; you now have 25 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 7 obsidian.
  1 geode-cracking robot cracks 1 geode; you now have 2 open geodes.

  == Minute 21 ==
  Spend 2 ore and 7 obsidian to start building a geode-cracking robot.
  1 ore-collecting robot collects 1 ore; you now have 3 ore.
  4 clay-collecting robots collect 4 clay; you now have 29 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 2 obsidian.
  1 geode-cracking robot cracks 1 geode; you now have 3 open geodes.
  The new geode-cracking robot is ready; you now have 2 of them.

  == Minute 22 ==
  1 ore-collecting robot collects 1 ore; you now have 4 ore.
  4 clay-collecting robots collect 4 clay; you now have 33 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 4 obsidian.
  2 geode-cracking robots crack 2 geodes; you now have 5 open geodes.

  == Minute 23 ==
  1 ore-collecting robot collects 1 ore; you now have 5 ore.
  4 clay-collecting robots collect 4 clay; you now have 37 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 6 obsidian.
  2 geode-cracking robots crack 2 geodes; you now have 7 open geodes.

  == Minute 24 ==
  1 ore-collecting robot collects 1 ore; you now have 6 ore.
  4 clay-collecting robots collect 4 clay; you now have 41 clay.
  2 obsidian-collecting robots collect 2 obsidian; you now have 8 obsidian.
  2 geode-cracking robots crack 2 geodes; you now have 9 open geodes.
  
  However, by using blueprint 2 in the example above, you could do even 
  better: the largest number of geodes you could open in 24 minutes is 12.

  Determine the quality level of each blueprint by multiplying that 
  blueprint's ID number with the largest number of geodes that can be opened 
  in 24 minutes using that blueprint. In this example, the first blueprint 
  has ID 1 and can open 9 geodes, so its quality level is 9. The second 
  blueprint has ID 2 and can open 12 geodes, so its quality level is 24. 
  Finally, if you add up the quality levels of all of the blueprints in the 
  list, you get 33.

  Determine the quality level of each blueprint using the largest number of 
  geodes it could produce in 24 minutes. What do you get if you add up the 
  quality level of all of the blueprints in your list?
  */

  public static int solve_part_1(List<Blueprint> input) {
    int sum = 0;
    for(Blueprint blueprint : input) {
      int quality_level = recurse(new Factory(blueprint), 1, 24) * blueprint.id;
      sum += quality_level;
      System.out.println("BLUEPRINT " + blueprint.id + " DONE: " + quality_level);
    }
    return sum;
  }

  public static int recurse(Factory factory, int minute, int max_time) {
    factory.collect();
    if(minute == max_time) {
      return factory.n_geode;
    }

    // We can try to be a bit smart, but essentially we still have to brute-force all possibilities.

    int n_geodes = 0;

    // (1) If we can build a Geode-Robot, we really only wanna do that:
    if(factory.can_build_geode_robot()) {
      Factory f1 = new Factory(factory);
      f1.build_geode_robot();
      return recurse(f1, minute+1, max_time); 
      // Once we are able to build Geode-Robots, we probably won't build anything else
    }

    // (2) (Possibly) build an Obsidian-Robot:
    if(factory.can_build_obsidian_robot(max_time-minute)) { // it's not only about "can", but also about "should" build
      Factory f2 = new Factory(factory);
      f2.build_obsidian_robot();
      n_geodes = Math.max(n_geodes, recurse(f2, minute+1, max_time));
    }

    // (3) (Possibly) build a Clay-Robot:
    if(factory.can_build_clay_robot(max_time-minute)) { // it's not only about "can", but also about "should" build
      Factory f3 = new Factory(factory);
      f3.build_clay_robot();
      n_geodes = Math.max(n_geodes, recurse(f3, minute+1, max_time));
    }

    // (4) (Possibly) build an Ore-Robot:
    if(factory.can_build_ore_robot(max_time-minute)) { // it's not only about "can", but also about "should" build
      Factory f4 = new Factory(factory);
      f4.build_ore_robot();
      n_geodes = Math.max(n_geodes, recurse(f4, minute+1, max_time));
    }

    // (5) Build Nothing:
    n_geodes = Math.max(n_geodes, recurse(factory, minute+1, max_time));
    
    return n_geodes;
  }

  /*
  --- Part Two ---

  */

  public static int solve_part_2(List<Blueprint> input) {
    return 0;
  }

  public static List<Blueprint> read_input() {
    List<Blueprint> input_list = new ArrayList<Blueprint>();
    Pattern number_pattern = Pattern.compile("\\d+");
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        Matcher number_matcher = number_pattern.matcher(scanner.nextLine());
        int id = -1;
        int ore_robot_cost = -1;
        int clay_robot_cost = -1;
        int obsidian_robot_cost_ore = -1;
        int obsidian_robot_cost_clay = -1;
        int geode_robot_cost_ore = -1;
        int geode_robot_cost_obsidian = -1;
        if(number_matcher.find()) {
          id = Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          ore_robot_cost = Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          clay_robot_cost = Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          obsidian_robot_cost_ore = Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          obsidian_robot_cost_clay = Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          geode_robot_cost_ore= Integer.valueOf(number_matcher.group());
        }
        if(number_matcher.find()) {
          geode_robot_cost_obsidian = Integer.valueOf(number_matcher.group());
        }
        input_list.add(new Blueprint(id, ore_robot_cost, clay_robot_cost, 
                                     obsidian_robot_cost_ore, obsidian_robot_cost_clay, 
                                     geode_robot_cost_ore, geode_robot_cost_obsidian));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class Blueprint {
  public int id;
  public int ore_robot_cost;
  public int clay_robot_cost;
  public int obsidian_robot_cost_ore;
  public int obsidian_robot_cost_clay;
  public int geode_robot_cost_ore;
  public int geode_robot_cost_obsidian;

  public Blueprint(int id, int ore_robot_cost, int clay_robot_cost, 
                   int obsidian_robot_cost_ore, int obsidian_robot_cost_clay, 
                   int geode_robot_cost_ore, int geode_robot_cost_obsidian) {
    this.id = id;
    this.ore_robot_cost = ore_robot_cost;
    this.clay_robot_cost = clay_robot_cost;
    this.obsidian_robot_cost_ore = obsidian_robot_cost_ore;
    this.obsidian_robot_cost_clay = obsidian_robot_cost_clay;
    this.geode_robot_cost_ore = geode_robot_cost_ore;
    this.geode_robot_cost_obsidian = geode_robot_cost_obsidian;
  }
}

enum Roboter {
  ORE, CLAY, OBSIDIAN, GEODE, NONE;
}

class Factory {
  Blueprint blueprint;

  public int n_ore = 0;
  public int ore_robots = 1;
  public int max_ore_robots = 0; 
 
  public int n_clay = 0;
  public int clay_robots = 0;
  public int max_clay_robots = 0;

  public int n_obsidian = 0;
  public int obisidan_robots = 0;
  public int max_obisidan_robots = 0;
  
  public int n_geode = 0;
  public int geode_robots = 0;

  public Factory(Blueprint blueprint) {
    this.blueprint = blueprint;
    // (R1) For any resource R that's not geode: 
    // If we already have X robots creating resource R, and no robot requires more than X 
    // of resource R to build, then we never need to build another robot mining R anymore. 
    // This is correct since we can only build one robot every minute.
    this.max_ore_robots = Arrays.stream(new int[]{blueprint.ore_robot_cost, 
                                                  blueprint.clay_robot_cost, 
                                                  blueprint.obsidian_robot_cost_ore, 
                                                  blueprint.geode_robot_cost_ore}).max().getAsInt();
    this.max_clay_robots = blueprint.obsidian_robot_cost_clay;
    this.max_obisidan_robots = blueprint.geode_robot_cost_obsidian;
  }

  public Factory(Factory other) {
    this.blueprint = other.blueprint;
    
    this.n_ore = other.n_ore;
    this.ore_robots = other.ore_robots;
    this.max_ore_robots = other.max_ore_robots;

    this.n_clay = other.n_clay;
    this.clay_robots = other.clay_robots;
    this.max_clay_robots = other.max_clay_robots;
    
    this.n_obsidian = other.n_obsidian;
    this.obisidan_robots = other.obisidan_robots;
    this.max_obisidan_robots = other.max_obisidan_robots;

    this.n_geode = other.n_geode;
    this.geode_robots = other.geode_robots;
  }

  public void print_inventory(int minute) {
    System.out.println(("=== MINUTE " + minute + " ==="));
    System.out.println("  " + this.n_ore + " Ore");
    System.out.println("  " + this.n_clay + " Clay");
    System.out.println("  " + this.n_obsidian + " Obsidian");
    System.out.println("  " + this.n_geode + " Geodes");
    System.out.println("  " + this.ore_robots + " Ore-Robots");
    System.out.println("  " + this.clay_robots + " Clay-Robots");
    System.out.println("  " + this.obisidan_robots + " Obsidian-Robots");
    System.out.println("  " + this.geode_robots + " Geode-Robots");
  }

  Roboter recently_built = Roboter.NONE;

  public void collect() {
    this.n_ore += this.ore_robots;
    this.n_clay += this.clay_robots;
    this.n_obsidian += this.obisidan_robots;
    this.n_geode += this.geode_robots;
    // If there was a robot build in the last round, it does not mine yet:
    if(this.recently_built == Roboter.NONE) {
      return;
    } else if(this.recently_built == Roboter.ORE) {
      this.n_ore--;
    } else if(this.recently_built == Roboter.CLAY) {
      this.n_clay--;
    } else if(this.recently_built == Roboter.OBSIDIAN) {
      this.n_obsidian--;
    } else if(this.recently_built == Roboter.GEODE) {
      this.n_geode--;
    }
    this.recently_built = Roboter.NONE;
  }

  // (R2) For any resource R that's not geode: 
  // If we already have X robots creating resource R, a current stock of Y for 
  // that resource, T minutes left, and no robot requires more than Z of resource 
  // R to build, and X * T+Y >= T * Z, then we never need to build another robot
  // mining R anymore.

  public boolean can_build_ore_robot(int minutes_left) {
    return this.blueprint.ore_robot_cost <= this.n_ore 
           && this.ore_robots < this.max_ore_robots // (R1)
           && this.n_ore + this.ore_robots * minutes_left < minutes_left * this.max_ore_robots; // (R2)
  }

  public void build_ore_robot() {
    this.ore_robots++;
    this.n_ore -= this.blueprint.ore_robot_cost;
    recently_built = Roboter.ORE;
  }

  public boolean can_build_clay_robot(int minutes_left) {
    return this.blueprint.clay_robot_cost <= this.n_ore 
           && this.clay_robots < this.max_clay_robots // (R1)
           && this.n_clay + this.clay_robots * minutes_left < minutes_left * this.max_clay_robots; // (R2)
  }

  public void build_clay_robot() {
    this.clay_robots++;
    this.n_ore -= this.blueprint.clay_robot_cost;
    recently_built = Roboter.CLAY;
  }

  public boolean can_build_obsidian_robot(int minutes_left) {
    return this.blueprint.obsidian_robot_cost_ore <= this.n_ore
           && this.blueprint.obsidian_robot_cost_clay <= this.n_clay
           && this.obisidan_robots < this.max_obisidan_robots // (R1)
           && this.n_obsidian + this.obisidan_robots * minutes_left < minutes_left * this.max_obisidan_robots; // (R2)
  }

  public void build_obsidian_robot() {
    this.obisidan_robots++;
    this.n_ore -= this.blueprint.obsidian_robot_cost_ore;
    this.n_clay -= this.blueprint.obsidian_robot_cost_clay;
    recently_built = Roboter.OBSIDIAN;
  }

  public boolean can_build_geode_robot() {
    return blueprint.geode_robot_cost_ore <= this.n_ore
           && blueprint.geode_robot_cost_obsidian <= this.n_obsidian;
  }

  public void build_geode_robot() {
    this.geode_robots++;
    this.n_ore -= blueprint.geode_robot_cost_ore;
    this.n_obsidian -= blueprint.geode_robot_cost_obsidian;
    recently_built = Roboter.GEODE;
  }
}