package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class SupplyStacks {

    public static void main(String[] args) {
        List<Stack<String>> stacks = read_stacks();
        List<Integer> instructions = read_instructions();
        System.out.println(solve_part_1(stacks, instructions));
        stacks = read_stacks(); // we need to read the stacks again, as it was modified
        System.out.println(solve_part_2(stacks, instructions));
    }

    /*
    --- Day 5: Supply Stacks ---
    The expedition can depart as soon as the final supplies have been unloaded 
    from the ships. Supplies are stored in stacks of marked crates, but 
    because the needed supplies are buried under many other crates, the crates 
    need to be rearranged.

    The ship has a giant cargo crane capable of moving crates between stacks. 
    To ensure none of the crates get crushed or fall over, the crane operator 
    will rearrange them in a series of carefully-planned steps. After the 
    crates are rearranged, the desired crates will be at the top of each stack.

    The Elves don't want to interrupt the crane operator during this delicate 
    procedure, but they forgot to ask her which crate will end up where, and 
    they want to be ready to unload them as soon as possible so they can embark.

    They do, however, have a drawing of the starting stacks of crates and the 
    rearrangement procedure (your puzzle input). For example:

        [D]    
    [N] [C]    
    [Z] [M] [P]
    1   2   3 

    move 1 from 2 to 1
    move 3 from 1 to 3
    move 2 from 2 to 1
    move 1 from 1 to 2
    
    In this example, there are three stacks of crates. Stack 1 contains two 
    crates: crate Z is on the bottom, and crate N is on top. Stack 2 contains 
    three crates; from bottom to top, they are crates M, C, and D. Finally, 
    stack 3 contains a single crate, P.

    Then, the rearrangement procedure is given. In each step of the procedure, 
    a quantity of crates is moved from one stack to a different stack. In the 
    first step of the above rearrangement procedure, one crate is moved from 
    stack 2 to stack 1, resulting in this configuration:

    [D]        
    [N] [C]    
    [Z] [M] [P]
    1   2   3 
    
    In the second step, three crates are moved from stack 1 to stack 3. Crates 
    are moved one at a time, so the first crate to be moved (D) ends up below 
    the second and third crates:

            [Z]
            [N]
        [C] [D]
        [M] [P]
    1   2   3
    
    Then, both crates are moved from stack 2 to stack 1. Again, because crates 
    are moved one at a time, crate C ends up below crate M:

            [Z]
            [N]
    [M]     [D]
    [C]     [P]
    1   2   3
    
    Finally, one crate is moved from stack 1 to stack 2:

            [Z]
            [N]
            [D]
    [C] [M] [P]
    1   2   3
    
    The Elves just need to know which crate will end up on top of each stack; 
    in this example, the top crates are C in stack 1, M in stack 2, and Z in 
    stack 3, so you should combine these together and give the Elves the 
    message CMZ.

    After the rearrangement procedure completes, what crate ends up on top of 
    each stack?
    */

    public static String solve_part_1(List<Stack<String>> stacks, List<Integer> instructions) {
        for(int i = 0; i < instructions.size()-2; i += 3) {
            int amount = instructions.get(i);
            int from = instructions.get(i+1);
            int to = instructions.get(i+2);
            for(int j = 0; j < amount; j++) {
                String to_move = stacks.get(from-1).pop();
                stacks.get(to-1).push(to_move);
            }
        }
        String out = "";
        for(Stack stack : stacks) {
            out += stack.peek();
        }
        return out;
    }

    /*
    --- Part Two ---
    As you watch the crane operator expertly rearrange the crates, you notice 
    the process isn't following your prediction.

    Some mud was covering the writing on the side of the crane, and you quickly 
    wipe it away. The crane isn't a CrateMover 9000 - it's a CrateMover 9001.

    The CrateMover 9001 is notable for many new and exciting features: air 
    conditioning, leather seats, an extra cup holder, and the ability to pick 
    up and move multiple crates at once.

    Again considering the example above, the crates begin in the same 
    configuration:

        [D]    
    [N] [C]    
    [Z] [M] [P]
    1   2   3 
    
    Moving a single crate from stack 2 to stack 1 behaves the same as before:

    [D]        
    [N] [C]    
    [Z] [M] [P]
    1   2   3 
    
    However, the action of moving three crates from stack 1 to stack 3 means 
    that those three moved crates stay in the same order, resulting in this new 
    configuration:

            [D]
            [N]
        [C] [Z]
        [M] [P]
    1   2   3
    
    Next, as both crates are moved from stack 2 to stack 1, they retain their 
    order as well:

            [D]
            [N]
    [C]     [Z]
    [M]     [P]
    1   2   3

    Finally, a single crate is still moved from stack 1 to stack 2, but now 
    it's crate C that gets moved:

            [D]
            [N]
            [Z]
    [M] [C] [P]
    1   2   3

    In this example, the CrateMover 9001 has put the crates in a totally 
    different order: MCD.

    Before the rearrangement process finishes, update your simulation so that 
    the Elves know where they should stand to be ready to unload the final 
    supplies. After the rearrangement procedure completes, what crate ends up 
    on top of each stack?
    */

    public static String solve_part_2(List<Stack<String>> stacks, List<Integer> instructions) {
        for(int i = 0; i < instructions.size()-2; i += 3) {
            int amount = instructions.get(i);
            int from = instructions.get(i+1);
            int to = instructions.get(i+2);
            List<String> block_to_move = new ArrayList<String>();
            for(int j = 0; j < amount; j++) {
                block_to_move.add(stacks.get(from-1).pop());
            }
            for(int j = amount-1; j >= 0; j--) {
                stacks.get(to-1).push(block_to_move.get(j));
            }
        }
        String out = "";
        for(Stack stack : stacks) {
            out += stack.peek();
        }
        return out;
    }

    // Reads the stack input
    // Lines may have the form "[B] [M] [V] [N]     [F] [D] [N]    "
    public static List<Stack<String>> read_stacks() {
        List<Stack<String>> reversed_input_stacks = new ArrayList<Stack<String>>();
        try {
            File input = new File("input.txt");
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Populate stacks:
                if(!line.startsWith("move")) { // this is a line showing part of the stack
                    for(int i = 0; i < line.length()-1; i += 4) {
                        String next_char = line.substring(i+1, i+2); // use that the input is nicely aligned
                        if(isNumeric(next_char)) { // skip the line corresponding to the stack numbers; we implicitly know them
                            continue;
                        }
                        if(reversed_input_stacks.size() <= i/4) { // this is the very first line
                            reversed_input_stacks.add(new Stack<String>());
                        }
                        if(!next_char.equals(" ")) { // skip whitespaces
                            reversed_input_stacks.get(i/4).push(next_char);
                        }
                    }
                } else {
                    break; // we read all the stack info
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Actually, we need to reverse the stacks, as we read them from top down:
        List<Stack<String>> input_stacks = new ArrayList<Stack<String>>();
        for(Stack<String> reversed_stack : reversed_input_stacks) {
            Stack<String> stack = new Stack<String>();
            while(!reversed_stack.empty()) {
                stack.push(reversed_stack.pop());
            }
            input_stacks.add(stack);
        }
        return input_stacks;
    }

    public static boolean isNumeric(String s) { 
        try {  
            Double.parseDouble(s);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }

    // Reads instructions of the form "move 3 from 2 to 5"
    public static List<Integer> read_instructions() {
        List<Integer> input_list = new ArrayList<Integer>();
        Pattern pattern = Pattern.compile("\\d+(\\s|$)");
        try {
            File input = new File("input.txt");
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.startsWith("move")) { // this is a line showing an instruction
                    Matcher matcher = pattern.matcher(line);
                    while(matcher.find()) {
                        String next = matcher.group();
                        if(next.contains(" ")) {
                            next = next.substring(0, next.length()-1);
                        }
                        input_list.add(Integer.parseInt(next));
                    }
                } else {
                    continue; // these input lines are already handled by read_stacks
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return input_list;
    }
}