package br.dev.ldemo.itau.dto;

import br.dev.ldemo.itau.entity.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record CustomerInputDto(
        @NotBlank
        String name,
        @NotBlank
        String document,
        @NotNull
        LocalDate birthDate,
        @NotBlank
        String location,
        @NotNull
        @PositiveOrZero
        double vehicle_value
) {
    public Customer toEntity() {
        return Customer.builder()
                .name(name)
                .document(document)
                .birthDate(birthDate)
                .location(location)
                .vehicle_value(vehicle_value)
                .build();
    }
}
