package student.ExploreAlgorithm;

import game.*;

import student.Node.NodeImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jakeholdom on 14/02/2017.
 */
public class Explore {

    private List<NodeImpl> allNodes = new ArrayList<>();
    private List<Long> idsVisited = new ArrayList<>();
    private List<Long> reverseNodes = new ArrayList<>();
    private List<Long> unaccessableNodes = new ArrayList<>();
    private List<Long> nodesAlreadyAttempted = new ArrayList<>();

    private Map<Integer, Long> allCurrentLocationsSearched = new HashMap<Integer, Long>();
    private int getLocation = 1;
    private int numberOfTimesMovedFurtherFromOrb = 0;


    private boolean isGoingBackwards = false;
    private long nodeToGetTo = 0;


    private int moveCount = 0;

    private ExplorationState state;


    public Explore(ExplorationState state) {

        this.state = state;

    }

    private void saveStateInfo() {

        //implement check to see if node already visited

        idsVisited.add(state.getCurrentLocation());
        NodeImpl node = new NodeImpl(moveCount);
        node.setDistanceFromOrb(state.getDistanceToTarget());
        node.setLocation(state.getCurrentLocation());
        node.setVisited(true);
        node.setNeighbours(state.getNeighbours());
        allNodes.add(node);
    }

    private void checkIfStateHasBeenSaved() {
        if (!idsVisited.contains(state.getCurrentLocation())) {
            saveStateInfo();

        }
    }


    private int getIndexOfLocation(long stateLocation) {

        int index = 0;
        for (int i = 0; i < allNodes.size(); i++) {

            if (allNodes.get(i).getLocation() == stateLocation) {

                index = i;

            }
        }
        return index;
    }

    public long getNextMove() {

        checkIfStateHasBeenSaved();

        if (numberOfTimesMovedFurtherFromOrb > 10) {
            numberOfTimesMovedFurtherFromOrb = 0;
            return moveBackwardsToFindAlternateRoute();

        }
        if (reverseNodes.size() == 0) {
            reverseNodes.clear();
            isGoingBackwards = false;
        }

        if (isGoingBackwards) {
            getLocation = 1;
            unaccessableNodes.clear();
            allCurrentLocationsSearched.clear();
            long returnLong = reverseNodes.get(0);
            reverseNodes.remove(0);
            List<Long> nextNodesNeighbours = new ArrayList<>();

            // System.out.println("Neighbour = ");

            for (NodeStatus n : state.getNeighbours()) {


                nextNodesNeighbours.add(n.getId());
                // System.out.println(n.getId());

            }
            if (!nextNodesNeighbours.contains(returnLong)) {
                reverseNodes.clear();
                //System.out.println("Return long before = " + returnLong);
                returnLong = moveBackwardsToFindAlternateRoute();
                //System.out.println("Return long after = " + returnLong);

            }
            return returnLong;
        }


        Collection<NodeStatus> nextNodes = allNodes.get(getIndexOfLocation(state.getCurrentLocation()))
                .getNeighbours().stream()
                .filter(s -> !idsVisited.contains(s.getId()))
                .collect(Collectors.toList());

        if (nextNodes.isEmpty()) {

            return moveBackwardsToFindAlternateRoute();

        }

        moveCount++;

        NodeStatus nextNode = nextNodes.stream()
                .min(Comparator.comparing(NodeStatus::getDistanceToTarget))
                .get();
        if (nextNode.getDistanceToTarget() > state.getDistanceToTarget()) {
            numberOfTimesMovedFurtherFromOrb++;
        } else {

            numberOfTimesMovedFurtherFromOrb = 0;
        }

        return nextNode.getId();

    }

    private long findClosestNumberToTargetFromCollection(Collection<NodeStatus> nextNodes) {
        int lowestNumber = Integer.MAX_VALUE;
        long closestNeighbour = 0;

        for (NodeStatus n : nextNodes) {


            if (n.getDistanceToTarget() < lowestNumber) {

                lowestNumber = n.getDistanceToTarget();

                closestNeighbour = n.getId();

            }


        }

        return closestNeighbour;

    }


    private long findFirstNodeWithUnusedNeighbour() {


        List<NodeStatus> neighbours = allNodes.stream().map(NodeImpl::getNeighbours)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<NodeStatus> neighboursNotVisited = new ArrayList<>();

        for (NodeStatus n : neighbours) {

            if (!idsVisited.contains(n.getId()) && !nodesAlreadyAttempted.contains(n.getId())) {
                neighboursNotVisited.add(n);

            }
        }

        neighboursNotVisited = neighboursNotVisited.stream().distinct().collect(Collectors.toList());


        return findClosestNumberToTargetFromCollection(neighboursNotVisited);
    }

    private Long findClosestNeighbourFromListOfNeighbours(Collection<NodeStatus> neighboursOfCurrentState,
                                                          Long nodeID, List<Long> nodesVisited) {


        long nearest = -1;
        long idToMoveTo = 10000000;
        for (NodeStatus n : neighboursOfCurrentState) {

            if (n.getId() == nodeID) {
                saveStateInfo();
                return n.getId();
            } else {

                long d = Math.abs(nodeID - n.getId());
                if (d < idToMoveTo && idsVisited.contains(n.getId()) && !nodesVisited.contains(n.getId())) {

                    idToMoveTo = d;
                    nearest = n.getId();

                }
            }
        }


        return nearest;
    }

    private void recursiveFindPathToOrb(long destination, NodeImpl currentLocation, List<Long> nodesToVisit, List<Long> nodesVisited) {

        long numberToFind = findClosestNeighbourFromListOfNeighbours(currentLocation.getNeighbours(), destination, nodesVisited);


        List<Long> checkNull = new ArrayList<>();

        if (numberToFind > 0) {
            for (NodeStatus n : currentLocation.getNeighbours()) {

                if (n.getId() == destination) {
                    allCurrentLocationsSearched.put(getLocation, currentLocation.getLocation());
                    getLocation++;
                    nodesVisited.add(n.getId());
                    nodesToVisit.add(n.getId());
                    this.reverseNodes = nodesToVisit;
                    return;
                }

                if (n.getId() == numberToFind && !nodesVisited.contains(n.getId())) {
                    checkNull.add(n.getId());


                    allCurrentLocationsSearched.put(getLocation, currentLocation.getLocation());
                    getLocation++;
                    currentLocation = allNodes.get(getIndexOfLocation(n.getId()));

                    nodesVisited.add(n.getId());
                    nodesToVisit.add(n.getId());
                    recursiveFindPathToOrb(destination, currentLocation, nodesToVisit, nodesVisited);
                }
            }

        }


        if (checkNull.isEmpty()) {

            getLocation--;

            currentLocation = allNodes.get(getIndexOfLocation(allCurrentLocationsSearched.get(getLocation )));

            allCurrentLocationsSearched.remove(getLocation);
            nodesToVisit.remove(nodesToVisit.get(nodesToVisit.size() - 1));

            //System.out.println("CURRENT LOCATION = ");


            recursiveFindPathToOrb(destination, currentLocation, nodesToVisit, nodesVisited);


        }
    }

    private long clearAllNodes() {

        allNodes.clear();
        allCurrentLocationsSearched.clear();
        unaccessableNodes.clear();
        idsVisited.clear();
        moveCount = 0;

        saveStateInfo();
        nodeToGetTo = findFirstNodeWithUnusedNeighbour();
        return nodeToGetTo;
    }

    private long moveBackwardsToFindAlternateRoute() {
        this.isGoingBackwards = true;


        while (reverseNodes.isEmpty()) {
            nodeToGetTo = findFirstNodeWithUnusedNeighbour();
            NodeImpl currentNode;


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
                recursiveFindPathToOrb(nodeToGetTo, currentNode, nodesToVisit, nodesVisited);

            }

        }
        unaccessableNodes.clear();

        if (reverseNodes.size() == 1) {
            isGoingBackwards = false;
        }

        long returnLong = reverseNodes.get(0);

        reverseNodes.remove(0);
        return returnLong;
    }
}
