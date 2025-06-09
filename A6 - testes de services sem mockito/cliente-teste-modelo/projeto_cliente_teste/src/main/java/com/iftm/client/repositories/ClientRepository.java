package com.iftm.client.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iftm.client.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // busca clientes por nome (exato)
    // anotação Query é utilizada quando há necessidade de funções específicas do
    // banco, como LOWER
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
    Client findClientByNameIgnoreCase(@Param("name") String name);

    // busca clientes por nome (parcial)
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :word, '%'))")
    List<Client> findClientsByNameContaining(@Param("word") String word);

    // busca clientes por salário maior que um valor
    List<Client> findClientsByIncomeGreaterThan(Double income);

    // busca clientes por salário menor que um valor
    List<Client> findClientsByIncomeLessThan(Double income);

    // busca clientes por salário entre dois valores
    List<Client> findClientsByIncomeBetween(Double minIncome, Double maxIncome);

    // busca clientes por data de nascimento em uma faixa de valores
    List<Client> findClientsByBirthDateBetween(Instant startDate, Instant endDate);

    // busca clientes pelo número de filhos
    List<Client> findClientsByChildren(Integer children);

    @Query("SELECT c FROM Client c WHERE c.cpf = :cpf")
    List<Client> findClientsByCpf(@Param("cpf") String cpf);

    // Métodos abaixo adicionados para o teste de busca

    // busca clientes pelo número de filhos entre dois valores
    List<Client> findByChildrenBetween(Integer minChildren, Integer maxChildren);

    // busca clientes cujo nome termina com uma palavra específica
    List<Client> findByNameEndingWithIgnoreCase(String suffix);

    // busca cientes cujo salario é múltiplo de um valor
    @Query("SELECT c FROM Client c WHERE MOD(c.income, :value) = 0")
    List<Client> findByIncomeMultipleOf(@Param("value") Double value);

    // busca clientes mais jovens que uma idade específica
    @Query("SELECT c FROM Client c WHERE YEAR(CURRENT_DATE) - YEAR(c.birthDate) < :age")
    List<Client> findByAgeLessThan(@Param("age") Integer age);

    // busca cliente com renda acima da média
    @Query("SELECT c FROM Client c WHERE c.income > (SELECT AVG(c2.income) FROM Client c2)")
    List<Client> findByIncomeAboveAverage();

    // busca clientes com data de nascimento em meses específicos
    @Query("SELECT c FROM Client c WHERE EXTRACT(MONTH FROM c.birthDate) IN :months")
    List<Client> findByBirthDateInMonths(@Param("months") List<Integer> months);

    // findByIncome do repository
    Page<Client> findByIncome(Double income, PageRequest pageRequest);

}