package entrants.pacman.username;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class MyPacman_DFS extends PacmanController{



    public static StarterGhosts ghosts = new StarterGhosts();
    public Constants.MOVE getMove(Game game, long timeDue)
    {
        Random rnd=new Random();
     //setting the moves for nodes ,then put them in a array
        Constants.MOVE[] allMoves = Constants.MOVE.values();

        int highScore = -1;
     //initializing setting of a assigned score  
        
        Constants.MOVE highMove = null;
     // setting a null move,will be updated.

        
        

		
		//node will receive its highest score and best move, and the final move will be decided together. 
        for(Constants.MOVE m: allMoves)
        {
            //System.out.println("Trying Move: " + m);
            Game gameCopy = game.copy();
            Game gameAtM = gameCopy;
            gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
            int tempHighScore = this.dfs_z(new PNode(gameAtM,0,0,0, 0), 7);

            if(highScore < tempHighScore)
            {
                highScore = tempHighScore;
                highMove = m;
            }

            System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);

        }

        System.out.println("High Score: " + highScore + ", High Move:" + highMove);
        //return the best move.
        return highMove;

    }

    
 // traverse by using DFS and return the best score
    public int dfs_z(PNode rootGameState, int maxdepth)
    {
        Constants.MOVE[] allMoves=Constants.MOVE.values();
		// initial given node an array of moves

        //initail an initializing score
        int depth = 0;
        
        //initail a setting score
        int highScore = -1;

        Stack<PNode> stack = new Stack<PNode>();
        stack.push(rootGameState);

        //System.out.println("Adding Node at Depth: " + rootGameState.depth);

        //traverse by DFS and remove Pacman state 
        while(!stack.isEmpty())
        {
            PNode pmnode = stack.pop();
            //System.out.println("Removing Node at Depth: " + pmnode.depth);

			//  dfs traverse tree when face maximum depth
            if(pmnode.depth >= maxdepth)
            {
                int score = pmnode.gameState.getScore();

                //when given a better score, then rewrite it
                if (highScore < score)
                    highScore = score;
            }
            else
            {

                //GET CHILDREN
                for(Constants.MOVE m: allMoves)
                {
                    Game gameCopy = pmnode.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                 
                    PNode node = new PNode(gameCopy,0,0,0, pmnode.depth+1);
             
                    //extract the unvisited nodes and give them moves
                    stack.push(node);
                }
            }

        }

        return highScore;
    }


}