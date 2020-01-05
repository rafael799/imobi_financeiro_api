package br.com.imobi.financeiro.application;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.imobi.financeiro.domain.model.TipoLancamento;
import br.com.imobi.financeiro.domain.service.TipoLancamentoService;

@RestController
@RequestMapping(value = "/tipoLancamentos")
public class TipoLancamentoApplication {
	
	@Autowired
	private TipoLancamentoService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TipoLancamento save(@RequestBody @Valid TipoLancamento imovel) {
		return service.save(imovel);
	}

	@GetMapping
	public List<TipoLancamento> getAll() {
		return service.getAll();
	}

	@DeleteMapping("/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable String code) {
		service.remove(code);
	}

	@PutMapping("/{code}")
	public TipoLancamento update(@PathVariable String code, @RequestBody @Valid TipoLancamento imovel) {
		return service.update(code, imovel);
	}

	@GetMapping("/{code}")
	public TipoLancamento getByCode(@PathVariable String code) {
		return service.findOrNull(code);
	}

}
