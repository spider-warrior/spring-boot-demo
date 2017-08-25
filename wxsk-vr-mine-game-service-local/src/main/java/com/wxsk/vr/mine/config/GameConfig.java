package com.wxsk.vr.mine.config;

import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.LandAreaType;
import com.wxsk.vr.mine.model.ReceiveEnergy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "game")
public class GameConfig {

    private static final Logger logger = LogManager.getLogger(GameConfig.class);

    /**
     * 体力配置
     * */
    private EnergyConfig energyConfig;

    /**
     * 奖励类型列表
     * */
    private List<AwardType> awardTypeList = new ArrayList<>();

    /**
     * 地块类型列表
     * */
    private List<LandAreaTypeConfig> landAreaTypeConfigList = new ArrayList<>();

    /**
     * 无限币配置
     * */
    private VrCoinConfig vrCoinConfig;

    /**
     *
     * 领取每日体力配置
     */
    private List<ReceiveEnergy> receiveEnergyList = new ArrayList<>();
    
    public EnergyConfig getEnergyConfig() {
        return energyConfig;
    }

    public void setEnergyConfig(EnergyConfig energyConfig) {
        this.energyConfig = energyConfig;
    }

    public List<AwardType> getAwardTypeList() {
        return awardTypeList;
    }

    public void setAwardTypeList(List<AwardType> awardTypeList) {
        this.awardTypeList = awardTypeList;
    }

    public List<LandAreaTypeConfig> getLandAreaTypeConfigList() {
        return landAreaTypeConfigList;
    }

    public void setLandAreaTypeConfigList(List<LandAreaTypeConfig> landAreaTypeConfigList) {
        this.landAreaTypeConfigList = landAreaTypeConfigList;
    }

    public VrCoinConfig getVrCoinConfig() {
        return vrCoinConfig;
    }

    public void setVrCoinConfig(VrCoinConfig vrCoinConfig) {
        this.vrCoinConfig = vrCoinConfig;
    }
    
	public List<ReceiveEnergy> getReceiveEnergyList() {
		return receiveEnergyList;
	}

	public void setReceiveEnergyList(List<ReceiveEnergy> receiveEnergyList) {
		this.receiveEnergyList = receiveEnergyList;
	}

	@PostConstruct
    public void init() {

        vrCoinConfig.setAverageByDay(vrCoinConfig.getMax() / vrCoinConfig.getBaseDayCount());
        logger.info("每日产出无限币: {}, 单位: 角", vrCoinConfig.getAverageByDay());
    }

    public static class LandAreaTypeConfig {

        private LandAreaType landAreaType;

        private List<Integer> totalList;

        public LandAreaType getLandAreaType() {
            return landAreaType;
        }

        public void setLandAreaType(LandAreaType landAreaType) {
            this.landAreaType = landAreaType;
        }

        public List<Integer> getTotalList() {
            return totalList;
        }

        public void setTotalList(List<Integer> totalList) {
            this.totalList = totalList;
        }
    }

    public static class EnergyConfig {

        /**
         * 体力上限
         * */
        private int max;

        /**
         * 回复单位体力所需时间
         * */
        private int timeInSecondIncreaseUnitEnergy;
        
        /**
         * 用户每天可购买体力最大值
         * */
        private int userDayBuyMaxEnergy;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getTimeInSecondIncreaseUnitEnergy() {
            return timeInSecondIncreaseUnitEnergy;
        }

        public void setTimeInSecondIncreaseUnitEnergy(int timeInSecondIncreaseUnitEnergy) {
            this.timeInSecondIncreaseUnitEnergy = timeInSecondIncreaseUnitEnergy;
        }

        public int getUserDayBuyMaxEnergy() {
			return userDayBuyMaxEnergy;
		}

		public void setUserDayBuyMaxEnergy(int userDayBuyMaxEnergy) {
			this.userDayBuyMaxEnergy = userDayBuyMaxEnergy;
		}

    }

    public static class VrCoinConfig {

        /**
         * 无限币上限
         * */
        private int max;

        /**
         * 无限币掉落天数基数
         * */
        private int baseDayCount;

        /**
         * 玩家基数
         * */
        private int basePlayerCount;

        /**
         * 开放日期
         * */
        private long startTime;

        /**
         * 每日平均无限币产量
         * */
        private int averageByDay;

        /**
         * VR币配置
         * subtype <-----> 随机可选项
         * */
        public List<List<Integer>> subtypeRandomConfig = new ArrayList<>();

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getBaseDayCount() {
            return baseDayCount;
        }

        public void setBaseDayCount(int baseDayCount) {
            this.baseDayCount = baseDayCount;
        }

        public int getBasePlayerCount() {
            return basePlayerCount;
        }

        public void setBasePlayerCount(int basePlayerCount) {
            this.basePlayerCount = basePlayerCount;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getAverageByDay() {
            return averageByDay;
        }

        public void setAverageByDay(int averageByDay) {
            this.averageByDay = averageByDay;
        }

        public List<List<Integer>> getSubtypeRandomConfig() {
            return subtypeRandomConfig;
        }

        public void setSubtypeRandomConfig(List<List<Integer>> subtypeRandomConfig) {
            this.subtypeRandomConfig = subtypeRandomConfig;
        }
    }

}
