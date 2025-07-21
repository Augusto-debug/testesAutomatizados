# Scripts do Selenium IDE para Testes de Veterinários

Este documento contém os scripts do Selenium IDE para testar as funcionalidades do sistema de gerenciamento de veterinários.

## 1. Teste de Cadastrar Veterinário

### Cenário
Usuário acessa o sistema e cadastra um novo veterinário com dados válidos.

### Script Selenium IDE
```json
{
  "id": "cadastrar-veterinario",
  "version": "2.0",
  "name": "Cadastrar Veterinário",
  "url": "http://localhost:8080",
  "tests": [{
    "id": "test-cadastrar",
    "name": "Cadastrar Veterinário",
    "commands": [{
      "id": "1",
      "comment": "Navegar para página de cadastro",
      "command": "open",
      "target": "/form",
      "targets": [],
      "value": ""
    }, {
      "id": "2",
      "comment": "Verificar se está na página correta",
      "command": "assertText",
      "target": "css=h1",
      "targets": [],
      "value": "Cadastrar novo veterinário"
    }, {
      "id": "3",
      "comment": "Preencher campo nome",
      "command": "type",
      "target": "id=nome",
      "targets": [],
      "value": "Dr. João Silva Teste"
    }, {
      "id": "4",
      "comment": "Verificar valor do campo nome",
      "command": "assertValue",
      "target": "id=nome",
      "targets": [],
      "value": "Dr. João Silva Teste"
    }, {
      "id": "5",
      "comment": "Preencher campo email",
      "command": "type",
      "target": "id=inputEmail",
      "targets": [],
      "value": "joao.silva.teste@veterinaria.com"
    }, {
      "id": "6",
      "comment": "Verificar valor do campo email",
      "command": "assertValue",
      "target": "id=inputEmail",
      "targets": [],
      "value": "joao.silva.teste@veterinaria.com"
    }, {
      "id": "7",
      "comment": "Preencher campo especialidade",
      "command": "type",
      "target": "id=inputEspecialidade",
      "targets": [],
      "value": "Cirurgia Veterinária"
    }, {
      "id": "8",
      "comment": "Verificar valor do campo especialidade",
      "command": "assertValue",
      "target": "id=inputEspecialidade",
      "targets": [],
      "value": "Cirurgia Veterinária"
    }, {
      "id": "9",
      "comment": "Preencher campo salário",
      "command": "type",
      "target": "id=inputSalario",
      "targets": [],
      "value": "5500.00"
    }, {
      "id": "10",
      "comment": "Verificar valor do campo salário",
      "command": "assertValue",
      "target": "id=inputSalario",
      "targets": [],
      "value": "5500.00"
    }, {
      "id": "11",
      "comment": "Clicar no botão cadastrar",
      "command": "click",
      "target": "css=button[type='submit']",
      "targets": [],
      "value": ""
    }, {
      "id": "12",
      "comment": "Verificar redirecionamento para home",
      "command": "assertLocation",
      "target": "http://localhost:8080/home",
      "targets": [],
      "value": ""
    }, {
      "id": "13",
      "comment": "Verificar se veterinário aparece na tabela",
      "command": "assertText",
      "target": "css=table",
      "targets": [],
      "value": "*Dr. João Silva Teste*"
    }]
  }]
}
```

## 2. Teste de Listar Veterinários

### Cenário
Usuário acessa a página inicial para visualizar todos os veterinários cadastrados.

### Script Selenium IDE
```json
{
  "id": "listar-veterinarios",
  "version": "2.0",
  "name": "Listar Veterinários",
  "url": "http://localhost:8080",
  "tests": [{
    "id": "test-listar",
    "name": "Listar Veterinários",
    "commands": [{
      "id": "1",
      "comment": "Navegar para página inicial",
      "command": "open",
      "target": "/home",
      "targets": [],
      "value": ""
    }, {
      "id": "2",
      "comment": "Verificar presença da tabela",
      "command": "assertElementPresent",
      "target": "css=table.table",
      "targets": [],
      "value": ""
    }, {
      "id": "3",
      "comment": "Verificar cabeçalho Nome",
      "command": "assertText",
      "target": "css=th",
      "targets": [],
      "value": "*Nome*"
    }, {
      "id": "4",
      "comment": "Verificar cabeçalho Especialidade",
      "command": "assertText",
      "target": "css=th",
      "targets": [],
      "value": "*Especialidade*"
    }, {
      "id": "5",
      "comment": "Verificar cabeçalho Email",
      "command": "assertText",
      "target": "css=th",
      "targets": [],
      "value": "*Email*"
    }, {
      "id": "6",
      "comment": "Verificar cabeçalho Salário",
      "command": "assertText",
      "target": "css=th",
      "targets": [],
      "value": "*Salario*"
    }, {
      "id": "7",
      "comment": "Verificar botão Adicionar",
      "command": "assertElementPresent",
      "target": "linkText=Adicionar",
      "targets": [],
      "value": ""
    }, {
      "id": "8",
      "comment": "Verificar botão Consultar",
      "command": "assertElementPresent",
      "target": "linkText=Consultar",
      "targets": [],
      "value": ""
    }]
  }]
}
```

## 3. Teste de Pesquisar Veterinário

### Cenário
Usuário realiza busca por veterinário específico usando o nome.

### Script Selenium IDE
```json
{
  "id": "pesquisar-veterinario",
  "version": "2.0",
  "name": "Pesquisar Veterinário",
  "url": "http://localhost:8080",
  "tests": [{
    "id": "test-pesquisar",
    "name": "Pesquisar Veterinário",
    "commands": [{
      "id": "1",
      "comment": "Navegar para página de pesquisa",
      "command": "open",
      "target": "/find",
      "targets": [],
      "value": ""
    }, {
      "id": "2",
      "comment": "Verificar título da página",
      "command": "assertText",
      "target": "css=h1",
      "targets": [],
      "value": "Pesquisar veterinários"
    }, {
      "id": "3",
      "comment": "Preencher campo de pesquisa",
      "command": "type",
      "target": "id=nome",
      "targets": [],
      "value": "João Silva"
    }, {
      "id": "4",
      "comment": "Verificar valor preenchido",
      "command": "assertValue",
      "target": "id=nome",
      "targets": [],
      "value": "João Silva"
    }, {
      "id": "5",
      "comment": "Clicar no botão consultar",
      "command": "click",
      "target": "css=button[type='submit']",
      "targets": [],
      "value": ""
    }, {
      "id": "6",
      "comment": "Verificar redirecionamento",
      "command": "assertLocation",
      "target": "*localhost:8080/home*",
      "targets": [],
      "value": ""
    }, {
      "id": "7",
      "comment": "Verificar resultados da pesquisa",
      "command": "assertText",
      "target": "css=table",
      "targets": [],
      "value": "*João Silva*"
    }]
  }]
}
```

## 4. Teste de Alterar Veterinário

### Cenário
Usuário edita informações de um veterinário existente.

### Script Selenium IDE
```json
{
  "id": "alterar-veterinario",
  "version": "2.0",
  "name": "Alterar Veterinário",
  "url": "http://localhost:8080",
  "tests": [{
    "id": "test-alterar",
    "name": "Alterar Veterinário",
    "commands": [{
      "id": "1",
      "comment": "Navegar para página inicial",
      "command": "open",
      "target": "/home",
      "targets": [],
      "value": ""
    }, {
      "id": "2",
      "comment": "Clicar no botão de editar (primeiro da lista)",
      "command": "click",
      "target": "css=a.btn-warning:first-of-type",
      "targets": [],
      "value": ""
    }, {
      "id": "3",
      "comment": "Verificar se está na página de edição",
      "command": "assertText",
      "target": "css=h1",
      "targets": [],
      "value": "Atualizar informacoes"
    }, {
      "id": "4",
      "comment": "Limpar e alterar nome",
      "command": "clear",
      "target": "id=nome",
      "targets": [],
      "value": ""
    }, {
      "id": "5",
      "comment": "Inserir novo nome",
      "command": "type",
      "target": "id=nome",
      "targets": [],
      "value": "Dr. João Silva Santos"
    }, {
      "id": "6",
      "comment": "Verificar novo valor do nome",
      "command": "assertValue",
      "target": "id=nome",
      "targets": [],
      "value": "Dr. João Silva Santos"
    }, {
      "id": "7",
      "comment": "Limpar e alterar email",
      "command": "clear",
      "target": "id=inputEmail",
      "targets": [],
      "value": ""
    }, {
      "id": "8",
      "comment": "Inserir novo email",
      "command": "type",
      "target": "id=inputEmail",
      "targets": [],
      "value": "joao.santos@veterinaria.com"
    }, {
      "id": "9",
      "comment": "Verificar novo valor do email",
      "command": "assertValue",
      "target": "id=inputEmail",
      "targets": [],
      "value": "joao.santos@veterinaria.com"
    }, {
      "id": "10",
      "comment": "Limpar e alterar especialidade",
      "command": "clear",
      "target": "id=inputEspecialidade",
      "targets": [],
      "value": ""
    }, {
      "id": "11",
      "comment": "Inserir nova especialidade",
      "command": "type",
      "target": "id=inputEspecialidade",
      "targets": [],
      "value": "Clínica Geral"
    }, {
      "id": "12",
      "comment": "Verificar novo valor da especialidade",
      "command": "assertValue",
      "target": "id=inputEspecialidade",
      "targets": [],
      "value": "Clínica Geral"
    }, {
      "id": "13",
      "comment": "Limpar e alterar salário",
      "command": "clear",
      "target": "id=inputSalario",
      "targets": [],
      "value": ""
    }, {
      "id": "14",
      "comment": "Inserir novo salário",
      "command": "type",
      "target": "id=inputSalario",
      "targets": [],
      "value": "6000.00"
    }, {
      "id": "15",
      "comment": "Verificar novo valor do salário",
      "command": "assertValue",
      "target": "id=inputSalario",
      "targets": [],
      "value": "6000.00"
    }, {
      "id": "16",
      "comment": "Clicar no botão atualizar",
      "command": "click",
      "target": "css=button[type='submit']",
      "targets": [],
      "value": ""
    }, {
      "id": "17",
      "comment": "Verificar redirecionamento",
      "command": "assertLocation",
      "target": "http://localhost:8080/home",
      "targets": [],
      "value": ""
    }, {
      "id": "18",
      "comment": "Verificar dados alterados na tabela",
      "command": "assertText",
      "target": "css=table",
      "targets": [],
      "value": "*Dr. João Silva Santos*"
    }]
  }]
}
```

## 5. Teste de Excluir Veterinário

### Cenário
Usuário remove um veterinário do sistema.

### Script Selenium IDE
```json
{
  "id": "excluir-veterinario",
  "version": "2.0",
  "name": "Excluir Veterinário",
  "url": "http://localhost:8080",
  "tests": [{
    "id": "test-excluir",
    "name": "Excluir Veterinário",
    "commands": [{
      "id": "1",
      "comment": "Navegar para página inicial",
      "command": "open",
      "target": "/home",
      "targets": [],
      "value": ""
    }, {
      "id": "2",
      "comment": "Verificar presença do veterinário antes da exclusão",
      "command": "assertText",
      "target": "css=table",
      "targets": [],
      "value": "*Dr. João Silva Santos*"
    }, {
      "id": "3",
      "comment": "Clicar no botão de excluir (primeiro da lista)",
      "command": "click",
      "target": "css=a.btn-danger:first-of-type",
      "targets": [],
      "value": ""
    }, {
      "id": "4",
      "comment": "Verificar redirecionamento após exclusão",
      "command": "assertLocation",
      "target": "http://localhost:8080/home",
      "targets": [],
      "value": ""
    }, {
      "id": "5",
      "comment": "Verificar que veterinário foi removido",
      "command": "assertNotText",
      "target": "css=table",
      "targets": [],
      "value": "Dr. João Silva Santos"
    }, {
      "id": "6",
      "comment": "Verificar que tabela ainda existe",
      "command": "assertElementPresent",
      "target": "css=table.table",
      "targets": [],
      "value": ""
    }]
  }]
}
```

## Como Usar os Scripts

### 1. Instalação do Selenium IDE
1. Instale a extensão Selenium IDE no seu navegador (Chrome/Firefox)
2. Acesse: https://www.selenium.dev/selenium-ide/

### 2. Importação dos Scripts
1. Abra o Selenium IDE
2. Clique em "Open an existing project"
3. Cole cada script JSON em um arquivo .side
4. Importe no Selenium IDE

### 3. Execução dos Testes
1. Certifique-se de que a aplicação está rodando em `http://localhost:8080`
2. Execute os testes na seguinte ordem:
   - Cadastrar Veterinário
   - Listar Veterinários
   - Pesquisar Veterinário
   - Alterar Veterinário
   - Excluir Veterinário

### 4. Verificações Importantes
- **assert value**: Utilizado para verificar valores em formulários
- **assert text**: Utilizado para verificar conteúdo de tabelas e labels
- **assertElementPresent**: Verifica presença de elementos na página
- **assertLocation**: Verifica URL atual da página

## Observações
- Os testes devem ser executados com a aplicação rodando
- Certifique-se de que o banco H2 está limpo antes de executar a sequência completa
- Os dados de teste podem ser alterados conforme necessário
- Para ambiente de produção, ajuste a URL base nos scripts 