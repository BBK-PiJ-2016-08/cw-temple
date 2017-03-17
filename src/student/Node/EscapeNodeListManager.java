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
 */
public class EscapeNodeListManager {
  private EscapeState state;

  public EscapeNodeListManager(EscapeState state){

    this.state = state;

  }
  private List<game.Node> getListOfHighestGoldAmounts(EscapeState state) {
    return state.getVertices().stream()
        .sorted(Comparator.comparing(s -> s.getTile().getGold()))
        .collect(Collectors.toList());

  }


  private List<game.Node> findSmallestListOfMoves(List<List<game.Node>> listOfListOfMoves) {
    List<game.Node> listOfMoves = new ArrayList<>();

    int smallest = Integer.MAX_VALUE;
    for (List<game.Node> lists : listOfListOfMoves) {

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
  private List<List<game.Node>> getTopFiveListOfNodes(List<game.Node> nodes, EscapeState state, Escape escape){

    List<List<game.Node>> topFiveNodeLists = new ArrayList<>();
    Collections.reverse(nodes);
    int nodeNumber = 0;
    escape.initialiseState(state.getCurrentNode());
    while (topFiveNodeLists.size() < 5){
      List<game.Node> nodePath = escape.getPathToTarget(nodes.get(nodeNumber));
      if (nodePath != null) {
        topFiveNodeLists.add(nodePath);
        nodeNumber++;
      }else {
        nodes.remove(nodeNumber);
      }

    }
    return topFiveNodeLists;
  }

  public List<Node> checkMovesToEnd(EscapeState state) {
    Escape escapeToExitNew = new Escape(state);
    escapeToExitNew.initialiseState(state.getCurrentNode());
    List<game.Node> listOfMovesToEnd = escapeToExitNew.getPathToTarget(state.getExit());

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
  public List<Node> getListOfMoves() {

    List<game.Node> nodes = getListOfHighestGoldAmounts(state);
    Escape escape = new Escape(state);
    return findSmallestListOfMoves(
        getTopFiveListOfNodes(nodes, state, escape));


  }
}
