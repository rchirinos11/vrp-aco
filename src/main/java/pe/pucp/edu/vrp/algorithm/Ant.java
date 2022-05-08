package pe.pucp.edu.vrp.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.pucp.edu.vrp.util.Constant;
import pe.pucp.edu.vrp.util.Speed;

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
    private List<Node> nodeList;
    private List<Connection> connectionList;
    private double totalCost;
    private double currentLoad;
    private int start;


    public Ant(int x, List<Node> nodeList, List<Connection> connectionList) {
        visitedNodes = new ArrayList<>();
        totalCost = 0;
        currentLoad = 0;
        start = x;
        this.nodeList = nodeList;
        this.connectionList = connectionList;
    }

    public void work(Matrix[][] mapGraph, List<Order> orderList, double maxLoad) {
        int xIndex = start, yIndex, count = 0;
        Node nextNode = nodeList.get(start);
        Order order;
        double speed;

        visitedNodes.add(nextNode);
        while(true) {
            yIndex = chooseNext(orderList, mapGraph, xIndex);
            if (yIndex == -1)
                break;
            nextNode = nodeList.get(yIndex);
            order = findOrder(orderList, nextNode);
            speed = Speed.valueOf(nextNode.getRegion().name() + nodeList.get(xIndex).getRegion().name()).getSpeed();
            if (nextNode.getRegion().getMaxHours() > totalCost + mapGraph[xIndex][yIndex].getHeuristicValue() / speed) {
                visitedNodes.add(nextNode);
                totalCost += (mapGraph[xIndex][yIndex].getHeuristicValue() / speed);
                if (xIndex != yIndex) totalCost += 1;
                localUpdate(mapGraph[xIndex][yIndex]);
                xIndex = yIndex;
                if (Objects.nonNull(order) && currentLoad + order.getPackageAmount() <= maxLoad) {
                    currentLoad += order.getPackageAmount();
                } else if (Objects.nonNull(order)) {
                    visitedNodes.remove(nextNode);
                    break;
                } else {
                    count++;
                }
            } else {
                break;
            }
        }
    }

    private int chooseNext(List<Order> orderList, Matrix[][] mapGraph, int x) {
        double randVal = Math.random();
        int i, yVal = -1;
        List<Double> probList = calcProbability(orderList, mapGraph, x);
        for (i = 0; i < mapGraph.length; i++) {
            if (probList.get(i) > randVal) {
                yVal = i;
                break;
            } else
                randVal -= probList.get(i);
        }
        if (yVal == -1) {
            //chooseRandom();
        }
        return yVal;
    }

    private List<Double> calcProbability(List<Order> orderList, Matrix[][] mapGraph, int x) {
        List<Double> probList = new ArrayList<>();
        for (int i = 0; i < mapGraph.length; i++) {
            probList.add(0.0);
        }
        double denominator = getDenominator(orderList, probList, mapGraph, x);
        for (int i = 0; i < mapGraph.length; i++) {
            probList.set(i, probList.get(i) / denominator);
        }
        return probList;
    }

    private double getDenominator(List<Order> orderList, List<Double> probList, Matrix[][] mapGraph, int x) {
        double denominator = 0.0;
        double speed;
        for (int y = 0; y < mapGraph.length; y++) {
            if (!isBlocked(x, y)) {
                if (Objects.isNull(findNode(visitedNodes, y)) && Objects.nonNull(findOrder(orderList, nodeList.get(y)))) {
                    if (x != y) {
                        speed = Speed.valueOf(nodeList.get(x).getRegion().name() + nodeList.get(y).getRegion().name()).getSpeed();
                        probList.set(y, getNumerator(mapGraph[x][y], nodeList.get(y)) / speed);
                        denominator += probList.get(y);
                    }
                }
            }
        }
        return denominator;
    }

    private boolean isBlocked(int x, int y) {
        for (Connection cn : connectionList) {
            if (cn.getXIndex() == x && cn.getYIndex() == y)
                return false;
        }
        return true;
    }

    private Double getNumerator(Matrix values, Node n) {
        double numerator = 0.0;
        double pheromoneConc = values.getPheromoneConc();
        if (pheromoneConc > 0.0) {
            numerator = Math.pow(pheromoneConc, Constant.ALPHA);
            numerator *= Math.pow(1 / values.getHeuristicValue(), Constant.BETA);
            numerator *= Math.pow(1 / (double) n.getRegion().getMaxHours(), Constant.BETA);
        }
        return numerator;
    }

    private Order findOrder(List<Order> orders, Node node) {
        return orders.stream().filter(order -> order.getDestination() == node).findFirst().orElse(null);
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
            pheromoneConc = (1 - Constant.RHO) * value.getPheromoneConc() + (1.0 / totalCost);
            value.setPheromoneConc(pheromoneConc);
        }
    }


    @Override
    public int compareTo(Ant ant) {
        return (int) ((visitedNodes.size() / totalCost) - (ant.getVisitedNodes().size() / ant.getTotalCost()));
    }
}
