package ru.geekbrains.lesson7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Socket socket;
    private final ChatServer server;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private String nick;

    public ClientHandler(Socket socket, ChatServer server) {
        try {
            this.nick = "";
            this.socket = socket;
            this.server = server;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authenticate();
                    readMessages();
                } finally {
                    closeConnection();
                }

            }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void authenticate() {
        while (true) {
            try {
                final String newMessage = dataInputStream.readUTF();
                if (newMessage.startsWith("/auth")) {
                    final String[] loginAndPassword = newMessage.split(" ");
                    final String login = loginAndPassword[1];
                    final String password = loginAndPassword[2];
                    final String nick = server.getAuthService().getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (server.isNickBusy(nick)) {
                            sendMessage("Пользователь уже авторизован");
                            continue;
                        }
                        sendMessage("/AuthOk " + nick);
                        this.nick = nick;
                        server.subscribe(this);
                        server.broadcast("Пользователь " + nick + " зашел в чат");
                        break;
                    } else {
                        sendMessage("Неверные логин и пароль");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessages() {
        try {
            while (true) {
                final String message = dataInputStream.readUTF();
                if ("/end".equals(message)) {
                    break;
                }
                if (message.startsWith("/w")){
                    String[] s = message.split(" ");
                    final String receiver = s[1];
                    final String privateMessage = message.substring(receiver.length() + 9);
                    server.privateMessage(nick, receiver, privateMessage);
                } else {
                    server.broadcast(nick + "/: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(String message) {
        try {
            System.out.println("Server: Send message to " + nick);
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }

    private void closeConnection() {
        server.unsubscribe(this);
        if (dataInputStream != null) {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
