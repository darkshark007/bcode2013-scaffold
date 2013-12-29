package ModularStrategyBot;

import java.util.Random;

import ModularStrategyBot.Strategies.*;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

/** The example funcs player is a player meant to demonstrate basic usage of the most common commands.
 * Robots will move around randomly, occasionally mining and writing useless messages.
 * The HQ will spawn soldiers continuously. 
 */
public class RobotPlayer {
	
	static I_RobotStrategy strategy;
	//static I_RobotStrategy strategy = PreProcessRushStrategy.getInstance();
	//static I_RobotStrategy strategy = new NukeAndBarricadeStrategy();
	//static I_RobotStrategy strategy = new NukeAndSealStrategy();
	
	public static void run(RobotController rc) throws GameActionException {

		//strategy = new PreProcessRushStrategy(rc);
		//strategy = new RushStrategy(rc);
		//strategy = new ExploreAndExpandStrategy(rc);
		//strategy = new RushAndHealStrategy(rc);
		strategy = new RushHealAndExpandStrategy(rc);
		//strategy = new NukeAndBarricadeStrategy(rc);
		//strategy = new NukeAndSealStrategy(rc);

		
		while (true) {
			if ( rc.isActive() ) {
				strategy.run();

				/*
				if ( rc.getType() == RobotType.SOLDIER ) {
					//strategy.goTo(rc.senseEnemyHQLocation());
					strategy.goTo(new MapLocation(49,8));
					System.out.println("RETURN!!!");
				}
				else if ( rc.canMove(Direction.SOUTH) ) rc.spawn(Direction.SOUTH);
				/* */
			}
			rc.yield();
		}
	}
}
