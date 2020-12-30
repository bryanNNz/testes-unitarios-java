package com.testes.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Locacao {
	
	@NonNull
	private Long id;
	
	@NonNull
	private Pessoa locatario;
	
	@NonNull
	private Carro carro;
	
	@NonNull
	private Date dataLocacao;
	
	@NonNull
	private Date dataDevolucao;
	
	@NonNull
	private Double valor;
}
