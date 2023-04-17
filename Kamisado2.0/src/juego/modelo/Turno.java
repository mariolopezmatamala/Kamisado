package juego.modelo;

/**
 * enumeracion con los dos tipos de turno, blanco y negro
 * 
 * @author mario lopez matamala
 * @version 2.0
 * @since java JDK 16
 * 
 */
public enum Turno {

	/** blanco */
	BLANCO('B'),
	/** negro */
	NEGRO('N');

	/**
	 * caracter que representa cada turno
	 */
	private char caracter;

	/**
	 * constructor de Turno
	 * 
	 * @param caracter, representa el turno
	 */
	private Turno(char caracter) {

		this.caracter = caracter;

	}

	/**
	 * Devuelve el caracter que posee la clase Turno
	 * 
	 * @return caracter - caracter
	 */
	public char toChar() {
		return caracter;
	}

}
