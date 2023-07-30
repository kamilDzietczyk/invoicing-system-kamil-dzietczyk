package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static buy() {
        Company.builder()
               .taskIdentificationNumber("123456789")
               .address("address")
               .name("buyer")
               .build()
    }

    static buy1() {
        Company.builder()
                .taskIdentificationNumber("2222222")
                .address("street")
                .name("buyer1")
                .build()
    }

    static sell() {
        Company.builder()
                .taskIdentificationNumber("987654321")
                .address("sserdda")
                .name("seller")
                .build()
    }

    static sell1() {
        Company.builder()
                .taskIdentificationNumber("111111111")
                .address("sellerStreet")
                .name("seller")
                .build()
    }

    static invoiceEntry(){
        InvoiceEntry.builder()
                    .description("aaaaa")
                    .price(BigDecimal.TEN)
                    .vatValue(BigDecimal.ONE)
                    .vatRate(Vat.Vat_8)
                    .build()
    }

    static invoiceEntry1(){
        InvoiceEntry.builder()
                .description("bbbbb")
                .price(BigDecimal.ONE)
                .vatValue(BigDecimal.TEN)
                .vatRate(Vat.Vat_21)
                .build()
    }

    static Invoice createTestInvoice() {
        return Invoice.builder()
                .date(LocalDate.now())
                .buyer(buy())
                .seller(sell())
                .entries(List.of(invoiceEntry()))
                .build();
    }

    static Invoice createTestInvoice1() {
        return Invoice.builder()
                .date(LocalDate.now())
                .buyer(buy1())
                .seller(sell1())
                .entries(List.of(invoiceEntry1()))
                .build();
    }
}
