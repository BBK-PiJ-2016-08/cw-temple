package student.Graph;
import student.Graph.Graph;
import student.Node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakeholdom on 14/02/2017.
 */
public class GraphImpl implements Graph {
    private Node rootNode;
    private final List nodes = new ArrayList();

    @Override
    public Node getRootNode() {
        return this.rootNode;
    }

    @Override
    public void setRootNode(Node node) {

        this.rootNode = node;
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);

    }

    @Override
    public void connectNode(Node start, Node end) {

    }
}
