# Simulador de contas e eventos de contas bancárias

## Como rodar o projeto Java
### Pré-requisitos
* Java 21
* Git

**Windows (PowerShell):**
`.\mvnw spring-boot:run`


### 2. Como executar os testes
`.\mvnw clean test`

## Decisões principais:
* Thread Safety: Uso do ConcurrentHashMap para gerenciar contas em memória, e métodos com syncrhronyzed par agarantir atomicidade de operações compostas.
* Separação de responsabilidades: As controllers são estritamente para rotear o HTTP, fazer o parsing dos payloads e mapear os status codes. A Lógica do negócio é exclusivamente responsabilidade do serviço.