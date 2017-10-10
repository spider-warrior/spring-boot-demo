package com.wxsk.vr.mine.controller.response.vo.constants;

public enum VoType {

    /**
     * 账户
     */
    ACCOUNT("game_account"),

    /**
     * 挖掘记录
     */
    DIG_RECORD("game_dig_record"),

    /**
     * 收益
     */
    HARVEST("game_harvest"),

    /**
     * 地块
     */
    LAND_AREA("game_land_area"),

    /**
     * 页地块
     */
    PAGE_LAND_AREA("game_page_land_area"),

    /**
     * 用户游戏数据
     */
    USER_GAME_DATA("game_user_game_data"),

    /**
     * 用户游戏buff
     */
    USER_GAME_BUFF("game_user_game_buff");

    VoType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "VoType{" +
                "value='" + value + '\'' +
                '}';
    }
}
