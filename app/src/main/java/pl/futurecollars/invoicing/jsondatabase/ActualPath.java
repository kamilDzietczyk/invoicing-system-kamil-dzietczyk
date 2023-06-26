package pl.futurecollars.invoicing.jsondatabase;

public class ActualPath {

  private String idPath = "E:\\Task_3\\id.json";
  private String databasePath = "E:\\Task_3\\databasefile.json";

  public String getIdPath(String name) {

    if (name.equals("id")) {
      return idPath;
    } else if (name.equals("database")) {
      return databasePath;
    }
    return null;
  }
}
