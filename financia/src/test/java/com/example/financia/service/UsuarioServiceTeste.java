package com.example.financia.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.financia.exception.ErroAutenticacao;
import com.example.financia.exception.RegraNegocioExcepion;
import com.example.financia.model.entity.Usuario;
import com.example.financia.model.repository.UsuarioRepository;
import com.example.financia.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTeste {

	/*
	 * @Autowired UsuarioService service;
	 * 
	 * @Autowired UsuarioRepository repository;
	 */

	//UsuarioService service;
	
	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	
	/*
	@BeforeEach
	public void setup() {
		// repository = Mockito.mock(UsuarioRepository.class);
		service = new UsuarioServiceImpl(repository);
	}
   */
	@Test
	public void deveSalvarUmUsuario() {
		//cenanrio
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		
		Usuario usuario = Usuario.builder()
				                 .id(1l)
				                 .nome("nome")
				                 .email("email@email.com")
				                 .senha("senha")
				                 .build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//açao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificaçao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	

	@Test
	public void naoDeveSalvarUmUsuarioComEmailCadastrado() {
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioExcepion.class).when(service).validarEmail(email);
		
		
		//ação
		//service.salvarUsuario(usuario);
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioExcepion.class,
				() -> service.salvarUsuario(usuario));

		
		
		//verificação
		Mockito.verify(repository,Mockito.never()).save(usuario);
	}
		
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenario

		String email = "email@email.com";
		String senha = "senha";

		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

		// ação
		Usuario result = service.autenticar(email, senha);

		// verificação
		Assertions.assertThat(result).isNotNull();

	}
	
	@Test
	public void deveLancarErroQuandoEncontrarUsuarioCadastradoComoEmailInformado() {
		// cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// acao
		// org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioExcepion.class, ()
		// -> service.autenticar("email@email.com","senha"));

		// ação
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));

		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Usuário não encontrado para o email informado.");

	}

	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		// cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		// ação
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));

		// verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");

	}

	@Test
	public void deveValidarEmail() {

		// cenario
		// UsuarioRepository usuarioRepositoryMock =
		// Mockito.mock(UsuarioRepository.class);

		// repository.deleteAll();

		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		// ação
		service.validarEmail("email@email.com");

	}

	@Test
	public void deveLancarErroAoValidarEmailJacadastrado() {

		// cenario
		// Usuario usuario =
		// Usuario.builder().nome("usuario").email("email@email.com").build();
		// repository.save(usuario);

		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

		// acao
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioExcepion.class,
				() -> service.validarEmail("email@email.com"));

	}

}
