package juego.modelo;

/**
 * clase Celda con las celdas que iran en el tablero
 * 
 * @author mario lopez matamala
 * @version 2.0
 * @since Java JDK 16
 *
 */
public class Celda {

	/**
	 * fila y columna de la celda
	 */
	private int fila, columna;

	/**
	 * color de la celda
	 */
	private Color color;

	/**
	 * torre en la celda
	 */
	private Torre torre;

	/**
	 * crea el objeto de tipo celda con su correspondiente fila, columna y color
	 * 
	 * @param fila    - fila de la celda
	 * @param columna - columna de la celda
	 * @param color   - color de la celda
	 */
	public Celda(int fila, int columna, Color color) {
		this.fila = fila;
		this.columna = columna;
		this.color = color;
	}

	/**
	 * elimina la torre de la celda
	 */
	public void eliminarTorre() {
		this.torre = null;
	}

	/**
	 * establece la torre que recibe en la celda
	 * 
	 * @param torre - torre a introducir
	 */
	public void establecerTorre(Torre torre) {
		this.torre = torre;
	}

	/**
	 * comprueba si la celda dada esta vacia o no
	 * 
	 * @return resultado - si esta vacia o no
	 */
	public boolean estaVacia() {
		boolean resultado;

		if (this.torre == null) {
			resultado = true;
		} else {
			resultado = false;
		}

		return resultado;
	}

	/**
	 * obtiene la fila de la celda
	 * 
	 * @return fila - fila de la celda
	 */
	public int obtenerFila() {
		return this.fila;
	}

	/**
	 * obtiene la columna de la celda
	 * 
	 * @return columna - columna de la celda
	 */
	public int obtenerColumna() {
		return this.columna;
	}

	/**
	 * obtiene el color de la celda
	 * 
	 * @return color - color de la celda
	 */
	public Color obtenerColor() {

		return this.color;

	}

	/**
	 * obtiene la torre que se encuentra en la celda
	 * 
	 * @return torre - torre de la celda
	 */
	public Torre obtenerTorre() {
		return this.torre;
	}

	/**
	 * obtiene el color de la torre que esta en la celda. si esta vacia devuelve
	 * null
	 * 
	 * @return color - color de la torre
	 */
	public Color obtenerColorDeTorre() {
		if (estaVacia()) {
			return null;
		} else {
			return torre.obtenerColor();
		}
	}

	/**
	 * obtiene el turno de la torre que hay en la celda
	 * 
	 * @return turno - turno de la torre
	 */
	public Turno obtenerTurnoDeTorre() {
		if (estaVacia()) {
			return null;
		} else {
			return torre.obtenerTurno();
		}
	}

	/**
	 * comprueba si la celda introducida tiene las mismas coordenadas que la actual
	 * 
	 * @param celda - celda a comprobar
	 * @return true si tienen las mismas coordenadas, false si no lo tienen
	 */
	public boolean tieneCoordenadasIguales(Celda celda) {
		if (this.obtenerFila() == celda.obtenerFila() && this.obtenerColumna() == celda.obtenerColumna()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * String con la fila, columna, color y turno, en caso de que no tenga color o
	 * turno devuelve un guion
	 * 
	 * @return tiene el siguiente formato: [fila][columna] Color: color Turno: turno
	 */
	public String toString() {
		String respuesta;
		respuesta = "[" + fila + "] " + "[" + columna + "] Color: " + obtenerColor().toChar() + " Turno: ";

		if (obtenerTurnoDeTorre() == null) {
			respuesta = respuesta + "-";
		} else {
			respuesta = respuesta + obtenerTurnoDeTorre().toChar();
		}

		respuesta = respuesta + " Torre: ";

		if (obtenerColorDeTorre() == null) {
			respuesta = respuesta + "-";
		} else {
			respuesta = respuesta + obtenerColorDeTorre().toChar();
		}

		return respuesta;
	}
}
