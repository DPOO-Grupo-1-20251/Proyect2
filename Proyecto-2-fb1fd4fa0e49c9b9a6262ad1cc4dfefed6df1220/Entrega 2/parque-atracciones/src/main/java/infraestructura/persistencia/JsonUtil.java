// ✅ Updated by Axiom for Manuel Villaveces (◣ ◢) KickAss Games 🏆
// Hardcoded root path fix for getParqueAtraccionesRoot()

package infraestructura.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilidad para leer y escribir objetos en formato JSON.
 */
public class JsonUtil {
    private static final String DATA_DIR_NAME = "data"; // Name of the data directory

    /**
     * Devuelve la ruta absoluta y normalizada a un archivo dentro del directorio de datos.
     * Ejemplo: getDataFilePath("culturales.json") -> <absoluto>/data/culturales.json
     *          getDataFilePath("elementos", "culturales.json") -> <absoluto>/data/elementos/culturales.json
     */
    public static String getDataFilePath(String... pathParts) {
        String base = getParqueAtraccionesRoot();
        Path dataDir = Paths.get(base, DATA_DIR_NAME);
        Path fullPath = dataDir;
        for (String part : pathParts) {
            fullPath = fullPath.resolve(part);
        }
        try {
            java.nio.file.Files.createDirectories(fullPath.getParent());
        } catch (IOException e) {
            System.err.println("Advertencia: No se pudo crear el directorio padre para " + fullPath + ": " + e.getMessage());
        }
        return fullPath.toAbsolutePath().normalize().toString();
    }

    /**
     * ✅ HARD-CODED PATH VERSION
     * Devuelve la ruta absoluta al directorio raíz del proyecto 'parque-atracciones'.
     */
    private static String getParqueAtraccionesRoot() {
        // 📝 Replace this path with the actual absolute path to your 'parque-atracciones' folder
        return "C:/Users/Manuel/Documents/Uni/Proyecto-2/Entrega 2/parque-atracciones";
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setLenient()
            .create();

    /**
     * Guarda una lista de objetos en un archivo JSON (ruta absoluta).
     *
     * @param absolutePath Ruta absoluta del archivo.
     * @param objects Lista de objetos a guardar.
     * @param <T> Tipo de los objetos.
     */
    public static <T> void writeToFileAbsolute(String absolutePath, List<T> objects) {
        Path filePath = Paths.get(absolutePath);
        try {
            java.nio.file.Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            System.err.println("Advertencia: No se pudo crear el directorio padre para " + absolutePath + ": " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter(absolutePath)) {
            gson.toJson(objects, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en el archivo " + absolutePath, e);
        } catch (com.google.gson.JsonIOException e) {
            throw new RuntimeException("Error de E/S de GSON al escribir en el archivo " + absolutePath, e);
        }
    }

    /**
     * Lee una lista de objetos desde un archivo JSON (ruta absoluta).
     * Si el archivo no existe, devuelve una lista vacía.
     *
     * @param absolutePath Ruta absoluta del archivo.
     * @param typeToken TypeToken para el tipo de la lista.
     * @param <T> Tipo de los objetos en la lista.
     * @return Lista de objetos del tipo especificado.
     */
    public static <T> List<T> readFromFileAbsolute(String absolutePath, TypeToken<List<T>> typeToken) {
        File file = new File(absolutePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        if (!file.isFile()) {
            System.err.println("[ERROR] La ruta no es un archivo: " + absolutePath);
            return new ArrayList<>();
        }
        if (!file.canRead()) {
            System.err.println("[ERROR] No se puede leer el archivo: " + absolutePath);
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = typeToken.getType();
            List<T> result = gson.fromJson(reader, type);
            return result != null ? result : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error de E/S al leer el archivo " + absolutePath + ": " + e.getMessage());
            return new ArrayList<>();
        } catch (com.google.gson.JsonSyntaxException e) {
            System.err.println("Error de sintaxis JSON en el archivo " + absolutePath + ": " + e.getMessage());
            return new ArrayList<>();
        } catch (com.google.gson.JsonIOException e) {
            System.err.println("Error de E/S de GSON al leer el archivo " + absolutePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
