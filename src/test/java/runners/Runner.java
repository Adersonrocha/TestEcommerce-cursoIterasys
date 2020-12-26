package runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;



/**
 * 
 * @author Anderson Rocha
 *features = "src\\test\\resources\\features\\visualizaProdutos.feature", caminho a partir do src
			glue ="steps", pacote onde estao as classes de teste
			tags ="@fluxopadrao", fluxo que desejo executar
			plugin = "pretty",
			monochrome=true
 */
@RunWith(Cucumber.class)
@CucumberOptions(
			features = "src\\test\\resources\\features\\comprar_produto.feature",
			glue ="steps",
			tags ="@fluxopadrao",
			plugin = "pretty",
			monochrome=true
		)
public class Runner {

	
	
	
}
