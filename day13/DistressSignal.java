package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class HillClimbing {

  public static void main(String[] args) {
    List<String> input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 13: Distress Signal ---
  You climb the hill and again try contacting the Elves. However, you instead 
  receive a signal you weren't expecting: a distress signal.

  Your handheld device must still not be working properly; the packets from 
  the distress signal got decoded out of order. You'll need to re-order the 
  list of received packets (your puzzle input) to decode the message.

  Your list consists of pairs of packets; pairs are separated by a blank 
  line. You need to identify how many pairs of packets are in the right order.

  For example:

  [1,1,3,1,1]
  [1,1,5,1,1]

  [[1],[2,3,4]]
  [[1],4]

  [9]
  [[8,7,6]]

  [[4,4],4,4]
  [[4,4],4,4,4]

  [7,7,7,7]
  [7,7,7]

  []
  [3]

  [[[]]]
  [[]]

  [1,[2,[3,[4,[5,6,7]]]],8,9]
  [1,[2,[3,[4,[5,6,0]]]],8,9]
  
  Packet data consists of lists and integers. Each list starts with [, ends 
  with ], and contains zero or more comma-separated values (either integers 
  or other lists). Each packet is always a list and appears on its own line.

  When comparing two values, the first value is called left and the second 
  value is called right. Then:

  - If both values are integers, the lower integer should come first. If 
    the left integer is lower than the right integer, the inputs are in 
    the right order. If the left integer is higher than the right integer, 
    the inputs are not in the right order. Otherwise, the inputs are the 
    same integer; continue checking the next part of the input.
  - If both values are lists, compare the first value of each list, then 
    the second value, and so on. If the left list runs out of items first, 
    the inputs are in the right order. If the right list runs out of items 
    first, the inputs are not in the right order. If the lists are the 
    same length and no comparison makes a decision about the order, 
    continue checking the next part of the input.
  - If exactly one value is an integer, convert the integer to a list 
    which contains that integer as its only value, then retry the 
    comparison. For example, if comparing [0,0,0] and 2, convert the right 
    value to [2] (a list containing 2); the result is then found by instead 
    comparing [0,0,0] and [2].
  
    Using these rules, you can determine which of the pairs in the example are 
    in the right order:

  == Pair 1 ==
  - Compare [1,1,3,1,1] vs [1,1,5,1,1]
    - Compare 1 vs 1
    - Compare 1 vs 1
    - Compare 3 vs 5
      - Left side is smaller, so inputs are in the right order

  == Pair 2 ==
  - Compare [[1],[2,3,4]] vs [[1],4]
    - Compare [1] vs [1]
      - Compare 1 vs 1
    - Compare [2,3,4] vs 4
      - Mixed types; convert right to [4] and retry comparison
      - Compare [2,3,4] vs [4]
        - Compare 2 vs 4
          - Left side is smaller, so inputs are in the right order

  == Pair 3 ==
  - Compare [9] vs [[8,7,6]]
    - Compare 9 vs [8,7,6]
      - Mixed types; convert left to [9] and retry comparison
      - Compare [9] vs [8,7,6]
        - Compare 9 vs 8
          - Right side is smaller, so inputs are not in the right order

  == Pair 4 ==
  - Compare [[4,4],4,4] vs [[4,4],4,4,4]
    - Compare [4,4] vs [4,4]
      - Compare 4 vs 4
      - Compare 4 vs 4
    - Compare 4 vs 4
    - Compare 4 vs 4
    - Left side ran out of items, so inputs are in the right order

  == Pair 5 ==
  - Compare [7,7,7,7] vs [7,7,7]
    - Compare 7 vs 7
    - Compare 7 vs 7
    - Compare 7 vs 7
    - Right side ran out of items, so inputs are not in the right order

  == Pair 6 ==
  - Compare [] vs [3]
    - Left side ran out of items, so inputs are in the right order

  == Pair 7 ==
  - Compare [[[]]] vs [[]]
    - Compare [[]] vs []
      - Right side ran out of items, so inputs are not in the right order

  == Pair 8 ==
  - Compare [1,[2,[3,[4,[5,6,7]]]],8,9] vs [1,[2,[3,[4,[5,6,0]]]],8,9]
    - Compare 1 vs 1
    - Compare [2,[3,[4,[5,6,7]]]] vs [2,[3,[4,[5,6,0]]]]
      - Compare 2 vs 2
      - Compare [3,[4,[5,6,7]]] vs [3,[4,[5,6,0]]]
        - Compare 3 vs 3
        - Compare [4,[5,6,7]] vs [4,[5,6,0]]
          - Compare 4 vs 4
          - Compare [5,6,7] vs [5,6,0]
            - Compare 5 vs 5
            - Compare 6 vs 6
            - Compare 7 vs 0
              - Right side is smaller, so inputs are not in the right order
  
  What are the indices of the pairs that are already in the right order? 
  (The first pair has index 1, the second pair has index 2, and so on.) 
  In the above example, the pairs in the right order are 1, 2, 4, and 6; 
  the sum of these indices is 13.

  Determine which pairs of packets are already in the right order. What is 
  the sum of the indices of those pairs?
  */

  public static int solve_part_1(List<String> input) {
    int sum = 0;
    int pair_number = 1;
    for(int i = 0; i < input.size(); i+=2) {
      if(check(input.get(i), input.get(i+1)) >= 0) {
        sum += pair_number;
        // System.out.println("true");
      } else {
        // System.out.println("false");
      }
      pair_number++;
      // System.out.println();
    }
    return sum;
  }

  // Returns 1 if okay
  // Returns 0 if inconclusive
  // Returns -1 if not okay
  public static int check(String a, String b) {
    // System.out.println(a + " vs " + b);
    a = a.startsWith("[") ? a.substring(1, a.length()-1) : a; // [...]
    b = b.startsWith("[") ? b.substring(1, b.length()-1) : b; // [...]
    while(a.length() > 0 && b.length() > 0) {
      String next_a = find_next(a);
      String next_b = find_next(b);
      if(next_a.startsWith("[") && next_b.startsWith("[")) {
        int res = check(next_a, next_b);
        if(res != 0) {
          return res; 
        }
      } else if(next_a.startsWith("[")) {
        int res = check(next_a, "[" + next_b + "]");
        if(res != 0) {
          return res; 
        }
      } else if(next_b.startsWith("[")) {
        int res = check("[" + next_a + "]", next_b);
        if(res != 0) {
          return res; 
        }
      } else { // All integers
        if(Integer.parseInt(next_a) > Integer.parseInt(next_b)) {
          return -1;
        } else if(Integer.parseInt(next_a) < Integer.parseInt(next_b)) {
          return 1;
        }
      }
      a = a.substring(next_a.length());
      a = a.startsWith(",") ? a.substring(1) : a;
      b = b.substring(next_b.length());
      b = b.startsWith(",") ? b.substring(1) : b;
    }
    if(a.length() > 0) { // a is longer
      return -1;
    } else if(b.length() > 0) { // b is longer
      return 1;
    }
    return 0; // same length
  }

  public static String find_next(String s) {
    if(s.charAt(0) == '[') {
      int i = 1;
      int left = 1;
      int right = 0;
      while(left > right) {
        if(s.charAt(i) == ']') {
          right++;
        } else if(s.charAt(i) == '[') {
          left++;
        }
        i++;
      }
      return s.substring(0, i);
    } else {
      Pattern pattern = Pattern.compile("\\d*");
      Matcher matcher = pattern.matcher(s);
      if(matcher.find()) {
        String number = matcher.group();
        return number.substring(0, number.length());
      }
    }
    return "";
  }

  /*
  --- Part Two ---
  Now, you just need to put all of the packets in the right order. Disregard 
  the blank lines in your list of received packets.

  The distress signal protocol also requires that you include two additional divider packets:

  [[2]]
  [[6]]

  Using the same rules as before, organize all packets - the ones in your 
  list of received packets as well as the two divider packets - into the correct order.

  For the example above, the result of putting the packets in the correct order is:

  []
  [[]]
  [[[]]]
  [1,1,3,1,1]
  [1,1,5,1,1]
  [[1],[2,3,4]]
  [1,[2,[3,[4,[5,6,0]]]],8,9]
  [1,[2,[3,[4,[5,6,7]]]],8,9]
  [[1],4]
  [[2]]
  [3]
  [[4,4],4,4]
  [[4,4],4,4,4]
  [[6]]
  [7,7,7]
  [7,7,7,7]
  [[8,7,6]]
  [9]
  
  Afterward, locate the divider packets. To find the decoder key for this 
  distress signal, you need to determine the indices of the two divider 
  packets and multiply them together. (The first packet is at index 1, the 
  second packet is at index 2, and so on.) In this example, the divider 
  packets are 10th and 14th, and so the decoder key is 140.

  Organize all of the packets into the correct order. What is the decoder key 
  for the distress signal?
  */

  public static int solve_part_2(List<String> input) {
    String[] input_array = new String[input.size()+2];
    for(int i = 0; i < input.size(); i++) {
      input_array[i] = input.get(i);
    }
    input_array[input.size()] = ("[[2]]");
    input_array[input.size()+1] = ("[[6]]");
    // Bubble sort action <3
    for(int i = 0; i < input_array.length; i++) {
      for(int j = 0; j < input_array.length-1-i; j++) {
        if(check(input_array[j], input_array[j+1]) == -1) { // out of order
          // System.out.println("Switching " + input_array[j] + " and " + input_array[j+1]);
          String temp = input_array[j];
          input_array[j] = input_array[j+1];
          input_array[j+1] = temp;
        }
        // System.out.println();
      }
    }
    int index = -1;
    for(int i = 0; i < input_array.length; i++) {
      if(input_array[i] == "[[2]]") {
        index = i+1;
      }
      if(input_array[i] == "[[6]]") {
        index *= (i+1);
      }
    }
    return index;
  }

  public static List<String> read_input() {
    List<String> input_list = new ArrayList<String>();
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
          String line = scanner.nextLine();
          if(line.length() > 0) {
            input_list.add(line);
          }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}