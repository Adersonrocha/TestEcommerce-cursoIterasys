package stpes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import pages.HomePage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.ProdutoPage;

/**
 * @author Anderson Rocha
 *
 */
public class visualizaProdutosSteps {

	private static WebDriver driver;

	private HomePage homepage = new HomePage(driver);

	@Before
	public static void inicializar() {
		System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	/**
	 * carrega pagina Inicial
	 */
	@Dado("que estou na pagina inivial")
	public void que_estou_na_pagina_inivial() {
		homepage.carregarPaginaInicial();
		assertThat(homepage.obterTituloPagina(), is("Loja de Teste"));

	}

	@Quando("nao estou logado")
	public void nao_estou_logado() {
		assertThat(homepage.estaLogado(), is(false));
	}

	@Entao("visualizo {int} produtos disponiveis")
	public void visualizo_produtos_disponiveis(Integer int1) {
		assertThat(homepage.contarProdutos(),is(int1));
	}

	/**
	 * 
	 */
	@Entao("carrinho zerado")
	public void carrinho_zerado() {
		assertThat(homepage.contarProdutos(),is(0));
	}

	@Dado("que estou na pagina inicial")
	public void que_estou_na_pagina_inicial() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	LoginPage loginPage;
	@Quando("estou logado")
	public void estou_logado() {
		// clica no botao Signin
				loginPage = homepage.ClicarBotaoSignIn();

				// preenche os campos email e senha
				loginPage.preencherEmail("marcelo@teste.com");
				loginPage.preencherPassword("marcelo");

				// clica no botao Signin
				loginPage.clicarBotaoSignIn();

				// confere se o usuario logado � o mesmo
				assertThat(homepage.estaLogado("Marcelo Bittencourt"), is(true));

				// carrega a homepage
				homepage.carregarPaginaInicial();
	}

	String nomeProduto_HomePage;
	String precoProduto_HomePage;
	String nomeProduto_ProdutoPage;
	String precoProduto_ProdutoPage;
	ProdutoPage produtoPage;
	@Quando("seleciono um produto na posicao {int}")
	public void seleciono_um_produto_na_posicao(Integer indice) {
	  
		 nomeProduto_HomePage = homepage.obterNomeProdutoIndice(indice);
		 precoProduto_HomePage = homepage.obterPrecoProdutoIndice(indice);

		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);

		produtoPage = homepage.clicarProduto(indice);

//		ProdutoPage produtoPage = homePage.clicarProduto(indice);
		 nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		 precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
		
	}
	
	
	
	@Quando("nome do produto na tela principal e na tela produto eh {string}")
	public void nome_do_produto_na_tela_principal_eh(String nomeProduto) {
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto.toUpperCase()));
		assertThat(nomeProduto_ProdutoPage, is(nomeProduto.toUpperCase()));
		
	}
	
	
	@Quando("preco do produto na tela principal eh {string}")
	public void preco_do_produto_na_tela_principal_eh(String precoProduto) {
		assertThat(precoProduto_HomePage.toUpperCase(), is(precoProduto.toUpperCase()));
		assertThat(precoProduto_ProdutoPage, is(precoProduto.toUpperCase()));
	}

	ModalProdutoPage modalProdutoPage;
	
	@Quando("adiciono o produto no carrinho com tamanho {string} cor {string} e quantidade {int}")
	public void adiciono_o_produto_no_carrinho_com_tamanho_cor_e_quantidade(String tamanhoProduto, String corProduto, Integer quantidadeProduto) {
		// Seleciona o tamanho
				List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();

				produtoPage.selecionarOpcaoDropDown(tamanhoProduto);

				listaOpcoes = produtoPage.obterOpcoesSelecionadas();

				produtoPage.selecionarCorPreta();

				produtoPage.alterarQuantidade(quantidadeProduto);

				modalProdutoPage = produtoPage.botaoAddCarrinho();

				// assertThat(modalProdutoPage.obterMensagemProdutoAdicionado(),is("Product
				// successfully added to your shopping cart"));
				assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado()
						.endsWith("Product successfully added to your shopping cart"));

				System.out.println(modalProdutoPage.obterMensagemProdutoAdicionado());

				assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));

				assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));

				assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));

	    
	    
	}

	
	
	
	@Entao("o produto aparece na confirmacao com nome {string} preco {string} tamanho {string} cor {string} e quantidade {int}")
	public void o_produto_aparece_na_confirmacao_com_nome_preco_tamanho_cor_e_quantidade(String nomeProduto, String precoProduto, String tamanhoProduto, String corProduto, Integer quantidadeProduto) {
	  
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado()
				.endsWith("Product successfully added to your shopping cart"));


		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));

		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));

		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		
		Double precoProdutoEncontrado = Double.parseDouble(modalProdutoPage.obterPrecoProduto().replace("$",""));
		Double precoProdutoEsperado = Double.parseDouble(precoProduto.replace("$",""));
		
		String subtotalString = modalProdutoPage.obterSubtotal();
		subtotalString = subtotalString.replace("$", "");
		Double subtotal = Double.parseDouble(subtotalString);

		Double subtotalCalculado = quantidadeProduto * precoProdutoEsperado;

		assertThat(subtotal, is(subtotalCalculado));


		
		
	}
	
	
	
	
	
	@After
	public static void finalizar() {
		driver.quit();
	}

}
