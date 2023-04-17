package juego.modelo;

/**
 * torre abstracta
 * 
 * @author mario lopez matamala
 * @version 1.1
 * @since jdk 16
 */
public abstract class TorreAbstracta implements Torre {

	/**
	 * Turno asociado a la torre
	 */
	private Turno turno;

	/**
	 * color de la torre
	 */
	private Color color;

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
	public TorreAbstracta(Turno turno, Color color) {
		this.turno = turno;
		this.color = color;
	}

	/**
	 * establece la celda
	 * 
	 * @param celda - celda
	 */
	@Override
	public void establecerCelda(Celda celda) {
		this.celda = celda;
	}

	/**
	 * obtiene la celda
	 * 
	 * @return celda - celda
	 */
	@Override
	public Celda obtenerCelda() {
		return this.celda;
	}

	/**
	 * obtiene el color
	 * 
	 * @return color - color
	 */
	@Override
	public Color obtenerColor() {
		return this.color;
	}

	/**
	 * obtiene el turno
	 * 
	 * @return turno - turno
	 */
	@Override
	public Turno obtenerTurno() {
		return this.turno;
	}

	/**
	 * obtiene el numero de dientes de la torre
	 * 
	 * @return numero de dientes
	 */
	public abstract int obtenerNumeroDientes();

	/**
	 * obtiene el numero de puntos de la torre
	 * 
	 * @return numero de puntos
	 */
	public abstract int obtenerNumeroPuntos();

	/**
	 * obtiene el numero de celdas maximo que alcanza la torre
	 * 
	 * @return numero de celdas maximo
	 */
	public abstract int obtenerMaximoAlcance();

	/**
	 * obtiene el numero maximo de torres que puede empujar
	 * 
	 * @return numero de torres maximo a empujar
	 */
	public abstract int obtenerNumeroMaximoTorresAEmpujar();

	/**
	 * devuelve un string con el turno y el color. Ademas de los dientes si se trata
	 * de una torre sumo uno
	 * 
	 * @return string
	 */
	public abstract String toString();

}
