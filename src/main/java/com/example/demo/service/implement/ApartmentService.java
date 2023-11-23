package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.exception.AppException;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.interfaces.ApartmentInterface;
import com.example.demo.service.message.ApartmentMessage;
import com.example.demo.utils.MyUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class ApartmentService implements ApartmentInterface {
    private final ApartmentRepository apartmentRepository;

    @Override
    public List<Apartment> getAll() throws AppException {
        List<Apartment> apartments = apartmentRepository.findAll();

        if (apartments.isEmpty())
            throw new AppException(HttpStatus.BAD_REQUEST, ApartmentMessage.EMPTY_LIST);

        return apartments;
    }

    @Override
    public Apartment getOne(String id) throws AppException {
        Apartment apartment = apartmentRepository.findById(id).orElse(null);

        if (apartment != null) {
            return apartment;
        } else
            throw new AppException(HttpStatus.NOT_FOUND, ApartmentMessage.NOT_FOUND);
    }

    @Override
    public Apartment create(Apartment apartment) throws AppException {
        return apartmentRepository.save(validateObject(apartment));
    }

    @Override
    public String update(String id, Map<String, Object> payload) throws AppException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);

        if (apartmentFromDB != null) {

            payload.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Apartment.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, apartmentFromDB, value);
                }
            });

            apartmentRepository.save(validateObject(apartmentFromDB));
            return ApartmentMessage.SUCCESS_UPDATED;
        } else
            throw new AppException(HttpStatus.NOT_FOUND, ApartmentMessage.NOT_FOUND);
    }

    @Override
    public String delete(String id) throws AppException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);

        if (apartmentFromDB != null) {
            apartmentRepository.delete(apartmentFromDB);

            return ApartmentMessage.SUCCESS_DELETED;
        } else
            throw new AppException(HttpStatus.NOT_FOUND, ApartmentMessage.NOT_FOUND);
    }

    private Apartment validateObject(Apartment apartment) {
        int numberOfRoom = apartment.getNumberOfRoom();

        if (numberOfRoom < 0 || !MyUtils.isNumeric(String.valueOf(numberOfRoom))) {
            throw new AppException(HttpStatus.BAD_REQUEST, ApartmentMessage.INVALID_DATA);
        }

        double retailPrice = MyUtils.stringToNumeric(apartment.getRetailPrice());

        if (retailPrice < 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, ApartmentMessage.INVALID_DATA);
        }

        apartment.setRetailPrice(String.valueOf(retailPrice));

        return apartment;
    }
}
