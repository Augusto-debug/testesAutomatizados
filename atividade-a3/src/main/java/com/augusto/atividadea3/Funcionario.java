package com.augusto.atividadea3;

public class Funcionario {

    private String nome;
    private int horasTrabalhadas;
    private double valorHora;

    private static final double SALARIO_MINIMO = 1518.00;

    public Funcionario(String nome, int horasTrabalhadas, double valorHora) {
        this.nome = nome;
        this.horasTrabalhadas = validaHorasTrabalhadas(horasTrabalhadas);
        this.valorHora = validaValorHora(valorHora);
        validaPagamento(this.horasTrabalhadas * this.valorHora);
    }

    public String getNome() {
        return nome;
    }

    public int getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(int horasTrabalhadas) {
        this.horasTrabalhadas = validaHorasTrabalhadas(horasTrabalhadas);
        validaPagamento(this.horasTrabalhadas * this.valorHora);
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) {
        this.valorHora = validaValorHora(valorHora);
        validaPagamento(this.horasTrabalhadas * this.valorHora);
    }

    public double calcularPagamento() {
        return horasTrabalhadas * valorHora;
    }

    private int validaHorasTrabalhadas(int horasTrabalhadas) {
        if (horasTrabalhadas < 20 || horasTrabalhadas > 40) {
            throw new IllegalArgumentException("O número de horas trabalhadas deve estar entre 20 e 40.");
        }
        return horasTrabalhadas;
    }

    private double validaValorHora(double valorHora) {
        double minimo = SALARIO_MINIMO * 0.04;
        double maximo = SALARIO_MINIMO * 0.10;
        if (valorHora < minimo || valorHora > maximo) {
            throw new IllegalArgumentException("O valor por hora deve estar entre 4% e 10% do salário mínimo.");
        }
        return valorHora;
    }

    private void validaPagamento(double pagamento) {
        if (pagamento < SALARIO_MINIMO) {
            throw new IllegalArgumentException("O pagamento deve ser maior ou igual ao salário mínimo.");
        }
        if (pagamento > 100000.00) {
            throw new IllegalArgumentException("O pagamento não pode ultrapassar R$ 100.000.00.");
        }
    }
}