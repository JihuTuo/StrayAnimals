package com.shiep.service;

public interface IAuthService {
    String accredit(String username, String password);

    boolean resetPassword(String username, String email);
}
