package student.Graph;

import student.Node.Node;

/**
 * Created by jakeholdom on 14/02/2017.
 */
public interface Graph {
    Node getRootNode();

    void setRootNode(Node node);

    void addNode(Node node);

    //This method will be called to connect two nodes
    void connectNode(Node start, Node end);
}
