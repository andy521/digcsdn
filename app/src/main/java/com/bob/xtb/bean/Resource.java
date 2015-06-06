package com.bob.xtb.bean;

/**
 * Created by bob on 15-5-11.
 * 资源实体类
 */
public class Resource {

    private long id;
    private String resId;//资源id
    private String resName;//资源名称
    private String resType;//资源类型
    private String resFrom;//源机器
    private String fromPath;//源路径
    private String resTo;//目的机器
    private String toPath;//目的路径
    private String createTime;//创建时间

    public Resource() {
    }

    public Resource(String resId, String resType, String resFrom, String createTime, String resTo, String toPath, String fromPath, String resName) {
        this.resId = resId;
        this.resType = resType;
        this.resFrom = resFrom;
        this.createTime = createTime;
        this.resTo = resTo;
        this.toPath = toPath;
        this.fromPath = fromPath;
        this.resName = resName;
    }

    public static final String ID= "_id";//与数据表所对应的几个字段
    public static final String RES_ID= "resId";
    public static final String RES_NAME= "resName";
    public static final String RES_TYPE= "resType";
    public static final String RES_FROM= "resFrom";
    public static final String FROM_PATH= "fromPath";
    public static final String RES_TO= "resTo";
    public static final String TO_PATH= "toPath";
    public static final String CREATE_TIME= "createTime";

    public String getResFrom() {
        return resFrom;
    }

    public void setResFrom(String resFrom) {
        this.resFrom = resFrom;
    }

    public String getFromPath() {
        return fromPath;
    }

    public void setFromPath(String fromPath) {
        this.fromPath = fromPath;
    }

    public String getResTo() {
        return resTo;
    }

    public void setResTo(String resTo) {
        this.resTo = resTo;
    }

    public String getToPath() {
        return toPath;
    }

    public void setToPath(String toPath) {
        this.toPath = toPath;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }
}
