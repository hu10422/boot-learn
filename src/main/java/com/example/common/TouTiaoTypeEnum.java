package com.example.common;

public enum TouTiaoTypeEnum {

    TOP("top", "推荐,默认"),
    GUONEI("guonei", "国内"),
    GUOJI("guoji", "国际"),
    YULE("yule", "娱乐"),
    TIYU("tiyu", "体育"),
    JUNSHI("junshi", "军事"),
    KEJI("keji", "科技"),
    CAIJING("caijing", "财经"),
    SHISHANG("shishang", "时尚"),
    YOUXI("youxi", "游戏"),
    QICHE("qiche", "汽车"),
    JIANKANG("jiankang", "健康"),
    ;

    private String type;
    private String remark;

    TouTiaoTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
