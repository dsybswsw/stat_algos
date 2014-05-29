package paral_opt;

/**
 * @author Shiwei Wu
 * @Date May 16, 2013
 */
public class Pair<A, B> {
	private A first;

	private B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	public boolean equals(Object other) {
		if (other instanceof Pair) {
			Pair<?, ?> otherPair = (Pair<?, ?>) other;
			return ((this.first == otherPair.first || (this.first != null && otherPair.first != null && this.first
							.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
							&& otherPair.second != null && this.second.equals(otherPair.second))));
		}
		return false;
	}

	public String toString() {
		return "<" + first + ", " + second + '>';
	}

	public int hashCode() {
		return first.hashCode() * 37 + second.hashCode();
	}
}
