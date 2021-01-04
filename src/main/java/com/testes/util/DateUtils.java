package com.testes.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
	
	public static Date adicionaDias(Date data, Integer qtdDias) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, qtdDias);
		return c.getTime();
	}
	
	public static int calculaDiasEntreDatas(Date inicio, Date fim) {
		long diferenca = fim.getTime() - inicio.getTime();
		return Long.valueOf(TimeUnit.MILLISECONDS.toDays(diferenca)).intValue();
	}
	
	public static boolean isMesmoDiaDoMes(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
	}
	
	public static boolean isMesmoDiaDaSemana(Date data, int diaDaSemana) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(data);
		c2.setTime(new Date());
		return c1.get(Calendar.DAY_OF_WEEK) == c2.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getDiaDaSemana(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.get(Calendar.DAY_OF_WEEK);
	}
}
