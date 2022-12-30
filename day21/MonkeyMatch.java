package adventofcode;

import java.util.*;
import java.io.*;

public class MonkeyMatch {

  public static void main(String[] args) {
    List<Monkey> input = read_input();
    System.out.println(solve_part_1(input));
    System.out.println(solve_part_2(input));
  }

  /*
  --- Day 21: Monkey Math ---
  The monkeys are back! You're worried they're going to try to steal your 
  stuff again, but it seems like they're just holding their ground and making 
  various monkey noises at you.

  Eventually, one of the elephants realizes you don't speak monkey and comes 
  over to interpret. As it turns out, they overheard you talking about trying 
  to find the grove; they can show you a shortcut if you answer their riddle.

  Each monkey is given a job: either to yell a specific number or to yell 
  the result of a math operation. All of the number-yelling monkeys know 
  their number from the start; however, the math operation monkeys need to 
  wait for two other monkeys to yell a number, and those two other monkeys 
  might also be waiting on other monkeys.

  Your job is to work out the number the monkey named root will yell before 
  the monkeys figure it out themselves.

  For example:

  root: pppw + sjmn
  dbpl: 5
  cczh: sllz + lgvd
  zczc: 2
  ptdq: humn - dvpt
  dvpt: 3
  lfqf: 4
  humn: 5
  ljgn: 2
  sjmn: drzm * dbpl
  sllz: 4
  pppw: cczh / lfqf
  lgvd: ljgn * ptdq
  drzm: hmdt - zczc
  hmdt: 32
  
  Each line contains the name of a monkey, a colon, and then the job of that 
  monkey:

    - A lone number means the monkey's job is simply to yell that number.
    - A job like aaaa + bbbb means the monkey waits for monkeys aaaa and 
      bbbb to yell each of their numbers; the monkey then yells the sum of 
      those two numbers.
    - aaaa - bbbb means the monkey yells aaaa's number minus bbbb's number.
    - Job aaaa * bbbb will yell aaaa's number multiplied by bbbb's number.
    - Job aaaa / bbbb will yell aaaa's number divided by bbbb's number.
  
  So, in the above example, monkey drzm has to wait for monkeys hmdt and zczc 
  to yell their numbers. Fortunately, both hmdt and zczc have jobs that involve 
  simply yelling a single number, so they do this immediately: 32 and 2. Monkey 
  drzm can then yell its number by finding 32 minus 2: 30.

  Then, monkey sjmn has one of its numbers (30, from monkey drzm), and already 
  has its other number, 5, from dbpl. This allows it to yell its own number by 
  finding 30 multiplied by 5: 150.

  This process continues until root yells a number: 152.

  However, your actual situation involves considerably more monkeys. 
  What number will the monkey named root yell?
  */

  public static long solve_part_1(List<Monkey> input) {
    Stack<Monkey> stack = new Stack<Monkey>();
    for(Monkey monkey : input) {
      if(!(monkey instanceof BinOpMonkey)) { // first consider all number monkeys
        stack.push(monkey);
      }
    }
    while(!stack.empty()) {
      Monkey monkey = stack.pop();
      for(Monkey other_monkey : input) {
        if(other_monkey instanceof BinOpMonkey) {
          BinOpMonkey bin_op_monkey = (BinOpMonkey) other_monkey;
          if(bin_op_monkey.operand_1_given_by.equals(monkey.name)) {
            bin_op_monkey.operand_1 = monkey.number;
          } 
          if(bin_op_monkey.operand_2_given_by.equals(monkey.name)) {
            bin_op_monkey.operand_2 = monkey.number;
          }
          if(bin_op_monkey.possibly_evaluate()) {
            if(bin_op_monkey.name.equals("root")) {
              return bin_op_monkey.number;
            } else {
              stack.push(bin_op_monkey);
            }
          }
        }
      }
    }
    return 0;
  }

  /*
  --- Part Two ---
  Due to some kind of monkey-elephant-human mistranslation, you seem to have 
  misunderstood a few key details about the riddle.

  First, you got the wrong job for the monkey named root; specifically, you 
  got the wrong math operation. The correct operation for monkey root should 
  be =, which means that it still listens for two numbers (from the same two 
  monkeys as before), but now checks that the two numbers match.

  Second, you got the wrong monkey for the job starting with humn:. It isn't 
  a monkey - it's you. Actually, you got the job wrong, too: you need to figure 
  out what number you need to yell so that root's equality check passes. 
  (The number that appears after humn: in your input is now irrelevant.)

  In the above example, the number you need to yell to pass root's equality 
  test is 301. (This causes root to get the same number, 150, from both of 
  its monkeys.)

  What number do you yell to pass root's equality test?
  */

  public static int solve_part_2(List<Monkey> input) {
    return 0;
  }

  public static List<Monkey> read_input() {
    List<Monkey> input_list = new ArrayList<Monkey>();
    try {
      File input = new File("input.txt");
      Scanner scanner = new Scanner(input);
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String name = line.substring(0,4);
        if(line.length() == 17) { // BinOp
          String operand_1_given_by = line.substring(6,10);
          String operand_2_given_by = line.substring(13,17);
          if(line.contains("+")) {
            input_list.add(new BinOpMonkey(name, operand_1_given_by, operand_2_given_by, BinOp.Add));
          } else if(line.contains("-")) {
            input_list.add(new BinOpMonkey(name, operand_1_given_by, operand_2_given_by, BinOp.Sub));
          } else if(line.contains("*")) {
            input_list.add(new BinOpMonkey(name, operand_1_given_by, operand_2_given_by, BinOp.Mul));
          } else if(line.contains("/")) {
            input_list.add(new BinOpMonkey(name, operand_1_given_by, operand_2_given_by, BinOp.Div));
          }
        } else { // Number
          long number = Long.parseLong(line.substring(6));
          input_list.add(new Monkey(name, number));
        }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return input_list;
  }
}

class Monkey { 
  public String name;
  public long number;

  public Monkey(String name, long number) {
    this.name = name;
    this.number = number;
  }
}

class BinOpMonkey extends Monkey {
  
  public BinOp operation;
  public long operand_1 = Long.MIN_VALUE;
  public String operand_1_given_by;
  public long operand_2 = Long.MIN_VALUE;
  public String operand_2_given_by;

  public BinOpMonkey(String name, String operand_1_given_by, String operand_2_given_by, BinOp operation) {
    super(name, Long.MIN_VALUE);
    this.operand_1_given_by = operand_1_given_by;
    this.operand_2_given_by = operand_2_given_by;
    this.operation = operation;
  }

  public Boolean possibly_evaluate() {
    if(this.number != Long.MIN_VALUE || this.operand_1 == Long.MIN_VALUE || this.operand_2 == Long.MIN_VALUE) {
      return false;
    }
    if(this.operation == BinOp.Add) {
      this.number = this.operand_1 + this.operand_2;
    } else if(this.operation == BinOp.Sub) {
      this.number = this.operand_1 - this.operand_2;
    } else if(this.operation == BinOp.Mul) {
      this.number = this.operand_1 * this.operand_2;
    } else if(this.operation == BinOp.Div) {
      this.number = this.operand_1 / this.operand_2;
    }
    return true;
  }
}

enum BinOp {
  Add,
  Sub,
  Mul,
  Div
}