package ru.job4j.pooh;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp response = null;
        if ("GET".equals(req.httpRequestType())) {
            map.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            map.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String text = map.get(req.getSourceName()).get(req.getParam()).poll();
            if (text == null || "".equals(text)) {
                response = new Resp("204");
            } else {
                response = new Resp(text, "200");
            }
        } else if ("POST".equals(req.httpRequestType())) {
            for (String key : map.keySet()) {
                if (Objects.equals(req.getSourceName(), key)) {
                    for (ConcurrentLinkedQueue<String> queue : map.get(key).values()) {
                        queue.add(req.getParam());
                        response = new Resp("200");
                    }
                }
            }
            if (response == null) {
                response = new Resp("204");
            }
        } else {
            response = new Resp("400");
        }
        return response;
    }
}