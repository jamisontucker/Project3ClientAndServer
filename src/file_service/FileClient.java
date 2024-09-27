package file_service;

import org.w3c.dom.DOMStringList;

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.io.FileInputStream;

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
                    ByteBuffer delRequest = ByteBuffer.wrap(
                            (command+fileName).getBytes());
                    SocketChannel channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(delRequest);
                    channel.shutdownOutput();
                    //TODO: receive the status code and tell the user
                    ByteBuffer delReply = ByteBuffer.allocate(3);
                    channel.read(delReply);
                    channel.close();
                    delReply.flip();
                    byte[] a = new byte[3];
                    delReply.get(a);
                    String code = new String(a);
                    if(code.equals("suc")){
                        System.out.println("File was successfully deleted.\nStatus code: " + code);
                    }else if (code.equals("fai")){
                        System.out.println("Failed to delete the file.\nStatus code: " + code);
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
                    ByteBuffer lisReply = ByteBuffer.allocate(1024);
                    int lisReplyRead = lisChannel.read(lisReply);
                    lisChannel.close();
                    lisReply.flip();
                    byte[] b = new byte[lisReplyRead];
                    lisReply.get(b);
                    String replyList = new String(b);
                    String[] arr = replyList.split("<");
                    for (int i = 0; i < arr.length; i++) {
                        System.out.println(arr[i]);
                    }
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
                        System.out.println("File was successfully renamed.\nStatus code: " + renCode);
                    }else if (renCode.equals("fai")){
                        System.out.println("Failed to rename the file.\n Status code: " + renCode);
                    }else{
                        System.out.println("Invalid server code received!");
                    }
                    break;
                    //rename
                case "upl":
                    System.out.println("Enter name of file to upload: ");
                    String uplFileName = keyboard.nextLine();
                    ByteBuffer uplRequest = ByteBuffer.wrap(
                            (command).getBytes()
                    );

                    int uplNameLength = uplFileName.length();

                    SocketChannel uplChannel = SocketChannel.open();
                    uplChannel.connect(new InetSocketAddress(args[0], serverPort));
                    FileInputStream fis = new FileInputStream("");
                    uplChannel.write(uplRequest);
                    uplChannel.write(ByteBuffer.allocateDirect(uplNameLength));
                    uplChannel.write(ByteBuffer.wrap(uplFileName.getBytes()));
                    byte[] data = new byte[1024];
                    int bytesRead = 0;
                    while((bytesRead=fis.read(data)) != -1) {
                        ByteBuffer buffer = ByteBuffer.wrap(data, 0, bytesRead);
                        uplChannel.write(buffer);
                    }

                    fis.close();
                    uplChannel.shutdownOutput();

                    ByteBuffer uplReply = ByteBuffer.allocate(1024);
                    uplChannel.read(uplReply);
                    uplChannel.close();
                    uplReply.flip();
                    byte[] u = new byte[3];
                    uplReply.get(u);
                    String uplCode = new String(u);
                    if(uplCode.equals("suc")){
                        System.out.println("File was successfully uploaded.");
                    }else if (uplCode.equals("fai")){
                        System.out.println("Failed to upload the file.");
                    }else{
                        System.out.println("Invalid server code received!");
                    }
                    break;
                    //upload
                case "dow":
                    System.out.println("Enter name of file to download: ");
                    String dowFileName = keyboard.nextLine();
                    ByteBuffer dowRequest = ByteBuffer.wrap(
                            (command+dowFileName).getBytes()
                    );
                    SocketChannel dowChannel = SocketChannel.open();
                    dowChannel.connect(new InetSocketAddress(args[0], serverPort));
                    dowChannel.write(dowRequest);
                    dowChannel.shutdownOutput();

                    ByteBuffer dowSucReply = ByteBuffer.allocate(3);
                    dowChannel.read(dowSucReply);
                    dowSucReply.flip();
                    byte[] f = new byte[3];
                    dowSucReply.get(f);
                    String dowCode = new String(f);
                    if (dowCode.equals("suc")) {
                        FileOutputStream dowFos = new FileOutputStream("Downloads/" + dowFileName);
                        ByteBuffer dowReply = ByteBuffer.allocate(1024);
                        int dowBytesRead = 0;
                        while ((dowBytesRead = dowChannel.read(dowReply)) != -1) {
                            dowReply.flip();
                            byte[] g = new byte[dowBytesRead];
                            dowReply.get(g);
                            dowFos.write(g);
                            dowReply.clear();
                        }
                        System.out.println("File downloaded!");
                        dowFos.close();
                    }
                    else if (dowCode.equals("fai")){
                        System.out.println("File does not exist!");
                    }
                    else{
                        System.out.println("Invalid code received!");
                    }


                    dowChannel.close();
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