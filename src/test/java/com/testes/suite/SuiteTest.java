package com.testes.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.testes.service.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	LocacaoServiceTest.class
})
public class SuiteTest {

}
