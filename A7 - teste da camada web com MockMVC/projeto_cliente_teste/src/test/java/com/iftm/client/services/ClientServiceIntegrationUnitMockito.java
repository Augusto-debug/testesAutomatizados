package com.iftm.client.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

/**
 * Classe de testes unitários do ClientService utilizando Mockito
 * Testa os métodos do service de forma isolada com mocks do repository
 * 
 * @author Augusto Cesar Rezende
 */
@ExtendWith(SpringExtension.class)
public class ClientServiceIntegrationUnitMockito {

    @InjectMocks
    private ClientService servico;

    @Mock
    private ClientRepository repositorio;

    /**
     * Testa o método delete da classe service para verificar se não retorna nada e exclui o registro quando o ID existe
     * Verifica se o método deleteById do repository é chamado exatamente uma vez e não lança exceções
     */
    @Test
    public void testarApagarClienteQuandoIDExisteNaoRetornandoNada(){
        // Assign - Configura ID existente e mock para não fazer nada (simula sucesso)
        Long idExistente = 1L;
        Mockito.doNothing().when(repositorio).deleteById(idExistente);
        
        // Act & Assert - Executa o método e verifica que não lança exceção
        assertDoesNotThrow(()->{servico.delete(idExistente);});
        
        // Assert - Verifica se o método do repository foi chamado exatamente uma vez
        Mockito.verify(repositorio, Mockito.times(1)).deleteById(idExistente);
    }

    /**
     * Testa o método delete da classe service para verificar se retorna ResourceNotFoundException quando o ID não existe
     * Simula o comportamento do repository lançando EmptyResultDataAccessException e verifica se o service converte para ResourceNotFoundException
     */
    @Test
    public void testarApagarClienteQuandoIDNaoExisteRetornaException(){
        // Assign - Configura ID inexistente e mock para lançar EmptyResultDataAccessException
        Long idNaoExistente = 100L;        
        Mockito.doThrow(org.springframework.dao.EmptyResultDataAccessException.class).when(repositorio).deleteById(idNaoExistente);

        // Act & Assert - Executa o método e verifica se lança ResourceNotFoundException
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()->{servico.delete(idNaoExistente);});
        
        // Assert - Verifica a mensagem da exceção e se o método do repository foi chamado
        assertEquals("Id not found " + idNaoExistente, e.getMessage());
        Mockito.verify(repositorio, Mockito.times(1)).deleteById(idNaoExistente);
    }    
}
