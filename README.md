# api-fisio

API Fisio é uma aplicação backend para gestão de empresas que prestam serviço terceirizado de fisioterapia para hospitais.

### Funcionamento da aplicação: 

Em resumo, o fisioterapeuta ao fim do plantão lança no sistema os atendimentos prestados, qual tipo de atendimento e em qual o hospital que foi feito, na data desejada será gerado uma cobrança para cada hospital e valor de pagamento para cada fisioterapeuta. 

### Tecnologias utilizadas: 

Até o momento Java, Spring boot, JPA / Hibernate e MySql.
Foi implementada a classe User com atributos para login e auditoria,  autenticação com Spring Security e JWT token, com perfis ADMIN e FISIO
Documentação com Spring doc e Swagger. Além disso, teste de integração para garantir o funcionamento correto da aplicação.

Ao decorrer do projeto irei implementar as Classes, Fisioterapeuta, Hospital, Paciente, Atendimento, Fatura e Enum TipoAtendimento com preços fixos para cada tipo.
Ao fim do projeto irei iniciar a aplicação em um container Docker.
