import java.util.List;

public class Vertex implements Comparable<Vertex> {
	private String name;
	private double latitdue;
	private double longitude;
	private List<Edge> edges;

	public Vertex(String name, double latitdue, double longitude) {
		this.name = name;
		this.latitdue = latitdue;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitdue() {
		return latitdue;
	}

	public void setLatitdue(double latitdue) {
		this.latitdue = latitdue;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public void addEdges(Vertex destination, double cost) {
		edges.add(new Edge(destination, cost));
	}

	@Override
	public String toString() {
		return name; // or any other information you want to include
	}

	@Override
	public int compareTo(Vertex o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
