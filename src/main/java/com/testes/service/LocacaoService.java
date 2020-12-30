package com.testes.service;

import java.util.Date;

import com.testes.domain.Carro;
import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;
import com.testes.util.DateUtils;

public class LocacaoService {
	
	public Locacao alugarCarro(Pessoa locatario, Carro carro) {
		Locacao locacao = new Locacao();
		locacao.setId(1L);
		locacao.setLocatario(locatario);
		locacao.setCarro(carro);
		locacao.setDataLocacao(new Date());		
		locacao.setDataDevolucao(DateUtils.adicionaDias(new Date(), 1));
		locacao.setValor(carro.getPrecoLocacao() * DateUtils.calculaDiasEntreDatas(locacao.getDataLocacao(), locacao.getDataDevolucao()));
		
		return locacao;
	}
	
}
