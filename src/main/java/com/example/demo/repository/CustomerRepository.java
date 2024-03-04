package com.example.demo.repository;

import com.example.demo.entity.Customer;
import com.example.demo.model.DTO.customer.CustomerDTO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("select c from Customer c where concat(c.firstName,' ', c.lastName, ' ', c.address, ' " +
            "', c.age, ' ', c.status) like %?1%")
    Page<Customer> search(@Param("query") String query, Pageable pageable);
}
