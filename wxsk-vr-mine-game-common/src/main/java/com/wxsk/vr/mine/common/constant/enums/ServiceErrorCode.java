package com.wxsk.vr.mine.common.constant.enums;

/**
 * service error code
 */
public enum ServiceErrorCode {

    /**
     * 已经在挖矿中
     */
    GAME_ALREADY_IN_MINING("GAME_0001", "已经在挖矿中"),

    /**
     * 金币余额不足
     * */
    GAME_GOLD_COIN_NOT_ENOUGH("GAME_0002", "金币余额不足"),
    /**
     * 矿区页不存在
     */
    GAME_PAGE_LAND_AREA_NOT_FOUND("GAME_0003", "矿区页不存在"),
    /**
     * 钻石余额不足
     */
    GAME_DIAMONDS_NOT_ENOUGH("GAME_0006", "钻石余额不足"),
    /**
     * 今天购买体力超限
     */
    GAME_DAY_BUY_ENERGY_OVERRUN("GAME_0007", "今天购买体力超限"),
    /**
     * 领取每日体力不在时间范围内
     */
    GAME_DAILY_RECIEVE_ENERGY_OUT_OF_RANGE("GAME_0008", "领取每日体力不在时间范围内"),
    /**
     * 未知的LandAwardType
     */
    GAME_UNKNOWN_LAND_AWARD_TYPE("GAME_0009", "未知的LandAwardType"),
    /**
     * 服务器内部异常
     */
    GAME_SERVER_INTERNAL_EXCEPTION("GAME_0010", "服务器内部异常"),
    /**
     * 体力不足
     */
    GAME_ENERGY_NOT_ENOUGH("GAME_0011", "体力不足"),

    /**
     * 客户端非法请求
     */
    GAME_CLIENT_INVILAD_REQUEST("GAME_0012", "客户端非法请求"),

    /**
     * 宝箱数量不足
     */
    GAME_USER_MAGIC_BOX_NOT_ENOUGH("GAME_0013", "宝箱数量不足");

    public String code;

    public String msg;

    ServiceErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
