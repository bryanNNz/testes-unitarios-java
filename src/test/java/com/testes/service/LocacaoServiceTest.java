package com.testes.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import com.testes.domain.Carro;
import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;
import com.testes.util.DateUtils;

public class LocacaoServiceTest {	
	@Rule //EM CASOS QUE O METODO NAO CONTEM APENAS UMA ASSERTIVA É ALGO INTERESSANTE
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException ee = ExpectedException.none();
	
	@Test
	public void testeLocacao() throws Exception {
		//CENARIO
		LocacaoService service = new LocacaoService();
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, true));
		
		//ACAO
		Locacao locacao = service.alugarCarro(locatario, carros);
		
		//VERIFICACAO
		assertNotNull(locacao);
		assertEquals(160.0, locacao.getValor(), 0.01);
		assertFalse(DateUtils.isMesmoDia(locacao.getDataLocacao(), locacao.getDataDevolucao()));
		
		//UTILIZADO JUNTO AO CORE MATCHER, FICA BEM LEGIVEL
		assertThat(locacao.getValor(), is(equalTo(160.0)));
	}
	
	@Test
	public void testeLocacaoWithErrorCollector() throws Exception {
		//CENARIO
		LocacaoService service = new LocacaoService();
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, true));
		
		//ACAO
		Locacao locacao = service.alugarCarro(locatario, carros);
		
		//VERIFICACAO
		error.checkThat(locacao, is(notNullValue()));
		error.checkThat(locacao.getValor(), is(equalTo(160.0)));
		error.checkThat(DateUtils.isMesmoDia(locacao.getDataLocacao(), locacao.getDataDevolucao()), is(false));
	}
	
	@Test(expected = Exception.class)
	public void testeCarroDisponivel() throws Exception {
		//CENARIO
		LocacaoService service = new LocacaoService();
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, false));
		
		//ACAO
		service.alugarCarro(locatario, carros);
	}
	
	@Test
	public void testeCarroDisponivelWithExpectedException() throws Exception {
		//CENARIO
		LocacaoService service = new LocacaoService();
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, false));
		
		ee.expect(Exception.class);
		
		//ACAO
		service.alugarCarro(locatario, carros);
	}
	
	@Test
	public void testeCarroDisponivelRobusto() {
		//CENARIO
		LocacaoService service = new LocacaoService();
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, false));
		
		//ACAO
		try {
			service.alugarCarro(locatario, carros);
			fail();
		} catch(Exception e) {
			error.checkThat(true, is(e instanceof Exception));
		}
		
	}
}
