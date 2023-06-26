package pl.futurecollars.invoicing.jsondatabase;

public class ActualPath {

  private String idPath = "id.json";
  private String databasePath = "databasefile.json";

  public String getIdPath(String name) {

    if (name.equals("id")) {
      return idPath;
    } else if (name.equals("database")) {
      return databasePath;
    }
    return null;
  }
}
