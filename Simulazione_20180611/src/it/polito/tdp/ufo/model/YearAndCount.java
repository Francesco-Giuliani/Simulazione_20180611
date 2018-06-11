package it.polito.tdp.ufo.model;

import java.time.Year;

public class YearAndCount {

	private Year anno;
	private int numAvvistamenti;
	public YearAndCount(Year anno, int numAvvistamenti) {
		super();
		this.anno = anno;
		this.numAvvistamenti = numAvvistamenti;
	}
	public Year getAnno() {
		return anno;
	}
	public int getNumAvvistamenti() {
		return numAvvistamenti;
	}
	@Override
	public String toString() {
		return anno + "  " + numAvvistamenti + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anno == null) ? 0 : anno.hashCode());
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
		YearAndCount other = (YearAndCount) obj;
		if (anno == null) {
			if (other.anno != null)
				return false;
		} else if (!anno.equals(other.anno))
			return false;
		return true;
	}
	
	
}
