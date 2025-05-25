package com.example.springboot.entity;

public class Log {
    private Integer id;
    private String time;
    private String name;
    private String type;
    private String username;
    private String ip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Log(Integer id, String time, String name, String username,String type, String ip) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.username = username;
        this.type = type;
        this.ip = ip;
    }
}
