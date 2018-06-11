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
	private Graph<String, DefaultEdge> grafo; 
	private List<String> stati;
	
	public Model() {
		this.sdao = new SightingsDAO();
		this.anniAvvistamenti = this.getAnniAvvistamenti();
	}
	
	public void creaGrafo(Year anno) {
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.grafo, sdao.getStati(anno));
		System.out.println(this.grafo.vertexSet().size());
		this.stati = new ArrayList<>(grafo.vertexSet());
		//EDGES
		//METODO LENTO CON DOPPIO CICLO MIGLIORABILE CON s1<s2 (CICLO TRIANGOLARE) O 
		//con QUERY CHE PRENDA DIRETTAMENTE TUTTI GLI EDGE (v. classe sightings commento 
		//in metodo esisteAvvistamento(anno, s1, s2) 
		
		for(int i =0; i<this.stati.size(); i++)
			for(int j=i+1; j<this.stati.size(); j++) {
				String st1 = stati.get(i), st2 = stati.get(j);
				if(!st1.equals(st2)) {
					if(sdao.esisteAvvistamento(anno, st1, st2))
						this.grafo.addEdge(st1, st2);
				}
			}
		System.out.println(this.grafo.edgeSet().size());
	}
	
	
	public List<YearAndCount> getAnniAvvistamenti(){
		if(this.anniAvvistamenti == null || this.anniAvvistamenti.isEmpty())
			return this.sdao.getAnniNumAvvistamenti();
		else 
			return this.anniAvvistamenti;
	}


}
