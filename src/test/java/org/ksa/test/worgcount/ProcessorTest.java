package org.ksa.test.worgcount;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

class ProcessorTest {

    @Test
    void test1() {
        String s = "Lord, hear my prayer oh so dear.\n My heart is aching; my mind isn't clear.";
        InputStream stream = new ByteArrayInputStream(s.getBytes(Charset.forName("UTF-8")));
        Map<String, Long> result = new Processor().process(stream);

        System.out.println(result);
    }


    @Test
    void test2() {
        String s = "42, and 43, -- and again 42 :)";
        InputStream stream = new ByteArrayInputStream(s.getBytes(Charset.forName("UTF-8")));
        Map<String, Long> result = new Processor().process(stream);

        System.out.println(result);
    }

}