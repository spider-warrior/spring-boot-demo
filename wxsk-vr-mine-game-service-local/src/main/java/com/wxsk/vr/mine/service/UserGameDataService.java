package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.GameBuff;
import com.wxsk.vr.mine.dao.UserGameDataDao;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.DigRecord;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.UserGameData;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户数据服务
 */
public interface UserGameDataService extends BaseService<UserGameData> {


    /**
     * 查询用户游戏数据
     *
     * @param user 用户
     */
    UserGameData queryUserGameData(User user) throws BusinessException;

    /**
     * 初始化用户数据
     *
     * @param user 用户数据
     */
    void initUserGameData(User user) throws BusinessException;

    /**
     * 获取当前可用体力体力
     */
    int exchangeTimeToEnergy(long startTime, long endTime);

    /**
     * 体力兑换时间
     */
    long exchangeEnergyToTime(int energy);

    /**
     * 用户兑换体力
     */
    void exchangeTimeToEnergy(UserGameData userGameData, long now);

    /**
     * 查询用户当前体力
     */
    int queryUserCurrentEnergy(UserGameData userGameData, long now) throws BusinessException;

    /**
     * 累计地块收益
     * */
    void collectLandAreaAward(UserGameData userGameData, Map<Byte, Long> awardTypeMappingToIncrease, LandArea... landAreas) throws BusinessException;

    /**
     * 请求本次挖矿当前总收益
     * */
    Collection<AwardType> queryDigRecordLandAreaAward(User user, UserGameData userGameData, DigRecord digRecord) throws BusinessException;

    /**
     * 查询当前挖掘地块信息
     * */
    LandArea queryCurrentLandArea(User user, long now) throws BusinessException;

    /**
     * 添加buff
     * */
    void addGameBuff(UserGameData userGameData, GameBuff gameBuff);

    /**
     * 移除buff
     * */
    void removeGameBuff(UserGameData userGameData, GameBuff gameBuff);

    /**
     * 触发火山buff
     *
     * @param user 当前用户
     * @param now 当前时间
     * */
    void triggerVolcanoBuff(User user, long now) throws BusinessException;

    /**
     * 移除水潭buff
     *
     * @param user 当前用户
     * @param now 当前时间
     * */
    void removeSwagBuff(User user, long now) throws BusinessException;

    /**
     * 移除藤蔓buff
     *
     * @param user 当前用户
     * @param now 当前时间
     * */
    void removePlantBuff(User user, long now) throws BusinessException;

    /**
     * 移除老鼠buff
     *
     * @param user 当前用户
     * @param now 当前时间
     * */
    void removeRatBuff(User user, long now) throws BusinessException;

    /**
     * 查询用户buff
     *
     * @param user 当前用户
     * @param now 当前时间
     * */
    List<GameBuff> queryUserGameBuff(User user, long now) throws BusinessException;

    /**
     * 领取每日体力
     * @param user 当前用户
     * @return 领取的体力点
     */
    long receiveEnergyDaily(User user, long now) throws BusinessException, ParseException;

    /**
     * 添加用户能量
     * @param user 用户游据
     * */
    int addEnergy(User user,int energyCount, long now) throws BusinessException;

    /**
     * 更新用户游戏数据
     *
     * @param userGameData 用户游戏数据
     */
    void updateUserGameData(UserGameData userGameData);


    /**
     * 查询用户游戏数据
     *
     * @param userGameDataParam 参数
     */
    List<UserGameData> queryUserGameDataByParam(UserGameDataDao.UserGameDataParam userGameDataParam);


}
