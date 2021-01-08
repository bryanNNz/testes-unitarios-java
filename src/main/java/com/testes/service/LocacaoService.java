package com.testes.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.testes.dao.LocacaoDAO;
import com.testes.domain.Carro;
import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;
import com.testes.util.DateUtils;

public class LocacaoService {
	
	private LocacaoDAO locacaoDAO;
	
	private SerasaService serasaService;
	
	private MailService mailService;
	
	public Locacao alugarCarro(Pessoa locatario, List<Carro> carros) throws Exception {
		
		if(serasaService.pessoaNegativada(locatario)) {
			throw new Exception("PESSOA NEGATIVADA");
		}
		
		for(Carro carro : carros) {
			if(!carro.getDisponivel())
				throw new Exception("CARRO INDISPONIVEL");
		}
		
		Locacao locacao = new Locacao();
		locacao.setId(1L);
		locacao.setLocatario(locatario);
		locacao.setCarros(carros);
		locacao.setDataLocacao(new Date());		
		locacao.setDataDevolucao(DateUtils.adicionaDias(new Date(), +2));
		
		Double total = 0.0;		
		for(Carro carro : carros) {
			total += carro.getPrecoLocacao() * DateUtils.calculaDiasEntreDatas(locacao.getDataLocacao(), locacao.getDataDevolucao());
		}
		
		if(DateUtils.isMesmoDiaDaSemana(locacao.getDataDevolucao(), Calendar.SUNDAY)) {
			total -= total * 0.25;
		}
		
		locacao.setValor(total);
		
		locacaoDAO.salvar(locacao);
		
		return locacao;
	}
	
	public void notificarAtraso() {
		
		List<Pessoa> locatarios = locacaoDAO.findLocacoesAtrasadas();
		
		for(Pessoa l : locatarios) {
			mailService.notificarLocatario(l);
		}
	}
	
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.locacaoDAO = dao;
	}
	
	public void setSerasaService(SerasaService serasaService) {
		this.serasaService = serasaService;
	}
	
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
}
