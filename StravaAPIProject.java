import java.util.*;

public class FlightPlan {

    private int numCities;
    private Map<String, Integer> cityIndex;
    private List<List<Edge>> adjList;

    public FlightPlan(int numCities) {
        this.numCities = numCities;
        this.cityIndex = new HashMap<>();
        this.adjList = new ArrayList<>(numCities);
        for (int i = 0; i < numCities; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addFlight(String fromCity, String toCity, double cost) {
        int from = getCityIndex(fromCity);
        int to = getCityIndex(toCity);
        adjList.get(from).add(new Edge(to, cost));
    }

    private int getCityIndex(String city) {
        if (!cityIndex.containsKey(city)) {
            int index = cityIndex.size();
            cityIndex.put(city, index);
        }
        return cityIndex.get(city);
    }

    public List<FlightPath> findShortestPath(String fromCity, String toCity) {
        int from = getCityIndex(fromCity);
        int to = getCityIndex(toCity);

        PriorityQueue<Node> pq = new PriorityQueue<>(numCities, Comparator.comparingDouble(n -> n.dist));
        pq.add(new Node(from, 0.0, null));

        Map<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(from, pq.peek());

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (node.city == to) {
                return buildPath(node);
            }
            for (Edge edge : adjList.get(node.city)) {
                double newDist = node.dist + edge.cost;
                Node neighbor = nodeMap.getOrDefault(edge.to, new Node(edge.to, Double.POSITIVE_INFINITY, null));
                if (newDist < neighbor.dist) {
                    neighbor.dist = newDist;
                    neighbor.prev = node;
                    if (pq.contains(neighbor)) {
                        pq.remove(neighbor);
                    }
                    pq.offer(neighbor);
                    nodeMap.put(neighbor.city, neighbor);
                }
            }
        }

        return null; // no path found
    }

    private List<FlightPath> buildPath(Node node) {
        List<FlightPath> path = new ArrayList<>();
        while (node.prev != null) {
            int from = node.prev.city;
            int to = node.city;
            double cost = getFlightCost(from, to);
            path.add(new FlightPath(getCityName(from), getCityName(to), cost));
            node = node.prev;
        }
        Collections.reverse(path);
        return path;
    }

    private double getFlightCost(int from, int to) {
        for (Edge edge : adjList.get(from)) {
            if (edge.to == to) {
                return edge.cost;
            }
        }
        throw new IllegalArgumentException("No flight from " + getCityName(from) + " to " + getCityName(to));
    }

    private String getCityName(int index) {
        for (Map.Entry<String, Integer> entry : cityIndex.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("No city with index " + index);
    }

    private static class Node {
        int city;
        double dist;
        Node prev;

        Node(int city, double dist, Node prev) {
            this.city = city;
            this.dist = dist;
            this.prev = prev;
        }
    }

    private static class Edge {
        int to;
        double cost;
    }
};