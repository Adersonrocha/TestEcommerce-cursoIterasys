package homepage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.BaseTests;
import pages.CarrinhoPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.PedidoPage;
import pages.ProdutoPage;
import util.Funcoes;

public class HomePageTests extends BaseTests {

	@Test
	public void testContarProdutos() {
		carregarPageHome();
		assertThat(homePage.contarProdutos(), is(8));

	}

	LoginPage loginPage;
	ProdutoPage produtoPage;
	ModalProdutoPage modalProdutoPage;

	@Test
	public void testValidarCarrinhoVazio() {
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosCarrinho();
		assertThat(produtosNoCarrinho, is(0));

	}

	@Test
	public void testValidarDetalhesDoProduto() {
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProdutoIndice(indice);
		String precoProduto_HomePage = homePage.obterPrecoProdutoIndice(indice);

		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);

		produtoPage = homePage.clicarProduto(indice);

//		ProdutoPage produtoPage = homePage.clicarProduto(indice);
		String nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
//		
		System.out.println(nomeProduto_ProdutoPage);
		System.out.println(precoProduto_ProdutoPage);

		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
		
		//carregarPageHome();
	}

	@Test
	public void testLoginComSucesso_UsuarioLogado() {

		// clica no botao Signin
		loginPage = homePage.ClicarBotaoSignIn();

		// preenche os campos email e senha
		loginPage.preencherEmail("marcelo@teste.com");
		loginPage.preencherPassword("marcelo");

		// clica no botao Signin
		loginPage.clicarBotaoSignIn();

		// confere se o usuario logado � o mesmo
		assertThat(homePage.estaLogado("Marcelo Bittencourt"), is(true));

		// carrega a homepage
		carregarPageHome();

	}
	String tamanhoProduto = "M";
	String corProduto = "Black";
	int quantidadeProduto = 2;
	@Test
	public void IncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() {

			
		

		// Pr�-condi��o o usuario tem que est� logado, se nao tiver ele loga
		if (!homePage.estaLogado("Marcelo Bittencourt")) {
			testLoginComSucesso_UsuarioLogado();
		}

		// seleciona o produto
		testValidarDetalhesDoProduto();

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


		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));

		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));

		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));

		String precoProdutoString = modalProdutoPage.obterPrecoProduto();
		precoProdutoString = precoProdutoString.replace("$", "");
		Double precoProduto = Double.parseDouble(precoProdutoString);

		String subtotalString = modalProdutoPage.obterSubtotal();
		subtotalString = subtotalString.replace("$", "");
		Double subtotal = Double.parseDouble(subtotalString);

		Double subtotalCalculado = quantidadeProduto * precoProduto;

		assertThat(subtotal, is(subtotalCalculado));

	}
	
	//Valores esperados
	
		String esperado_nomeProduto = "Hummingbird printed t-shirt"; 
		Double esperado_precoProduto = 19.12;
		String esperado_tamanhoProduto = "M";
		String esperado_corProduto = "Black";
		int esperado_input_quantidadeProduto = 2;
		Double esperado_subtotalProduto = esperado_precoProduto * esperado_input_quantidadeProduto;
		
		int esperado_numeroItensTotal = esperado_input_quantidadeProduto;
		Double esperado_subtotalTotal = esperado_subtotalProduto;
		Double esperado_shippingTotal = 7.00;
		Double esperado_totalTaxExclTotal = esperado_subtotalTotal + esperado_shippingTotal;
		Double esperado_totalTaxIncTotal = esperado_totalTaxExclTotal;
		Double esperado_taxesTotal = 0.00;	
		
		String esperado_nomeCliente = "Marcelo Bittencourt";
		
		CarrinhoPage carrinhoPage;
		
		@Test
		public void testIrParaCarrinho_InformacoesPersistidas() {
			// --Pr�-condi��es
			// Produto inclu�do na tela ModalProdutoPage
			IncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso();

			carrinhoPage = modalProdutoPage.clicarBotaoProceedToCheckout();
			//Asser��es Hamcrest
			assertThat(carrinhoPage.obter_nomeProduto(), is(esperado_nomeProduto));
			assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()), is(esperado_precoProduto));
			assertThat(carrinhoPage.obter_tamanhoProduto(),is(esperado_tamanhoProduto));
			assertThat(carrinhoPage.obter_corProduto(), is(esperado_corProduto));
			assertThat(Integer.parseInt(carrinhoPage.obter_input_quantidadeProduto()), is(esperado_input_quantidadeProduto));
			assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()),
					is(esperado_subtotalProduto));

			assertThat(Funcoes.removeTextoItemsDevolveInt(carrinhoPage.obter_numeroItensTotal()), is(esperado_numeroItensTotal));
			assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()), is(esperado_subtotalTotal));
			assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()), is(esperado_shippingTotal));
			assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxExclTotal()), is(esperado_totalTaxExclTotal));		
			assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxIncTotal()), is(esperado_totalTaxIncTotal));			
			assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()), is(esperado_taxesTotal));		
			
		

		}
		
		CheckoutPage checkoutPage;
		
		

		
		
		@Test
		public void testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk() {
			//Pr�-condi��es
			
			//Produto dispon�vel no carrinho de compras
			testIrParaCarrinho_InformacoesPersistidas();
			
			//Teste
			
			//Clicar no bot�o
			checkoutPage = carrinhoPage.clicarBotaoProceedToCheckout();
			
			//Preencher informa��es
			
			//Validar Informa��es na tela
			assertThat(Funcoes.removeCifraoDevolveDouble(checkoutPage.obter_totalTaxIncTotal()), is(esperado_totalTaxIncTotal));
			//assertThat(checkoutPage.obter_nomeCliente(), is(esperado_nomeCliente));
			assertTrue(checkoutPage.obter_nomeCliente().startsWith(esperado_nomeCliente));
			
			
			checkoutPage.clicarBotaoContinueAddress();
			
			String encontrado_shippingValor = checkoutPage.obter_shippingValor();
			encontrado_shippingValor = Funcoes.removeTexto(encontrado_shippingValor, " tax excl.");
			Double encontrado_shippingValor_Double = Funcoes.removeCifraoDevolveDouble(encontrado_shippingValor);
			
			assertThat(encontrado_shippingValor_Double, is(esperado_shippingTotal) );	
			
			checkoutPage.clicarBotaoContinueShipping();
			
			//Selecionar op��o "Pay by Check"
			checkoutPage.selecionarRadioPayByCheck();
			//Validar valor do cheque (amount)
			String encontrado_amountPayByCheck = checkoutPage.obter_amountPayByCheck();
			encontrado_amountPayByCheck = Funcoes.removeTexto(encontrado_amountPayByCheck, " (tax incl.)");
			Double encontrado_amountPayByCheck_Double = Funcoes.removeCifraoDevolveDouble(encontrado_amountPayByCheck);
			
			assertThat( encontrado_amountPayByCheck_Double, is(esperado_totalTaxIncTotal));
		    //Clicar na op��o "I agree"
			checkoutPage.selecionarCheckboxIAgree();
			
			assertTrue(checkoutPage.estaSelecionadoCheckboxIAgree());
			
		}
		
		
		@Test
		public void testFinalizarPedido_pedidoFinalizadoComSucesso() {
			//Pr�-condi��es
			//Checkout completamente conclu�do
			testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk();
			
			//Teste
			//Clicar no bot�o para confirmar o pedido
			PedidoPage pedidoPage = checkoutPage.clicarBotaoConfirmaPedido();
			
			//Validar valores da tela
			assertTrue(pedidoPage.obter_textoPedidoConfirmado().endsWith("YOUR ORDER IS CONFIRMED"));		
			//assertThat(pedidoPage.obter_textoPedidoConfirmado().toUpperCase(), is("YOUR ORDER IS CONFIRMED"));
			
			assertThat(pedidoPage.obter_email(), is("marcelo@teste.com"));
			
			assertThat(pedidoPage.obter_totalProdutos(), is(esperado_subtotalProduto));
			
			assertThat(pedidoPage.obter_totalTaxIncl(), is(esperado_totalTaxIncTotal));
			
			assertThat(pedidoPage.obter_metodoPagamento(), is("check"));
			
		}
		

}
