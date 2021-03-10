package cosa.Ejercicios.XPath;

import javax.xml.transform.OutputKeys;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

public class OpsXPath {

    private static String URI = "xmldb:exist://localhost:6969/exist/xmlrpc/db/pruebete";
    private static String USER = "admin";
    private static String PASSWORD = "";
  
    public static void main(String[] args) {
      final String driver = "org.exist.xmldb.DatabaseImpl";
  
      // initialize database driver
      Class cl = null;
      try {
        cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);
  
        Collection col = null;
        XMLResource res = null;
  
        // get the collection
        col = DatabaseManager.getCollection(URI, USER, PASSWORD);
        col.setProperty(OutputKeys.INDENT, "no");
        res = (XMLResource) col.getResource("productos.xml");
  
        if (res == null) {
          System.out.println("document not found!");
        } else {
          System.out.println(res.getContent());
        }
  
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (XMLDBException e) {
        e.printStackTrace();
      }
    }
}
