import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {
        try(
                final var executorService = Executors.newCachedThreadPool();
                final var serverSocket = new ServerSocket(8080)
        ){
            final Collection<Socket> activeSockets = new ConcurrentLinkedDeque<>();

            while (true) {
                Socket socket = serverSocket.accept();

                executorService.execute(new SocketBroadcaster(activeSockets, socket));
            }
        }
    }
}
