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
  private Map<Node, Node> previousNodes = new HashMap<>();
  private Map<Node, Integer> distanceToTarget = new HashMap<>();
  private List<Node> uncheckedNodes = new ArrayList<>();
  private List<Edge> allEdges = new ArrayList<>();
  private int totalMoves = 0;


  public Escape(EscapeState state) {
    this.state = state;
  }

  public void initialiseState(Node initialNode) {

    List<Node> nodes = new ArrayList<>(state.getVertices());

    for (Node n : nodes) {
      for (Edge e : n.getExits()) {
        this.allEdges.add(e);
      }
    }
    distanceToTarget.put(initialNode, 0);
    uncheckedNodes.add(initialNode);
    while (uncheckedNodes.size() > 0) {
      Node node = getMinimum(uncheckedNodes);
      uncheckedNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  public List<Node> getPathToTarget(Node target) {

    List<Node> pathToTarget = new ArrayList<>();
    Node step = target;

    if (previousNodes.get(step) == null) {
      return null; //Path doesn't exist
    }
    pathToTarget.add(step);
    while (previousNodes.get(step) != null) {
      setTotalMoves(previousNodes.get(step).getEdge(step).length);
      step = previousNodes.get(step);
      pathToTarget.add(step);

    }
    Collections.reverse(pathToTarget);
    return pathToTarget;
  }

  private void setTotalMoves(int totalMoves) {

    this.totalMoves += totalMoves;
  }

  public int getTotalMoves() {

    return totalMoves;
  }

  private int getDistanceToTarget(Node node, Node target) {

    Optional<Edge> edge = allEdges.stream().filter(s-> s.getSource().equals(node))
        .filter(e -> e.getDest().equals(target)).findFirst();
    if (edge.isPresent()){
      return edge.get().length();
    }else {
      throw new IllegalArgumentException("Error: Edge not found");
    }
  }

  private void findMinimalDistances(Node node) {
    for (Node target : node.getNeighbours()) {
      if (getShortestDistanceToDestination(target) >
          getShortestDistanceToDestination(node) + getDistanceToTarget(node, target)) {
        distanceToTarget.put(target, (getShortestDistanceToDestination(node)
            + getDistanceToTarget(node, target)));
        previousNodes.put(target, node);
        uncheckedNodes.add(target);
      }
    }

  }

  private int getShortestDistanceToDestination(Node destination) {
    Integer i = distanceToTarget.get(destination);
    if (i == null) {
      return Integer.MAX_VALUE;
    } else {
      return i;
    }
  }

  private Node getMinimum(List<Node> vertices) {
    Node minimum = vertices.get(0);
    for (Node vertex : vertices) {
      if (getShortestDistanceToDestination(vertex) < getShortestDistanceToDestination(minimum)) {
        minimum = vertex;
      }
    }
    return minimum;
  }
}
