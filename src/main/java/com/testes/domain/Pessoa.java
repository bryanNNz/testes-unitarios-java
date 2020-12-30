package com.testes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Pessoa {

	@NonNull
	private Long id;
	
	@NonNull
	private String nome;
}
