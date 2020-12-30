package com.testes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Carro {
	
	@NonNull
	private Long id;
	
	@NonNull
	private String placa;
	
	@NonNull
	private Double precoLocacao;
}
