package com.example.demo.repositories;

public interface RefreshTokenDao {
    String getRefreshToken(String encrypted_token);

    boolean delete(String encrypted_refresh_token);
    boolean updatePreviousRefreshToken(String encrypted_refresh_token);

    boolean save(String encrypted_refresh_token, String user_id);
}
