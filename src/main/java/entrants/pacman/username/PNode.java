
package entrants.pacman.username;


import pacman.game.Game;


public class PNode
{
    Game gameState;
    int index;
    double g_score;
    double h_score;
    PNode parent;
    private boolean explored;
    int depth;

    
    
    // calculates f_score for a path
    public double f_score() 
    {
    	return g_score + h_score;
    }
   
    
    public PNode(Game game, int index, double g_score, double h_score,int depth)
    {
    	this.gameState = game;
        this.index = index;
        this.g_score = g_score;
        this.h_score = h_score;   
        this.depth = depth;
    }
    
   

	/**
     * This method returns whether the node has been visited or not
     */
    public boolean beenExplored()
    {
    	return explored;
    }
    
    /**
     * This method sets the node as visited
     */
    public void setExplored(boolean explored)
    {
    	this.explored = explored;
    }
}
