package com.komatsu.pt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Collections;

@Data
@AllArgsConstructor
public class RouteResult {
    private List<String> route;    // Lista de ubicaciones en la ruta
    private int totalTime;         // Tiempo total de la ruta

    // Constructor para cuando no se encuentre una ruta
    public RouteResult() {
        this.route = Collections.emptyList(); // Lista vacía para ruta no encontrada
        this.totalTime = -1; // Indicador de error en el tiempo total
    }
}
