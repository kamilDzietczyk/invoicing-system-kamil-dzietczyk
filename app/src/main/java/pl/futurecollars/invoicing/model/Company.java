package pl.futurecollars.invoicing.model;

import lombok.Data;

@Data
public class Company {

  private String taskIdentificationNumber;
  private String address;
  private String name;
}
