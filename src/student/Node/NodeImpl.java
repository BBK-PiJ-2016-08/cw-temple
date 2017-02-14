package student.Node;

import game.*;
import student.AStarAlgorithm.AStar;
import student.Node.Node;


/**
 * Created by jakeholdom on 14/02/2017.
 */
public class NodeImpl implements Node {

    private long id;
    private boolean visited;
    public AStar aStar = new AStar();

    public NodeImpl(final long id) {
        this.setLabel(id);
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setLabel(long id) {

        this.id = id;
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
