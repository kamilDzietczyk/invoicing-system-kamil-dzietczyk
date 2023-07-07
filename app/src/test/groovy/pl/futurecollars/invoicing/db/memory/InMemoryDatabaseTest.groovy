package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import java.time.LocalDate

class InMemoryDatabaseTest extends Specification{
    def "should save invoice and return incremented ID"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 0, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])

        when:
        int savedId = database.save(invoice)

        then:
        savedId == 1

        and:
        Optional<Invoice> savedInvoiceOptional = database.getById(savedId)
        savedInvoiceOptional.isPresent()
        savedInvoiceOptional.get() == invoice
    }

    def "should get invoice by id"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 0, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        database.save(invoice)

        when:
        Optional<Invoice> inv = database.getById(1)

        then:
        inv.isPresent()
    }

    def "should throw error when try get invoice by id"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 0, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        database.save(invoice)

        when:
        Optional<Invoice> inv = database.getById(2)

        then:
        final IllegalArgumentException exception = thrown()
        exception.message == "Nie znaleziono faktury o podanym ID: 2"
    }

    def "should get invoice by id"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 0, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        Invoice invoice2 = new Invoice(id: 1, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        database.save(invoice)
        database.save(invoice2)

        when:
        List<Invoice> listOfInvoice = database.getAll()

        then:
        listOfInvoice.size() == 2
    }

    def "should update invoice"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 1, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        database.save(invoice)

        when:
        Invoice updatedInvoice = new Invoice(id: 1, date: LocalDate.now().minusDays(1), seller: new Company(), buyer: new Company(), entries: [])
        database.update(1, updatedInvoice)

        then:
        Optional<Invoice> retrievedInvoice = database.getById(1)
        retrievedInvoice.isPresent()
        retrievedInvoice.get() == updatedInvoice
    }

    def "should delete"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 1, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        database.save(invoice)

        when:
        database.delete(1)
        database.getById(1)
        then:
        final IllegalArgumentException exception = thrown()
        exception.message == "Nie znaleziono faktury o podanym ID: 1"
    }

    def "update should throw error when invoice not found"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 1, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        database.save(invoice)

        when:
        Invoice updatedInvoice = new Invoice(id: 1, date: LocalDate.now().minusDays(1), seller: new Company(), buyer: new Company(), entries: [])
        database.update(2, updatedInvoice)
        then:
        final IllegalArgumentException exception = thrown()
        exception.message == "Nie znaleziono faktury o podanym ID: 2"
    }

    def "delete should throw error when invoice not found"() {
        given:
        InMemoryDatabase database = new InMemoryDatabase()
        Invoice invoice = new Invoice(id: 1, date: LocalDate.now(), seller: new Company(), buyer: new Company(), entries: [])
        database.save(invoice)

        when:
        database.delete(2)
        then:
        final IllegalArgumentException exception = thrown()
        exception.message == "Nie znaleziono faktury o podanym ID: 2"
    }
}
