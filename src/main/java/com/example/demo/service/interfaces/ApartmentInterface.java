package com.example.demo.service.interfaces;

import com.example.demo.entity.Apartment;

import java.util.List;
import java.util.Map;

public interface ApartmentInterface {
    List<Apartment> getAll();

    Apartment getOne(String id);

    Apartment create(Apartment apartment);

    String update(String id, Map<String, Object> payload);

    String delete(String id);
}
