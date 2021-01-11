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
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.testes.dao.LocacaoDAO;
import com.testes.domain.Carro;
import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;
import com.testes.util.DateUtils;

public class LocacaoServiceTest {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SerasaService serasaService;
	
	@Mock
	private LocacaoDAO locacaoDAO;
	
	@Mock
	private MailService mailService;
	
	@Rule //EM CASOS QUE O METODO NAO CONTEM APENAS UMA ASSERTIVA É ALGO INTERESSANTE
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException ee = ExpectedException.none();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
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
	
	@Test
	public void deveEnviarEmailQuandoExistirLocacoesAtrasadas() {
		//CENARIO
		Pessoa locatario = new Pessoa(1L, "MARIA");
		Mockito.doReturn(Arrays.asList(locatario)).when(locacaoDAO).findLocacoesAtrasadas();
		
		//ACAO
		service.notificarAtraso();
		
		//VERIFICACAO
		Mockito.verify(mailService).notificarLocatario(locatario);
		
	}
	
	@Test
	public void deveAplicarDescontoNoTotal() throws Exception {
		//CENARIO
		Locacao locacao = new Locacao();
		locacao.setId(1L);
		locacao.setLocatario(new Pessoa(1L, "MARIA"));
		locacao.setCarros(Arrays.asList(new Carro(1L, "ABC1234", 80.0, true)));
		locacao.setDataLocacao(new Date());		
		locacao.setDataDevolucao(DateUtils.adicionaDias(new Date(), 2));
		locacao.setValor(100.0);
		
		//ACAO
		ArgumentCaptor<Locacao> argCaptor = ArgumentCaptor.forClass(Locacao.class);
		service.aplicaDescontoLocacao(locacao);
		
		//VERIFICACAO
		Mockito.verify(locacaoDAO).salvar(argCaptor.capture());
		Locacao locacaoCapturada = argCaptor.getValue();
		assertThat(locacaoCapturada.getValor(), is(equalTo(80.0)));
	}
}
