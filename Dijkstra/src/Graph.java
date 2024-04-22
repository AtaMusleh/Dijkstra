import java.util.*;

public class Graph {
	Hashtable<Vertex, List<Edge>> graph;
	Hashtable<Vertex, Double> distance;

	public Graph() {
		graph = new Hashtable<>();
		distance = new Hashtable<>();
	}

	public void addVertex(Vertex vertex) {
		graph.put(vertex, new LinkedList<>());
		distance.put(vertex, Double.MAX_VALUE);
	}

	public void addEdge(Vertex source, Vertex destination, double cost) {
		graph.get(source).add(new Edge(destination, cost));
	}

	public Vertex getVertexByName(String name) {
		for (Vertex vertex : graph.keySet()) {
			if (vertex.getName().equals(name)) {
				return vertex;
			}
		}
		return null;
	}

	public List<Vertex> getVertices() {
		return new ArrayList<>(graph.keySet());
	}
}
