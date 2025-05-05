package com.augusto.atividadea3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTerceirizadoTest {

    // @author Ruan Neres
    @Test
    public void testarConstrutorEntradasValida() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("João", 30, 60.72, 500.0);
        assertEquals("João", f.getNome());
        assertEquals(30, f.getHorasTrabalhadas());
        assertEquals(60.72, f.getValorHora(), 0.01);
        assertEquals(500.0, f.getDespesaAdicional(), 0.01);
    }

    // @author Augusto Cesar
    @Test
    public void testarConstrutorEntradaDespesasInvalida() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioTerceirizado("Maria", 30, 60.72, 1001.0);
        });
        assertEquals("O valor das despesas adicionais não pode ultrapassar R$ 1000.00.", exception.getMessage());
    }

    // @author André Luiz Vicente
    @Test
    public void testarModificarDespesasEntradaValida() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Carlos", 30, 60.72, 500.0);
        f.setDespesaAdicional(800.0);
        assertEquals(800.0, f.getDespesaAdicional(), 0.01);
    }

    // @author Victor Hugo
    @Test
    public void testarModificarDespesasEntradaInvalida() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Ana", 30, 60.72, 500.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            f.setDespesaAdicional(1001.0);
        });
        assertEquals("O valor das despesas adicionais não pode ultrapassar R$ 1000.00.", exception.getMessage());
    }

    // @author Artur Machado Soares
    @Test
    public void testarCalcularPagamentoComBonus() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Luana", 30, 60.72, 500.0);
        double pagamentoEsperado = 30 * 60.72 + (500.0 * 1.10);
        assertEquals(pagamentoEsperado, f.calcularPagamento(), 0.01);
    }

    // @author Augusto Cesar
    @Test
    public void testarModificarDespesasPagamentoInvalido() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Bruno", 30, 60.72, 500.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            f.setDespesaAdicional(900.0);
        });
        assertEquals(900.0, f.getDespesaAdicional(), 0.01); // ainda é válido

        // Agora alterando para um valor que gere pagamento abaixo do mínimo
        f.setHorasTrabalhadas(20);
        f.setValorHora(75.0); // 20 * 75 = 1500 < 1518
        exception = assertThrows(IllegalArgumentException.class, () -> {
            f.setDespesaAdicional(10.0); // mesmo pequeno, total ainda é menor
        });
        assertTrue(exception.getMessage().contains("menor que o salário mínimo"));
    }

    // @author Ruan Neres
    @Test
    public void testarConstrutorDespesasPagamentoExcedente() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioTerceirizado("Roberto", 40, 2500.0, 1000.0);
        });
        assertTrue(exception.getMessage().contains("ultrapassar R$ 100.000.00"));
    }

    // @author André Luiz Vicente
    @Test
    public void testarModificarDespesasPagamentoExcedente() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Diego", 40, 2400.0, 100.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            f.setDespesaAdicional(900.0);
        });
        assertTrue(exception.getMessage().contains("ultrapassar R$ 100.000.00"));
    }

    // @author Victor Hugo
    @Test
    public void testarConstrutorDespesasPagamentoMinimo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioTerceirizado("Marcos", 20, 75.0, 0.0);
        });
        assertTrue(exception.getMessage().contains("menor que o salário mínimo"));
    }

    // @author Artur Machado Soares
    @Test
    public void testarModificarHorasDespesasPagamentoMinimo() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Felipe", 30, 60.72, 500.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            f.setHorasTrabalhadas(20);
        });
        assertTrue(exception.getMessage().contains("menor que o salário mínimo"));
    }
}