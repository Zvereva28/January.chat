package com.github.Zvereva28.January.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private AuthManager authManager;
    private List<ClientHandler> clients;

    public AuthManager getAuthManager() {
        return authManager;
    }

    public Server(int port) {
        clients = new ArrayList<>();
        authManager = new BasicAuthManager();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен. Ожидаем подключения клиентов...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMsg(String msg) { // рассылка сообщений всем "клиентам чата"
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }
    public void wispMsg(ClientHandler senderClient, String nick, String msg) { // приват сообщение
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(nick)) {
                o.sendMsg("персональное сообщение от  " + senderClient.getNickname() + msg);
            }
        }
        senderClient.sendMsg( "пользователя нет в сети");
    }

    public boolean isNickBusy(String nickname) { // проверка занят ли никнэйм
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void subscribe(ClientHandler clientHandler) { //добавление клиента в список пользователей чата
        broadcastMsg(clientHandler.getNickname() + " вошел в чат /n");
        clients.add(clientHandler);
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) { //удаление клиента из списка пользователей в сети
        broadcastMsg(clientHandler.getNickname() + " вышел из чата /n");
        clients.remove(clientHandler);
    }


}