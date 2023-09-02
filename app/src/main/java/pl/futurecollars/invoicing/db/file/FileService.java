package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileService {

  ActualPath act = new ActualPath();

  public void appendLineToFile(Path path, String line) throws IOException {
    Files.writeString(path, line + System.lineSeparator(), StandardOpenOption.APPEND);
  }

  public void writeToFile(Path path, String line) throws IOException {
    Files.writeString(path, line, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public void writeLinesToFile(Path path, List<String> lines) throws IOException {
    Files.write(path, lines);
  }

  public List<String> readAllLines(Path path) throws IOException {
    return Files.readAllLines(path);
  }

  public int getId() throws IOException {
    List<String> actId = readAllLines(Path.of(ActualPath.idPath));
    return Integer.parseInt(actId.get(0));
  }

  public void updateId() throws IOException {
    int actIid = getId();
    writeToFile(Path.of(ActualPath.idPath), String.valueOf(actIid + 1));
  }

}
