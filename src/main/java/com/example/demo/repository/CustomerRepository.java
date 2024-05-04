package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("select c from Customer c where concat(c.fullName,' ', c.phoneNumber, ' ', c.address, ' " +
            "', c.citizenId, ' ', c.dob) like %:query%")
    Page<Customer> search(@Param("query") String query, Pageable pageable);
}
