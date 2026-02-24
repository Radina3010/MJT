package bg.sofia.uni.fmi.mjt.taskmanager.server;

import bg.sofia.uni.fmi.mjt.taskmanager.command.Command;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class TaskManagerServer {

    private final int port;
    private final TaskManagerStorage taskManagerStorage;
    private final CommandExecutor commandExecutor;

    private ByteBuffer buffer;
    private Selector selector;

    private static final int BUFFER_SIZE = 8192;
    private static final String HOST = "localhost";
    private static final int SAVING_DATA_TIME_INTERVAL_IN_MS = 15 * 1000;
    private static final int SELECT_TIME_IN_MS = 1000;

    private long lastSaveTime;

    public TaskManagerServer(int port, TaskManagerStorage taskManagerStorage) {
        this.port = port;
        this.taskManagerStorage = taskManagerStorage;
        this.commandExecutor = new CommandExecutor(this.taskManagerStorage);
        this.lastSaveTime = System.currentTimeMillis();
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel);

            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                try {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastSaveTime > SAVING_DATA_TIME_INTERVAL_IN_MS) {
                        taskManagerStorage.saveRepositories();
                        lastSaveTime = currentTime;
                    }
                    int readyChannels = selector.select(SELECT_TIME_IN_MS);
                    if (readyChannels == 0) {
                        continue;
                    }
                    processReadyKeys();

                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void processReadyKeys() {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            keyIterator.remove();

            if (!key.isValid()) {
                continue;
            }

            try {
                if (key.isReadable()) {
                    handleReadable(key);
                } else if (key.isAcceptable()) {
                    accept(key);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected forcibly: " + e.getMessage());
            }
        }
    }

    private void handleReadable(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        String clientInput = getClientInput(clientChannel);
        String sessionId = clientChannel.getRemoteAddress().toString();

        if (clientInput == null) {
            commandExecutor.removeSession(sessionId);
            return;
        }

        try {
            Command command = CommandCreator.newCommand(clientInput);
            String output = commandExecutor.execute(command, sessionId);
            writeClientOutput(clientChannel, output);
        } catch (InvalidArgumentsForCommandException e) {
            writeClientOutput(clientChannel, "Error: " + e.getMessage());
        }
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put((output + System.lineSeparator()).getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

}
