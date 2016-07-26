package entrants.pacman.username;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class MyPacmanAB extends PacmanController{


    private class GameNode{
        Game game;

        public GameNode(Game game){
            this.game = game;
        }

        public GameNode makeMove(MOVE m){
            Game gameCopy = game.copy();
            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
            GameNode nextGameNode = new GameNode(gameCopy);
            return nextGameNode;
        }

        public int getPacmanNode(){
            return game.getPacmanCurrentNodeIndex();
        }

        public MOVE[] allPossiblemoves() {

            //return game.getPossibleMoves(getPacmanNode());
            return game.getPossibleMoves(getPacmanNode(),game.getPacmanLastMoveMade());


        }


    }


    public static StarterGhosts ghosts = new StarterGhosts();

    public MOVE getMove(Game game, long timeDue)
    {
        GameNode currentGame = new GameNode(game);
        MOVE[] allMoves= currentGame.allPossiblemoves();



        int highScore = -1;
        MOVE highMove = null;


        for(MOVE m: allMoves)
        {
            int tempHighScore = this.min_value(currentGame.makeMove(m), Integer.MIN_VALUE, Integer.MAX_VALUE, 20);

            if(highScore < tempHighScore)
            {
                highScore = tempHighScore;
                highMove = m;
            }

            System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
            System.out.printf("%d, " ,highScore);
        }

        System.out.println("High Score: " + highScore + ", High Move:" + highMove);
        return highMove;

    }

    /* AlphaBeta Searching */
    public int max_value(GameNode gameNode, int a, int b, int termination){
        if (termination == 0){
            int current = gameNode.game.getPacmanCurrentNodeIndex();
            int v = Integer.MAX_VALUE;
            for(Constants.GHOST ghost : Constants.GHOST.values()){
                int new_v = gameNode.game.getShortestPathDistance(current,gameNode.game.getGhostCurrentNodeIndex(ghost));
                v = (v < new_v) ? v : new_v;
            }
            return v;
        }
        int v = Integer.MIN_VALUE;
        for (MOVE m : gameNode.allPossiblemoves()){
            int new_v = min_value(gameNode.makeMove(m), a, b, termination - 1);
            v = (v > new_v) ? v : new_v;
            if (v >= b){
                return v;
            }
            a = (a > v) ? a : v;
        }
        return v;
    }

    public int min_value(GameNode gameNode, int a, int b, int termination){
        if (termination == 0){
            int current = gameNode.game.getPacmanCurrentNodeIndex();
            int v = Integer.MAX_VALUE;
            for(Constants.GHOST ghost : Constants.GHOST.values()){
                int new_v = gameNode.game.getShortestPathDistance(current,gameNode.game.getGhostCurrentNodeIndex(ghost));
                v = (v < new_v) ? v : new_v;
            }
            return v;
        }
        int v = Integer.MAX_VALUE;
        for (MOVE m : gameNode.allPossiblemoves()){
            int new_v = max_value(gameNode.makeMove(m), a, b, termination - 1);
            v = (v < new_v) ? v : new_v;
            if (v <= a){
                return v;
            }
            b = (b < v) ? b : v;
        }
        return v;
    }

}