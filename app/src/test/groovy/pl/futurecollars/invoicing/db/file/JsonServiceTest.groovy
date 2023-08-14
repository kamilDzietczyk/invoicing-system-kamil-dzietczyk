package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class JsonServiceTest extends Specification{

    def "Convert object to json and read it back"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.createTestInvoice(12)

        when:
        def invoiceAsString = jsonService.convertToJson(invoice)

        and:
        def invoiceFromJson = jsonService.convertToObject(invoiceAsString, Invoice)

        then:
        invoice == invoiceFromJson
    }
}
