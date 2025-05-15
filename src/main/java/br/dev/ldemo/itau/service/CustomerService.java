package br.dev.ldemo.itau.service;

import br.dev.ldemo.itau.dto.CustomerInputDto;
import br.dev.ldemo.itau.dto.CustomerOutputDto;
import br.dev.ldemo.itau.dto.CustomerWrapper;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface CustomerService {

    CustomerOutputDto calculateInsurance(CustomerWrapper customerWrapper);

    List<CustomerOutputDto> getAllInsurance();

    CustomerOutputDto getCustomerById(@NotNull Long id);

    CustomerOutputDto update(@NotNull Long id, CustomerInputDto dto);

    void delete(@NotNull Long id);
}

