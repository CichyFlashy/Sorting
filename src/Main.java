import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Arrays;

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

    public static List<?> detectAndSort(List<String> data) {
        if (data.isEmpty()) return Collections.emptyList();

        try {
            List<Integer> integers = data.stream().map(Integer::parseInt).collect(Collectors.toList());
            bubbleSort(integers);
            return integers;
        } catch (NumberFormatException ignored) { }

        try {
            List<Double> doubles = data.stream().map(Double::parseDouble).collect(Collectors.toList());
            selectionSort(doubles);
            return doubles;
        } catch (NumberFormatException ignored) { }

        List<String> strings = new ArrayList<>(data);
        bubbleSort(strings);
        return strings;
    }

    public static <T extends Comparable<T>> void bubbleSort(List<T> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
    }

    public static <T extends Comparable<T>> void selectionSort(List<T> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j).compareTo(list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            Collections.swap(list, i, minIndex);
        }
    }

    public static List<String> generateData(String type, int size) {
        Random random = new Random();
        List<String> data = new ArrayList<>();

        switch (type) {
            case "Integer":
                for (int i = 0; i < size; i++) {
                    data.add(String.valueOf(random.nextInt(10000)));
                }
                break;
            case "Double":
                for (int i = 0; i < size; i++) {
                    data.add(String.valueOf(random.nextDouble() * 10000));
                }
                break;
            case "String":
                for (int i = 0; i < size; i++) {
                    data.add(UUID.randomUUID().toString().substring(0, 8));
                }
                break;
        }

        return data;
    }

    public static void saveToFile(List<?> data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Object obj : data) {
                writer.write(obj.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu do pliku: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sortowanie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel fileLabel = new JLabel("Wprowadź nazwę pliku:");
        JTextField fileField = new JTextField(20);
        JButton loadButton = new JButton("Wczytaj");

        JLabel generateLabel = new JLabel("Generuj dane:");
        JComboBox<String> dataTypeBox = new JComboBox<>(new String[]{"Integer", "Double", "String"});
        JComboBox<Integer> dataSizeBox = new JComboBox<>(new Integer[]{100, 1000, 100000});
        JButton generateButton = new JButton("Generuj");

        JButton sortButton = new JButton("Sortuj i Zapisz");
        JTextArea textArea = new JTextArea(20, 60);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        inputPanel.add(fileLabel);
        inputPanel.add(fileField);
        inputPanel.add(loadButton);
        inputPanel.add(generateLabel);
        inputPanel.add(dataTypeBox);
        inputPanel.add(dataSizeBox);
        inputPanel.add(generateButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(sortButton, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> {
            String filePath = fileField.getText().trim();
            if (!filePath.isEmpty()) {
                List<String> data = readFile(filePath);
                if (data != null) {
                    textArea.setText(String.join("\n", data));
                } else {
                    JOptionPane.showMessageDialog(frame, "Nie udało się wczytać danych z pliku.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        generateButton.addActionListener(e -> {
            String type = (String) dataTypeBox.getSelectedItem();
            int size = (int) dataSizeBox.getSelectedItem();
            List<String> data = generateData(type, size);
            textArea.setText(String.join("\n", data));
        });

        sortButton.addActionListener(e -> {
            String content = textArea.getText();
            if (!content.isEmpty()) {
                List<String> data = Arrays.asList(content.split("\n"));
                List<?> sortedData = detectAndSort(data);
                textArea.setText(sortedData.stream().map(Object::toString).collect(Collectors.joining("\n")));
                saveToFile(sortedData, "sorted_data.txt");
                JOptionPane.showMessageDialog(frame, "Dane zostały posortowane i zapisane do pliku sorted_data.txt.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}
