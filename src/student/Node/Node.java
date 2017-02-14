package student.Node;

/**
 * Created by jakeholdom on 14/02/2017.
 */
public interface Node {

        long getId();

        void setLabel(long id);

        boolean isVisited();

        void setVisited(boolean visited);
}

