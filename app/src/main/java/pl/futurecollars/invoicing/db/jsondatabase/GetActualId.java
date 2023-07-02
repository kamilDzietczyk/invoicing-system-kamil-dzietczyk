package pl.futurecollars.invoicing.db.jsondatabase;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class GetActualId {

  ObjectMapper objecMapper = new ObjectMapper();
  ActualPath actualPath = new ActualPath();

  public int getId() throws IOException {
    ActualId actualId = objecMapper.readValue(new File(actualPath.getIdPath("id")), ActualId.class);
    return actualId.getId();
  }

  public void updateId() throws IOException {
    int actIid = getId();
    actIid++;
    objecMapper.writeValue(new File(actualPath.getIdPath("id")), actIid);
  }

}
