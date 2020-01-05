package br.com.imobi.financeiro.domain.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.imobi.financeiro.domain.model.LancamentoEntrada;

@Repository
public interface LancamentoEntradaRepository extends MongoRepository<LancamentoEntrada, Long> {
	Optional<LancamentoEntrada> findBycode(String descricao);
}
