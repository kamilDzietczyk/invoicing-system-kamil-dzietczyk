package pl.futurecollars.invoicing.jsondatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileService {

  public void appendLineToFile(Path path, String line) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
      writer.write(line + System.lineSeparator());
    }
  }

  public void writeLinesToFile(Path path, List<String> lines) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
      for (String line : lines) {
        writer.write(line);
        writer.newLine();
      }
    }
  }

  public List<String> readAllLines(Path path) throws IOException {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    }
    return lines;
  }
}
