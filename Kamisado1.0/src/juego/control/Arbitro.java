package juego.control;

import juego.modelo.*;
import juego.util.Sentido;

/**
 * clase con el arbitro encargado de controlar el juego y asignar el tablero
 * 
 * @author mario lopez matamala
 * @version 2.1
 * @since java JDK 16
 */
public class Arbitro {

	/**
	 * registro del turno que se esta llevando actualmente
	 */
	private Turno turnoActual = null;

	/**
	 * contador con los movimientos que se han llevado a cabo
	 */
	private int contador = 0;

	/** tipo de sentido */
	private Sentido sentido;

	/** tablero */
	private Tablero tablero;

	/** array de colores para asignar las torres de turno blanco */
	private final Color[] coloresBlanca = { Color.NARANJA, Color.AZUL, Color.PURPURA, Color.ROSA, Color.AMARILLO,
			Color.ROJO, Color.VERDE, Color.MARRON };

	/** array de colores para asignar las torres de turno negro */
	private final Color[] coloresNegra = { Color.MARRON, Color.VERDE, Color.ROJO, Color.AMARILLO, Color.ROSA,
			Color.PURPURA, Color.AZUL, Color.NARANJA };

	/**
	 * ultimo color de la celda donde ha movido el jugador de turno negro
	 */
	private Color ultimoColorTurnoNegro;

	/**
	 * ultimo color de la celda donde ha movido el jugador de turno blanco
	 */
	private Color ultimoColorTurnoBlanco;

	/**
	 * constructor de la clase arbitro que recibe y asigna el tablero con el que se
	 * va a jugar.
	 * 
	 * @param tablero - tablero de la partida
	 */
	public Arbitro(Tablero tablero) {
		this.tablero = tablero;
	}

	/**
	 * cambia el turno de blanco a negro y viceversa. Si el turno es null, es decir,
	 * que no se ha asignado, lo establece a negro.
	 * 
	 */
	public void cambiarTurno() {
		if (obtenerTurno() == Turno.BLANCO || obtenerTurno() == null) {
			turnoActual = Turno.NEGRO;
		} else {
			turnoActual = Turno.BLANCO;
		}
	}

	/**
	 * inicialmente coloca las torres en el tablero, tanto las blancas como las
	 * negras
	 */
	public void colocarTorres() {

		for (int i = 0, j = 0; j < tablero.obtenerNumeroColumnas(); j++) {

			tablero.colocar(new Torre(Turno.BLANCO, coloresBlanca[j]), tablero.obtenerCelda(i, j));
		}

		for (int i = 7, j = 0; j < tablero.obtenerNumeroColumnas(); j++) {

			tablero.colocar(new Torre(Turno.NEGRO, coloresNegra[j]), tablero.obtenerCelda(i, j));
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
	 */
	void colocarTorres(Torre[] torres, String[] coordenadas, Color ultimoColorTurnoNegro, Color ultimoColorTurnoBlanco,
			Turno turnoActual) {
		this.turnoActual = turnoActual;

		for (int i = 0; i < torres.length; i++) {

			tablero.colocar(torres[i], tablero.obtenerCeldaParaNotacionAlgebraica(coordenadas[i]));
		}

		this.ultimoColorTurnoBlanco = ultimoColorTurnoBlanco;
		this.ultimoColorTurnoNegro = ultimoColorTurnoNegro;

	}

	/**
	 * obtiene el turno del ganador de la partida, si ha ganado la partida o si ha
	 * alcanzado la ultima fila el turno.
	 * 
	 * @return ganador o null si no hay ganador
	 */
	public Turno consultarGanador() {
		if (hayBloqueoMutuo()) {
			cambiarTurno();
			// cambiamos el turno ya que gana el ultimo que hizo un movimiento de distancia
			// cero
			return obtenerTurno();
		}
		cambiarTurno();
		if (estaAlcanzadaUltimaFilaPor(obtenerTurno())) {
			return obtenerTurno();
		}
		return null;

	}

	/**
	 * consulta si es legal realizar el movimiento, dado una celda de origen o una
	 * celda de destino
	 * 
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 * @return true si es legal, false si no es legal
	 */
	public boolean esMovimientoLegalConTurnoActual(Celda origen, Celda destino) {
		if (origen == null || destino == null) {
			return false;
		}

		sentido = tablero.obtenerSentido(origen, destino);

		if (obtenerTurno() == Turno.NEGRO && !origen.estaVacia() && destino.estaVacia()
				&& tablero.estanVaciasCeldasEntre(origen, destino)) {

			if (sentido == Sentido.DIAGONAL_NO || sentido == Sentido.DIAGONAL_NE || sentido == Sentido.VERTICAL_N) {
				if (origen.obtenerTurnoDeTorre() == Turno.NEGRO) {
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

		if (obtenerTurno() == Turno.BLANCO && !origen.estaVacia() && destino.estaVacia()
				&& tablero.estanVaciasCeldasEntre(origen, destino)) {

			if (sentido == Sentido.DIAGONAL_SO || sentido == Sentido.DIAGONAL_SE || sentido == Sentido.VERTICAL_S) {

				if (origen.obtenerTurnoDeTorre() == Turno.BLANCO) {
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

		return false;

	}

	/**
	 * comprueba si el del turno pasado al actual ha conseguido llegar al final del
	 * tablero
	 * 
	 * @param turno - turno actual
	 * @return true si ha alcanzado la ultima fila, false en caso contrario
	 */
	public boolean estaAlcanzadaUltimaFilaPor(Turno turno) {
		// comprobamos si ese turno tiene alguna torre en el lado del turno contrario
		if (turno == Turno.NEGRO) {

			for (int j = 0; j < tablero.obtenerNumeroFilas(); j++) {

				if (tablero.obtenerCelda(0, j).obtenerTurnoDeTorre() == Turno.NEGRO) {

					return true;
				}
			}
		}

		if (turno == Turno.BLANCO) {
			for (int j = 0; j < tablero.obtenerNumeroFilas(); j++) {

				if (tablero.obtenerCelda(7, j).obtenerTurnoDeTorre() == Turno.BLANCO) {

					return true;
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
	public boolean estaBloqueadoTurnoActual() {

		if (obtenerNumeroJugada() < 2) {// no puede estar bloqueada en los dos primeros movimientos
			return false;
		} // FIX, este IF se elimina en la version kamisado4.0

		if (estaAlcanzadaUltimaFilaPor(Turno.NEGRO) || estaAlcanzadaUltimaFilaPor(Turno.BLANCO)) {
			return false;
		}

		if (obtenerTurno() == Turno.NEGRO) {

			final int filaOrigen, columnaOrigen;
			Celda celda = buscarCeldaConTorreDeColor(Turno.NEGRO, ultimoColorTurnoBlanco);
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
			/*
			 * en caso de que este vacia la celda en la que estamos mirando, quiere decir
			 * que el jugador no tiene la torre bloqueada y con lo cual puede mover, siempre
			 * que es celda este en el tablero			 * 
			 */

		}

		if (obtenerTurno() == Turno.BLANCO) {

			// obtiene la celda del ultimo movimiento, comprueba si hay torres
			// justo en frente. si es asi devuelve true.

			final int filaOrigen, columnaOrigen;
			Celda celda = buscarCeldaConTorreDeColor(Turno.BLANCO, ultimoColorTurnoNegro);
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

		return true;

	}

	/**
	 * funcion de "apoyo" a la funcion anterior estaBloqueadoTurnoActual. Busca en
	 * todo el tablero la celda que tiene la torre con el turno y el color
	 * correspondiente.
	 * 
	 * @param turno - turno del jugador
	 * @param color - color de la torre
	 * @return celda
	 */
	private Celda buscarCeldaConTorreDeColor(Turno turno, Color color) {
		Celda celda = null;
		for (int i = 0; i < tablero.obtenerNumeroFilas(); i++) {
			for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {

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

			}
		}
		return null;
	}

	/**
	 * 
	 * comprueba si ninguno de los dos jugadores puede mover su torre
	 * 
	 * @return true si ninguno puede moverla, false en caso contrario
	 */
	public boolean hayBloqueoMutuo() {
		// pierde el jugador que hizo el ultimo movimiento de distancia cero
		if (estaBloqueadoTurnoActual()) {
			cambiarTurno();

			if (estaBloqueadoTurnoActual()) {
				cambiarTurno();
				return true;
			}
		}
		return false;

	}

	/**
	 * realiza el movimiento de la celda de origen a la de destino, Se ajusta el
	 * color y el turno al actual, asi como se cuenta una movimiento
	 * 
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 */
	public void moverConTurnoActual(Celda origen, Celda destino) {

		if (obtenerTurno() == Turno.BLANCO) {
			ultimoColorTurnoBlanco = destino.obtenerColor();

		} else {
			ultimoColorTurnoNegro = destino.obtenerColor();

		}

		tablero.moverTorre(origen, destino);
		this.contador++;

		cambiarTurno();

	}

	/**
	 * en caso de que la torre este bloqueada, se acude a este metodo para realizar
	 * un movimiento de distancia cero. Se actualizan las celdas, se cambia el turno
	 * y se cuenta una jugada
	 */
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
	 * obtiene el total de jugadas
	 * 
	 * @return contador - numero de jugadas
	 */
	public int obtenerNumeroJugada() {

		return this.contador;

	}

	/**
	 * se obtiene el turno actual.
	 * 
	 * @return turno - turno actual
	 */
	public Turno obtenerTurno() {

		return this.turnoActual;
	}

	/**
	 * obtiene el color de la celda del turno que ha realizado el ultimo movimiento
	 * 
	 * @param turno - turno actual
	 * @return color de la celda
	 */
	public Color obtenerUltimoMovimiento(Turno turno) {
		if (turno == Turno.BLANCO) {

			return this.ultimoColorTurnoBlanco;

		} else {

			return this.ultimoColorTurnoNegro;
		}
	}

	/**
	 * comprueba si una celda dada con su fila y columna pertenece la tablero
	 * 
	 * @param fila    - fila de la celda
	 * @param columna - columna de la celda
	 * @return true si esta en tablero, false en caso contrario
	 */
	private boolean estaEnTablero(int fila, int columna) {
		return (fila < tablero.obtenerNumeroFilas() && columna < tablero.obtenerNumeroColumnas() && fila >= 0
				&& columna >= 0);
	}

}
