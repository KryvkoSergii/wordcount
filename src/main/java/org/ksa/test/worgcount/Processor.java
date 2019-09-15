package org.ksa.test.worgcount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Processor {

    private final Logger logger = Logger.getLogger(Processor.class.getCanonicalName());

    private final Pattern pattern = Pattern.compile("([\\w\\d\\p{L}']*)");

    public Map<String, Long> process(InputStream stream) {
        return wordsStream(stream)
                .flatMap(line -> StreamSupport.stream(new MatcherSpliterator(pattern.matcher(line)), false))
                .filter(line -> !line.isEmpty())
                .peek(word -> logger.fine(() -> word))
                .map(normalizeSingleWorldF)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private Stream<String> wordsStream(InputStream stream) {
        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        Stream<String> str = new BufferedReader(reader, 1024).lines();
        return Runtime.getRuntime().availableProcessors() > 1 ? str.parallel() : str;
    }

    private Function<String, String> normalizeSingleWorldF = (word) -> {
        String result = word;
        if (word.contains("'")) {
            result = word.replace("'", "");
        }
        return result.toLowerCase();
    };


    private static class MatcherSpliterator extends Spliterators.AbstractSpliterator<String> {
        private final Matcher matcher;

        MatcherSpliterator(Matcher m) {
            super(m.regionEnd() - m.regionStart(), NONNULL);
            matcher = m;
        }

        @Override
        public boolean tryAdvance(Consumer<? super String> action) {
            if (!matcher.find()) return false;
            action.accept(matcher.group());
            return true;
        }

        @Override
        public Spliterator<String> trySplit() {
            return super.trySplit();
        }
    }
}
