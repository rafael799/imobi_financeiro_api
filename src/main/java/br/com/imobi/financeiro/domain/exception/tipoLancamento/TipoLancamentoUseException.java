package br.com.imobi.financeiro.domain.exception.tipoLancamento;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.imobi.financeiro.domain.exception.EntityUseException;

@ResponseStatus(HttpStatus.CONFLICT)
public class TipoLancamentoUseException extends EntityUseException {
	
	private static final long serialVersionUID = 1L;

	public TipoLancamentoUseException(String mensagem) {
		super(mensagem);
	}
	
	public TipoLancamentoUseException(Long id) {
		this(String.format("Tipo Lancamento de código %d não pode ser removida, pois está em uso", id));
	}

}