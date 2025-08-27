package com.komatsu.pt;

import com.komatsu.pt.model.Connection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.komatsu.pt.service.RouteService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteService routeService;

    @BeforeAll
    static void setUpAll(@Autowired MockMvc mockMvc,
                         @Autowired RouteService routeService) throws Exception {

        // Obtener archivo reducido de prueba
        Path path = Paths.get(RouteControllerTest.class.getClassLoader().getResource("datos_test.csv").toURI());
        File file = path.toFile();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                file.getName(),
                "text/csv",
                new FileInputStream(file)
        );

        // Cargar conexiones en memoria antes de todos los tests
        mockMvc.perform(multipart("/api/routes/upload").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("Archivo cargado correctamente"));

        // Verificación rápida
        List<Connection> connections = routeService.getConnections();
        assertEquals(3, connections.size(), "El CSV de prueba debería cargar 3 conexiones");
    }

    @Test
    void testUploadRoutes() {
        List<Connection> connections = routeService.getConnections();
        assertEquals(3, connections.size());
        assertEquals("A", connections.get(0).getLocStart());
    }

    @Test
    void testGetShortestPath() throws Exception {
        mockMvc.perform(get("/api/routes/shortest-path")
                        .param("start", "A")
                        .param("end", "C"))
                .andDo(print()) // 👈 imprime el response en consola
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route[0]").value("A"))
                .andExpect(jsonPath("$.route[1]").value("C"))
                .andExpect(jsonPath("$.totalTime").value(30));
    }

    @Test
    void testGetShortestPathInvalidParams() throws Exception {
        mockMvc.perform(get("/api/routes/shortest-path")
                        .param("start", "")
                        .param("end", ""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
