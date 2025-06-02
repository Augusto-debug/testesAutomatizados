package com.iftm.client.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {
    
    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

    private Long idExistente;
    private Long idNaoExistente;
    private Long idDependente;

    private PageRequest pageRequest;
    private Client client;
    private Page<Client> clientePagina;

    private Double targetIncome;
    private Client clientWithTargetIncome;
    private Page<Client> pageOfClientsWithTargetIncome;

    private ClientDTO clientDTOtoUpdate;
    private Client uptadeClientEntity;

    @BeforeEach
    void setUp() throws Exception {
        idExistente = 1L;
        idNaoExistente = 1000L;
        idDependente = 4L;

        Mockito.doNothing().when(repository).deleteById(idExistente);

        Mockito.doThrow(new EmptyResultDataAccessException(1))
            .when(repository).deleteById(idNaoExistente);

        Mockito.doThrow(DataIntegrityViolationException.class)
            .when(repository).deleteById(idDependente);

        pageRequest = PageRequest.of(0, 10);
        client = new Client(1L, "Cliente Test", "12345678901", 2000.0, Instant.now(), 0);
        clientePagina = new PageImpl<>(List.of(client), pageRequest, 1);

        Mockito.when(repository.findAll(pageRequest)).thenReturn(clientePagina);

        targetIncome = 2000.0;
        clientWithTargetIncome = new Client(2L, "Cliente Alvo", "09876543210", targetIncome, Instant.now(), 1);
        pageOfClientsWithTargetIncome = new PageImpl<>(List.of(clientWithTargetIncome), pageRequest, 1);

        Mockito.when(repository.findByIncome(eq(targetIncome), any(PageRequest.class)))
            .thenReturn(pageOfClientsWithTargetIncome);

        Mockito.when(repository.findById(idExistente)).thenReturn(Optional.of(client));

        Mockito.when(repository.findById(idNaoExistente)).thenReturn(Optional.empty());

        clientDTOtoUpdate = new ClientDTO(null, "Nome Atualizado", "99988877700", 5000.0, Instant.parse("2001-10-20T00:00:00Z"), 2);

        Client originalClient = new Client(idExistente, "Cliente Original", "45612398700", 4000.0, Instant.parse("1998-03-10T00:00:00Z"), 1);
    
        Mockito.when(repository.getOne(idExistente)).thenReturn(originalClient);

        Mockito.when(repository.getOne(idNaoExistente))
            .thenThrow(new EntityNotFoundException("Id não encontrado: " + idNaoExistente));

        uptadeClientEntity = new Client();
        uptadeClientEntity.setId(idExistente);
        uptadeClientEntity.setName(clientDTOtoUpdate.getName());
        uptadeClientEntity.setCpf(clientDTOtoUpdate.getCpf());
        uptadeClientEntity.setIncome(clientDTOtoUpdate.getIncome());
        uptadeClientEntity.setBirthDate(clientDTOtoUpdate.getBirthDate());
        uptadeClientEntity.setChildren(clientDTOtoUpdate.getChildren());

        Mockito.when(repository.save(any(Client.class))).thenReturn(uptadeClientEntity);
    }

    /* delete deveria
    ◦ retornar vazio quando o id existir
    ◦ lançar uma EmptyResultDataAccessException quando o id não existir */
    @Test
    public void apagarNaoDeveFazerNadaQuandoIdExistente() {
        Assertions.assertDoesNotThrow(()->{service.delete(idExistente);});
        Mockito.verify(repository, Mockito.times(1)).deleteById(idExistente);
    }

    @Test 
    void apagarDeveLancarUmaExcecaoQuandoIdNaoExistente() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(idNaoExistente);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(idNaoExistente);
    }

    // findAllPaged deveria retornar uma página com todos os clientes (e chamar o método findAll do repository)
    @Test
    void findAllPageDeveRetornarPaginaComTodosOsClientes() {
        Page<ClientDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");

        Assertions.assertEquals(1, result.getTotalElements(), "Deveria haver 1 cliente na página");

        Assertions.assertFalse(result.getContent().isEmpty(), "A lista de clientes não deve estar vazia");

        Assertions.assertEquals(client.getName(), result.getContent().get(0).getName(), "O nome do cliente deve ser igual");

        Assertions.assertEquals(client.getCpf(), result.getContent().get(0).getCpf(), "O CPF do cliente deve ser igual");

        Mockito.verify(repository, Mockito.times(1)).findAll(pageRequest);
    }

    // findByIncome deveria retornar uma página com os clientes que tenham o Income informado (e chamar o método findByIncome do repository)
    @Test
    public void findByIncomeDeveRetornarPaginaComClientesComIncomeEspecifico() {
        Page<ClientDTO> result = service.findByIncome(targetIncome, pageRequest);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");

        Assertions.assertEquals(1, result.getTotalElements(), "Deveria haver 1 cliente com o income especificado");

        Assertions.assertFalse(result.getContent().isEmpty(), "A lista de clientes não deve estar vazia");

        ClientDTO clientDTO = result.getContent().get(0);
        Assertions.assertEquals(clientWithTargetIncome.getId(), clientDTO.getId());
        Assertions.assertEquals(clientWithTargetIncome.getName(), clientDTO.getName());
        Assertions.assertEquals(targetIncome, clientDTO.getIncome());

        Mockito.verify(repository, Mockito.times(1)).findByIncome(eq(targetIncome), eq(pageRequest));
    }

    /* findById deveria
    ◦ retornar um ClientDTO quando o id existir
    ◦ lançar ResourceNotFoundException quando o id não existir */
    @Test
    public void findByIdDeveRetornarClientDTOQuandoIdExistir() {
        ClientDTO result = service.findById(idExistente);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(client.getId(), result.getId(), "O ID do cliente deve ser igual");
        Assertions.assertEquals(client.getName(), result.getName(), "O nome do cliente deve ser igual");
        Assertions.assertEquals(client.getCpf(), result.getCpf(), "O CPF do cliente deve ser igual");

        Mockito.verify(repository, Mockito.times(1)).findById(idExistente);
    }

    @Test
    public void findByIdDeveLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(idNaoExistente);
        });

        Mockito.verify(repository, Mockito.times(1)).findById(idNaoExistente);
    }

    /*  update deveria
    ◦ retornar um ClientDTO quando o id existir
    ◦ lançar uma ResourceNotFoundException quando o id não existir */

    @Test
    public void updateDeveRetornarClientDTOQuandoIdExistir() {
        ClientDTO result = service.update(idExistente, clientDTOtoUpdate);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(clientDTOtoUpdate.getName(), result.getName(), "O nome do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getCpf(), result.getCpf(), "O CPF do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getIncome(), result.getIncome(), "A renda do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getChildren(), result.getChildren(), "O número de filhos do cliente atualizado deve ser igual");

        Mockito.verify(repository, Mockito.times(1)).getOne(idExistente);
        Mockito.verify(repository, Mockito.times(1)).save(any(Client.class));
    }

    @Test
    public void updateDeveLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(idNaoExistente, clientDTOtoUpdate);
        });

        Mockito.verify(repository, Mockito.times(1)).getOne(idNaoExistente);
    }

    //  insert deveria retornar um ClientDTO ao inserir um novo cliente

    @Test
    public void insertDeveRetornarClientDTOAoInserirNovoCliente() {

        ClientDTO newClientInputDTO = new ClientDTO(
            null, 
            "Novo Cliente Inserido", 
            "12345678900", 
            3500.0, 
            Instant.now(), 
            1
        );

        Long idGeradoSimulado = 101L;
        Client clienteSalvoComId = new Client();
        clienteSalvoComId.setId(idGeradoSimulado);
        clienteSalvoComId.setName(newClientInputDTO.getName());
        clienteSalvoComId.setCpf(newClientInputDTO.getCpf());
        clienteSalvoComId.setIncome(newClientInputDTO.getIncome());
        clienteSalvoComId.setBirthDate(newClientInputDTO.getBirthDate());
        clienteSalvoComId.setChildren(newClientInputDTO.getChildren());

        Mockito.when(repository.save(any(Client.class))).thenReturn(clienteSalvoComId);

        ClientDTO resultDTO = service.insert(newClientInputDTO);

        Assertions.assertNotNull(resultDTO, "O resultado não deve ser nulo");

        Assertions.assertEquals(idGeradoSimulado, resultDTO.getId(), "O ID do cliente inserido deve ser o ID gerado.");
        
        Assertions.assertEquals(newClientInputDTO.getName(), resultDTO.getName(), "O nome do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getCpf(), resultDTO.getCpf(), "O CPF do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getIncome(), resultDTO.getIncome(), "A renda do cliente inserida deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getChildren(), resultDTO.getChildren(), "O número de filhos do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getBirthDate(), resultDTO.getBirthDate(), "A data de nascimento do cliente inserido deve ser igual");

        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        Mockito.verify(repository, Mockito.times(1)).save(clientArgumentCaptor.capture());
        
        Client entidadePassadaParaSave = clientArgumentCaptor.getValue();
        
        Assertions.assertNull(entidadePassadaParaSave.getId(), "O ID da entidade passada para o método save deveria ser nulo.");
        Assertions.assertEquals(newClientInputDTO.getName(), entidadePassadaParaSave.getName(), "O nome da entidade passada para save não confere.");
    }

}
