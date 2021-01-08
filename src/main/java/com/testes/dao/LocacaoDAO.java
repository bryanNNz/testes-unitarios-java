package com.testes.dao;

import java.util.List;

import com.testes.domain.Locacao;
import com.testes.domain.Pessoa;

public interface LocacaoDAO {
	public void salvar(Locacao locacao);
	
	public List<Pessoa> findLocacoesAtrasadas();
}
