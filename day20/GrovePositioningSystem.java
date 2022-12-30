package adventofcode;

import java.util.*;
import java.io.*;
import java.math.BigInteger;

public class GrovePositioningSystem {

  public static void main(String[] args) {
    List<MyInteger> input = read_input();
    System.out.println(solve_part_1(input));
    long decription_key = 811589153;
    System.out.println(solve_part_2(input, decription_key));
  }

  /*
  --- Day 20: Grove Positioning System ---
  It's finally time to meet back up with the Elves. When you try to contact 
  them, however, you get no reply. Perhaps you're out of range?

  You know they're headed to the grove where the star fruit grows, so if you 
  can figure out where that is, you should be able to meet back up with them.

  Fortunately, your handheld device has a file (your puzzle input) that 
  contains the grove's coordinates! Unfortunately, the file is encrypted - 
  just in case the device were to fall into the wrong hands.

  Maybe you can decrypt it?

  When you were still back at the camp, you overheard some Elves talking 
  about coordinate file encryption. The main operation involved in decrypting 
  the file is called mixing.

  The encrypted file is a list of numbers. To mix the file, move each number 
  forward or backward in the file a number of positions equal to the value of 
  the number being moved. The list is circular, so moving a number off one 
  end of the list wraps back around to the other end as if the ends were 
  connected.

  For example, to move the 1 in a sequence like 4, 5, 6, 1, 7, 8, 9, the 1 
  moves one position forward: 4, 5, 6, 7, 1, 8, 9. To move the -2 in a 
  sequence like 4, -2, 5, 6, 7, 8, 9, the -2 moves two positions backward, 
  wrapping around: 4, 5, 6, 7, 8, -2, 9.

  The numbers should be moved in the order they originally appear in the 
  encrypted file. Numbers moving around during the mixing process do not 
  change the order in which the numbers are moved.

  Consider this encrypted file:

  1
  2
  -3
  3
  -2
  0
  4
  
  Mixing this file proceeds as follows:

  Initial arrangement:
  1, 2, -3, 3, -2, 0, 4

  1 moves between 2 and -3:
  2, 1, -3, 3, -2, 0, 4

  2 moves between -3 and 3:
  1, -3, 2, 3, -2, 0, 4

  -3 moves between -2 and 0:
  1, 2, 3, -2, -3, 0, 4

  3 moves between 0 and 4:
  1, 2, -2, -3, 0, 3, 4

  -2 moves between 4 and 1:
  1, 2, -3, 0, 3, 4, -2

  0 does not move:
  1, 2, -3, 0, 3, 4, -2

  4 moves between -3 and 0:
  1, 2, -3, 4, 0, 3, -2
  
  Then, the grove coordinates can be found by looking at the 1000th, 2000th, 
  and 3000th numbers after the value 0, wrapping around the list as 
  necessary. In the above example, the 1000th number after 0 is 4, the 
  2000th is -3, and the 3000th is 2; adding these together produces 3.

  Mix your encrypted file exactly once. What is the sum of the three numbers 
  that form the grove coordinates?
  */

  public static long solve_part_1(List<MyInteger> input) {
    int n = input.size();
    List<MyInteger> decrypted_input = new LinkedList();
    MyInteger zero = null;
    for(int i = 0; i < n; i++) {
      decrypted_input.add(input.get(i));
      if(input.get(i).value == 0) {
        zero = input.get(i);
      }
    }
    // print_list(decrypted_input);
    for(int i = 0; i < n; i++) {
      MyInteger current = input.get(i);
      int current_index = decrypted_input.indexOf(current);
      int new_index = current_index + current.value;
      while(new_index <= 0) {
        new_index += (n-1);
      }
      while(new_index >= n) {
        new_index -= (n-1);
      }
      decrypted_input.remove(current);
      decrypted_input.add(new_index, current);
      // print_list(decrypted_input);
    }
    int index_of_zero = decrypted_input.indexOf(zero);
    return decrypted_input.get((index_of_zero + 1000) % input.size()).value 
           + decrypted_input.get((index_of_zero + 2000) % input.size()).value 
           + decrypted_input.get((index_of_zero + 3000) % input.size()).value;
  }

  public static void print_list(List<MyInteger> list) {
    for(int k = 0; k < list.size(); k++) {
      MyInteger next = list.get(k);
      System.out.print((next.big_value == null? next.value : next.big_value) + " ");
    }
    System.out.println();
  }

  /*
  --- Part Two ---
  The grove coordinate values seem nonsensical. While you ponder the 
  mysteries of Elf encryption, you suddenly remember the rest of the 
  decryption routine you overheard back at camp.

  First, you need to apply the decryption key, 811589153. Multiply each 
  number by the decryption key before you begin; this will produce the actual 
  list of numbers to mix.

  Second, you need to mix the list of numbers ten times. The order in which 
  the numbers are mixed does not change during mixing; the numbers are still 
  moved in the order they appeared in the original, pre-mixed list. (So, if 
  -3 appears fourth in the original list of numbers to mix, -3 will be the 
  fourth number to move during each round of mixing.)

  Using the same example as above:

  Initial arrangement:
  811589153, 1623178306, -2434767459, 2434767459, -1623178306, 0, 3246356612

  After 1 round of mixing:
  0, -2434767459, 3246356612, -1623178306, 2434767459, 1623178306, 811589153

  After 2 rounds of mixing:
  0, 2434767459, 1623178306, 3246356612, -2434767459, -1623178306, 811589153

  After 3 rounds of mixing:
  0, 811589153, 2434767459, 3246356612, 1623178306, -1623178306, -2434767459

  After 4 rounds of mixing:
  0, 1623178306, -2434767459, 811589153, 2434767459, 3246356612, -1623178306

  After 5 rounds of mixing:
  0, 811589153, -1623178306, 1623178306, -2434767459, 3246356612, 2434767459

  After 6 rounds of mixing:
  0, 811589153, -1623178306, 3246356612, -2434767459, 1623178306, 2434767459

  After 7 rounds of mixing:
  0, -2434767459, 2434767459, 1623178306, -1623178306, 811589153, 3246356612

  After 8 rounds of mixing:
  0, 1623178306, 3246356612, 811589153, -2434767459, 2434767459, -1623178306

  After 9 rounds of mixing:
  0, 811589153, 1623178306, -2434767459, 3246356612, 2434767459, -1623178306

  After 10 rounds of mixing:
  0, -2434767459, 1623178306, 3246356612, -1623178306, 2434767459, 811589153
  
  The grove coordinates can still be found in the same way. Here, the 1000th 
  number after 0 is 811589153, the 2000th is 2434767459, and the 3000th is 
  -1623178306; adding these together produces 1623178306.

  Apply the decryption key and mix your encrypted file ten times. What is the 
  sum of the three numbers that form the grove coordinates?
  */

  public static BigInteger solve_part_2(List<MyInteger> input, long decription_key) {
    int n = input.size();
    List<MyInteger> decrypted_input = new LinkedList();
    MyInteger zero = null;
    for(int i = 0; i < n; i++) {
      decrypted_input.add(input.get(i));
      if(input.get(i).value == 0) {
        zero = input.get(i);
      }
      input.get(i).set_big_value(decription_key);
    }
    for(int round = 0; round < 10; round++) {
      for(int i = 0; i < n; i++) {
        MyInteger current = input.get(i);
        int current_index = decrypted_input.indexOf(current);
        int offset = current.big_value.mod(BigInteger.valueOf(input.size()-1)).intValue();
        int new_index = current_index + offset;
        while(new_index >= n) {
          new_index -= (n-1);
        }
        decrypted_input.remove(current);
        decrypted_input.add(new_index, current);        
      }
    }
    int index_of_zero = decrypted_input.indexOf(zero);
    BigInteger out = BigInteger.ZERO;
    out = out.add(decrypted_input.get((index_of_zero + 1000) % input.size()).big_value);
    out = out.add(decrypted_input.get((index_of_zero + 2000) % input.size()).big_value);
    out = out.add(decrypted_input.get((index_of_zero + 3000) % input.size()).big_value);
    return out;
  }

  public static List<MyInteger> read_input() {
    List<MyInteger> input_list = new ArrayList<MyInteger>();
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        input_list.add(new MyInteger(Integer.parseInt(scanner.nextLine())));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class MyInteger {
  public int value;
  public BigInteger big_value;

  // Constructor for part 1
  public MyInteger(int value) {
    this.value = value;
  }

  // For part 2
  public void set_big_value(long factor) {
    this.big_value = BigInteger.valueOf(value);
    this.big_value = this.big_value.multiply(BigInteger.valueOf(factor));
  }

}