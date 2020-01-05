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

import br.com.imobi.financeiro.domain.model.LancamentoEntrada;
import br.com.imobi.financeiro.domain.service.LancamentoEntradaService;

@RestController
@RequestMapping(value = "/lancamentoEntrada")
public class LancamentoEntradaApplication {
	
	@Autowired
	private LancamentoEntradaService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LancamentoEntrada save(@RequestBody @Valid LancamentoEntrada imovel) {
		return service.save(imovel);
	}

	@GetMapping
	public List<LancamentoEntrada> getAll() {
		return service.getAll();
	}

	@DeleteMapping("/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable String code) {
		service.remove(code);
	}

	@PutMapping("/{code}")
	public LancamentoEntrada update(@PathVariable String code, @RequestBody @Valid LancamentoEntrada imovel) {
		return service.update(code, imovel);
	}

	@GetMapping("/{code}")
	public LancamentoEntrada getByCode(@PathVariable String code) {
		return service.findOrNull(code);
	}

}
