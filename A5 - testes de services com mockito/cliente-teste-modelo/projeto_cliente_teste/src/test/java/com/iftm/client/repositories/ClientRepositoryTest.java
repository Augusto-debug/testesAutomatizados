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

    /*
     * <b>Cenário:</b> Verifica clientes com idade menor que um valor específico.
     * <br>Considerando a data atual como 19 de maio de 2025.
     * <b>Entrada:</b> age = 30.
     * <b>Saída esperada:</b> Lista com 4 clientes mais jovens que 30 anos (Conceição Evaristo, Lázaro Ramos, Carolina Maria de Jesus e Jose Saramago).
     */
    @Test
    @DisplayName("Testa busca por idade menor que um valor específico.")
    void testFindByAgeLessThan() {
        // Arrange
        Integer age = 30;

        // Act
        List<Client> result = repository.findByAgeLessThan(age);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Conceição Evaristo", result.stream().filter(c -> c.getName().equals("Conceição Evaristo")).findFirst().orElse(null).getName());
        assertEquals("Lázaro Ramos", result.stream().filter(c -> c.getName().equals("Lázaro Ramos")).findFirst().orElse(null).getName());
        assertEquals("Carolina Maria de Jesus", result.stream().filter(c -> c.getName().equals("Carolina Maria de Jesus")).findFirst().orElse(null).getName());
        assertEquals("Jose Saramago", result.stream().filter(c -> c.getName().equals("Jose Saramago")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Verifica clientes nascidos em meses específicos.
     * <b>Entrada:</b> months = [1, 2, 3].
     * <b>Saída esperada:</b> Lista com 1 cliente nascido em fevereiro (Toni Morrison).
     */
    @Test
    @DisplayName("Testa busca por data de nascimento em meses específicos.")
    void testFindByBirthDateInMonths() {
        // Arrange
        List<Integer> months = Arrays.asList(1, 2, 3);

        // Act
        List<Client> result = repository.findByBirthDateInMonths(months);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toni Morrison", result.get(0).getName());
    }

    /*
     * <b>Cenário:</b> Verifica clientes pelo número de filhos em uma faixa.
     * <b>Entrada:</b> minChildren = 1, maxChildren = 3.
     * <b>Saída esperada:</b> Lista com 5 clientes (Conceição Evaristo, Lázaro Ramos, Clarice Lispector, Djamila Ribeiro e Silvio Almeida).
     */
    @Test
    @DisplayName("Testa busca por número de filhos entre dois valores.")
    void testFindByChildrenBetween() {
        // Arrange
        Integer minChildren = 1;
        Integer maxChildren = 3;

        // Act
        List<Client> result = repository.findByChildrenBetween(minChildren, maxChildren);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("Conceição Evaristo", result.stream().filter(c -> c.getName().equals("Conceição Evaristo")).findFirst().orElse(null).getName());
        assertEquals("Lázaro Ramos", result.stream().filter(c -> c.getName().equals("Lázaro Ramos")).findFirst().orElse(null).getName());
        assertEquals("Clarice Lispector", result.stream().filter(c -> c.getName().equals("Clarice Lispector")).findFirst().orElse(null).getName());
        assertEquals("Djamila Ribeiro", result.stream().filter(c -> c.getName().equals("Djamila Ribeiro")).findFirst().orElse(null).getName());
        assertEquals("Silvio Almeida", result.stream().filter(c -> c.getName().equals("Silvio Almeida")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Verifica clientes com renda acima da média (média aproximada = 3933.33).
     * <b>Saída esperada:</b> Lista com 5 clientes (Carolina Maria de Jesus, Djamila Ribeiro, Jose Saramago, Toni Morrison e Silvio Almeida).
     */
    @Test
    @DisplayName("Testa busca por clientes com renda acima da média.")
    void testFindByIncomeAboveAverage() {
        // Act
        List<Client> result = repository.findByIncomeAboveAverage();

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("Carolina Maria de Jesus", result.stream().filter(c -> c.getName().equals("Carolina Maria de Jesus")).findFirst().orElse(null).getName());
        assertEquals("Djamila Ribeiro", result.stream().filter(c -> c.getName().equals("Djamila Ribeiro")).findFirst().orElse(null).getName());
        assertEquals("Jose Saramago", result.stream().filter(c -> c.getName().equals("Jose Saramago")).findFirst().orElse(null).getName());
        assertEquals("Toni Morrison", result.stream().filter(c -> c.getName().equals("Toni Morrison")).findFirst().orElse(null).getName());
        assertEquals("Silvio Almeida", result.stream().filter(c -> c.getName().equals("Silvio Almeida")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Verifica clientes com renda múltipla de um valor.
     * <b>Entrada:</b> value = 2500.
     * <b>Saída esperada:</b> Lista com 6 clientes (Lázaro Ramos, Carolina Maria de Jesus, Gilberto Gil, Jose Saramago, Jorge Amado e Toni Morrison).
     */
    @Test
    @DisplayName("Testa busca por clientes com renda múltipla de um valor.")
    void testFindByIncomeMultipleOf() {
        // Arrange
        Double value = 2500.0;

        // Act
        List<Client> result = repository.findByIncomeMultipleOf(value);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.size());
        assertEquals("Lázaro Ramos", result.stream().filter(c -> c.getName().equals("Lázaro Ramos")).findFirst().orElse(null).getName());
        assertEquals("Carolina Maria de Jesus", result.stream().filter(c -> c.getName().equals("Carolina Maria de Jesus")).findFirst().orElse(null).getName());
        assertEquals("Gilberto Gil", result.stream().filter(c -> c.getName().equals("Gilberto Gil")).findFirst().orElse(null).getName());
        assertEquals("Jose Saramago", result.stream().filter(c -> c.getName().equals("Jose Saramago")).findFirst().orElse(null).getName());
        assertEquals("Jorge Amado", result.stream().filter(c -> c.getName().equals("Jorge Amado")).findFirst().orElse(null).getName());
        assertEquals("Toni Morrison", result.stream().filter(c -> c.getName().equals("Toni Morrison")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Verifica clientes pelo nome terminado com um sufixo (ignorando case).
     * <b>Entrada:</b> suffix = "amos".
     * <b>Saída esperada:</b> Lista com 1 cliente (Lázaro Ramos).
     */
    @Test
    @DisplayName("Testa busca por nome terminado com um sufixo (ignora case).")
    void testFindByNameEndingWithIgnoreCase() {
        // Arrange
        String suffix = "amos";

        // Act
        List<Client> result = repository.findByNameEndingWithIgnoreCase(suffix);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lázaro Ramos", result.get(0).getName());
    }

    /*
     * <b>Cenário:</b> Verifica a unicidade do CPF ao buscar.
     * <b>Entrada:</b> cpf = "10619244881".
     * <b>Saída esperada:</b> Erro no teste se mais de um cliente for encontrado com o mesmo CPF.
     */
    @Test
    @DisplayName("Testa a unicidade do CPF ao buscar.")
    void testFindClientByCpfUnico() {
        // Arrange
        String cpf = "10619244881";

        // Act
        List<Client> results = repository.findClientsByCpf(cpf);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size(), "Erro: Múltiplos clientes encontrados com o mesmo CPF: " + cpf);
        assertEquals(cpf, results.get(0).getCpf());
    }

    /*
     * <b>Cenário:</b> Verifica busca por nome ignorando case sensitivity.
     * <b>Entrada:</b> name = "lázaro ramos".
     * <b>Saída esperada:</b> Cliente com nome "Lázaro Ramos".
     */
    @Test
    @DisplayName("Testa busca por nome ignorando case sensitivity.")
    void testFindClientByNameIgnoreCase() {
        // Arrange
        String name = "lázaro ramos";

        // Act
        Client result = repository.findClientByNameIgnoreCase(name);

        // Assert
        assertNotNull(result);
        assertEquals("Lázaro Ramos", result.getName());
    }

    /*
     * <b>Cenário:</b> Verifica busca de clientes por data de nascimento entre dois valores.
     * <b>Entrada:</b> startDate = "1990-01-01T00:00:00Z", endDate = "2000-01-01T00:00:00Z".
     * <b>Saída esperada:</b> Lista com 3 clientes (Lázaro Ramos, Carolina Maria de Jesus e Jose Saramago).
     */
    @Test
    @DisplayName("Testa busca de clientes por data de nascimento entre dois valores.")
    void testFindClientsByBirthDateBetween() {
        // Arrange
        Instant startDate = Instant.parse("1990-01-01T00:00:00Z");
        Instant endDate = Instant.parse("2000-01-01T00:00:00Z");

        // Act
        List<Client> result = repository.findClientsByBirthDateBetween(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Lázaro Ramos", result.stream().filter(c -> c.getName().equals("Lázaro Ramos")).findFirst().orElse(null).getName());
        assertEquals("Carolina Maria de Jesus", result.stream().filter(c -> c.getName().equals("Carolina Maria de Jesus")).findFirst().orElse(null).getName());
        assertEquals("Jose Saramago", result.stream().filter(c -> c.getName().equals("Jose Saramago")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes com salário maior que um valor específico.
     * <b>Entrada:</b> income = 5000.0.
     * <b>Saída esperada:</b> Lista com 2 clientes (Carolina Maria de Jesus e Toni Morrison).
     */
    @Test
    @DisplayName("Testa busca por salário maior que um valor.")
    void testFindClientsByIncomeGreaterThan() {
        // Arrange
        Double income = 5000.0;

        // Act
        List<Client> result = repository.findClientsByIncomeGreaterThan(income);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Carolina Maria de Jesus", result.stream().filter(c -> c.getName().equals("Carolina Maria de Jesus")).findFirst().orElse(null).getName());
        assertEquals("Toni Morrison", result.stream().filter(c -> c.getName().equals("Toni Morrison")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes com salário menor que um valor específico.
     * <b>Entrada:</b> income = 2000.0.
     * <b>Saída esperada:</b> Lista com 3 clientes (Conceição Evaristo, Yuval Noah Harari e Chimamanda Adichie).
     */
    @Test
    @DisplayName("Testa busca por salário menor que um valor.")
    void testFindClientsByIncomeLessThan() {
        // Arrange
        Double income = 2000.0;

        // Act
        List<Client> result = repository.findClientsByIncomeLessThan(income);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Conceição Evaristo", result.stream().filter(c -> c.getName().equals("Conceição Evaristo")).findFirst().orElse(null).getName());
        assertEquals("Yuval Noah Harari", result.stream().filter(c -> c.getName().equals("Yuval Noah Harari")).findFirst().orElse(null).getName());
        assertEquals("Chimamanda Adichie", result.stream().filter(c -> c.getName().equals("Chimamanda Adichie")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes com salário entre dois valores.
     * <b>Entrada:</b> minIncome = 2000.0, maxIncome = 3000.0.
     * <b>Saída esperada:</b> Lista com 3 clientes (Lázaro Ramos, Gilberto Gil, Jorge Amado).
     */
    @Test
    @DisplayName("Testa busca por salário entre dois valores.")
    void testFindClientsByIncomeBetween() {
        // Arrange
        Double minIncome = 2000.0;
        Double maxIncome = 3000.0;

        // Act
        List<Client> result = repository.findClientsByIncomeBetween(minIncome, maxIncome);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Lázaro Ramos", result.stream().filter(c -> c.getName().equals("Lázaro Ramos")).findFirst().orElse(null).getName());
        assertEquals("Gilberto Gil", result.stream().filter(c -> c.getName().equals("Gilberto Gil")).findFirst().orElse(null).getName());
        assertEquals("Jorge Amado", result.stream().filter(c -> c.getName().equals("Jorge Amado")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes pelo número exato de filhos.
     * <b>Entrada:</b> children = 2.
     * <b>Saída esperada:</b> Lista com 4 clientes (Conceição Evaristo, Lázaro Ramos, Clarice Lispector e Silvio Almeida).
     */
    @Test
    @DisplayName("Testa busca por número exato de filhos.")
    void testFindClientsByChildren() {
        // Arrange
        Integer children = 2;

        // Act
        List<Client> result = repository.findClientsByChildren(children);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Conceição Evaristo", result.stream().filter(c -> c.getName().equals("Conceição Evaristo")).findFirst().orElse(null).getName());
        assertEquals("Lázaro Ramos", result.stream().filter(c -> c.getName().equals("Lázaro Ramos")).findFirst().orElse(null).getName());
        assertEquals("Clarice Lispector", result.stream().filter(c -> c.getName().equals("Clarice Lispector")).findFirst().orElse(null).getName());
        assertEquals("Silvio Almeida", result.stream().filter(c -> c.getName().equals("Silvio Almeida")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes cujo nome contém uma palavra específica (ignorando case).
     * <b>Entrada:</b> word = "maria".
     * <b>Saída esperada:</b> Lista com 1 cliente (Carolina Maria de Jesus).
     */
    @Test
    @DisplayName("Testa busca por nome contendo uma palavra (ignora case).")
    void testFindClientsByNameContaining() {
        // Arrange
        String word = "maria";

        // Act
        List<Client> result = repository.findClientsByNameContaining(word);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Carolina Maria de Jesus", result.get(0).getName());
    }

    /*
     * <b>Cenário:</b> Busca cliente pelo nome exato (ignorando case).
     * <b>Entrada:</b> name = "clarice lispector".
     * <b>Saída esperada:</b> Cliente com nome "Clarice Lispector".
     */
    @Test
    @DisplayName("Testa busca por nome exato (ignora case).")
    void testFindClientByNameIgnoreCaseExact() {
        // Arrange
        String name = "clarice lispector";

        // Act
        Client result = repository.findClientByNameIgnoreCase(name);

        // Assert
        assertNotNull(result);
        assertEquals("Clarice Lispector", result.getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes cujo nome contém uma parte (ignorando case).
     * <b>Entrada:</b> word = "silvio".
     * <b>Saída esperada:</b> Lista com 1 cliente (Silvio Almeida).
     */
    @Test
    @DisplayName("Testa busca por nome contendo parte da string (ignora case).")
    void testFindClientsByNameContainingPartial() {
        // Arrange
        String word = "silvio";

        // Act
        List<Client> result = repository.findClientsByNameContaining(word);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Silvio Almeida", result.get(0).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes com data de nascimento dentro de um intervalo específico.
     * <b>Entrada:</b> startDate = "1955-01-01T00:00:00Z", endDate = "1965-01-01T00:00:00Z".
     * <b>Saída esperada:</b> Lista com 2 clientes (Clarice Lispector e Yuval Noah Harari).
     */
    @Test
    @DisplayName("Testa busca por data de nascimento em um intervalo diferente.")
    void testFindClientsByBirthDateBetweenDifferentRange() {
        // Arrange
        Instant startDate = Instant.parse("1955-01-01T00:00:00Z");
        Instant endDate = Instant.parse("1965-01-01T00:00:00Z");

        // Act
        List<Client> result = repository.findClientsByBirthDateBetween(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Clarice Lispector", result.stream().filter(c -> c.getName().equals("Clarice Lispector")).findFirst().orElse(null).getName());
        assertEquals("Yuval Noah Harari", result.stream().filter(c -> c.getName().equals("Yuval Noah Harari")).findFirst().orElse(null).getName());
        assertEquals("Chimamanda Adichie", result.stream().filter(c -> c.getName().equals("Chimamanda Adichie")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes com um número específico de filhos (zero).
     * <b>Entrada:</b> children = 0.
     * <b>Saída esperada:</b> Lista com 6 clientes (Carolina Maria de Jesus, Jose Saramago, Toni Morrison, Yuval Noah Harari, Chimamanda Adichie e Jorge Amado).
     */
    @Test
    @DisplayName("Testa busca por número exato de filhos (zero).")
    void testFindClientsByChildrenZero() {
        // Arrange
        Integer children = 0;

        // Act
        List<Client> result = repository.findClientsByChildren(children);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.size());
        assertEquals("Carolina Maria de Jesus", result.stream().filter(c -> c.getName().equals("Carolina Maria de Jesus")).findFirst().orElse(null).getName());
        assertEquals("Jose Saramago", result.stream().filter(c -> c.getName().equals("Jose Saramago")).findFirst().orElse(null).getName());
        assertEquals("Toni Morrison", result.stream().filter(c -> c.getName().equals("Toni Morrison")).findFirst().orElse(null).getName());
        assertEquals("Yuval Noah Harari", result.stream().filter(c -> c.getName().equals("Yuval Noah Harari")).findFirst().orElse(null).getName());
        assertEquals("Chimamanda Adichie", result.stream().filter(c -> c.getName().equals("Chimamanda Adichie")).findFirst().orElse(null).getName());
        assertEquals("Jorge Amado", result.stream().filter(c -> c.getName().equals("Jorge Amado")).findFirst().orElse(null).getName());
    }

    /*
     * <b>Cenário:</b> Busca clientes cujo nome termina com um sufixo diferente (ignorando case).
     * <b>Entrada:</b> suffix = "o".
     * <b>Saída esperada:</b> Lista com 4 clientes (Lázaro Ramos, Jose Saramago, Silvio Almeida e Jorge Amado).
     */
    @Test
    @DisplayName("Testa busca por nome terminado com outro sufixo (ignora case).")
    void testFindByNameEndingWithIgnoreCaseDifferentSuffix() {
        // Arrange
        String suffix = "o";

        // Act
        List<Client> result = repository.findByNameEndingWithIgnoreCase(suffix);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Conceição Evaristo", result.stream().filter(c -> c.getName().equals("Conceição Evaristo")).findFirst().orElse(null).getName());
        assertEquals("Djamila Ribeiro", result.stream().filter(c -> c.getName().equals("Djamila Ribeiro")).findFirst().orElse(null).getName());
        assertEquals("Jose Saramago", result.stream().filter(c -> c.getName().equals("Jose Saramago")).findFirst().orElse(null).getName());
        assertEquals("Jorge Amado", result.stream().filter(c -> c.getName().equals("Jorge Amado")).findFirst().orElse(null).getName());
    }
}