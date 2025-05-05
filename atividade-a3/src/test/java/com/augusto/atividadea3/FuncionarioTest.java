package com.augusto.atividadea3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTest {
    private static final double SALARIO_MINIMO = 1518.00;

    // @author Ruan Neres
    @Test
    public void testarConstrutorEntradasValida() {
        Funcionario funcionario = new Funcionario("João", 30, 60.72);
        assertEquals("João", funcionario.getNome());
        assertEquals(30, funcionario.getHorasTrabalhadas());
        assertEquals(60.72, funcionario.getValorHora(), 0.01);
    }

    // @author Augusto Cesar
    @Test
    public void testarConstrutorEntradaHorasInvalida() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("Maria", 19, 60.72);
        });
        assertEquals("O número de horas trabalhadas deve estar entre 20 e 40.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("José", 41, 60.72);
        });
        assertEquals("O número de horas trabalhadas deve estar entre 20 e 40.", exception.getMessage());
    }

    // @author André Luiz Vicente
    @Test
    public void testarConstrutorEntradaValorHoraInvalida() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("Ana", 30, 60.00);
        });
        assertEquals("O valor por hora deve estar entre 4% e 10% do salário mínimo.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("Carlos", 30, 151.81);
        });
        assertEquals("O valor por hora deve estar entre 4% e 10% do salário mínimo.", exception.getMessage());
    }

    // @author Victor Hugo
    @Test
    public void testarConstrutorPagamentoInvalido() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("Pedro", 20, 75.0);
        });
        assertTrue(exception.getMessage().contains("menor que o salário mínimo"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("Lucas", 40, 3000.0);
        });
        assertTrue(exception.getMessage().contains("ultrapassar R$ 100.000.00"));
    }

    // @author Artur Machado Soares
    @Test
    public void testarModificarHorasEntradaValida() {
        Funcionario funcionario = new Funcionario("Marta", 30, 60.72);
        funcionario.setHorasTrabalhadas(25);
        assertEquals(25, funcionario.getHorasTrabalhadas());
    }

    // @author Ruan Neres
    @Test
    public void testarModificarHorasEntradaInvalida() {
        Funcionario funcionario = new Funcionario("Rafael", 30, 60.72);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setHorasTrabalhadas(19);
        });
        assertEquals("O número de horas trabalhadas deve estar entre 20 e 40.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setHorasTrabalhadas(41);
        });
        assertEquals("O número de horas trabalhadas deve estar entre 20 e 40.", exception.getMessage());
    }

    // @author Augusto Cesar
    @Test
    public void testarModificarHorasPagamentoInvalido() {
        Funcionario funcionario = new Funcionario("Paulo", 30, 60.72);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setHorasTrabalhadas(20);
        });
        assertTrue(exception.getMessage().contains("menor que o salário mínimo"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setHorasTrabalhadas(40);
        });
        assertTrue(exception.getMessage().contains("menor que o salário mínimo"));
    }

    // @author André Luiz Vicente
    @Test
    public void testarModificarValorEntradaValida() {
        Funcionario funcionario = new Funcionario("Tereza", 30, 60.72);
        funcionario.setValorHora(70.0);
        assertEquals(70.0, funcionario.getValorHora(), 0.01);
    }

    // @author Victor Hugo
    @Test
    public void testarModificarValorEntradaInvalida() {
        Funcionario funcionario = new Funcionario("Fernanda", 30, 60.72);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setValorHora(60.0);
        });
        assertEquals("O valor por hora deve estar entre 4% e 10% do salário mínimo.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setValorHora(151.81);
        });
        assertEquals("O valor por hora deve estar entre 4% e 10% do salário mínimo.", exception.getMessage());
    }

    // @author Artur Machado Soares
    @Test
    public void testarModificarValorPagamentoInvalido() {
        Funcionario funcionario = new Funcionario("Eduardo", 30, 60.72);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setValorHora(50.0);
        });
        assertTrue(exception.getMessage().contains("menor que o salário mínimo"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            funcionario.setValorHora(3000.0);
        });
        assertTrue(exception.getMessage().contains("ultrapassar R$ 100.000.00"));
    }
}