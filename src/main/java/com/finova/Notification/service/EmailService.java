package com.finova.Notification.service;

public interface EmailService {

    void sendPasswordResetEmail(String email, String token);
}
