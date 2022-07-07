package pe.pucp.edu.vrp.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pe.pucp.edu.vrp.util.Constant;
import pe.pucp.edu.vrp.util.Problem;
import pe.pucp.edu.vrp.util.Region;
import pe.pucp.edu.vrp.util.Speed;

/**
 * <b>Class</b>: Ant <br/>
 * Ants can carry 50 times their own body weight
 */
@Getter
@Setter
@ToString
public class Ant implements Comparable<Ant> {
    private List<Visited> visitedList;
    private List<Node> nodeList;
    private List<Order> orderList;
    private double totalCost;
    private double currentLoad;
    private int start;


    public Ant(int x, List<Node> nodeList, List<Order> orderList) {
        visitedList = new ArrayList<>();
        totalCost = 0;
        currentLoad = 0;
        start = x;
        this.nodeList = nodeList;
        this.orderList = new ArrayList<>(orderList);
    }

    public Ant(int x, List<Node> nodeList) {
        visitedList = new ArrayList<>();
        totalCost = 0;
        start = x;
        orderList = new ArrayList<>();
        this.nodeList = nodeList;
    }

    public void work(Matrix[][] mapGraph, double maxLoad) {
        int xIndex = start, yIndex, count = 0;
        Node next = nodeList.get(start);
        Order order;
        Visited visited;
        double speed, cost;
        boolean existsOrder, existsNode;

        visitedList.add(new Visited(next.getUbigeo(), next.getMatrixIndex(), 0.0));
        while (true) {
            yIndex = chooseNext(mapGraph, xIndex);
            if (yIndex == -1) {
                break;
            }
            next = nodeList.get(yIndex);
            order = findOrder(next);
            speed = Speed.valueOf(next.getRegion().name() + nodeList.get(xIndex).getRegion().name()).getSpeed();
            existsOrder = Objects.nonNull(order);
            existsNode = notExistsNode(next.getMatrixIndex());
            cost = mapGraph[xIndex][yIndex].getHeuristicValue() / speed;
            visited = new Visited(next.getUbigeo(), next.getMatrixIndex(), cost);
            visitedList.add(visited);
            totalCost += cost;
            if (xIndex != yIndex) totalCost += 1;
            if (existsOrder && existsNode) {
                if (order.getPackageAmount() + currentLoad > maxLoad ||
                        order.getRemainingTime() < totalCost + mapGraph[xIndex][yIndex].getHeuristicValue() / speed) {
                    count++;
                    break;
                } else {
                    count = 0;
                    visited.setOrderId(order.getOrderId());
                    boolean first = true;
                    while(existsOrder && order.getPackageAmount() + currentLoad < maxLoad) {
                        currentLoad += order.getPackageAmount();
                        orderList.remove(order);
                        if (first) {
                            first = false;
                        } else {    //add partial/same node order to visited list
                            visited = new Visited(next.getUbigeo(), next.getMatrixIndex(), 0);
                            visited.setOrderId(order.getOrderId());
                            visitedList.add(visited);
                        }

                        order = findOrder(next);
                        existsOrder = Objects.nonNull(order);
                    }
                }
            } else if (!existsOrder) {
                count++;
            }
            localUpdate(mapGraph[xIndex][yIndex]);
            xIndex = yIndex;

            if (totalCost > Region.SELVA.getMaxHours() || orderList.isEmpty()) {
                break;
            }
        }

        for (int i = visitedList.size() - 1; count > 0; i--) {
            totalCost -= visitedList.get(i).getTravelCost();
            visitedList.remove(i);
            count--;
        }
    }

    public void routeBack(Matrix[][] mapGraph) {
        int xIndex = start, yIndex;
        Node nextNode;
        double speed, cost;

        while (true) {
            yIndex = chooseNext(mapGraph, xIndex);
            if (yIndex == -1) {
                break;
            }
            nextNode = nodeList.get(yIndex);
            speed = Speed.valueOf(nextNode.getRegion().name() + nodeList.get(xIndex).getRegion().name()).getSpeed();
            if (notExistsNode(nextNode.getMatrixIndex())) {
                cost = mapGraph[xIndex][yIndex].getHeuristicValue() / speed;
                visitedList.add(new Visited(nextNode.getUbigeo(), nextNode.getMatrixIndex(), cost));
                totalCost += cost;
                if (xIndex != yIndex) totalCost += 1;
                localUpdate(mapGraph[xIndex][yIndex]);
                xIndex = yIndex;
            }

            if (isDepot(nextNode)) {
                break;
            }
        }
    }

    private boolean isDepot(Node node) {
        return node.getCity().equals("LIMA") || node.getCity().equals("TRUJILLO") || node.getCity().equals("AREQUIPA");
    }

    private int chooseNext(Matrix[][] mapGraph, int x) {
        double randVal = Math.random();
        int i, yVal = -1;
        List<Double> probList = calcProbability(mapGraph, x);
        for (i = 0; i < mapGraph.length; i++) {
            if (probList.get(i) > randVal) {
                yVal = i;
                break;
            } else
                randVal -= probList.get(i);
        }
        return yVal;
    }

    private List<Double> calcProbability(Matrix[][] mapGraph, int x) {
        List<Double> probList = new ArrayList<>();
        for (int i = 0; i < mapGraph.length; i++) {
            probList.add(0.0);
        }
        double denominator = getDenominator(probList, mapGraph, x);
        for (int i = 0; i < mapGraph.length; i++) {
            probList.set(i, probList.get(i) / denominator);
        }
        return probList;
    }

    private double getDenominator(List<Double> probList, Matrix[][] mapGraph, int x) {
        double denominator = 0.0;
        double speed;
        for (int y = 0; y < mapGraph.length; y++) {
            if (!Problem.isBlocked(x, y, totalCost) && notExistsNode(y)) {
                speed = Speed.valueOf(nodeList.get(x).getRegion().name() + nodeList.get(y).getRegion().name()).getSpeed();
                probList.set(y, getNumerator(mapGraph[x][y], nodeList.get(y)) / speed);
                denominator += probList.get(y);
            }
        }
        return denominator;
    }

    private Double getNumerator(Matrix values, Node n) {
        double numerator = 0.0;
        double pheromoneConc = values.getPheromoneConc();
        if (pheromoneConc > 0.0) {
            numerator = Math.pow(pheromoneConc, Constant.ALPHA);
            numerator *= Math.pow(1 / values.getHeuristicValue(), Constant.BETA);
            numerator *= Math.pow(1 / (double) n.getRegion().getMaxHours(), Constant.BETA);
            if (Objects.nonNull(findOrder(n)))
                numerator *= 3;
            else
                numerator *= 0.5;
        }
        return numerator;
    }

    private Order findOrder(Node node) {
        return orderList.stream().filter(order -> order.getDestination() == node).findFirst().orElse(null);
    }

    private boolean notExistsNode(int i) {
        return Objects.isNull(visitedList.stream()
                .filter(node -> i == node.getMatrixIndex())
                .findFirst()
                .orElse(null));
    }

    private void localUpdate(Matrix value) {
        double pheromoneConc;
        if (value.getHeuristicValue() > 0 && totalCost > 0) {
            pheromoneConc = (1 - Constant.RHO) * value.getPheromoneConc() + (1.0 / totalCost);
            value.setPheromoneConc(pheromoneConc);
        }
    }


    @Override
    public int compareTo(Ant ant) {
        return (int) ((orderList.size() * 100 + totalCost) - (ant.getOrderList().size() * 100 + ant.getTotalCost()));
    }
}
