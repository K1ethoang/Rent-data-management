package com.example.demo.repository;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    @Query("select c from Contract c where concat(c.apartment, ' ', c.customer, ' ', c.endDate, " +
            "' ', c.startDate) like %:query%")
    Page<Contract> search(@Param("query") String query, Pageable pageable);
}
