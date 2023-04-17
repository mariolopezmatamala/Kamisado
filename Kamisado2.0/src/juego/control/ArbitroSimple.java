package juego.control;

import juego.modelo.*;
import juego.util.*;

/**
 * arbitro simple
 * 
 * @author mario lopez matamala
 * @since JDK 16
 * @version 2.1
 *
 */
public class ArbitroSimple extends ArbitroAbstracto {

	/**
	 * constructor de la clase arbitro simple que recibe y asigna el tablero con el
	 * que se va a jugar. llama a la super clase.
	 * 
	 * @param tablero - tablero de la partida
	 */
	public ArbitroSimple(Tablero tablero) {
		super(tablero);
	}

	/**
	 * si la ronda esta acabada, comprueba el ganador de la misma. como solo hay una
	 * ronda, es equivalente a consultar el ganador de la partida
	 * 
	 * @return turno - turno del ganador de la ronda
	 */
	@Override
	public Turno consultarGanadorRonda() {

		return consultarGanadorPartida();
	}

	/**
	 * Comprueba si es legal realizar un empujón sumo con la torre colocada en la
	 * celda de origen. Dado que en este tipo de partida no hay empujones, devuelve
	 * siempre false
	 * 
	 * @param celda - celda de origen donde va a mover la torre
	 * @return false
	 * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen
	 *                                         son incorrectas
	 */
	@Override
	public boolean esEmpujonSumoLegal(Celda celda) throws CoordenadasIncorrectasException {
		if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
			throw new CoordenadasIncorrectasException("Las coordenadas de la celda son incorrectas");
		}
		return false;
	}

	/**
	 * comprueba si esta acabada la ronda. una ronda esta acabada cuando alguno de
	 * los dos jugadores llega a su fila contraria
	 * 
	 * @return true si esta finalizada, false en caso contrario
	 */
	@Override
	public boolean estaAcabadaRonda() {

		if (estaAlcanzadaUltimaFilaPor(Turno.NEGRO)) {
			aumentarPuntuacion(Turno.NEGRO, 3);

			return true;

		}

		if (estaAlcanzadaUltimaFilaPor(Turno.BLANCO)) {

			aumentarPuntuacion(Turno.BLANCO, 3);
			return true;

		}

		return false;
	}

	/**
	 * Realiza un empujón sumo con la torre en la celda de origen. Dado que en este
	 * tipo de partida no hay empujones sumo. lanza una excepcion
	 * 
	 * @param origen celda con la torre sumo que empuja
	 * 
	 */
	@Override
	public void empujarSumo(Celda origen) {
		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * se encarga de reiniciar la ronda. Dado que solo hay una ronda, lanza una
	 * excepcion
	 */
	@Override
	public void reiniciarRonda() {
		throw new java.lang.UnsupportedOperationException();
	}
}
