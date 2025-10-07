package com.gmarussi.backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sistemaTarefasAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Tarefas - API REST")
                        .description("API de gerenciamento de tarefas e projetos para o teste técnico Java + Angular.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gabriel Marussi")
                                .url("https://github.com/gmarussi")
                                .email("gmarussi@gmail.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local de desenvolvimento")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório no GitHub")
                        .url("https://github.com/gmarussi/sistema_tarefas_vaga"));
    }
}
