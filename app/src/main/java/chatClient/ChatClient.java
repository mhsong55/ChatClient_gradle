package chatClient;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ChatClient {
    private final static String SERVER_HOST = "localhost";
    private final static int SERVER_PORT = 20111;
    private final static String NICKNAME = "ChatClient";

    private Socket mSocket;
    private String mName;

    public void connect() {
        try {
            mSocket = new Socket(SERVER_HOST, SERVER_PORT);
            // mSocket.setKeepAlive(true);
            // System.out.println("KeepAlive = " + mSocket.getKeepAlive());

            ClientWrite clientWrite = new ClientWrite(NICKNAME);
            clientWrite.start();
            ClientRead clientRead = new ClientRead();
            clientRead.start();
            // KeepAlive Settings
            // ClientKeepAlive clientKeepAlive = new ClientKeepAlive();
            // clientKeepAlive.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientRead extends Thread {
        private DataInputStream mInputStream;

        @Override
        public void run() {
            try {
                mInputStream = new DataInputStream(mSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.run();
            try {
                // Always Listen
                while (mInputStream != null) {
                    // json parsing
                    String json = mInputStream.readUTF();
                    try {
                        MsgInfo msgInfo = new Gson().fromJson(json, MsgInfo.class);
                        System.out.println(json);
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Exit
                mSocket = null;
            }
        }
    }

    class ClientWrite extends Thread {

        private DataOutputStream mOutputStream;


        public ClientWrite(String nickName) {
            try {
                mName = nickName;
                mOutputStream = new DataOutputStream(mSocket.getOutputStream());
                mOutputStream.writeUTF(nickName);
                mOutputStream.flush();

                System.out.println("id : " + nickName + " connected, DateTime = " + LocalDateTime.now()
                        .plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " KST");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("writeUTF IOException");
            }
        }

        @Override
        public void run() {
            Scanner in = new Scanner(System.in);

            while (true) {
                System.out.print("message : ");

                String msg = in.nextLine();
                long time = System.currentTimeMillis();
                MsgInfo msgInfo = new MsgInfo(mName, msg, LocalDateTime.now().plusHours(9)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " KST");

                Gson gson = new Gson();
//					String json = "{\"nickName\":\"" + nickName + "\",\"msg\":\"" + msg + "\",\"time\":\"" + time + "\"}";
                try {
                    mOutputStream.writeUTF(gson.toJson(msgInfo));
//                    System.out.println(gson.toJson(msgInfo));
                    mOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // in.close();
            }
        }
    }

    class ClientKeepAlive extends Thread {

        private DataOutputStream mOutputStream;

        public ClientKeepAlive() {
            try {
                mOutputStream = new DataOutputStream(mSocket.getOutputStream());
                mOutputStream.writeUTF("PING, DateTime = " + LocalDateTime.now().plusHours(9)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " KST");
                mOutputStream.flush();
                System.out.println("PING, DateTime = " + LocalDateTime.now().plusHours(9)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " KST");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("writeUTF IOException");
            }
        }

        @Override
        public void run() {

            while (true) {

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mOutputStream.writeUTF("PING, DateTime = " + LocalDateTime.now().plusHours(9)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " KST");
                    mOutputStream.flush();
                    System.out.println("PING, DateTime = " + LocalDateTime.now().plusHours(9)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " KST");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
