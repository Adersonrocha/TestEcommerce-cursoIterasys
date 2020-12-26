# language: pt
Funcionalidade: Comprar produto Com um usuario logado
  Eu quero visualizar produtos disponiveis
  Para poder escolher qual eu vou comprar
  Para concluir o pedido

	@validacaoinicial
  Cenario: Deve mostrar uma lista de oito produtos na pagina inicial
    Dado que estou na pagina inivial
    Quando nao estou logado
    Entao visualizo 8 produtos disponiveis
    E carrinho zerado


	@fluxopadrao
  Cenario: Deve mostrar produto escolhido confirmado
    Dado que estou na pagina inicial
    Quando estou logado
    E seleciono um produto na posicao 0
    E nome do produto na tela principal e na tela do produto eh "Hummingbird Printed T-Shirt"
    E preco do produto na tela principal e na tela produto eh "$19.12"
    E adiciono o produto no carrinho com tamanho "M" cor "Black" e quantidade 2
    Entao o produto aparece na confirmacao com nome "Hummingbird Printed T-shirt" preco "$19.12" tamanho "M" cor "Black" e quantidade 2
