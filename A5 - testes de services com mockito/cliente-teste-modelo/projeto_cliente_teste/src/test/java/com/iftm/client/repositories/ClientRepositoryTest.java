package com.iftm.client.repositories;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.iftm.client.entities.Client;

@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes com idade menor que 30")
    void deveRetornarClientesComIdadeMenorQueValorInformado() {
        // Arrange
        Integer age = 30;

        // Act
        List<Client> result = repository.findByAgeLessThan(age);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Conceição Evaristo", result.get(0).getName());
        assertEquals("Lázaro Ramos", result.get(1).getName());
        assertEquals("Carolina Maria de Jesus", result.get(2).getName());
        assertEquals("Jose Saramago", result.get(3).getName());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes nascidos em janeiro, fevereiro ou março")
    void deveRetornarClientesNascidosEmMesesEspecificos() {
        // Arrange
        List<Integer> months = Arrays.asList(1, 2, 3);

        // Act
        List<Client> result = repository.findByBirthDateInMonths(months);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toni Morrison", result.get(0).getName());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes com número de filhos entre 1 e 3")
    void deveRetornarClientesComNumeroDeFilhosEntreValoresInformados() {
        // Arrange
        Integer min = 1;
        Integer max = 3;

        // Act
        List<Client> result = repository.findByChildrenBetween(min, max);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes com renda acima da média")
    void deveRetornarClientesComRendaAcimaDaMedia() {
        // Arrange

        // Act
        List<Client> result = repository.findByIncomeAboveAverage();

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes com renda múltipla de 2500")
    void deveRetornarClientesComRendaMultiplaDeValorInformado() {
        // Arrange
        Double value = 2500.0;

        // Act
        List<Client> result = repository.findByIncomeMultipleOf(value);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.size());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes cujo nome termina com sufixo (ignora case)")
    void deveRetornarClientesComNomeTerminandoComSufixoIgnorandoCase() {
        // Arrange
        String suffix = "amos";

        // Act
        List<Client> result = repository.findByNameEndingWithIgnoreCase(suffix);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lázaro Ramos", result.get(0).getName());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna apenas um cliente por CPF informado")
    void deveRetornarApenasUmClientePorCpfInformado() {
        // Arrange
        String cpf = "10619244881";

        // Act
        List<Client> result = repository.findClientsByCpf(cpf);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cpf, result.get(0).getCpf());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna cliente por nome ignorando case")
    void deveRetornarClientePorNomeIgnorandoCase() {
        // Arrange
        String name = "lázaro ramos";

        // Act
        Client result = repository.findClientByNameIgnoreCase(name);

        // Assert
        assertNotNull(result);
        assertEquals("Lázaro Ramos", result.getName());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes com nascimento entre duas datas")
    void deveRetornarClientesComNascimentoEntreDuasDatas() {
        // Arrange
        Instant startDate = Instant.parse("1990-01-01T00:00:00Z");
        Instant endDate = Instant.parse("2000-01-01T00:00:00Z");

        // Act
        List<Client> result = repository.findClientsByBirthDateBetween(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes com salário maior que valor informado")
    void deveRetornarClientesComSalarioMaiorQueValorInformado() {
        // Arrange
        Double income = 5000.0;

        // Act
        List<Client> result = repository.findClientsByIncomeGreaterThan(income);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Augusto Cesar Rezende
     */
    @Test
    @DisplayName("Retorna clientes com salário menor que valor informado")
    void deveRetornarClientesComSalarioMenorQueValorInformado() {
        // Arrange
        Double income = 2000.0;

        // Act
        List<Client> result = repository.findClientsByIncomeLessThan(income);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }
}
