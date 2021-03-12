package cosa.Ejercicios.XPath;

import java.util.Objects;

import javax.xml.transform.OutputKeys;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;

import cosa.Ejercicios.Leer;

public class OpsXPath {

  /*
   * Conexion para mi ordenador de casa
   * 
   * private static String URI =
   * "xmldb:exist://localhost:8080/exist/xmlrpc/db/Recursetes"; private static
   * String USER = "admin"; private static String PASSWORD = "admin";
   */

  private static String URI = "xmldb:exist://localhost:6969/exist/xmlrpc/db/pruebete";
  private static String USER = "admin";
  private static String PASSWORD = "";
  private static XPathQueryService xp;
  private static XQueryService xq;

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
      xp = Objects.requireNonNull((XPathQueryService) col.getService(XPathQueryService.SERVICE_NAME, null));
      xq = Objects.requireNonNull((XQueryService) col.getService(XQueryService.SERVICE_NAME, null));

      int opcion = 0;
      do {

        System.out.println("1 -> Obtén los nodos denominación y precio de todos los productos.\n"
            + "2 -> Obtén los nodos de los productos que sean placas base.\n"
            + "3 -> Obtén los nodos de los productos con precio mayor de 60€ y de la zona 20.\n"
            + "4 -> Obtén el número de productos que sean memorias y de la zona 10.\n"
            + "5 -> Obtén la media de precio de los micros.\n"
            + "6 -> Obtén los datos de los productos cuyo stock mínimo sea mayor que su stock actual.\n"
            + "7 -> Obtén el nombre del producto y el precio de aquellos cuyo stock mínimo sea mayor que su stock actual y sean de la zona 40.\n"
            + "8 -> Obtén el producto más caro.\n" + "9 -> Obtén el producto más barato de la zona 20.\n"
            + "10 -> Obtén el producto más caro de la zona 10.\n");

      } while (opcion != 0);

      switch (opcion = Leer.pedirEnteroValidar()) {
      case 1:
        // res = (XMLResource) col.getResource("productos.xml");

        ResourceIterator pit = xp.query("/productos/produc/*[self::denominacion or self::precio]").getIterator();

        while (pit.hasMoreResources()) {

          System.out.println();

          XMLResource denominacion = ((XMLResource) pit.nextResource());
          System.out.println(denominacion.getContent());

          if (pit.hasMoreResources()) {
            XMLResource precio = ((XMLResource) pit.nextResource());
            System.err.println(precio.getContent());
          }
        }

        break;
      case 2:

        break;

      case 3:

        break;

      case 4:

        break;

      case 5:

        break;

      case 6:

        break;
        
      case 7:

        break;

      case 8:

        break;

      case 9:

        break;

      case 10:

        break;

      default:
        break;
      }

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
