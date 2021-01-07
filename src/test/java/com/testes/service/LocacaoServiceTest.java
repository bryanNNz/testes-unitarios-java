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
import java.util.Calendar;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.testes.dao.LocacaoDAO;
import com.testes.domain.Carro;
import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;
import com.testes.util.DateUtils;

public class LocacaoServiceTest {
	
	private LocacaoService service;
	
	private SerasaService serasaService;
	
	private LocacaoDAO locacaoDAO;
	
	@Rule //EM CASOS QUE O METODO NAO CONTEM APENAS UMA ASSERTIVA � ALGO INTERESSANTE
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException ee = ExpectedException.none();
	
	@Before
	public void init() {
		serasaService = Mockito.mock(SerasaService.class);
		locacaoDAO = Mockito.mock(LocacaoDAO.class);
		service = new LocacaoService();
		service.setLocacaoDAO(locacaoDAO);
		service.setSerasaService(serasaService);
	}
	
	@Test
	public void testeLocacao() throws Exception {
		//CENARIO
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, true));
		
		//ACAO
		Locacao locacao = service.alugarCarro(locatario, carros);
		
		//ASSUMPTIONS
		Assume.assumeFalse(DateUtils.isMesmoDiaDaSemana(locacao.getDataDevolucao(), Calendar.SUNDAY));
		
		//VERIFICACAO
		assertNotNull(locacao);
		assertEquals(160.0, locacao.getValor(), 0.01);
		assertFalse(DateUtils.isMesmoDiaDoMes(locacao.getDataLocacao(), locacao.getDataDevolucao()));
		
		//UTILIZADO JUNTO AO CORE MATCHER, FICA BEM LEGIVEL
		assertThat(locacao.getValor(), is(equalTo(160.0)));
	}
	
	@Test
	public void testeLocacaoWithErrorCollector() throws Exception {
		//CENARIO
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, true));
		
		//ACAO
		Locacao locacao = service.alugarCarro(locatario, carros);
		
		//ASSUMPTIONS
		Assume.assumeFalse(DateUtils.isMesmoDiaDaSemana(locacao.getDataDevolucao(), Calendar.SUNDAY));
		
		//VERIFICACAO
		error.checkThat(locacao, is(notNullValue()));
		error.checkThat(locacao.getValor(), is(equalTo(160.0)));
		error.checkThat(DateUtils.isMesmoDiaDoMes(locacao.getDataLocacao(), locacao.getDataDevolucao()), is(false));
	}
	
	@Test(expected = Exception.class)
	public void testeCarroDisponivel() throws Exception {
		//CENARIO
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, false));
		
		//ACAO
		service.alugarCarro(locatario, carros);
	}
	
	@Test
	public void testeCarroDisponivelWithExpectedException() throws Exception {
		//CENARIO
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, false));
		
		ee.expect(Exception.class);
		
		//ACAO
		service.alugarCarro(locatario, carros);
	}
	
	@Test
	public void testeCarroDisponivelRobusto() {
		//CENARIO
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
	
	@Test
	public void deveHaverDescontoDe25PctDoTotalQuandoADevolucaoForDomingo() throws Exception {
		
		//CENARIO
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, true));
		
		//ACAO
		Locacao locacao = service.alugarCarro(locatario, carros);
		
		//ASSUMPTIONS
		Assume.assumeTrue(DateUtils.isMesmoDiaDaSemana(locacao.getDataDevolucao(), Calendar.SUNDAY));

		//VERIFICACAO
		assertThat(locacao.getValor(), is(equalTo(120.0)));
		
	}
	
	@Test
	public void deveImpedirLocacaoQuandoLocatarioEstiverNegativoNoSerasa() throws Exception {
		//CENARIO
		Pessoa locatario = new Pessoa(1L, "MARIA");
		List<Carro> carros = Arrays.asList(new Carro(1L, "ABC1234", 80.0, true));
		
		ee.expect(Exception.class);
		ee.expectMessage("PESSOA NEGATIVADA");
		
		Mockito.when(serasaService.pessoaNegativada(locatario)).thenReturn(true);
		
		//ACAO
		service.alugarCarro(locatario, carros);
		
	}
}
