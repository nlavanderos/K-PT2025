package com.komatsu.pt.controller;

import com.komatsu.pt.model.RouteResult;
import com.komatsu.pt.service.DijkstraService;
import com.komatsu.pt.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;
    private final DijkstraService dijkstraService;

    @Autowired
    public RouteController(RouteService routeService, DijkstraService dijkstraService) {
        this.routeService = routeService;
        this.dijkstraService = dijkstraService;
    }

    // Cargar el archivo CSV
    @PostMapping("/upload")
    public String uploadRoutes(@RequestParam("file") MultipartFile file) {
        try {
            // Cargar las rutas desde el archivo CSV de manera sincrónica
            routeService.loadRoutesFromCSV(file);
            return "Archivo cargado correctamente";
        } catch (Exception e) {
            return "Error al cargar el archivo: " + e.getMessage();
        }
    }

    // Consultar la ruta más rápida
    @GetMapping("/shortest-path")
    public ResponseEntity<RouteResult> getShortestPath(@RequestParam String start, @RequestParam String end) {
        if (start == null || start.isEmpty() || end == null || end.isEmpty()) {
            return ResponseEntity.badRequest().body(new RouteResult());
        }

        try {
            RouteResult result = dijkstraService.findShortestPath(routeService.getConnections(), start, end);

            if (result == null || result.getRoute() == null || result.getRoute().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RouteResult());
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace(); // 👈 imprime el error real en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RouteResult());
        }
    }

}