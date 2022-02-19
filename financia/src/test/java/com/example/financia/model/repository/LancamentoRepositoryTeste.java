package com.example.financia.model.repository;



import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.financia.model.entity.Lancamento;
import com.example.financia.model.entity.Usuario;
import com.example.financia.model.enums.StatusLancamento;
import com.example.financia.model.enums.TipoLancamento;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class LancamentoRepositoryTeste {

	@Autowired
	LancamentoRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveSalvarUmLancamento() {

		Lancamento lancamento = criarLancamento();

		lancamento = repository.save(lancamento);

		Assertions.assertThat(lancamento.getId()).isNotNull();
		
	}

	public static Lancamento criarLancamento() {
	
		 //Usuario usuario = new Usuario();
		
		return Lancamento.builder()
				.ano(2019)
				.mes(1)
				.descricao("lancamento qualquer")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.usuario(new Usuario(1l, null, null, null))
				.status(StatusLancamento.PENDENTE).dataCadastro(LocalDate.now()).build();
	}
}
