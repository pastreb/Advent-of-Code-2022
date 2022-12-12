package adventofcode;

import java.util.*;
import java.io.*;

public class HillClimbing {

  public static void main(String[] args) {
    List<List<Character>> input = read_input();
    List<List<Node>> graph = get_graph(input);
    System.out.println(solve_part_1(input, graph));
    System.out.println(solve_part_2(input, graph));
  }

  /*
  --- Day 12: Hill Climbing Algorithm ---
  You try contacting the Elves using your handheld device, but the river 
  you're following must be too low to get a decent signal.

  You ask the device for a heightmap of the surrounding area (your puzzle 
  input). The heightmap shows the local area from above broken into a grid; 
  the elevation of each square of the grid is given by a single lowercase 
  letter, where a is the lowest elevation, b is the next-lowest, and so on 
  up to the highest elevation, z.

  Also included on the heightmap are marks for your current position (S) and 
  the location that should get the best signal (E). Your current position (S) 
  has elevation a, and the location that should get the best signal (E) has 
  elevation z.

  You'd like to reach E, but to save energy, you should do it in as few 
  steps as possible. During each step, you can move exactly one square up, 
  down, left, or right. To avoid needing to get out your climbing gear, the 
  elevation of the destination square can be at most one higher than the 
  elevation of your current square; that is, if your current elevation is m, 
  you could step to elevation n, but not to elevation o. (This also means 
  that the elevation of the destination square can be much lower than the 
  elevation of your current square.)

  For example:

  Sabqponm
  abcryxxl
  accszExk
  acctuvwj
  abdefghi
  
  Here, you start in the top-left corner; your goal is near the middle. You 
  could start by moving down or right, but eventually you'll need to head 
  toward the e at the bottom. From there, you can spiral around to the goal:

  v..v<<<<
  >v.vv<<^
  .>vv>E^^
  ..v>>>^^
  ..>>>>>^
  
  In the above diagram, the symbols indicate whether the path exits each 
  square moving up (^), down (v), left (<), or right (>). The location that 
  should get the best signal is still E, and . marks unvisited squares.

  This path reaches the goal in 31 steps, the fewest possible.

  What is the fewest steps required to move from your current position to the 
  location that should get the best signal?
  */

  public static int solve_part_1(List<List<Character>> input, List<List<Node>> graph) {
    int n = input.size();
    int m = input.get(0).size();
    int source = -1; // for finding the index of 'S'
    int target = -1; // for finding the index of 'E'
    for(int i = 0; i < n; i++) {
      for(int j = 0; j < m; j++) {
        char current = input.get(i).get(j);
        if(current == 'S') {
          source = i*m+j;
        }
        if(current == 'E') {
          target = i*m+j;
        }
      }
    }
    return find_shortest_path_length(input, graph, source, target);
  }

  // Transforms the value into an integer denoting its elevation
  public static int get_elevation(char value) {
    if(value == 'S') {
      return 0;
    } else if(value == 'E') {
      return 25;
    } else {
      return ((int) value) - 97; // map a to 0, b to 1, ..., z to 25
    }
  }

  // Creates graph where every pair of squares a, b is connected if one can go from a to b
  public static List<List<Node>> get_graph(List<List<Character>> input) {
    int n = input.size();
    int m = input.get(0).size();
    List<List<Node>> graph = new ArrayList<List<Node>>();
    for(int i = 0; i < n; i++) {
      for(int j = 0; j < m; j++) {
        List<Node> adj = new ArrayList<Node>();
        char current = input.get(i).get(j);
        // Check whether we can go to the square above:
        if(i > 0 && get_elevation(input.get(i-1).get(j)) <= get_elevation(current) + 1) {
          adj.add(new Node((i-1)*m+j)); // add node for top neighbor
        }
        // Check whether we can go to the square below:
        if(i < n-1 && get_elevation(input.get(i+1).get(j)) <= get_elevation(current) + 1) {
          adj.add(new Node((i+1)*m+j)); // add node for bottom neighbor
        }
        // Check whether we can go to the square left:
        if(j > 0 && get_elevation(input.get(i).get(j-1)) <= get_elevation(current) + 1) {
          adj.add(new Node(i*m+(j-1))); // add node for left neighbor
        }
        // Check whether we can go to the square right:
        if(j < m-1 && get_elevation(input.get(i).get(j+1)) <= get_elevation(current) + 1) {
          adj.add(new Node(i*m+(j+1))); // add node for right neighbor
        }
        graph.add(adj);
      }
    }
    return graph;
  }


  // Finds the length of a shortest path between source and target in graph
  public static int find_shortest_path_length(List<List<Character>> input, List<List<Node>> graph, 
                                              int source, int target) {
    int n = input.size();
    int m = input.get(0).size();
    // Initialize the stuff we need for Dijkstra's Shortest Path Algorithm:
    int[] dist = new int[n*m];
    for(int i = 0; i < n*m; i++) {
      dist[i] = Integer.MAX_VALUE;
    }
    dist[source] = 0;
    Set<Integer> visited = new HashSet<Integer>();
    // Run Dijkstra to find the shortest path from source to target:
    int x = 0;
    Queue<Node> queue = new LinkedList<Node>();
    queue.add(new Node(source));
    while(!queue.isEmpty()) {
      int u = queue.remove().get_id();
      if(visited.contains(u)) {
        continue; // no need to process node again
      }
      visited.add(u);
      for(Node neighbor : graph.get(u)) {
        if(!visited.contains(neighbor)) {
          dist[neighbor.get_id()] = Math.min(dist[neighbor.get_id()], dist[u]+1);
          queue.add(neighbor);
        }
      }
    }
    return dist[target];
  }

  /*
  --- Part Two ---
  As you walk up the hill, you suspect that the Elves will want to turn this 
  into a hiking trail. The beginning isn't very scenic, though; perhaps you 
  can find a better starting point.

  To maximize exercise while hiking, the trail should start as low as 
  possible: elevation a. The goal is still the square marked E. However, the 
  trail should still be direct, taking the fewest steps to reach its goal. 
  So, you'll need to find the shortest path from any square at elevation a 
  to the square marked E.

  Again consider the example from above:

  Sabqponm
  abcryxxl
  accszExk
  acctuvwj
  abdefghi
  
  Now, there are six choices for starting position (five marked a, plus the 
  square marked S that counts as being at elevation a). If you start at the 
  bottom-left square, you can reach the goal most quickly:

  ...v<<<<
  ...vv<<^
  ...v>E^^
  .>v>>>^^
  >^>>>>>^

  This path reaches the goal in only 29 steps, the fewest possible.

  What is the fewest steps required to move starting from any square with 
  elevation a to the location that should get the best signal?
  */

  public static int solve_part_2(List<List<Character>> input, List<List<Node>> graph) {
    int n = input.size();
    int m = input.get(0).size();
    List<Integer> sources = new ArrayList<Integer>(); // for finding the index of all sources
    int target = -1; // for finding the index of 'E'
    for(int i = 0; i < n; i++) {
      for(int j = 0; j < m; j++) {
        char current = input.get(i).get(j);
        if(current == 'S' || current == 'a') {
          sources.add(i*m+j);
        }
        if(current == 'E') {
          target = i*m+j;
        }
      }
    }
    int min_shortest_path = Integer.MAX_VALUE;
    for(int source : sources) {
      min_shortest_path = Math.min(min_shortest_path, find_shortest_path_length(input, graph, source, target));
    }
    return min_shortest_path;
  }

  public static List<List<Character>> read_input() {
    List<List<Character>> input_list = new ArrayList<List<Character>>();
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
          String line = scanner.nextLine();
          List<Character> row = new ArrayList<Character>();
          for(int i = 0; i < line.length(); i++) {
            row.add(line.charAt(i));
          }
          input_list.add(row);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class Node {
  private int id;

  public Node(int id) {
    this.id = id;
  }

  public int get_id() {
    return this.id;
  }
}