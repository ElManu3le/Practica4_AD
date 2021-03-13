package cosa.Ejercicios.XQuery;

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
import org.xmldb.api.modules.XQueryService;

import cosa.Ejercicios.Leer;

public class OpsXQuery {

    // Conexion para mi ordenador de casa

    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/Recursetes";
    private static String USER = "admin";
    private static String PASSWORD = "admin";
    /*
     * private static String URI =
     * "xmldb:exist://localhost:6969/exist/xmlrpc/db/pruebete"; private static
     * String USER = "admin"; private static String PASSWORD = "";
     */
    
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
            xq = Objects.requireNonNull((XQueryService) col.getService(XQueryService.SERVICE_NAME, null));

            ResourceIterator pit = null;

            int opcion = 0;
            do {

                System.out.println("1 - Obtén por cada zona el número de productos que tiene.\n"
                        + "2 - Obtén la denominación de los productos entre las etiquetas <zona__></zona__> en funcion del codigo de la zona\n"
                        + "3 - Obtén por cada zona la denominación del o de los productos más caros.\n"
                        + "4 - Obten la denominacion de los productos clasificados <Placa>,<Memoria>,<Micro>,<Otros>\n"
                        + "5 - Devuelve el código de sucursal y el número de cuentas que tiene de tipo AHORRO y de tipo pensiones.\n"
                        + "6 - Devuelve por cada sucursal el código de sucursal, el director, la población, la suma del total saldodebe y la suma del total saldohaber de sus cuentas.\n"
                        + "7 - Devuelve el nombre de los directores, el código de sucursal y la población de las sucursales con más de 3 cuentas.\n"
                        + "8 - Devuelve por cada sucursal, el código de sucursal y los datos de las cuentas con más saldodebe.\n"
                        + "9 - Devuelve la cuenta del tipo PENSIONES que ha hecho más aportación.\n");

            } while (opcion != 0);

            switch (opcion = Leer.pedirEnteroValidar()) {

            case 1:
                pit = xq.query(
                        "for $v in distinct-values(/productos/produc/cod_zona) return ($v, count(/productos/produc[cod_zona = $v]))")
                        .getIterator();

                while (pit.hasMoreResources()) {
                    System.out.println();
                    XMLResource zone = ((XMLResource) pit.nextResource());
                    System.out.print("La zona " + zone.getContent());
                    if (pit.hasMoreResources()) {
                        XMLResource count = ((XMLResource) pit.nextResource());
                        System.out.println(" tiene " + count.getContent() + " productos \n");
                    }
                }

                break;
            case 2:

                pit = xq.query(
                        "for $v in distinct-values(/productos/produc/cod_zona) return element{ 'zona' || $v }{ /productos/produc[cod_zona = $v]/denominacion }")
                        .getIterator();
                while (pit.hasMoreResources()) {
                    System.out.println();
                    XMLResource nodo = ((XMLResource) pit.nextResource());
                    System.out.println(nodo.getContent());
                }

                break;

            case 3:

                pit = xq.query(
                        "for $v in distinct-values(/productos/produc/cod_zona) return ($v, /productos/produc[precio = max(/productos/produc[cod_zona = $v]/precio)]/denominacion/text())")
                        .getIterator();
                while (pit.hasMoreResources()) {
                    System.out.println();
                    XMLResource nodo = ((XMLResource) pit.nextResource());
                    System.out.print("En la zona " + nodo.getContent());
                    if (pit.hasMoreResources()) {
                        XMLResource den = ((XMLResource) pit.nextResource());
                        System.out.println(", el producto más caro es " + den.getContent());
                    }
                }

                break;

            case 4:

                pit = xq.query("(<placa>{/productos/produc/denominacion[contains(., 'Placa Base')]}</placa>,"
                        + "<micro>{/productos/produc/denominacion[contains(., 'Micro')]}</micro>,"
                        + "<memoria>{/productos/produc/denominacion[contains(., 'Memoria')]}</memoria>,"
                        + "<otros>{/productos/produc/denominacion[not(contains(., 'Memoria') or contains(., 'Micro') or contains(., 'Placa Base'))]}</otros>)")
                        .getIterator();
                while (pit.hasMoreResources()) {
                    System.out.println();
                    XMLResource nodo = ((XMLResource) pit.nextResource());
                    System.out.println(nodo.getContent());
                }

                break;

            case 5:
                pit = xq.query(
                        "for $suc in /sucursales/sucursal return (data($suc/@codigo), count($suc/cuenta[data(@tipo)='AHORRO']), count($suc/cuenta[data(@tipo)='PENSIONES']))")
                        .getIterator();
                while (pit.hasMoreResources()) {
                    System.out.println();
                    XMLResource nodo = ((XMLResource) pit.nextResource());
                    System.out.print("La sucursal " + nodo.getContent());

                    if (pit.hasMoreResources()) {
                        XMLResource n2 = (XMLResource) pit.nextResource();
                        System.out.print(" tiene " + n2.getContent() + " cuentas tipo AHORRO y ");
                    }

                    if (pit.hasMoreResources()) {
                        XMLResource n2 = (XMLResource) pit.nextResource();
                        System.out.println(n2.getContent() + " cuentas tipo PENSIONES");
                    }
                }

                break;

            case 6:
                pit = xq.query(
                        "for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text(), sum($suc/cuenta/saldodebe), sum($suc/cuenta/saldohaber))")
                        .getIterator();

                while (pit.hasMoreResources()) {
                    System.out.println();

                    res = (XMLResource) pit.nextResource();
                    System.out.println("Código: " + res.getContent());

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Director: " + res.getContent());
                    }

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Población: " + res.getContent());
                    }

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Total saldodebe: " + res.getContent());
                    }

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Total saldohaber: " + res.getContent());
                    }
                }

                break;

            case 7:
                pit = xq.query(
                        "for $suc in /sucursales/sucursal[count(cuenta) > 3] return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text())")
                        .getIterator();

                while (pit.hasMoreResources()) {
                    System.out.println();

                    res = (XMLResource) pit.nextResource();
                    System.out.println("Código: " + res.getContent());

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Director: " + res.getContent());
                    }

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Población: " + res.getContent());
                    }
                }

                break;

            case 8:
                pit = xq.query(
                        "for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/cuenta[saldodebe = max($suc/cuenta/saldodebe)]/*/text())")
                        .getIterator();

                while (pit.hasMoreResources()) {
                    System.out.println();

                    res = (XMLResource) pit.nextResource();
                    System.out.println("Cuenta con más saldodebe de la sucursal " + res.getContent());

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Nombre: " + res.getContent());
                    }

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Número: " + res.getContent());
                    }

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Saldohaber: " + res.getContent());
                    }

                    if (pit.hasMoreResources()) {
                        res = (XMLResource) pit.nextResource();
                        System.out.println("Saldodebe: " + res.getContent());
                    }
                }

                break;

            case 9:
                pit = xq.query(
                        "/sucursales/sucursal/cuenta[data(@tipo) = 'PENSIONES' and aportacion = max(/sucursales/sucursal/cuenta/aportacion)]")
                        .getIterator();

                while (pit.hasMoreResources()) {
                    System.out.println();

                    res = (XMLResource) pit.nextResource();
                    System.out.println(res.getContent());
                }

                break;

            }

        } catch (

        ClassNotFoundException e) {
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
