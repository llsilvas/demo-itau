package br.dev.ldemo.itau.entity;

import br.dev.ldemo.itau.dto.CustomerOutputDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String document;
    private LocalDate birthDate;
    private String location;
    private Double vehicle_value;

    public CustomerOutputDto toOutputDto(Customer customer){
        return new CustomerOutputDto(id, name, location, vehicle_value);
    }

}
