package com.happio.application;

import org.springframework.stereotype.Service;
import org.flywaydb.core.Flyway;


@Service
public class MigrationService {
    private Flyway flyway;

    public MigrationService (Flyway flyway) {
        this.flyway = flyway;
    }

    public void run(){
        createInitialTables();
    }

    private void createInitialTables(){
        flyway.migrate();
    }
}
