package pl.futurecollars.invoicing.jsondatabase;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class JsonDatabase implements Database {

  private ActualPath actualPath = new ActualPath();
  private GetActualId getActualId = new GetActualId();
  private FileService fileService = new FileService();
  private JsonService jsonService = new JsonService();

  @Override
  public int save(Invoice invoice) throws IOException {
    getActualId.updateId();
    try {
      invoice.setId(getActualId.getId());
      fileService.appendLineToFile(Path.of(actualPath.getIdPath("database")), jsonService.json(invoice));
    } catch (IOException e) {
      throw e;
    }
    return getActualId.getId();
  }

  @Override
  public Optional<Invoice> getById(int id) throws IOException {
    List<String> invoices = fileService.readAllLines(Path.of(actualPath.getIdPath("database")));
    for (String invoice : invoices) {
      if (invoice.contains("\"id\":" + id + ",")) {
        return Optional.of(jsonService.object(invoice, Invoice.class));
      }
    }
    throw new IllegalArgumentException("Nie znaleziono faktury o podanym ID: " + id);
  }

  @Override
  public List<Invoice> getAll() throws IOException {
    List<String> invoices = fileService.readAllLines(Path.of(actualPath.getIdPath("database")));
    List<Invoice> returnInvoices = new ArrayList<>();
    for (String invoice : invoices) {
      returnInvoices.add(jsonService.object(invoice, Invoice.class));
    }
    return returnInvoices;
  }

  @Override
  public void update(int id, Invoice updatedInvoice) throws IOException {
    List<Invoice> invoices = getAll();
    List<String> updateInvoices = new ArrayList<>();
    Boolean ifExist = false;
    for (Invoice invoice : invoices) {
      if (invoice.getId() == id) {
        ifExist = true;
        invoice.setDate(updatedInvoice.getDate());
        invoice.setBuyer(updatedInvoice.getBuyer());
        invoice.setSeller(updatedInvoice.getSeller());
        invoice.setEntries(updatedInvoice.getEntries());
        updateInvoices.add(jsonService.json(invoice));
      } else {
        updateInvoices.add(jsonService.json(invoice));
      }
    }
    if (ifExist) {
      fileService.writeLinesToFile(Path.of(actualPath.getIdPath("database")), updateInvoices);
    } else {
      throw new IllegalArgumentException("Nie znaleziono faktury o podanym ID: " + id);
    }
  }

  @Override
  public void delete(int id) throws IOException {
    List<Invoice> listOfInvoice = getAll();
    List<String> deletedInvoices = new ArrayList<>();
    Boolean ifExist = false;
    for (Invoice invoice : listOfInvoice) {
      if (invoice.getId() == id) {
        ifExist = true;
      } else {
        deletedInvoices.add(jsonService.json(invoice));
      }
    }
    if (ifExist) {
      fileService.writeLinesToFile(Path.of(actualPath.getIdPath("database")), deletedInvoices);
    } else {
      throw new IllegalArgumentException("Nie znaleziono faktury o podanym ID: " + id);
    }
  }
}
