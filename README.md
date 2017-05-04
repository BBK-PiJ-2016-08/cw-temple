# Coursework 4 - PiJ 2016/2017 - cw-temple - Jake Holdom

## Overview

This project used a pre made game called 'Phillip Hammond and the temple of gloom' with the objective of moving a character around a map to reach certain tiles. There are two different rounds to the game these are Escape and Explore.

## Explore

The explore game provides no access to the map information other than which neighbour nodes are closer to the orb which the character has to reach. 
The algorithm that completes this works by seeing which neighbouring node of the current node is closest to the orb until it reaches the destination. Each node the character steps on is saved to a list as well as the neighbours of that node and the characters next move will always be a new node that the character has not yet stepped on before. If the character runs out of possible moves from its current location then the the algorithm goes back through the list of nodes for all neighbours that haven't been checked and filters out the one which is closest to the orb. If the character is also consistently moving further away from the orb then the next closest unvisited neighbour is moved to. From testing the average bonus multiplier after each game around 1.2.

### Problems

In rare cases there have been no more possible moves for the character to go to. In order to fix this all stored nodes that the character has already visited is cleared and the algorithm starts again from its current location which fixes the issue.

## Escape

The Escape game gives you access to the whole graph of nodes inside the map. There are a number of different algorithm's that could have been implemented for this specific game, however this implementation takes advantage of the Dijkstra algorithm to find the shortest path to a given node which works by setting a start node and processing each neighbour from that node and saving the path with the smallest weighting but also checking if other paths have a smaller weighting throughout and then carrying on this process until the destination node is reached. To know which node to go to next an algorithm is implemented that gathers the data of how many coins are on each tile and finds the paths of the nodes with the top 5 amounts of gold on them. These lists are then checked to see which one is closest to the current destination of the character and then that path is chosen. 
    After each move the distance it will take to reach the exit is checked against the current amount of time left before the game ends. If this distance is too far then the character will head back to the exit in enough time. 
    
### Problems

Another algorithm which could have been implemented is the AStar algorithm that works by checking every possible route from the characters current state to the exit. Then checking the paths of each route to find each path that fits inside the time restraints and which route collects the most amount of gold. However, this algorithm would have been very processor intensive and I wasn't sure whether the the time taken to complete the game because of the computing time would have been ok.


## Classes

#### Explore.java ##### - Returns the Long value for the Explorer to move the character to next
