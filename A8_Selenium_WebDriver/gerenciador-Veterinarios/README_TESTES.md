# Testes Automatizados com Selenium - Gerenciador de Veterinários

Este projeto implementa testes funcionais automatizados usando Selenium IDE e Selenium WebDriver para o sistema de gerenciamento de veterinários.

## 📋 Funcionalidades Testadas

### Parte 1 - Selenium IDE
- ✅ Cadastrar Veterinário
- ✅ Pesquisar Veterinário 
- ✅ Excluir Veterinário
- ✅ Alterar Veterinário
- ✅ Listar Veterinário

### Parte 2 - Selenium WebDriver
- ✅ Cadastrar Veterinário
- ✅ Pesquisar Veterinário
- ✅ Excluir Veterinário
- ✅ Alterar Veterinário
- ✅ Listar Veterinário

## 🛠️ Configuração do Ambiente

### Pré-requisitos
- Java 8 ou superior
- Maven 3.6+
- Chrome Browser
- Selenium IDE (extensão do navegador)

### Dependências Adicionadas
```xml
<!-- Selenium WebDriver Dependencies -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-chrome-driver</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.3.0</version>
    <scope>test</scope>
</dependency>
```

## 🚀 Como Executar os Testes

### 1. Executar a Aplicação
```bash
cd gerenciador-Veterinarios
./mvnw spring-boot:run
```
A aplicação estará disponível em: `http://localhost:8080`

### 2. Executar Testes WebDriver
```bash
# Executar todos os testes
./mvnw test

# Executar apenas os testes Selenium
./mvnw test -Dtest=VeterinarioSeleniumTest
```

### 3. Executar Testes Selenium IDE
1. Instale a extensão Selenium IDE no Chrome/Firefox
2. Abra os scripts do arquivo `SELENIUM_IDE_SCRIPTS.md`
3. Importe cada script no Selenium IDE
4. Execute os testes na ordem indicada

## 📁 Estrutura dos Testes

```
src/test/java/org/iftm/gerenciadorveterinarios/
├── BaseSeleniumTest.java          # Classe base para configuração
├── VeterinarioSeleniumTest.java   # Testes WebDriver completos
└── ...

docs/
├── SELENIUM_IDE_SCRIPTS.md        # Scripts para Selenium IDE
└── README_TESTES.md              # Este arquivo
```

## 🧪 Descrição dos Testes

### Teste de Cadastrar Veterinário
**Cenário**: Usuário cadastra um novo veterinário com dados válidos
- **Ações**: Preenche formulário com nome, email, especialidade e salário
- **Verificações**: 
  - Assert value nos campos do formulário
  - Assert text na tabela após cadastro
  - Redirecionamento correto

### Teste de Listar Veterinários
**Cenário**: Usuário visualiza todos os veterinários cadastrados
- **Ações**: Acessa página inicial
- **Verificações**:
  - Presença da tabela
  - Assert text nos cabeçalhos
  - Botões de navegação

### Teste de Pesquisar Veterinário
**Cenário**: Usuário busca veterinário específico por nome
- **Ações**: Preenche campo de busca e submete
- **Verificações**:
  - Assert value no campo de pesquisa
  - Assert text nos resultados
  - Teste de busca vazia

### Teste de Alterar Veterinário
**Cenário**: Usuário edita dados de um veterinário existente
- **Ações**: Clica em editar, altera campos e salva
- **Verificações**:
  - Assert value nos campos alterados
  - Assert text na tabela com novos dados
  - Dados antigos não aparecem mais

### Teste de Excluir Veterinário
**Cenário**: Usuário remove um veterinário do sistema
- **Ações**: Clica no botão excluir
- **Verificações**:
  - Assert text que o veterinário não aparece mais
  - Tabela ainda existe
  - Redirecionamento correto

## 📊 Tipos de Verificações Utilizadas

### Assert Value
Utilizado para verificar valores presentes em formulários:
```java
assertEquals(NOME_TESTE, nomeField.getAttribute("value"));
```

### Assert Text
Utilizado para verificar conteúdo de tabelas e labels:
```java
assertTrue(driver.getPageSource().contains(NOME_TESTE));
```

### Assert Element Present
Verifica presença de elementos na página:
```java
WebElement tabela = driver.findElement(By.className("table"));
assertNotNull(tabela);
```

## 🔧 Configurações dos Testes

### WebDriver Configuração
- **Modo Headless**: Habilitado para CI/CD
- **Timeout**: 10 segundos para wait
- **Resolução**: 1920x1080
- **WebDriverManager**: Gerenciamento automático do ChromeDriver

### Ordem de Execução
Os testes são executados em ordem específica usando `@Order`:
1. Cadastrar (cria dados)
2. Listar (verifica dados)
3. Pesquisar (busca dados)
4. Alterar (modifica dados)
5. Excluir (remove dados)

## 🐛 Solução de Problemas

### ChromeDriver não encontrado
```bash
# O WebDriverManager resolve automaticamente
# Certifique-se de ter conexão com internet na primeira execução
```

### Aplicação não está rodando
```bash
# Verifique se a aplicação está ativa em localhost:8080
curl http://localhost:8080/home
```

### Testes falhando
```bash
# Execute os testes individualmente para debug
./mvnw test -Dtest=VeterinarioSeleniumTest#testCadastrarVeterinario
```

## 📝 Observações Importantes

1. **Ordem dos Testes**: Execute na sequência definida, pois alguns testes dependem de dados criados anteriormente

2. **Ambiente Limpo**: Para melhores resultados, execute com banco H2 limpo

3. **Timeout**: Se os testes falharem por timeout, aumente o valor em `BaseSeleniumTest.java`

4. **Dados de Teste**: Os valores podem ser alterados nas constantes da classe de teste

5. **Selenium IDE**: Scripts fornecidos são compatíveis com Selenium IDE 3.x+

## 🏆 Resultados Esperados

Todos os testes devem passar com sucesso, demonstrando:
- ✅ Funcionalidade completa de CRUD
- ✅ Validação de formulários 
- ✅ Navegação entre páginas
- ✅ Persistência de dados
- ✅ Interface responsiva

## 📧 Entrega

Para entrega, inclua:
1. ✅ Repositório GitHub com código dos testes
2. ✅ Scripts do Selenium IDE (arquivo SELENIUM_IDE_SCRIPTS.md)
3. ✅ Documentação completa
4. ✅ Testes WebDriver funcionais
5. ✅ Evidências de execução (opcional: screenshots/vídeos) 