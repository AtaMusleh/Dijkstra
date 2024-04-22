import java.util.List;

public class Test {
	public static void main(String[] args) {
		// Create vertices
		Vertex A = new Vertex("A", 0.0, 0.0);
		Vertex B = new Vertex("B", 1.0, 0.0);
		Vertex C = new Vertex("C", 2.0, 0.0);
		Vertex D = new Vertex("D", 2.0, 1.0);
		Vertex E = new Vertex("E", 2.0, 2.0);

		// Create graph
		Graph graph = new Graph();
		graph.addVertex(A);
		graph.addVertex(B);
		graph.addVertex(C);
		graph.addVertex(D);
		graph.addVertex(E);

		// Create edges
		graph.addEdge(A, B, 1.0);
		graph.addEdge(A, C, 4.0);
		graph.addEdge(B, C, 2.0);
		graph.addEdge(B, D, 5.0);
		graph.addEdge(C, D, 1.0);
		graph.addEdge(C, E, 3.0);
		graph.addEdge(D, E, 7.0);

		// Create Dijkstra object
		Dijkstra dijkstra = new Dijkstra();

		// Find shortest path from A to E
		List<Vertex> shortestPath = dijkstra.shortestPath(A, E, graph);

		// Print the result
		if (shortestPath != null) {
			System.out.println("Shortest Path from A to E: " + shortestPath);
		} else {
			System.out.println("No path found from A to E");
		}
		if (shortestPath != null) {
			double totalDistance = 0.0;

			System.out.println("Shortest Path from A to E:");
			for (int i = 0; i < shortestPath.size(); i++) {
				Vertex vertex = shortestPath.get(i);
				System.out.print(vertex);

				if (i < shortestPath.size() - 1) {
					Edge edge = findEdge(graph, vertex, shortestPath.get(i + 1));
					double distance = edge.getCost();
					totalDistance += distance;
					System.out.print(" -> " + distance + " -> ");
				}
			}

			System.out.println("\nTotal Distance: " + totalDistance);
		} else {
			System.out.println("No path found from A to E");
		}
	}

	private static Edge findEdge(Graph graph, Vertex source, Vertex destination) {
		for (Edge edge : graph.graph.get(source)) {
			if (edge.getDestination().equals(destination)) {
				return edge;
			}
		}
		return null; // Handle appropriately in your code
	}

}
