package juego.modelo;

import static juego.modelo.Color.AMARILLO;
import static juego.modelo.Color.AZUL;
import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.NARANJA;
import static juego.modelo.Color.PURPURA;
import static juego.modelo.Color.ROJO;
import static juego.modelo.Color.ROSA;
import static juego.modelo.Color.VERDE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import juego.util.Sentido;

/**
 * Pruebas unitarias sobre el tablero.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20181008
 * 
 */
@DisplayName("Tests sobre Tablero")
public class TableroTest {

	/** Texto mal detectado el sentido. */
	private static final String TEXTO_MAL_DETECTADO_EL_SENTIDO = "Mal detectado el sentido";

	/** Texto coordenadas incorrectas. */
	private static final String TEXTO_COORDENADAS_INCORRECTAS = "Coordenadas incorrectas.";

	/** Texto de celda incorrecta. */
	private static final String TEXTO_CELDA_INCORRECTA = "Celda incorrecta.";

	/** Tablero de testing. */
	private Tablero tablero;

	/** Colores en tablero. */
	private static final Color[][] MATRIZ_COLORES = { { NARANJA, AZUL, PURPURA, ROSA, AMARILLO, ROJO, VERDE, MARRON },
			{ ROJO, NARANJA, ROSA, VERDE, AZUL, AMARILLO, MARRON, PURPURA },
			{ VERDE, ROSA, NARANJA, ROJO, PURPURA, MARRON, AMARILLO, AZUL },
			{ ROSA, PURPURA, AZUL, NARANJA, MARRON, VERDE, ROJO, AMARILLO },
			{ AMARILLO, ROJO, VERDE, MARRON, NARANJA, AZUL, PURPURA, ROSA },
			{ AZUL, AMARILLO, MARRON, PURPURA, ROJO, NARANJA, ROSA, VERDE },
			{ PURPURA, MARRON, AMARILLO, AZUL, VERDE, ROSA, NARANJA, ROJO },
			{ MARRON, VERDE, ROJO, AMARILLO, ROSA, PURPURA, AZUL, NARANJA } };

	/** Inicializa valores para cada test. */
	@BeforeEach
	void inicializar() {
		tablero = new Tablero();
	}

	/**
	 * Comprueba que los colores de las celdas del tablero están correctamente
	 * asignados.
	 */
	@Test
	@DisplayName("Colores del tablero bien inicializados.")
	void comprobarColoresEnCeldas() {
		for (int fila = 0; fila < tablero.obtenerNumeroFilas(); fila++) {
			for (int columna = 0; columna < tablero.obtenerNumeroColumnas(); columna++) {
				Celda celda = tablero.obtenerCelda(fila, columna);
				Color color = celda.obtenerColor();
				assertThat("Color de celda mal configurado.", color, is(MATRIZ_COLORES[fila][columna]));
			}
		}
	}

	/**
	 * Rellenado del tablero de torres blancas del mismo color hasta ver que está
	 * completo.
	 */
	@DisplayName("Rellena el tablero de torres blancas hasta completarlo")
	@Test
	void rellenarTableros() {
		Tablero tableroLocal = new Tablero();

		Color[] colores = Color.values();
		for (Color color : colores) {
			for (int ii = 0; ii < tableroLocal.obtenerNumeroFilas(); ii++) {
				for (int jj = 0; jj < tableroLocal.obtenerNumeroColumnas(); jj++) {
					Torre torre = new Torre(Turno.BLANCO, color);
					Celda celda = tableroLocal.obtenerCelda(ii, jj);
					tableroLocal.colocar(torre, celda);
					assertAll("Estado correcto en celdas con torre.",
							() -> assertThat("Celda mal asignada.", torre.obtenerCelda(), is(celda)),
							() -> assertThat("Torre mal asignada.", celda.obtenerTorre(), is(torre)),
							() -> assertFalse("La celda está vacía", celda.estaVacia()));
				}
			}
			assertAll("Estado correcto",
					() -> assertThat("Número de torres incorrecto para color " + color,
							tableroLocal.obtenerNumeroTorres(color), is(64)),
					() -> assertThat("Número de torres incorrecto del turno blanco.",
							tableroLocal.obtenerNumeroTorres(Turno.BLANCO), is(64)));
		}

	}

	/**
	 * Revisa la consulta de celdas en distintas posiciones del tablero.
	 */
	@DisplayName("Comprueba la consulta de celdas en posiciones correctas e incorrectas del tablero")
	@Test
	void comprobarAccesoACeldas() {
		Tablero tableroLocal = new Tablero();
		// coordenadas incorrectas
		int[][] coordenadasIncorrectas = { { -1, -1 }, { 7, -1 }, { -1, 7 }, { 8, 8 } };
		for (int i = 0; i < coordenadasIncorrectas.length; i++) {
			assertNull(tableroLocal.obtenerCelda(coordenadasIncorrectas[i][0], coordenadasIncorrectas[i][1]),
					"La celda no debería estar contenida en el tablero devolviendo un nulo");
		}
		// coordenadas correctas
		int[][] coordenadasCorrectas = { { 4, 3 }, { 0, 0 }, { 7, 7 }, { 0, 7 }, { 7, 0 } };
		for (int i = 0; i < coordenadasCorrectas.length; i++) {
			assertNotNull("La celda sí debería estar contenida en el tablero, no debe devolver un nulo",
					tableroLocal.obtenerCelda(coordenadasCorrectas[i][0], coordenadasCorrectas[i][1]));
		}
	}

	/**
	 * Comprueba la detección del sentido de movimiento entre dos celdas origen y
	 * destino que están en diagonal.
	 */
	@DisplayName("Detección del sentido en el movimiento diagonal entre dos celdas")
	@Test
	void comprobarSentidoEnMovimientoDiagonal() {
		assertAll(
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(0, 0, Color.AMARILLO), new Celda(5, 5, Color.AMARILLO)),
						is(Sentido.DIAGONAL_SE)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(0, 1, Color.AMARILLO), new Celda(4, 5, Color.AMARILLO)),
						is(Sentido.DIAGONAL_SE)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(2, 2, Color.AMARILLO), new Celda(0, 0, Color.AMARILLO)),
						is(Sentido.DIAGONAL_NO)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(5, 5, Color.AMARILLO), new Celda(0, 0, Color.AMARILLO)),
						is(Sentido.DIAGONAL_NO)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(5, 5, Color.AMARILLO), new Celda(3, 7, Color.AMARILLO)),
						is(Sentido.DIAGONAL_NE)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(2, 2, Color.AMARILLO), new Celda(0, 4, Color.AMARILLO)),
						is(Sentido.DIAGONAL_NE)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(4, 6, Color.AMARILLO), new Celda(6, 4, Color.AMARILLO)),
						is(Sentido.DIAGONAL_SO)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(2, 4, Color.AMARILLO), new Celda(6, 0, Color.AMARILLO)),
						is(Sentido.DIAGONAL_SO)));
	}

	/** Genera la cadena de texto correcta para un tablero vacío. */
	@DisplayName("Comprueba la generación de la cadena de texto en toString con tablero vacío")
	@Test
	void comprobarCadenaTextoConTableroVacio() {
		String cadenaEsperada = """
				  a   b   c   d   e   f   g   h
				  
				 N..NZ..ZP..PS..SA..AR..RV..VM..M
				8--------------------------------
				 N..NZ..ZP..PS..SA..AR..RV..VM..M

				 R..RN..NS..SV..VZ..ZA..AM..MP..P
				7--------------------------------
				 R..RN..NS..SV..VZ..ZA..AM..MP..P

				 V..VS..SN..NR..RP..PM..MA..AZ..Z
				6--------------------------------
				 V..VS..SN..NR..RP..PM..MA..AZ..Z

				 S..SP..PZ..ZN..NM..MV..VR..RA..A
				5--------------------------------
				 S..SP..PZ..ZN..NM..MV..VR..RA..A

				 A..AR..RV..VM..MN..NZ..ZP..PS..S
				4--------------------------------
				 A..AR..RV..VM..MN..NZ..ZP..PS..S

				 Z..ZA..AM..MP..PR..RN..NS..SV..V
				3--------------------------------
				 Z..ZA..AM..MP..PR..RN..NS..SV..V

				 P..PM..MA..AZ..ZV..VS..SN..NR..R
				2--------------------------------
				 P..PM..MA..AZ..ZV..VS..SN..NR..R

				 M..MV..VR..RA..AS..SP..PZ..ZN..N
				1--------------------------------
				 M..MV..VR..RA..AS..SP..PZ..ZN..N
				""";

		cadenaEsperada = cadenaEsperada.replaceAll("\\s", "");
		// eliminamos espacios/tabuladores para comparar
		String salida = tablero.toString().replaceAll("\\s", "");
		assertEquals(cadenaEsperada, salida, "La cadena de texto generada para un tablero vacío es incorecta.");
	}

	/**
	 * Genera la cadena de texto correcta para un tablero con algunos peones
	 * colocados en las esquinas del tablero.
	 */
	@DisplayName("Comprueba la generación de la cadena de texto en toString con tablero con algunos peones en las esquinas")
	@Test
	void comprobarCadenaTextoConTableroConPeonesEnEsquinas() {
		String cadenaEsperada = """
				  a   b   c   d   e   f   g   h
				  
				 N..NZ..ZP..PS..SA..AR..RV..VM..M
				8-BA--------------------------BZ-
				 N..NZ..ZP..PS..SA..AR..RV..VM..M

				 R..RN..NS..SV..VZ..ZA..AM..MP..P
				7--------------------------------
				 R..RN..NS..SV..VZ..ZA..AM..MP..P

				 V..VS..SN..NR..RP..PM..MA..AZ..Z
				6--------------------------------
				 V..VS..SN..NR..RP..PM..MA..AZ..Z

				 S..SP..PZ..ZN..NM..MV..VR..RA..A
				5--------------------------------
				 S..SP..PZ..ZN..NM..MV..VR..RA..A

				 A..AR..RV..VM..MN..NZ..ZP..PS..S
				4--------------------------------
				 A..AR..RV..VM..MN..NZ..ZP..PS..S

				 Z..ZA..AM..MP..PR..RN..NS..SV..V
				3--------------------------------
				 Z..ZA..AM..MP..PR..RN..NS..SV..V

				 P..PM..MA..AZ..ZV..VS..SN..NR..R
				2--------------------------------
				 P..PM..MA..AZ..ZV..VS..SN..NR..R

				 M..MV..VR..RA..AS..SP..PZ..ZN..N
				1-BR--------------------------BV-
				 M..MV..VR..RA..AS..SP..PZ..ZN..N
				 """;
		cadenaEsperada = cadenaEsperada.replaceAll("\\s", "");
		tablero.colocar(new Torre(Turno.BLANCO, Color.AMARILLO), tablero.obtenerCelda(0, 0));
		tablero.colocar(new Torre(Turno.BLANCO, Color.AZUL), tablero.obtenerCelda(0, 7));
		tablero.colocar(new Torre(Turno.BLANCO, Color.ROJO), tablero.obtenerCelda(7, 0));
		tablero.colocar(new Torre(Turno.BLANCO, Color.VERDE), tablero.obtenerCelda(7, 7));
		// eliminamos espacios/tabuladores para comparar
		String salida = tablero.toString().replaceAll("\\s", "");
		assertEquals(cadenaEsperada, salida,
				"La cadena de texto generada para un tablero con torres en las esquinas es incorecta.");
	}

	/**
	 * Genera la cadena de texto correcta para un tablero con algunos peones y
	 * damas.
	 */
	@DisplayName("Comprueba la generación de la cadena de texto en toString con tablero con algunos peones y damas")
	@Test
	void comprobarCadenaTextoConTableroConPeonesYDamas() {
		String cadenaEsperada = """
				  a   b   c   d   e   f   g   h
								  
				 N..NZ..ZP..PS..SA..AR..RV..VM..M
				8-BA--------------------------BZ-
				 N..NZ..ZP..PS..SA..AR..RV..VM..M

				 R..RN..NS..SV..VZ..ZA..AM..MP..P
				7--------------------------------
				 R..RN..NS..SV..VZ..ZA..AM..MP..P

				 V..VS..SN..NR..RP..PM..MA..AZ..Z
				6--------------------------------
				 V..VS..SN..NR..RP..PM..MA..AZ..Z

				 S..SP..PZ..ZN..NM..MV..VR..RA..A
				5-------------BR--BV-------------
				 S..SP..PZ..ZN..NM..MV..VR..RA..A

				 A..AR..RV..VM..MN..NZ..ZP..PS..S
				4-------------BP--BS-------------
				 A..AR..RV..VM..MN..NZ..ZP..PS..S

				 Z..ZA..AM..MP..PR..RN..NS..SV..V
				3--------------------------------
				 Z..ZA..AM..MP..PR..RN..NS..SV..V

				 P..PM..MA..AZ..ZV..VS..SN..NR..R
				2--------------------------------
				 P..PM..MA..AZ..ZV..VS..SN..NR..R

				 M..MV..VR..RA..AS..SP..PZ..ZN..N
				1-BM--------------------------BN-
				 M..MV..VR..RA..AS..SP..PZ..ZN..N
				""";

		cadenaEsperada = cadenaEsperada.replaceAll("\\s", "");
		tablero.colocar(new Torre(Turno.BLANCO, Color.AMARILLO), tablero.obtenerCelda(0, 0));
		tablero.colocar(new Torre(Turno.BLANCO, Color.AZUL), tablero.obtenerCelda(0, 7));
		tablero.colocar(new Torre(Turno.BLANCO, Color.ROJO), tablero.obtenerCelda(3, 3));
		tablero.colocar(new Torre(Turno.BLANCO, Color.VERDE), tablero.obtenerCelda(3, 4));

		tablero.colocar(new Torre(Turno.BLANCO, Color.MARRON), tablero.obtenerCelda(7, 0));
		tablero.colocar(new Torre(Turno.BLANCO, Color.NARANJA), tablero.obtenerCelda(7, 7));
		tablero.colocar(new Torre(Turno.BLANCO, Color.PURPURA), tablero.obtenerCelda(4, 3));
		tablero.colocar(new Torre(Turno.BLANCO, Color.ROSA), tablero.obtenerCelda(4, 4));

		// eliminamos espacios/tabuladores para comparar
		String salida = tablero.toString().replaceAll("\\s", "");
		assertEquals(cadenaEsperada, salida, "La cadena de texto generada para un tablero con torres es incorecta.");
	}

	/**
	 * Comprueba la detección del sentido de movimiento entre dos celdas origen y
	 * destino que estén en horizontal o vertical.
	 */
	@DisplayName("Detección del sentido en el movimiento horizontal o vertical entre dos celdas")
	@Test
	void comprobarSentidoEnMovimientoHorizonalOVertical() {
		assertAll(
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(0, 0, Color.AZUL), new Celda(1, 0, Color.AMARILLO)),
						is(Sentido.VERTICAL_S)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(7, 7, Color.MARRON), new Celda(6, 7, Color.ROJO)),
						is(Sentido.VERTICAL_N)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(2, 2, Color.NARANJA), new Celda(2, 3, Color.PURPURA)),
						is(Sentido.HORIZONTAL_E)),
				() -> assertThat(TEXTO_MAL_DETECTADO_EL_SENTIDO,
						tablero.obtenerSentido(new Celda(5, 5, Color.VERDE), new Celda(5, 4, Color.ROSA)),
						is(Sentido.HORIZONTAL_O)));
	}

	/**
	 * Comprueba que devuelve todas las celdas con independencia del orden.
	 */
	@DisplayName("Comprueba que la consulta de todas las celdas devuelve efectivamente todas (con independencia del orden)")
	@Test
	void comprobarObtenerCeldas() {
		Celda[] todas = tablero.obtenerCeldas();
		int encontrada = 0;
		for (int i = 0; i < tablero.obtenerNumeroFilas(); i++) {
			for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
				Celda celda = tablero.obtenerCelda(i, j);
				for (Celda celdaAux : todas) {
					if (celdaAux.tieneCoordenadasIguales(celda)) {
						encontrada++;
						break;
					}
				}
			}
		}
		assertThat("No devuelve todas las celdas", encontrada, is(64));
	}

	/**
	 * Comprueba la conversión de celda a formato texto en notación algebraica.
	 */
	@DisplayName("Conversión de celda a texto en notación algebraica")
	@Test
	void comprobarConversionCeldaATexto() {
		assertAll("conversiones",
				() -> assertThat(TEXTO_COORDENADAS_INCORRECTAS,
						tablero.obtenerCoordenadasEnNotacionAlgebraica(new Celda(0, 0, Color.obtenerColorAleatorio())),
						is("a8")),
				() -> assertThat(TEXTO_COORDENADAS_INCORRECTAS,
						tablero.obtenerCoordenadasEnNotacionAlgebraica(new Celda(7, 0, Color.obtenerColorAleatorio())),
						is("a1")),
				() -> assertThat(TEXTO_COORDENADAS_INCORRECTAS,
						tablero.obtenerCoordenadasEnNotacionAlgebraica(new Celda(0, 7, Color.obtenerColorAleatorio())),
						is("h8")),
				() -> assertThat(TEXTO_COORDENADAS_INCORRECTAS,
						tablero.obtenerCoordenadasEnNotacionAlgebraica(new Celda(7, 7, Color.obtenerColorAleatorio())),
						is("h1")),
				() -> assertThat(TEXTO_COORDENADAS_INCORRECTAS,
						tablero.obtenerCoordenadasEnNotacionAlgebraica(new Celda(4, 4, Color.obtenerColorAleatorio())),
						is("e4")),
				() -> assertThat(TEXTO_COORDENADAS_INCORRECTAS,
						tablero.obtenerCoordenadasEnNotacionAlgebraica(new Celda(3, 3, Color.obtenerColorAleatorio())),
						is("d5")));
	}

	/**
	 * Comprueba la conversión de formato texto en notación algebraica a celda.
	 */
	@DisplayName("Conversión de texto en notación algebraica a celda")
	@Test
	void comprobarConversionTextoACelda() {
		assertAll("conversiones",
				() -> assertTrue(tablero.obtenerCeldaParaNotacionAlgebraica("a8").tieneCoordenadasIguales(
						new Celda(0, 0, Color.obtenerColorAleatorio())), TEXTO_CELDA_INCORRECTA),
				() -> assertTrue(tablero.obtenerCeldaParaNotacionAlgebraica("a1").tieneCoordenadasIguales(
						new Celda(7, 0, Color.obtenerColorAleatorio())), TEXTO_CELDA_INCORRECTA),
				() -> assertTrue(tablero.obtenerCeldaParaNotacionAlgebraica("h8").tieneCoordenadasIguales(
						new Celda(0, 7, Color.obtenerColorAleatorio())), TEXTO_CELDA_INCORRECTA),
				() -> assertTrue(tablero.obtenerCeldaParaNotacionAlgebraica("h1").tieneCoordenadasIguales(
						new Celda(7, 7, Color.obtenerColorAleatorio())), TEXTO_CELDA_INCORRECTA),
				() -> assertTrue(tablero.obtenerCeldaParaNotacionAlgebraica("e4").tieneCoordenadasIguales(
						new Celda(4, 4, Color.obtenerColorAleatorio())), TEXTO_CELDA_INCORRECTA),
				() -> assertTrue(tablero.obtenerCeldaParaNotacionAlgebraica("d5").tieneCoordenadasIguales(
						new Celda(3, 3, Color.obtenerColorAleatorio())), TEXTO_CELDA_INCORRECTA),
				() -> assertNull(tablero.obtenerCeldaParaNotacionAlgebraica("i5"), TEXTO_CELDA_INCORRECTA),
				() -> assertNull(tablero.obtenerCeldaParaNotacionAlgebraica("a9"), TEXTO_CELDA_INCORRECTA),
				() -> assertNull(tablero.obtenerCeldaParaNotacionAlgebraica("x9"), TEXTO_CELDA_INCORRECTA));
	}

	/**
	 * Proveedor de argumentos con jugadas y ganador.
	 * 
	 * @return coordenadas y torress en fila inferior de torres negras
	 */
	static Stream<Arguments> proveedorTorrePosicion() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(new Torre(Turno.NEGRO, Color.AMARILLO), "a1"),
				arguments(new Torre(Turno.NEGRO, Color.VERDE), "b4"),
				arguments(new Torre(Turno.BLANCO, Color.AZUL), "e6"),
				arguments(new Torre(Turno.BLANCO, Color.MARRON), "h8"));
	}

	/**
	 * Prueba la búsqueda de torre de un determinado turno y color.
	 * 
	 * @param torre    torre
	 * @param posicion posición en notación algebraica donde colocar la torre
	 */
	@DisplayName("Busqueda de torre.")
	@ParameterizedTest
	@MethodSource("proveedorTorrePosicion")
	void encontrarTorreColor(Torre torre, String posicion) {
		Celda celda = tablero.obtenerCeldaParaNotacionAlgebraica(posicion);
		tablero.colocar(torre, celda);
		assertThat("La celda donde se encuentra la torre es incorrecta.",
				tablero.buscarTorre(torre.obtenerTurno(), torre.obtenerColor()), is(celda));
	}

	/**
	 * Prueba la búsqueda de torre de un determinado turno y color en un tablero
	 * vacío sin colocar dicha torre.
	 * 
	 * @param torre    torre
	 * @param posicion posición en notación algebraica donde buscar la torre
	 */
	@DisplayName("Busqueda de torre que no se ha colocado.")
	@ParameterizedTest
	@MethodSource("proveedorTorrePosicion")
	void buscarTorreColorNoColocada(Torre torre, String posicion) {
		// NO COLOCAMOS LA TORRE Y NO DEBERÍA ENCONTRARLA
		assertNull(tablero.buscarTorre(torre.obtenerTurno(), torre.obtenerColor()),
				"No debería encontar una torre que no está colocada en el tablero.");
	}

	/**
	 * Prueba el número de torres en un tablero vacío.
	 */
	@DisplayName("Número de torres en tablero vacío.")
	@Test
	void contarTorresEnTableroVacío() {
		assertAll("Número de torres en tablero vacío",
				() -> assertThat("Numero incorrecto de torres negras en tablero vacío.",
						tablero.obtenerNumeroTorres(Turno.NEGRO), is(0)),
				() -> assertThat("Numero incorrecto de torres blancas en tablero vacío.",
						tablero.obtenerNumeroTorres(Turno.BLANCO), is(0)));
	}

	/**
	 * Traducción de notación algebraica a la celda correspondiente.
	 * 
	 * @param texto   texto en notación algebraica
	 * @param fila    fila
	 * @param columna columna
	 */
	@DisplayName("Traduccion a notación algebraica.")
	@ParameterizedTest
	@CsvSource({ "a8, 0, 0", "b6, 2, 1", "c4, 4, 2", "d2, 6, 3", "e7, 1, 4", "f1, 7, 5", "g5, 3, 6", "h3, 5, 7" })
	void probarTraducciónNotacionAlgebraica(String texto, int fila, int columna) {
		Celda celda = tablero.obtenerCeldaParaNotacionAlgebraica(texto);
		assertAll("coordenadasCorrectas", () -> assertThat("Fila incorrecta.", celda.obtenerFila(), is(fila)),
				() -> assertThat("Columnas incorrecta.", celda.obtenerColumna(), is(columna)));

	}

	/**
	 * Traducción no posible con textos incorrectos.
	 * 
	 * @param texto texto con las coordenadas de la celda en notación algebraica
	 */
	@DisplayName("Traduccion no posible notación algebraica.")
	@ParameterizedTest
	@CsvSource({ "a9", "z0", "l1", "i0", "i9", "z8", "*0" })
	void probarTraducciónNoPosibleNotacionAlgebraica(String texto) {
		Celda celda = tablero.obtenerCeldaParaNotacionAlgebraica(texto);
		assertNull(celda, "La celda debería ser null, porque no se puede traducir.");
	}

	/**
	 * Traducción de jugada en notación algebraica a las celdas correspondientes.
	 * 
	 * @param texto   		jugada en notación algebraica
	 * @param filaOrigen 	fila origen
	 * @param columnaOrigen columna origen
	 * @param filaDestino 	fila destino
	 * @param columnaDestino columna destino
	 */
	@DisplayName("Traduccion a notación algebraica de jugadas completas.")
	@ParameterizedTest
	@CsvSource({ "a8d5, 0, 0, 3, 3", "b6b3, 2, 1, 5, 1", "c4a2, 4, 2, 6, 0", "d2h6, 6, 3, 2, 7", "e7e2, 1, 4, 6, 4",
			"f1a6, 7, 5, 2, 0", "g5d2, 3, 6, 6, 3", "h3d7, 5, 7, 1, 3" })
	void probarTraducciónNotacionAlgebraicaDeJugadas(String texto, int filaOrigen, int columnaOrigen, int filaDestino,
			int columnaDestino) {
		Celda celdaOrigen = tablero.obtenerCeldaOrigenEnJugada(texto);
		Celda celdaDestino = tablero.obtenerCeldaDestinoEnJugada(texto);
		assertAll("traduccionJugadasCompletas",
				() -> assertThat("Fila incorrecta en origen.", celdaOrigen.obtenerFila(), is(filaOrigen)),
				() -> assertThat("Columnas incorrecta en origen.", celdaOrigen.obtenerColumna(), is(columnaOrigen)),
				() -> assertThat("Fila incorrecta en destino.", celdaDestino.obtenerFila(), is(filaDestino)),
				() -> assertThat("Columnas incorrecta en destino.", celdaDestino.obtenerColumna(), is(columnaDestino)));
	}

	/**
	 * Traducción no posible con textos incorrectos en jugadas completas.
	 * 
	 * @param texto texto en notación algebraica de una jugada con celda origen y destino
	 */
	@DisplayName("Traduccion no posible en notación algebraica con jugadas completas incorrectas.")
	@ParameterizedTest
	@CsvSource({ "a9h0", "z0a9", "a9l1", "h0i0", "d9i9", "z8e0", "a9*0", "a0c2a", "a1", "a", "z", "0" })
	void probarTraducciónNoPosibleNotacionAlgebraicaJugadasCompletas(String texto) {
		Celda celdaOrigen = tablero.obtenerCeldaOrigenEnJugada(texto);
		Celda celdaDestino = tablero.obtenerCeldaDestinoEnJugada(texto);
		assertAll("traduccionJugadasCompletasIncorrectas",
				() -> assertNull(celdaOrigen, "La celda origen debería ser null, porque no se puede traducir."),
				() -> assertNull(celdaDestino, "La celda destino debería ser null, porque no se puede traducir."));
	}

	/**
	 * Comprueba la colocación en celdas con notación algebraica.
	 * 
	 * @param texto   texto en notación algebraica
	 * @param fila    fila
	 * @param columna columna
	 * @param color   color de la celda
	 */
	@DisplayName("Colocar en el tablero con notación algebraica.")
	@ParameterizedTest
	@CsvSource({ "a8, 0, 0, NARANJA" })
	void comprobarColocarConNotacionAlgebraica(String texto, int fila, int columna, Color color) {
		Celda celdaInicial = tablero.obtenerCelda(fila, columna);
		assertTrue(celdaInicial.estaVacia(), "Debería estar la celda vacía incialmente");
		tablero.colocar(new Torre(Turno.NEGRO, Color.AMARILLO), texto);
		final Celda celda = tablero.obtenerCelda(fila, columna);
		assertAll(() -> assertThat("Fila incorrecta", celda.obtenerFila(), is(fila)),
				() -> assertThat("Columna incorrecta", celda.obtenerColumna(), is(columna)),
				() -> assertThat("Color de celda incorrecto", celda.obtenerColor(), is(color)),
				() -> assertThat("Torre con color incorrecto", celda.obtenerColorDeTorre(), is(Color.AMARILLO)),
				() -> assertThat("Turno de torre incorrecto", celda.obtenerTurnoDeTorre(), is(Turno.NEGRO)));

	}

	/**
	 * Comprueba que no hay torres entre medías de dos celdas colocas en un sentido
	 * correcto.
	 * 
	 * @param fo fila origen
	 * @param co columna origen
	 * @param fd fila destino
	 * @param cd columna destino
	 */
	@DisplayName("Comprobar vacías en tablero vacío con sentidos correctos.")
	@ParameterizedTest
	@CsvSource({ "0, 0, 7, 7", "0, 0, 0, 7", "7, 0, 7, 7", "2, 2, 1, 3", "3, 4, 3, 1", "6, 4, 2, 4", "5, 6, 6, 5",
			"6, 6, 5, 5" })
	void comprobarCeldasVacíasEntreOrigenYDestinoConSentidoCorrecto(int fo, int co, int fd, int cd) {
		Celda origen = tablero.obtenerCelda(fo, co);
		Celda destino = tablero.obtenerCelda(fd, cd);
		assertThat("Las celdas deberían estar vacías", tablero.estanVaciasCeldasEntre(origen, destino), is(true));
	}

	/**
	 * Comprueba que no hay torres entre medias de dos celdas colocas en un sentido
	 * incorrecto.
	 * 
	 * @param fo fila origen
	 * @param co columna origen
	 * @param fd fila destino
	 * @param cd columna destino
	 */
	@DisplayName("Comprobar vacías en tablero vacío con sentidos incorrectos.")
	@ParameterizedTest
	@CsvSource({ "0, 0, 7, 6", "0, 0, 1, 7", "7, 0, 6, 7", "2, 2, 1, 6", "3, 4, 4, 1", "6, 4, 1, 3", "5, 6, 3, 5",
			"6, 6, 4, 5" })
	void comprobarCeldasVacíasEntreOrigenYDestinoConSentidoIncorrecto(int fo, int co, int fd, int cd) {
		Celda origen = tablero.obtenerCelda(fo, co);
		Celda destino = tablero.obtenerCelda(fd, cd);
		assertThat("Las celdas deberían estar vacías porque no están en un sentido bien definido",
				tablero.estanVaciasCeldasEntre(origen, destino), is(false));
	}

	/**
	 * Método auxiliar para inicializar el tablero con torres.
	 * 
	 * @param tableroAuxiliar tablero auxiliar a inicializar
	 */
	//  @formatter:off
	/* Partimos de un tablero que debe tener este estado.
	*   a  b  c  d  e  f  g  h  
	* 8 BN -- BP -- -- BR -- BM 
	* 7 -- BZ -- -- -- -- BV --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- BS BA -- -- --
	* 4 -- -- -- NA NS -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- NV -- -- -- -- NZ -- 
	* 1 NM -- NR -- -- NP -- NN
	*/
	// @formatter:on
	private void iniciarTableroAux(Tablero tableroAuxiliar) {
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.NARANJA), 0, 0);
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.AZUL), 1, 1);
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.PURPURA), 0, 2);
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.ROSA), 3, 3);
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.AMARILLO), 3, 4);
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.ROJO), 0, 5);
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.VERDE), 1, 6);
		tableroAuxiliar.colocar(new Torre(Turno.BLANCO, Color.MARRON), 0, 7);
		//
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.MARRON), 7, 0);
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.VERDE), 6, 1);
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.ROJO), 7, 2);
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.AMARILLO), 4, 3);
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.ROSA), 4, 4);
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.PURPURA), 7, 5);
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.AZUL), 6, 6);
		tableroAuxiliar.colocar(new Torre(Turno.NEGRO, Color.NARANJA), 7, 7);
	}

	/**
	 * Comprueba que se comprueba bien la condición de vacías entre medias de dos
	 * celdas.
	 * 
	 * @param filaOrigen     fila origen
	 * @param columnaOrigen  columna origen
	 * @param filaDestino    fila destino
	 * @param columnaDestino columna destino
	 */
	//  @formatter:off
	/* Partimos de un tablero que debe tener este estado.
	*   a  b  c  d  e  f  g  h  
	* 8 BN -- BP -- -- BR -- BM 
	* 7 -- BZ -- -- -- -- BV --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- BS BA -- -- --
	* 4 -- -- -- NA NS -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- NV -- -- -- -- NZ -- 
	* 1 NM -- NR -- -- NP -- NN
	*/
	// @formatter:on
	@DisplayName("Comprobar vacías entre celdas.")
	@ParameterizedTest
	@CsvSource({ "0, 0, 7, 0", "0, 0, 1, 1", "0, 0, 0, 2", "1, 1, 3, 3", "1, 1, 1, 6", "1, 1, 6, 1", "0, 2, 0, 0", "0, 2, 0, 5",
			"0, 2, 5, 7", "0, 2, 7, 2", 
			"3, 3, 1, 1", "3, 3, 3, 4", "3, 3, 4, 3", "3, 3, 4, 4", 
			"3, 4, 3, 3", "3, 4, 4, 3", "3, 4, 4, 4",
			"4, 3, 3, 3", "4, 3, 4, 4", "4, 3, 3, 4",
			"6, 1, 7, 0", "6, 1, 7, 2", "6, 1, 1, 1", "6, 1, 4, 3", "6, 1, 6, 6"})
	void comprobarVacíasEntreCeldas(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
		Tablero tableroAuxiliar = new Tablero();
		iniciarTableroAux(tableroAuxiliar);
		Celda celdaOrigen = tableroAuxiliar.obtenerCelda(filaOrigen, columnaOrigen);
		Celda celdaDestino = tableroAuxiliar.obtenerCelda(filaDestino, columnaDestino);
		assertThat(
				"Debería estar vacía entre celdas con origen en " + celdaOrigen.toString() + " a destino "
						+ celdaDestino.toString(),
				tableroAuxiliar.estanVaciasCeldasEntre(celdaOrigen, celdaDestino), is(true));

	}
	
	/**
	 * Comprueba que se comprueba bien la condición de no vacías entre medias de dos
	 * celdas.
	 * 
	 * @param filaOrigen     fila origen
	 * @param columnaOrigen  columna origen
	 * @param filaDestino    fila destino
	 * @param columnaDestino columna destino
	 */
	//  @formatter:off
	/* Partimos de un tablero que debe tener este estado.
	*   a  b  c  d  e  f  g  h  
	* 8 BN -- BP -- -- BR -- BM 
	* 7 -- BZ -- -- -- -- BV --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- BS BA -- -- --
	* 4 -- -- -- NA NS -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- NV -- -- -- -- NZ -- 
	* 1 NM -- NR -- -- NP -- NN
	*/
	// @formatter:on
	@DisplayName("Comprobar NO vacías entre celdas.")
	@ParameterizedTest
	@CsvSource({ "0, 0, 0, 5", "0, 0, 0, 7", "0, 0, 3, 3", "0, 0, 4, 4",
		"1, 1, 4, 4", "1, 1, 6, 6", "1, 1, 7, 7", "1, 1, 7, 1",
		"0, 2, 0, 7", "0, 2, 2, 0",
		"0, 5, 0, 0", "0, 5, 2, 7",
		"6, 1, 3, 4", "6, 1, 0, 1", "6, 1, 0, 7,",
		"6, 6, 3, 3", "6, 6, 0, 6", "6, 6, 0, 0", "6, 6, 1, 1",
		"7, 7, 4, 4", "7, 7, 7, 0", "7, 7, 7, 2", "7, 7, 3, 3", "7, 7, 0, 0"		
	})
	void comprobarNoVacíasEntreCeldas(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
		Tablero tableroAuxiliar = new Tablero();
		iniciarTableroAux(tableroAuxiliar);
		Celda celdaOrigen = tableroAuxiliar.obtenerCelda(filaOrigen, columnaOrigen);
		Celda celdaDestino = tableroAuxiliar.obtenerCelda(filaDestino, columnaDestino);
		assertThat(
				"Debería NO estar vacía entre celdas con origen en " + celdaOrigen.toString() + " a destino "
						+ celdaDestino.toString(),
				tableroAuxiliar.estanVaciasCeldasEntre(celdaOrigen, celdaDestino), is(false));

	}
	

	/**
	 * Comprueba que se convierte bien la jugada a formato texto en
	 * notación algebraica clásica.
	 * 
	 * @param filaO fila origen
	 * @param colO columna origen
	 * @param filaD fila destino
	 * @param colD columna destino
	 * @param texto texto 
	 */
	@DisplayName("Conversión de jugadas a formato coordenadas en notación algebrica")
	@ParameterizedTest
	@CsvSource({ "0,0,0,1,a8b8", "0,0,7,7,a8h1", "0,7,4,4,h8e4", "7,0,3,3,a1d5"})
	void comprobarConversionCoordenadasATexto(int filaO, int colO, int filaD, int colD, String texto) {
		Celda origen = tablero.obtenerCelda(filaO, colO);
		Celda destino = tablero.obtenerCelda(filaD,  colD);
		assertThat(tablero.obtenerJugadaEnNotacionAlgebraica(origen, destino), is(texto));
	}
	
	
	/**
	 * Comprueba movimientos de torre.
	 * 
	 * @param filaOrigen     fila origen
	 * @param columnaOrigen  columna origen
	 * @param filaDestino    fila destino
	 * @param columnaDestino columna destino
	 */
	//  @formatter:off
	/* Partimos de un tablero que debe tener este estado.
	*   a  b  c  d  e  f  g  h  
	* 8 BN -- BP -- -- BR -- BM 
	* 7 -- BZ -- -- -- -- BV --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- BS BA -- -- --
	* 4 -- -- -- NA NS -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- NV -- -- -- -- NZ -- 
	* 1 NM -- NR -- -- NP -- NN
	*/
	// @formatter:on
	@DisplayName("Comprobar movimientos de torre.")
	@ParameterizedTest
	@CsvSource({    
		"1, 1, 2, 1",
		"3, 3, 2, 2",
		"4 ,4, 3, 5",
		"6, 1, 7, 1"
	})
	void comprobarMovimientosDeTorre(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
		Tablero tableroAuxiliar = new Tablero();
		iniciarTableroAux(tableroAuxiliar);
		Celda celdaOrigen = tableroAuxiliar.obtenerCelda(filaOrigen, columnaOrigen);
		Torre torre = celdaOrigen.obtenerTorre();
		Celda celdaDestino = tableroAuxiliar.obtenerCelda(filaDestino, columnaDestino);
		tableroAuxiliar.moverTorre(celdaOrigen, celdaDestino);
		assertAll("moverTorre",
				() -> assertThat("El origen debería estar vacío.", celdaOrigen.estaVacia(), is(true)),
				() -> assertThat("El destino no debería estar vacío.", celdaDestino.estaVacia(), is(false)),
				() -> assertThat("La torre no es la correcta.", celdaDestino.obtenerTorre(), is(torre))				
				);

	}

	
	/**
	 * Comprueba la conversión de celda no perteneciente al tablero a notación algebraica.
	 *
	 * @param fila fila
	 * @param columna columna
	 */
	@DisplayName("Conversión a notación algebraica de celdas fuera del tablero")
	@ParameterizedTest
	@CsvSource({
		// @formatter:off
		"-1, -1", "0, -1", "-1, 0", "8, 8", "8, 7", "7, 8", "0, 8",
		"8, 0", "100, 100", "-100, -100", "1000, 1000"
	// 	@formatter:on
	})
	void conversionTextoCeldasFueraDelTablero(int fila, int columna) {
		Celda celda = new Celda(fila, columna, Color.AMARILLO);
		String notacionAlgebraica = tablero.obtenerCoordenadasEnNotacionAlgebraica(celda);
		assertThat("Texto mal generado en notación algebraica si la celda está fuera del tablero.", 
				notacionAlgebraica, is("--"));
	}
		
}
