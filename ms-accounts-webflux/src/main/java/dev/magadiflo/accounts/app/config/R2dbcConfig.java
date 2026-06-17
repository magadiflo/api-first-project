package dev.magadiflo.accounts.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Configuration
@EnableR2dbcAuditing(dateTimeProviderRef = "microsecondDateTimeProvider")
public class R2dbcConfig {
    @Bean
    public DateTimeProvider microsecondDateTimeProvider() {
        // Trunca el tiempo actual a microsegundos para alinearse perfectamente con PostgreSQL
        return () -> Optional.of(Instant.now().truncatedTo(ChronoUnit.MICROS));
    }
}
