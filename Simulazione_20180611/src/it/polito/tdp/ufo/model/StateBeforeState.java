package it.polito.tdp.ufo.model;

public class StateBeforeState {

	private String firstState, secondState;

	public StateBeforeState() {
		super();
	}

	public StateBeforeState(String firstState, String secondState) {
		super();
		this.firstState = firstState;
		this.secondState = secondState;
	}

	@Override
	public String toString() {
		return firstState + " " + secondState+";";
	}

	public String getFirstState() {
		return firstState;
	}

	public String getSecondState() {
		return secondState;
	}

	public void setFirstState(String firstState) {
		this.firstState = firstState;
	}

	public void setSecondState(String secondState) {
		this.secondState = secondState;
	}
	
}
