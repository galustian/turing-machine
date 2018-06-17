package turingmachine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TuringMachine {
    private Stack<Character> left = new Stack<>();
    private Stack<Character> right = new Stack<>();

    private int state;

    private char[] action;
    private List<HashMap<Character, Integer>> next = new ArrayList<>();
    private List<HashMap<Character, Character>> out = new ArrayList<>();


    public TuringMachine(String filepath) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filepath));

            action = new char[allLines.size()];

            int i = 0;
            for (var line: allLines) {
                if (i == 0) {
                    // init start state
                    var lineSplit = line.split(" ");
                    state = Integer.parseInt(lineSplit[lineSplit.length-1]);
                    i++;
                    continue;
                }

                var splitLine = line.split(" , ");

                // fill 'action' data structure
                // i-1 => first line does not count to action
                action[i-1] = splitLine[0].toCharArray()[0];

                // fill 'next' and 'out' data structures
                var nextStrArray = splitLine[1].split(" ");
                var outStrArray = splitLine[2].split(" ");

                var map1 = new HashMap<Character, Integer>();
                var map2 = new HashMap<Character, Character>();
                for (int j = 0; j <= 2; j++) {
                    // fill 'next' data structure
                    if (j == 2) {
                        map1.put('#', Character.getNumericValue(nextStrArray[j].charAt(0)));
                    } else {
                        map1.put(Integer.toString(j).charAt(0), Character.getNumericValue(nextStrArray[j].charAt(0)));
                    }

                    // fill 'out' data structure
                    if (j == 2) {
                        map2.put('#', outStrArray[j].charAt(0));
                    } else {
                        map2.put(Integer.toString(j).charAt(0), outStrArray[j].charAt(0));
                    }
                }
                next.add(map1);
                out.add(map2);

                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Character read() {
        if (right.isEmpty()) return '#';
        return right.pop();
    }
    private void write(Character c) {
        right.push(c);
    }
    private void moveLeft() {
        if (left.isEmpty()) {
            right.push('#');
        } else {
            right.push(left.pop());
        }
    }
    private void moveRight() {
        if (right.isEmpty()) {
            left.push('#');
        } else {
            left.push(right.pop());
        }
    }

    public String simulate(String input) {
        for (int i = input.length()-1; i >= 0; i--) {
            right.push(input.charAt(i));
        }

        while (action[state] != 'H') {
            char c = read();

            write(out.get(state).get(c));
            state = next.get(state).get(c);

            if (action[state] == 'L') moveLeft();
            if (action[state] == 'R') moveRight();
        }

        return constructOutput();
    }

    private String constructOutput() {
        var reverseLeftStack = new Stack<Character>();
        while (!left.isEmpty()) {
            reverseLeftStack.push(left.pop());
        }

        String leftString = "";
        String rightString = "";

        while (!reverseLeftStack.isEmpty()) {
            leftString += reverseLeftStack.pop();
        }

        while(!right.isEmpty()) {
            rightString += right.pop();
        }

        return leftString + rightString;
    }
}
