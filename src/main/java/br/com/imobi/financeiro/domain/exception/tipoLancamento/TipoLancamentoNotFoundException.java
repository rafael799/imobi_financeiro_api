package br.com.imobi.financeiro.domain.exception.tipoLancamento;

import br.com.imobi.financeiro.domain.exception.EntityNotFoundException;

public class TipoLancamentoNotFoundException extends EntityNotFoundException {
	
	private static final long serialVersionUID = 1L;

	public TipoLancamentoNotFoundException(String code) {
		super(String.format("Não existe um cadastro de Tipo Lancamento com código %s", code));
	}
	
}
