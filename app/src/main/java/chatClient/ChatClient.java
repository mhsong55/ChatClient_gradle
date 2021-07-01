package chatClient;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    private final static String SERVER_HOST = "*";
    private final static int SERVER_PORT = 0;
    private final static String NICKNAME = "*";

    private Socket mSocket;
    private String mName;

    public void connect() {
        try {
            mSocket = new Socket(SERVER_HOST, SERVER_PORT);
            ClientWrite clientWrite = new ClientWrite(NICKNAME);
            clientWrite.start();
            ClientRead clientRead = new ClientRead();
            clientRead.start();
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
                System.out.println("id : " + nickName + " connected");
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
                // Json구성
                String msg = in.nextLine();
                long time = System.currentTimeMillis();
                MsgInfo msgInfo = new MsgInfo(mName, msg, time);

                Gson gson = new Gson();
//					String json = "{\"nickName\":\"" + nickName + "\",\"msg\":\"" + msg + "\",\"time\":\"" + time + "\"}";
                try {
                    mOutputStream.writeUTF(gson.toJson(msgInfo));
//                    System.out.println(gson.toJson(msgInfo));
                    mOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               in.close();
            }
        }
    }
}
