package wwu.compiler.cfg;

import java.util.*;

public class GraphOps {
    /**
     * Graph coloring for register allocation
     * @param adjList adjacency list of vertex -> neighbors
     * @param k number of allowed colors
     * @return vertex -> assignment if one exists, otherwise null
     */
    public static Map<String, Integer> colorGraph(
            Map<String, Set<String>> adjList, int k) {
        Set<String> available = new HashSet<>(adjList.keySet());
        // Get neighbor count of every vertex
        Map<String, Integer> nbrCnts = new HashMap<>();
        for (String v : adjList.keySet()) {
            nbrCnts.put(v, adjList.get(v).size());
        }
        List<String> stack = new ArrayList<>();

        while (!available.isEmpty()) {
            String v = null;
            for (String x : available) {
                if (nbrCnts.get(x) < k) {
                    v = x;
                    break;
                }
            }
            if (v == null) {
                return null;
            }
            available.remove(v);
            stack.add(v);
            for (String nbr : adjList.get(v)) {
                if (available.contains(nbr)) {
                    nbrCnts.put(nbr, nbrCnts.get(nbr) - 1);
                }
            }
            nbrCnts.put(v, 0);
        }

        Map<String, Integer> assignment = new HashMap<>();
        Set<Integer> usedColors = new HashSet<>();

        while (!stack.isEmpty()) {
            usedColors.clear();
            String v = stack.remove(stack.size() - 1);
            for (String nbr : adjList.get(v)) {
                if (assignment.containsKey(nbr)) {
                    usedColors.add(assignment.get(nbr));
                }
            }
            if (usedColors.size() == k) {
                return null;
            }
            for (int i = 0; i < k; i++) {
                if (!usedColors.contains(i)) {
                    assignment.put(v, i);
                    break;
                }
            }
        }

        return assignment;
    }
}