package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

  private int id;
  private LocalDate date;
  private Company seller;
  private Company buyer;
  private List<InvoiceEntry> entries;
}
