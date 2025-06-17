package com.iftm.client.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Busca clientes por renda exata
    Page<Client> findByIncome(Double income, Pageable pageable);
    
    // Busca clientes com renda maior que o valor informado
    Page<Client> findByIncomeGreaterThan(Double income, Pageable pageable);
    
    // Busca clientes por CPF usando LIKE
    @Query("SELECT c FROM Client c WHERE c.cpf LIKE %:cpf%")
    Page<Client> findByCpfLike(@Param("cpf") String cpf, Pageable pageable);
}
