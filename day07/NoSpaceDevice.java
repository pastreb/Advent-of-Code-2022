package adventofcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class NoSpaceDevice {

    public static void main(String[] args) {
        List<String> input = read_input();
        Directory root = populate_directories(input);
        System.out.println(solve_part_1(root, 100000));
        System.out.println(solve_part_2(root, 70000000, 30000000));
    }

    /*
    --- Day 7: No Space Left On Device ---
    You can hear birds chirping and raindrops hitting leaves as the expedition 
    proceeds. Occasionally, you can even hear much louder sounds in the 
    distance; how big do the animals get out here, anyway?

    The device the Elves gave you has problems with more than just its 
    communication system. You try to run a system update:

    $ system-update --please --pretty-please-with-sugar-on-top
    Error: No space left on device
    
    Perhaps you can delete some files to make space for the update?

    You browse around the filesystem to assess the situation and save the 
    resulting terminal output (your puzzle input). For example:

        $ cd /
        $ ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        $ cd a
        $ ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        $ cd e
        $ ls
        584 i
        $ cd ..
        $ cd ..
        $ cd d
        $ ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    
    The filesystem consists of a tree of files (plain data) and directories 
    (which can contain other directories or files). The outermost directory is 
    called /. You can navigate around the filesystem, moving into or out of 
    directories and listing the contents of the directory you're currently in.

    Within the terminal output, lines that begin with $ are commands you 
    executed, very much like some modern computers:

    - cd means change directory. This changes which directory is the current 
      directory, but the specific result depends on the argument:
        - cd x moves in one level: it looks in the current directory for the 
          directory named x and makes it the current directory.
        - cd .. moves out one level: it finds the directory that contains 
          the current directory, then makes that directory the current 
          directory.
        - cd / switches the current directory to the outermost directory, /.
    
    - ls means list. It prints out all of the files and directories 
      immediately contained by the current directory:
        - 123 abc means that the current directory contains a file named abc
          with size 123.
        - dir xyz means that the current directory contains a directory 
          named xyz.
    
    Given the commands and output in the example above, you can determine that 
    the filesystem looks visually like this:

    - / (dir)
    - a (dir)
        - e (dir)
        - i (file, size=584)
        - f (file, size=29116)
        - g (file, size=2557)
        - h.lst (file, size=62596)
    - b.txt (file, size=14848514)
    - c.dat (file, size=8504156)
    - d (dir)
        - j (file, size=4060174)
        - d.log (file, size=8033020)
        - d.ext (file, size=5626152)
        - k (file, size=7214296)
    
    Here, there are four directories: / (the outermost directory), a and d 
    (which are in /), and e (which is in a). These directories also contain
    files of various sizes.

    Since the disk is full, your first step should probably be to find 
    directories that are good candidates for deletion. To do this, you need to 
    determine the total size of each directory. The total size of a directory 
    is the sum of the sizes of the files it contains, directly or indirectly. 
    (Directories themselves do not count as having any intrinsic size.)

    The total sizes of the directories above can be found as follows:

    - The total size of directory e is 584 because it contains a single file 
      i of size 584 and no other directories.
    - The directory a has total size 94853 because it contains files f (size 
      29116), g (size 2557), and h.lst (size 62596), plus file i indirectly 
      (a contains e which contains i).
    - Directory d has total size 24933642.
    - As the outermost directory, / contains every file. Its total size is 
      48381165, the sum of the size of every file.
    
    To begin, find all of the directories with a total size of at most 100000, 
    then calculate the sum of their total sizes. In the example above, these 
    directories are a and e; the sum of their total sizes is 95437 (94853 + 584). 
    (As in this example, this process can count files more than once!)

    Find all of the directories with a total size of at most 100000. What is 
    the sum of the total sizes of those directories?
    */

    public static int solve_part_1(Directory root, int size_upper_bound) {
        root.compute_size(); // iteratively computes the sizes of all directories;
        int sum = 0;
        for(int directory_size : Directory.all_sizes) {
            if(directory_size < size_upper_bound) {
                sum += directory_size;
            }
        }
        return sum;
    }

    /*
    --- Part Two ---
    Now, you're ready to choose a directory to delete.

    The total disk space available to the filesystem is 70000000. To run the 
    update, you need unused space of at least 30000000. You need to find a 
    directory you can delete that will free up enough space to run the update.

    In the example above, the total size of the outermost directory (and thus 
    the total amount of used space) is 48381165; this means that the size of 
    the unused space must currently be 21618835, which isn't quite the 
    30000000 required by the update. Therefore, the update still requires a 
    directory with total size of at least 8381165 to be deleted before it can run.

    To achieve this, you have the following options:

    - Delete directory e, which would increase unused space by 584.
    - Delete directory a, which would increase unused space by 94853.
    - Delete directory d, which would increase unused space by 24933642.
    - Delete directory /, which would increase unused space by 48381165.
    - Directories e and a are both too small; deleting them would not free up 
      enough space. However, directories d and / are both big enough! Between 
      these, choose the smallest: d, increasing unused space by 24933642.

    Find the smallest directory that, if deleted, would free up enough space on 
    the filesystem to run the update. What is the total size of that directory?
    */

    public static int solve_part_2(Directory root, int total_size, int needed_size) {
        int current_size = root.compute_size(); // iteratively computes the sizes of all directories;
        int free_space = total_size - current_size;
        int delete_size = needed_size - free_space;
        int current_min = current_size; // always possible to delete root
        for(int directory_size : Directory.all_sizes) {
            if(directory_size >= delete_size) {
                current_min = Math.min(current_min, directory_size);
            }
        }
        return current_min;
    }

    // Parse to input to re-create the file system
    public static Directory populate_directories(List<String> input) {
        Directory root = new Directory("/", null);
        Directory current = root;
        Pattern file_name_pattern = Pattern.compile("\\s.*$");
        Pattern file_size_pattern = Pattern.compile("^\\d+\\s");
        for(int i = 0; i < input.size(); i++) {
            if(input.get(i).equals("$ cd /")) { // change directory to root
                current = root;
            } else if(input.get(i).equals("$ ls")) { // list contents
                i++;
                while(i < input.size() && !input.get(i).startsWith("$")) { // read the list of directory contents
                    if(input.get(i).startsWith("dir")) { // new directory
                        String name = input.get(i).substring(4);
                        current.add_sub_directory(new Directory(name, current));
                        // System.out.println("Found directory " + name);
                    } else { // new file
                        Matcher file_name_matcher = file_name_pattern.matcher(input.get(i));
                        Matcher file_size_matcher = file_size_pattern.matcher(input.get(i));
                        if(file_name_matcher.find() && file_size_matcher.find()) {
                            String file_name = file_name_matcher.group();
                            file_name = file_name.substring(1);
                            String file_size = file_size_matcher.group();
                            file_size = file_size.substring(0, file_size.length()-1);
                            current.add_file(file_name, Integer.parseInt(file_size));
                        } else {
                            System.out.println("Failed to parse file information: " + input.get(i));
                        }
                    }
                    i++;
                }
                i--; // go back because what we just read is no more input and will be treated 
                     // in the next iteration of the for-loop
            } else if(input.get(i).equals("$ cd ..")) { // change directory to parent directory
                current = current.get_parent_directory();
            } else if(input.get(i).startsWith("$ cd")) { // change directory to some sub-directory
                current = current.get_sub_directory(input.get(i).substring(5));
                if(current == null) {
                    System.out.println("Failed to find subdirectory: " + input.get(i));
                }
            } else {
                System.out.println("Untreated input line: " + input.get(i));
            }
        }
        return root;
    }

    public static List<String> read_input() {
        List<String> input_list = new ArrayList<String>();
        try {
            File input = new File("input.txt");
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()) {
                input_list.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return input_list;
    }
}

class Directory {

    public static List<Integer> all_sizes = new ArrayList<Integer>(); // is modified by all object instances

    private String name;
    private List<String> files;
    private List<Integer> file_sizes;
    private List<Directory> sub_directories;
    private Directory parent_directory;
    private int size = -1;

    public Directory(String name, Directory parent_directory) {
        this.name = name;
        this.parent_directory = parent_directory;
        this.files = new ArrayList<String>();
        this.file_sizes = new ArrayList<Integer>();
        this.sub_directories = new ArrayList<Directory>();
    }

    public String get_name() {
        return this.name;
    }

    public void add_file(String name, int size) {
        this.files.add(name);
        this.file_sizes.add(size);
    }

    public void add_sub_directory(Directory sub_directory) {
        this.sub_directories.add(sub_directory);
    }

    public Directory get_sub_directory(String name) {
        for(Directory sub_directory : this.sub_directories) {
            if(sub_directory.get_name().equals(name)) {
                return sub_directory;
            }
        }
        return null; // not found
    }

    public Directory get_parent_directory() {
        return this.parent_directory;
    }

    public int compute_size() {
        if(size == -1) {
            this.size = 0;
            for(int file_size : file_sizes) {
                this.size += file_size;
            }
            for(Directory sub_directory : sub_directories) {
                this.size += sub_directory.compute_size();
            }
            all_sizes.add(this.size);
        }
        return this.size;
    }
}