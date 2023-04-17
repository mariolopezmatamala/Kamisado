package juego.modelo;

/**
 * Torre simple. Es la torre normal. No puede realizar empujones sumo, el numero
 * maximo de puntos que obtiene es 3.
 * 
 * @author mario lopez matamala
 * @since JDK 16
 * @version 1.0
 *
 */
public class TorreSimple extends TorreAbstracta {

	/**
	 * constructor de la clase torreSimple. se le asigna un turno y un color.
	 * llamando a la super clase
	 * 
	 * @param turno - turno de la torre
	 * @param color - color de la torre
	 */
	public TorreSimple(Turno turno, Color color) {
		super(turno, color);
	}

	/**
	 * obtiene el numero de dientes de la torre
	 * 
	 * @return numero de dientes - 0
	 */
	@Override
	public int obtenerNumeroDientes() {
		return 0;
	}

	/**
	 * obtiene el numero de puntos de la torre
	 * 
	 * @return numero de puntos - 1
	 */
	@Override
	public int obtenerNumeroPuntos() {
		return 1;
	}

	/**
	 * obtiene el numero de celdas maximo que alcanza la torre
	 * 
	 * @return numero de celdas maximo - ilimitado
	 */
	@Override
	public int obtenerMaximoAlcance() {
		return Integer.MAX_VALUE;
	}

	/**
	 * obtiene el numero maximo de torres que puede empujar
	 * 
	 * @return numero de torres maximo a empujar - ninguna porque esta torre no
	 *         puede realizar empujones sumo
	 */
	@Override
	public int obtenerNumeroMaximoTorresAEmpujar() {
		return 0;
	}

	/**
	 * devuelve un string con el turno y el color.
	 * 
	 * @return string con el siguiente formato: TurnoColor
	 */
	@Override
	public String toString() {
		return obtenerTurno().toChar() + "" + obtenerColor().toChar();
	}
}
