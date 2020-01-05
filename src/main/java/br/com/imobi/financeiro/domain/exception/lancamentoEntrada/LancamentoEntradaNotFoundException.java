package br.com.imobi.financeiro.domain.exception.lancamentoEntrada;

import br.com.imobi.financeiro.domain.exception.EntityNotFoundException;

public class LancamentoEntradaNotFoundException extends EntityNotFoundException {
	
	private static final long serialVersionUID = 1L;

	public LancamentoEntradaNotFoundException(String code) {
		super(String.format("Não existe um cadastro de Tipo Lancamento com código %s", code));
	}
	
}
