package TestBot;

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
import battlecode.common.Team;

/**
 * The Modular 
 * @author Stephen Bush
 */
public class RobotPlayer {
	
	public static void run(RobotController rc) throws GameActionException {
		
		
		while (true) {
			if ( rc.isActive() ) {
				if ( rc.getType() == RobotType.HQ ) {
					rc.spawn(Direction.EAST);
					System.out.println("Read: "+rc.readBroadcast(0));
					if ( rc.getTeam().equals(Team.A) ) rc.broadcast(0, 10);
					else if ( rc.getTeam().equals(Team.B) ) rc.broadcast(0, 20);
				}
				else if ( rc.getType() == RobotType.SOLDIER ) {
					System.out.println("Read: "+rc.readBroadcast(0));
					if ( rc.canMove(Direction.NORTH_WEST)) rc.move(Direction.NORTH_WEST);
				}
			}
			rc.yield();
		}
	}
}
