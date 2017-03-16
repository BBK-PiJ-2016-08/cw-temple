package student.EscapeAlgorithm;

import game.Edge;
import game.EscapeState;
import game.Node;
import java.util.*;

/**
 * Created by jakeholdom on 27/02/2017.
 */
public class Escape {

  private EscapeState state;
  private int MAX_NUMBER_OF_MOVES;
  private Node startNode;
  private Node endNode;
  private Map<Node, Node> predecessors = new HashMap<>();
  private Map<Node, Integer> distance = new HashMap<>();
  private Set<Node> uncheckedNodes = new HashSet<>();
  private Set<Node> checkedNodes = new HashSet<>();
  private List<Edge> edges = new ArrayList<>();
  private int totalMoves = 0;


  public Escape(EscapeState state) {

    this.state = state;
    this.MAX_NUMBER_OF_MOVES = state.getTimeRemaining();
    this.startNode = state.getCurrentNode();
    this.endNode = state.getExit();

  }

  public void initialiseState(Node firstNode) {

    List<Node> nodes = new ArrayList<>(state.getVertices());

    for (Node n : nodes) {

      for (Edge e : n.getExits()) {

        this.edges.add(e);
      }

    }
    distance.put(firstNode, 0);
    uncheckedNodes.add(firstNode);
    while (uncheckedNodes.size() > 0) {
      Node node = getMinimum(uncheckedNodes);
      checkedNodes.add(node);
      uncheckedNodes.remove(node);
      findMinimalDistances(node);
    }

  }

  public List<Node> getPathToTarget(Node target) {

    List<Node> path = new ArrayList<>();
    Node step = target;

    // check if a path exists
    if (predecessors.get(step) == null) {
      return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      setTotalMoves(predecessors.get(step).getEdge(step).length);
      step = predecessors.get(step);
      path.add(step);
    }
    Collections.reverse(path);
    return path;
  }

  private void setTotalMoves(int totalMoves) {

    this.totalMoves += totalMoves;
  }

  public int getTotalMoves() {

    return totalMoves;
  }

  private int getDistance(Node node, Node target) {

    for (Edge edge : this.edges) {

      if (edge.getSource().equals(node)
          && edge.getDest().equals(target)) {
        return edge.length();
      }
    }
    throw new IllegalArgumentException("Error: Distance not found");
  }

  private List<Node> getNeighbors(Node node) {
    List<Node> neighbors = new ArrayList<>();
    for (Edge edge : edges) {
      if (edge.getSource().equals(node)
          && !isChecked(edge.getDest())) {
        neighbors.add(edge.getDest());
      }
    }
    return neighbors;
  }


  private void findMinimalDistances(Node node) {
    List<Node> adjacentNodes = getNeighbors(node);
    for (Node target : adjacentNodes) {
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) {
        distance.put(target, (getShortestDistance(node)
            + getDistance(node, target)));
        predecessors.put(target, node);
        uncheckedNodes.add(target);
      }
    }

  }

  private boolean isChecked(Node vertex) {
    return checkedNodes.contains(vertex);
  }

  private int getShortestDistance(Node destination) {
    Integer d = distance.get(destination);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }

  private Node getMinimum(Set<Node> vertices) {
    Node minimum = null;
    for (Node vertex : vertices) {
      if (minimum == null) {
        minimum = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
          minimum = vertex;
        }
      }
    }
    return minimum;
  }
}
