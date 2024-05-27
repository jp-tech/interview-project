package com.happio.application;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSetupRunner implements ApplicationRunner {
    private MigrationService migrationService;

    public ApplicationSetupRunner(MigrationService migrationService){
        this.migrationService = migrationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        migrationService.run();
    }
}