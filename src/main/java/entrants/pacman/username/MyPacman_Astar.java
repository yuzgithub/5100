package entrants.pacman.username;

import java.util.List;
import java.util.ArrayList;
import pacman.game.Game;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

/**
 * This class implements the Pathfinding Algorithm A Star
 * 
 */

public class MyPacman_Astar extends PacmanController{

	
	public int startNodeIndex;
	public int targetNodeIndex;
	public static StarterGhosts ghosts = new StarterGhosts();
	
	public MOVE getMove(Game game,long timeDue){
		
		startNodeIndex = game.getPacmanCurrentNodeIndex();
		
		PNode startNode = new PNode(game.copy(), startNodeIndex, 0, game.getShortestPathDistance(startNodeIndex, targetNodeIndex),0);
		
		//get all active pills
		int[] activePills=game.getActivePillsIndices();
		
		//get all active power pills
		int[] activePowerPills=game.getActivePowerPillsIndices();
		
		//create a target array that includes all ACTIVE pills and power pills
		int[] targetNodeIndices=new int[activePills.length+activePowerPills.length];
		
		for(int i=0;i<activePills.length;i++)
			targetNodeIndices[i]=activePills[i];
		
		for(int i=0;i<activePowerPills.length;i++)
			targetNodeIndices[activePills.length+i]=activePowerPills[i];
		
		targetNodeIndex = game.getClosestNodeIndexFromNodeIndex(startNodeIndex,targetNodeIndices,DM.PATH);
		
		PNode targetNode = new PNode(game.copy(), targetNodeIndex, game.getShortestPathDistance(startNodeIndex, targetNodeIndex), 0,0);
		
		return zastar(startNode, targetNode);
	}
	

	/** This method is for the implementation of A Star 
	 * Algorithm. The aim is to find the path(branch) that with 
	 * pills and powerpills as the heuristics.
	 * 
	 * Here, the g_score is the distance between root-node and 
	 * the current node. The h_score is the distance between the current-node 
	 * and the goal-state (which in this case is the position of the pills/powerpills.
	 * 
	 * @param game class containing the game engine and all methods required to
	 * @param startNode root-node of the A Star Pathfinding
	 * @param targetNode goal-state of the game
	 * @return MOVE moves of the optimal path from startNode to targetNode
	 */
	public MOVE zastar(PNode startNode, PNode targetNode)
	{
		// Creating OPENLIST for storing all the PacMan neighbor nodes
        List<PNode> openList = new ArrayList<PNode>();
        // Add the start-node to OPENLIST and set it as explored
        openList.add(startNode);
        startNode.setExplored(true);
	   

        while(!openList.isEmpty())
        {
            PNode pmnode = openList.get(0);
            Boolean flag = true;
            
            for (int i = 1; i < openList.size(); i++)
            {
            	PNode first = openList.get(1);
            	for(int j = 2; j < openList.size(); j++){
            		if(openList.get(j).f_score() != first.f_score())
            			flag = false;
            	}
            	
            	if(flag){
            		pmnode = openList.get(1);
            	}
            	else if(openList.get(i).f_score() < pmnode.f_score() || openList.get(i).f_score() == pmnode.f_score() 
            			&& openList.get(i).h_score == pmnode.h_score)
            		pmnode = openList.get(i);
            }
            
            openList.remove(pmnode);
            pmnode.setExplored(true);
            
            // if current-node is same as target-node
            if(pmnode == targetNode)
            {
                 retracePath(startNode, targetNode, pmnode.gameState);
            }
            
            else
            {
            	//GET NEIGHBOR NODES
            	for(MOVE m: pmnode.gameState.getPossibleMoves(pmnode.index))
            	{
            		Game gameCopy = pmnode.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                    int nodeIndex = gameCopy.getPacmanCurrentNodeIndex();
                    PNode node = new PNode(gameCopy, nodeIndex, gameCopy.getDistance(startNodeIndex, nodeIndex, DM.PATH), gameCopy.getDistance(nodeIndex, targetNodeIndex, DM.PATH),0);
                    
                    //if neighbor-node has been evaluated and the 
                    //newer fscore is higher, skip 
                    if(node.beenExplored())
                    {
                    	continue;
                    }
                    
                    // calculate g_score for the path from current to next node
                    double temp_g_score = pmnode.g_score + gameCopy.getDistance(pmnode.index, nodeIndex, DM.PATH);
                    
                    // if new path to neighbor is shorter or neighbor is not in open-list
                    if (temp_g_score < node.g_score || !openList.contains(node))
                    {
                    	// set neighbor-node's g_score, h_score and parent
                    	node.g_score = temp_g_score;
                    	node.h_score = gameCopy.getDistance(nodeIndex, targetNodeIndex, DM.PATH);
                    	node.parent = pmnode;
                    	
                    	// if neighbor-node not in open-list, add it
                    	if (!openList.contains(node))
                    		openList.add(node);
                    }
                    
                    
            	 }	
            		
             }
             	
          }
        
         return MOVE.DOWN;   
	}
    
	// retrace the entire path to get the next MOVE once the target-node is reached
	private MOVE retracePath(PNode start, PNode end, Game game) {
		
		List<PNode> path = new ArrayList<PNode>();
    	PNode currentNode = end;
    	
    	
    	while (currentNode != start)
    	{
    		path.add(currentNode);
    		currentNode = currentNode.parent;
    	}
    	
    	//PacManNode a = path.get(0); 
      	//PacManNode b = path.get(1);
    	
    
    	return game.getMoveToMakeToReachDirectNeighbour(start.index, currentNode.index);
	}
}
