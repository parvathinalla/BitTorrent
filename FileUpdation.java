
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileUpdation {
    public static boolean checkFileExists(String peerId) throws IOException {
        File file = new File("./input_files" + peerId + "/thefile");
        return file.exists();
    }

    public static byte[] extractChunk(byte[] original, int start, int end) {
        byte[] chunk = new byte[end - start];
        System.arraycopy(original, start, chunk, 0, Math.min(original.length - start,
                end - start));
        return chunk;
    }

    public static HashMap<Integer, byte[]> chunkFileData(int fileSize, int chunkSize, String fileName)
            throws Exception {
        HashMap<Integer, byte[]> chunkedData = new HashMap<>();
        BufferedInputStream inputStream = new BufferedInputStream(
                new FileInputStream("./input_files/" + ParentThread.peerID + "/" +
                        fileName));
        byte[] fileBuffer = new byte[fileSize];

        inputStream.read(fileBuffer);
        inputStream.close();
        int currentChunkIndex = 0, chunkCounter = 0;

        while (currentChunkIndex < fileSize) {
            if (currentChunkIndex + chunkSize <= fileSize) {
                chunkedData.put(chunkCounter,
                        extractChunk(fileBuffer, currentChunkIndex, currentChunkIndex + chunkSize));
                chunkCounter++;
            } else {
                chunkedData.put(chunkCounter, extractChunk(fileBuffer, currentChunkIndex,
                        fileSize));
                chunkCounter++;
            }
            currentChunkIndex += chunkSize;
        }

        return chunkedData;
    }

    public static void initializeDirectories(int peerID, String fileName) throws IOException {
        Path directoryPath = Paths.get("./input_files/" + peerID);
        System.out.println(directoryPath.toString());
        if (Files.exists(directoryPath)) {
            cleanDirectory(directoryPath, fileName);
        } else {
            Files.createDirectory(directoryPath);
        }
        System.out.println("Directory initialized");
        new File("./input_files/" + peerID + "/logs_" + peerID + ".log");
    }

    public static void cleanDirectory(Path directory, String fileName) throws IOException {
        Stream<Path> filesStream = Files.list(directory);

        for (Object pathObject : filesStream.toArray()) {
            Path currentPath = (Path) pathObject;
            if (!currentPath.getFileName().toString().equals(fileName)) {
                Files.delete(currentPath);
            }
        }
        filesStream.close();
    }

    public static HashMap<Integer, byte[]> sortChunkedData(HashMap<Integer, byte[]> dataMap) throws Exception {
        List<Map.Entry<Integer, byte[]>> entryList = new LinkedList<>(dataMap.entrySet());

        // Sort the list
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, byte[]>>() {
            public int compare(Map.Entry<Integer, byte[]> firstEntry,
                    Map.Entry<Integer, byte[]> secondEntry) {
                return firstEntry.getKey().compareTo(secondEntry.getKey());
            }
        });

        // Put data from sorted list to hashmap
        HashMap<Integer, byte[]> sortedData = new LinkedHashMap<>();
        for (Map.Entry<Integer, byte[]> sortedEntry : entryList) {
            sortedData.put(sortedEntry.getKey(), sortedEntry.getValue());
        }
        return sortedData;
    }
}
