package juego.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias sobre el tipo enumerado.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * 
 */
@DisplayName("Tests sobre Color")
public class ColorTest {
	
	/**
	 * Comprueba el número de valores definidos.
	 */
	@Test
	@DisplayName("Numero de valores definido en la enumeración.")
	void numeroDeValoresEnColor() {
		assertThat("El número de valores es incorrecto.", Color.values().length, is(8));
	}
	
	/**
	 * Correcta inicialización de letras para cada valor.
	 */
	@DisplayName("Letras correctas para cada valor.")
	@Test
	public void probarLetra() {
		assertAll("letras correctas",
			() -> assertThat(Color.AMARILLO.toChar(), is('A')),
			() -> assertThat(Color.AZUL.toChar(), is('Z')),
			() -> assertThat(Color.MARRON.toChar(), is('M')),
			() -> assertThat(Color.NARANJA.toChar(), is('N')),
			() -> assertThat(Color.PURPURA.toChar(), is('P')),
			() -> assertThat(Color.ROJO.toChar(), is('R')),
			() -> assertThat(Color.ROSA.toChar(), is('S')),
			() -> assertThat(Color.VERDE.toChar(), is('V'))
			);			
	} 
}
