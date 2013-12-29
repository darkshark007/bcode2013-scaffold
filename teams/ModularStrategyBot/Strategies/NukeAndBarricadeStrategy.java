package ModularStrategyBot.Strategies;

import ModularStrategyBot.Path.Path;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Upgrade;

public class NukeAndBarricadeStrategy extends Strategy {

	public NukeAndBarricadeStrategy(RobotController in) { super(in); }

	public void run() {
		try {
			Direction dir;
			switch (rc.getType()) {
			case HQ:
				// Spawn direction
				dir = Direction.NORTH;
				while ( true ) { 
					if ( rc.isActive() ) {
						if ( rc.canMove(dir) ) rc.spawn(dir);
						else {
							while (true) {
								rc.researchUpgrade(Upgrade.NUKE);
								rc.yield();
							}
						}
					} 
					rc.yield();
				}
			
			case SOLDIER:
				Path patrolPath = getPatrolPath(rc.senseHQLocation());
				int path = 0;
				int timeSinceMove = 0;
				MapLocation nextLink = patrolPath.getLink(path++);;
				while ( true ) {
					if ( rc.isActive() ) {
						if ( nextLink != null ) {
							dir = rc.getLocation().directionTo(nextLink);
							MapLocation m = rc.getLocation().add(dir);
							if ( rc.senseMine(m) != rc.getTeam() && rc.senseMine(m) != null ) {
								rc.defuseMine(m);
								rc.yield();  continue;
							}
							if ( !rc.canMove(dir) ) {
								if ( rc.senseMine(rc.getLocation()) != rc.getTeam()) {
									rc.layMine();
								}
								timeSinceMove++;
								if ( timeSinceMove > 30 ) {
									if ( rc.senseEncampmentSquare(rc.getLocation()) ) {
										rc.captureEncampment(RobotType.SHIELDS);
									}
									else {
										while (true) rc.yield();
									}
								}
								rc.yield();  continue;
							}
							timeSinceMove = 0;
							rc.move(dir);
							nextLink = patrolPath.getLink(path++);
						}
						else {
							if ( rc.senseMine(rc.getLocation()) != rc.getTeam()) {
								rc.layMine();
							}
							while (true) rc.yield();
						}
					}
					rc.yield();
				}
			
			}
		} catch (Exception e) {
			rc.breakpoint();
			e.printStackTrace();
		}		
	}
	
	public Path getPatrolPath(MapLocation center) {
		Path p = new Path();
		p.addLinkE(new MapLocation(center.x-1,center.y-1));
		p.addLinkE(new MapLocation(center.x-1,center.y));
		p.addLinkE(new MapLocation(center.x-1,center.y+1));
		p.addLinkE(new MapLocation(center.x,center.y+1));
		p.addLinkE(new MapLocation(center.x+1,center.y+1));
		p.addLinkE(new MapLocation(center.x+1,center.y));
		p.addLinkE(new MapLocation(center.x+1,center.y-1));
		p.addLinkE(new MapLocation(center.x,center.y-2));
		p.addLinkE(new MapLocation(center.x-1,center.y-2));
		p.addLinkE(new MapLocation(center.x-2,center.y-2));
		p.addLinkE(new MapLocation(center.x-2,center.y-1));
		p.addLinkE(new MapLocation(center.x-2,center.y));
		p.addLinkE(new MapLocation(center.x-2,center.y+1));
		p.addLinkE(new MapLocation(center.x-2,center.y+2));
		p.addLinkE(new MapLocation(center.x-1,center.y+2));
		p.addLinkE(new MapLocation(center.x,center.y+2));
		p.addLinkE(new MapLocation(center.x+1,center.y+2));
		p.addLinkE(new MapLocation(center.x+2,center.y+2));
		p.addLinkE(new MapLocation(center.x+2,center.y+1));
		p.addLinkE(new MapLocation(center.x+2,center.y));
		p.addLinkE(new MapLocation(center.x+2,center.y-1));
		p.addLinkE(new MapLocation(center.x+2,center.y-2));
		p.addLinkE(new MapLocation(center.x+1,center.y-2));
		return p;
	}

}
