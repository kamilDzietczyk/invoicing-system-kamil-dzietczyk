package pl.futurecollars.invoicing.db.file

import spock.lang.Shared
import spock.lang.Specification
import java.nio.file.Path

class FileServiceTest extends Specification{

    @Shared FileService fileService
    @Shared file

    void setup() {
        fileService = new FileService()
        file = new File(ActualPath.databasePath)
    }

    def "Correctly appended line to file"() {
        given:
        file.createNewFile();
        def testLine = "Test line to write"

        when:
        fileService.appendLineToFile(Path.of(ActualPath.databasePath),testLine)
        fileService.appendLineToFile(Path.of(ActualPath.databasePath), testLine)

        then:
        [testLine, testLine] == fileService.readAllLines(Path.of(ActualPath.databasePath))

        cleanup:
        file.delete()
    }
    def "Correctly add line to file"() {
        given:
        file.createNewFile();
        def testLine = "A"

        when:
        fileService.writeToFile(Path.of(ActualPath.databasePath),testLine)

        then:
        ["A"] == fileService.readAllLines(Path.of(ActualPath.databasePath))

        cleanup:
        file.delete()
    }
    def "correctly written list of lines to file"() {
        given:
        file.createNewFile();
        def digits = ['1', '2', '3']

        when:
        fileService.writeLinesToFile(Path.of(ActualPath.databasePath), digits)

        then:
        digits == fileService.readAllLines(Path.of(ActualPath.databasePath))

        cleanup:
        file.delete()
    }
    def "Should return empty val"() {
        given:
        file.createNewFile();

        expect:
        [] == fileService.readAllLines(Path.of(ActualPath.databasePath))

        cleanup:
        file.delete()
    }
}
