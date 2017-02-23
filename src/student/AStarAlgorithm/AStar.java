package student.AStarAlgorithm;

import game.*;

import student.Node.Node;
import student.Node.NodeImpl;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jakeholdom on 14/02/2017.
 */
public class AStar {

    private List<NodeImpl> allNodes = new ArrayList<>();
    private List<Long> idsVisited = new ArrayList<>();
    private List<Long> reverseNodes = new ArrayList<>();
    private List<Long> reverseNodesImpl = new ArrayList<>();
    private int getLocation = 1;

    private boolean isGoingBackwards = false;
    private long nodeToGetTo = 0;


    private int moveCount = 0;

    private ExplorationState state;


    public AStar(ExplorationState state) {

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
/*
    private long moveBackwards() {

        long nextMove = nodesToVisit.get(0);
        nodesToVisit.remove(0);

        if (nextMove == this.nodeToGetTo) {
            System.out.println("Nodes to visit is empty");
            this.isGoingBackwards = false;

        }
        System.out.println("next move = " + nextMove);

        moveCount = allNodes.size() - 1;
        return nextMove;

    }
    */

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
        if (reverseNodes.size() == 0) {
            isGoingBackwards = false;
        }

        if (isGoingBackwards) {


            getLocation = 1;
            reverseNodesImpl.clear();
            long returnLong = reverseNodes.get(0);
            reverseNodes.remove(0);
            return returnLong;
        }


        Collection<NodeStatus> nextNodes = allNodes.get(getIndexOfLocation(state.getCurrentLocation()))
                .getNeighbours().stream()
                .filter(s -> !idsVisited.contains(s.getId()))
                .collect(Collectors.toList());

        if (nextNodes.isEmpty()) {
            System.out.println("is empty called");


            //moveBackwardsToFindAlternateRoute();

            return moveBackwardsToFindAlternateRoute();

        }

        moveCount++;
        return nextNodes.stream()
                .min(Comparator.comparing(NodeStatus::getDistanceToTarget))
                .get()
                .getId();


        //long nextMoveLong = nextNodes.stream().mapToLong(s -> allNodes.stream().mapToLong(n -> s == n.getId()));


/*

        if (nextNodes.isEmpty()){
            System.out.println("is empty called");

            return moveBackwardsToFindAlternateRoute();

        }

*/
        /*long nextMove = nextNodes.stream()
                .min(Comparator.comparing(NodeStatus::getDistanceToTarget))
                .get()
                .getId();

*/
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

        for (int i = 0; i < neighbours.size(); i++) {

            if (!idsVisited.contains(neighbours.get(i).getId())) {
                neighboursNotVisited.add(neighbours.get(i));

            }
        }

        neighboursNotVisited = neighboursNotVisited.stream().distinct().collect(Collectors.toList());


        // Collection<NodeStatus> allNeighbours = allNodes.stream().map(NodeImpl::getNeighbours).findFirst().get();


        // long allNeigh = allNeighbours.stream().map(NodeStatus::getId).filter(s -> !idsVisited.contains(s)).findFirst().get();

        // System.out.println("neighbour = " + allNeigh);

        /*
        Collection<NodeStatus> nextNodes = allNodes.stream().map(NodeImpl::getNeighbours)
                .filter(s -> !idsVisited.contains(s.stream().map(NodeStatus::getId).findFirst().get()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        */



        /*List<NodeImpl> firstNeighbour = allNodes.stream().map(NodeImpl::getNeighbours)
                .filter(s -> !idsVisited.equals(s.stream().map(NodeStatus::getId)));
        */
        /*
        Collection<NodeStatus> firstNeighbour = allNodes.stream().map(NodeImpl::getNeighbours)
                .filter(s -> !idsVisited.equals(NodeStatus::getId));
                */


/*
        long idToGet = firstNeighbour.stream().min(Comparator.comparing(NodeStatus::getDistanceToTarget)).get().getId();
        for (NodeStatus n : firstNeighbour){



            idToGet = n.getId();

            System.out.println("id we need to get to is = " + n.getId());

        }
*/


        return findClosestNumberToTargetFromCollection(neighboursNotVisited);
    }

    private Long findClosestNeighbourFromListOfNeighbours(Collection<NodeStatus> neighboursOfCurrentState,
                                                          Long nodeID, List<Long> nodesVisited) {


        long nearest = -1;
        long idToMoveTo = 10000000;
        for (NodeStatus n : neighboursOfCurrentState) {

            // System.out.println("neighbours = " + n.getId());

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

    /*
        private NodeImpl removeDuplicateStatesAndFindAlternateRoute(long l) {

            for (int i = nodesToVisit.size() - 1; i > 0; i--) {

                int j = getIndexOfLocation(nodesToVisit.get(i));
                List<Long> nodesWhichHasNeighbourInNodesToVisit = new ArrayList<>();

                for (NodeStatus n : allNodes.get(j).getNeighbours()) {

                    if (!nodesToVisit.contains(n.getId())) {
                        nodesWhichHasNeighbourInNodesToVisit.add(n.getId());
                        System.out.println("added = " + n.getId());

                    }

                }

                if (nodesWhichHasNeighbourInNodesToVisit.size() > 1) {
                    System.out.println("removed = " + nodesToVisit.get(j));

                    nodesToVisit.remove(j);

                    return allNodes.get(j);


                }


            }
            System.out.println("remove duplicates and find alternate route called");


            return null;
        }
        */
/*
    private NodeImpl getVisitedNode(NodeImpl node) {
        for (NodeStatus n : node.getNeighbours()) {

            if (idsVisited.contains(n.getId())) {

                return n;

            }

        }
        return null;
    }
*/
    private void recursiveFindPathToOrb(long destination, NodeImpl currentLocation, List<Long> nodesToVisit, List<Long> nodesVisited) {

        long numberToFind = findClosestNeighbourFromListOfNeighbours(currentLocation.getNeighbours(), destination, reverseNodesImpl);

        List<Long> checkNull = new ArrayList<>();

        if (numberToFind > 0) {
            for (NodeStatus n : currentLocation.getNeighbours()) {


                if (n.getId() == destination) {
                    System.out.println("Found Destination");
                    reverseNodesImpl.add(currentLocation.getLocation());
                    nodesVisited.add(n.getId());
                    nodesToVisit.add(n.getId());
                    this.reverseNodes = nodesToVisit;
                    return;
                }

                if (n.getId() == numberToFind && !nodesVisited.contains(n.getId())) {
                    checkNull.add(n.getId());
                    reverseNodesImpl.add(currentLocation.getLocation());
                    currentLocation = allNodes.get(getIndexOfLocation(n.getId()));
                    nodesVisited.add(n.getId());
                    nodesToVisit.add(n.getId());
                    recursiveFindPathToOrb(destination, currentLocation, nodesToVisit, nodesVisited);
                }
            }
        }



        if (checkNull.isEmpty()) {
            currentLocation = allNodes.get(getIndexOfLocation(reverseNodesImpl.get(reverseNodesImpl.size() - getLocation)));
            getLocation++;

            //currentLocation = allNodes.get(getIndexOfLocation(nodesToVisit.get(nodesVisited.size() - 1)));

            nodesToVisit.remove(nodesToVisit.size() - 1);


            recursiveFindPathToOrb(destination, currentLocation, nodesToVisit, nodesVisited);

        }
    }

    private long moveBackwardsToFindAlternateRoute() {
        this.isGoingBackwards = true;


        nodeToGetTo = findFirstNodeWithUnusedNeighbour();


        NodeImpl currentNode = allNodes.get(getIndexOfLocation(state.getCurrentLocation()));


        List<Long> nodesToVisit = new ArrayList<>();
        List<Long> nodesVisited = new ArrayList<>();
        recursiveFindPathToOrb(nodeToGetTo, currentNode, nodesToVisit, nodesVisited);


        if (reverseNodes.size() == 0) {
            isGoingBackwards = false;
        }
        long returnLong = reverseNodes.get(0);
        reverseNodes.remove(0);
        return returnLong;
        /*

        while (!nodesToVisit.contains(this.nodeToGetTo)) {


            for (NodeImpl n : allNodes){

                if (n.getLocation() == currentLocation){

                    currentNode = n;
                    System.out.println("current node location = " + currentNode.getLocation());

                }
            }
            //currentNode = allNodes.stream().map(s -> s.getMoveCount() == idMoveCount).findFirst().get();



            if (nodesToVisit.contains(currentLocation)){


                 currentNode = removeDuplicateStatesAndFindAlternateRoute(currentNode.getLocation());

            }

            currentLocation = currentNode.getLocation();

            //List<Long> neighboursOfCurrentState = state.getNeighbours().stream().map(s -> s.getId()).collect(Collectors.toList());

            long nextId = findClosestNeighbourFromListOfNeighbours(currentNode.getNeighbours(), this.nodeToGetTo);

            for (NodeImpl n : allNodes){

                if (n.getLocation() == nextId){
                    currentLocation = n.getLocation();

                }
            }
            nodesToVisit.add(nextId);

        }
        System.out.println("NODES WE NEED TO GO TO");

        for (long l : nodesToVisit){
            System.out.println(l);

        }
        */


    }

}
