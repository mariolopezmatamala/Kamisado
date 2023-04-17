package juego.modelo;

import juego.util.Sentido;
import juego.util.CoordenadasIncorrectasException;

import java.util.ArrayList;

/**
 * clase con el tablero para el juego, donde se crearan las celdas
 * 
 * @author mario lopez matamala
 * @version 2.1
 * @since JDK 11
 *
 */
public class Tablero {

	/**
	 * tamaño fijo del tablero (8*8)
	 */
	private static final int RANGO = 8;

	/**
	 * ArrayList de ArrayList con las celdas.
	 */
	public ArrayList<ArrayList<Celda>> matriz;

	/**
	 * array constante de colores, con los colores que tendran las celdas
	 */
	private final Color[] colores = { Color.NARANJA, Color.AZUL, Color.PURPURA, Color.ROSA, Color.AMARILLO, Color.ROJO,
			Color.VERDE, Color.MARRON, Color.ROJO, Color.NARANJA, Color.ROSA, Color.VERDE, Color.AZUL, Color.AMARILLO,
			Color.MARRON, Color.PURPURA, Color.VERDE, Color.ROSA, Color.NARANJA, Color.ROJO, Color.PURPURA,
			Color.MARRON, Color.AMARILLO, Color.AZUL, Color.ROSA, Color.PURPURA, Color.AZUL, Color.NARANJA,
			Color.MARRON, Color.VERDE, Color.ROJO, Color.AMARILLO, Color.AMARILLO, Color.ROJO, Color.VERDE,
			Color.MARRON, Color.NARANJA, Color.AZUL, Color.PURPURA, Color.ROSA, Color.AZUL, Color.AMARILLO,
			Color.MARRON, Color.PURPURA, Color.ROJO, Color.NARANJA, Color.ROSA, Color.VERDE, Color.PURPURA,
			Color.MARRON, Color.AMARILLO, Color.AZUL, Color.VERDE, Color.ROSA, Color.NARANJA, Color.ROJO, Color.MARRON,
			Color.VERDE, Color.ROJO, Color.AMARILLO, Color.ROSA, Color.PURPURA, Color.AZUL, Color.NARANJA };

	/**
	 * constructor de tablero, inicializa el arraylist de celdas
	 */
	public Tablero() {
		int l = 0;
		matriz = new ArrayList<ArrayList<Celda>>(RANGO);
		for (int i = 0; i < RANGO; i++) {
			matriz.add(new ArrayList<Celda>(RANGO));
			for (int j = 0; j < RANGO; j++) {
				matriz.get(i).add(new Celda(i, j, colores[l]));
				l++;
			}
		}

	}

	/**
	 * devuelve la celda del color y turno asociado.
	 * 
	 * @param turno - turno
	 * @param color - color de la celda
	 * @return celda de dicho color y turno
	 */
	public Celda buscarCeldaOrigen(Turno turno, Color color) {

		if (turno == Turno.BLANCO) {
			for (int i = 0, j = 0; j < obtenerNumeroColumnas(); j++) {
				try {
					if (obtenerCelda(i, j).obtenerColor() == color) {
						return obtenerCelda(i, j);
					}
				} catch (CoordenadasIncorrectasException e) {
					throw new RuntimeException("Error en ejecución", e);

				}
			}
		}

		if (turno == Turno.NEGRO) {
			for (int i = 7, j = 0; j < obtenerNumeroColumnas(); j++) {
				try {
					if (obtenerCelda(i, j).obtenerColor() == color) {
						return obtenerCelda(i, j);
					}
				} catch (CoordenadasIncorrectasException e) {
					throw new RuntimeException("Error en ejecución", e);
				}
			}
		}

		return null;
	}

	/**
	 * Dado un turno y un color de la celda, devuelve la celda donde se encuentra
	 * 
	 * @param turno - turno
	 * @param color - color
	 * @return celda que contiene el turno y color, sino devuelve null
	 */
	public Celda buscarTorre(Turno turno, Color color) {
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				try {
					if (obtenerCelda(i, j) != null) {
						if (obtenerCelda(i, j).obtenerTurnoDeTorre() == turno
								&& obtenerCelda(i, j).obtenerColorDeTorre() == color) {
							return obtenerCelda(i, j);
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
	 * establece la torre indicada en la celda indicada
	 * 
	 * @param torre - torre
	 * @param celda - celda
	 * @throws CoordenadasIncorrectasException - en caso de que la celda no este en
	 *                                         el tablero se lanza una excepcion
	 * 
	 */
	public void colocar(Torre torre, Celda celda) throws CoordenadasIncorrectasException {
		if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
			throw new CoordenadasIncorrectasException("Las coordenadas no estan dentro del tablero");
		}
		torre.establecerCelda(celda);
		celda.establecerTorre(torre);
	}

	/**
	 * dada un celda en notacion algebraica, coloca la torre en esa posicion.
	 * previamente comprueba si los valores son correctos
	 * 
	 * @param torre              - torre
	 * @param notacionAlgebraica - celda dada en notacion algebraica
	 * 
	 * @throws CoordenadasIncorrectasException - en caso de que la celda
	 *                                         proporcionanda en notacion
	 *                                         algebraica, se lanza una excepcion
	 */
	public void colocar(Torre torre, String notacionAlgebraica) throws CoordenadasIncorrectasException {
		Celda celda = obtenerCeldaParaNotacionAlgebraica(notacionAlgebraica);

		if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
			throw new CoordenadasIncorrectasException("Las coordenadas no estan dentro del tablero");
		}

		torre.establecerCelda(obtenerCeldaParaNotacionAlgebraica(notacionAlgebraica));
		obtenerCeldaParaNotacionAlgebraica(notacionAlgebraica).establecerTorre(torre);

	}

	/**
	 * coloca la torre en una celda dada como dos enteros, fila - columna
	 * 
	 * @param torre   - torre
	 * @param fila    - fila de la celda
	 * @param columna - columna de la celda
	 * @throws CoordenadasIncorrectasException - en caso de que la celda
	 *                                         proporcionada como fila y columna no
	 *                                         este en el tablero, se lanza una
	 *                                         excepcion
	 */
	public void colocar(Torre torre, int fila, int columna) throws CoordenadasIncorrectasException {

		if (!estaEnTablero(fila, columna)) {
			throw new CoordenadasIncorrectasException("Las coordenadas no estan dentro del tablero");
		}

		torre.establecerCelda(obtenerCelda(fila, columna));
		obtenerCelda(fila, columna).establecerTorre(torre);

	}

	/**
	 * comprueba si no hay torres entre dos celdas, es decir, si estan vacias. Si
	 * son consecutivas (sin celdas entre medias), se cuenta como que estan vacias.
	 * 
	 * @param origen  - celda de origen
	 * @param destino - celda de destino
	 * @throws CoordenadasIncorrectasException - en caso de que alguna de las dos
	 *                                         celdas no sea correcta
	 * @return true si estan vacias, false si hay torres entre medias o el sentido
	 *         no es correcto
	 */
	public boolean estanVaciasCeldasEntre(Celda origen, Celda destino) throws CoordenadasIncorrectasException {
		int filaOrigen = origen.obtenerFila();
		int columnaOrigen = origen.obtenerColumna();
		final int filaDestino = destino.obtenerFila();
		final int columnaDestino = destino.obtenerColumna();

		boolean bool = true;// comprueba si NO se ha topado la celda origen con la celda destino

		if (!estaEnTablero(origen.obtenerFila(), origen.obtenerColumna())
				|| !estaEnTablero(destino.obtenerFila(), destino.obtenerColumna())) {
			throw new CoordenadasIncorrectasException(
					"Las coordenadas de la fila de origen o destino no esta en el tablero");
		}

		Sentido sentido = obtenerSentido(origen, destino);
		if (sentido == null) {
			return false;
		} else {
			filaOrigen = filaOrigen + sentido.obtenerDesplazamientoEnFilas();
			columnaOrigen = columnaOrigen + sentido.obtenerDesplazamientoEnColumnas();
			if (filaOrigen == filaDestino && columnaOrigen == columnaDestino) {
				bool = false;

			}
			while (estaEnTablero(filaOrigen, columnaOrigen) && bool) {

				if (!obtenerCelda(filaOrigen, columnaOrigen).estaVacia()) {
					return false;
				}
				filaOrigen = filaOrigen + sentido.obtenerDesplazamientoEnFilas();
				columnaOrigen = columnaOrigen + sentido.obtenerDesplazamientoEnColumnas();

				if (filaOrigen == filaDestino && columnaOrigen == columnaDestino) {
					bool = false;

				}
			}
		}

		return true;
	}

	/**
	 * comprueba si hay alguna torre del turno en la fila de origen del turno
	 * contrario
	 * 
	 * @param turno - turno
	 * @return true si hay torre o false en caso contrario
	 */
	public boolean hayTorreColorContrario(Turno turno) {

		for (int i = 0, j = 7; j < obtenerNumeroColumnas(); j++) {

			try {
				if (obtenerCelda(i, j).obtenerTorre() != null) {
					if (obtenerCelda(i, j).obtenerTurnoDeTorre() == turno) {
						return true;
					}
				}
			} catch (CoordenadasIncorrectasException e) {
				throw new RuntimeException("Error en ejecución", e);
			}
		}

		return false;
	}

	/**
	 * mueve la torre desde la celda origen hasta la celda destino
	 * 
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 * @throws CoordenadasIncorrectasException - en caso de que alguna de las dos
	 *                                         celdas no sea correcta, se lanza una
	 *                                         excepcion
	 * 
	 */
	public void moverTorre(Celda origen, Celda destino) throws CoordenadasIncorrectasException {
		if (!estaEnTablero(origen.obtenerFila(), origen.obtenerColumna())
				|| !estaEnTablero(destino.obtenerFila(), destino.obtenerColumna())) {
			throw new CoordenadasIncorrectasException(
					"Las coordenadas de la fila de origen o destino no esta en el tablero");
		}

		if (origen.estaVacia() || !destino.estaVacia()) {
			// no hace nada
		} else {

			Torre torre;
			torre = origen.obtenerTorre();
			origen.eliminarTorre();

			destino.establecerTorre(torre);
		}
	}

	/**
	 * dado la fila y columna, obtiene la referencia a la celda
	 * 
	 * @param fila    - fila de la celda
	 * @param columna - columna de la celda
	 * @throws CoordenadasIncorrectasException - en caso de que la celda
	 *                                         proporcionada como fila y columna no
	 *                                         este en el tablero
	 * @return celda referenciada
	 */
	public Celda obtenerCelda(int fila, int columna) throws CoordenadasIncorrectasException {

		if (!estaEnTablero(fila, columna)) {
			throw new CoordenadasIncorrectasException("Las coordenadas son incorrectas");
		}

		return matriz.get(fila).get(columna);

	}

	/**
	 * dado unas coordenadas en notacion algebraica, devuelve la celda de origen.
	 * Por ejemplo, a4b7, se queda con b7 y lo transforma a la celda quedandose con
	 * sus propias coordenadas.
	 * 
	 * @param textoJugada - celda dada en notacion algebraica
	 * @throws CoordenadasIncorrectasException - en caso de que la celda no este en
	 *                                         el tablero
	 * @return celda de origen o null en caso de que sea incorrecto
	 */
	public Celda obtenerCeldaDestinoEnJugada(String textoJugada) throws CoordenadasIncorrectasException {
		if (textoJugada.length() != 4) {
			throw new CoordenadasIncorrectasException("Las coordenadas son incorrectas");
		}

		Celda celda = obtenerCeldaParaNotacionAlgebraica(textoJugada.substring(2, 4));
		if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
			throw new CoordenadasIncorrectasException("Las coordenadas son incorrectas");
		}
		return celda;

	}

	/**
	 * dado unas coordenadas en notacion algebraica, devuelve la celda de origen.
	 * Por ejemplo, a4b7, se queda con a4 y lo transforma a la celda quedandose con
	 * sus propias coordenadas
	 * 
	 * @param textoJugada - celda dada en notacion algebraica
	 * @throws CoordenadasIncorrectasException - en caso de que la celda no este en
	 *                                         el tablero
	 * @return celda de destino o null en caso de que sea incorrecto
	 */
	public Celda obtenerCeldaOrigenEnJugada(String textoJugada) throws CoordenadasIncorrectasException {
		if (textoJugada.length() != 4) {
			throw new CoordenadasIncorrectasException("Las coordenadas son incorrectas");
		}

		Celda celda = obtenerCeldaParaNotacionAlgebraica(textoJugada.substring(0, 2));

		if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
			throw new CoordenadasIncorrectasException("Las coordenadas son incorrectas");
		}
		return celda;
	}

	/**
	 * obtiene la celda dada en notacion algebraica
	 * 
	 * @param texto - dado en notacion algebraica
	 * @return celda o null en caso de que sea incorrecto
	 * @throws CoordenadasIncorrectasException en caso de formato incorrecto o de
	 *                                         que las celdas no esten en el tablero
	 */
	public Celda obtenerCeldaParaNotacionAlgebraica(String texto) throws CoordenadasIncorrectasException {

		char letra1 = texto.charAt(0);
		int numero1 = Integer.parseInt(Character.toString(texto.charAt(1)));

		if (letra1 < 'a' || letra1 > 'h' || numero1 < 1 || numero1 > 8) {
			throw new CoordenadasIncorrectasException("Las coordenadas son incorrectas");
		}

		Celda celda = null;

		char letra2 = 'a';
		int numero2 = 8;
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {

				if (letra2 == letra1 && numero2 == numero1) {

					celda = obtenerCelda(i, j);

				}
				letra2++;
			}
			letra2 = 'a';
			numero2--;

		}

		return celda;
	}

	/**
	 * obtiene un array de una dimension con todas las celdas del tablero
	 * 
	 * @return array con las celdas
	 */
	public Celda[] obtenerCeldas() {

		Celda array[] = new Celda[obtenerNumeroFilas() * obtenerNumeroColumnas()];

		int l = 0;
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				array[l] = matriz.get(i).get(j);
				l++;
			}
		}
		return array;
	}

	/**
	 * dada una celda, obtiene sus coordenadas en notacion algebraica. En caso de
	 * que no pertenezca al tablero, devuelve "--"
	 * 
	 * @param celda - celda
	 * @return notacion algebraica de la celda o "--"
	 * @throws CoordenadasIncorrectasException en caso de que la celda no este en el
	 *                                         tablero
	 */
	public String obtenerCoordenadasEnNotacionAlgebraica(Celda celda) throws CoordenadasIncorrectasException {

		char letra = 'a';
		char letra2;
		int numero = RANGO;
		String cadena = "";

		if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
			throw new CoordenadasIncorrectasException("Las coordenadas son incorrectas");
		} else {
			for (int i = 0; i < obtenerNumeroFilas(); i++) {
				for (int j = 0; j < obtenerNumeroColumnas(); j++) {

					if (celda.obtenerFila() == i && celda.obtenerColumna() == j) {
						letra2 = letra;

						cadena = cadena + String.valueOf(letra2) + String.valueOf(numero);
					}

					letra++;

				}
				letra = 'a';
				numero--;

			}

		}
		return cadena;

	}

	/**
	 * obtiene la distancia que hay entre las celdas de origen y la de destino, sin
	 * contar la celda de origen
	 * 
	 * @param origen  - celda de origen
	 * @param destino - celda de destino
	 * @return numero de distancia
	 * @throws CoordenadasIncorrectasException en caso de que las coordenadas de
	 *                                         alguna de las celdas no esten en el
	 *                                         tablero
	 */
	public int obtenerDistancia(Celda origen, Celda destino) throws CoordenadasIncorrectasException {
		int filaOrigen = origen.obtenerFila();
		int columnaOrigen = origen.obtenerColumna();
		final int filaDestino = destino.obtenerFila();
		final int columnaDestino = destino.obtenerColumna();

		Sentido sentido = obtenerSentido(origen, destino);
		boolean bool = true;// comprueba si NO se ha topado la celda origen con la celda destino
		int contador = 0;

		if (!estaEnTablero(origen.obtenerFila(), origen.obtenerColumna())
				|| !estaEnTablero(destino.obtenerFila(), destino.obtenerColumna())) {
			throw new CoordenadasIncorrectasException(
					"Las coordenadas de la fila de origen o destino no esta en el tablero");
		}

		filaOrigen = filaOrigen + sentido.obtenerDesplazamientoEnFilas();
		columnaOrigen = columnaOrigen + sentido.obtenerDesplazamientoEnColumnas();
		if (filaOrigen == filaDestino && columnaOrigen == columnaDestino) {
			bool = false;

		}
		while (bool) {
			contador++;
			filaOrigen = filaOrigen + sentido.obtenerDesplazamientoEnFilas();
			columnaOrigen = columnaOrigen + sentido.obtenerDesplazamientoEnColumnas();

			if (filaOrigen == filaDestino && columnaOrigen == columnaDestino) {
				bool = false;

			}
		}
		return contador;

	}

	/**
	 * obtiene la notacion algebraica de una jugada, dado dos celdas. Con las celdas
	 * 6 5 y 4 7 devolvera su correspondiente notacion algebraica
	 *
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 * @return cadena con el string
	 * @throws CoordenadasIncorrectasException en caso de que la celda de origen o
	 *                                         destino no este en el tablero
	 */
	public String obtenerJugadaEnNotacionAlgebraica(Celda origen, Celda destino)
			throws CoordenadasIncorrectasException {

		if (!estaEnTablero(origen.obtenerFila(), origen.obtenerColumna())
				|| !estaEnTablero(destino.obtenerFila(), destino.obtenerColumna())) {
			throw new CoordenadasIncorrectasException(
					"Las coordenadas de la fila de origen o destino no esta en el tablero");
		}

		String cadena = "";

		cadena = cadena + obtenerCoordenadasEnNotacionAlgebraica(origen);
		cadena = cadena + obtenerCoordenadasEnNotacionAlgebraica(destino);

		return cadena;

	}

	/**
	 * obtiene el numero de columnas del tablero
	 * 
	 * @return tamaño en columnas
	 */
	public int obtenerNumeroColumnas() {
		return matriz.get(0).size();
	}

	/**
	 * obtiene el numero de filas del tablero
	 * 
	 * @return tamaño en filas
	 */
	public int obtenerNumeroFilas() {
		return matriz.size();
	}

	/**
	 * obtiene el numero de torres de un determinado color
	 * 
	 * @param color - color
	 * @return contador - numero de torres
	 */
	public int obtenerNumeroTorres(Color color) {
		int contador = 0;

		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {

				try {
					if (obtenerCelda(i, j).obtenerTorre() != null) {
						if (obtenerCelda(i, j).obtenerColorDeTorre() == color) {
							contador++;
						}
					}
				} catch (CoordenadasIncorrectasException e) {

					throw new RuntimeException("Error en ejecución", e);
				}
			}
		}

		return contador;
	}

	/**
	 * obtiene el numero de torres dado un determinado turno
	 * 
	 * @param turno - blanco o negro
	 * @return contador - numero de torres de un turno
	 */
	public int obtenerNumeroTorres(Turno turno) {
		int contador = 0;

		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {

				try {
					if (obtenerCelda(i, j).obtenerTorre() != null) {
						if (obtenerCelda(i, j).obtenerTurnoDeTorre() == turno) {
							contador++;
						}

					}
				} catch (CoordenadasIncorrectasException e) {
					throw new RuntimeException("Error en ejecución", e);
				}
			}
		}
		return contador;
	}

	/**
	 * obtiene el sentido desde una celda origen a una celda destino. Los tipos de
	 * sentidos posibles vienen predefinidos en la enumeracion de la clase sentido,
	 * de forma que si no se corresponde con ninguno de ellos, devuelve null
	 * 
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 * @return sentido o null en caso de que no tenga ningun sentido
	 * @throws CoordenadasIncorrectasException en caso de que la celda de origen o
	 *                                         destino no este en el tablero
	 */
	public Sentido obtenerSentido(Celda origen, Celda destino) throws CoordenadasIncorrectasException {

		int filaOrigen = origen.obtenerFila();
		int columnaOrigen = origen.obtenerColumna();
		int filaDestino = destino.obtenerFila();
		int columnaDestino = destino.obtenerColumna();

		if (!estaEnTablero(filaOrigen, columnaOrigen) || !estaEnTablero(filaDestino, columnaDestino)) {
			throw new CoordenadasIncorrectasException(
					"Las coordenadas de la fila de origen o destino no esta en el tablero");
		}

		for (int i = 0; i < Sentido.values().length; i++) {
			if (esEseSentido(Sentido.values()[i], filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
				return Sentido.values()[i];
			}
		}
		return null;

	}

	/**
	 * funcion que recibe todos los sentidos posibles hasta encontrarse con el
	 * sentido correcto, el que corresponde con la celda de origen y destino.
	 * 
	 * @param sentido        - sentido del movimiento
	 * @param filaOrigen     - fila de la celda de origen
	 * @param columnaOrigen  - columna de la celda de origen
	 * @param filaDestino    - fila de la celda de destino
	 * @param columnaDestino - columna de la celda de destino
	 * @return true si se corresponde con el sentido, false en caso contrario
	 */
	private boolean esEseSentido(Sentido sentido, int filaOrigen, int columnaOrigen, int filaDestino,
			int columnaDestino) {

		while (estaEnTablero(filaOrigen, columnaOrigen)) {
			if (filaOrigen == filaDestino && columnaOrigen == columnaDestino) {
				return true;
			}
			filaOrigen = filaOrigen + sentido.obtenerDesplazamientoEnFilas();
			columnaOrigen = columnaOrigen + sentido.obtenerDesplazamientoEnColumnas();
		}
		return false;

	}

	/**
	 * devuelve el tablero en el momento actual, con su estado actual. Viene dado
	 * por los indices de fila (numeros de 1 a 8) y columna(letras de a hasta h), se
	 * indican tambien cada una de las celdas con su correspondiente color (en cada
	 * esquina) y el turno y color correspondiente a la torre (en el centro) o dos
	 * guiones en el caso de que la celda este vacia.
	 * 
	 * @return string con el tablero
	 */
	public String toString() {
		String cadena = new String();
		int numero = 8;

		cadena = "\n     a       b       c       d       e       f       g       h  \n";

		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			cadena = cadena + "\n";
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				cadena = cadena + "    " + matriz.get(i).get(j).obtenerColor().toChar() + ".."
						+ matriz.get(i).get(j).obtenerColor().toChar();
			}
			cadena = cadena + "\n" + numero;
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				cadena = cadena + "   ";
				if (matriz.get(i).get(j).estaVacia()) {
					cadena = cadena + "---- ";
				} else {
					cadena = cadena + "-";

					cadena = cadena + matriz.get(i).get(j).obtenerTorre().toString();

					cadena = cadena + "- ";
				}

			}

			cadena = cadena + "\n";
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				cadena = cadena + "    " + matriz.get(i).get(j).obtenerColor().toChar() + ".."
						+ matriz.get(i).get(j).obtenerColor().toChar();
			}

			numero--;
			cadena = cadena + "\n";
		}

		return cadena;
	}

	/**
	 * comprueba si unas coordenadas estan en el tablero
	 * 
	 * @param fila    - fila
	 * @param columna - columna
	 * @return true si esta, false si no esta
	 */
	private boolean estaEnTablero(int fila, int columna) {
		return (fila < obtenerNumeroFilas() && columna < obtenerNumeroColumnas() && fila >= 0 && columna >= 0);
	}

}
