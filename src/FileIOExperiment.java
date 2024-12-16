import java.io.*;
import java.util.Random;

public class FileIOExperiment {
    private static final String FILENAME = "test_data.bin";
    private static final int DATA_SIZE = 100_000_000; // Размер массива данных (10 миллионов)

    public static void main(String[] args) {
        byte[] data = generateRandomData();

        System.out.println("Начало записи в файл без буферизации...");
        long timeWithoutBufferWrite = writeToFileWithoutBuffer(data);
        System.out.println("Время записи без буферизации: " + timeWithoutBufferWrite + " мс");

        System.out.println("Начало записи в файл с буферизацией...");
        long timeWithBufferWrite = writeToFileWithBuffer(data);
        System.out.println("Время записи с буферизацией: " + timeWithBufferWrite + " мс");

        System.out.println("Начало чтения из файла без буферизации...");
        long timeWithoutBufferRead = readFromFileWithoutBuffer();
        System.out.println("Время чтения без буферизации: " + timeWithoutBufferRead + " мс");

        System.out.println("Начало чтения из файла с буферизацией...");
        long timeWithBufferRead = readFromFileWithBuffer();
        System.out.println("Время чтения с буферизацией: " + timeWithBufferRead + " мс");
    }

    // Генерация случайных данных для массива байтов
    private static byte[] generateRandomData() {
        byte[] data = new byte[FileIOExperiment.DATA_SIZE];
        new Random().nextBytes(data);
        return data;
    }

    // Запись в файл без использования буферизации
    private static long writeToFileWithoutBuffer(byte[] data) {
        long startTime = System.currentTimeMillis();
        try (FileOutputStream fos = new FileOutputStream(FileIOExperiment.FILENAME)) {
            fos.write(data);
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла (без буферизации): " + e.getMessage());
        }
        return System.currentTimeMillis() - startTime;
    }

    // Запись в файл с использованием буферизации
    private static long writeToFileWithBuffer(byte[] data) {
        long startTime = System.currentTimeMillis();
        try (FileOutputStream fos = new FileOutputStream(FileIOExperiment.FILENAME);
        BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(data);
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла (с буферизацией): " + e.getMessage());
        }

        return System.currentTimeMillis() - startTime;
    }

    // Чтение из файла без использования буферизации
    private static long readFromFileWithoutBuffer() {
        long startTime = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(FileIOExperiment.FILENAME)) {
            byte[] buffer = new byte[FileIOExperiment.DATA_SIZE];
            int bytesRead = fis.read(buffer);

            if (bytesRead != FileIOExperiment.DATA_SIZE) {
                System.err.println("Ошибка: Прочитано неверное количество байт при чтении без буферизации.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла (без буферизации): " + e.getMessage());
        }
        return System.currentTimeMillis() - startTime;
    }

    // Чтение из файла с использованием буферизации
    private static long readFromFileWithBuffer() {
        long startTime = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(FileIOExperiment.FILENAME);
        BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] buffer = new byte[FileIOExperiment.DATA_SIZE];
            int bytesRead = bis.read(buffer);

            if (bytesRead != FileIOExperiment.DATA_SIZE) {
                System.err.println("Ошибка: Прочитано неверное количество байт при чтении c буферизацией.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла (с буферизацией): " + e.getMessage());
        }
        return System.currentTimeMillis() - startTime;
    }
}