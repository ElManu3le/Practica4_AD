package cosa.Ejercicios.XPath;

import java.util.Objects;

import javax.xml.transform.OutputKeys;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import cosa.Ejercicios.Leer;

public class OpsXPath {

  

  private static String URI = "xmldb:exist://localhost:6969/exist/xmlrpc/db/pruebete";
  private static String USER = "admin";
  private static String PASSWORD = "";
  /* Conexion para Ordenador Clase

   * private static String URI = "xmldb:exist://localhost:6969/exist/xmlrpc/db/pruebete"; 
   * private static String USER = "admin"; 
   * private static String PASSWORD = "";
   */

   /* Conexion para ordenador de casa
   
   * private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/Recursetes"; 
   * private static String USER = "admin"; 
   * private static String PASSWORD = "admin";
   */
  private static XPathQueryService xp;

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

      ResourceIterator pit = null;

      int opcion = 0;
      do {

        System.out.println("1 - Obtén los nodos denominación y precio de todos los productos.\n"
            + "2 - Obtén los nodos de los productos que sean placas base.\n"
            + "3 - Obtén los nodos de los productos con precio mayor de 60€ y de la zona 20.\n"
            + "4 - Obtén el número de productos que sean memorias y de la zona 10.\n"
            + "5 - Obtén la media de precio de los micros.\n"
            + "6 - Obtén los datos de los productos cuyo stock mínimo sea mayor que su stock actual.\n"
            + "7 - Obtén el nombre del producto y el precio de aquellos cuyo stock mínimo sea mayor que su stock actual y sean de la zona 40.\n"
            + "8 - Obtén el producto más caro.\n" + "9 -> Obtén el producto más barato de la zona 20.\n"
            + "10 - Obtén el producto más caro de la zona 10.\n");

      } while (opcion != 0);

      switch (opcion = Leer.pedirEnteroValidar()) {

      case 1:
        // res = (XMLResource) col.getResource("productos.xml");

        pit = xp.query("/productos/produc/*[self::denominacion or self::precio]").getIterator();

        while (pit.hasMoreResources()) {

          XMLResource denominacion = ((XMLResource) pit.nextResource());
          System.out.println(denominacion.getContent());

          if (pit.hasMoreResources()) {
            XMLResource precio = ((XMLResource) pit.nextResource());
            System.err.println(precio.getContent() + "\n");
          }
        }

        break;
      case 2:

        pit = xp.query("/productos/produc[denominacion[contains(., 'Placa Base')]]").getIterator();

        while (pit.hasMoreResources()) {

          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println(nodo.getContent() + "\n");
        }

        break;

      case 3:

        pit = xp.query("/productos/produc[precio[text() > 60] and cod_zona[text() = 20]]").getIterator();

        while (pit.hasMoreResources()) {

          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println(nodo.getContent() + "\n");
        }

        break;

      case 4:

        pit = xp.query("count(/productos/produc[denominacion[contains(., 'Memoria')] and cod_zona[text() = 10]])")
            .getIterator();

        while (pit.hasMoreResources()) {
          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println("Hay " + nodo.getContent() + " productos denominados 'Memoria' " + "\n");
        }

        break;

      case 5:
        pit = xp.query(
            "sum(/productos/produc[denominacion[contains(., 'Micro')]]/precio/text()) div count(/productos/produc[denominacion[contains(., 'Micro')]])")
            .getIterator();

        while (pit.hasMoreResources()) {
          System.out.println();
          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println("La media de los precios en los microprocesadores es " + nodo.getContent() + "\n");
        }

        break;

      case 6:
        pit = xp.query("/productos/produc[number(stock_minimo) > number(stock_actual)]").getIterator();

        while (pit.hasMoreResources()) {
          System.out.println();
          XMLResource nodo = ((XMLResource) pit.nextResource());

          NodeList nodosProduc = nodo.getContentAsDOM().getChildNodes();

          for (int i = 0; i < nodosProduc.getLength(); ++i) {
            Node n = nodosProduc.item(i);
            if (n.getLocalName() != null) {
              System.out.println(n.getLocalName() + " = " + n.getTextContent() + "\n");
            }
          }
        }

        break;

      case 7:
        pit = xp.query(
            "/productos/produc/*[(self::denominacion or self::precio) and number(../stock_minimo/text()) > number(../stock_actual/text()) and ../cod_zona/text() = 40]/text()")
            .getIterator();

        while (pit.hasMoreResources()) {
          System.out.println();
          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println("nombre = " + nodo.getContent());

          if (pit.hasMoreResources()) {
            XMLResource n2 = ((XMLResource) pit.nextResource());
            System.out.println("precio = " + n2.getContent() + "\n");
          }
        }

        break;

      case 8:
        pit = xp.query("/productos/produc[precio = max(/productos/produc/precio)]").getIterator();
        while (pit.hasMoreResources()) {
          System.out.println();
          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println(nodo.getContent() + "\n");
        }

        break;

      case 9:
        pit = xp.query("/productos/produc[precio = min(/productos/produc[cod_zona = 20]/precio)]").getIterator();
        while (pit.hasMoreResources()) {
          System.out.println();
          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println(nodo.getContent() + "\n");
        }

        break;

      case 10:
        pit = xp.query("/productos/produc[precio = max(/productos/produc[cod_zona = 10]/precio)]").getIterator();
        while (pit.hasMoreResources()) {
          System.out.println();
          XMLResource nodo = ((XMLResource) pit.nextResource());
          System.out.println(nodo.getContent() + "\n");
        }

        break;

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
