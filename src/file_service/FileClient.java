package file_service;

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
                    ByteBuffer lisrequest = ByteBuffer.wrap((command).getBytes());
                    SocketChannel lischannel = SocketChannel.open();
                    lischannel.connect(new InetSocketAddress(args[0], serverPort));
                    lischannel.write(lisrequest);
                    lischannel.shutdownOutput();
                    ByteBuffer lisreply = ByteBuffer.allocate(3);
                    lischannel.read(lisreply);
                    lischannel.close();
                    lisreply.flip();
                    byte[] l = new byte[3];
                    lisreply.get(l);
                    String liscode = new String(l);
                    break;
                    //list
                case "ren":
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