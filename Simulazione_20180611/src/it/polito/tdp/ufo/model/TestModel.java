package it.polito.tdp.ufo.model;

import java.time.Year;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		for(YearAndCount y : m.getAnniAvvistamenti())
			System.out.println(y);
		
		m.creaGrafo(Year.of(2000));
	}

}
