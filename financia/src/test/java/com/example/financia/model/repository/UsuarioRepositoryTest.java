package com.example.financia.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.financia.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager entityManager;

	public static Usuario criarUsuario() {
		return Usuario
			   .builder()
			   .nome("usuario")
			   .email("usuario@email.com")
			    .senha("senha")
			    .build();
	}

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {

		// cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		/// repository.save(usuario);
		entityManager.persist(usuario);

		// acão /execucao
		boolean result = repository.existsByEmail("usuario@email.com");

		// verificação
		Assertions.assertThat(result).isTrue();

	}

	public void deveRetornarFalsoQuandonaoHouverUsuarioCadastradocomEmail() {
		// cenario
		// repository.deleteAll();

		// acao
		boolean result = repository.existsByEmail("usuario@email.com");

		// verificacao
		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		// cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").senha("senha").build();

		// ação
		Usuario usuarioSalvo = repository.save(usuario);

		// verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();

	}

	public void deveBuscarUmUsuarioPorEmail() {

		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario1@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}

	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExistNaBase() {

		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario1@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}
	
}
