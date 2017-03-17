package student.Node;

import game.*;
import java.util.Collection;


/**
 * Created by jakeholdom on 14/02/2017.
 */
public class NodeImpl implements Node {

    private Integer moveCount;
    private boolean visited;
    private Collection<NodeStatus> neighbours;
    private long currentLocation;
    private int distanceFromOrb;


    public NodeImpl(final int tileID) {

        this.moveCount = moveCount;
    }

    public void setDistanceFromOrb(int distance){

        this.distanceFromOrb = distance;
    }
    @Override
    public int getDistanceFromOrb(){

        return this.distanceFromOrb;
    }

    @Override
    public long getMoveCount() {
        return this.moveCount;
    }

    @Override
    public void setMoveCount(int moveCount) {


        this.moveCount = moveCount;
    }

    public long getLocation() {
        return this.currentLocation;
    }

    public void setLocation(long location) {

        this.currentLocation = location;

    }
    public void setNeighbours(Collection<NodeStatus> neighbours) {

        this.neighbours = neighbours;
    }

    public Collection<NodeStatus> getNeighbours() {

        return this.neighbours;
    }
    @Override
    public boolean isVisited() {
        return this.visited;
    }

    @Override
    public void setVisited(boolean visited) {
        this.visited = visited;

    }
}
