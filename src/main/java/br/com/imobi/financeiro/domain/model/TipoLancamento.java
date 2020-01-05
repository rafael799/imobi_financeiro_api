package br.com.imobi.financeiro.domain.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Document(collection = "tipoLancamento")
public class TipoLancamento implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotNull
	private String code;
	
	@NotBlank
	private String descricao;

}
