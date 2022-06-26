package it.polito.tdp.PremierLeague.model;

public class Action {
	private Player player;
	private Integer matchID;
	private Integer teamID;
	private Integer timePlayed;
	private Integer totalSuccessfulPassesAll;
	private Integer assists;
	private double efficiency;
	
	public Action(Player player, Integer matchID, Integer teamID, Integer timePlayed, Integer totalSuccessfulPassesAll, Integer assists) {
		super();
		this.player = player;
		this.matchID = matchID;
		this.teamID = teamID;
		this.timePlayed = timePlayed;
		this.totalSuccessfulPassesAll = totalSuccessfulPassesAll;
		this.assists = assists;
		calcolaEfficienza();
	}
	private void calcolaEfficienza() {
		Double efficienza =  ( (double) this.totalSuccessfulPassesAll + this.assists) / this.timePlayed;
		this.efficiency = efficienza;
	}
	
	public double getEfficiency() {
		return efficiency;
	}
	public Player getPlayer() {
		return player;
	}
	public Integer getMatchID() {
		return matchID;
	}
	public Integer getTeamID() {
		return teamID;
	}
	public Integer getTimePlayed() {
		return timePlayed;
	}
	public Integer getTotalSuccessfulPassesAll() {
		return totalSuccessfulPassesAll;
	}
	public Integer getAssists() {
		return assists;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matchID == null) ? 0 : matchID.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (matchID == null) {
			if (other.matchID != null)
				return false;
		} else if (!matchID.equals(other.matchID))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return player.getName() + ": " + efficiency;
	}
	
	
	
	
	
	
	
}
