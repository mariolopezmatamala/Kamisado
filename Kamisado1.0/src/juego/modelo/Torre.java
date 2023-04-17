/**
 * 
 * 
 */
package juego.modelo;

/**
 * clase con las torres que se colocaran en las celdas
 * 
 * @author mario lopez matamala
 * @version 1.1
 * @since java JDK 16
 * 
 */
public class Torre {

	/**
	 * color de la torre
	 */
	private Color color;

	/**
	 * turno de la torre
	 */
	private Turno turno;

	/**
	 * celda para la torre
	 */
	private Celda celda;

	/**
	 * constructor de la clase Torre
	 * 
	 * @param turno, turno de la torre
	 * @param color, color de la torre
	 */
	public Torre(Turno turno, Color color) {

		this.turno = turno;
		this.color = color;
	}

	/**
	 * establece la celda
	 * 
	 * @param celda - celda
	 */
	public void establecerCelda(Celda celda) {
		this.celda = celda;
	}

	/**
	 * obtiene la celda
	 * 
	 * @return celda - celda
	 */
	public Celda obtenerCelda() {
		return this.celda;
	}

	/**
	 * obtiene el color
	 * 
	 * @return color - color
	 */
	public Color obtenerColor() {
		return this.color;
	}

	/**
	 * obtiene el turno
	 * 
	 * @return turno - turno
	 */
	public Turno obtenerTurno() {
		return this.turno;
	}

	/**
	 * devuelve un string con el turno y el color.
	 * 
	 * @return string con el siguiente formato: TurnoColor
	 */
	public String toString() {
		return obtenerTurno().toChar() + "" + obtenerColor().toChar();
	}

}
