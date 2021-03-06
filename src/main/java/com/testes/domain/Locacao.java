package com.testes.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Locacao {
	
	@NonNull
	private Long id;
	
	@NonNull
	private Pessoa locatario;
	
	@NonNull
	private List<Carro> carros;
	
	@NonNull
	private Date dataLocacao;
	
	@NonNull
	private Date dataDevolucao;
	
	@NonNull
	private Double valor;
}
