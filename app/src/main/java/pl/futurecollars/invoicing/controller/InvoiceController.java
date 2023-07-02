package pl.futurecollars.invoicing.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RestController
@RequestMapping("invoice")
public class InvoiceController {

  private final InvoiceService invoiceService;

  @Autowired
  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping()
  public ResponseEntity<List<Invoice>> getAllInvoicesMemory() throws IOException {
    List<Invoice> invoices = invoiceService.getAll();
    return ResponseEntity.ok(invoices);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Invoice> getInvoiceByIdMemory(@PathVariable("id") int id) throws IOException {
    Optional<Invoice> invoice = invoiceService.getById(id);
    return invoice.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/add")
  public ResponseEntity<Integer> addInvoiceMemory(@RequestBody Invoice invoice) throws IOException {
    int id = invoiceService.save(invoice);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
  }

  @PutMapping("/put/{id}")
  public ResponseEntity<Void> updateInvoiceMemory(@PathVariable("id") int id, @RequestBody Invoice updatedInvoice) throws IOException {
    invoiceService.update(id, updatedInvoice);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteInvoiceMemory(@PathVariable("id") int id) throws IOException {
    invoiceService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
