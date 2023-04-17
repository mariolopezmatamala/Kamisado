package juego.control;

import java.util.ArrayList;

import juego.modelo.*;
import juego.util.*;

/**
 * arbitro estandar
 * 
 * @author mario lopez matamala
 * @since JDK 16
 * @version 2.1
 *
 */
public class ArbitroEstandar extends ArbitroAbstracto {

	/**
	 * constructor de la clase arbitro simple que recibe y asigna el tablero con el
	 * que se va a jugar. llama a la super clase.
	 * 
	 * @param tablero - tablero de la partida
	 */
	public ArbitroEstandar(Tablero tablero) {
		super(tablero);
	}

	/**
	 * si la ronda esta acabada, comprueba el ganador de la misma
	 * 
	 * @return turno - turno del ganador de la ronda
	 */
	@Override
	public Turno consultarGanadorRonda() {

		if (estaAlcanzadaUltimaFilaPor(Turno.BLANCO)) {
			return Turno.BLANCO;
		}
		if (estaAlcanzadaUltimaFilaPor(Turno.NEGRO)) {
			return Turno.NEGRO;
		}

		return null;
	}

	/**
	 * Comprueba si es legal realizar un empujón sumo con la torre colocada en la
	 * celda de origen.
	 * 
	 * @param origen celda de origen
	 * @return true si es legal, false en caso contrario
	 * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen
	 *                                         son incorrectas
	 */
	@Override
	public boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException {

		int filaCelda = origen.obtenerFila(), columnaCelda = origen.obtenerColumna();

		if (!estaEnTablero(filaCelda, columnaCelda)) {
			throw new CoordenadasIncorrectasException("Las coordenadas de la celda son incorrectas");
		}

		Celda celdaAEmpujar = null;
		Celda celdaDondeEmpujar = null;

		if (!estaEnTablero(filaCelda, columnaCelda)) {
			throw new CoordenadasIncorrectasException();
		}

		if (obtenerTurno() == Turno.BLANCO) {
			celdaAEmpujar = tablero.obtenerCelda(filaCelda + 1, columnaCelda);
			try {
				celdaDondeEmpujar = tablero.obtenerCelda(filaCelda + 2, columnaCelda);
			} catch (CoordenadasIncorrectasException e) {
				return false;
			}

			if (origen.obtenerTorre() != null && origen.obtenerTorre().obtenerNumeroDientes() == 1) {
				if (celdaAEmpujar.obtenerTorre() != null && celdaAEmpujar.obtenerTorre().obtenerNumeroDientes() == 0) {
					if (celdaAEmpujar.obtenerTurnoDeTorre() == Turno.NEGRO) {
						if (estaEnTablero(filaCelda + 2, columnaCelda) && celdaDondeEmpujar.estaVacia()) {
							return true;
						}
					}
				}

			}
		}

		if (obtenerTurno() == Turno.NEGRO) {

			celdaAEmpujar = tablero.obtenerCelda(filaCelda - 1, columnaCelda);
			try {
				celdaDondeEmpujar = tablero.obtenerCelda(filaCelda - 2, columnaCelda);
			} catch (CoordenadasIncorrectasException e) {
				return false;
			}

			if (origen.obtenerTorre() != null && origen.obtenerTorre().obtenerNumeroDientes() == 1) {
				if (celdaAEmpujar.obtenerTorre() != null && celdaAEmpujar.obtenerTorre().obtenerNumeroDientes() != 1) {
					if (celdaAEmpujar.obtenerTurnoDeTorre() == Turno.BLANCO) {
						if (estaEnTablero(filaCelda - 2, columnaCelda) && celdaDondeEmpujar.estaVacia()) {
							return true;
						}
					}
				}

			}
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

		if (estaAlcanzadaUltimaFilaPor(Turno.BLANCO)) {

			return true;

		}

		if (estaAlcanzadaUltimaFilaPor(Turno.NEGRO)) {

			return true;

		}

		return false;
	}

	/**
	 * Realiza un empujón sumo con la torre en la celda de origen. Si la celda está
	 * vacía, no se realiza ninguna operación.
	 * 
	 * @param origen celda con la torre sumo que empuja
	 * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen
	 *                                         son incorrectas
	 */
	@Override
	public void empujarSumo(Celda origen) throws CoordenadasIncorrectasException {

		int filaCelda = origen.obtenerFila(), columnaCelda = origen.obtenerColumna();

		if (!estaEnTablero(filaCelda, columnaCelda)) {
			throw new CoordenadasIncorrectasException();
		}

		if (obtenerTurno() == Turno.BLANCO) {
			tablero.moverTorre(tablero.obtenerCelda(filaCelda + 1, columnaCelda),
					tablero.obtenerCelda(filaCelda + 2, columnaCelda));
			tablero.moverTorre(origen, tablero.obtenerCelda(filaCelda + 1, columnaCelda));

			ultimoColorTurnoNegro = tablero.obtenerCelda(filaCelda + 2, columnaCelda).obtenerColor();

		}
		if (obtenerTurno() == Turno.NEGRO) {
			tablero.moverTorre(tablero.obtenerCelda(filaCelda - 1, columnaCelda),
					tablero.obtenerCelda(filaCelda - 2, columnaCelda));
			tablero.moverTorre(origen, tablero.obtenerCelda(filaCelda - 1, columnaCelda));
			ultimoColorTurnoBlanco = tablero.obtenerCelda(filaCelda - 2, columnaCelda).obtenerColor();

		}

	}

	/**
	 * se encarga de reiniciar la ronda una vez ha finalizado la partida
	 */
	@Override
	public void reiniciarRonda() {

		ArrayList<Torre> torresBlanca = new ArrayList<Torre>();
		ArrayList<Torre> torresNegra = new ArrayList<Torre>();

		for (int i = 0; i < tablero.obtenerNumeroFilas(); i++) {
			for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
				Celda celda = null;
				try {
					celda = tablero.obtenerCelda(i, j);
				} catch (CoordenadasIncorrectasException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!celda.estaVacia()) {
					Torre torre = celda.obtenerTorre();
					if (torre.obtenerTurno() == Turno.BLANCO) {
						torresBlanca.add(celda.obtenerTorre());
					}
					if (torre.obtenerTurno() == Turno.NEGRO) {
						torresNegra.add(celda.obtenerTorre());
					}
					celda.eliminarTorre();

				}
			}
		}

		for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
			Color colorCelda = null;
			try {
				colorCelda = tablero.obtenerCelda(0, j).obtenerColor();
			} catch (CoordenadasIncorrectasException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int l = 0; l < torresBlanca.size(); l++) {
				if (colorCelda == torresBlanca.get(l).obtenerColor()) {
					try {
						tablero.colocar(torresBlanca.get(l), tablero.obtenerCelda(0, j));
					} catch (CoordenadasIncorrectasException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
			Color colorCelda = null;
			try {
				colorCelda = tablero.obtenerCelda(7, j).obtenerColor();
			} catch (CoordenadasIncorrectasException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int l = 0; l < torresNegra.size(); l++) {
				if (colorCelda == torresNegra.get(l).obtenerColor()) {
					try {
						tablero.colocar(torresNegra.get(l), tablero.obtenerCelda(7, j));
					} catch (CoordenadasIncorrectasException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}

		ultimoColorTurnoBlanco = null;
		ultimoColorTurnoNegro = null;
		contador = 0;

	}

}
