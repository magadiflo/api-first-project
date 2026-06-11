package dev.magadiflo.accounts.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@EnableR2dbcAuditing
@SpringBootApplication
public class MsAccountsWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAccountsWebfluxApplication.class, args);
    }

}
