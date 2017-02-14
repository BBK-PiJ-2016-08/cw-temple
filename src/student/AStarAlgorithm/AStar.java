package student.AStarAlgorithm;

import game.*;

import student.Node.Node;
import student.Node.NodeImpl;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by jakeholdom on 14/02/2017.
 */
public class AStar {

    public List<NodeImpl> allNodes = new ArrayList<>();
    public List ids = new ArrayList<>();
    private int moveCount = 0;

    public long getNextMove(ExplorationState state){

        System.out.println("nodes list size = " + allNodes.size());
        List<NodeStatus> nodes = new ArrayList<>();

        for (NodeStatus n : allNodes.get(moveCount).getNeighbours()){

            if (!ids.contains(n.getId())){
                nodes.add(n);

            }

        }

        System.out.println(" nodes size = " + nodes.size());

        long n = nodes.stream()
                .min(Comparator.comparing(NodeStatus::getDistanceToTarget)).get().getId();

        moveCount++;


        return n;
    }






}
