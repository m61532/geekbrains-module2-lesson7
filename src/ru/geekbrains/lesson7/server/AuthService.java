package ru.geekbrains.lesson7.server;

public interface AuthService {
    String getNickByLoginAndPassword(String login, String password);
}
