package student;

import game.*;
import java.util.List;
import student.ExploreAlgorithm.Explore;

import student.Node.EscapeNodeListManager;


public class Explorer {


  /**
   * Explore the cavern, trying to find the orb in as few steps as possible.
   * Once you find the orb, you must return from the function in order to pick
   * it up. If you continue to move after finding the orb rather
   * than returning, it will not count.
   * If you return from this function while not standing on top of the orb,
   * it will count as a failure.
   * <p>There is no limit to how many steps you can take, but you will receive
   * a score bonus multiplier for finding the orb in fewer steps.
   * <p>At every step, you only know your current tile's ID and the ID of all
   * open neighbor tiles, as well as the distance to the orb at each of these tiles
   * (ignoring walls and obstacles).
   * <p>To get information about the current state, use functions
   * getCurrentLocation(),
   * getNeighbours(), and
   * getDistanceToTarget()
   * in ExplorationState.
   * You know you are standing on the orb when getDistanceToTarget() is 0.
   * <p>Use function moveTo(long id) in ExplorationState to move to a neighboring
   * tile by its ID. Doing this will change state to reflect your new position.
   * <p>A suggested first implementation that will always find the orb, but likely won't
   * receive a large bonus multiplier, is a depth-first search.
   *
   * @param state the information available at the current state
   */
  public void explore(ExplorationState state) {

    Explore explore = new Explore(state);

    while (state.getDistanceToTarget() != 0) { //while character is not on the target tile

      state.moveTo(explore.getNextMove()); //get next move


    }


  }


  /**
   * Escape from the cavern before the ceiling collapses, trying to collect as much
   * gold as possible along the way. Your solution must ALWAYS escape before time runs
   * out, and this should be prioritized above collecting gold.
   * <p>
   * You now have access to the entire underlying graph, which can be accessed through EscapeState.
   * getCurrentNode() and getExit() will return you ExploreNode objects of interest, and getVertices()
   * will return a collection of all nodes on the graph.
   * <p>
   * Note that time is measured entirely in the number of steps taken, and for each step
   * the time remaining is decremented by the weight of the edge taken. You can use
   * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
   * on your current tile (this will fail if no such gold exists), and moveTo() to move
   * to a destination node adjacent to your current node.
   * <p>
   * You must return from this function while standing at the exit. Failing to do so before time
   * runs out or returning from the wrong location will be considered a failed run.
   * <p>
   * You will always have enough time to escape using the shortest path from the starting
   * position to the exit, although this will not collect much gold.
   *
   * @param state the information available at the current state
   */

  public void escape(EscapeState state) {

    checkGold(state); //checks gold on first tile

    while (!state.getCurrentNode().equals(state.getExit())) { //While state isn't on the exit

      EscapeNodeListManager listManager = new EscapeNodeListManager(state);

      List<Node> listOfMoves = listManager.getListOfMoves();

      boolean headingBack = false;
      while (!listOfMoves.isEmpty()) {
        checkGold(state); //Check for any gold
        if (!headingBack) { //If character isn't heading backwards get next list of moves
          List<Node> movesToEnd = listManager.checkMovesToEnd(state);
          if (movesToEnd != null) { //Character is starting to head to the exit
            listOfMoves = movesToEnd; //list of moves set to moves heading to the exit
            headingBack = true; //boolean set to true so character heads back

          } else {
            for (Node n : state.getCurrentNode().getNeighbours()) {
              if (n.getTile().getGold() > 100) { //If neighbour has tile with more than 100 gold
                listOfMoves.clear();
                listOfMoves.add(n); //Go to the neighbour node and restart search
                break;
              }

            }
          }
        }

        state.moveTo(listOfMoves.get(0)); //gets the next move according to the list
        listOfMoves.remove(0); //removes that move from the list

      }


    }

  }

  /**
   * Checks whether the current tile that the character is on contains any gold.
   *
   * @param state current state of the character
   */
  private void checkGold(EscapeState state) {
    if (state.getCurrentNode().getTile().getGold() > 0) { //If tile has gold
      state.getCurrentNode().getTile().getGold();
      state.pickUpGold(); //pick up gold
    }
  }
}
