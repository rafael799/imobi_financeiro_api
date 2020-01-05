package br.com.imobi.financeiro.domain.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.imobi.financeiro.domain.exception.tipoLancamento.TipoLancamentoNotFoundException;
import br.com.imobi.financeiro.domain.model.TipoLancamento;
import br.com.imobi.financeiro.domain.repository.TipoLancamentoRepository;

@Service
public class TipoLancamentoService {

	@Autowired
	private TipoLancamentoRepository repository;

	@Transactional
	public TipoLancamento save(TipoLancamento tipoLancamento) {
		return repository.save(tipoLancamento);
	}

	@Transactional
	public TipoLancamento update(String code, TipoLancamento imovel) {
		TipoLancamento imovelAtual = findOrNull(code);
		BeanUtils.copyProperties(imovel, imovelAtual, "id");
		return repository.save(imovelAtual);
	}

	public List<TipoLancamento> getAll() {
		return repository.findAll();
	}

	public TipoLancamento getByCode(String code) {
		return findOrNull(code);
	}
	
	@Transactional
	public void remove(String code) {
		//try {

			TipoLancamento imovel = findOrNull(code);
			repository.delete(imovel);

		//} catch (DataIntegrityViolationException e) {
			//throw new ImovelUseException(code);
		//}

	}
	
	public TipoLancamento findOrNull(String code) {
		return repository.findBycode(code).orElseThrow(() -> new TipoLancamentoNotFoundException(code));
	}

}
