package com.iftm.client.services;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

/**
 * Classe de testes de integração para ClientService
 * Testa o serviço com dados reais do banco H2, sem usar Mockito
 * 
 * @author Augusto Cesar Rezende
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalClients;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        // Arrange - dados baseados no import.sql
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalClients = 12L; // Total de clientes no import.sql
        pageRequest = PageRequest.of(0, 10);
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa a busca paginada de todos os clientes - deve retornar dados reais
     */
    @Test
    void findAllPagedShouldReturnPageWithRealData() {
        // Act
        Page<ClientDTO> result = clientService.findAllPaged(pageRequest);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(countTotalClients, result.getTotalElements());
        assertTrue(result.getSize() >= 10);
        
        // Verifica se retorna dados reais do banco
        ClientDTO firstClient = result.getContent().get(0);
        assertNotNull(firstClient.getName());
        assertNotNull(firstClient.getCpf());
        assertNotNull(firstClient.getIncome());
    }

    /**
     * @author Augusto Cesar Rezende  
     * Testa busca por ID existente - deve retornar cliente real
     */
    @Test
    void findByIdShouldReturnClientDTOWhenIdExists() {
        // Act
        ClientDTO result = clientService.findById(existingId);

        // Assert
        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals("Conceição Evaristo", result.getName());
        assertEquals("10619244889", result.getCpf());
        assertEquals(1500.0, result.getIncome());
        assertEquals(2, result.getChildren());
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa busca por ID inexistente - deve lançar exceção
     */
    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.findById(nonExistingId);
        });
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa inserção de novo cliente - deve persistir no banco
     */
    @Test
    void insertShouldPersistClientInDatabase() {
        // Arrange
        ClientDTO newClientDTO = new ClientDTO(null, "Maria Silva", "12345678901", 
                3500.0, Instant.parse("1985-06-15T10:00:00Z"), 1);
        
        long initialCount = clientRepository.count();

        // Act
        ClientDTO result = clientService.insert(newClientDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Maria Silva", result.getName());
        assertEquals("12345678901", result.getCpf());
        assertEquals(3500.0, result.getIncome());
        assertEquals(1, result.getChildren());
        
        // Verifica se foi persistido no banco
        assertEquals(initialCount + 1, clientRepository.count());
        
        // Verifica se o cliente foi realmente salvo no banco
        Client savedClient = clientRepository.findById(result.getId()).orElse(null);
        assertNotNull(savedClient);
        assertEquals("Maria Silva", savedClient.getName());
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa atualização de cliente existente - deve atualizar no banco
     */
    @Test
    void updateShouldUpdateClientInDatabase() {
        // Arrange
        ClientDTO updateDTO = new ClientDTO(existingId, "Conceição Evaristo Atualizada", 
                "10619244889", 2000.0, Instant.parse("2020-07-13T20:50:00Z"), 3);

        // Act
        ClientDTO result = clientService.update(existingId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals("Conceição Evaristo Atualizada", result.getName());
        assertEquals(2000.0, result.getIncome());
        assertEquals(3, result.getChildren());
        
        // Verifica se foi atualizado no banco
        Client updatedClient = clientRepository.findById(existingId).orElse(null);
        assertNotNull(updatedClient);
        assertEquals("Conceição Evaristo Atualizada", updatedClient.getName());
        assertEquals(2000.0, updatedClient.getIncome());
        assertEquals(3, updatedClient.getChildren());
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa atualização com ID inexistente - deve lançar exceção
     */
    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        ClientDTO updateDTO = new ClientDTO(nonExistingId, "Nome Teste", 
                "12345678901", 3000.0, Instant.now(), 1);

        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.update(nonExistingId, updateDTO);
        });
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa deleção de cliente existente - deve remover do banco
     */
    @Test
    void deleteShouldRemoveClientFromDatabase() {
        // Arrange
        long initialCount = clientRepository.count();
        
        // Verifica se o cliente existe antes da deleção
        assertTrue(clientRepository.existsById(existingId));

        // Act
        assertDoesNotThrow(() -> clientService.delete(existingId));

        // Assert
        assertEquals(initialCount - 1, clientRepository.count());
        assertFalse(clientRepository.existsById(existingId));
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa deleção com ID inexistente - deve lançar exceção
     */
    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.delete(nonExistingId);
        });
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa busca por renda específica - deve retornar clientes com a renda informada
     */
    @Test
    void findByIncomeShouldReturnClientsWithSpecificIncome() {
        // Arrange
        Double targetIncome = 2500.0; // Renda que existe no import.sql (Lázaro Ramos, Gilberto Gil, Jorge Amado)
        
        // Act - Busca todos os clientes e filtra por renda (simulando findByIncome)
        Page<ClientDTO> allClients = clientService.findAllPaged(PageRequest.of(0, 20));
        
        // Filtra clientes com a renda específica (equivalente ao findByIncome)
        long clientsWithTargetIncome = allClients.getContent().stream()
                .filter(client -> client.getIncome().equals(targetIncome))
                .count();

        // Assert
        assertNotNull(allClients);
        assertFalse(allClients.isEmpty());
        
        // Verifica se encontrou exatamente 3 clientes com renda de 2500.0
        assertEquals(3, clientsWithTargetIncome, "Deveria encontrar 3 clientes com renda de 2500.0");
        
        // Valida se os clientes corretos foram encontrados
        boolean hasLazaro = allClients.getContent().stream()
                .anyMatch(client -> client.getName().equals("Lázaro Ramos") && client.getIncome().equals(2500.0));
        boolean hasGilberto = allClients.getContent().stream()
                .anyMatch(client -> client.getName().equals("Gilberto Gil") && client.getIncome().equals(2500.0));
        boolean hasJorge = allClients.getContent().stream()
                .anyMatch(client -> client.getName().equals("Jorge Amado") && client.getIncome().equals(2500.0));
        
        assertTrue(hasLazaro, "Deveria encontrar Lázaro Ramos com renda 2500.0");
        assertTrue(hasGilberto, "Deveria encontrar Gilberto Gil com renda 2500.0");
        assertTrue(hasJorge, "Deveria encontrar Jorge Amado com renda 2500.0");
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa busca por renda inexistente - deve retornar página vazia
     */
    @Test
    void findByIncomeShouldReturnEmptyWhenIncomeNotExists() {
        // Arrange
        Double nonExistentIncome = 99999.0; // Renda que não existe no import.sql
        
        // Act - Busca todos os clientes e filtra por renda inexistente
        Page<ClientDTO> allClients = clientService.findAllPaged(PageRequest.of(0, 20));
        
        // Filtra clientes com renda inexistente
        long clientsWithNonExistentIncome = allClients.getContent().stream()
                .filter(client -> client.getIncome().equals(nonExistentIncome))
                .count();

        // Assert
        assertNotNull(allClients);
        assertEquals(0, clientsWithNonExistentIncome, "Não deveria encontrar clientes com renda de 99999.0");
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa a consistência dos dados através de busca paginada
     */
    @Test
    void shouldValidateDataConsistencyThroughPagination() {
        // Arrange
        Double expectedIncome = 2500.0; // Renda que existe no import.sql
        
        // Act - Busca todos os clientes
        Page<ClientDTO> allClients = clientService.findAllPaged(PageRequest.of(0, 20));

        // Assert
        assertNotNull(allClients);
        assertFalse(allClients.isEmpty());
        
        // Verifica se existem clientes com a renda esperada (validação manual dos dados)
        long clientsWithExpectedIncome = allClients.getContent().stream()
                .filter(client -> client.getIncome().equals(expectedIncome))
                .count();
        
        // Verifica se encontrou os clientes esperados (Lázaro Ramos, Gilberto Gil, Jorge Amado)
        assertEquals(3, clientsWithExpectedIncome);
        
        // Verifica se não existem clientes com renda inexistente
        long clientsWithHighIncome = allClients.getContent().stream()
                .filter(client -> client.getIncome().equals(99999.0))
                .count();
        
        assertEquals(0, clientsWithHighIncome);
    }

    /**
     * @author Augusto Cesar Rezende
     * Testa integridade dos dados após múltiplas operações
     */
    @Test
    void shouldMaintainDataIntegrityAfterMultipleOperations() {
        // Arrange
        long initialCount = clientRepository.count();
        
        // Act - Inserir um cliente
        ClientDTO newClient = new ClientDTO(null, "Teste Integração", "11111111111", 
                4000.0, Instant.now(), 2);
        ClientDTO insertedClient = clientService.insert(newClient);
        
        // Act - Atualizar o cliente
        insertedClient.setName("Teste Integração Atualizado");
        insertedClient.setIncome(4500.0);
        ClientDTO updatedClient = clientService.update(insertedClient.getId(), insertedClient);
        
        // Act - Buscar o cliente
        ClientDTO foundClient = clientService.findById(insertedClient.getId());
        
        // Act - Deletar o cliente
        clientService.delete(insertedClient.getId());

        // Assert
        assertEquals("Teste Integração Atualizado", updatedClient.getName());
        assertEquals(4500.0, updatedClient.getIncome());
        assertEquals("Teste Integração Atualizado", foundClient.getName());
        assertEquals(initialCount, clientRepository.count()); // Volta ao count inicial
        assertFalse(clientRepository.existsById(insertedClient.getId()));
    }
}