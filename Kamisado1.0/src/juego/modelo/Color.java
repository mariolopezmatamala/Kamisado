package juego.modelo;

/**
 * enumeracion con los colores para la celda
 * 
 * @author mario lopez matamala
 * @version 1.1
 * @since java JDK 16
 *
 */
public enum Color {

	/** marron */
	MARRON('M'),
	/** verde */
	VERDE('V'),
	/** rojo */
	ROJO('R'),
	/** amarillo */
	AMARILLO('A'),
	/** rosa */
	ROSA('S'),
	/** purpura */
	PURPURA('P'),
	/** azul */
	AZUL('Z'),
	/** naranja */
	NARANJA('N');

	/**
	 * caracter que representa cada color
	 */
	private char caracter;

	/**
	 * constructor de color
	 * 
	 * @param caracter, representa el color
	 */
	private Color(char caracter) {
		this.caracter = caracter;
	}

	/**
	 * devuelve el caracter que posee la clase Color
	 * 
	 * @return caracter - caracter
	 */
	public char toChar() {
		return caracter;
	}

	/**
	 * obtiene al azar un color entre los posibles
	 * 
	 * @return color - color
	 */
	public static Color obtenerColorAleatorio() {

		int numero = (int) (Math.random() * 8);

		return Color.values()[numero];

	}

}
