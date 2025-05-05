package com.augusto.atividadea3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTerceirizadoTest {

    @Test
    public void testarConstrutorEntradasValida() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("João", 30, 60.72, 500.0);
        assertEquals("João", f.getNome());
        assertEquals(30, f.getHorasTrabalhadas());
        assertEquals(60.72, f.getValorHora(), 0.01);
        assertEquals(500.0, f.getDespesaAdicional(), 0.01);
    }

    @Test
    public void testarConstrutorEntradaDespesasInvalida() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FuncionarioTerceirizado("Maria", 30, 60.72, 1001.0);
        });
        assertEquals("O valor das despesas adicionais não pode ultrapassar R$ 1000.00.", exception.getMessage());
    }

    @Test
    public void testarModificarDespesasEntradaValida() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Carlos", 30, 60.72, 500.0);
        f.setDespesaAdicional(800.0);
        assertEquals(800.0, f.getDespesaAdicional(), 0.01);
    }

    @Test
    public void testarModificarDespesasEntradaInvalida() {
        FuncionarioTerceirizado f = new FuncionarioTerceirizado("Ana", 30, 60.72, 500.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            f.setDespesaAdicional(1001.0);
        });
        assertEquals("O valor das despesas adicionais não pode ultrapassar R$ 1000.00.", exception.getMessage());
    }
}