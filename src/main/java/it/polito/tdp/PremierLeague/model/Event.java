package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event> {
	
	public enum EventType{
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	private int time; //numero azione
	private EventType type;
	public Event(int time, EventType type) {
		super();
		this.time = time;
		this.type = type;
	}
	public int getTime() {
		return time;
	}
	public EventType getType() {
		return type;
	}
	@Override
	public int compareTo(Event o) {
		return this.time - o.getTime();
	}
	
	
	

}
