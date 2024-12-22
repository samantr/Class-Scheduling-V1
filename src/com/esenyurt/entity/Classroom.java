package com.esenyurt.entity;

public class Classroom {
    public int id;
    public String classRoomName;
    public String classRoomType;
    public boolean isLab;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public String getClassRoomType() {
        return classRoomType;
    }

    public void setClassRoomType(String classRoomType) {
        this.classRoomType = classRoomType;
    }

    public boolean isLab() {
        return isLab;
    }

    public void setLab(boolean lab) {
        isLab = lab;
    }
}
