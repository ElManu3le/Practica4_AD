package cosa.Ejercicios.XPath;

import java.util.Objects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XQueryService;

import cosa.Ejercicios.Leer;

public class pene 
{
    private static Database db;
    private static XPathQueryService xpath;
    private static XQueryService xquery;

/* LA MOVIDA QUE ME FUNCIONÓ A MI :)
 * ResourceIterator ri = xpath.query("/productos/produc[precio=50]/stock_actual/text()").getIterator();
 * while (ri.hasMoreResources()) {
 *     XMLResource res = ((XMLResource) ri.nextResource());
 *     System.out.println(res.getContent());
 * }
 */

    @SuppressWarnings("deprecation")
    public static void main( String[] args ) throws Exception
    {
        db = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
        Collection col = Objects.requireNonNull(db.getCollection("xmldb:exist://localhost:8080/exist/xmlrpc/db/Recursetes", "admin", "admin"));
        xpath = Objects.requireNonNull((XPathQueryService) col.getService(XPathQueryService.SERVICE_NAME, null));
        xquery = Objects.requireNonNull((XQueryService) col.getService(XQueryService.SERVICE_NAME, null));

        int orden = 0;
        do {
            imprMenu();
            orden = Leer.pedirEnteroValidar();
            acatar(orden);
        } while (orden != 0);
    }

    // Imprime el menú
    private static void imprMenu() {
        System.out.println(
                 
                "\n XQuery\n ------\n"
                + "11 -> Obtener por cada zona el número de productos que tiene.\n"
                + "12 -> Obtener la denominación de los productos entre las etiquetas <zona_>, donde _ es el número de zona.\n"
                + "13 -> Obtener por cada zona la denominación del o de los productos más caros.\n"
                + "14 -> Obtener la denominación de los productos contenida entra las etiquetas <placa>, <micro>, <memoria> u <otros>.\n"
                + "15 -> Devuelve el código de sucursal y el número de cuentas que tiene de tipo AHORRO y de tipo PENSIONES.\n"
                + "16 -> Devuelve por cada sucursal el código de sucursal, el director, la población, la suma del total saldodebe y la suma del total saldohaber de sus cuentas.\n"
                + "17 -> Devuelve el nombre de los directores, el código de sucursal y la población de las sucursales con más de 3 cuentas.\n"
                + "18 -> Devuelve por cada sucursal, el código de sucursal y los datos de las cuentas con más saldodebe.\n"
                + "19 -> Devuelve la cuenta del tipo PENSIONES que ha hecho más aportación.\n"
        );
    }

    // llama a la funcion que corresponda
    private static void acatar(int orden) throws XMLDBException {
        switch (orden) {
            case 0: default: return;

            // XQuery (productos)
            case 11: xquery_prods_1(); break;
            case 12: xquery_prods_2(); break;
            case 13: xquery_prods_3(); break;
            case 14: xquery_prods_4(); break;

            // XQuery (sucursales)
            case 15: xquery_sucur_1(); break;
            case 16: xquery_sucur_2(); break;
            case 17: xquery_sucur_3(); break;
            case 18: xquery_sucur_4(); break;
            case 19: xquery_sucur_5(); break;
        }
    }

    /**
     * Devuelve la cuenta del tipo PENSIONES que ha hecho más aportación.
     * @throws XMLDBException
     */
    private static void xquery_sucur_5() throws XMLDBException {
        ResourceIterator ri = xquery.query("/sucursales/sucursal/cuenta[data(@tipo) = 'PENSIONES' and aportacion = max(/sucursales/sucursal/cuenta/aportacion)]").getIterator();

        while (ri.hasMoreResources()) {
            System.out.println();

            XMLResource res = (XMLResource) ri.nextResource();
            System.out.println(res.getContent());
        }
    }

    /**
     * Devuelve por cada sucursal, el código de sucursal y los datos de las cuentas con más saldodebe.
     * @throws XMLDBException
     */
    private static void xquery_sucur_4() throws XMLDBException {
        ResourceIterator ri = xquery.query("for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/cuenta[saldodebe = max($suc/cuenta/saldodebe)]/*/text())").getIterator();

        while (ri.hasMoreResources()) {
            System.out.println();

            XMLResource res = (XMLResource) ri.nextResource();
            System.out.println("Cuenta con más saldodebe de la sucursal " + res.getContent());

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Nombre: " + res.getContent());
            }

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Número: " + res.getContent());
            }

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Saldohaber: " + res.getContent());
            }

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Saldodebe: " + res.getContent());
            }
        }
    }

    /**
     * Devuelve el nombre de los directores, el código de sucursal y
     * la población de las sucursales con más de 3 cuentas.
     * @throws XMLDBException
     */
    private static void xquery_sucur_3() throws XMLDBException {
        ResourceIterator ri = xquery.query("for $suc in /sucursales/sucursal[count(cuenta) > 3] return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text())").getIterator();

        while (ri.hasMoreResources()) {
            System.out.println();

            XMLResource res = (XMLResource) ri.nextResource();
            System.out.println("Código: " + res.getContent());

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Director: " + res.getContent());
            }

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Población: " + res.getContent());
            }
        }
    }

    /**
     * Devuelve por cada sucursal el código de sucursal, el director, la población, la suma
     * del total saldodebe y la suma del total saldohaber de sus cuentas.
     * @throws XMLDBException
     */
    private static void xquery_sucur_2() throws XMLDBException {
        ResourceIterator ri = xquery.query("for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text(), sum($suc/cuenta/saldodebe), sum($suc/cuenta/saldohaber))").getIterator();

        while (ri.hasMoreResources()) {
            System.out.println();

            XMLResource res = (XMLResource) ri.nextResource();
            System.out.println("Código: " + res.getContent());

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Director: " + res.getContent());
            }

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Población: " + res.getContent());
            }

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Total saldodebe: " + res.getContent());
            }

            if (ri.hasMoreResources()) {
                res = (XMLResource) ri.nextResource();
                System.out.println("Total saldohaber: " + res.getContent());
            }
        }

    }

    /**
     * Devuelve el código de sucursal y el número de cuentas que tiene de tipo AHORRO
     * y de tipo PENSIONES.
     * @throws XMLDBException
     */
    private static void xquery_sucur_1() throws XMLDBException {
        ResourceIterator ri = xquery.query("for $suc in /sucursales/sucursal return (data($suc/@codigo), count($suc/cuenta[data(@tipo)='AHORRO']), count($suc/cuenta[data(@tipo)='PENSIONES']))").getIterator();
        while (ri.hasMoreResources()) {
            System.out.println();
            XMLResource nodo = ((XMLResource) ri.nextResource());
            System.out.print("La sucursal " + nodo.getContent());

            if (ri.hasMoreResources()) {
                XMLResource n2 = (XMLResource) ri.nextResource();
                System.out.print(" tiene " + n2.getContent() + " cuentas tipo AHORRO y ");
            }

            if (ri.hasMoreResources()) {
                XMLResource n2 = (XMLResource) ri.nextResource();
                System.out.println(n2.getContent() + " cuentas tipo PENSIONES");
            }
        }
    }

    /**
     * Obtener la denominación de los productos contenida entra las etiquetas
     * <placa></placa> para los productos en cuya denominación aparece la palabra
     * Placa Base, <memoria></memoria>, para los que contienen la palabra Memoria
     * <micro></micro>, para los que contienen la palabra Micro y <otros></otros>
     * para el resto de productos.
     * 
     * @throws XMLDBException
     */
    private static void xquery_prods_4() throws XMLDBException {
        ResourceIterator ri = xquery.query("(<placa>{/productos/produc/denominacion[contains(., 'Placa Base')]}</placa>,"
                                        + "<micro>{/productos/produc/denominacion[contains(., 'Micro')]}</micro>,"
                                        + "<memoria>{/productos/produc/denominacion[contains(., 'Memoria')]}</memoria>,"
                                        + "<otros>{/productos/produc/denominacion[not(contains(., 'Memoria') or contains(., 'Micro') or contains(., 'Placa Base'))]}</otros>)").getIterator();
        while (ri.hasMoreResources()) {
            System.out.println();
            XMLResource nodo = ((XMLResource) ri.nextResource());
            System.out.println(nodo.getContent());
        }
    }

    /**
     * Obtener por cada zona la denominación del o de los productos más caros.
     * 
     * @throws XMLDBException
     */
    private static void xquery_prods_3() throws XMLDBException {
        ResourceIterator ri = xquery.query("for $v in distinct-values(/productos/produc/cod_zona) return ($v, /productos/produc[precio = max(/productos/produc[cod_zona = $v]/precio)]/denominacion/text())").getIterator();
        while (ri.hasMoreResources()) {
            System.out.println();
            XMLResource nodo = ((XMLResource) ri.nextResource());
            System.out.print("En la zona " + nodo.getContent());
            if (ri.hasMoreResources()) {
                XMLResource den = ((XMLResource) ri.nextResource());
                System.out.println(", el producto más caro es " + den.getContent());
            }
        }
    }

    /**
     * Obtener la denominación de los productos entra las etiquetas <zona10></zona10>
     * si son del código de zona 10, <zona20></zona20> si son del código de zona 20,
     * <zona30></zona30> si son del código de zona 30 y <zona40></zona40> si son del
     * código de zona 40.
     * 
     * @throws XMLDBException
     */
    private static void xquery_prods_2() throws XMLDBException {
        ResourceIterator ri = xquery.query("for $v in distinct-values(/productos/produc/cod_zona) return element{ 'zona' || $v }{ /productos/produc[cod_zona = $v]/denominacion }").getIterator();
        while (ri.hasMoreResources()) {
            System.out.println();
            XMLResource nodo = ((XMLResource) ri.nextResource());
            System.out.println(nodo.getContent());
        }
    }

    /**
     * Obtener por cada zona el número de productos que tiene.
     * @throws XMLDBException
     */
    private static void xquery_prods_1() throws XMLDBException {
        ResourceIterator ri = xquery.query("for $v in distinct-values(/productos/produc/cod_zona) return ($v, count(/productos/produc[cod_zona = $v]))").getIterator();

        while (ri.hasMoreResources()) {
            System.out.println();
            XMLResource zona = ((XMLResource) ri.nextResource());
            System.out.print("La zona " + zona.getContent());
            if (ri.hasMoreResources()) {
                XMLResource count = ((XMLResource) ri.nextResource());
                System.out.println(" tiene " + count.getContent() + " productos");
            }
        }
    }

    

}