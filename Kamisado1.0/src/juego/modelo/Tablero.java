package juego.modelo;

import juego.util.Sentido;

/**
 * clase con el tablero para el juego, donde se crearan las celdas
 * 
 * @author mario lopez matamala
 * @version 2.1
 * @since java JDK 16
 *
 */
public class Tablero {

	/**
	 * tamaño fijo del tablero (8*8)
	 */
	private final static int RANGO = 8;

	/**
	 * array de celdas, donde iran las celdas del tablero
	 */
	private Celda[][] matriz = new Celda[RANGO][RANGO];

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
	 * constructor de tablero, inicializa la matriz de celdas
	 */
	public Tablero() {

		for (int i = 0, contador = 0; i < RANGO; i++) {
			for (int j = 0; j < RANGO; j++) {
				matriz[i][j] = new Celda(i, j, colores[contador]);
				contador++;
			}

		}

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
				if (obtenerCelda(i, j).obtenerTurnoDeTorre() == turno
						&& obtenerCelda(i, j).obtenerColorDeTorre() == color) {
					return obtenerCelda(i, j);
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
	 */
	public void colocar(Torre torre, Celda celda) {
		torre.establecerCelda(celda);
		celda.establecerTorre(torre);
	}

	/**
	 * dada un celda en notacion algebraica, coloca la torre en esa posicion.
	 * previamente comprueba si los valores son correctos
	 * 
	 * @param torre              - torre
	 * @param notacionAlgebraica - celda dada en notacion algebraica
	 */
	public void colocar(Torre torre, String notacionAlgebraica) {
		if (notacionAlgebraica.length() <= 2 || notacionAlgebraica.length() < 1) {
			torre.establecerCelda(obtenerCeldaParaNotacionAlgebraica(notacionAlgebraica));
			obtenerCeldaParaNotacionAlgebraica(notacionAlgebraica).establecerTorre(torre);
		}

	}

	/**
	 * coloca la torre en una celda dada como dos enteros, fila - columna
	 * 
	 * @param torre   - torre
	 * @param fila    - fila de la celda
	 * @param columna - columna de la celda
	 */
	public void colocar(Torre torre, int fila, int columna) {

		if (estaEnTablero(fila, columna)) {

			torre.establecerCelda(obtenerCelda(fila, columna));
			obtenerCelda(fila, columna).establecerTorre(torre);
		}

	}

	/**
	 * comprueba si no hay torres entre dos celdas, es decir, si estan vacias. Si
	 * son consecutivas (sin celdas entre medias), se cuenta como que estan vacias.
	 * 
	 * @param origen  - celda de origen
	 * @param destino - celda de destino
	 * @return true si estan vacias, false si hay torres entre medias o el sentido
	 *         no es correcto
	 */
	public boolean estanVaciasCeldasEntre(Celda origen, Celda destino) {
		int filaOrigen = origen.obtenerFila();
		int columnaOrigen = origen.obtenerColumna();
		final int filaDestino = destino.obtenerFila();
		final int columnaDestino = destino.obtenerColumna();

		boolean bool = true;// comprueba si se ha topado la celda origen con la celda destino

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
	 * mueve la torre desde la celda origen hasta la celda destino
	 * 
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 */
	public void moverTorre(Celda origen, Celda destino) {

		Torre torre;
		torre = origen.obtenerTorre();
		origen.eliminarTorre();

		destino.establecerTorre(torre);
	}

	/**
	 * dada la fila y columna, obtiene la referencia a la celda
	 * 
	 * @param fila    - fila de la celda
	 * @param columna - columna de la celda
	 * @return celda referenciada
	 */
	public Celda obtenerCelda(int fila, int columna) {
		if (fila >= 0 && columna >= 0 && fila < obtenerNumeroFilas() && columna < obtenerNumeroColumnas()) {
			return matriz[fila][columna];
		}
		return null;

	}

	/**
	 * dadas unas coordenadas en notacion algebraica, devuelve la celda de origen.
	 * Por ejemplo, a4b7, se queda con b7 y lo transforma a la celda quedandose con
	 * sus propias coordenadas.
	 * 
	 * @param textoJugada - celda dada en notacion algebraica
	 * @return celda de origen o null en caso de que sea incorrecto
	 */
	public Celda obtenerCeldaDestinoEnJugada(String textoJugada) {
		if (textoJugada.length() <= 3 || textoJugada.length() > 4) {
			return null;
		}
		return obtenerCeldaParaNotacionAlgebraica(textoJugada.substring(2, 4));

	}

	/**
	 * dadas unas coordenadas en notacion algebraica, devuelve la celda de origen.
	 * Por ejemplo, a4b7, se queda con a4 y lo transforma a la celda quedandose con
	 * sus propias coordenadas
	 * 
	 * @param textoJugada - celda dada en notacion algebraica
	 * @return celda de destino o null en caso de que sea incorrecto
	 */
	public Celda obtenerCeldaOrigenEnJugada(String textoJugada) {
		if (textoJugada.length() <= 3 || textoJugada.length() > 4) {
			return null;
		}
		return obtenerCeldaParaNotacionAlgebraica(textoJugada.substring(0, 2));
	}

	/**
	 * obtiene la celda dada en notacion algebraica
	 * 
	 * @param texto - dado en notacion algebraica
	 * @return celda o null en caso de que sea incorrecto
	 */
	public Celda obtenerCeldaParaNotacionAlgebraica(String texto) {

		char letra1 = texto.charAt(0);
		int numero1 = Integer.parseInt(Character.toString(texto.charAt(1)));

		if (texto.length() > 2 || texto.length() <= 1 || letra1 < 'a' || letra1 > 'h' || numero1 < 1 || numero1 > 8) {
			return null;
		}

		char letra2 = 'a';
		int numero2 = 8;
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {

				if (letra2 == letra1 && numero2 == numero1) {
					return obtenerCelda(i, j);
				}
				letra2++;
			}
			letra2 = 'a';
			numero2--;

		}

		return null;
	}

	/**
	 * obtiene un array de una dimension con todas las celdas del tablero
	 * 
	 * @return array con las celdas
	 */
	public Celda[] obtenerCeldas() {

		Celda array[] = new Celda[obtenerNumeroFilas() * obtenerNumeroColumnas()];

		int l = 0;// posiciones del array
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				array[l] = matriz[i][j];
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
	 */
	public String obtenerCoordenadasEnNotacionAlgebraica(Celda celda) {

		char letra = 'a';
		char letra2;
		int numero = RANGO;
		String cadena = "";

		if (!estaEnTablero(celda.obtenerFila(), celda.obtenerColumna())) {
			cadena = "--";
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
	 * obtiene la notacion algebraica de una jugada, dado dos celdas. Con las celdas
	 * 6 5 y 4 7 devolvera su correspondiente notacion algebraica
	 *
	 * @param origen  - celda origen
	 * @param destino - celda destino
	 * @return cadena con el string
	 */
	public String obtenerJugadaEnNotacionAlgebraica(Celda origen, Celda destino) {

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
		return matriz[0].length;
	}

	/**
	 * obtiene el numero de filas del tablero
	 * 
	 * @return tamaño en filas
	 */
	public int obtenerNumeroFilas() {
		return matriz.length;
	}

	/**
	 * obtiene el numero de torres de un determinado color
	 * 
	 * @param color - color
	 * @return contador - numero de torres
	 */
	public int obtenerNumeroTorres(Color color) {
		int contador = 0;
		if (estaVacio()) {
			return 0;
		}
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				if (obtenerCelda(i, j).obtenerTorre() != null) {
					if (obtenerCelda(i, j).obtenerTorre().obtenerColor() == color) {
						contador++;
					}
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
		if (estaVacio()) {
			return 0;
		}
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				if (obtenerCelda(i, j).obtenerTorre() != null) {
					if (obtenerCelda(i, j).obtenerTorre().obtenerTurno() == turno) {
						contador++;
					}
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
	 */
	public Sentido obtenerSentido(Celda origen, Celda destino) {

		int filaOrigen = origen.obtenerFila();
		int columnaOrigen = origen.obtenerColumna();
		int filaDestino = destino.obtenerFila();
		int columnaDestino = destino.obtenerColumna();

		for (int i = 0; i < Sentido.values().length; i++) {
			if (esEseSentido(Sentido.values()[i], filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
				return Sentido.values()[i];
			}
		}
		return null;

	}

	/**
	 * funcion de "apoyo" hacia la anterior obtenerSentido. Comprueba si el sentido
	 * que se le pasa es realmente ese sentido entre las dos celdas. Para ello
	 * recorre el bucle moviendose por ese sentido hasta que se topa con la celda
	 * destino. Si se topa es que se corresponde con el sentido
	 * 
	 * @param sentido        - enumeracion correspondiente a la clase sentido
	 * @param filaOrigen     - fila de la celda de origen
	 * @param columnaOrigen  - columna de la celda de origen
	 * @param filaDestino    - fila de la celda de destino
	 * @param columnaDestino - columna de la celda destino
	 * @return true si el sentido es el correcto, false en caso contrario
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
				cadena = cadena + "    " + matriz[i][j].obtenerColor().toChar() + ".."
						+ matriz[i][j].obtenerColor().toChar();
			}
			cadena = cadena + "\n" + numero;
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				cadena = cadena + "   ";
				if (matriz[i][j].estaVacia()) {
					cadena = cadena + "---- ";
				} else {
					cadena = cadena + "-" + matriz[i][j].obtenerTurnoDeTorre().toChar() + ""
							+ matriz[i][j].obtenerColorDeTorre().toChar() + "- ";
				}

			}

			cadena = cadena + "\n";
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				cadena = cadena + "    " + matriz[i][j].obtenerColor().toChar() + ".."
						+ matriz[i][j].obtenerColor().toChar();
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

	/**
	 * comprueba si el tablero esta vacio, es decir, si en las celdas no hay ninguna
	 * torre.
	 * 
	 * @return true si esta vacio, false si no lo esta
	 */
	private boolean estaVacio() {
		for (int i = 0; i < obtenerNumeroFilas(); i++) {
			for (int j = 0; j < obtenerNumeroColumnas(); j++) {
				if (!obtenerCelda(i, j).estaVacia()) {
					return false;
				}
			}
		}
		return true;
	}
}
