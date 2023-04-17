package juego.control;

import juego.modelo.*;
import juego.util.CoordenadasIncorrectasException;
import juego.util.Sentido;

/**
 * clase con el arbitro encargado de controlar el juego y asignar el tablero
 * 
 * @author mario lopez matamala
 * @since JDK 11
 * @version 1.0
 */
public abstract class ArbitroAbstracto implements Arbitro {

	/**
	 * registro del turno que se esta llevando actualmente
	 */
	private Turno turnoActual = null;

	/**
	 * contador con los movimientos que se han llevado a cabo
	 */
	protected int contador = 0;

	/** tablero */
	protected static Tablero tablero;

	/** array de colores para asignar las torres de turno blanco */
	private final Color[] coloresBlanca = { Color.NARANJA, Color.AZUL, Color.PURPURA, Color.ROSA, Color.AMARILLO,
			Color.ROJO, Color.VERDE, Color.MARRON };

	/** array de colores para asignar las torres de turno negro */
	private final Color[] coloresNegra = { Color.MARRON, Color.VERDE, Color.ROJO, Color.AMARILLO, Color.ROSA,
			Color.PURPURA, Color.AZUL, Color.NARANJA };

	/** color de la ultima celda donde ha movido el turno negro */
	protected Color ultimoColorTurnoNegro;

	/** color de la ultima celda donde ha movido el turno blanco */
	protected Color ultimoColorTurnoBlanco;

	/** puntuacion total del turno blanco */
	private int puntuacionBlanco = 0;

	/** puntuacion total del turno negro */
	private int puntuacionNegro = 0;

	/**
	 * constructor de la clase arbitro que recibe y asigna el tablero con el que se
	 * va a jugar.
	 * 
	 * @param tablero - tablero de la partida
	 */
	public ArbitroAbstracto(Tablero tablero) {
		ArbitroAbstracto.tablero = tablero;
	}

	/**
	 * Retorna la celda que contiene la torre del turno y color indicado.
	 * 
	 * @param turno turno
	 * @param color color
	 * @return celda
	 */
	@Override
	public Celda buscarCeldaConTorreDeColor(Turno turno, Color color) {
		Celda celda = null;
		for (int i = 0; i < tablero.obtenerNumeroFilas(); i++) {
			for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {

				try {
					celda = tablero.obtenerCelda(i, j);
					if (celda.obtenerTorre() != null) {
						if (celda.obtenerTurnoDeTorre() == turno) {
							if (celda.obtenerTorre() != null) {
								if (celda.obtenerColorDeTorre() == color) {
									return celda;
								}
							}
						}

					}
				} catch (CoordenadasIncorrectasException e) {
					throw new RuntimeException("Error en ejecución", e);
				}

			}
		}
		return null;
	}

	/**
	 * inicialmente coloca las torres en el tablero, tanto las blancas como las
	 * negras
	 */
	@Override
	public void colocarTorres() {

		for (int i = 0, j = 0; j < tablero.obtenerNumeroColumnas(); j++) {

			try {
				tablero.colocar(new TorreSimple(Turno.BLANCO, coloresBlanca[j]), tablero.obtenerCelda(i, j));
			} catch (CoordenadasIncorrectasException e) {
				throw new RuntimeException("Error en ejecución", e);
			}
		}

		for (int i = 7, j = 0; j < tablero.obtenerNumeroColumnas(); j++) {

			try {
				tablero.colocar(new TorreSimple(Turno.NEGRO, coloresNegra[j]), tablero.obtenerCelda(i, j));
			} catch (CoordenadasIncorrectasException e) {
				throw new RuntimeException("Error en ejecución", e);
			}
		}
		cambiarTurno();// como al principio el turno es null, lo establece a NEGRO

	}

	/**
	 * funcion de prueba, no se implementa en el juego final sino en los test, donde
	 * recibe las torres, las coordenadas en notacion algebraica, los colores de las
	 * ultimas celdas donde han estado ambos jugadores, y el turno al que
	 * corresponde, donde iniciara la partida colocando las torres en el tablero y
	 * asignando las variables.
	 * 
	 * @param torres                 - array de torres
	 * @param coordenadas            - coordenadas de las celdas dadas en notacion
	 *                               algebraica
	 * @param ultimoColorTurnoNegro  - color de la ultima celda donde ha movido el
	 *                               de turno negro
	 * @param ultimoColorTurnoBlanco - color de la ultima celda donde ha movido el
	 *                               de turno blanco
	 * @param turnoActual            - jugador a quien le toca el turno
	 * 
	 * @throws CoordenadasIncorrectasException si alguna de las coordenadas es
	 *                                         incorrecta
	 */
	@Override
	public void colocarTorres(Torre[] torres, String[] coordenadas, Color ultimoColorTurnoNegro,
			Color ultimoColorTurnoBlanco, Turno turnoActual) throws CoordenadasIncorrectasException {
		this.turnoActual = turnoActual;

		Celda celda;
		for (int i = 0; i < torres.length; i++) {

			celda = tablero.obtenerCeldaParaNotacionAlgebraica(coordenadas[i]);
			if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
				throw new CoordenadasIncorrectasException("Las coordenas son incorrectas");
			}
			tablero.colocar(torres[i], celda);
		}

		this.ultimoColorTurnoBlanco = ultimoColorTurnoBlanco;
		this.ultimoColorTurnoNegro = ultimoColorTurnoNegro;

	}

	/**
	 * comprueba si esta acabada la partida. Una partida esta acabada cuando alguno
	 * de los jugadores consigue dos o mas puntos
	 * 
	 * @return true si esta acbaada, false en caso contrario
	 */
	@Override
	public boolean estaAcabadaPartida() {

		if (estaAcabadaRonda()) {

			if (obtenerPuntuacionTurnoBlanco() > 2) {

				return true;
			}
			if (obtenerPuntuacionTurnoNegro() > 2) {

				return true;
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
	public abstract boolean estaAcabadaRonda();

	/**
	 * si la partida esta acabada, obtiene el ganador de la misma
	 * 
	 * @return turno - turno del ganador
	 */
	@Override
	public Turno consultarGanadorPartida() {

		if (estaAcabadaPartida()) {
			if (obtenerPuntuacionTurnoNegro() > 2) {

				return Turno.NEGRO;
			}
			if (obtenerPuntuacionTurnoBlanco() > 2) {

				return Turno.BLANCO;
			}

		}
		return null;

	}

	/**
	 * si la ronda esta acabada, comprueba el ganador de la misma
	 * 
	 * @return turno - turno del ganador de la ronda
	 */
	public abstract Turno consultarGanadorRonda();

	/**
	 * Realiza un empujón sumo con la torre en la celda de origen. Si la celda está
	 * vacía, no se realiza ninguna operación.
	 * 
	 * @param origen celda con la torre sumo que empuja
	 * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen
	 *                                         son incorrectas
	 */
	public abstract void empujarSumo(Celda origen) throws CoordenadasIncorrectasException;

	/**
	 * consulta si es legal realizar el movimiento, dado una celda de origen o una
	 * celda de destino
	 * 
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 * @return true si es legal, false si no es legal
	 * @throws CoordenadasIncorrectasException si alguna de las coordenadas de las
	 *                                         dos celdas es incorrects
	 */
	@Override
	public boolean esMovimientoLegalConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException {

		Sentido sentido = tablero.obtenerSentido(origen, destino);

		if (obtenerTurno() == Turno.NEGRO && !origen.estaVacia() && destino.estaVacia()
				&& tablero.estanVaciasCeldasEntre(origen, destino)) {

			if (sentido == Sentido.DIAGONAL_NO || sentido == Sentido.DIAGONAL_NE || sentido == Sentido.VERTICAL_N) {
				if (origen.obtenerTurnoDeTorre() == Turno.NEGRO) {
					// comprobamos que, por ejemplo si es una torre sumo uno, no se mueva mas de lo
					// permitido
					if (tablero.obtenerDistancia(origen, destino) <= origen.obtenerTorre().obtenerMaximoAlcance()) {
						if (obtenerNumeroJugada() == 0) {// el primer movimiento siempre lo puede hacer "libremente"
							return true;
						} else {
							if (origen.obtenerColorDeTorre() == obtenerUltimoMovimiento(Turno.BLANCO)) {
								return true;
							}
						}
					}

				}

			}
		}

		if (obtenerTurno() == Turno.BLANCO && !origen.estaVacia() && destino.estaVacia()
				&& tablero.estanVaciasCeldasEntre(origen, destino)) {

			if (sentido == Sentido.DIAGONAL_SO || sentido == Sentido.DIAGONAL_SE || sentido == Sentido.VERTICAL_S) {

				if (origen.obtenerTurnoDeTorre() == Turno.BLANCO) {
					if (tablero.obtenerDistancia(origen, destino) <= origen.obtenerTorre().obtenerMaximoAlcance()) {
						if (obtenerNumeroJugada() == 0) {// el primer movimiento siempre lo puede hacer "libremente"
							return true;
						} else {
							if (origen.obtenerColorDeTorre() == obtenerUltimoMovimiento(Turno.NEGRO)) {

								return true;
							}
						}
					}
				}
			}

		}

		return false;
	}

	/**
	 * comprueba si el jugador con el turno actual no puede mover su torre
	 * 
	 * @return true si no puede moverla, false en caso contrario
	 */
	@Override
	public boolean estaBloqueadoTurnoActual() {

		try {
			if (estaAlcanzadaUltimaFilaPor(Turno.NEGRO) || estaAlcanzadaUltimaFilaPor(Turno.BLANCO) || estaVacio()) {
				return false;
			}
		} catch (CoordenadasIncorrectasException e) {
			throw new RuntimeException("Error en ejecución", e);
		}

		try {
			if (obtenerTurno() == Turno.NEGRO) {

				final int filaOrigen, columnaOrigen;

				Celda celda = buscarCeldaConTorreDeColor(Turno.NEGRO, ultimoColorTurnoBlanco);
				if (celda == null) {
					return false;
				}
				filaOrigen = celda.obtenerFila();
				columnaOrigen = celda.obtenerColumna();

				// caso noreste

				if (estaEnTablero(filaOrigen - 1, columnaOrigen + 1)
						&& tablero.obtenerCelda(filaOrigen - 1, columnaOrigen + 1).estaVacia()) {
					return false;
				}

				// caso noroeste

				if (estaEnTablero(filaOrigen - 1, columnaOrigen - 1)
						&& tablero.obtenerCelda(filaOrigen - 1, columnaOrigen - 1).estaVacia()) {
					return false;
				}

				// caso verticalnorte

				if (estaEnTablero(filaOrigen - 1, columnaOrigen)
						&& tablero.obtenerCelda(filaOrigen - 1, columnaOrigen).estaVacia()) {
					return false;
				}

			}

			if (obtenerTurno() == Turno.BLANCO) {

				// obtiene la celda del ultimo movimiento, comprueba si hay torres
				// justo en frente. si es asi devuelve true.

				final int filaOrigen, columnaOrigen;
				Celda celda = buscarCeldaConTorreDeColor(Turno.BLANCO, ultimoColorTurnoNegro);
				if (celda == null) {
					return false;
				}
				filaOrigen = celda.obtenerFila();
				columnaOrigen = celda.obtenerColumna();

				// caso sureste

				if (estaEnTablero(filaOrigen + 1, columnaOrigen + 1)
						&& tablero.obtenerCelda(filaOrigen + 1, columnaOrigen + 1).estaVacia()) {
					return false;
				}

				// caso suroeste

				if (estaEnTablero(filaOrigen + 1, columnaOrigen - 1)
						&& tablero.obtenerCelda(filaOrigen + 1, columnaOrigen - 1).estaVacia()) {
					return false;
				}

				// caso verticalsur

				if (estaEnTablero(filaOrigen + 1, columnaOrigen)
						&& tablero.obtenerCelda(filaOrigen + 1, columnaOrigen).estaVacia()) {
					return false;
				}

			}
		} catch (CoordenadasIncorrectasException e) {
			throw new RuntimeException("Error en ejecución", e);
		}

		return true;

	}

	/**
	 * comprueba si el tablero esta vacio, es decir, si en las celdas no hay ninguna
	 * torre.
	 * 
	 * @return true si esta vacio, false si no lo esta
	 * @throws CoordenadasIncorrectasException si hay algun fallo con las celdas
	 */
	private boolean estaVacio() throws CoordenadasIncorrectasException {
		for (int i = 0; i < tablero.obtenerNumeroFilas(); i++) {
			for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
				if (!tablero.obtenerCelda(i, j).estaVacia()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * comprueba si ninguno de los dos jugadores puede mover su torre
	 * 
	 * @return true si ninguno puede moverla, false en caso contrario
	 */
	@Override
	public boolean hayBloqueoMutuo() {
		// pierde el jugador que hizo el ultimo movimiento de distancia cero
		if (estaBloqueadoTurnoActual()) {
			cambiarTurno();

			if (estaBloqueadoTurnoActual()) {

				cambiarTurno();
				return true;
			}
			cambiarTurno();// recuperamos el turno que habia anteriormente
		}

		return false;

	}

	/**
	 * realiza el movimiento de la celda de origen a la de destino, Se ajusta el
	 * color y el turno al actual, asi como se cuenta una movimiento
	 * 
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 * @throws CoordenadasIncorrectasException si alguna de las coordenadas de las
	 *                                         dos celdas es incorrecta
	 *
	 */
	@Override
	public void moverConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException {

		if (!estaEnTablero(origen.obtenerFila(), origen.obtenerColumna())
				|| !estaEnTablero(destino.obtenerFila(), destino.obtenerColumna())) {
			throw new CoordenadasIncorrectasException(
					"Las coordenadas de la fila de origen o destino no esta en el tablero");
		}

		if (obtenerTurno() == Turno.BLANCO) {
			ultimoColorTurnoBlanco = destino.obtenerColor();

		} else {
			ultimoColorTurnoNegro = destino.obtenerColor();

		}

		tablero.moverTorre(origen, destino);
		if (estaAlcanzadaUltimaFilaPor(obtenerTurno())) {
			if (destino.obtenerTorre().obtenerNumeroDientes() == 1) {
				aumentarPuntuacion(obtenerTurno(), 3);
			} else {
				aumentarPuntuacion(obtenerTurno(), 1);
				Color colorTorre = destino.obtenerColorDeTorre();
				destino.eliminarTorre();
				tablero.colocar(new TorreSumoUno(obtenerTurno(), colorTorre), destino);
			}
		}
		this.contador++;

		cambiarTurno();

	}

	/**
	 * se obtiene el turno actual.
	 * 
	 * @return turno - turno actual
	 */
	@Override
	public Turno obtenerTurno() {

		return this.turnoActual;
	}

	/**
	 * obtiene el total de jugadas
	 * 
	 * @return contador - numero de jugadas
	 */
	@Override
	public int obtenerNumeroJugada() {

		return this.contador;

	}

	/**
	 * en caso de que la torre este bloqueada, se acude a este metodo para realizar
	 * un movimiento de distancia cero. Se actualizan las celdas, se cambia el turno
	 * y se cuenta una jugada
	 */
	@Override
	public void moverConTurnoActualBloqueado() {

		// Debe ajustar el color de último movimiento para el turno actual
		if (obtenerTurno() == Turno.BLANCO) {
			this.ultimoColorTurnoBlanco = buscarCeldaConTorreDeColor(Turno.BLANCO, ultimoColorTurnoNegro)
					.obtenerColor();
		} else {
			this.ultimoColorTurnoNegro = buscarCeldaConTorreDeColor(Turno.NEGRO, ultimoColorTurnoBlanco).obtenerColor();
		}

		this.contador++;
		cambiarTurno();

	}

	/**
	 * obtiene el color de la celda del turno que ha realizado el ultimo movimiento
	 * 
	 * @param turno - turno actual
	 * @return color de la celda
	 */
	@Override
	public Color obtenerUltimoMovimiento(Turno turno) {
		if (turno == Turno.BLANCO) {
			return this.ultimoColorTurnoBlanco;

		} else {
			return this.ultimoColorTurnoNegro;
		}
	}

	/**
	 * Comprueba si es legal realizar un empujón sumo con la torre colocada en la
	 * celda de origen.
	 * 
	 * @param origen celda de origen
	 * @return true si es legal el empujón sumo, false en caso contrario
	 * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen
	 *                                         son incorrectas
	 */
	public abstract boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException;

	/**
	 * Consulta la puntuación del jugador con turno blanco.
	 * 
	 * @return puntuación del jugador con turno blanco
	 */
	@Override
	public int obtenerPuntuacionTurnoBlanco() {
		return this.puntuacionBlanco;
	}

	/**
	 * Consulta la puntuación del jugador con turno negro.
	 * 
	 * @return puntuación del jugador con turno negro
	 */
	@Override
	public int obtenerPuntuacionTurnoNegro() {
		return this.puntuacionNegro;
	}

	/**
	 * se encarga de reiniciar la ronda.
	 */
	public abstract void reiniciarRonda();

	/**
	 * comprueba si la celda esta dentro del tablero
	 * 
	 * @param fila    - fila de la celda
	 * @param columna - columna de la celda
	 * @return true si esta en el tablero, false en caso contrario
	 */
	protected boolean estaEnTablero(int fila, int columna) {
		return (fila < tablero.obtenerNumeroFilas() && columna < tablero.obtenerNumeroColumnas() && fila >= 0
				&& columna >= 0);
	}

	/**
	 * cambia el turno de blanco a negro y viceversa. Si el turno es null, es decir,
	 * que no se ha asignado, lo establece a negro.
	 * 
	 * 
	 */
	public void cambiarTurno() {
		if (obtenerTurno() == Turno.BLANCO || obtenerTurno() == null) {
			this.turnoActual = Turno.NEGRO;
		} else {
			this.turnoActual = Turno.BLANCO;
		}
	}

	/**
	 * aumenta la puntuacion al turno deseado
	 * 
	 * @param turno      - turno
	 * @param puntuacion - puntuacion a aumentar
	 */
	protected void aumentarPuntuacion(Turno turno, int puntuacion) {

		if (turno == Turno.BLANCO) {
			this.puntuacionBlanco = obtenerPuntuacionTurnoBlanco() + puntuacion;

		}
		if (turno == Turno.NEGRO) {
			this.puntuacionNegro = obtenerPuntuacionTurnoNegro() + puntuacion;
		}
	}

	/**
	 * comprueba si el turno pasado ha alcanzado la fila del rival
	 * 
	 * @param turno - turno
	 * @return true si esta alcanzada, false en caso contrario
	 */
	@Override
	public boolean estaAlcanzadaUltimaFilaPor(Turno turno) {

		if (turno == Turno.NEGRO) {

			for (int j = 0; j < tablero.obtenerNumeroFilas(); j++) {

				try {
					if (tablero.obtenerCelda(0, j).obtenerTurnoDeTorre() == Turno.NEGRO) {

						return true;
					}
				} catch (CoordenadasIncorrectasException e) {
					throw new RuntimeException("Error en ejecución", e);
				}
			}
		}

		if (turno == Turno.BLANCO) {
			for (int j = 0; j < tablero.obtenerNumeroFilas(); j++) {

				try {
					if (tablero.obtenerCelda(7, j).obtenerTurnoDeTorre() == Turno.BLANCO) {

						return true;
					}
				} catch (CoordenadasIncorrectasException e) {
					throw new RuntimeException("Error en ejecución", e);
				}
			}
		}
		return false;
	}

}
