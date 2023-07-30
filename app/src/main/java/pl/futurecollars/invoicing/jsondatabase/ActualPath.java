package pl.futurecollars.invoicing.jsondatabase;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Data;

@Data
public class ActualPath {

  private Path basePath = Paths.get("").toAbsolutePath();
  private String idPath = basePath.resolve("id.json").toString();
  private String databasePath = basePath.resolve("database.json").toString();
}
