package cosa.Ejercicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Leer {

    static public String pedirCadena() { 
		BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));
		String dato="";
		boolean error = true;
		while (error) {
			try {
				dato = dataIn.readLine();
				error=false;
			} catch (IOException e) {
				System.out.println("Vuelve a inroducir el dato, por favor: ");
				error = true;
			}
		}
		return dato;
	}
	
	static public int pedirEnteroValidar() {
		BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));
		int dato=0;
		boolean error = true;
		while (error) {
			try {
				dato = Integer.parseInt(dataIn.readLine());
				error=false;
			} catch (IOException e) {
				System.out.println("Vuelve a inroducir el dato, por favor");
				error = true;
			} catch(NumberFormatException e){
				System.out.println("El dato introducido no es entero");
				System.out.println("Vuelve a inroducir el dato, por favor: ");
				error=true;
			}
		}
		return dato;

	}
    
}
