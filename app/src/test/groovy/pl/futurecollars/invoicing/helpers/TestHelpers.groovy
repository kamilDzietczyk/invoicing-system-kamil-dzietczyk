package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id){
        Company.builder()
                .taskIdentificationNumber(id.toString().repeat(10))
                .address("ul Leszczynowa $id 12-345 Zamosc")
                .name("Company $id sp.zoo")
                .build()
    }

    static invoiceEntry(int id){
        InvoiceEntry.builder()
                    .description("Monitor Dell vfg456$id")
                    .price(BigDecimal.TEN)
                    .vatValue(BigDecimal.ONE)
                    .vatRate(Vat.Vat_8)
                    .build()
    }

    static Invoice createTestInvoice(int id) {
        return Invoice.builder()
                .date(LocalDate.now())
                .buyer(company(id))
                .seller(company(id+1))
                .entries(List.of(invoiceEntry(id+2)))
                .build();
    }
}
