package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {

  private final Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }


  public Optional<Invoice> getById(int id) throws IOException {
    return database.getById(id);
  }

  public List<Invoice> getAll() throws IOException {
    return database.getAll();
  }

  public int save(Invoice invoice) throws IOException {
    return database.save(invoice);
  }

  public void update(int id, Invoice updatedInvoice) throws IOException {
    database.update(id, updatedInvoice);
  }

  public void delete(int id) throws IOException{
    database.delete(id);
  }
}
