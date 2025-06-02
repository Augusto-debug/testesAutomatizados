package com.iftm.client.repositories;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    private PageRequest pageRequest;
    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        // Arrange – @Autor: Augusto Cesar Rezende

        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 4L;

        pageRequest = PageRequest.of(0, 10);
        client = new Client(existingId, "João Silva", "12345678900", 3000.0, Instant.now(), 2);
        clientDTO = new ClientDTO(client);

        // findAll
        when(repository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(client)));

        // findByIncome
        when(repository.findByIncome(eq(3000.0), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(client)));

        // findById
        when(repository.findById(existingId)).thenReturn(Optional.of(client));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // getOne (para update)
        when(repository.getOne(existingId)).thenReturn(client);
        when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        // save
        when(repository.save(any())).thenReturn(client);

        // delete
        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa a deleção com ID existente
    @Test
    void deleteShouldDoNothingWhenIdExists() {
        // Act
        assertDoesNotThrow(() -> service.delete(existingId));

        // Assert
        verify(repository, times(1)).deleteById(existingId);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa exceção ao deletar ID inexistente
    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
        verify(repository).deleteById(nonExistingId);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa exceção ao deletar cliente com dependência
    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        // Act + Assert
        assertThrows(DataIntegrityViolationException.class, () -> service.delete(dependentId));
        verify(repository).deleteById(dependentId);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa listagem paginada de todos os clientes
    @Test
    void findAllPagedShouldReturnPage() {
        // Act
        Page<ClientDTO> result = service.findAllPaged(pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(pageRequest);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa listagem paginada por renda
    @Test
    void findByIncomeShouldReturnFilteredPage() {
        // Act
        Page<ClientDTO> result = service.findByIncome(3000.0, pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().get(0).getName());
        verify(repository).findByIncome(eq(3000.0), eq(pageRequest));
    }

    // @Autor: Augusto Cesar Rezende
    // Testa busca por ID existente
    @Test
    void findByIdShouldReturnClientDTOWhenIdExists() {
        // Act
        ClientDTO result = service.findById(existingId);

        // Assert
        assertNotNull(result);
        assertEquals(client.getName(), result.getName());
        verify(repository).findById(existingId);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa exceção ao buscar por ID inexistente
    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
        verify(repository).findById(nonExistingId);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa atualização de cliente com ID existente
    @Test
    void updateShouldReturnClientDTOWhenIdExists() {
        // Arrange
        ClientDTO updateDTO = new ClientDTO(null, "Maria Atualizada", "00011122233", 5000.0, Instant.now(), 0);
        Client updatedEntity = new Client(existingId, updateDTO.getName(), updateDTO.getCpf(), updateDTO.getIncome(), updateDTO.getBirthDate(), updateDTO.getChildren());

        when(repository.save(any())).thenReturn(updatedEntity);

        // Act
        ClientDTO result = service.update(existingId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Maria Atualizada", result.getName());
        assertEquals("00011122233", result.getCpf());
        verify(repository).getOne(existingId);
        verify(repository).save(any(Client.class));
    }

    // @Autor: Augusto Cesar Rezende
    // Testa exceção ao atualizar cliente com ID inexistente
    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Act + Assert
        assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, clientDTO));
        verify(repository).getOne(nonExistingId);
    }

    // @Autor: Augusto Cesar Rezende
    // Testa inserção de novo cliente
    @Test
    void insertShouldReturnSavedClientDTO() {
        // Arrange
        ClientDTO newClientDTO = new ClientDTO(null, "Carlos Novo", "99988877700", 4000.0, Instant.now(), 1);
        Client savedClient = new Client(10L, newClientDTO.getName(), newClientDTO.getCpf(), newClientDTO.getIncome(), newClientDTO.getBirthDate(), newClientDTO.getChildren());

        when(repository.save(any())).thenReturn(savedClient);

        // Act
        ClientDTO result = service.insert(newClientDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Carlos Novo", result.getName());
        assertEquals("99988877700", result.getCpf());
        assertEquals(4000.0, result.getIncome());

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(repository).save(captor.capture());

        Client capturedClient = captor.getValue();
        assertEquals("Carlos Novo", capturedClient.getName());
        assertNull(capturedClient.getId()); // ID deve ser nulo antes de salvar
    }
}
