package pl.futurecollars.invoicing.jsondatabase;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ActualPath {

  private String idPath = "id.json";
  private String databasePath = "database.json";
  private Path basePath = Paths.get("").toAbsolutePath();

  public String getIdPath(String name) {

    if (name.equals("id")) {
      return basePath.resolve(idPath).toString();
    } else if (name.equals("database")) {
      return basePath.resolve(databasePath).toString();
    }
    return null;
  }
}
