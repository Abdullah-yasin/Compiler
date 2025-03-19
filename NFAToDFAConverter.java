import java.util.*;

class NFA {
    private final Map<Integer, Map<Character, List<Integer>>> transitions;
    private final int startState;
    private final Set<Integer> acceptingStates;

    public NFA(int startState, Set<Integer> acceptingStates) {
        this.transitions = new HashMap<>();
        this.startState = startState;
        this.acceptingStates = new HashSet<>(acceptingStates);
    }

    public void addTransition(int fromState, char input, int toState) {
        transitions.putIfAbsent(fromState, new HashMap<>());
        transitions.get(fromState).putIfAbsent(input, new ArrayList<>());
        transitions.get(fromState).get(input).add(toState);
    }

    public void printNFA() {
        System.out.println("NFA Transitions:");
        for (var entry : transitions.entrySet()) {
            int state = entry.getKey();
            for (var trans : entry.getValue().entrySet()) {
                char symbol = trans.getKey();
                for (int nextState : trans.getValue()) {
                    System.out.println(state + " -- " + symbol + " --> " + nextState);
                }
            }
        }
        System.out.println("Accepting States: " + acceptingStates);
    }

    public DFA convertToDFA() {
        Map<Set<Integer>, Integer> stateMapping = new HashMap<>();
        List<Set<Integer>> dfaStates = new ArrayList<>();
        Queue<Set<Integer>> queue = new LinkedList<>();

        Set<Integer> startSet = epsilonClosure(Set.of(startState));
        queue.add(startSet);
        dfaStates.add(startSet);
        stateMapping.put(startSet, 0);
        int stateCounter = 0;

        Map<Integer, Map<Character, Integer>> dfaTransitions = new HashMap<>();
        Set<Integer> dfaAcceptingStates = new HashSet<>();

        while (!queue.isEmpty()) {
            Set<Integer> currentSet = queue.poll();
            int currentDFAState = stateMapping.get(currentSet);
            dfaTransitions.put(currentDFAState, new HashMap<>());

            for (char symbol : getAlphabet()) {
                Set<Integer> nextSet = new HashSet<>();
                for (int state : currentSet) {
                    if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                        nextSet.addAll(transitions.get(state).get(symbol));
                    }
                }
                nextSet = epsilonClosure(nextSet);
                if (!nextSet.isEmpty()) {
                    if (!stateMapping.containsKey(nextSet)) {
                        stateCounter++;
                        stateMapping.put(nextSet, stateCounter);
                        dfaStates.add(nextSet);
                        queue.add(nextSet);
                    }
                    dfaTransitions.get(currentDFAState).put(symbol, stateMapping.get(nextSet));
                }
            }
        }
        
        for (Set<Integer> stateSet : dfaStates) {
            for (int state : stateSet) {
                if (acceptingStates.contains(state)) {
                    dfaAcceptingStates.add(stateMapping.get(stateSet));
                    break;
                }
            }
        }

        return new DFA(0, dfaAcceptingStates, dfaTransitions);
    }

    private Set<Integer> epsilonClosure(Set<Integer> states) {
        return new HashSet<>(states);
    }

    private Set<Character> getAlphabet() {
        Set<Character> alphabet = new HashSet<>();
        for (var stateTransitions : transitions.values()) {
            alphabet.addAll(stateTransitions.keySet());
        }
        return alphabet;
    }
}

class DFA {
    private final int startState;
    private final Set<Integer> acceptingStates;
    private final Map<Integer, Map<Character, Integer>> transitions;

    public DFA(int startState, Set<Integer> acceptingStates, Map<Integer, Map<Character, Integer>> transitions) {
        this.startState = startState;
        this.acceptingStates = new HashSet<>(acceptingStates);
        this.transitions = transitions;
    }

    public boolean accepts(String input) {
        int currentState = startState;
        for (char symbol : input.toCharArray()) {
            if (!transitions.containsKey(currentState) || !transitions.get(currentState).containsKey(symbol)) {
                return false;
            }
            currentState = transitions.get(currentState).get(symbol);
        }
        return acceptingStates.contains(currentState);
    }
}

public class NFAToDFAConverter {
    public static void main(String[] args) {
        NFA identifierNFA = buildIdentifierNFA();
        identifierNFA.printNFA();
        DFA identifierDFA = identifierNFA.convertToDFA();

        String[] testInputs = {"abc", "x1", "123", "3.14", "var", "+", ";", "true", "2h7h"};
        for (String input : testInputs) {
            System.out.println("Input: " + input);
            if (identifierDFA.accepts(input)) System.out.println("  → Identifier (DFA)");
        }
    }

    private static NFA buildIdentifierNFA() {
        NFA nfa = new NFA(0, Set.of(1));
        for (char c = 'a'; c <= 'z'; c++) nfa.addTransition(0, c, 1);
        for (char c = 'a'; c <= 'z'; c++) nfa.addTransition(1, c, 1);
        for (char c = '0'; c <= '9'; c++) nfa.addTransition(1, c, 1);
        return nfa;
    }
}
