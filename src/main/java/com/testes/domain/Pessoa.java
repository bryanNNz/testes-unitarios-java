package com.testes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Pessoa {

	@NonNull
	private Long id;
	
	@NonNull
	private String nome;
}
