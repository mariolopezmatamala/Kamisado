package juego.textui;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.BACK_COLOR;
import static com.diogonunes.jcolor.Attribute.BLACK_BACK;
import static com.diogonunes.jcolor.Attribute.BLACK_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_BLUE_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_BLUE_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_GREEN_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_GREEN_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_MAGENTA_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_MAGENTA_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_RED_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_RED_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_WHITE_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_WHITE_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_YELLOW_BACK;
import static com.diogonunes.jcolor.Attribute.TEXT_COLOR;

import java.util.Scanner;

import com.diogonunes.jcolor.Attribute;

import juego.control.Arbitro;
import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.modelo.Turno;

/**
 * Kamisado en modo texto.
 * 
 * Se abusa del uso de static tanto en atributos como en métodos para comprobar
 * su similitud a variables globales y funciones globales de otros lenguajes.
 *
 * @author <a href="rmartico@ubu.es">Raúl Marticorena</a>
 * @author mario lopez matamala
 * @since jdk 16
 * @version 2.1
 */
public class Kamisado {

	/** Tamaño en caracteres de una jugada. */
	private static final int TAMAÑO_JUGADA = 4;

	/**
	 * Tablero.
	 */
	private static Tablero tablero;

	/**
	 * Arbitro.
	 */
	private static Arbitro arbitro;

	/**
	 * Lector por teclado.
	 */
	private static Scanner scanner;

	/**
	 * Método raíz.
	 * 
	 * @param args argumentos de entrada
	 */
	public static void main(String[] args) {
		inicializarPartida();
		mostrarMensajeBienvenida();

		String texto;

		int modoTablero = 1;

		do {// bucle que se ejecuta hasta que se termine la partida
			if (modoTablero == 1) {
				mostrarTableroEnPantalla();
			} else {
				mostrarTableroEnFormatoTexto();
			}

			do {// bucle que valida el formato
				texto = recogerJugada();
				if (texto.equalsIgnoreCase("texto")) {
					System.out.println("Mostrando tablero en modo texto");
					mostrarTableroEnFormatoTexto();
					modoTablero = 2;

				}
				if (texto.equalsIgnoreCase("salir")) {
					mostrarInterrupcionPartida();
					System.exit(0);
				}
				if (!validarFormato(texto) && !texto.equalsIgnoreCase("texto")) {
					mostrarErrorEnFormatoDeEntrada();
				}
			} while (!validarFormato(texto));

			realizarJugada(texto);
			mostrarInformacionUltimoMovimiento();
		} while (!comprobarSiFinalizaPartida());

		if (modoTablero == 1) {
			mostrarTableroEnPantalla();
		} else {
			mostrarTableroEnFormatoTexto();
		}
		finalizarPartida();
	}

	/**
	 * Muestra el mensaje de interrupción de partida.
	 */
	private static void mostrarInterrupcionPartida() {
		System.out.println("Interrumpida la partida, se concluye el juego.");
	}

	/**
	 * Muestra el estado actual del tablero en formato texto. Utilidad si hay
	 * problemas con la visualización gráfica o con usuarios con daltonismo.
	 */
	private static void mostrarTableroEnFormatoTexto() {
		System.out.println();
		System.out.println(tablero.toString());
	}

	/**
	 * Realiza la jugada introducida por teclado realizando las correspondientes
	 * comprobaciones relativas a las reglas del juego. Se supone que la jugada en
	 * cuanto al formato ya ha sido validada previamente.
	 * 
	 * @param jugada jugada
	 */
	private static void realizarJugada(String jugada) {
		Celda origen = leerOrigen(jugada);
		Celda destino = leerDestino(jugada);

		if (arbitro.esMovimientoLegalConTurnoActual(origen, destino)) { // si el movimiento es legal
			Turno turno = arbitro.obtenerTurno();
			arbitro.moverConTurnoActual(origen, destino);// mueve
			if (!arbitro.estaAlcanzadaUltimaFilaPor(turno)) {// si no esta alcanzada la ultima fila por quien ha movido
				if (arbitro.estaBloqueadoTurnoActual()) {// si esta bloqueado el mismo turno
					if (destino != null) {
						System.out.printf("La torre de turno %s y color %s está bloqueada y pierde turno.%n",
								arbitro.obtenerTurno(), destino.obtenerColor());
					}
					System.out.println("Se realiza un movimiento de distancia cero y se pierde el turno.\n");
					arbitro.moverConTurnoActualBloqueado();
				}
			}
		} else {
			System.out.println("Movimiento ilegal.");
		}
	}

	/**
	 * Muestra el mensaje de bienvenida con instrucciones para finalizar la partida.
	 */
	private static void mostrarMensajeBienvenida() {
		System.out.println("Bienvenido al juego del Kamisado");
		System.out.println("Para interrumpir partida introduzca \"salir\".");
		System.out.println("Para mostrar el estado del tablero en formato texto introduzca \"texto\".");
		System.out.println("Disfrute de la partida simple...");
	}

	/**
	 * Mostrar al usuario información de error en el formato de entrada, mostrando
	 * ejemplos.
	 */
	private static void mostrarErrorEnFormatoDeEntrada() {
		System.out.println();
		System.out.println("Error en el formato de entrada.");
		System.out.println("El formato debe ser letranumeroletranumero, por ejemplo a7a5 o g1f3");
		System.out.println("Las letras deben estar en el rango [a,h] y los números en el rango [1,8]\n");
	}

	/**
	 * Comprueba si la partida está finalizada.
	 * 
	 * @return true se ha finalizado la partida, false en caso contrario
	 */
	private static boolean comprobarSiFinalizaPartida() {
		return arbitro.estaAlcanzadaUltimaFilaPor(Turno.BLANCO) || arbitro.estaAlcanzadaUltimaFilaPor(Turno.NEGRO)
				|| arbitro.hayBloqueoMutuo();
	}

	/**
	 * Finaliza la partida informando al usuario y cerrando recursos abiertos.
	 */
	private static void finalizarPartida() {
		if (arbitro.hayBloqueoMutuo()) {
			System.out.println("Situacion de bloqueo mutuo.");
			System.out.printf("Ganada la partida por el jugador con turno %s,%n", arbitro.consultarGanador());
			System.out.println("porque no ha provocado el bloqueo.");
		} else {
			System.out.printf("Ganada la partida por el jugador con turno %s alcanzando la fila de salida contraria.%n",
					arbitro.consultarGanador());
		}
		scanner.close();
	}

	/**
	 * Muestra informacion del último movimiento para ambos turnos.
	 */
	private static void mostrarInformacionUltimoMovimiento() {
		Color color = null;
		System.out.println();

		color = arbitro.obtenerUltimoMovimiento(Turno.NEGRO);
		imprimirColorUltimoTurno("negro", BRIGHT_WHITE_TEXT(), color);

		color = arbitro.obtenerUltimoMovimiento(Turno.BLANCO);
		imprimirColorUltimoTurno("blanco", BLACK_TEXT(), color);

		System.out.println();
	}

	/**
	 * Imprime el color de celda del último turno.
	 * 
	 * @param textoTurno texto del turno
	 * @param colorTexto color del texto
	 * @param color      color
	 */
	private static void imprimirColorUltimoTurno(String textoTurno, Attribute colorTexto, Color color) {
		if (color != null) {
			System.out.print("Último color de turno " + textoTurno + ":");
			Attribute colorFondo = elegirColorFondo(color);
			System.out.print(colorize(color.toString(), colorTexto, colorFondo));
			System.out.println(" ");
		}
	}

	/**
	 * Muestra el tablero en pantalla con su estado actual.
	 */
	private static void mostrarTableroEnPantalla() {
		System.out.print("  ");
		for (int col = 0; col < tablero.obtenerNumeroColumnas(); col++) {
			char c = (char) (col + 'a');
			System.out.print("   " + c + "  ");
		}
		System.out.println();
		for (int fila = 0; fila < tablero.obtenerNumeroFilas(); fila++) {
			mostrarFilaDeTableroEnPantalla(fila);
		}
	}

	/**
	 * Muestra la fila correspondiente en pantalla.
	 * 
	 * @param fila fila
	 */
	private static void mostrarFilaDeTableroEnPantalla(int fila) {
		for (int contador = 0; contador < 3; contador++) {
			if (contador == 1) {
				System.out.print((tablero.obtenerNumeroFilas() - fila) + " ");
			} else {
				System.out.print("  ");
			}
			for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
				Celda celda = tablero.obtenerCelda(fila, j);
				if (contador == 1 && !celda.estaVacia()) {
					mostrarLineaColorCeldaConTorre(celda);
				} else {
					mostrarLineaColor(celda.obtenerColor());
				}
			}
			System.out.println();
		}
	}

	/**
	 * Muestra en color de fondo el texto vacío de la celda.
	 * 
	 * @param color color
	 */
	private static void mostrarLineaColor(Color color) {
		Attribute colorFondo = elegirColorFondo(color);
		System.out.print(colorize("      ", colorFondo));
	}

	/**
	 * Muestra en color el texto con torre en la celda correspondiente.
	 * 
	 * @param celda celda
	 */
	private static void mostrarLineaColorCeldaConTorre(Celda celda) {
		Color colorCelda = celda.obtenerColor();
		Turno turno = celda.obtenerTurnoDeTorre();
		Color colorTorre = celda.obtenerColorDeTorre();

		Attribute colorFondo = elegirColorFondo(colorCelda);
		System.out.print(colorize("  ", colorFondo));

		Attribute colorTurno = turno == Turno.BLANCO ? BRIGHT_WHITE_BACK() : BLACK_BACK();
		Attribute colorTexto = elegirColorTexto(colorTorre);
		System.out.print(colorize(celda.obtenerColorDeTorre().toChar() + ".", colorTexto, colorTurno));

		System.out.print(colorize("  ", colorFondo));
	}

	/**
	 * Seleccionar el color de primer plano en formato texto.
	 * 
	 * @param color color
	 * @return color de primer plano en formato texto
	 */
	private static Attribute elegirColorTexto(Color color) {
		return switch (color) {
		case AMARILLO -> TEXT_COLOR(223, 227, 12); // BRIGHT_YELLOW_TEXT();
		case AZUL -> BRIGHT_BLUE_TEXT();
		case MARRON -> TEXT_COLOR(110, 44, 0);
		case NARANJA -> TEXT_COLOR(248, 162, 65);
		case ROJO -> BRIGHT_RED_TEXT();
		case ROSA -> BRIGHT_MAGENTA_TEXT();
		case PURPURA -> TEXT_COLOR(155, 89, 182);
		case VERDE -> BRIGHT_GREEN_TEXT();
		};
	}

	/**
	 * Seleccionar el color de fondo en formato texto.
	 * 
	 * @param color color
	 * @return color de fondo en formato texto
	 */
	private static Attribute elegirColorFondo(Color color) {
		return switch (color) {
		case AMARILLO -> BRIGHT_YELLOW_BACK();
		case AZUL -> BRIGHT_BLUE_BACK();
		case MARRON -> BACK_COLOR(110, 44, 0);
		case NARANJA -> BACK_COLOR(248, 162, 65);
		case ROJO -> BRIGHT_RED_BACK();
		case ROSA -> BRIGHT_MAGENTA_BACK();
		case PURPURA -> BACK_COLOR(155, 89, 182);
		case VERDE -> BRIGHT_GREEN_BACK();
		};
	}

	/**
	 * Inicializa el estado de los elementos de la partida.
	 */
	private static void inicializarPartida() {
		// Inicializaciones
		tablero = new Tablero();
		arbitro = new Arbitro(tablero);
		// Cargar figuras...
		arbitro.colocarTorres();
		// Abrimos la lectura desde teclado
		scanner = new Scanner(System.in);
	}

	/**
	 * Recoge jugada del teclado.
	 * 
	 * @return jugada jugada en formato texto
	 */
	private static String recogerJugada() {
		System.out.printf("Introduce jugada el jugador con turno %s (máscara cfcf): ", arbitro.obtenerTurno());
		return scanner.next();
	}

	/**
	 * Valida la corrección del formato de la jugada. Solo comprueba la corrección
	 * del formato de entrada en cuanto al tablero. La jugada tiene que tener cuatro
	 * caracteres y contener letras y números de acuerdo a las reglas de la notación
	 * algebraica.
	 * 
	 * Otra mejor solución alternativa es el uso de expresiones regulares (se verán
	 * en la asignatura de 3º Procesadores del Lenguaje).
	 * 
	 * @param jugada a validar
	 * @return true si el formato de la jugada es correcta según las coordenadas
	 *         disponibles del tablero
	 */
	private static boolean validarFormato(String jugada) {
		boolean estado = true;
		if (jugada.length() != TAMAÑO_JUGADA || esLetraInvalida(jugada.charAt(0)) || esLetraInvalida(jugada.charAt(2))
				|| esNumeroInvalido(jugada.charAt(1)) || esNumeroInvalido(jugada.charAt(3))) {
			estado = false;
		}
		return estado;
	}

	/**
	 * Comprueba si la letra está fuera del rango [a,h].
	 * 
	 * @param letra letra a comprobar
	 * @return true si la letra no está en el rango, false en caso contrario
	 */
	private static boolean esLetraInvalida(char letra) {
		return letra < 'a' || letra > 'h';
	}

	/**
	 * Comprueba si el número (en formato letra) está fuera del rango [1,8].
	 * 
	 * @param numero numero
	 * @return true si el número no está en el rango, false en caso contrario
	 */
	private static boolean esNumeroInvalido(char numero) {
		return numero < '1' || numero > '8';
	}

	/**
	 * Obtiene la celda origen.
	 * 
	 * @param jugada jugada en formato notación algebraica (e.g. a1)
	 * @return celda origen o null si no es posible extraerla
	 */
	private static Celda leerOrigen(String jugada) {
		if (jugada.length() != TAMAÑO_JUGADA) {
			return null;
		}
		String textoOrigen = jugada.substring(0, 2);
		return tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
	}

	/**
	 * Obtiene la celda destino.
	 * 
	 * @param jugada jugada en formato notación algebraica (e.g. a1)
	 * @return celda destino o null si no es posible extraerla
	 */
	private static Celda leerDestino(String jugada) {
		if (jugada.length() != TAMAÑO_JUGADA) {
			return null;
		}
		String textoOrigen = jugada.substring(2, 4);
		return tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
	}

}
