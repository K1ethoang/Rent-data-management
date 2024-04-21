package com.example.demo.repository;

import com.example.demo.entity.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String> {

    @Query("select a from Apartment a where concat(a.address, ' ', a.numberOfRoom, ' ', a" +
            ".retailPrice) like %:query%")
    Page<Apartment> search(@Param("query") String query, Pageable pageable);
}
