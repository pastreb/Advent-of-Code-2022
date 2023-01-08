package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class CampCleanup {

    public static void main(String[] args) {
        List<List<Integer>> input = read_input();
        System.out.println(solve_part_1(input));
        System.out.println(solve_part_2(input));
    }

    /*
    --- Day 4: Camp Cleanup ---
    Space needs to be cleared before the last supplies can be unloaded from the 
    ships, and so several Elves have been assigned the job of cleaning up 
    sections of the camp. Every section has a unique ID number, and each Elf is 
    assigned a range of section IDs.

    However, as some of the Elves compare their section assignments with each 
    other, they've noticed that many of the assignments overlap. To try to 
    quickly find overlaps and reduce duplicated effort, the Elves pair up and 
    make a big list of the section assignments for each pair (your puzzle input).

    For example, consider the following list of section assignment pairs:

    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8

    For the first few pairs, this list means:
    - Within the first pair of Elves, the first Elf was assigned sections 
      2-4 (sections 2, 3, and 4), while the second Elf was assigned sections 
      6-8 (sections 6, 7, 8).
    - The Elves in the second pair were each assigned two sections.
    - The Elves in the third pair were each assigned three sections: one got 
      sections 5, 6, and 7, while the other also got 7, plus 8 and 9.
    
    This example list uses single-digit section IDs to make it easier to draw; 
    your actual list might contain larger numbers. Visually, these pairs of 
    section assignments look like this:

    .234.....  2-4
    .....678.  6-8

    .23......  2-3
    ...45....  4-5

    ....567..  5-7
    ......789  7-9

    .2345678.  2-8
    ..34567..  3-7

    .....6...  6-6
    ...456...  4-6

    .23456...  2-6
    ...45678.  4-8
    
    Some of the pairs have noticed that one of their assignments fully contains 
    the other. For example, 2-8 fully contains 3-7, and 6-6 is fully contained 
    by 4-6. In pairs where one assignment fully contains the other, one Elf in 
    the pair would be exclusively cleaning sections their partner will already 
    be cleaning, so these seem like the most in need of reconsideration. In 
    this example, there are 2 such pairs.

    In how many assignment pairs does one range fully contain the other?
    */

    public static int solve_part_1(List<List<Integer>> input) {
        int x = 0;
        for(List<Integer> pair : input) {
            int a_low = pair.get(0);
            int a_high = pair.get(1);
            int b_low = pair.get(2);
            int b_high = pair.get(3);
            if(a_low <= b_low && b_high <= a_high) { // b contained in a
                x++;
            } else if(b_low <= a_low && a_high <= b_high) { // a contained in b
                x++;
            }
        }
        return x;
    }

    /*
    --- Part Two ---
    It seems like there is still quite a bit of duplicate work planned. 
    Instead, the Elves would like to know the number of pairs that overlap 
    at all.

    In the above example, the first two pairs (2-4,6-8 and 2-3,4-5) don't 
    overlap, while the remaining four pairs (5-7,7-9, 2-8,3-7, 6-6,4-6, and 
    2-6,4-8) do overlap:

        - 5-7,7-9 overlaps in a single section, 7.
        - 2-8,3-7 overlaps all of the sections 3 through 7.
        - 6-6,4-6 overlaps in a single section, 6.
        - 2-6,4-8 overlaps in sections 4, 5, and 6.
    
    So, in this example, the number of overlapping assignment pairs is 4.

    In how many assignment pairs do the ranges overlap?
    */

    public static int solve_part_2(List<List<Integer>> input) {
        int x = 0;
        for(List<Integer> pair : input) {
            int a_low = pair.get(0);
            int a_high = pair.get(1);
            int b_low = pair.get(2);
            int b_high = pair.get(3);
            if(a_low <= b_low && b_low <= a_high) { // part of a contained in b
                x++;
            } else if(b_low <= a_low && a_low <= b_high) { // part of b contained in a
                x++;
            }
        }
        return x;
    }

    public static List<List<Integer>> read_input() {
        List<List<Integer>> input_list = new ArrayList<List<Integer>>();
        Pattern pattern = Pattern.compile("(\\-|\\,|^)\\d+");
        try {
            File input = new File("input.txt");
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()) {
                List<Integer> next_pair = new ArrayList<Integer>();
                Matcher matcher = pattern.matcher(scanner.nextLine());
                while(matcher.find()) {
                    String next = matcher.group();
                    if(next.charAt(0) == '-' || next.charAt(0) == ',') {
                        next = next.substring(1);
                    }
                    if(next.charAt(next.length()-1) == '-' || next.charAt(next.length()-1) == ',') {
                        next = next.substring(0,next.length()-1);
                    }
                    next_pair.add(Integer.parseInt(next));
                }
                input_list.add(next_pair);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return input_list;
    }

}