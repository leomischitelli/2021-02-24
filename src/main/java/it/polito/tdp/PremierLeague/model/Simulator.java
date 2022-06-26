package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulator {
	
	//coda degli eventi
	private Queue<Event> queue;
	
	//parametri simulazione
	private int numAzioni;
	private int idHome;
//	private int idAway;
	private Graph<Player, DefaultWeightedEdge> grafo;

	//stato del mondo
	private List<Player> giocatori; //giocatori in campo, serve per il migliore. Ogni azion
	private int numHome;
	private int numAway;
	private Player best;
	private Model model;
	
	//output
	private int goalHome;
	private int goalAway;
//	private int espHome;
//	private int espAway;
	
	public Simulator(Graph<Player, DefaultWeightedEdge> grafo, Model model, int idHome) {
		super();
		this.grafo = grafo;
		this.model = model;
		this.idHome = idHome;
//		this.idAway = idAway;
	}
	
	public void init(int numAzioni) {
		this.queue = new PriorityQueue<>();
		this.numAzioni = numAzioni;
		this.giocatori = new ArrayList<Player>(this.grafo.vertexSet());	
		this.numHome = 11;
		this.numAway = 11;
		this.goalHome = 0;
		this.goalAway = 0;
//		this.espHome = 0;
//		this.espAway = 0;
		this.best = model.getBestPlayer(); //per inizializzare utilizzo forma classica
		
		int i = 0;
		while(i<this.numAzioni) {
			generaEvento(i);
			i++;
		}
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		// TODO Auto-generated method stub
		EventType type = e.getType();
		
		switch(type) {
		case GOAL:
			if(numHome > numAway) {
				goalHome++;
			} else if (numAway > numHome) {
				goalAway++;
			} else {
				//parita numerica, scelgo giocatore migliore
				if(idHome == best.getTeamID())
					goalHome++;
				else
					goalAway++;
			}
			
			break;
		
		case ESPULSIONE:
			if(Math.random() < 0.6) {
				//espulsione squadra del migliore
				int i = 0;
				int giocatoriInCampo = numHome + numAway;
				while(giocatoriInCampo == (numHome + numAway)) {
					Player player = giocatori.get(i);
					if(giocatori.get(i).getTeamID() == best.getTeamID()) {
						giocatori.remove(i);
						if(idHome == best.getTeamID())
							numHome--;
						else
							numAway--;
						//se ho espulso il migliore, trovo il nuovo migliore in campo
						if(player.equals(best)) {
							best = model.getBestPlayer(giocatori);
						}
						
					}
					i++;
				}
			} else {
				//espulsione altra squadra
				int i = 0;
				int giocatoriInCampo = numHome + numAway;
				while(giocatoriInCampo == (numHome + numAway)) {
					if(giocatori.get(i).getTeamID() != best.getTeamID()) {
						giocatori.remove(i);
						if(idHome != best.getTeamID())
							numHome--;
						else
							numAway--;
												
					}
					i++;
				}	
			}
			
			break;
			
		case INFORTUNIO:
			for(int i=1; i<=2; i++) {
				generaEvento(numAzioni+1);
				numAzioni++;
			}
			
			if(Math.random() < 0.50) {
				//genero un altro evento
				generaEvento(numAzioni+1);
				numAzioni++;
			}
			break;
		}
		
	}
	
	private void generaEvento(int time) {
		double probabilta = Math.random();
		if(probabilta < 0.50) {
			//goal
			this.queue.add(new Event(time, EventType.GOAL));
		} else if (probabilta < 0.80) {
			//espulsione
			this.queue.add(new Event(time, EventType.ESPULSIONE));
		} else {
			//infortunio
			this.queue.add(new Event(time, EventType.INFORTUNIO));
		}
	}

	public int getGoalHome() {
		return goalHome;
	}

	public int getGoalAway() {
		return goalAway;
	}

	public int getEspulsiHome() {
		return 11 - numHome;
	}

	public int getEspulsiAway() {
		return 11 - numAway;
	}
	
	
	
	

}
