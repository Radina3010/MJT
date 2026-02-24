package bg.sofia.uni.fmi.mjt.taskmanager.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TaskManagerClient {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 8192;
    private static final String CHARSET = "UTF-8";
    private static final String MESSAGE_TO_USER = "Enter command: ";
    private static final String BREAK_WORD = "disconnect";

    static void main() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            while (true) {
                System.out.print(MESSAGE_TO_USER);
                String message = scanner.nextLine();

                if (BREAK_WORD.equals(message)) {
                    break;
                }
                process(buffer, message, socketChannel);
            }

            //custom IOExcception??
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication.", e);
        }
    }

    private static void process(ByteBuffer buffer, String message, SocketChannel socketChannel) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);

        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        String reply = new String(byteArray, CHARSET);

        System.out.println(reply);
    }
}
