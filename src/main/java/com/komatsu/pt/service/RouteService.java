package com.komatsu.pt.service;

import com.komatsu.pt.model.Connection;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private final List<Connection> connections = new ArrayList<>();

    // Cargar rutas desde el archivo CSV de forma sincrónica
    public void loadRoutesFromCSV(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;  // Indicador para omitir el encabezado

            while ((line = reader.readLine()) != null) {
                // Omitir la primera línea (encabezado)
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                line = line.trim();

                // Ignorar líneas vacías
                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");

                // Asegurarse de que la línea tiene 3 campos
                if (fields.length == 3) {
                    String locStart = fields[0].trim();
                    String locEnd = fields[1].trim();
                    try {
                        // Intentar convertir el valor de tiempo a entero
                        int time = Integer.parseInt(fields[2].trim());
                        connections.add(new Connection(locStart, locEnd, time));
                    } catch (NumberFormatException e) {
                        // Manejar el error si el campo "time" no es un número válido
                        System.out.println("Error al procesar el tiempo (debe ser un número): " + fields[2]);
                    }
                } else {
                    System.out.println("Fila mal formateada: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo CSV", e);
        }
    }

    // Obtener las conexiones cargadas
    public List<Connection> getConnections() {
        return connections;
    }
}
