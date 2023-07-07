package pl.futurecollars.invoicing.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

  private String taskIdentificationNumber;
  private String address;
  private String name;
}
