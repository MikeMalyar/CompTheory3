import java.io.*;
import java.util.*;

public class Lab3 {

    private static final Map<AbstractMap.SimpleEntry<Integer, Character>, Integer> map = new HashMap<>();
    private static List<State> states;
    private static List<Character> symbols;

    private static String res;

    public static void main(String[] args) {
        prepareData();

        List<String> text = new LinkedList<>();

        try {
            File file = new File("input.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            while (line != null) {
                text.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("output.txt", false)) {
            for (String str : text) {
                int i = 0;
                State currentState = states.get(0);
                char sym;
                res = "";

                while (i <= str.length()) {
                    try {
                        sym = str.charAt(i);
                        if (!symbols.contains(sym)) {
                            sym = (char) Character.UNASSIGNED;
                        } else {
                            res += sym;
                        }
                    } catch (StringIndexOutOfBoundsException endOfLine) {
                        sym = (char) Character.UNASSIGNED;
                    }
                    ++i;

                    currentState = getNextState(currentState, sym, writer, false);
                }
            }

            writer.flush();
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }


    }

    private static State getNextState(State currentState, char sym, FileWriter writer, boolean recursed) {
        try {
            int stateName = currentState.getName();
            int nextStateName = map.entrySet().stream().filter(simpleEntryStateEntry ->
                    simpleEntryStateEntry.getKey().getKey().equals(stateName) &&
                            simpleEntryStateEntry.getKey().getValue().equals(sym)
            ).map(Map.Entry::getValue).findFirst().orElseThrow(() ->
                    new IllegalStateException(String.format("State %s or symbol %s not found", stateName, sym)));
            State nextState = states.stream().filter(state -> state.getName() == nextStateName)
                    .findFirst().orElseThrow(() ->
                            new IllegalStateException(String.format("State %s not found", nextStateName)));
            if (nextState.isFinal()) {
                writer.write(nextState.getOutput(res));
                res = "";
                currentState = states.get(0);
                if (!recursed) {
                    return getNextState(currentState, sym, writer, true);
                }
            } else {
                currentState = nextState;
            }
        } catch (IllegalStateException | IOException ex) {
            currentState = states.get(0);
        }
        return currentState;
    }

    /**
     *  Init configuration
     *  */
    private static void prepareData() {
        int[][] table = new int[][] {
                {-7, -7, -7, -7,  1, -7,  6, -7, -7, -7},
                {-1, -1,  2, -1,  5, -1,  5,  5,  5, -1},
                { 3, -7, -7, -7, -7, -7, -7, -7, -7, -7},
                {-7,  4, -7, -7, -7, -7, -7, -7, -7, -7},
                {-5, -5, -5, -5, -5, -5, -5, -5, -5, -5},
                {-2, -2, -2, -2,  5, -2,  5,  5,  5, -2},
                {-3, -3, -3, -3,  7, -3, -3, -3, -3, -3},
                {-4, -4, -4,  8, -4, -4, -4, -4, -4, -4},
                {-7, -7, -7, -7, -7,  9, -7, -7, -7, -7},
                {-7, -7, 10, -7, -7, -7, -7, -7, -7, -7},
                {-6, -6, -6, -6, -6, -6, -6, -6, -6, -6}
        };
        states = new ArrayList<>(Arrays.asList(
                new State().setName(0),
                new State().setName(1),
                new State().setName(2),
                new State().setName(3),
                new State().setName(4),
                new State().setName(5),
                new State().setName(6),
                new State().setName(7),
                new State().setName(8),
                new State().setName(9),
                new State().setName(10),
                new State().setName(-1).setFinal(true).setOutput("<identifier, %s>"),
                new State().setName(-2).setFinal(true).setOutput("<identifier, %s>"),
                new State().setName(-3).setFinal(true).setOutput("<identifier, %s>"),
                new State().setName(-4).setFinal(true).setOutput("<identifier, %s>"),
                new State().setName(-5).setFinal(true).setOutput("<keyword, read>"),
                new State().setName(-6).setFinal(true).setOutput("<keyword, write>"),
                new State().setName(-7).setFinal(true)
        ));
        symbols = new ArrayList<>(Arrays.asList(
                'a', 'd', 'e', 'i', 'r', 't', 'w', '0', '1'
        ));

        symbols.add((char) Character.UNASSIGNED);
        prepareMap(table, states, symbols);
    }

    private static void prepareMap(int[][] table, List<State> states, List<Character> symbols) {
        for (int i = 0, tableLength = table.length; i < tableLength; i++) {
            for (int j = 0, rowLength = table[i].length; j < rowLength; j++) {
                AbstractMap.SimpleEntry<Integer, Character> entry
                        = new AbstractMap.SimpleEntry<>(states.get(i).getName(), symbols.get(j));
                int finalI = i;
                int finalJ = j;
                map.put(entry, states.stream().filter(state -> state.getName()
                        == table[finalI][finalJ]).findFirst().orElseThrow(IllegalStateException::new).getName());
            }
        }
    }
}