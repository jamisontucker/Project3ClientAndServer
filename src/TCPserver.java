import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class TCPserver {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        listenChannel.bind(new InetSocketAddress(3000));
    }
}
