package student.Node;

import game.*;
import java.util.Collection;


/**
 * Created by jakeholdom on 14/02/2017.
 * Implementation of Explore Node
 */
public class ExploreNodeImpl implements ExploreNode {

  private Collection<NodeStatus> neighbours; //Nodes neighbours
  private long currentLocation; //Nodes location


  /**
   * Getter: gets location
   *
   * @return location
   */
  @Override
  public long getLocation() {
    return this.currentLocation;
  }

  /**
   * Setter: sets Location
   *
   * @param location long of state's location
   */
  @Override
  public void setLocation(long location) {

    this.currentLocation = location;

  }

  /**
   * Getter: gets Neighbours
   *
   * @return collection of Nodestatus/Neighbours
   */
  @Override
  public Collection<NodeStatus> getNeighbours() {

    return this.neighbours;
  }

  /**
   * Setter: sets Neighbours
   *
   * @param neighbours collection of Nodestatus/Neighbours
   */
  @Override
  public void setNeighbours(Collection<NodeStatus> neighbours) {

    this.neighbours = neighbours;
  }



}
