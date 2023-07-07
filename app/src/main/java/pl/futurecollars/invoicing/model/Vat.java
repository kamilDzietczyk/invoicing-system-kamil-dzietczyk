package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public enum Vat {

  Vat_21(21),
  Vat_8(8),
  Vat_7(7),
  Vat_5(5),
  Vat_0(0),
  Vat_ZW(0);

  private final BigDecimal rate;

  Vat(int rate) {
    this.rate = BigDecimal.valueOf(rate);
  }
}
