# AstroQuestions

Sistema de perguntas e respostas para facilitar o trabalho da Staff, organizado por menus e salvo em banco de dados (**MySQL**). Recomendado para qualquer modo de jogo.

# Download

Você pode encontrar uma JAR para download [aqui](https://github.com/GabrielBS-21/AstroQuestions/releases).

## Comandos

| Comando | Descrição | Permissão |
|----------------|-------------------------------|-----------------------------|
|/duvida < pergunta > |Envie uma pergunta para um membro da staff            |Sem permissão
|/duvida listar |Mostra as perguntas feitas por você, podendo editar ou excluí-las.           |Sem permissão            |
|/duvidas |Lista todas as perguntas feitas por outros jogadores, podendo responde-las.|astroquestions.staff|


## Configuração

O plugin conta com um arquivo de configuração ``config.yml`` com mensagens configuráveis. 
*Observação: não são todas as mensagens que são configuráveis.*

```yaml
connection:  
  mysql:  
    address: 'localhost:3306'  
    username: 'root'  
    password: ''  
    database: 'test'  
  
question:  
  inventory:  
    title: 'Minhas Dúvidas'  
    lines: 6  
    item:  
      name: '&7ID: &f#@id'  
      material: PAPER  
      data: 0  
      lore: # lore de uma pergunta ainda não respondida  
        - ''
        - '&7Dúvida: &f@question'  
        - '&7Criada em: &f@createdAt'  
        - '&7Situação: &f@situation'  
      lore-replied: # linhas que serão adicionadas caso a pergunta tenha sido respondida  
        - ''  
        - '&7Respondida por: &f@staff'  
        - '&7Respondida em: &f@repliedAt'  
        - '&7Resposta: &f@reply'  
        - ''  
  staff-inventory:  
    title: 'Lista de Dúvidas'  
    lines: 6  
    item:  
      name: '&aDúvida de @player'  
      material: PAPER  
      data: 0  
      lore:  
        - ''  
        - '&7Dúvida: &f@question'  
        - '&7Criada em: &f@createdAt'  
        - '&7Situação: &f@situation'  
      lore-replied:  
        - ''  
        - '&7Respondida por: &f@staff'  
        - '&7Respondida em: &f@repliedAt'  
        - '&7Resposta: &f@reply'  
        - ''  
  new-question: # Mensagem que será enviada para todos os jogadores que tiverem permissão (astroquestions.staff)  
    - ''  
    - '&aUm nova dúvida está disponível para ser respondida!'  
    - '&aClique aqui para abrir o menu de dúvidas.'  
    - ''  
  question-replied:  
    - ''  
    - '&aSua pergunta &7(#@id) foi respondida por &f@staff&a!'  
    - ''  
    - '&aResposta:'  
    - '&7  @reply'  
  
messages:  
  error: '&cOcorreu um erro durante a execução deste comando.'  
  no-permission: '&cVocê não tem permissão para utilizar este comando.'  
  invalid-target: '&cEste comando apenas pode ser executado por jogadores.'  
  invalid-usage: '&cUso incorreto, utilize /duvida <dúvida>'  
  already-replied: '&cEsta dúvida já foi respondida.'  
  reply-sent: '&aA sua resposta foi enviada com sucesso.'  
  text-edited: '&aO texto da pergunta foi editada com sucesso.'  
  question-sent: '&aA sua pergunta foi enviada com sucesso! Aguarde até uma resposta.'
  ``` 

## Dependências

O plugin não precisa de nenhum outro plugin como dependência, apenas de algumas APIs, que serão baixadas durante o carregamento.

## Tecnologias usadas

- [HikariCP](https://github.com/brettwooldridge/HikariCP) - Um gerenciador de alta performance para conexões JDBC pool.

- [Google Guice](https://github.com/google/guice) - Fornece suporte para injeção de dependência usando anotações.

**APIs e Frameworks**

- [command-framework](https://github.com/SaiintBrisson/command-framework) - Framework que facilita a criação de comandos.

- [inventory-api](https://github.com/HenryFabio/inventory-api) - API que facilita a criação e o gerenciamento de inventários.
