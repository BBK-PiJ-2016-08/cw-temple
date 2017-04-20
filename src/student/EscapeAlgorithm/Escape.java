package student.EscapeAlgorithm;

import game.Edge;
import game.EscapeState;
import game.Node;
import java.util.*;

/**
 * Created by jakeholdom on 27/02/2017.
 * Based on the Dijkstra algorithm
 */
public class Escape {

  private EscapeState state; //current state of the character
  private Map<Node, Node> predecessorNodes = new HashMap<>();
  private Map<Node, Integer> distanceToInitialNode = new HashMap<>();
  private List<Node> undefinedNodes = new ArrayList<>();
  private List<Edge> allEdges = new ArrayList<>();
  private int totalMoves = 0; //Total moves it'll take to get to the destination node


  /**
   * Constructor: Initialise Explore class with the state.
   *
   * @param state The current position of the character
   */
  public Escape(EscapeState state) {
    this.state = state;
  }

  /**
   * This method needs to be called before any path can be created. This sets all of the Lists
   * and map's with the nodes the edge's of the graph. Gets all the edges from the vertices and
   * puts them in a list. This also maps the shortest distance to each node from the initial node.
   *
   * @param initialNode The first node that the path starts at
   */
  public void initialiseState(Node initialNode) {

    for (Node n : state.getVertices()) {
      for (Edge e : n.getExits()) {
        this.allEdges.add(e);
      }
    }
    distanceToInitialNode.put(initialNode, 0);
    undefinedNodes.add(initialNode);
    while (undefinedNodes.size() > 0) {
      Node node = getSmallestDistanceToNode();
      undefinedNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  /**
   * This method finds the nearest node from the list of undefined nodes.
   *
   * @return The closest node to the initial node in the undefined nodes list
   */
  private Node getSmallestDistanceToNode() {
    Node minimum = undefinedNodes.get(0);
    for (Node vertex : undefinedNodes) {
      if (checkShortestDistance(vertex) < checkShortestDistance(minimum)) {
        minimum = vertex;
      }
    }
    return minimum;
  }

  /**
   * Checks whether the node is in the distanceToTarget map and returns an int corresponding
   * to how far away the node is from the initial node.
   *
   * @param node node to check how far away from initial node
   * @return int of distance
   */
  private int checkShortestDistance(Node node) {
    Integer i = distanceToInitialNode.get(node);
    if (i == null) {
      return Integer.MAX_VALUE;
    } else {
      return i;
    }
  }

  /**
   * Checks each neighbour of the node being analysed. If the distance of the neighbour is more
   * than current node plus the distance plus the distance from the current node to the neighbour
   * then it is a shorter distance
   *
   * @param node node currently checking for distances
   */
  private void findMinimalDistances(Node node) {
    for (Node neighbour : node.getNeighbours()) {

      if (checkShortestDistance(neighbour)
          > checkShortestDistance(node) + getDistanceToTarget(node, neighbour)) {

        distanceToInitialNode.put(neighbour, (checkShortestDistance(node)
            + getDistanceToTarget(node, neighbour))); //Adds node and distance to node to map
        predecessorNodes.put(neighbour, node);
        undefinedNodes.add(neighbour);

      }

    }

  }

  /**
   * Finds the initial node and target node from the list of edges and returns an int
   * defining the distance between the two vertices. Returns illegalArgumentException if
   * the node is not found.
   *
   * @param node start node
   * @param target end node
   * @return int of distance
   */
  private int getDistanceToTarget(Node node, Node target) {

    Optional<Edge> edge = allEdges.stream().filter(s -> s.getSource().equals(node))
        .filter(e -> e.getDest().equals(target)).findFirst();
    if (edge.isPresent()) {
      return edge.get().length();
    } else {
      throw new IllegalArgumentException("Error: Edge not found");
    }
  }

  /**
   * Initialises a list to contain the path to the target. Searches the predecessorNodes Map
   * for the destination node and uses the map created by the in the initialisation algorithm
   * to iteratively get the closest neighbour to the initial node until it comes to the initial
   * node. The list is then reversed so it is in the correct order for the character to move
   * to the correct adjacent nodes. setTotalMoves method is run after each node is added to the
   * list to keep a record of how long the path will take to complete.
   *
   * @param destination The node that is started on to reach the initial node
   * @return List defining the path to the destination node
   */
  public List<Node> getPathToTarget(Node destination) {

    List<Node> pathToTarget = new ArrayList<>();

    if (predecessorNodes.get(destination) == null) {
      return null; //Path doesn't exist
    }
    pathToTarget.add(destination);
    while (predecessorNodes.get(destination) != null) {
      setTotalMoves(predecessorNodes.get(destination).getEdge(destination).length);
      destination = predecessorNodes.get(destination);
      pathToTarget.add(destination);

    }
    Collections.reverse(pathToTarget);
    return pathToTarget;
  }

  /**
   * Setter: Called every time a new move is added to the path of moves.
   *
   * @param totalMoves How much time the given move will take
   */
  private void setTotalMoves(int totalMoves) {

    this.totalMoves += totalMoves;
  }

  /**
   * Getter: The total amount of time the path will take to complete.
   *
   * @return int of the amount of time the path will take to complete
   */
  public int getTotalMoves() {

    return totalMoves;
  }


}
