package com.iftm.client.resources;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftm.client.dto.ClientDTO;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

/**
 * Classe de testes de integração da camada Web com MockMVC utilizando MockBean
 * Testa os endpoints do ClientResource com simulação da camada de serviço
 * 
 * @author Augusto Cesar Rezende
 */
@WebMvcTest(ClientResource.class)
public class ClientResourcesTestsWithMockIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ClientService service;

	private Long existingId;
	private Long nonExistingId;
	private ClientDTO clientDTO;
	private ClientDTO existingClientDTO;
	private PageImpl<ClientDTO> page;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;

		clientDTO = new ClientDTO(null, "Maria Silva", "12345678900", 2500.0, 
			Instant.parse("1990-05-15T10:30:00Z"), 1);

		existingClientDTO = new ClientDTO(existingId, "Conceição Evaristo", "10619244881", 1500.0,
			Instant.parse("2020-07-13T20:50:00Z"), 2);

		page = new PageImpl<>(List.of(existingClientDTO));

		// Mock para findAllPaged
		when(service.findAllPaged(any(PageRequest.class))).thenReturn(page);

		// Mock para findById
		when(service.findById(existingId)).thenReturn(existingClientDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		// Mock para findByIncome
		when(service.findByIncome(eq(1500.0), any(PageRequest.class))).thenReturn(page);
		when(service.findByIncome(eq(99999.0), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

		// Mock para findByIncomeGreaterThan
		when(service.findByIncomeGreaterThan(eq(1000.0), any(PageRequest.class))).thenReturn(page);
		when(service.findByIncomeGreaterThan(eq(50000.0), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

		// Mock para findByCpfLike
		when(service.findByCpfLike(eq("106"), any(PageRequest.class))).thenReturn(page);
		when(service.findByCpfLike(eq("99999"), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

		// Mock para insert
		ClientDTO insertedClient = new ClientDTO(10L, "Maria Silva", "12345678900", 2500.0, 
			Instant.parse("1990-05-15T10:30:00Z"), 1);
		when(service.insert(any(ClientDTO.class))).thenReturn(insertedClient);

		// Mock para update
		ClientDTO updatedClient = new ClientDTO(existingId, "Maria Silva Updated", "12345678900", 3500.0,
			Instant.parse("1990-05-15T10:30:00Z"), 2);
		when(service.update(eq(existingId), any(ClientDTO.class))).thenReturn(updatedClient);
		when(service.update(eq(nonExistingId), any(ClientDTO.class))).thenThrow(ResourceNotFoundException.class);

		// Mock para delete
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
	}

	/**
	 * Testa o endpoint GET /clients para buscar todos os clientes com paginação usando mock
	 * Verifica se retorna status 200 OK, estrutura de paginação e dados mockados
	 */
	@Test
	public void findAllShouldReturnPagedClients() throws Exception {
		// Assign - Mocks já configurados no setUp
		
		// Act - Executa requisição GET para /clients
		ResultActions result = mockMvc.perform(get("/clients")
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status, paginação e dados mockados
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").exists());
		result.andExpect(jsonPath("$.totalPages").exists());
		result.andExpect(jsonPath("$.size").exists());
		result.andExpect(jsonPath("$.number").exists());
		result.andExpect(jsonPath("$.content[0].id").value(existingId));
		result.andExpect(jsonPath("$.content[0].name").value("Conceição Evaristo"));
	}

	/**
	 * Testa o endpoint GET /clients/{id} com ID existente usando mock
	 * Verifica se retorna status 200 OK e os dados mockados do cliente específico
	 */
	@Test
	public void findByIdShouldReturnClientWhenIdExists() throws Exception {
		// Assign - Mock já configurado no setUp para retornar cliente existente
		
		// Act - Executa requisição GET para /clients/{id}
		ResultActions result = mockMvc.perform(get("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e campos do cliente mockado
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value("Conceição Evaristo"));
		result.andExpect(jsonPath("$.cpf").value("10619244881"));
		result.andExpect(jsonPath("$.income").value(1500.0));
		result.andExpect(jsonPath("$.children").value(2));
	}

	/**
	 * Testa o endpoint GET /clients/{id} com ID inexistente usando mock
	 * Verifica se retorna status 404 Not Found quando o mock lança ResourceNotFoundException
	 */
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		// Assign - Mock já configurado no setUp para lançar exception
		
		// Act - Executa requisição GET para /clients/{id} com ID inexistente
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 404 e mensagem de erro
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
		result.andExpect(jsonPath("$.status").value(404));
	}

	/**
	 * Testa o endpoint GET /clients/income com renda existente usando mock
	 * Verifica se retorna status 200 OK e clientes mockados com a renda especificada
	 */
	@Test
	public void findByIncomeShouldReturnClientsWhenIncomeExists() throws Exception {
		// Assign - Define renda que tem mock configurado para retornar dados
		Double income = 1500.0;

		// Act - Executa requisição GET para /clients/income
		ResultActions result = mockMvc.perform(get("/clients/income")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e dados mockados
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").value(1));
		result.andExpect(jsonPath("$.content[0].income").value(income));
	}

	/**
	 * Testa o endpoint GET /clients/income com renda inexistente usando mock
	 * Verifica se retorna status 200 OK mas página vazia conforme mock configurado
	 */
	@Test
	public void findByIncomeShouldReturnEmptyPageWhenIncomeDoesNotExist() throws Exception {
		// Assign - Define renda que tem mock configurado para retornar lista vazia
		Double income = 99999.0;

		// Act - Executa requisição GET para /clients/income
		ResultActions result = mockMvc.perform(get("/clients/income")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status OK e página vazia conforme mock
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isEmpty());
		result.andExpect(jsonPath("$.totalElements").value(0));
	}

	/**
	 * Testa o endpoint GET /clients/incomeGreaterThan com valor que retorna clientes usando mock
	 * Verifica se retorna status 200 OK e clientes mockados com renda maior que o valor
	 */
	@Test
	public void findByIncomeGreaterThanShouldReturnClientsWhenIncomeExists() throws Exception {
		// Assign - Define renda que tem mock configurado para retornar dados
		Double income = 1000.0;

		// Act - Executa requisição GET para /clients/incomeGreaterThan
		ResultActions result = mockMvc.perform(get("/clients/incomeGreaterThan")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e dados mockados
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").value(1));
	}

	/**
	 * Testa o endpoint GET /clients/incomeGreaterThan com valor alto usando mock
	 * Verifica se retorna status 200 OK mas página vazia conforme mock configurado
	 */
	@Test
	public void findByIncomeGreaterThanShouldReturnEmptyPageWhenNoClientsFound() throws Exception {
		// Assign - Define renda que tem mock configurado para retornar lista vazia
		Double income = 50000.0;

		// Act - Executa requisição GET para /clients/incomeGreaterThan
		ResultActions result = mockMvc.perform(get("/clients/incomeGreaterThan")
				.param("income", String.valueOf(income))
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status OK e página vazia conforme mock
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isEmpty());
		result.andExpect(jsonPath("$.totalElements").value(0));
	}

	/**
	 * Testa o endpoint GET /clients/cpfLike com CPF existente usando mock
	 * Verifica se retorna status 200 OK e clientes mockados com CPF similar
	 */
	@Test
	public void findByCpfLikeShouldReturnClientsWhenCpfExists() throws Exception {
		// Assign - Define CPF que tem mock configurado para retornar dados
		String cpf = "106";

		// Act - Executa requisição GET para /clients/cpfLike
		ResultActions result = mockMvc.perform(get("/clients/cpfLike")
				.param("cpf", cpf)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status e dados mockados
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").value(1));
	}

	/**
	 * Testa o endpoint GET /clients/cpfLike com CPF inexistente usando mock
	 * Verifica se retorna status 200 OK mas página vazia conforme mock configurado
	 */
	@Test
	public void findByCpfLikeShouldReturnEmptyPageWhenCpfDoesNotExist() throws Exception {
		// Assign - Define CPF que tem mock configurado para retornar lista vazia
		String cpf = "99999";

		// Act - Executa requisição GET para /clients/cpfLike
		ResultActions result = mockMvc.perform(get("/clients/cpfLike")
				.param("cpf", cpf)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status OK e página vazia conforme mock
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isEmpty());
		result.andExpect(jsonPath("$.totalElements").value(0));
	}

	/**
	 * Testa o endpoint POST /clients para criar um novo cliente usando mock
	 * Verifica se retorna status 201 Created e os dados mockados do cliente criado
	 */
	@Test
	public void insertShouldReturnCreatedAndClientDTO() throws Exception {
		// Assign - Converte o DTO para JSON (mock já configurado no setUp)
		String json = objectMapper.writeValueAsString(clientDTO);

		// Act - Executa requisição POST para /clients
		ResultActions result = mockMvc.perform(post("/clients")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 201 e dados mockados do cliente criado
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(10L));
		result.andExpect(jsonPath("$.name").value("Maria Silva"));
		result.andExpect(jsonPath("$.cpf").value("12345678900"));
		result.andExpect(jsonPath("$.income").value(2500.0));
		result.andExpect(jsonPath("$.children").value(1));
	}

	/**
	 * Testa o endpoint DELETE /clients/{id} com ID existente usando mock
	 * Verifica se retorna status 204 No Content quando o mock não lança exception
	 */
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		// Assign - Mock já configurado no setUp para não fazer nada (sucesso)
		
		// Act - Executa requisição DELETE para /clients/{id}
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 204 No Content
		result.andExpect(status().isNoContent());
	}

	/**
	 * Testa o endpoint DELETE /clients/{id} com ID inexistente usando mock
	 * Verifica se retorna status 404 Not Found quando o mock lança ResourceNotFoundException
	 */
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		// Assign - Mock já configurado no setUp para lançar exception
		
		// Act - Executa requisição DELETE para /clients/{id} com ID inexistente
		ResultActions result = mockMvc.perform(delete("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 404 Not Found
		result.andExpect(status().isNotFound());
	}

	/**
	 * Testa o endpoint PUT /clients/{id} com ID existente usando mock
	 * Verifica se retorna status 200 OK e os dados mockados atualizados do cliente
	 */
	@Test
	public void updateShouldReturnOkAndUpdatedClientWhenIdExists() throws Exception {
		// Assign - Cria DTO com dados atualizados e converte para JSON (mock já configurado)
		ClientDTO updatedClientDTO = new ClientDTO(existingId, "Maria Silva Updated", "12345678900", 3500.0,
			Instant.parse("1990-05-15T10:30:00Z"), 2);
		String json = objectMapper.writeValueAsString(updatedClientDTO);

		// Act - Executa requisição PUT para /clients/{id}
		ResultActions result = mockMvc.perform(put("/clients/{id}", existingId)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		// Assert - Verifica status 200 e dados mockados atualizados
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value("Maria Silva Updated"));
		result.andExpect(jsonPath("$.income").value(3500.0));
		result.andExpect(jsonPath("$.children").value(2));
	}

	/**
	 * Testa o endpoint PUT /clients/{id} com ID inexistente usando mock
	 * Verifica se retorna status 404 Not Found quando o mock lança ResourceNotFoundException
	 */
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		// Assign - Cria DTO com ID inexistente e converte para JSON (mock já configurado para lançar exception)
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