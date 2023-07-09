/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
package pl.futurecollars.invoicing

import spock.lang.Specification

class AppTest extends Specification {
    def "application has a greeting"() {
        setup:
        def app = new App()

        when:
        def result = app.greeting

        then:
        result != null
    }

    def "Test enum values"() {
        when:
        def vatValues = Vat.values()

        then:
        vatValues.size() == 6
        vatValues[0].name() == "Vat_21"
        vatValues[0].rate == BigDecimal.valueOf(21)
        vatValues[1].name() == "Vat_8"
        vatValues[1].rate == BigDecimal.valueOf(8)
        vatValues[2].name() == "Vat_7"
        vatValues[2].rate == BigDecimal.valueOf(7)
        vatValues[3].name() == "Vat_5"
        vatValues[3].rate == BigDecimal.valueOf(5)
        vatValues[4].name() == "Vat_0"
        vatValues[4].rate == BigDecimal.valueOf(0)
        vatValues[5].name() == "Vat_ZW"
        vatValues[5].rate == BigDecimal.valueOf(0)
    }

    def "should print correct greeting"() {
        given:
        def outputStream = new ByteArrayOutputStream()
        System.setOut(new PrintStream(outputStream))

        when:
        App.main(new String[0])

        then:
        String output = outputStream.toString().trim()
        output == "Hello World!"
    }

    def "should print correct greeting"() {
        given:
        def outputStream = new ByteArrayOutputStream()
        System.setOut(new PrintStream(outputStream))

        when:
        App.main(new String[0])

        then:
        String output = outputStream.toString().trim()
        output == "Hello World!"
    }
}
