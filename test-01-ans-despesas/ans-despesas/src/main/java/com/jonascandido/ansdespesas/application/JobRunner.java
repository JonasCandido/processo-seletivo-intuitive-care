package com.jonascandido.ansdespesas.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {

    private final DespesasJob despesasJob;

    public JobRunner(DespesasJob despesasJob) {
        this.despesasJob = despesasJob;
    }

    @Override
    public void run(String... args) {
        despesasJob.execute();
    }
}
