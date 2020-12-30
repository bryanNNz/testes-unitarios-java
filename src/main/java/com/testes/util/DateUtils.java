package com.testes.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
	
	public static Date adicionaDias(Date data, Integer qtdDias) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
	
	public static int calculaDiasEntreDatas(Date inicio, Date fim) {
		long diferenca = fim.getTime() - inicio.getTime();
		return Long.valueOf(TimeUnit.DAYS.toDays(diferenca)).intValue();
	}
}
