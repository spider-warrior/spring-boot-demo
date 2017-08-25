package com.wxsk.vr.mine.common.constant.enums;

/**
 * service error code
 */
public enum ServiceErrorCode {

    /** 已经在挖矿中  */
    GAME_ALREADY_IN_MINING("GAME_0001", "已经在挖矿中"),
    /**  矿区不存在 */
    GAME_NO_USER_LAND_AREA("GAME_0002", "矿区不存在"),
    /**  矿区不存在 */
    GAME_NO_USER_LAND_AREA_PAGE("GAME_0003", "矿区不存在"),
    /**  矿区刷新次数限制 */
    GAME_PAGE_LAND_AREA_FLUSH_LIMIT("GAME_0004", "矿区刷新次数限制"),
    /**  当前页仍在挖掘中 */
    CURRENT_PAGE_LAND_AREA_ING("GAME_0005", "当前页仍在挖掘中"),
    /**  钻石余额不足 */
    GAME_DIAMONDS_NOT_ENOUGH("GAME_0006", "钻石余额不足"),
	/**  今天购买体力超限 */
    GAME_DAY_BUY_ENERGY_OVERRUN("GAME_0007", "今天购买体力超限"),
	/**  领取每日体力不在时间范围内 */
    GAME_DAILY_RECIEVE_ENERGY_OUT_OF_RANGE("GAME_0008", "领取每日体力不在时间范围内"),
    /**  未知的LandAwardType */
    GAME_UNKNOWN_LAND_AWARD_TYPE("GAME_0009", "未知的LandAwardType"),
    /** 服务器内部异常*/
    GAME_SERVER_INTERNAL_EXCEPTION("GAME_0010", "服务器内部异常"),
    /** 体力不足*/
    ENERGY_NOT_ENOUGH("GAME_0011", "体力不足");

    public String code;

    public String msg;

    ServiceErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
