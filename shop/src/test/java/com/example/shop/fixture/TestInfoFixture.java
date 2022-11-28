package com.example.shop.fixture;

import lombok.Data;

public class TestInfoFixture {

    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setUserName("name");
        info.setPassword("password");
        info.setSecretKey("fast-campus.simple_sns_2022_secret_key");
        return info;
    }

    @Data
    public static class TestInfo {
        private String userName;
        private String password;
        private String secretKey;
    }
}
