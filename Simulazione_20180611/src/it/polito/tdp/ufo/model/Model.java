package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private List<YearAndCount> anniAvvistamenti;
	private SightingsDAO sdao;
	private Graph<String, StateBeforeState> grafo; 
	private List<String> stati;
	private List<StateBeforeState> edges;
	
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
	
	

}
