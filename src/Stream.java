import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public interface Stream {
    

    public static String PRODUCER_VIDEO_FRAMES_PATH = "producer_data/video_frames/";
    public static String PRODUCER_AUDIO_CHUNKS_PATH = "producer_data/audio_chunks/";

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
    //

    public default byte[] generateRandomString() {
        Random random = new Random();
        int length = random.nextInt(1024) + 1; 
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(62) + 48);
            if (randomChar > 57) randomChar += 7; 
            if (randomChar > 90) randomChar += 6; 
            stringBuilder.append(randomChar);
        }

        String result = "Randomly generated string: { " + stringBuilder.toString() + " }\n";

        return result.getBytes();
    }

}
