
public class Edge {
	private Vertex destination;
	private double cost;

	public Edge(Vertex destination, double cost) {
		this.destination = destination;
		this.cost = cost;
	}

	public Vertex getDestination() {
		return destination;
	}

	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

}
