package pl.futurecollars.invoicing.jsondatabase;

public class ActualPath {

  private String idPath = "https://github.com/kamilDzietczyk/invoicing-system-kamil-dzietczyk/blob/b9dc3861c50efd1016bbe8de041164cdc8dd6a5d/id.json";
  private String databasePath = "https://github.com/kamilDzietczyk/invoicing-system-kamil-dzietczyk/blob/b9dc3861c50efd1016bbe8de041164cdc8dd6a5d/databasefile.json";

  public String getIdPath(String name) {

    if (name.equals("id")) {
      return idPath;
    } else if (name.equals("database")) {
      return databasePath;
    }
    return null;
  }
}
