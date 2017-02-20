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
    private List<Long> nodesToVisit = new ArrayList<>();
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

    private void checkIfStateHasBeenSaved(){
        if (!idsVisited.contains(state.getCurrentLocation())){
            saveStateInfo();
            System.out.println("Save info called");

        }

    }

    private long moveBackwards(){

        long nextMove = nodesToVisit.get(0);
        nodesToVisit.remove(0);

        if (nextMove == this.nodeToGetTo){
            System.out.println("Nodes to visit is empty");
            this.isGoingBackwards = false;

        }
        System.out.println("next move = " + nextMove);

        moveCount = allNodes.size() - 1;
        return nextMove;

    }

    public int getIndexOfCurrentState(){

        int index = 0;
        for (int i = 0; i < allNodes.size(); i++){

            if (allNodes.get(i).getLocation() == state.getCurrentLocation()){

                index = i;

            }
        }
        return index;
    }

    public long getNextMove() {

        checkIfStateHasBeenSaved();

        if (isGoingBackwards){

           return moveBackwards();
        }



        Collection<NodeStatus> nextNodes = allNodes.get(getIndexOfCurrentState())
                .getNeighbours().stream()
                .filter(s -> !idsVisited.contains(s.getId()))
                .collect(Collectors.toList());

        if (nextNodes.isEmpty()) {
            System.out.println("is empty called");


            moveBackwardsToFindAlternateRoute();

            return moveBackwards();

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

        List<NodeStatus> noDuplicatesNeighboursNotVisited = neighboursNotVisited.stream().distinct().collect(Collectors.toList());



       // Collection<NodeStatus> allNeighbours = allNodes.stream().map(NodeImpl::getNeighbours).findFirst().get();



       // long allNeigh = allNeighbours.stream().map(NodeStatus::getId).filter(s -> !idsVisited.contains(s)).findFirst().get();

       // System.out.println("neighbour = " + allNeigh);

        /*
        Collection<NodeStatus> nextNodes = allNodes.stream().map(NodeImpl::getNeighbours)
                .filter(s -> !idsVisited.contains(s.stream().map(NodeStatus::getId).findFirst().get()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        */


        long nextNode = findClosestNumberToTargetFromCollection(noDuplicatesNeighboursNotVisited);

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


        return nextNode;
    }

    private Long findClosestNeighbourFromListOfNeighbours(Collection<NodeStatus> neighboursOfCurrentState, Long nodeID) {


        long nearest = -1;
        long idToMoveTo = 10000000;
        for (NodeStatus n : neighboursOfCurrentState){

            System.out.println("neighbours = " + n.getId());

            if (n.getId() == nodeID){
                saveStateInfo();
                return n.getId();
            }else {

                long d = Math.abs(nodeID - n.getId());
                if (d < idToMoveTo && !nodesToVisit.contains(n.getId())) {

                    idToMoveTo = d;
                    nearest = n.getId();
                }
            }
        }




        return nearest;
    }


    private void moveBackwardsToFindAlternateRoute() {
        this.isGoingBackwards = true;


        this.nodeToGetTo = findFirstNodeWithUnusedNeighbour();

        System.out.println("The id we need to get to is = " + this.nodeToGetTo);

        long currentLocation = state.getCurrentLocation();
        NodeImpl currentNode = allNodes.get(moveCount);


        while (!nodesToVisit.contains(this.nodeToGetTo)) {


            for (NodeImpl n : allNodes){

                if (n.getLocation() == currentLocation){

                    currentNode = n;
                    System.out.println("current node location = " + currentNode.getLocation());

                }
            }
            //currentNode = allNodes.stream().map(s -> s.getMoveCount() == idMoveCount).findFirst().get();
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

    }


}
