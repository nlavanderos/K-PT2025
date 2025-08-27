# Proyecto Komatsu PT - Rutas más rápidas (API con Spring Boot) @Nicolas Lavanderos
Este proyecto implementa una API REST en **Java + Spring Boot** que permite calcular la ruta más corta entre dos puntos usando el algoritmo de Dijkstra.

## Requisitos

Antes de ejecutar este proyecto, asegúrate de tener instalado:

- **Java 21**: `java -version`
- **Spring Boot 3.5.5** (gestionado con Maven según `pom.xml`)
- **Maven 3.9.10**: `mvn -version`
- **Postman**: [https://www.postman.com/downloads/](https://www.postman.com/downloads/)  
  Importa la colección incluida: `komatsu-pt.postman_collection.json`

## Instalación y ejecución

```bash
# Clonar repositorio
git clone https://github.com/tu-usuario/pt.git
cd pt

# Instalar dependencias y compilar
mvn clean install

# Iniciar la aplicación
mvn spring-boot:run

#Consulta los endpoints disponibles en la collection(usa los archivos csv datos.csv/datos_test.csv)
Komatsu PT

# Ejecutar tests unitarios
mvn test
