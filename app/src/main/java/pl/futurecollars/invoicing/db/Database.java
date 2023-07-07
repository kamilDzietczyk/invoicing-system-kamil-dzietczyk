package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.Invoice;

public interface Database {
  int save(Invoice invoice) throws IOException;

  Optional<Invoice> getById(int id) throws IOException;

  List<Invoice> getAll() throws IOException;

  void update(int id, Invoice updatedInvoice) throws IOException;

  void delete(int id) throws IOException;
}
