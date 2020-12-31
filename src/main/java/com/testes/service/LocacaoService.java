package com.testes.service;

import java.util.Date;
import java.util.List;

import com.testes.domain.Carro;
import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;
import com.testes.util.DateUtils;

public class LocacaoService {
	
	public Locacao alugarCarro(Pessoa locatario, List<Carro> carros) throws Exception {
		
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
		
		locacao.setValor(total);
		
		return locacao;
	}
	
}
