package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.exception.AppException;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.interfaces.ApartmentInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ApartmentService implements ApartmentInterface {
    private final ApartmentRepository apartmentRepository;

    @Override
    public List<Apartment> getAll() {
        return apartmentRepository.findAll();
    }

    @Override
    public Apartment getOne(String id) throws AppException {
        Apartment apartment = apartmentRepository.findById(id).orElse(null);

        if (apartment != null) {
            return apartment;
        } else
            throw new AppException(404, "Apartment not found");
    }

    @Override
    public Apartment create(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    @Override
    public Apartment update(String id, Map<String, Object> payload) throws AppException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);

        if (apartmentFromDB != null) {

            payload.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Apartment.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, apartmentFromDB, value);
                }
            });

            return apartmentRepository.save(apartmentFromDB);
        } else
            throw new AppException(404, "Apartment not found");
    }

    @Override
    public void delete(String id) throws AppException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);

        if (apartmentFromDB != null) {
            apartmentRepository.delete(apartmentFromDB);
        } else
            throw new AppException(404, "Apartment not found");
    }
}
