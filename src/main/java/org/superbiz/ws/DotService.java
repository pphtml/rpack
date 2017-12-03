package org.superbiz.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DotService {
    private final int width;
    private final int height;
    private final Random random;
    private final int widthDouble;
    private final int heightDouble;
    private final int colorCount;
    private final int levelCount;

    private final ObjectMapper mapper = new ObjectMapper();


    private Map<String, Dot> dots = new HashMap<>();

    public DotService(int width, int height) {
        this.width = width;
        this.height = height;
        this.widthDouble = width * 2;
        this.heightDouble = height * 2;
        this.colorCount = Dot.Color.values().length;
        this.levelCount = 4;
        this.random = new Random();
    }

    public void generate(int count) {
        for (int i = 0; i < count; i++) {
            final Dot dot = generateDot();
            dots.put(dot.getKey(), dot);
        }
    }

    private Dot generateDot() {
        int x = random.nextInt(widthDouble) - width;
        int y = random.nextInt(heightDouble) - height;
        Dot.Color color = Dot.Color.values()[random.nextInt(colorCount)];
        int level = random.nextInt(levelCount);
        Dot dot = Dot.create(x, y, color, level);
        return dot;
    }

    public static void main(String[] args) throws JsonProcessingException {
        final DotService dotService = new DotService(512, 512);
        dotService.generate(10);
        // System.out.println(dotService.dots);
        String all = dotService.allToJson();
        System.out.println(all);
    }

    private String allToJson() throws JsonProcessingException {
        final Collection<Dot> values = dots.values();
        return mapper.writer().writeValueAsString(values);
    }
}
