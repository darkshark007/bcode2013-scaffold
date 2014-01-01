package ModularStrategyBot.Strategies;

import ModularStrategyBot.Orders.rOrders;
import ModularStrategyBot.Path.Path;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;

public interface I_RobotStrategy {

	public void run();
	
	public void goTo(MapLocation in) throws GameActionException;
	public void goTo(MapLocation in, rOrders r) throws GameActionException;

	public void followPath(Path in) throws GameActionException;
}
