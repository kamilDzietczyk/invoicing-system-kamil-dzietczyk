package pl.futurecollars.invoicing.db.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Repository
public class InMemoryDatabase implements Database {

  private final Map<Integer, Invoice> mapOfInvoices = new HashMap<>();
  private int id = 1;

  @Override
  public Optional<Invoice> getById(int id) {
    Invoice invoice = mapOfInvoices.get(id);
    if (invoice == null) {
      throw new IllegalArgumentException("Nie znaleziono faktury o podanym ID: " + id);
    }
    return Optional.of(invoice);
  }

  @Override
  public List<Invoice> getAll() {
    return mapOfInvoices.values().stream().toList();
  }

  @Override
  public int save(Invoice invoice) {
    mapOfInvoices.put(id, invoice);
    return id++;
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    if (mapOfInvoices.containsKey(id)) {
      mapOfInvoices.put(id, updatedInvoice);
      System.out.println("Faktura o ID " + id + " została zaktualizowana.");
    } else {
      throw new IllegalArgumentException("Nie znaleziono faktury o podanym ID: " + id);
    }
  }

  @Override
  public void delete(int id) {
    if (mapOfInvoices.containsKey(id)) {
      mapOfInvoices.remove(id);
      System.out.println("Faktura o ID " + id + " została usunięta.");
    } else {
      throw new IllegalArgumentException("Nie znaleziono faktury o podanym ID: " + id);
    }
  }
}
