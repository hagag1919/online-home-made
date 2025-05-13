package com.example.systemadmin.utils;

public class CompanyAccountResponse {
    private Long id;
    private String name;
    private String password;

    public CompanyAccountResponse(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public CompanyAccountResponse() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
