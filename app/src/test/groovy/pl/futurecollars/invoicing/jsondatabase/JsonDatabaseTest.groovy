package pl.futurecollars.invoicing.jsondatabase

import pl.futurecollars.invoicing.db.jsondatabase.ActualPath
import pl.futurecollars.invoicing.db.jsondatabase.FileService
import pl.futurecollars.invoicing.db.jsondatabase.GetActualId
import pl.futurecollars.invoicing.db.jsondatabase.JsonDatabase
import pl.futurecollars.invoicing.db.jsondatabase.JsonService
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class JsonDatabaseTest extends Specification{

    private static ActualPath actualPath;
    private static GetActualId getActualId;
    private static FileService fileService;
    private static JsonService jsonService;
    private static JsonDatabase jsonDatabase;
    private static File file;
    private static File fileid;
    private static Vat vat;

    def setupSpec() {
        actualPath = new ActualPath();
        getActualId = new GetActualId();
        fileService = new FileService();
        jsonService = new JsonService();
        jsonDatabase = new JsonDatabase();
        file = new File(actualPath.getIdPath("database"));
        fileid = new File(actualPath.getIdPath("id"));
        vat = Vat.Vat_7
        getActualId = new GetActualId();
    }

    def "Should save invoice"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);
        when:
        jsonDatabase.save(invoice);
        then:
        file.length()!= 0;
        cleanup:
        // nadpisanie pliku
        new FileOutputStream(file).close();
    }

    def "Should get by ID"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);
        Optional<Invoice> invoice2;
        jsonDatabase.save(invoice);
        when:
        invoice2 = jsonDatabase.getById(getActualId.getId());
        then:
        invoice2.get().getBuyer().getTaskIdentificationNumber() =="123456789";
        cleanup:
        /// wyczyszczenie pliku
        new FileOutputStream(file).close();
    }

    def "Should throw error when try get by invalid Id"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);
        Optional<Invoice> invoice2;
        jsonDatabase.save(invoice);
        when:
        invoice2 = jsonDatabase.getById(getActualId.getId()+1);
        then:
        // Sprawdzenie, czy wyjątek został wyrzucony i czy zawiera oczekiwany tekst
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("Nie znaleziono faktury o podanym ID: " + (getActualId.getId() + 1))

        cleanup:
        // wyczyszczenie pliku
        new FileOutputStream(file).close();
    }

    def "Should get all Invoice"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);

        Company buyer1 = new Company("123456789","address","buyer");
        Company seller1 = new Company("123456789","address","seller");
        InvoiceEntry inve1 = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list1 = new ArrayList<>();
        list.add(inve);
        Invoice invoice1 = new Invoice(0, LocalDate.now(), buyer1, seller1, list1);

        jsonDatabase.save(invoice);
        jsonDatabase.save(invoice1);
        List<Invoice> invoices;
        when:
        invoices = jsonDatabase.getAll();
        then:
        invoices.size() ==2;
        cleanup:
        // wyczyszczenie pliku
        new FileOutputStream(file).close();
    }

    def "Should update Invoice"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);

        Company buyer1 = new Company("000000","address","buyer");
        Company seller1 = new Company("000000","address","seller");
        InvoiceEntry inve1 = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list1 = new ArrayList<>();
        list.add(inve);
        Invoice invoice1 = new Invoice(0, LocalDate.now(), buyer1, seller1, list1);
        jsonDatabase.save(invoice);

        when:
        jsonDatabase.update(getActualId.getId(),invoice1)

        then:
        def Optional<Invoice> inv = jsonDatabase.getById(getActualId.getId())
        inv.get().getBuyer().getTaskIdentificationNumber() == "000000"

        cleanup:
        // wyczyszczenie pliku
        new FileOutputStream(file).close();
    }

    def "Should throw exception when try update invalid invoice"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);

        Company buyer1 = new Company("000000","address","buyer");
        Company seller1 = new Company("000000","address","seller");
        InvoiceEntry inve1 = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list1 = new ArrayList<>();
        list.add(inve);
        Invoice invoice1 = new Invoice(0, LocalDate.now(), buyer1, seller1, list1);
        jsonDatabase.save(invoice);

        when:
        jsonDatabase.update(getActualId.getId()+2,invoice1)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("Nie znaleziono faktury o podanym ID: " + (getActualId.getId() + 2))

        cleanup:
        // wyczyszczenie pliku
        new FileOutputStream(file).close();
    }

    def "Should delete Invoice"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);

        Company buyer1 = new Company("000000","address","buyer");
        Company seller1 = new Company("000000","address","seller");
        InvoiceEntry inve1 = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list1 = new ArrayList<>();
        list.add(inve1);
        Invoice invoice1 = new Invoice(0, LocalDate.now(), buyer1, seller1, list1);
        jsonDatabase.save(invoice);
        jsonDatabase.save(invoice1);
        when:
        jsonDatabase.delete(getActualId.getId()-1)

        then:
        def List<Invoice> tempInv = jsonDatabase.getAll();
        tempInv.size()==1

        cleanup:
        // wyczyszczenie pliku
        new FileOutputStream(file).close();
    }

    def "Should throw error when try delete not exists Invoice"() {
        given:
        Company buyer = new Company("123456789","address","buyer");
        Company seller = new Company("123456789","address","seller");
        InvoiceEntry inve = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list = new ArrayList<>();
        list.add(inve);
        Invoice invoice = new Invoice(0, LocalDate.now(), buyer, seller, list);

        Company buyer1 = new Company("000000","address","buyer");
        Company seller1 = new Company("000000","address","seller");
        InvoiceEntry inve1 = new InvoiceEntry("aaaaa",BigDecimal.TEN,BigDecimal.ONE, vat);
        List<InvoiceEntry> list1 = new ArrayList<>();
        list.add(inve1);
        Invoice invoice1 = new Invoice(0, LocalDate.now(), buyer1, seller1, list1);
        jsonDatabase.save(invoice);
        jsonDatabase.save(invoice1);
        when:
        jsonDatabase.delete(getActualId.getId()+123)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("Nie znaleziono faktury o podanym ID: " + (getActualId.getId() + 123))

        cleanup:
        // wyczyszczenie pliku
        new FileOutputStream(file).close();
    }
}
