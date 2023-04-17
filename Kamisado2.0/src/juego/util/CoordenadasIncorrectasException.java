package juego.util;

/**
 * excepcion coordenadas incorrectas
 * 
 * @author mario lopez matamala
 * @version 1.0
 * @since JDK 16
 *
 */
@SuppressWarnings("serial")
public class CoordenadasIncorrectasException extends Exception {

	/**
	 * Constructor sin argumentos.
	 */
	public CoordenadasIncorrectasException() {
	}

	/**
	 * Constructor con texto descriptivo.
	 * 
	 * @param message - texto descriptivo.
	 */
	public CoordenadasIncorrectasException(String message) {
		super(message);
	}

	/**
	 * Constructor con excepción encadenada.
	 * 
	 * @param cause - excepción encadenada.
	 */
	public CoordenadasIncorrectasException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor con texto descriptivo y excepción encadenada.
	 * 
	 * @param message - texto descriptivo
	 * @param cause   - excepción encadenada.
	 */
	public CoordenadasIncorrectasException(String message, Throwable cause) {
		super(message, cause);
	}

}
