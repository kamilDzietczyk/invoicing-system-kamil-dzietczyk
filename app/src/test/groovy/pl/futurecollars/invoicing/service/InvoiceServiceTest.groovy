package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import java.time.LocalDate

class InvoiceServiceTest extends Specification{
    def "should get invoice by id"() {
        given:
        Database database = Mock(Database)
        InvoiceService invoiceService = new InvoiceService(database)
        Invoice expectedInvoice = new Invoice(id: 1, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])

        when:
        database.getById(1) >> Optional.of(expectedInvoice)
        Optional<Invoice> actualInvoice = invoiceService.getById(1)

        then:
        actualInvoice.isPresent()
        actualInvoice.get() == expectedInvoice
    }

    def "should return all invoices"() {
        given:
        Database database = Mock(Database)
        InvoiceService invoiceService = new InvoiceService(database)
        List<Invoice> expectedInvoices = [
                new Invoice(id: 1, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: []),
                new Invoice(id: 2, date: LocalDate.now().minusDays(1), seller: new Company(), buyer: new Company(), entries: [])
        ]

        when:
        database.getAll() >> expectedInvoices
        List<Invoice> actualInvoices = invoiceService.getAll()

        then:
        actualInvoices == expectedInvoices
    }

    def "should save invoice and return incremented ID"() {
        given:
        Database database = Mock(Database)
        InvoiceService invoiceService = new InvoiceService(database)
        Invoice invoice = new Invoice(id: 0, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        int expectedId = 1

        when:
        database.save(invoice) >> expectedId
        int actualId = invoiceService.save(invoice)

        then:
        actualId == expectedId
    }

    def "should update invoice by id"() {
        given:
        Database database = Mock(Database)
        InvoiceService invoiceService = new InvoiceService(database)
        Invoice updatedInvoice = new Invoice(id: 1, date: LocalDate.now().minusDays(1), seller: new Company(), buyer: new Company(), entries: [])

        when:
        invoiceService.update(1, updatedInvoice)

        then:
        1 * database.update(1, updatedInvoice)
    }

    def "should delete invoice by id"() {
        given:
        Database database = Mock(Database)
        InvoiceService invoiceService = new InvoiceService(database)

        when:
        invoiceService.delete(1)

        then:
        1 * database.delete(1)
    }
}
