package pe.pucp.edu.vrp.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
    private double currentLoad;

    public Ant() {
        visitedNodes = new ArrayList<>();
        totalCost = 0;
        currentLoad = 0;
    }

    public void work(Matrix[][] mapGraph, List<Node> orders, double maxLoad) {
        int xIndex = 0, yIndex = 0;
        Node nextNode = new Node(0, 0, 0);

        for (int i = 0; i <= orders.size(); i++) {
            visitedNodes.add(nextNode);
            totalCost += mapGraph[xIndex][yIndex].getHeuristicValue();
            currentLoad += nextNode.getTotalWeight();
            localUpdate(mapGraph[xIndex][yIndex]);
            xIndex = yIndex;
            nextNode = chooseNext(orders, mapGraph, xIndex);
            if (currentLoad > maxLoad) {
                visitedNodes.remove(visitedNodes.size() - 1);
                break;
            }
            if (Objects.isNull(nextNode))
                break;
            yIndex = nextNode.getMatrixIndex();
        }
    }

    private Node chooseNext(List<Node> orders, Matrix[][] mapGraph, int x) {
        double randVal = Math.random();
        int i, yVal = -1;
        List<Double> probList = calcProbability(orders, mapGraph, x);
        for (i = 0; i < mapGraph.length; i++) {
            if (probList.get(i) > randVal) {
                yVal = i;
                break;
            } else
                randVal -= probList.get(i);
        }
        if (yVal == -1)
            return null;
        return findNode(orders, yVal);
    }

    private List<Double> calcProbability(List<Node> orders, Matrix[][] mapGraph, int x) {
        List<Double> probList = new ArrayList<>();
        for (int i = 0; i < mapGraph.length; i++) {
            probList.add(0.0);
        }
        double denominator = getDenominator(orders, probList, mapGraph, x);
        for (int i = 0; i < mapGraph.length; i++) {
            probList.set(i, probList.get(i) / denominator);
        }
        return probList;
    }

    private double getDenominator(List<Node> orders, List<Double> probList, Matrix[][] mapGraph, int x) {
        double denominator = 0.0;
        for (int y = 0; y < mapGraph.length; y++) {
            if (Objects.isNull(findNode(visitedNodes, y)) && Objects.nonNull(findNode(orders, y))) {
                if (x == y) probList.set(y, 0.0);
                else probList.set(y, getNumerator(mapGraph[x][y]));
                denominator += probList.get(y);
            }
        }
        return denominator;
    }

    private Double getNumerator(Matrix values) {
        double numerator = 0.0;
        double pheromoneConc = values.getPheromoneConc();
        if (pheromoneConc > 0.0)
            numerator = Math.pow(pheromoneConc, Constant.ALPHA) * Math.pow(1 / values.getHeuristicValue(), Constant.BETA);
        return numerator;
    }

    private Node findNode(List<Node> nodes, int i) {
        return nodes.stream()
                .filter(node -> i == node.getMatrixIndex())
                .findFirst()
                .orElse(null);
    }

    private void localUpdate(Matrix value) {
        double pheromoneConc;
        if (value.getHeuristicValue() > 0) {
            pheromoneConc = (1 - Constant.RHO) * value.getPheromoneConc() + (1 / totalCost);
            value.setPheromoneConc(pheromoneConc);
        }
    }


    @Override
    public int compareTo(Ant ant) {
        return (int) ((totalCost - ant.getTotalCost()) / (currentLoad - ant.getCurrentLoad()));
    }
}
