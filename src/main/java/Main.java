

import entrants.pacman.username.MyPacman_Astar;
import entrants.pacman.username.MyPacmanAB;
import entrants.pacman.username.MyPacman_DFS;
import examples.commGhosts.POCommGhosts;
import pacman.Executor;


/**
 * Created by pwillic on 06/05/2016.
 */
// DFS is slightly much better in my case.
//DFS: time complexity: O(n+e) 
//     space complexity:O(|n|)

//ASTAR: time complexity: O(log(heapSize)) 
//      space complexity£ºO(|n|+|e|)


public class Main {

    public static void main(String[] args) {

    	Executor executor = new Executor(false, true);
//        Executor executor = new Executor(true, true);
    	 
    	executor.runGameTimed(new MyPacmanAB(), new POCommGhosts(50), true);   	
//      executor.runGameTimed(new MyPacman_Astar(), new POCommGhosts(50), true);
//    	executor.runGameTimed(new MyPacman_DFS(), new POCommGhosts(50), true);
    }
}
