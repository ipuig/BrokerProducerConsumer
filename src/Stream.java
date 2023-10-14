import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Stream {
    

    public static String PRODUCER_VIDEO_FRAMES_PATH = "producer_data/video_frames/";
    public static String PRODUCER_AUDIO_CHUNKS_PATH = "producer_data/video_frames/";

    public static String CONSUMER_DATA_OUTPUT = "consumer_data/";

    // default void createDir() {
    //
    // }

    public default byte[] loadFile(String filePath) {

        try {
            Path path = Path.of(filePath);
            byte[] data = Files.readAllBytes(path);
            return data;
        }
        catch(IOException e) {
            return new byte[0];
        }


    }

    // default void storeFile() {
    //
    // }
}
