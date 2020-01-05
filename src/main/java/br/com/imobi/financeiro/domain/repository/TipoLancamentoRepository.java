package br.com.imobi.financeiro.domain.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.imobi.financeiro.domain.model.TipoLancamento;

@Repository
public interface TipoLancamentoRepository extends MongoRepository<TipoLancamento, Long> {
	Optional<TipoLancamento> findBycode(String descricao);
}
