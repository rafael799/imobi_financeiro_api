package br.com.imobi.financeiro.domain.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import br.com.imobi.financeiro.domain.exception.lancamentoEntrada.LancamentoEntradaNotFoundException;
import br.com.imobi.financeiro.domain.model.Imovel;
import br.com.imobi.financeiro.domain.model.LancamentoEntrada;
import br.com.imobi.financeiro.domain.repository.LancamentoEntradaRepository;

@Service
public class LancamentoEntradaService {

	@Autowired
	private LancamentoEntradaRepository repository;
	
	@Autowired
	private RestTemplate client;

	@Transactional
	public LancamentoEntrada save(LancamentoEntrada tipoEntrada) {
		ResponseEntity<Imovel> exchange = client.exchange("http://locacao/imoveis/"+tipoEntrada.getImovel().getCode(), HttpMethod.GET,null,Imovel.class);
		Imovel i = exchange.getBody();
		System.out.println(i);
		return repository.save(tipoEntrada);
	}

	@Transactional
	public LancamentoEntrada update(String code, LancamentoEntrada tipoEntrada) {
		LancamentoEntrada tipoEntradaAtual = findOrNull(code);
		BeanUtils.copyProperties(tipoEntrada, tipoEntradaAtual, "id");
		return repository.save(tipoEntradaAtual);
	}

	public List<LancamentoEntrada> getAll() {
		return repository.findAll();
	}

	public LancamentoEntrada getByCode(String code) {
		return findOrNull(code);
	}
	
	@Transactional
	public void remove(String code) {
		//try {

			LancamentoEntrada tipoEntrada = findOrNull(code);
			repository.delete(tipoEntrada);

		//} catch (DataIntegrityViolationException e) {
			//throw new tipoEntradaUseException(code);
		//}

	}
	
	public LancamentoEntrada findOrNull(String code) {
		return repository.findBycode(code).orElseThrow(() -> new LancamentoEntradaNotFoundException(code));
	}

}
