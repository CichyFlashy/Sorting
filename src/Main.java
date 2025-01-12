import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        //Tworzenie okna
        JFrame frame = new JFrame("Sortowanie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maksymalizacja okna
        frame.setResizable(true); // Umożliwienie zmiany rozmiaru

        // Tworzenie przycisku
        JButton button = new JButton("Kliknij mnie!");

        // Dodanie przycisku do okna
        frame.add(button);

        // Wyświetlenie okna
        frame.setVisible(true);

    }
}