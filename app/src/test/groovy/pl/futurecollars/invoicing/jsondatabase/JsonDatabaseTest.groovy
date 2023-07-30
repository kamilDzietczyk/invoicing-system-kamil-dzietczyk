package pl.futurecollars.invoicing.jsondatabase

import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path

class JsonDatabaseTest extends Specification{

    @Shared ActualPath actualPath = new ActualPath();
    @Shared FileService fileService = new FileService();
    @Shared JsonService jsonService = new JsonService();
    @Shared JsonDatabase jsonDatabase = new JsonDatabase();
    @Shared file = new File(actualPath.databasePath);
    @Shared Vat vat = Vat.Vat_7

    def "Should save invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        when:
        jsonDatabase.save(invoice);
        then:
        file.length()!= 0;
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(actualPath.getIdPath()),"0")
    }
    def "Should get by ID"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        jsonDatabase.save(invoice);
        when:
        def invoiceId = fileService.getId()
        def foundInvoice = jsonDatabase.getById(invoiceId)
        then:
        foundInvoice.isPresent()
        foundInvoice.ifPresent { invoice1 ->
            invoice.getBuyer().getTaskIdentificationNumber() == "123456789"
        }
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(actualPath.getIdPath()), "0")
    }

    def "Should throw error when try get by invalid Id"() {
        when:
        jsonDatabase.getById(fileService.getId() + 1)
        then:
        def exception = thrown(RuntimeException)
        exception.message.contains("Failed to get by id: " + (fileService.getId() + 1))
        fileService.writeToFile(Path.of(actualPath.getIdPath()),"0")
    }

    def "Should get all Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        def invoice1 = TestHelpers.createTestInvoice1()
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
        fileService.writeToFile(Path.of(actualPath.getIdPath()),"0")
    }

    def "Should throw error when try get all Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        def invoice1 = TestHelpers.createTestInvoice1()
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
        fileService.writeToFile(Path.of(actualPath.getIdPath()),"0")
    }

    def "Should update Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        jsonDatabase.save(invoice);
        when:
        jsonDatabase.update(1,invoice)
        then:
        Optional<Invoice> inv = jsonDatabase.getById(1)
        inv.get().getBuyer().getTaskIdentificationNumber() == "123456789"

        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(actualPath.getIdPath()),"0")
    }

    def "Should throw error when failed to update invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        jsonDatabase.save(invoice);

        when:
        def updatedInvoice = TestHelpers.createTestInvoice() // Poniżej implementacja metody, która zwraca zmodyfikowaną fakturę
        jsonDatabase.update(222,updatedInvoice)

        then:
        def exception = thrown(RuntimeException)
        exception.message.contains("Failed to update invoice")

        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(actualPath.getIdPath()), "0")
    }


    def "Should throw exception when try update invalid invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        def invoice1 = TestHelpers.createTestInvoice()
        jsonDatabase.save(invoice);
        when:
        jsonDatabase.update(fileService.getId()+2,invoice1)
        then:
        def exception = thrown(RuntimeException)
        exception.message.contains("Failed to update invoice")
        cleanup:
        // Usunięcie pliku z indeksami
        file.delete()
        fileService.writeToFile(Path.of(actualPath.getIdPath()),"0")
    }

    def "Should delete Invoice"() {
        given:
        file.createNewFile();
        def invoice = TestHelpers.createTestInvoice()
        def invoice1 = TestHelpers.createTestInvoice1()
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
        fileService.writeToFile(Path.of(actualPath.getIdPath()),"0")
    }
}
