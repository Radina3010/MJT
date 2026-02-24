import bg.sofia.uni.fmi.mjt.taskmanager.server.TaskManagerServer;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;

void main() {
    TaskManagerStorage storage = new TaskManagerStorage();
    TaskManagerServer server = new TaskManagerServer(7777, storage);
    server.start();
}
