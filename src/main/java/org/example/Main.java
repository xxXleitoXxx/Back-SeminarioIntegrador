package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
       System.out.println("  ______             _        _____                _");
       System.out.println(" |  ____|           | |      / ____|              | |");
       System.out.println(" | |__ _ __ ___  ___| |_ ___| |     _ __ __ _  ___| | __");
       System.out.println(" |  __| '__/ _ \\/ __| __/ _ \\ |    | '__/ _` |/ __| |/ /");
       System.out.println(" | |  | | |  __/ (__| ||  __/ |____| | | (_| | (__|   <");
       System.out.println(" |_|  |_|  \\___|\\___|\\__\\___|\\_____|_|  \\__,_|\\___|_|\\_\\");
    }
}