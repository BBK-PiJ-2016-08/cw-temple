package student.Node;

/**
 * Created by jakeholdom on 14/02/2017.
 */
public interface Node {

        long getMoveCount();

        void setMoveCount(int moveCount);

        boolean isVisited();

        void setVisited(boolean visited);

        int getDistanceFromOrb();
}

