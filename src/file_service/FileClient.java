package file_service;

import org.w3c.dom.DOMStringList;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class FileClient {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Please provide <serverIP> and <serverPort>");
            return;
        }
        int serverPort = Integer.parseInt(args[1]);
        String command;
        do{
            System.out.println("\nPlease enter a command");
            Scanner keyboard = new Scanner(System.in);
            command = keyboard.nextLine();
            switch(command){
                case "del":
                    System.out.println("Please enter the file name:");
                    String fileName = keyboard.nextLine();
                    ByteBuffer request = ByteBuffer.wrap(
                            (command+fileName).getBytes());
                    SocketChannel channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(request);
                    channel.shutdownOutput();
                    //TODO: receive the status code and tell the user
                    ByteBuffer reply = ByteBuffer.allocate(3);
                    channel.read(reply);
                    channel.close();
                    reply.flip();
                    byte[] a = new byte[3];
                    reply.get(a);
                    String code = new String(a);
                    if(code.equals("suc")){
                        System.out.println("File was successfully deleted.");
                    }else if (code.equals("fai")){
                        System.out.println("Failed to delete the file.");
                    }else{
                        System.out.println("Invalid server code received!");
                    }
                    break;
                    //delete
                case "lis":
                    ByteBuffer lisRequest = ByteBuffer.wrap((command).getBytes());
                    SocketChannel lisChannel = SocketChannel.open();
                    lisChannel.connect(new InetSocketAddress(args[0], serverPort));
                    lisChannel.write(lisRequest);
                    lisChannel.shutdownOutput();
                    //TODO: Receive list array from server
                    break;
                    //list
                case "ren":
                    System.out.println("Please enter the file name: ");
                    String renFileName = keyboard.nextLine();
                    System.out.println("Please enter the new name:" );
                    String newName = keyboard.nextLine();
                    ByteBuffer renRequest = ByteBuffer.wrap(
                            (command+renFileName+"<"+newName).getBytes()
                    );
                    SocketChannel renChannel = SocketChannel.open();
                    renChannel.connect(new InetSocketAddress(args[0], serverPort));
                    renChannel.write(renRequest);
                    renChannel.shutdownOutput();

                    ByteBuffer renReply = ByteBuffer.allocate(3);
                    renChannel.read(renReply);
                    renChannel.close();
                    renReply.flip();
                    byte[] c = new byte[3];
                    renReply.get(c);
                    String renCode = new String(c);
                    if(renCode.equals("suc")){
                        System.out.println("File was successfully renamed.");
                    }else if (renCode.equals("fai")){
                        System.out.println("Failed to rename the file.");
                    }else{
                        System.out.println("Invalid server code received!");
                    }
                    break;
                    //rename
                case "upl":
                    break;
                    //upload
                case "dow":
                    break;
                    //download
                default:
                    if(!command.equals("Q")) {
                        System.out.println("Invalid command!");
                    }
            }
        }while(!command.equals("Q"));
    }
}