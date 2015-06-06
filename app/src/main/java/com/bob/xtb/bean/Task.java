package com.bob.xtb.bean;

/**
 * Created by bob on 15-5-10.
 * 任务实体类
 */
public class Task {
    private long id;
    private String taskId;//任务id
    private String taskName;//任务名称
    private String taskDesc;//任务描述
    private int taskPriority;//任务优先级
    private String taskType;//任务类型
    private int isTimed;//是否为定时任务,0表示否，1肯定
    private String createTime;//创建世间

    public Task() {
    }

    public Task(String taskId, String taskName, String taskDesc, int taskPriority, int isTimed, String taskType, String createTime) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskPriority = taskPriority;
        this.isTimed = isTimed;
        this.taskType = taskType;
        this.createTime = createTime;
    }

    public static final String ID = "_id";//与数据表所对应的几个字段
    public static final String TASK_ID = "taskId";
    public static final String TASK_NAME = "taskName";
    public static final String TASK_DESC = "taskDesc";
    public static final String TASK_PRIORITY = "taskPriority";
    public static final String TASK_TYPE = "taskType";
    public static final String IS_TIMED = "isTimed";
    public static final String CREATE_TIME = "createTime";

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsTimed() {
        return isTimed;
    }

    public void setIsTimed(int isTimed) {
        this.isTimed = isTimed;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
}
