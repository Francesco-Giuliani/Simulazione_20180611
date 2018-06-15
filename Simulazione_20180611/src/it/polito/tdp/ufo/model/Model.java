package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private List<YearAndCount> anniAvvistamenti;
	private SightingsDAO sdao;
	private Graph<String, StateBeforeState> grafo; 
	private List<String> stati;
	private List<StateBeforeState> edges;
	
	//Ricorsione
	private List<String> percorso;
	
	public Model() {
		this.sdao = new SightingsDAO();
		this.anniAvvistamenti = this.getAnniAvvistamenti();
	}
	
	public void creaGrafo(Year anno) {
		this.grafo = new SimpleDirectedGraph<>(StateBeforeState.class);
		
		Graphs.addAllVertices(this.grafo, sdao.getStati(anno));
		System.out.println(this.grafo.vertexSet().size());
		this.stati = new ArrayList<>(grafo.vertexSet());
		
		long start = System.nanoTime();
		edges = sdao.getAlledges(anno);
		long end = System.nanoTime();
		double runTime = ((double)(end-start))/Math.pow(10, 9);
		System.out.println("Completion time of getAlledges(): "+runTime+" secondi");
		
		start = System.nanoTime();
		for(StateBeforeState sbs: edges) {
			StateBeforeState e = this.grafo.addEdge(sbs.getFirstState(), sbs.getSecondState());
			e.setFirstState(sbs.getFirstState());
			e.setSecondState(sbs.getSecondState());
		}
		end = System.nanoTime();
		runTime = ((double)(end-start))/Math.pow(10, 9);
		System.out.println("Completion time to add all edges: "+runTime+" secondi");
		System.out.println(this.grafo.edgeSet().size());
		
	}
	
	
	public List<YearAndCount> getAnniAvvistamenti(){
		if(this.anniAvvistamenti == null || this.anniAvvistamenti.isEmpty())
			return this.sdao.getAnniNumAvvistamenti();
		else 
			return this.anniAvvistamenti;
	}

	public List<String> getStati() {
		return stati;
	}

	public List<StateBeforeState> getEdges() {
		return edges;
	}

	public List<String> getStatiAdiacenti(String state) {
		return Graphs.neighborListOf(grafo, state);
	}

	public List<String> getStatiRaggiungibili(String state) {
		ConnectivityInspector<String, StateBeforeState> ci = new ConnectivityInspector<String, StateBeforeState>(grafo);
		List<String > raggiungibili = new ArrayList<>(ci.connectedSetOf(state));
		return raggiungibili;
	}

	public List<String> trovaSequenzaStatiPiuLungaDa(String state) {
		percorso = new ArrayList<>();
		
		List<String> parziale = new LinkedList<>();
		parziale.add(state);
		
		this.recursive(state, parziale);
		
		return percorso;
	}

	private void recursive(String selectedState, List<String> parziale) {
		
		List<String> successivi = new LinkedList<>(Graphs.successorListOf(grafo, selectedState));
		successivi.removeAll(parziale);
		
		//CONDIZIONE DI USCITA
		if(successivi.isEmpty()) {
			if(percorso.size()<parziale.size()) {
				percorso = new ArrayList<>(parziale);
			}
			return;
		}
		
		for(String state: successivi) {
			parziale.add(state);
			this.recursive(state, parziale);
			parziale.remove(state);
		}
	}
	
	

}
