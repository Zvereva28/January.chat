package com.github.Zvereva28.January.chat.server;

public interface AuthManager {
    String getNicknameByLoginAndPassword(String login, String password);
}