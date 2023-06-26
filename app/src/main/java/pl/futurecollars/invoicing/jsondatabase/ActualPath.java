package pl.futurecollars.invoicing.jsondatabase;

public class ActualPath {

  private String idPath = "///home/runner/work/invoicing-system-kamil-dzietczyk/invoicing-system-kamil-dzietczyk/id.json";
  private String databasePath = "///home/runner/work/invoicing-system-kamil-dzietczyk/invoicing-system-kamil-dzietczyk/databasefile.json";

  public String getIdPath(String name) {

    if (name.equals("id")) {
      return idPath;
    } else if (name.equals("database")) {
      return databasePath;
    }
    return null;
  }
}
