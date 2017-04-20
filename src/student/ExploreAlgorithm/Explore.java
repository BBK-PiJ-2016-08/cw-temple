package student.ExploreAlgorithm;

import game.*;

import java.util.*;
import java.util.stream.Collectors;
import student.Node.ExploreNodeImpl;


/**
 * Created by jakeholdom on 14/02/2017.
 */
public class Explore {

  private List<ExploreNodeImpl> allNodes = new ArrayList<>(); //Array storing all nodes
  private List<Long> idsVisited = new ArrayList<>(); //Array storing all ID's visited
  private List<Long> goingBackwardsNodes = new ArrayList<>(); //Array of nodes when going backwards
  private List<Long> nodesAlreadyAttempted = new ArrayList<>();//Array of nodes already attempted
  //when going backwards to search

  private Map<Integer, Long> allCurrentNodesSearched = new HashMap<>();//Map of all nodes searched
  private int getLocation = 1;
  private int numberOfTimesMovedFurtherFromOrb = 0;//Number to see if moving further from orb


  private boolean isGoingBackwards = false;//Boolean to check whether going backwards
  private long nodeToGetTo = 0;//Next node which is going to be reached


  private int moveCount = 0;//Number of moves

  private ExplorationState state;//Current state of character


  /**
   * Constructor: Initialise Explore class with the state.
   *
   * @param state The current position of the character
   */
  public Explore(ExplorationState state) {

    this.state = state;

  }

  /**
   * Saves current state information into their respective arrays.
   */
  private void saveStateInfo() {
    idsVisited.add(state.getCurrentLocation());//Adds ID to array
    ExploreNodeImpl node = new ExploreNodeImpl();//Initialise ExploreNodeImpl
    node.setLocation(state.getCurrentLocation());//Sets current location
    node.setNeighbours(state.getNeighbours());//Sets neighbours
    allNodes.add(node);//Adds current node to array of all nodes
  }

  /**
   * Checks whether the current state has already been saved
   * If not then saveStateInfo is called.
   */
  private void checkIfStateHasBeenSaved() {
    if (!idsVisited
        .contains(state.getCurrentLocation())) { //Checks if current location has been saved
      saveStateInfo();

    }
  }

  /**
   * Method which runs through the array of nodes to find the index
   * of the node that needs to be found.
   *
   * @param stateNode ExploreNode which needs to be found
   * @return index of where node is in AllNodes
   */
  private int getIndexOfLocation(long stateNode) {

    int index = 0;//initialise int
    for (int i = 0; i < allNodes.size(); i++) {

      if (allNodes.get(i).getLocation() == stateNode) { //if location equals location provided

        index = i;

      }
    }
    return index;//return array index of node
  }


  /**
   * Main method which is called from the Explorer class, this finds the next appropriate move
   * for the character and returns the next move.
   *
   * @return the long value of the next move.
   */
  public long getNextMove() {

    checkIfStateHasBeenSaved();

    if (numberOfTimesMovedFurtherFromOrb
        > 10) { //If character has moved further away from orb 10 times
      numberOfTimesMovedFurtherFromOrb = 0;
      return moveBackwardsToFindAlternateRoute();//Then move find alternate route

    }
    if (goingBackwardsNodes.size() == 0) { //If character has finished going backwards
      goingBackwardsNodes.clear();
      isGoingBackwards = false;//change to false
    }

    if (isGoingBackwards) { //If character is going backwards

      return getNextBackwardsMove();
    }

    Collection<NodeStatus> nextNodes = findAllNodesNotVisited();

    if (nextNodes.isEmpty()) { //If there are no more possible moves, go backwards

      return moveBackwardsToFindAlternateRoute();

    }

    moveCount++;//increment move count

    NodeStatus nextNode = getShortestNodeToTarget(nextNodes);//Returns the closest node to the orb

    incrementNumberIfCharacterMovesFurtherFromOrb(nextNode);
    return nextNode.getId();//return move

  }

  /**
   * increments or reinitialise's the numberOfTimeMovedFurtherFromOrb int, on whether the
   * character is moving further away or closer to the orb.
   *
   * @param nodeFromOrb ExploreNode which is further or closer to orb
   */
  private void incrementNumberIfCharacterMovesFurtherFromOrb(NodeStatus nodeFromOrb) {
    if (nodeFromOrb.getDistanceToTarget() > state.getDistanceToTarget()) {
      numberOfTimesMovedFurtherFromOrb++;
    } else {

      numberOfTimesMovedFurtherFromOrb = 0;
    }

  }

  /**
   * Finds next move if moving backward through the nodes, if route is going round in a circle
   * then it will find an alternate route.
   *
   * @return long of next move
   */
  private long getNextBackwardsMove() {

    getLocation = 1;
    allCurrentNodesSearched.clear();
    long nextMove = goingBackwardsNodes.get(0);//Get next move for going backwards
    goingBackwardsNodes.remove(0);//Remove the next move from list
    List<Long> nextNodesNeighbours = new ArrayList<>();

    for (NodeStatus n : state.getNeighbours()) { //Gets all neighbours of current node

      nextNodesNeighbours.add(n.getId());//Adds them to a list

    }
    if (!nextNodesNeighbours.contains(nextMove)) { //If neighbours doesn't contain next move
      goingBackwardsNodes.clear();//clear current list, not correct route
      nextMove = moveBackwardsToFindAlternateRoute();//Find alternate route

    }
    return nextMove;//return next move
  }

  /**
   * Searches through array of nodes visited's neighbours, and then compares them to the
   * ID's visited and produces a collection of NodeStatuses which haven't been visited yet.
   *
   * @return Collection of nodes not visited
   */
  private Collection<NodeStatus> findAllNodesNotVisited() {

    return allNodes.get(getIndexOfLocation(state.getCurrentLocation()))
        .getNeighbours().stream() //finds all nodes currently accessed neighbours
        .filter(s -> !idsVisited.contains(s.getId()))//checks whether the id's have been visited
        .collect(Collectors.toList());//collects into a list on NodeStatus's

  }

  /**
   * Compares all the nodes in a collection and returns the single NodeStatus with the shortest
   * distance to the target.
   *
   * @return Collection of nodes not visited
   */
  private NodeStatus getShortestNodeToTarget(Collection<NodeStatus> nodes) {
    return nodes.stream()
        .min(Comparator.comparing(NodeStatus::getDistanceToTarget))
        .get();//returns node with smallest distance to target
  }

  /**
   * Searches through the allNodes list to find all available nodes with a neighbour
   * which hasn't been accessed yet. It is then passed through the getShortestNodeToTarget
   * method to return the single node which hasn't been accessed and with the shortest
   * distance to the orb.
   *
   * @return Returns unused node.
   */
  private long findFirstNodeWithUnusedNeighbour() {

    List<NodeStatus> neighbours = allNodes.stream().map(ExploreNodeImpl::getNeighbours)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());//Collects all neighbours of all nodes visited
    List<NodeStatus> neighboursNotVisited = new ArrayList<>();//initialise new list to collect
    // all neighbours not visited

    for (NodeStatus n : neighbours) { //loops through all neighbours

      if (!idsVisited.contains(n.getId()) && !nodesAlreadyAttempted.contains(n.getId())) {
        neighboursNotVisited.add(n);//if neighbour hasn't been visited add to this list

      }
    }

    //remove any duplicates
    neighboursNotVisited = neighboursNotVisited.stream().distinct().collect(Collectors.toList());

    return getShortestNodeToTarget(neighboursNotVisited).getId();//return neighbour with shortest
    //distance to target
  }

  /**
   * Recursive method that gets the path to a given destination. It saves the nodes that have
   * already been searched to find alternative routes if the route is a dead end. This method
   * is used when the character has to go backwards to find an alternate route.
   *
   * @param destination Dynamic node id to reach
   * @param currentLocation Dynamic node currently on whilst searching for target
   * @param nodesToVisit Nodes that still need to be searched to reach target
   * @param nodesVisited Nodes already searched to reach target
   */
  private void findPathToDestination(long destination, ExploreNodeImpl currentLocation,
      List<Long> nodesToVisit, List<Long> nodesVisited) {

    long numberToFind = findClosestNeighbourFromListOfNeighbours(currentLocation.getNeighbours(),
        destination, nodesVisited); //Returns the target number to create a path to

    List<Long> checkNull = new ArrayList<>();//Array to check whether the path is null which means
    //that the the target is the tile that the character is already on.

    if (numberToFind > 0) {
      for (NodeStatus n : currentLocation.getNeighbours()) {

        //if the neighbour is the final destination we are trying to find
        if (n.getId() == destination) {
          allCurrentNodesSearched.put(getLocation, currentLocation.getLocation());
          getLocation++;
          nodesVisited.add(n.getId());
          nodesToVisit.add(n.getId());
          this.goingBackwardsNodes = nodesToVisit;
          return;
        }
        //if the neighbour is the number we are trying to find
        if (n.getId() == numberToFind && !nodesVisited.contains(n.getId())) {
          checkNull.add(n.getId());

          allCurrentNodesSearched.put(getLocation, currentLocation.getLocation());
          getLocation++;
          currentLocation = allNodes.get(getIndexOfLocation(n.getId()));

          nodesVisited.add(n.getId());
          nodesToVisit.add(n.getId());
          //recursively run the method again with an updated current location and nodesVisited and
          //nodesToVisit
          findPathToDestination(destination, currentLocation, nodesToVisit, nodesVisited);
        }
      }

    }

    if (checkNull.isEmpty()) { //Deal with error of trying to find tile which character is on

      getLocation--;

      currentLocation = allNodes
          .get(getIndexOfLocation(allCurrentNodesSearched.get(getLocation)));

      allCurrentNodesSearched.remove(getLocation);
      nodesToVisit.remove(nodesToVisit.get(nodesToVisit.size() - 1));

      findPathToDestination(destination, currentLocation, nodesToVisit, nodesVisited);


    }
  }


  /**
   * Finds the closest neighbour to a specified node id which hasn't been visited yet.
   *
   * @param neighboursOfCurrentState Collection of all neighbours of the specified state
   * @param nodeId Target node id to compare the neighbour id's to
   * @param nodesVisited Nodes already visited
   * @return long value of node which is closest to destination
   */
  private long findClosestNeighbourFromListOfNeighbours(
      Collection<NodeStatus> neighboursOfCurrentState,
      long nodeId, List<Long> nodesVisited) {

    long nearest = -1;
    long idToMoveTo = 10000000;
    for (NodeStatus n : neighboursOfCurrentState) {

      if (n.getId() == nodeId) {
        saveStateInfo();
        return n.getId();
      } else {

        long d = Math.abs(nodeId - n.getId());
        if (d < idToMoveTo && idsVisited.contains(n.getId()) && !nodesVisited.contains(n.getId())) {

          idToMoveTo = d;
          nearest = n.getId();

        }
      }
    }

    return nearest;
  }

  /**
   * If there has been an error and no routes can be found for some reason then this method
   * clears all of the current saved node data and resets it's search for the correct route.
   * It then finds a new node from the current position which is the closest neighbour to the orb.
   *
   * @return next node to visit
   */
  private long clearAllNodes() {

    allNodes.clear();
    allCurrentNodesSearched.clear();
    idsVisited.clear();
    moveCount = 0;

    saveStateInfo();
    nodeToGetTo = findFirstNodeWithUnusedNeighbour();
    return nodeToGetTo;
  }

  /**
   * First method called if the character has to go backwards. Sets boolean isGoingBackwards
   * to true and then runs method which checks if the neighbour which needs to be reached
   * has already been attempted. If it has then
   *
   * @return next node to visit
   */
  private long moveBackwardsToFindAlternateRoute() {

    this.isGoingBackwards = true;//boolean set to true for getNextMove method

    while (goingBackwardsNodes.isEmpty()) {
      nodeToGetTo = findFirstNodeWithUnusedNeighbour();
      ExploreNodeImpl currentNode;

      if (!nodesAlreadyAttempted.contains(nodeToGetTo)) {

        if (nodeToGetTo != 0) {

          currentNode = allNodes.get(getIndexOfLocation(state.getCurrentLocation()));

        } else {

          nodeToGetTo = clearAllNodes();
          currentNode = allNodes.get(getIndexOfLocation(state.getCurrentLocation()));

        }
        nodesAlreadyAttempted.add(nodeToGetTo);
        List<Long> nodesToVisit = new ArrayList<>();
        List<Long> nodesVisited = new ArrayList<>();
        getLocation = 0;
        findPathToDestination(nodeToGetTo, currentNode, nodesToVisit, nodesVisited);

      }

    }

    if (goingBackwardsNodes.size() == 1) {
      isGoingBackwards = false;
    }

    long returnLong = goingBackwardsNodes.get(0);

    goingBackwardsNodes.remove(0);
    return returnLong;
  }
}
