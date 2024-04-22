
import java.util.*;

public class Dijkstra {

	public List<Vertex> shortestPath(Vertex source, Vertex destination, Graph graph) {
		XHeap<Vertex> heap = new XHeap<>(
				(vertex1, vertex2) -> Double.compare(graph.distance.get(vertex1), graph.distance.get(vertex2)));
		heap.add(source);
		graph.distance.put(source, 0.0);

		Hashtable<Vertex, Vertex> pre = new Hashtable<>();

		boolean destinationReached = false;

		while (!heap.isEmpty()) {
			Vertex curr = heap.poll();

			if (curr.equals(destination)) {
				destinationReached = true;
				break;
			}

			for (Edge edge : graph.graph.get(curr)) {
				Vertex adj = edge.getDestination();
				double distance = edge.getCost();

				if (graph.distance.get(adj) > graph.distance.get(curr) + distance) {
					graph.distance.put(adj, graph.distance.get(curr) + distance);
					pre.put(adj, curr);
					heap.add(adj);
				}
			}
		}

		if (!destinationReached) {
			return null;
		}

		List<Vertex> path = new ArrayList<>();
		Vertex curr = destination;
		while (curr != source) {
			path.add(curr);
			curr = pre.get(curr);
		}
		path.add(source);
		Collections.reverse(path);

		return path;
	}
}
