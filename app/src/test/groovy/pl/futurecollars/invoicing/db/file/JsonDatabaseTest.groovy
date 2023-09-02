package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.db.file.ActualPath
import pl.futurecollars.invoicing.db.file.FileService
import pl.futurecollars.invoicing.db.file.JsonDatabase
import pl.futurecollars.invoicing.db.file.JsonService
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path

class JsonDatabaseTest extends Specification{

    @Shared ActualPath actualPath
    @Shared FileService fileService
    @Shared JsonService jsonService
    @Shared JsonDatabase jsonDatabase
    @Shared file
    @Shared Vat vat

    void setup() {
        actualPath = new ActualPath()
        fileService = new FileService()
        jsonService = new JsonService()
        jsonDatabase = new JsonDatabase()
        file = new File(actualPath.databasePath)
        vat = Vat.Vat_7
    }

    def "Should save invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        when:
        jsonDatabase.save(invoice);
        then:
        file.length()!= 0;
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(ActualPath.idPath),"0")
    }
    def "Should get by ID"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        jsonDatabase.save(invoice);
        when:
        def invoiceId = fileService.getId()
        def foundInvoice = jsonDatabase.getById(invoiceId)
        then:
        foundInvoice.ifPresent { invoice1 ->
            invoice.getBuyer().getTaskIdentificationNumber() == "123456789"
        }
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(ActualPath.idPath), "0")
    }
    def "Should throw error when try get by invalid Id"() {
        when:
        jsonDatabase.getById(fileService.getId() + 1)
        then:
        def exception = thrown(RuntimeException)
        exception.message.contains("Failed to get by id: " + (fileService.getId() + 1))
        fileService.writeToFile(Path.of(ActualPath.idPath),"0")
    }
    def "Should get all Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        def invoice1 = TestHelpers.createTestInvoice(2)
        jsonDatabase.save(invoice);
        jsonDatabase.save(invoice1);
        List<Invoice> invoices;
        when:
        invoices = jsonDatabase.getAll();
        then:
        invoices.size() ==2;
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(ActualPath.idPath),"0")
    }
    def "Should throw error when try get all Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        def invoice1 = TestHelpers.createTestInvoice(2)
        jsonDatabase.save(invoice);
        jsonDatabase.save(invoice1);
        List<Invoice> invoices;
        file.delete()
        when:
        invoices = jsonDatabase.getAll();
        then:
        def exception = thrown(RuntimeException)
        exception.message.contains("Filed to get all invoice")
        cleanup:
        // Usunięcie pliku z indeksami
        fileService.writeToFile(Path.of(ActualPath.idPath),"0")
    }
    def "Should update Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        jsonDatabase.save(invoice);
        when:
        jsonDatabase.update(1,invoice)
        then:
        Optional<Invoice> inv = jsonDatabase.getById(1)
        inv.get().getBuyer().getTaskIdentificationNumber() == "1111111111"

        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(ActualPath.idPath),"0")
    }
    def "Should throw error when failed to update invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        jsonDatabase.save(invoice);

        when:
        def updatedInvoice = TestHelpers.createTestInvoice(2)
        jsonDatabase.update(222,updatedInvoice)

        then:
        def exception = thrown(RuntimeException)
        exception.message.contains("Failed to update invoice")

        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(ActualPath.idPath), "0")
    }
    def "Should throw exception when try update invalid invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        def invoice1 = TestHelpers.createTestInvoice(2)
        jsonDatabase.save(invoice);
        when:
        jsonDatabase.update(fileService.getId()+5,invoice1)
        then:
        def exception = thrown(RuntimeException)
        exception.message.contains("Failed to update invoice")
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(ActualPath.idPath),"0")
    }
    def "Should delete Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice(1)
        def invoice1 = TestHelpers.createTestInvoice(2)
        jsonDatabase.save(invoice);
        jsonDatabase.save(invoice1);
        when:
        jsonDatabase.delete(1)
        then:
        List<Invoice> tempInv = jsonDatabase.getAll();
        tempInv.size()==1
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(ActualPath.idPath),"0")
    }
}
