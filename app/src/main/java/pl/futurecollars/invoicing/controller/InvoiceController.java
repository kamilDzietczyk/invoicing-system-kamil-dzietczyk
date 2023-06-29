package pl.futurecollars.invoicing.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.jsondatabase.JsonDatabase;
import pl.futurecollars.invoicing.model.Invoice;

@RestController
@RequestMapping("invoice")
public class InvoiceController {
  private final InMemoryDatabase inMemoryDatabase;
  private final JsonDatabase jsonDatabase;

  public InvoiceController() {
    this.inMemoryDatabase = new InMemoryDatabase();
    this.jsonDatabase = new JsonDatabase();
  }

  @GetMapping("/Memory")
  public ResponseEntity<List<Invoice>> getAllInvoicesMemory() throws IOException {
    List<Invoice> invoices = inMemoryDatabase.getAll();
    return ResponseEntity.ok(invoices);
  }

  @GetMapping("/File")
  public ResponseEntity<List<Invoice>> getAllInvoicesFile() throws IOException {
    List<Invoice> invoices = jsonDatabase.getAll();
    return ResponseEntity.ok(invoices);
  }

  @GetMapping("/Memory/{id}")
  public ResponseEntity<Invoice> getInvoiceByIdMemory(@PathVariable("id") int id) {
    Optional<Invoice> invoice = inMemoryDatabase.getById(id);
    return invoice.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/File/{id}")
  public ResponseEntity<Invoice> getInvoiceByIdFile(@PathVariable("id") int id) throws IOException {
    Optional<Invoice> invoice = jsonDatabase.getById(id);
    return invoice.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/Memory/add")
  public ResponseEntity<Integer> addInvoiceMemory(@RequestBody Invoice invoice) {
    int id = inMemoryDatabase.save(invoice);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
  }

  @PostMapping("/File/add")
  public ResponseEntity<Integer> addInvoiceFile(@RequestBody Invoice invoice) throws IOException {
    int id = jsonDatabase.save(invoice);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
  }

  @PutMapping("/Memory/put/{id}")
  public ResponseEntity<Void> updateInvoiceMemory(@PathVariable("id") int id, @RequestBody Invoice updatedInvoice) throws IOException {
    inMemoryDatabase.update(id, updatedInvoice);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/File/put/{id}")
  public ResponseEntity<Void> updateInvoiceFile(@PathVariable("id") int id, @RequestBody Invoice updatedInvoice) throws IOException {
    jsonDatabase.update(id, updatedInvoice);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/Memory/delete/{id}")
  public ResponseEntity<Void> deleteInvoiceMemory(@PathVariable("id") int id) throws IOException {
    inMemoryDatabase.delete(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/File/delete/{id}")
  public ResponseEntity<Void> deleteInvoiceFile(@PathVariable("id") int id) throws IOException {
    jsonDatabase.delete(id);
    return ResponseEntity.noContent().build();
  }
}
