package com.example.demo;

public class MapPath {
    static String HOST_PATH = "http://localhost:5174";
    static String LOGIN_PATH = HOST_PATH.concat("/login");
    static String CHANGE_PASSWORD_PATH = HOST_PATH.concat("/account/edit" +
            "/changePassword");
}
