package pl.futurecollars.invoicing.jsondatabase;

import lombok.Data;

@Data
public class ActualId {
  private int id;

  public ActualId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
