package org.ksa.test.worgcount;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) throws IOException {
        System.out.println("Brake after you finish input");
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        PrintWriter printWriter = new PrintWriter(outputStream);
        String s;
        long start = System.currentTimeMillis();
        long count = new Processor().process(inputStream)
                .entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, (e1, e2) -> -e1.compareTo(e2)))
                .peek(e -> {
                    printWriter.println(e.getKey() + "=" + e.getValue());
                    printWriter.flush();
                }).mapToLong(Map.Entry::getValue).sum();
        System.out.printf("words count =%d %d ms", count, System.currentTimeMillis() - start);
    }

}
