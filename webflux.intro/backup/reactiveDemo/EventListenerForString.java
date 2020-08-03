package reactiveDemo;

import java.util.List;

public interface EventListenerForString {
    void onDataChunk(List<String> chunk);
    void processComplete();
}
