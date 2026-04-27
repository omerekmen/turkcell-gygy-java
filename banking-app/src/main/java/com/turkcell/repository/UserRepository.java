package com.turkcell.repository;

import com.turkcell.model.User;

public interface UserRepository {
    String addUser(String name, String tckn, String password);
    User getUser(String tckn, String password);
    boolean checkUserExists(String tckn);
    boolean checkTcknRules(String tckn);
}
