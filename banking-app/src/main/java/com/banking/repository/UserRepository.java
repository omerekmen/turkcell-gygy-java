package com.banking.repository;

import com.banking.model.User;

public interface UserRepository {
    String addUser(String name, String tckn, String password);
    User getUser(String tckn, String password);
    boolean checkUserExists(String tckn);
    boolean checkTcknRules(String tckn);
}
