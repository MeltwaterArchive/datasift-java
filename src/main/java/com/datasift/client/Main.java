package com.datasift.client;

public class Main {
    public static void main(String... args) {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");
        //or
        //config.auth("username","api-key");
        DataSiftClient datasift = new DataSiftClient(config);
        System.out.println("Hello!");
    }
}