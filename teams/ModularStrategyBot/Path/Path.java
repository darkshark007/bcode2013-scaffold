package ModularStrategyBot.Path;

import java.util.LinkedList;

import battlecode.common.MapLocation;

public class Path {
	
	private LinkedList<MapLocation> p = new LinkedList<MapLocation>();
	private int iter = 0; 

	public void addLink(MapLocation in) {
		p.addFirst(in);
	}
	public void addLinkE(MapLocation in) {
		p.addLast(in);
	}
	
	public void addPathE(Path in) {
		for ( MapLocation n : in.p ) {
			addLinkE(n);
		}
	}
	
	public MapLocation nextLink() {
		if ( iter < p.size() ) return p.get(iter++);
		else return null;
	}
	
	public MapLocation getLink(int l) {
		if ( l >= p.size() ) return null;
		return p.get(l);
	}

	public MapLocation removeLastLink() {
		return p.removeLast();
	}
	
	

}
