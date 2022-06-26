package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private Graph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private List<Match> listaMatch;
	private Map<Integer, Player> playerMap;
	private double bestEff;
	private Simulator sim;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(Match match) {
		
		this.grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.playerMap = new HashMap<Integer, Player>();
		List<Player> listaPlayer = new ArrayList<>(this.dao.listPlayersMatch(match));
		
		for(Player p : listaPlayer)
			this.playerMap.put(p.getPlayerID(), p);
		
		Graphs.addAllVertices(this.grafo, listaPlayer);
		
		List<Action> listaAzioni = new ArrayList<>(this.dao.listMatchActions(match, this.playerMap));
		//divido le azioni in home e away
		List<Action> listaHome = new ArrayList<>();
		List<Action> listaAway = new ArrayList<>();
		for(Action a : listaAzioni) {
			if(a.getTeamID().equals(match.getTeamHomeID())) {
				listaHome.add(a);
			} else {
				listaAway.add(a);
			}
		}
		
		for(Action aHome : listaHome) {
			for(Action aAway : listaAway) {
				double peso = aHome.getEfficiency() - aAway.getEfficiency();
				if(peso >= 0) {
					//da home verso away
					Graphs.addEdge(this.grafo, aHome.getPlayer(), aAway.getPlayer(), peso);
				} else {
					//da away verso home
					Graphs.addEdge(this.grafo, aAway.getPlayer(), aHome.getPlayer(), (peso * -1));
				}
			}
		}
		
		
	}
	
	public Player getBestPlayer() {
		Player best = null;
		this.bestEff = -100000.0;
		
		for(Player p : this.grafo.vertexSet()) {
			double sommaUscenti = 0.0;
			double sommaEntranti = 0.0;
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				sommaUscenti+=this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				sommaEntranti+=this.grafo.getEdgeWeight(e);
			}
			
			double delta = sommaUscenti - sommaEntranti;
			if(delta > bestEff) {
				this.bestEff = delta;
				best = p;
			}
		}
		
		return best;
	}
	
	public Player getBestPlayer(List<Player> setGiocatori) { //versione per la simulazione con il set di giocatori in campo
		Player best = null;
		this.bestEff = -100000.0;
		
		for(Player p : setGiocatori) {
			double sommaUscenti = 0.0;
			double sommaEntranti = 0.0;
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				sommaUscenti+=this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				sommaEntranti+=this.grafo.getEdgeWeight(e);
			}
			
			double delta = sommaUscenti - sommaEntranti;
			if(delta > bestEff) {
				this.bestEff = delta;
				best = p;
			}
		}
		
		return best;
	}
	
	public void simula(Match match, int numAzioni) {
		this.sim = new Simulator(this.grafo, this, match.getTeamHomeID());
		sim.init(numAzioni);
		sim.run();
	}
	
	public int getHomeGoals() {
		return this.sim.getGoalHome();
	}
	
	public int getAwayGoals() {
		return this.sim.getGoalAway();
	}
	
	public int getEspulsiHome() {
		return this.sim.getEspulsiHome();
	}
	
	public int getEspulsiAway() {
		return this.sim.getEspulsiAway();
	}
	
	public double getBestEffiency() {
		return this.bestEff;
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> listAllMatches(){
		if(this.listaMatch == null)
			this.listaMatch = new ArrayList<Match>(dao.listAllMatches());
		return this.listaMatch;
	}
	
	
}
