package com.shiep.service;

public interface IMailService {
    Boolean sendMailAndGenerateCode(String  mailAddress);


    String generateCodeFromRedis(String mail);
}
