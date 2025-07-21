package org.iftm.gerenciadorveterinarios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VeterinarioSeleniumTest extends BaseSeleniumTest {

    private static final String NOME_TESTE = "Dr. João Silva Teste";
    private static final String EMAIL_TESTE = "joao.silva.teste@veterinaria.com";
    private static final String ESPECIALIDADE_TESTE = "Cirurgia Veterinária";
    private static final String SALARIO_TESTE = "5500.00";
    
    private static final String NOME_ALTERADO = "Dr. João Silva Santos";
    private static final String EMAIL_ALTERADO = "joao.santos@veterinaria.com";
    private static final String ESPECIALIDADE_ALTERADA = "Clínica Geral";
    private static final String SALARIO_ALTERADO = "6000.00";

    @Test
    @Order(1)
    public void testCadastrarVeterinario() {
        // Cenário: Usuário deseja cadastrar um novo veterinário
        navegarParaCadastro();
        
        // Verificar se está na página correta
        wait.until(ExpectedConditions.titleContains("Cadastrar"));
        assertTrue(driver.getPageSource().contains("Cadastrar novo veterinário"));
        
        // Preencher formulário
        WebElement nomeField = driver.findElement(By.id("nome"));
        WebElement emailField = driver.findElement(By.id("inputEmail"));
        WebElement especialidadeField = driver.findElement(By.id("inputEspecialidade"));
        WebElement salarioField = driver.findElement(By.id("inputSalario"));
        
        nomeField.clear();
        nomeField.sendKeys(NOME_TESTE);
        
        emailField.clear();
        emailField.sendKeys(EMAIL_TESTE);
        
        especialidadeField.clear();
        especialidadeField.sendKeys(ESPECIALIDADE_TESTE);
        
        salarioField.clear();
        salarioField.sendKeys(SALARIO_TESTE);
        
        // Verificar valores preenchidos (assert value)
        assertEquals(NOME_TESTE, nomeField.getAttribute("value"));
        assertEquals(EMAIL_TESTE, emailField.getAttribute("value"));
        assertEquals(ESPECIALIDADE_TESTE, especialidadeField.getAttribute("value"));
        assertEquals(SALARIO_TESTE, salarioField.getAttribute("value"));
        
        // Submeter formulário
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
        
        // Verificar redirecionamento para home
        wait.until(ExpectedConditions.urlContains("/home"));
        assertTrue(driver.getCurrentUrl().contains("/home"));
        
        // Verificar se o veterinário foi adicionado na tabela (assert text)
        assertTrue(driver.getPageSource().contains(NOME_TESTE));
        assertTrue(driver.getPageSource().contains(EMAIL_TESTE));
        assertTrue(driver.getPageSource().contains(ESPECIALIDADE_TESTE));
        assertTrue(driver.getPageSource().contains("R$" + SALARIO_TESTE));
    }

    @Test
    @Order(2)
    public void testListarVeterinarios() {
        // Cenário: Usuário deseja visualizar todos os veterinários cadastrados
        navegarParaHome();
        
        // Verificar se está na página correta
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
        
        // Verificar se existe uma tabela de veterinários
        WebElement tabela = driver.findElement(By.className("table"));
        assertNotNull(tabela);
        
        // Verificar cabeçalhos da tabela (assert text)
        assertTrue(driver.getPageSource().contains("Nome"));
        assertTrue(driver.getPageSource().contains("Especialidade"));
        assertTrue(driver.getPageSource().contains("Email"));
        assertTrue(driver.getPageSource().contains("Salario"));
        
        // Verificar se o veterinário cadastrado no teste anterior aparece na lista
        assertTrue(driver.getPageSource().contains(NOME_TESTE));
        
        // Verificar se existe botão de adicionar
        WebElement botaoAdicionar = driver.findElement(By.linkText("Adicionar"));
        assertNotNull(botaoAdicionar);
        
        // Verificar se existe botão de consultar
        WebElement botaoConsultar = driver.findElement(By.linkText("Consultar"));
        assertNotNull(botaoConsultar);
    }

    @Test
    @Order(3)
    public void testPesquisarVeterinario() {
        // Cenário: Usuário deseja pesquisar um veterinário específico
        navegarParaPesquisa();
        
        // Verificar se está na página correta
        wait.until(ExpectedConditions.titleContains("Pesquisar"));
        assertTrue(driver.getPageSource().contains("Pesquisar veterinários"));
        
        // Preencher campo de pesquisa
        WebElement nomeField = driver.findElement(By.id("nome"));
        nomeField.clear();
        nomeField.sendKeys("João Silva");
        
        // Verificar valor preenchido (assert value)
        assertEquals("João Silva", nomeField.getAttribute("value"));
        
        // Submeter pesquisa
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
        
        // Verificar redirecionamento para home com parâmetro de pesquisa
        wait.until(ExpectedConditions.urlContains("/home"));
        
        // Verificar se os resultados da pesquisa são exibidos (assert text)
        assertTrue(driver.getPageSource().contains(NOME_TESTE));
        assertTrue(driver.getPageSource().contains(EMAIL_TESTE));
        
        // Testar pesquisa que não retorna resultados
        navegarParaPesquisa();
        nomeField = driver.findElement(By.id("nome"));
        nomeField.clear();
        nomeField.sendKeys("Nome Inexistente");
        
        submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
        
        wait.until(ExpectedConditions.urlContains("/home"));
        
        // Verificar que o veterinário de teste não aparece nos resultados
        assertFalse(driver.getPageSource().contains(NOME_TESTE));
    }

    @Test
    @Order(4)
    public void testAlterarVeterinario() {
        // Cenário: Usuário deseja alterar dados de um veterinário existente
        navegarParaHome();
        
        // Aguardar carregamento da página
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table")));
        
        // Encontrar o veterinário de teste e clicar no botão de editar
        WebElement botaoEditar = driver.findElement(By.xpath("//a[contains(@href, '/form/')]"));
        botaoEditar.click();
        
        // Verificar se está na página de edição
        wait.until(ExpectedConditions.titleContains("Atualizar"));
        assertTrue(driver.getPageSource().contains("Atualizar informacoes"));
        
        // Verificar se os campos estão preenchidos com os valores atuais (assert value)
        WebElement nomeField = driver.findElement(By.id("nome"));
        WebElement emailField = driver.findElement(By.id("inputEmail"));
        WebElement especialidadeField = driver.findElement(By.id("inputEspecialidade"));
        WebElement salarioField = driver.findElement(By.id("inputSalario"));
        
        // Alterar os valores
        nomeField.clear();
        nomeField.sendKeys(NOME_ALTERADO);
        
        emailField.clear();
        emailField.sendKeys(EMAIL_ALTERADO);
        
        especialidadeField.clear();
        especialidadeField.sendKeys(ESPECIALIDADE_ALTERADA);
        
        salarioField.clear();
        salarioField.sendKeys(SALARIO_ALTERADO);
        
        // Verificar novos valores preenchidos (assert value)
        assertEquals(NOME_ALTERADO, nomeField.getAttribute("value"));
        assertEquals(EMAIL_ALTERADO, emailField.getAttribute("value"));
        assertEquals(ESPECIALIDADE_ALTERADA, especialidadeField.getAttribute("value"));
        assertEquals(SALARIO_ALTERADO, salarioField.getAttribute("value"));
        
        // Submeter alterações
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
        
        // Verificar redirecionamento para home
        wait.until(ExpectedConditions.urlContains("/home"));
        
        // Verificar se os dados foram alterados na tabela (assert text)
        assertTrue(driver.getPageSource().contains(NOME_ALTERADO));
        assertTrue(driver.getPageSource().contains(EMAIL_ALTERADO));
        assertTrue(driver.getPageSource().contains(ESPECIALIDADE_ALTERADA));
        assertTrue(driver.getPageSource().contains("R$" + SALARIO_ALTERADO));
        
        // Verificar que os dados antigos não aparecem mais
        assertFalse(driver.getPageSource().contains(NOME_TESTE));
        assertFalse(driver.getPageSource().contains(EMAIL_TESTE));
    }

    @Test
    @Order(5)
    public void testExcluirVeterinario() {
        // Cenário: Usuário deseja excluir um veterinário existente
        navegarParaHome();
        
        // Aguardar carregamento da página
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table")));
        
        // Verificar que o veterinário alterado está presente antes da exclusão
        assertTrue(driver.getPageSource().contains(NOME_ALTERADO));
        
        // Encontrar e clicar no botão de excluir
        WebElement botaoExcluir = driver.findElement(By.xpath("//a[contains(@href, '/delete/')]"));
        botaoExcluir.click();
        
        // Verificar redirecionamento para home após exclusão
        wait.until(ExpectedConditions.urlContains("/home"));
        
        // Verificar que o veterinário foi removido da tabela (assert text)
        assertFalse(driver.getPageSource().contains(NOME_ALTERADO));
        assertFalse(driver.getPageSource().contains(EMAIL_ALTERADO));
        assertFalse(driver.getPageSource().contains(ESPECIALIDADE_ALTERADA));
        
        // Verificar que a tabela ainda está presente (pode estar vazia)
        WebElement tabela = driver.findElement(By.className("table"));
        assertNotNull(tabela);
    }
} 