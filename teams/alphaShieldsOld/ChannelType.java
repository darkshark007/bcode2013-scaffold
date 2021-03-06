package alphaShieldsOld;

import battlecode.common.GameConstants;

public enum ChannelType {

	// broadcast shields location
	SHIELDS,
	
	// for shields to ping back to the HQ
	SHIELDS_PING,
	
	// channel for reporting artillery sighting
	ARTILLERY_SEEN,
	
	// for strategy
	STRATEGY,
	
	// for encampments
	ENC1,
	ENC2,
	ENC3,
	ENC4,
	ENC5,
	
	// completion channels
	COMP1,
	COMP2,
	COMP3,
	COMP4,
	COMP5,
	
	// broadcasting waypoints for clearing wide swath for army
	WAYPOINTS,
	
	// power level
	HQPOWERLEVEL,
	
	// checking if enemy nuke is half done
	ENEMY_NUKE_HALF_DONE,
	
	// check if our nuke is half done
	OUR_NUKE_HALF_DONE;
	
	public static final int size = ChannelType.values().length;
	public static final int range = GameConstants.BROADCAST_MAX_CHANNELS / size;
}