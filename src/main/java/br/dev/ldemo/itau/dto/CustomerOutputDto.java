package br.dev.ldemo.itau.dto;

import br.dev.ldemo.itau.entity.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;

public record CustomerOutputDto (

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long id,
        String name,
        String location,
        double vehicle_value
){

        private CustomerOutputDto toDto(Customer customer){
                return new CustomerOutputDto(
                        customer.getId(),
                        customer.getName(),
                        customer.getLocation(),
                        customer.getVehicle_value()
                );
        }
}
