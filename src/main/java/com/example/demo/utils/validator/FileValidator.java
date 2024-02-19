package com.example.demo.utils.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.helpers.CsvHelper;
import com.example.demo.message.FileMessage;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator {
    public static void notNullFile(MultipartFile file) throws InValidException {
        if (file.isEmpty()) throw new InValidException(FileMessage.FILE_NOT_FOUND);
    }

    public static void notCsvType(MultipartFile file) throws InValidException {
        if (!CsvHelper.hasCsvFormat(file)) throw new InValidException(FileMessage.MUST_TYPE_CSV);
    }

    public static void validatorMultipartFile(MultipartFile file) {
        notNullFile(file);
    }

}
