package student.Node;

import game.*;
import game.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import student.EscapeAlgorithm.Escape;

/**
 * Created by jakeholdom on 15/03/2017.
 * This class handles which node should be the next destination for the character to
 * move to and returning the path/list of moves to the Explorer.
 *
 */
public class EscapeNodeListManager {


  private EscapeState state;

  /**
   * Constructor: Initialise EscapeNodeListManager class with the state.
   *
   * @param state The current position of the character
   */
  public EscapeNodeListManager(EscapeState state){

    this.state = state;

  }

  /**
   * Method which returns the list of moves to the explorer. Initialises the Dijkstra algorithm
   * class and returns the list of moves with the shortest path and highest gold amount
   *
   * @return Final list of moves
   */
  public List<Node> getListOfMoves() {
    Escape escape = new Escape(state);
    List<Node> nodes = getListOfHighestGoldAmounts(state);
    return findSmallestListOfMoves(
        getTopFiveListOfNodesPaths(nodes, state, escape));


  }

  /**
   * Sorts the vertices in order of largest amounts of gold.
   *
   * @param state For the current state of the graph
   * @return List of nodes in order of amount of gold on the tile
   */
  private List<Node> getListOfHighestGoldAmounts(EscapeState state) {
    return state.getVertices().stream()
        .sorted(Comparator.comparing(s -> s.getTile().getGold()))
        .collect(Collectors.toList());
  }

  /**
   * Finds paths for the tiles with the top five amounts of gold on them.
   *
   * @param nodes Contains all nodes in order of the amount gold on the tile
   * @param state The current state that the character is in
   * @param escape The current instance of the Dijkstra algorithm
   * @return List of List of nodes containing the path to the top five nodes
   */
  private List<List<Node>> getTopFiveListOfNodesPaths(List<Node> nodes, EscapeState state, Escape escape){

    List<List<Node>> topFiveNodeLists = new ArrayList<>();
    Collections.reverse(nodes);
    int nodeNumber = 0;
    escape.initialiseState(state.getCurrentNode());
    while (topFiveNodeLists.size() < 5){
      List<Node> nodePath = escape.getPathToTarget(nodes.get(nodeNumber));
      if (nodePath != null) {
        topFiveNodeLists.add(nodePath);
        nodeNumber++;
      }else {
        nodes.remove(nodeNumber);
      }

    }
    return topFiveNodeLists;
  }


  /**
   * Finds which one of the nodes with the top 5 has the smallest path and removes
   * initial value which would be the tile that the character is already on.
   *
   * @param listOfListOfMoves Contains all of the list of moves to the top 5 amounts
   */
  private List<Node> findSmallestListOfMoves(List<List<Node>> listOfListOfMoves) {
    List<Node> listOfMoves = new ArrayList<>();

    int smallest = Integer.MAX_VALUE;
    for (List<Node> lists : listOfListOfMoves) {

      if (lists.size() < smallest) {

        listOfMoves = lists;
        smallest = lists.size();
      }

    }

    if (!listOfMoves.isEmpty()) {
      listOfMoves.remove(0); // remove initial value

    } else {

      return null;
    }

    return listOfMoves;

  }

  /**
   * This method checks to see whether the character has enough time to get to the gold
   * by constantly checking how far the current state is to the exit and how many moves
   * are left. If there is not enough time then the character begins to move to the exit
   *
   * @param state The current state in which the character is in
   */
  public List<Node> checkMovesToEnd(EscapeState state) {
    Escape escapeToExitNew = new Escape(state);
    escapeToExitNew.initialiseState(state.getCurrentNode());
    List<Node> listOfMovesToEnd = escapeToExitNew.getPathToTarget(state.getExit());

    int movesToEnd = escapeToExitNew.getTotalMoves();

    if (movesToEnd + 30 > state.getTimeRemaining()) {

      if (listOfMovesToEnd != null) {
        listOfMovesToEnd.remove(0);
      }
    }else{
      return null;
    }

    return listOfMovesToEnd;

  }

}
