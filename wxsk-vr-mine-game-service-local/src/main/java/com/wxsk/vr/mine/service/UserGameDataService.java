package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import org.springframework.data.mongodb.core.query.Criteria;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 用户数据服务
 */
public interface UserGameDataService extends BaseService<UserGameData> {
    /**
     * 消耗体力挖矿
     * 前置逻辑: 判断当前用户是否在进行中
     * 挖矿逻辑:
     *      1.消耗体力   ----> 更新上次消耗体力时间为当前时间
     *      2.挖矿      ---->  根据已生成的矿地标记所有本次可挖掘的狂地
     *      3.记录本次挖掘完毕时间入redis, 记录挖矿结束时间,以及用户ID(该逻辑为手机推送使用)
     *
     * @param user 请求消耗体力挖矿的用户
     * */
    void consumeAllEnergy4Mining(User user) throws BusinessException;

    /**
     * 查询用户游戏数据
     * @param user 用户
     * */
    UserGameData queryUserGameData(User user);

    /**
     * 初始化用户数据
     * @param user 用户数据
     * */
    void initUserGameData(User user);


    /**
     * 更新用户游戏数据
     * @param userGameData 用户游戏数据
     * */
    void updateUserGameData(UserGameData userGameData);

    /**
     * 查询用户能量
     * @param userGameData 用户游戏数据
     * */
    int queryUserEnergy(UserGameData userGameData);

    /**
     * 查询用户挖矿结束时间
     * @param user 用户数据
     * */
    Date queryMineEndTime(User user);
    
    /**
     * 添加用户能量
     * @param user 用户游据
     * */
    int addEnergy(User user,int energyCount);
    
    /**
     * 刷新矿区
     * @param index
     */
    PageLandArea flushArea(User user, int index) throws BusinessException ;
    
    /**
     * 计算刷新次数需要的钻石
     * @param cnt 当前刷新次数
     * @return 所需钻石数
     */
    long reckonDamondsByFlushCnt(int cnt);

    /**
     * 领取每日体力
     * @param user 当前用户 
     * @return 领取的体力点
     * @throws BusinessException 
     * @throws ParseException 
     */
	long receiveEnergyDaily(User user) throws BusinessException, ParseException;

	/**
     * 查询用户游戏数据
     * @param userGameDataParam 参数
     * */
	List<UserGameData> queryUserGameDataByParam(UserGameDataParam userGameDataParam);


	class UserGameDataParam extends BaseParam {

	    /**
         * 挖掘结束时间开始
         * */
	    private Long digEndTimeStart;

        /**
         * 挖掘结束时间结束
         * */
        private Long digEndTimeEnd;


        public Long getDigEndTimeStart() {
            return digEndTimeStart;
        }

        public UserGameDataParam setDigEndTimeStart(Long digEndTimeStart) {
            this.digEndTimeStart = digEndTimeStart;
            return this;
        }

        public Long getDigEndTimeEnd() {
            return digEndTimeEnd;
        }

        public UserGameDataParam setDigEndTimeEnd(Long digEndTimeEnd) {
            this.digEndTimeEnd = digEndTimeEnd;
            return this;
        }

        @Override
        void buildSubCriteria(final Criteria criteria) {
            if (digEndTimeStart != null && digEndTimeEnd != null) {
                criteria.andOperator(Criteria.where("digRecord.endTime").gte(digEndTimeStart), Criteria.where("digRecord.endTime").lte(digEndTimeEnd));
            }
            else if (digEndTimeStart != null) {
                criteria.and("digRecord.endTime").gte(digEndTimeStart);
            }
            else if (digEndTimeEnd != null) {
                criteria.and("digRecord.endTime").lte(digEndTimeEnd);
            }
        }
    }

}
