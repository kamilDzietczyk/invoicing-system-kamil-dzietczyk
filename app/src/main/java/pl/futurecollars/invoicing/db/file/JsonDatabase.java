package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class JsonDatabase implements Database {

  private ActualPath actualPath = new ActualPath();
  private FileService fileService = new FileService();
  private JsonService jsonService = new JsonService();

  @Override
  public int save(Invoice invoice) {
    try {
      fileService.updateId();
      invoice.setId(fileService.getId());
      fileService.appendLineToFile(Path.of(ActualPath.databasePath), jsonService.convertToJson(invoice));
    } catch (IOException ex) {
      throw new RuntimeException("Database failed to save invoice", ex);
    }
    return 0;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    try {
      return fileService.readAllLines(Path.of(ActualPath.databasePath))
          .stream()
          .filter(line -> existsId(line, id))
          .map(line -> jsonService.convertToObject(line, Invoice.class))
          .findFirst();
    } catch (IOException ex) {
      throw new RuntimeException("Failed to get by id: " + id);
    }
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return fileService.readAllLines(Path.of(ActualPath.databasePath))
          .stream()
          .map(line -> jsonService.convertToObject(line, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException ex) {
      throw new RuntimeException("Filed to get all invoice");
    }
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    try {
      List<String> allLinesFromFile = fileService.readAllLines(Path.of(ActualPath.databasePath));
      List<String> tempList = allLinesFromFile
          .stream()
          .filter(line -> !existsId(line, id))
          .collect(Collectors.toCollection(ArrayList::new));

      if (allLinesFromFile.size() == tempList.size()) {
        throw new RuntimeException("Failed to update invoice");
      }

      updatedInvoice.setId(id);
      tempList.add(jsonService.convertToJson(updatedInvoice));

      fileService.writeLinesToFile(Path.of(ActualPath.databasePath), tempList);

    } catch (IOException ex) {
      throw new RuntimeException("Failed to update invoice");
    }
  }

  @Override
  public void delete(int id) {
    try {
      var tempList = fileService.readAllLines(Path.of(ActualPath.databasePath))
          .stream()
          .filter(line -> !existsId(line, id))
          .toList();
      fileService.writeLinesToFile(Path.of(ActualPath.databasePath), tempList);

    } catch (IOException ex) {
      throw new RuntimeException("Failed to delete invoice with id: " + id, ex);
    }
  }

  private boolean existsId(String line, int id) {
    return line.contains("\"id\":" + id + ",");
  }
}
