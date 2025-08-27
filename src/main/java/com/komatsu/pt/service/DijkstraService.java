package com.komatsu.pt.service;

import com.komatsu.pt.model.Connection;
import com.komatsu.pt.model.RouteResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DijkstraService {

    public RouteResult findShortestPath(List<Connection> connections, String start, String end) {
        // Validar datos de entrada
        if (connections == null || connections.isEmpty() || start == null || end == null) {
            return null;
        }

        // Construir el grafo (incluir nodos como start y end aunque solo aparezcan como destino)
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        for (Connection connection : connections) {
            graph.computeIfAbsent(connection.getLocStart(), k -> new HashMap<>())
                    .put(connection.getLocEnd(), connection.getTime());
            // asegurar que el destino también está en el grafo
            graph.computeIfAbsent(connection.getLocEnd(), k -> new HashMap<>());
        }

        // Si start o end no existen en el grafo → no hay ruta
        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            return null;
        }

        // Inicialización
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String loc : graph.keySet()) {
            distances.put(loc, Integer.MAX_VALUE);
            previousNodes.put(loc, null);
        }
        distances.put(start, 0);
        pq.add(start);

        // Algoritmo de Dijkstra
        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(end)) {
                break; // ya encontramos la ruta más corta
            }

            for (Map.Entry<String, Integer> neighbor : graph.getOrDefault(current, Collections.emptyMap()).entrySet()) {
                String neighborLoc = neighbor.getKey();
                int travelTime = neighbor.getValue();
                int newDist = distances.get(current) + travelTime;

                if (newDist < distances.get(neighborLoc)) {
                    distances.put(neighborLoc, newDist);
                    previousNodes.put(neighborLoc, current);
                    pq.add(neighborLoc);
                }
            }
        }

        // Si no hay camino hacia el destino
        if (distances.get(end) == Integer.MAX_VALUE) {
            return null;
        }

        // Reconstruir la ruta
        List<String> path = new ArrayList<>();
        String currentNode = end;
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = previousNodes.get(currentNode);
        }
        Collections.reverse(path);

        return new RouteResult(path, distances.get(end));
    }
}
