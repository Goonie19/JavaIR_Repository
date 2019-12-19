package riSystem;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class RISystem  {

    private static Scanner _reader;
    public static void main(String[] args) {
        _reader = new Scanner(System.in);
        System.out.println("Muy buenas! Bienvenido a RISystem!\n--------------------------------------------------------");
        int iOperation;
        boolean exit = false;

        while(!exit) {
            System.out.println("¿Qué operación desea realizar?\n 0. Salir!\n1. Consulta");
            iOperation = _reader.nextInt();
            switch(iOperation) {
                case 0: {

                    exit = true;
                }; break;
                case 1: {

                    System.out.println("No está implementado aún");
                }; break;
                default: System.out.println("Eso no es una opción!");break;
            }
        }
    }
}
