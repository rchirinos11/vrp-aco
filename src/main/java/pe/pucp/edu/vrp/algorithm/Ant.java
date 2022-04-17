package pe.pucp.edu.vrp.algorithm;

import java.util.ArrayList;
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
@AllArgsConstructor
@ToString
public class Ant implements Comparable<Ant> {
    private List<Node> visitedNodes;
    private int totalCost;

    public Ant() {
        visitedNodes = new ArrayList<>();
    }
    public void work(Matrix[][] mapGraph, List<Node> orders) {
        int xIndex = 0, yIndex = 0;
        Node nextNode = orders.get(xIndex);

        for (int i = 0; i <= orders.size(); i++) {
            visitedNodes.add(nextNode);
            totalCost += mapGraph[xIndex][yIndex].getHeuristicValue();
            localUpdate(mapGraph[xIndex][yIndex]);
            xIndex = yIndex;
            nextNode = chooseNext(orders, mapGraph[xIndex]);
            yIndex = nextNode.getMatrixIndex();
        }
    }

    private Node chooseNext(List<Node> orders, Matrix[] xRow) {
        double p = 1, randVal = Math.random();
        int i;
        for (i = 1; i < orders.size() - 1 && p > randVal; i++) {
            p = calcProbability(xRow, orders, orders.get(i).getMatrixIndex());
            randVal -= p;
        }
        return orders.get(i);
    }

    private double calcProbability(Matrix[] values, List<Node> nodes, int i) {
        double numerator, denominator = 0;
        if (values[i].getHeuristicValue() == 0)
            return 0;
        numerator = multiply(values[i].getPheromoneConc(), values[i].getHeuristicValue());
        for (int x = 0; x < values.length; x++) {
            Node foundOrder = findNode(nodes, x);
            Node foundVisited = findNode(visitedNodes, x);
            if (Objects.isNull(foundVisited) && Objects.nonNull(foundOrder))
                denominator += multiply(values[x].getPheromoneConc(), values[x].getHeuristicValue());
        }
        if (numerator == 0 || denominator == 0)
            return 0;

        return numerator / denominator;
    }

    private double multiply(double pheromone, double heuristic) {
        double value = Math.pow(pheromone, Constant.alpha) * Math.pow(heuristic, Constant.beta);
        return value;
    }

    private Node findNode(List<Node> nodes, int x) {
        return nodes.stream()
                .filter(node -> x == node.getMatrixIndex())
                .findFirst()
                .orElse(null);
    }

    private void localUpdate(Matrix value) {
        double pheromoneConc;
        if (value.getHeuristicValue() > 0) {
            pheromoneConc = (1 - Constant.rho) * value.getPheromoneConc() + (1 / value.getHeuristicValue());
            value.setPheromoneConc(pheromoneConc);
        }
    }


    @Override
    public int compareTo(Ant ant) {
        return getTotalCost() - ant.getTotalCost();
    }
}
