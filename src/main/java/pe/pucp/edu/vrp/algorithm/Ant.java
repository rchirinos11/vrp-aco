package pe.pucp.edu.vrp.algorithm;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pe.pucp.edu.vrp.Constant;

/**
 * <b>Class</b>: Ant <br/>
 * Ants can carry 50 times their own body weight
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ant implements Comparable<Ant> {
    private List<Node> visitedNodes;
    private int totalCost;

    public void work(Matrix[][] mapGraph, List<Node> nodes) {
        int xIndex = 0, yIndex = 0;
        Node nextNode = nodes.get(xIndex);

        for (int i = 0; i <= nodes.size(); i++) {
            visitedNodes.add(nextNode);
            totalCost += mapGraph[xIndex][yIndex].getHeuristicValue();
            localUpdate(mapGraph[xIndex][yIndex]);
            xIndex = yIndex;
            nextNode = chooseNext(nodes, mapGraph[xIndex]);
            yIndex = nextNode.getMatrixIndex();
        }
    }

    private Node chooseNext(List<Node> nodes, Matrix[] xRow) {
        double randVal, p;
        int i = 1;
        randVal = Math.random();
        p = calcProbability(xRow, nodes, i);
        for (i = 1; i < nodes.size() - 1 && p > randVal; i++) {
            randVal -= p;
            p = calcProbability(xRow, nodes, nodes.get(i).getMatrixIndex());
        }
        return nodes.get(i);
    }

    private double calcProbability(Matrix[] values, List<Node> nodes, int i) {
        double numerator, denominator = 0;
        numerator = multiply(values[i].getPheromoneConc(), values[i].getHeuristicValue());
        for (int x = 0; x < values.length; x++) {
            Node foundOrder = findNode(nodes, x);
            Node foundVisited = findNode(nodes, x);
            if (Objects.isNull(foundVisited) && Objects.nonNull(foundOrder))
                denominator += multiply(values[x].getPheromoneConc(), values[x].getHeuristicValue());
        }
        return numerator / (denominator == 0 ? 1 : denominator);
    }

    private double multiply(double pheromone, double heuristic) {
        return Math.pow(pheromone, Constant.alpha) * Math.pow(heuristic, Constant.beta);
    }

    private Node findNode(List<Node> nodes, int x) {
        return nodes.stream()
                .filter(node -> x == node.getMatrixIndex())
                .findFirst()
                .orElse(null);
    }

    private void localUpdate(Matrix value) {
        double pheromoneConc;
        pheromoneConc = (1 - Constant.rho) * value.getPheromoneConc() + 1 / value.getHeuristicValue();
        value.setPheromoneConc(pheromoneConc);
    }


    @Override
    public int compareTo(Ant ant) {
        return ant.getTotalCost() - getTotalCost();
    }
}
