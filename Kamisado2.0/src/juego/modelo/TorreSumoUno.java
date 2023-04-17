package juego.modelo;

/**
 * torre sumo uno
 * 
 * @author mario lopez matamala
 * @since JDK 16
 * @version 2.0
 *
 */
public class TorreSumoUno extends TorreAbstracta {

	/**
	 * constructor de la clase torreSimple. se le asigna un turno y un color.
	 * llamando a la super clase
	 * 
	 * @param turno - turno de la torre
	 * @param color - color de la torre
	 */
	public TorreSumoUno(Turno turno, Color color) {
		super(turno, color);
	}

	/**
	 * obtiene el numero de dientes de la torre
	 * 
	 * @return numero de dientes - 1
	 */
	@Override
	public int obtenerNumeroDientes() {
		return 1;
	}

	/**
	 * obtiene el numero de puntos de la torre
	 * 
	 * @return numero de puntos - 3
	 */
	@Override
	public int obtenerNumeroPuntos() {
		return 3;
	}

	/**
	 * obtiene el numero de celdas maximo que alcanza la torre
	 * 
	 * @return numero de celdas maximo - 5
	 */
	@Override
	public int obtenerMaximoAlcance() {
		return 5;
	}

	/**
	 * obtiene el numero maximo de torres que puede empujar
	 * 
	 * @return numero de torres maximo a empujar - 1
	 */
	@Override
	public int obtenerNumeroMaximoTorresAEmpujar() {
		return 1;
	}

	/**
	 * devuelve un string con el turno y el color.
	 * 
	 * @return string con el siguiente formato: TurnoColor
	 */
	@Override
	public String toString() {
		return obtenerTurno().toChar() + "" + obtenerColor().toChar() + "" + obtenerNumeroDientes();
	}

}
