import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania pliku: " + e.getMessage());
            return null;
        }
        return lines;
    }

    public static void main(String[] args) {
        //Tworzenie okna
        JFrame frame = new JFrame("Sortowanie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maksymalizacja okna
        frame.setResizable(true); // Umożliwienie zmiany rozmiaru

        // Wczytywanie danych z pliku
        String filePath = "data.txt"; // Ścieżka do pliku tekstowego
        List<String> fileData = readFile(filePath);

        // Wyświetlenie danych w konsoli
        if (fileData != null) {
            for (String line : fileData) {
                System.out.println(line);
            }
        } else {
            System.out.println("Nie udało się wczytać danych z pliku.");
        }

        // Wyświetlenie okna
        frame.setVisible(true);

    }
}