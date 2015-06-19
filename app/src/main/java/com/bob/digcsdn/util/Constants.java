package com.bob.digcsdn.util;

/**
 * Created by bob on 15-5-10.
 */
public class Constants {//存放常量的一个类，ps:禁止使用常量接口
    public static final String ALL_BLOG_LIST= "100";

    public class DEF_BLOG_ITEM_TYPE {//博客内容条目信息分类
        public static final int TITLE = 1; // 标题
        public static final int SUMMARY = 2; // 摘要
        public static final int CONTENT = 3; // 内容
        public static final int IMG = 4; // 图片
        public static final int BOLD_TITLE = 5; // 加粗标题
        public static final int CODE = 6; // 代码
    }

    // 评论类型(ListView分类)
    public class DEF_COMMENT_TYPE {
        public static final int PARENT = 0;
        public static final int CHILD = 1;
    }

    public class DEF_ARTICLE_TYPE {//文章模块分类
        public static final int HOME = 0; // 首页
        public static final int DAILY_LIFE = 1; // 生活杂谈
        public static final int ANDROID = 2; // android启蒙
        public static final int JAVA = 3; // java进阶
        public static final int DATA_STRUCTURE = 4; // 数据结构
        public static final int WINDOWS = 5; // windows系统
        public static final int MAC = 6; // mac osx系统
        public static final int LINUX = 7; // linux
        public static final int MASTER = 8; // 硕士研究生
        public static final int PHOTOGRAPH = 9; // 走进摄影
        public static final int JAPANESE = 10; // 日语学习

    }

    public class DEF_TASK_TYPE{//任务类型
        public static final int REFRESH = 0;
        public static final int LOAD = 1;
    }
}
