package student.Node;

import game.*;
import student.AStarAlgorithm.AStar;
import student.Node.Node;

import java.util.Collection;


/**
 * Created by jakeholdom on 14/02/2017.
 */
public class NodeImpl implements Node {

    public long id;
    private boolean visited;
    private Collection<NodeStatus> neighbours;

    public NodeImpl(final long id) {
        this.setId(id);
    }

    public NodeImpl(){

    }


    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long id) {


        this.id = id;
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
