package file_service;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class FileServer {
    public static void main(String[] args)throws Exception{
        int port = 3000;
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        listenChannel.bind(new InetSocketAddress(port));
        while(true){
            SocketChannel serverChannel = listenChannel.accept();
            ByteBuffer request = ByteBuffer.allocate(1024);
            int numBytes = serverChannel.read(request);
            request.flip();
            //the size of the byte[] should match the number of bytes of the commands
            byte[] a = new byte[3];
            request.get(a);
            String command = new String(a);
            switch (command){
                case "del":
                    break;
                case "lis":
                    break;
                case "ren":
                    break;
                case "upl":
                    break;
                case "dow":
                    break;
                default:
                    System.out.print("Invalid command!");
            }
        }
    }
}
