// Importowane biblioteki
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Arrays;

class Sorting {

    /**
     * Wczytuje dane z pliku tekstowego.
     *
     * @param sciezkaPliku Ścieżka do pliku, z którego mają zostać wczytane dane.
     * @return Lista łańcuchów znaków reprezentujących wiersze w pliku.
     */
    private static List<String> wczytajDaneZPliku(String sciezkaPliku) {
        List<String> linie = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sciezkaPliku))) {
            String linia;
            while ((linia = br.readLine()) != null) {
                linie.add(linia);
            }
        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
        return linie;
    }

    /**
     * Rozpoznaje typ danych w liście i sortuje je odpowiednią metodą.
     *
     * @param dane Lista danych w postaci łańcuchów znaków.
     * @return Posortowana lista danych. Może to być lista liczb całkowitych, zmiennoprzecinkowych lub tekstów.
     */
    private static List<?> rozpoznajITablicaSortujaca(List<String> dane) {
        if (dane.isEmpty()) return Collections.emptyList();

        try {
            List<Integer> liczbyCalkowite = dane.stream().map(Integer::parseInt).collect(Collectors.toList());
            sortowanieBabelkowe(liczbyCalkowite);
            return liczbyCalkowite;
        } catch (NumberFormatException e) {
            System.err.println("Nie można sparsować danych jako Integer: " + e.getMessage());
        }

        try {
            List<Double> liczbyZmiennoprzecinkowe = dane.stream().map(Double::parseDouble).collect(Collectors.toList());
            sortowaniePrzezWybieranie(liczbyZmiennoprzecinkowe);
            return liczbyZmiennoprzecinkowe;
        } catch (NumberFormatException e) {
            System.err.println("Nie można sparsować danych jako Double: " + e.getMessage());
        }

        List<String> teksty = new ArrayList<>(dane);
        sortowanieBabelkowe(teksty);
        return teksty;
    }

    /**
     * Sortuje listę danych metodą sortowania bąbelkowego.
     *
     * @param lista Lista elementów, która implementuje interfejs Comparable.
     *              Jest to ogólny typ, który umożliwia porównywanie elementów na podstawie ich naturalnej kolejności.
     *              W przypadku różnych typów danych, jak Integer, Double czy String, lista będzie przyjmować odpowiedni typ.
     * @param <T>   Typ elementów na liście, który musi implementować interfejs Comparable.
     */
    private static <T extends Comparable<T>> void sortowanieBabelkowe(List<T> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (lista.get(j).compareTo(lista.get(j + 1)) > 0) {
                    Collections.swap(lista, j, j + 1);
                }
            }
        }
    }

    /**
     * Sortuje listę danych metodą sortowania przez wybieranie.
     *
     * @param lista Lista elementów, która implementuje interfejs Comparable.
     * @param <T>   Typ elementów na liście, który musi implementować interfejs Comparable.
     */
    private static <T extends Comparable<T>> void sortowaniePrzezWybieranie(List<T> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndeks = i;
            for (int j = i + 1; j < n; j++) {
                if (lista.get(j).compareTo(lista.get(minIndeks)) < 0) {
                    minIndeks = j;
                }
            }
            Collections.swap(lista, i, minIndeks);
        }
    }

    /**
     * Generuje dane na podstawie zadanego typu i rozmiaru.
     *
     * @param typ   Typ danych, które mają zostać wygenerowane: "Integer", "Double" lub "String".
     * @param rozmiar Liczba elementów, które mają zostać wygenerowane.
     * @return Lista danych wygenerowanych na podstawie zadanego typu i rozmiaru.
     */
    private static List<String> generujDane(String typ, int rozmiar) {
        Random losowy = new Random();
        List<String> dane = new ArrayList<>();

        switch (typ) {
            case "Integer":
                for (int i = 0; i < rozmiar; i++) {
                    dane.add(String.valueOf(losowy.nextInt(10000)));
                }
                break;
            case "Double":
                for (int i = 0; i < rozmiar; i++) {
                    dane.add(String.format("%.2f", losowy.nextDouble() * 10000));
                }
                break;
            case "String":
                for (int i = 0; i < rozmiar; i++) {
                    dane.add(UUID.randomUUID().toString().substring(0, 8));
                }
                break;
            default:
                throw new IllegalArgumentException("Nieobsługiwany typ danych: " + typ);
        }

        return dane;
    }

    /**
     * Zapisuje dane do pliku tekstowego.
     *
     * @param dane         Lista danych, które mają zostać zapisane do pliku.
     * @param sciezkaPliku Ścieżka do pliku, do którego dane mają zostać zapisane.
     */
    private static void zapiszDoPliku(List<?> dane, String sciezkaPliku) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(sciezkaPliku))) {
            for (Object element : dane) {
                bw.write(element.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Tworzenie interfejsu graficznego
        JFrame ramka = new JFrame("Sortowanie danych");
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ramka.setSize(800, 600);
        ramka.setLayout(new BorderLayout());

        // Panel wejściowy z polami do wczytywania plików i generowania danych
        JPanel panelWejsciowy = new JPanel(new FlowLayout());
        JLabel etykietaPlik = new JLabel("Podaj nazwę pliku:");
        JTextField poleTekstowePlik = new JTextField(20);
        JButton przyciskWczytaj = new JButton("Wczytaj");

        JLabel etykietaGenerowanie = new JLabel("Generuj dane:");
        JComboBox<String> typDanychBox = new JComboBox<>(new String[]{"Integer", "Double", "String"});
        JComboBox<Integer> rozmiarDanychBox = new JComboBox<>(new Integer[]{100, 1000, 100000});
        JButton przyciskGeneruj = new JButton("Generuj");

        JButton przyciskSortuj = new JButton("Sortuj i Zapisz");
        JTextArea obszarTekstowy = new JTextArea(20, 60);
        obszarTekstowy.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(obszarTekstowy);

        // Dodanie komponentów do panelu wejściowego
        panelWejsciowy.add(etykietaPlik);
        panelWejsciowy.add(poleTekstowePlik);
        panelWejsciowy.add(przyciskWczytaj);
        panelWejsciowy.add(etykietaGenerowanie);
        panelWejsciowy.add(typDanychBox);
        panelWejsciowy.add(rozmiarDanychBox);
        panelWejsciowy.add(przyciskGeneruj);

        ramka.add(panelWejsciowy, BorderLayout.NORTH);
        ramka.add(scrollPane, BorderLayout.CENTER);
        ramka.add(przyciskSortuj, BorderLayout.SOUTH);

        // Akcja dla przycisku Wczytaj
        przyciskWczytaj.addActionListener(e -> {
            String sciezkaPliku = poleTekstowePlik.getText().trim();
            if (!sciezkaPliku.isEmpty()) {
                List<String> dane = wczytajDaneZPliku(sciezkaPliku);
                if (dane != null) {
                    obszarTekstowy.setText(String.join("\n", dane));
                } else {
                    JOptionPane.showMessageDialog(ramka, "Nie udało się wczytać danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Akcja dla przycisku Generuj
        przyciskGeneruj.addActionListener(e -> {
            String typ = (String) typDanychBox.getSelectedItem();
            int rozmiar = (int) rozmiarDanychBox.getSelectedItem();
            List<String> dane = generujDane(typ, rozmiar);
            obszarTekstowy.setText(String.join("\n", dane));
        });

        // Akcja dla przycisku Sortuj
        przyciskSortuj.addActionListener(e -> {
            String zawartosc = obszarTekstowy.getText();
            if (!zawartosc.isEmpty()) {
                List<String> dane = Arrays.asList(zawartosc.split("\n"));
                List<?> posortowane = rozpoznajITablicaSortujaca(dane);
                obszarTekstowy.setText(posortowane.stream().map(Object::toString).collect(Collectors.joining("\n")));
                zapiszDoPliku(posortowane, "posortowane_dane.txt");
                JOptionPane.showMessageDialog(ramka, "Dane zostały zapisane do pliku posortowane_dane.txt.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ramka.setVisible(true);
    }
}
