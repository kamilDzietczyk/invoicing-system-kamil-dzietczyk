package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.service.IdService
import spock.lang.Specification

import java.nio.file.Files
import java.time.LocalDate

class JsonDatabaseTest extends Specification{

    def dbPath

    Database setup() {
        def filesService = new FileService()

        def idPath = File.createTempFile('ids', '.txt').toPath()
        def idService = new IdService(idPath, filesService)

        dbPath = File.createTempFile('invoices', '.txt').toPath()
        return new JsonDatabase(dbPath, idService, filesService, new JsonService())
    }


    def "Should save invoice"() {
        given:
        def db = setup()

        when:
        db.save(TestHelpers.createTestInvoice(4))

        then:
        1 == Files.readAllLines(dbPath).size()
    }
    def "Should get by ID"() {
        given:
        def db = setup()
        def testInvoice = TestHelpers.createTestInvoice(4)
        db.save(testInvoice)

        when:
        def result = db.getById(1)

        then:
        result.isPresent()
        result.get().id == 1
    }

    def "Should throw error when try get by invalid Id"() {
        given:
        def db = setup()

        when:
        def result = db.getById(-1)

        then:
        !result.isPresent()
    }


    def "Should get all Invoice"() {
        given:
        def db = setup()
        def testInvoice1 = TestHelpers.createTestInvoice(1)
        def testInvoice2 = TestHelpers.createTestInvoice(2)
        db.save(testInvoice1)
        db.save(testInvoice2)

        when:
        def allInvoices = db.getAll()

        then:
        allInvoices.size() == 2
    }


    def "Should update Invoice"() {
        given:
        def db = setup()
        def testInvoice = TestHelpers.createTestInvoice(1)
        db.save(testInvoice)

        when:
        testInvoice.date = LocalDate.now().plusDays(1)
        db.update(1, testInvoice)
        def updatedInvoice = db.getById(1)

        then:
        updatedInvoice.isPresent()
        updatedInvoice.get().date == LocalDate.now().plusDays(1)
    }


    def "Should throw error when failed to update invoice"() {
        given:
        def db = setup()
        def testInvoice = TestHelpers.createTestInvoice(1)

        when:
        db.update(1, testInvoice)

        then:
        def e = thrown(RuntimeException)
        e.message == "Failed to update invoice"
    }



    def "Should delete Invoice"() {
        given:
        def db = setup()
        def testInvoice = TestHelpers.createTestInvoice(1)
        db.save(testInvoice)

        when:
        db.delete(1)
        def deletedInvoice = db.getById(1)

        then:
        deletedInvoice.isEmpty()
    }

}
