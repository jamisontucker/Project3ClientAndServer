package file_service;

import java.io.File;
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
                    byte[] b = new byte[request.remaining()];
                    request.get(b);
                    String fileName = new String(b);

                    System.out.println("File to delete: " +fileName);
                    File file = new File("ServerFiles/"+fileName);
                    boolean success = false;
                    if(file.exists()){
                        success = file.delete();
                    }
                    if(success){
                        System.out.println("File deleted successfully.");
                        ByteBuffer code = ByteBuffer.wrap("suc".getBytes());
                        serverChannel.write(code);
                    }else{
                        System.out.println("Unable to delete file");
                        ByteBuffer code = ByteBuffer.wrap("fai".getBytes());
                        serverChannel.write(code);
                    }
                    serverChannel.close();
                    //delete
                    break;
                case "lis":
                    File folder = new File(".");
                    File[] listOfFiles = folder.listFiles();
                    if (listOfFiles != null) {
                        for (File listOfFile : listOfFiles) {
                            String listFile = (listOfFile.getName());
                            ByteBuffer toClient = ByteBuffer.wrap(listFile.getBytes());
                            serverChannel.write(toClient);
                        }
                    }
                    serverChannel.close();
                    break;
                case "ren":
                    byte[] d = new byte[request.remaining()];
                    request.get(d);
                    String remaining = new String(d);
                    String[] arr = remaining.split("<");

                    System.out.println("File to rename: " + arr[0]);
                    System.out.println("New name: " + arr[1]);
                    File renFile = new File("ServerFiles/"+arr[0]);
                    File toFile = new File("ServerFiles/"+arr[1]);
                    boolean renSuccess = false;
                    if(renFile.exists()){
                        renSuccess = renFile.renameTo(toFile);
                    }
                    if(renSuccess){
                        System.out.println("File renamed successfully");
                        ByteBuffer renCode = ByteBuffer.wrap("suc".getBytes());
                        serverChannel.write(renCode);
                    }else{
                        System.out.println("File could not be renamed");
                        ByteBuffer renCode = ByteBuffer.wrap("fai".getBytes());
                        serverChannel.write(renCode);
                    }
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
