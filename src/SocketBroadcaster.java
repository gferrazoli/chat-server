import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collection;

public class SocketBroadcaster implements Runnable{
    private final Collection<Socket> activeSockets;
    private final Socket socket;

    public SocketBroadcaster(Collection<Socket> activeSockets, Socket socket) {
        this.activeSockets = activeSockets;
        this.socket = socket;
    }

    @Override
    public void run() {
        activeSockets.add(socket);
        try {
            var socketInputStream = socket.getInputStream();
            var bufferedReader = new BufferedReader(new InputStreamReader(socketInputStream));

            while(true) {
                var inputAsString = bufferedReader.readLine().concat("\n");

                for (var activeSocket : activeSockets) {
                    if(!activeSocket.equals(socket)){
                        var outputStream = activeSocket.getOutputStream();
                        var inputStreamBytes = inputAsString.getBytes();
                        outputStream.write(inputStreamBytes);
                        outputStream.flush();
                        System.out.print("Broadcasting: " + inputAsString);
                    }
                }
            }
        } catch (IOException e) {
            activeSockets.remove(socket);
            System.out.println("IO exception on socket, removing it");
        }
    }
}