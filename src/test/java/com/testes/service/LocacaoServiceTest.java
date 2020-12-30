package com.testes.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.testes.domain.Carro;
import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;
import com.testes.util.DateUtils;

public class LocacaoServiceTest {
	
	@Test
	public void teste() {
		//CENARIO
		LocacaoService service = new LocacaoService();
		Pessoa locatario = new Pessoa(1L, "MARIA");
		Carro carro = new Carro(1L, "ABC1234", 80.0);
		
		//ACAO
		Locacao locacao = service.alugarCarro(locatario, carro);
		
		//VERIFICACAO
		assertNotNull(locacao);
		assertEquals(160.0, locacao.getValor(), 0.01);
		assertFalse(DateUtils.isMesmoDia(locacao.getDataLocacao(), locacao.getDataDevolucao()));
		
		assertThat(locacao.getValor(), is(equalTo(160.0)));
	}
}
