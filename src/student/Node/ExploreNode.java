package student.Node;

import game.NodeStatus;
import java.util.Collection;

/**
 * Created by jakeholdom on 14/02/2017.
 * Interface for the nodes used in the explore class
 */
public interface ExploreNode {

  long getLocation();

  void setLocation(long location);

  void setNeighbours(Collection<NodeStatus> neighbours);

  Collection<NodeStatus> getNeighbours();


}

