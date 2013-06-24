package data;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/18/13
 */
public class DataArcContent {
	private int arcType;
	private double value;

	public DataArcContent(int arcType) {
		this.arcType = arcType;
		this.value = 0;
	}

	public DataArcContent(int arcType, double value) {
		this.arcType = arcType;
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public int getArcType() {
		return arcType;
	}
}
