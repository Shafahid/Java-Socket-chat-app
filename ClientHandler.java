import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) throws IOException {
        try {
            // creating a client
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine(); // this will come from client
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {

            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

//    @Override
//    public void run() {
//        String messageFromClient;
//        while (socket.isConnected()) {
//            try {
//                messageFromClient = bufferedReader.readLine();
//                broadcastMessage(messageFromClient);
//            } catch (IOException e) {
//                try {
//                    closeEverything(socket, bufferedReader, bufferedWriter);
//                } catch (IOException ex) {
//                    System.out.println(clientUsername+" got disconnected!");
//                    throw new RuntimeException(ex);
//                }
//                break;
//            }
//        }
//    }

    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient == null) {
                    // Client disconnected
                    break;
                }
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                break;
            }
        }

        // Client disconnected, remove them and notify others
        try {
            closeEverything(socket, bufferedReader, bufferedWriter);
            System.out.println(clientUsername + " has disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String messageToSend) throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) { // Sends the message to all clients except the sender.
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
        public void removeClientHandler () throws IOException {
            clientHandlers.remove(this);
            broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
        }

        public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter) throws IOException {
            removeClientHandler();
            try{
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
                if(bufferedWriter!=null){
                    bufferedWriter.close();
                }
                if(socket!=null){
                    socket.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }


}


