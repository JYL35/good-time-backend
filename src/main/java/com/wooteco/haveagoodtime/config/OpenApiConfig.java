package com.wooteco.haveagoodtime.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SESSION_AUTH = "sessionAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Have A Good Time API")
                        .description("모임 생성, 조회, 수정, 참여 관리를 위한 API 문서입니다.")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(SESSION_AUTH))
                .schemaRequirement(SESSION_AUTH, new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE)
                        .name("JSESSIONID")
                        .description("GitHub OAuth2 로그인 후 발급되는 세션 쿠키입니다."));
    }
}
