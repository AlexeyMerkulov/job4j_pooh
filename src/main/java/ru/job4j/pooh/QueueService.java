package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp response = new Resp("", "501");
        if ("POST".equals(req.httpRequestType())) {
            map.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            map.get(req.getSourceName()).add(req.getParam());
            response = new Resp("", "200");
        } else if ("GET".equals(req.httpRequestType())) {
            ConcurrentLinkedQueue<String> queue = map.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>());
            String text = queue.poll();
            if (text == null || "".equals(text)) {
                response = new Resp("", "204");
            } else {
                response = new Resp(text, "200");
            }
        }
        return response;
    }
}
