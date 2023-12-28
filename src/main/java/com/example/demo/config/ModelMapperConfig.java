package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();

        // Configuration
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Add Mapping
        // Contract -> ContractDTO
//        modelMapper.typeMap(Contract.class, ContractDTO.class).addMappings(mapper -> {
//            mapper.map(src -> src.getCustomer().getId(), ContractDTO::setCustomerId);
//            mapper.map(src -> src.getApartment().getId(), ContractDTO::setApartmentId);
//        });

        // ContractDTO -> Contract
//        modelMapper.typeMap(ContractDTO.class, Contract.class).addMappings(mapper -> {
//            mapper.map(ContractDTO::getCustomerId, (dest, value) -> dest.getCustomer().setId((value.toString())));
//            mapper.map(ContractDTO::getApartmentId, (dest, value) -> dest.getApartment().setId(value.toString()));
//        });

        return modelMapper;
    }
}
