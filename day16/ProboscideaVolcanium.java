package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class ProboscideaVolcanium {

  public static void main(String[] args) {
    Map<String,Valve> input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 16: Proboscidea Volcanium ---
  The sensors have led you to the origin of the distress signal: yet another 
  handheld device, just like the one the Elves gave you. However, you don't 
  see any Elves around; instead, the device is surrounded by elephants! They 
  must have gotten lost in these tunnels, and one of the elephants apparently 
  figured out how to turn on the distress signal.

  The ground rumbles again, much stronger this time. What kind of cave is 
  this, exactly? You scan the cave with your handheld device; it reports 
  mostly igneous rock, some ash, pockets of pressurized gas, magma... this 
  isn't just a cave, it's a volcano!

  You need to get the elephants out of here, quickly. Your device estimates 
  that you have 30 minutes before the volcano erupts, so you don't have time 
  to go back out the way you came in.

  You scan the cave for other options and discover a network of pipes and 
  pressure-release valves. You aren't sure how such a system got into a 
  volcano, but you don't have time to complain; your device produces a 
  report (your puzzle input) of each valve's flow rate if it were opened 
  (in pressure per minute) and the tunnels you could use to move between the 
  valves.

  There's even a valve in the room you and the elephants are currently 
  standing in labeled AA. You estimate it will take you one minute to open a 
  single valve and one minute to follow any tunnel from one valve to another. 
  What is the most pressure you could release?

  For example, suppose you had the following scan output:

  Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
  Valve BB has flow rate=13; tunnels lead to valves CC, AA
  Valve CC has flow rate=2; tunnels lead to valves DD, BB
  Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
  Valve EE has flow rate=3; tunnels lead to valves FF, DD
  Valve FF has flow rate=0; tunnels lead to valves EE, GG
  Valve GG has flow rate=0; tunnels lead to valves FF, HH
  Valve HH has flow rate=22; tunnel leads to valve GG
  Valve II has flow rate=0; tunnels lead to valves AA, JJ
  Valve JJ has flow rate=21; tunnel leads to valve II
  
  All of the valves begin closed. You start at valve AA, but it must be 
  damaged or jammed or something: its flow rate is 0, so there's no point 
  in opening it. However, you could spend one minute moving to valve BB 
  and another minute opening it; doing so would release pressure during 
  the remaining 28 minutes at a flow rate of 13, a total eventual pressure 
  release of 28 * 13 = 364. Then, you could spend your third minute moving 
  to valve CC and your fourth minute opening it, providing an additional 
  26 minutes of eventual pressure release at a flow rate of 2, or 52 total 
  pressure released by valve CC.

  Making your way through the tunnels like this, you could probably open many 
  or all of the valves by the time 30 minutes have elapsed. However, you need 
  to release as much pressure as possible, so you'll need to be methodical. 
  Instead, consider this approach:

  == Minute 1 ==
  No valves are open.
  You move to valve DD.

  == Minute 2 ==
  No valves are open.
  You open valve DD.

  == Minute 3 ==
  Valve DD is open, releasing 20 pressure.
  You move to valve CC.

  == Minute 4 ==
  Valve DD is open, releasing 20 pressure.
  You move to valve BB.

  == Minute 5 ==
  Valve DD is open, releasing 20 pressure.
  You open valve BB.

  == Minute 6 ==
  Valves BB and DD are open, releasing 33 pressure.
  You move to valve AA.

  == Minute 7 ==
  Valves BB and DD are open, releasing 33 pressure.
  You move to valve II.

  == Minute 8 ==
  Valves BB and DD are open, releasing 33 pressure.
  You move to valve JJ.

  == Minute 9 ==
  Valves BB and DD are open, releasing 33 pressure.
  You open valve JJ.

  == Minute 10 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You move to valve II.

  == Minute 11 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You move to valve AA.

  == Minute 12 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You move to valve DD.

  == Minute 13 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You move to valve EE.

  == Minute 14 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You move to valve FF.

  == Minute 15 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You move to valve GG.

  == Minute 16 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You move to valve HH.

  == Minute 17 ==
  Valves BB, DD, and JJ are open, releasing 54 pressure.
  You open valve HH.

  == Minute 18 ==
  Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
  You move to valve GG.

  == Minute 19 ==
  Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
  You move to valve FF.

  == Minute 20 ==
  Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
  You move to valve EE.

  == Minute 21 ==
  Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
  You open valve EE.

  == Minute 22 ==
  Valves BB, DD, EE, HH, and JJ are open, releasing 79 pressure.
  You move to valve DD.

  == Minute 23 ==
  Valves BB, DD, EE, HH, and JJ are open, releasing 79 pressure.
  You move to valve CC.

  == Minute 24 ==
  Valves BB, DD, EE, HH, and JJ are open, releasing 79 pressure.
  You open valve CC.

  == Minute 25 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

  == Minute 26 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

  == Minute 27 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

  == Minute 28 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

  == Minute 29 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

  == Minute 30 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.
  
  This approach lets you release the most pressure possible in 30 minutes 
  with this valve layout, 1651.

  Work out the steps to release the most pressure in 30 minutes. What is the 
  most pressure you can release?
  */

  public static int solve_part_1(Map<String,Valve> input) {
    int duration = 30;

    // For valve "XY" and 0 <= i <= 30, max_pressure_1.get("XY").get(i) stores the max total 
    // pressure amount if I MOVE TO valve "XY" in minute i:
    Map<String, List<Integer>> max_pressure_1 = new HashMap<String, List<Integer>>();
    // For valve "XY" and 0 <= i <= 30, opened_valves_1.get("XY").get(i) stores all the valves
    // that are open in minute i to achieve this max total pressure amount:
    Map<String, List<Set<String>>> opened_valves_1 = new HashMap<String, List<Set<String>>>();

    // For valve "XY" and 0 <= i <= 30, max_pressure_2.get("XY").get(i) stores the max total 
    // pressure amount if I OPEN valve "XY" in minute i:
    Map<String, List<Integer>> max_pressure_2 = new HashMap<String, List<Integer>>();
    // For valve "XY" and 0 <= i <= 30, opened_valves_2.get("XY").get(i) stores all the valves
    // that are open in minute i to achieve this max total pressure amount:
    Map<String, List<Set<String>>> opened_valves_2 = new HashMap<String, List<Set<String>>>();

    // Initialization:
    for(Valve valve : input.values()) {
      max_pressure_1.put(valve.name, new ArrayList<Integer>(duration + 1));
      opened_valves_1.put(valve.name, new ArrayList<Set<String>>(duration + 1));
      max_pressure_2.put(valve.name, new ArrayList<Integer>(duration + 1));
      opened_valves_2.put(valve.name, new ArrayList<Set<String>>(duration + 1));
      if(valve.name.equals("AA")) {
        max_pressure_1.get(valve.name).add(0);
      } else {
        max_pressure_1.get(valve.name).add(-1);
      }
      opened_valves_1.get(valve.name).add(new HashSet<String>());
      max_pressure_2.get(valve.name).add(-1);
      opened_valves_2.get(valve.name).add(new HashSet<String>());
      for(int i = 1; i <= duration; i++) {
        max_pressure_1.get(valve.name).add(-1);
        opened_valves_1.get(valve.name).add(new HashSet<String>());
        max_pressure_2.get(valve.name).add(-1);
        opened_valves_2.get(valve.name).add(new HashSet<String>());
      }
    }

    for(int i = 1; i <= duration; i++) {
      for(Valve valve : input.values()) {
        // Consider opening valve now:
        int current_pressure = max_pressure_1.get(valve.name).get(i-1);
        if(current_pressure >= 0 && !opened_valves_1.get(valve.name).get(i-1).contains(valve.name)) { // allowed to open valve
          // Include the pressure released between step i-1 and i:
          for(String open_valve : opened_valves_1.get(valve.name).get(i-1)) {
            current_pressure += input.get(open_valve).flow_rate;
          }
          max_pressure_2.get(valve.name).remove(i);
          max_pressure_2.get(valve.name).add(i, current_pressure);
          opened_valves_2.get(valve.name).remove(i);
          opened_valves_2.get(valve.name).add(i, new HashSet<String>());
          opened_valves_2.get(valve.name).get(i).addAll(opened_valves_1.get(valve.name).get(i-1));
          opened_valves_2.get(valve.name).get(i).add(valve.name); // valve was just opened
        }
        // Consider coming here from a neighbor:
        for(String neighbor : valve.neighbors) {
          // Case 1: NEIGHBOR DID NOT JUST OPEN
          // Get the pressure released so far:
          int neighbor_pressure = max_pressure_1.get(neighbor).get(i-1);
          if(neighbor_pressure >= 0) { // Otherwise unreachable
            // Get the Valves that are already open:
            Set<String> neighbor_opened_valves = opened_valves_1.get(neighbor).get(i-1);
            // Include the pressure released between step i-1 and i:
            for(String open_valve : neighbor_opened_valves) {
              neighbor_pressure += input.get(open_valve).flow_rate;
            }
            // Possibly move from neighbor to valve:
            if(neighbor_pressure > max_pressure_1.get(valve.name).get(i)) {
              max_pressure_1.get(valve.name).remove(i);
              max_pressure_1.get(valve.name).add(i, neighbor_pressure);
              opened_valves_1.get(valve.name).remove(i);
              opened_valves_1.get(valve.name).add(i, new HashSet<String>());
              opened_valves_1.get(valve.name).get(i).addAll(neighbor_opened_valves);
            }
          }
          // Case 2: NEIGHBOR DID JUST OPEN
          // Get the pressure released so far:
          neighbor_pressure = max_pressure_2.get(neighbor).get(i-1);
          if(neighbor_pressure >= 0) { // Otherwise unreachable
            // Get the Valves that are already open:
            Set<String> neighbor_opened_valves = opened_valves_2.get(neighbor).get(i-1);
            // Include the pressure released between step i-1 and i:
            for(String open_valve : neighbor_opened_valves) {
              neighbor_pressure += input.get(open_valve).flow_rate;
            }
            // Possibly move from neighbor to valve:
            if(neighbor_pressure > max_pressure_1.get(valve.name).get(i)) {
              max_pressure_1.get(valve.name).remove(i);
              max_pressure_1.get(valve.name).add(i, neighbor_pressure);
              opened_valves_1.get(valve.name).remove(i);
              opened_valves_1.get(valve.name).add(i, new HashSet<String>());
              opened_valves_1.get(valve.name).get(i).addAll(neighbor_opened_valves);
            }
          }
        }
      }
    }
    int max = 0;
    for(Valve valve : input.values()) {
      max = Math.max(max, max_pressure_1.get(valve.name).get(duration));
      max = Math.max(max, max_pressure_2.get(valve.name).get(duration));
    }
    return max;
  }

  /*
  --- Part Two ---
  You're worried that even with an optimal approach, the pressure released 
  won't be enough. What if you got one of the elephants to help you?

  It would take you 4 minutes to teach an elephant how to open the right 
  valves in the right order, leaving you with only 26 minutes to actually 
  execute your plan. Would having two of you working together be better, even 
  if it means having less time? (Assume that you teach the elephant before 
  opening any valves yourself, giving you both the same full 26 minutes.)

  In the example above, you could teach the elephant to help you as follows:

  == Minute 1 ==
  No valves are open.
  You move to valve II.
  The elephant moves to valve DD.

  == Minute 2 ==
  No valves are open.
  You move to valve JJ.
  The elephant opens valve DD.

  == Minute 3 ==
  Valve DD is open, releasing 20 pressure.
  You open valve JJ.
  The elephant moves to valve EE.

  == Minute 4 ==
  Valves DD and JJ are open, releasing 41 pressure.
  You move to valve II.
  The elephant moves to valve FF.

  == Minute 5 ==
  Valves DD and JJ are open, releasing 41 pressure.
  You move to valve AA.
  The elephant moves to valve GG.

  == Minute 6 ==
  Valves DD and JJ are open, releasing 41 pressure.
  You move to valve BB.
  The elephant moves to valve HH.

  == Minute 7 ==
  Valves DD and JJ are open, releasing 41 pressure.
  You open valve BB.
  The elephant opens valve HH.

  == Minute 8 ==
  Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
  You move to valve CC.
  The elephant moves to valve GG.

  == Minute 9 ==
  Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
  You open valve CC.
  The elephant moves to valve FF.

  == Minute 10 ==
  Valves BB, CC, DD, HH, and JJ are open, releasing 78 pressure.
  The elephant moves to valve EE.

  == Minute 11 ==
  Valves BB, CC, DD, HH, and JJ are open, releasing 78 pressure.
  The elephant opens valve EE.

  (At this point, all valves are open.)

  == Minute 12 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

  ...

  == Minute 20 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

  ...

  == Minute 26 ==
  Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.
  
  With the elephant helping, after 26 minutes, the best you could do would 
  release a total of 1707 pressure.

  With you and an elephant working together for 26 minutes, what is the most 
  pressure you could release?
  */

  public static int solve_part_2(Map<String,Valve> input) {
    return 0;
  }

  public static Map<String,Valve> read_input() {
    Map<String,Valve> input_map = new HashMap<String,Valve>();
    Pattern number_pattern = Pattern.compile("-?\\d+");
    Pattern name_pattern = Pattern.compile("[A-Z]{2}");
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Matcher name_matcher = name_pattern.matcher(line);
        Matcher number_matcher = number_pattern.matcher(line);
        if(name_matcher.find() && number_matcher.find()) {
          Valve new_valve = new Valve(name_matcher.group(), Integer.valueOf(number_matcher.group()));
          while(name_matcher.find()) {
            new_valve.add_neighbor(name_matcher.group());
          }
          input_map.put(new_valve.name, new_valve);
        }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_map;
  }
}

class Valve {
  public String name;
  public int flow_rate;
  public List<String> neighbors;

  public Valve(String name, int flow_rate) {
    this.name = name;
    this.flow_rate = flow_rate;
    this.neighbors = new ArrayList<String>();
  }

  public void add_neighbor(String other_name) {
    neighbors.add(other_name);
  }
}