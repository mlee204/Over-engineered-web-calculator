package com.qub.customproxyrouter.operator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OperatorConfig {
    // Add initial operators - you can add, update and remove operators via API
    @Bean
    CommandLineRunner commandLineRunner(OperatorRepository repository) {
        // If table already populated skip
        if (repository.count() != 0) {
            return null;
        }
        return args -> {
            Operator add = new Operator(
                    "add",
                    "http://add.php.webcalc.40151615.qpc.hal.davecutting.uk"
            );

            Operator subtract = new Operator(
                    "subtract",
                    "http://subtract.nodejs.webcalc.40151615.qpc.hal.davecutting.uk"
            );

            Operator multiply = new Operator(
                    "multiply",
                    "http://multiply.python.webcalc.40151615.qpc.hal.davecutting.uk"
            );

            Operator divide = new Operator(
                    "divide",
                    "http://divide.golang.webcalc.40151615.qpc.hal.davecutting.uk"
            );
            Operator square = new Operator(
                    "square",
                    "http://square.springbootjava.webcalc.40151615.qpc.hal.davecutting.uk"
            );
            Operator modulo = new Operator(
                    "modulo",
                    "http://modulo.ruby.webcalc.40151615.qpc.hal.davecutting.uk"
            );

            repository.saveAll(List.of(add, subtract, multiply, divide, square, modulo));

        };
    }
}
