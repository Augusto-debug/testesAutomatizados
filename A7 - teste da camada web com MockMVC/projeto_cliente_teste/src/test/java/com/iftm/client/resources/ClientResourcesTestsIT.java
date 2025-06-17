package com.iftm.client.resources;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftm.client.dto.ClientDTO;

/**
 * Classe de testes de integração da camada Web com MockMVC
 * Testa os endpoints do ClientResource utilizando o banco H2 em memória
 * 
 * @author Augusto Cesar Rezende
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ClientResourcesTestsIT {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	private Long existingId;
	private Long nonExistingId;
	private ClientDTO clientDTO;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		existingId = 1L;
		nonExistingId = 1000L;

		clientDTO = new ClientDTO(null, "Maria Silva", "12345678900", 2500.0, 
			Instant.parse("1990-05-15T10:30:00Z"), 1);
	}

	/**
	 * Testa o endpoint GET /clients para buscar todos os clientes com paginação
	 * Verifica se retorna status 200 OK e a estrutura JSON de paginação correta
	 */
	@Test
	public void findAllShouldReturnPagedClients() throws Exception {
		// Assign - Não há dados específicos para configurar
		
		// Act - Executa requisição GET para /clients
		ResultActions result = mockMvc.perform(get("/clients")
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e estrutura de paginação
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").exists());
		result.andExpect(jsonPath("$.totalPages").exists());
		result.andExpect(jsonPath("$.size").exists());
		result.andExpect(jsonPath("$.number").exists());
	}

	/**
	 * Testa o endpoint GET /clients/{id} com ID existente
	 * Verifica se retorna status 200 OK e os dados do cliente específico
	 */
	@Test
	public void findByIdShouldReturnClientWhenIdExists() throws Exception {
		// Assign - ID existente já configurado no setup
		
		// Act - Executa requisição GET para /clients/{id}
		ResultActions result = mockMvc.perform(get("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e campos do cliente
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.cpf").exists());
		result.andExpect(jsonPath("$.income").exists());
		result.andExpect(jsonPath("$.birthDate").exists());
		result.andExpect(jsonPath("$.children").exists());
	}

	/**
	 * Testa o endpoint GET /clients/{id} com ID inexistente
	 * Verifica se retorna status 404 Not Found e a mensagem de erro adequada
	 */
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		// Assign - ID inexistente já configurado no setup
		
		// Act - Executa requisição GET para /clients/{id} com ID inexistente
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 404 e mensagem de erro
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
		result.andExpect(jsonPath("$.message").value("Entity not found"));
		result.andExpect(jsonPath("$.status").value(404));
		result.andExpect(jsonPath("$.path").value("/clients/" + nonExistingId));
	}

	/**
	 * Testa o endpoint GET /clients/income com renda que existe na base
	 * Verifica se retorna status 200 OK e clientes com a renda especificada
	 */
	@Test
	public void findByIncomeShouldReturnClientsWhenIncomeExists() throws Exception {
		// Assign - Define renda que existe na base de dados
		Double income = 2500.0;

		// Act - Executa requisição GET para /clients/income
		ResultActions result = mockMvc.perform(get("/clients/income")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e existência de conteúdo
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").exists());
	}

	/**
	 * Testa o endpoint GET /clients/income com renda que não existe na base
	 * Verifica se retorna status 200 OK mas uma página vazia
	 */
	@Test
	public void findByIncomeShouldReturnEmptyPageWhenIncomeDoesNotExist() throws Exception {
		// Assign - Define renda que não existe na base de dados
		Double income = 99999.0;

		// Act - Executa requisição GET para /clients/income
		ResultActions result = mockMvc.perform(get("/clients/income")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status OK mas página vazia
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isEmpty());
		result.andExpect(jsonPath("$.totalElements").value(0));
	}

	/**
	 * Testa o endpoint GET /clients/incomeGreaterThan com valor que retorna clientes
	 * Verifica se retorna status 200 OK e clientes com renda maior que o valor
	 */
	@Test
	public void findByIncomeGreaterThanShouldReturnClientsWhenIncomeExists() throws Exception {
		// Assign - Define renda base para busca (maior que)
		Double income = 3000.0;

		// Act - Executa requisição GET para /clients/incomeGreaterThan
		ResultActions result = mockMvc.perform(get("/clients/incomeGreaterThan")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e existência de conteúdo
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").exists());
	}

	/**
	 * Testa o endpoint GET /clients/incomeGreaterThan com valor muito alto
	 * Verifica se retorna status 200 OK mas uma página vazia (nenhum cliente com renda tão alta)
	 */
	@Test
	public void findByIncomeGreaterThanShouldReturnEmptyPageWhenNoClientsFound() throws Exception {
		// Assign - Define renda muito alta que não existe na base
		Double income = 50000.0;

		// Act - Executa requisição GET para /clients/incomeGreaterThan
		ResultActions result = mockMvc.perform(get("/clients/incomeGreaterThan")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status OK mas página vazia
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isEmpty());
		result.andExpect(jsonPath("$.totalElements").value(0));
	}

	/**
	 * Testa o endpoint GET /clients/cpfLike com CPF que existe na base
	 * Verifica se retorna status 200 OK e clientes com CPF similar
	 */
	@Test
	public void findByCpfLikeShouldReturnClientsWhenCpfExists() throws Exception {
		// Assign - Define parte do CPF que existe na base de dados
		String cpf = "106";

		// Act - Executa requisição GET para /clients/cpfLike
		ResultActions result = mockMvc.perform(get("/clients/cpfLike")
				.param("cpf", cpf)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e existência de conteúdo
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").exists());
	}

	/**
	 * Testa o endpoint GET /clients/cpfLike com CPF que não existe na base
	 * Verifica se retorna status 200 OK mas uma página vazia
	 */
	@Test
	public void findByCpfLikeShouldReturnEmptyPageWhenCpfDoesNotExist() throws Exception {
		// Assign - Define CPF que não existe na base de dados
		String cpf = "99999";

		// Act - Executa requisição GET para /clients/cpfLike
		ResultActions result = mockMvc.perform(get("/clients/cpfLike")
				.param("cpf", cpf)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status OK mas página vazia
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isEmpty());
		result.andExpect(jsonPath("$.totalElements").value(0));
	}

	/**
	 * Testa o endpoint POST /clients para criar um novo cliente
	 * Verifica se retorna status 201 Created e os dados do cliente criado
	 */
	@Test
	public void insertShouldReturnCreatedAndClientDTO() throws Exception {
		// Assign - Converte o DTO para JSON
		String json = objectMapper.writeValueAsString(clientDTO);

		// Act - Executa requisição POST para /clients
		ResultActions result = mockMvc.perform(post("/clients")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 201 e dados do cliente criado
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Maria Silva"));
		result.andExpect(jsonPath("$.cpf").value("12345678900"));
		result.andExpect(jsonPath("$.income").value(2500.0));
		result.andExpect(jsonPath("$.children").value(1));
	}

	/**
	 * Testa o endpoint DELETE /clients/{id} com ID existente
	 * Verifica se retorna status 204 No Content indicando remoção bem-sucedida
	 */
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		// Assign - ID existente já configurado no setup
		
		// Act - Executa requisição DELETE para /clients/{id}
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 204 No Content
		result.andExpect(status().isNoContent());
	}

	/**
	 * Testa o endpoint DELETE /clients/{id} com ID inexistente
	 * Verifica se retorna status 404 Not Found
	 */
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		// Assign - ID inexistente já configurado no setup
		
		// Act - Executa requisição DELETE para /clients/{id} com ID inexistente
		ResultActions result = mockMvc.perform(delete("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 404 Not Found
		result.andExpect(status().isNotFound());
	}

	/**
	 * Testa o endpoint PUT /clients/{id} com ID existente
	 * Verifica se retorna status 200 OK e os dados atualizados do cliente
	 */
	@Test
	public void updateShouldReturnOkAndUpdatedClientWhenIdExists() throws Exception {
		// Assign - Cria DTO com dados atualizados e converte para JSON
		ClientDTO updatedClientDTO = new ClientDTO(existingId, "Maria Silva Updated", "12345678900", 3500.0,
			Instant.parse("1990-05-15T10:30:00Z"), 2);
		String json = objectMapper.writeValueAsString(updatedClientDTO);

		// Act - Executa requisição PUT para /clients/{id}
		ResultActions result = mockMvc.perform(put("/clients/{id}", existingId)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 200 e dados atualizados
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value("Maria Silva Updated"));
		result.andExpect(jsonPath("$.income").value(3500.0));
		result.andExpect(jsonPath("$.children").value(2));
	}

	/**
	 * Testa o endpoint PUT /clients/{id} com ID inexistente
	 * Verifica se retorna status 404 Not Found e mensagem de erro adequada
	 */
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		// Assign - Cria DTO com ID inexistente e converte para JSON
		ClientDTO updatedClientDTO = new ClientDTO(nonExistingId, "Maria Silva Updated", "12345678900", 3500.0,
			Instant.parse("1990-05-15T10:30:00Z"), 2);
		String json = objectMapper.writeValueAsString(updatedClientDTO);

		// Act - Executa requisição PUT para /clients/{id} com ID inexistente
		ResultActions result = mockMvc.perform(put("/clients/{id}", nonExistingId)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 404 e mensagem de erro
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
	}
} 