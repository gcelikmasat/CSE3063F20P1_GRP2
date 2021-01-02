/*
 * Label.java
 * This class keeps the neccessary information about a label that is used for tagging.
 * 
 */

package CSE3063F20P1_GRP2;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Label implements Serializable{

	private int id;
	private String text;
	private double freq;	

	public Label(int id, String text) {
		this.id = id;
		this.text = text;
	}

	//getter - setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	//prints frequency of a label
	public String printFreq() {
		return text + ": %" + freq;
	}

	@Override
	public String toString() {
		return id + ":" + text;
	}

}
