# vault-example

O Vault é organizado por Secret Engines que podem possuir tipos diferentes.
- KV: armazenar secrets no formato chave:valor.
- Transit: armazenar secrets para criptografar/descriptografar.

Secrets Engines são disponíveis como um caminho no Vault.
Para os usuários, o comportamento delas seria similar a um sistema de arquivos virtual (leitura, escrita e remoção).

Secret Engines possuem ciclo de vida

## Run docker
```
docker-compose up
```

## Adicionar Secret
```
vault kv put secret/github github.oauth2.key=foobar
```