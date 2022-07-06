package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] parts = content.split(System.lineSeparator());
        String[] firstPart = parts[0].split("/");
        if (firstPart[0].startsWith("POST")) {
            return new Req("POST", firstPart[1],
                    firstPart[2].split(" ")[0], parts[parts.length - 1]);
        } else if (firstPart[0].startsWith("GET") && firstPart.length == 5) {
            return new Req("GET", firstPart[1],
                    firstPart[2], firstPart[3].split(" ")[0]);
        } else if (firstPart[0].startsWith("GET")) {
            return new Req("GET", firstPart[1],
                    firstPart[2].split(" ")[0], "");
        } else {
            return new Req(null, null, null, null);
        }
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}