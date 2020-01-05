package br.com.imobi.financeiro.application.exceptionhandler;

import lombok.Getter;

@Getter
public enum ApiHandlerType {
	
	INVALID_DATA("/dados-invalidos", "Dados inválidos"),
	SYSTEM_ERROR("/erro-de-sistema", "Erro de sistema"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso");

	private String title;
	private String uri;

	ApiHandlerType(String path, String title) {
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
	}

}
