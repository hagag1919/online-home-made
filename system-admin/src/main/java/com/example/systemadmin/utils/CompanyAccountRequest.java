package com.example.systemadmin.utils;

import java.util.List;

public class CompanyAccountRequest {
    private List<String> companyNames;

    public CompanyAccountRequest(List<String> companyNames) {
        this.companyNames = companyNames;
    }
    public List<String> getCompanyNames() {
        return companyNames;
    }
    public void setCompanyNames(List<String> companyNames) {
        this.companyNames = companyNames;
    }
}
