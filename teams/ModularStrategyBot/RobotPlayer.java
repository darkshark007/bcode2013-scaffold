package ModularStrategyBot;

import java.util.Random;

import ModularStrategyBot.Orders.WearHat;
import ModularStrategyBot.Orders.I_Orders;
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
	
	public static void run(RobotController rc) throws GameActionException {

		//strategy = new PreProcessRushStrategy(rc);
		//strategy = new RushStrategy(rc);
		//strategy = new ExploreAndExpandStrategy(rc);
		//strategy = new RushAndHealStrategy(rc);
		//strategy = new NukeAndBarricadeStrategy(rc);
		//strategy = new NukeAndSealStrategy(rc);

		
		
		//strategy = new ___RushHealAndExpandStrategy___OLD(rc);		
		strategy = new RushHealAndExpandStrategy(rc);

		
		while (true) {
			if ( rc.isActive() ) {
				strategy.run();

				/*
				if ( rc.getType() == RobotType.SOLDIER ) {
					strategy.goTo(rc.senseEnemyHQLocation(),(new WearHat()));
					System.out.println("RETURN!!!");
				}
				else if ( rc.canMove(Direction.SOUTH) ) rc.spawn(Direction.SOUTH);
				/* */
			}
			rc.yield();
		}
	}
}
