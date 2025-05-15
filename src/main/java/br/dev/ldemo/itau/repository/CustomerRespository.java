package br.dev.ldemo.itau.repository;

import br.dev.ldemo.itau.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRespository extends JpaRepository<Customer, Long> {

    Optional<Customer> findById(Long id);
}
