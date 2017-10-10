package com.wxsk.vr.mine.properties;

import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.LandAreaType;
import com.wxsk.vr.mine.model.ReceiveEnergy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ConfigurationProperties(prefix = "game")
public class GameProperties {

    private static final Logger logger = LogManager.getLogger(GameProperties.class);

    /**
     * 体力配置
     */
    private EnergyConfig energyConfig;

    /**
     * 奖励类型列表
     */
    private List<AwardType> awardTypeList = new ArrayList<>();

    /**
     * 地块类型列表
     */
    private LandAreaConfig landAreaConfig;

    /**
     * 无限币配置
     */
    private VrCoinConfig vrCoinConfig;

    /**
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

    public LandAreaConfig getLandAreaConfig() {
        return landAreaConfig;
    }

    public void setLandAreaConfig(LandAreaConfig landAreaConfig) {
        this.landAreaConfig = landAreaConfig;
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
        if (landAreaConfig != null) {
            List<List<LandAreaType>> landAreaPools = landAreaConfig.getLandAreaPools();
            if (landAreaPools == null) {
                landAreaPools = new ArrayList<>();
                landAreaConfig.setLandAreaPools(landAreaPools);
            }
            for (LandAreaTypeConfig landAreaTypeConfig: landAreaConfig.getLandAreaTypeConfigList()) {
                List<Integer> landAreaTypeCount = landAreaTypeConfig.getTotalList();
                if (landAreaTypeCount != null && landAreaTypeCount.size() > 0) {
                    for (int i=0; i<landAreaTypeCount.size(); i++) {
                        Integer count = landAreaTypeCount.get(i);
                        if (landAreaPools.size() < i+1) {
                            landAreaPools.add(i, new ArrayList<>(count));
                        }
                        List<LandAreaType> landAreaTypeList = landAreaPools.get(i);
                        for (int k=0; k<count; k++) {
                            landAreaTypeList.add(landAreaTypeConfig.getLandAreaType());
                        }
                    }
                }
            }
        }

        logger.info("=========================================== [game config list] ===========================================");
        if (energyConfig != null) {
            energyConfig.printConfigInfo();
        }
        if (landAreaConfig != null) {
            landAreaConfig.printConfigInfo();
        }
        if (vrCoinConfig != null) {
            vrCoinConfig.printConfigInfo();
        }
    }

    public static class LandAreaConfig implements ConfigInfo {

        /**
         * 地块类型配置列表
         */
        private List<LandAreaTypeConfig> landAreaTypeConfigList;
        /**
         * 单页地块数量
         */
        private int sizeOfPageLandArea;

        /**
         * 地块随机池
         */
        private List<List<LandAreaType>> landAreaPools = new ArrayList<>();


        public List<LandAreaTypeConfig> getLandAreaTypeConfigList() {
            return landAreaTypeConfigList;
        }

        public void setLandAreaTypeConfigList(List<LandAreaTypeConfig> landAreaTypeConfigList) {
            this.landAreaTypeConfigList = landAreaTypeConfigList;
        }

        public int getSizeOfPageLandArea() {
            return sizeOfPageLandArea;
        }

        public void setSizeOfPageLandArea(int sizeOfPageLandArea) {
            this.sizeOfPageLandArea = sizeOfPageLandArea;
        }

        public List<List<LandAreaType>> getLandAreaPools() {
            return landAreaPools;
        }

        public void setLandAreaPools(List<List<LandAreaType>> landAreaPools) {
            this.landAreaPools = landAreaPools;
        }

        @Override
        public void printConfigInfo() {
            logger.info("=========================================== land-area-config ===========================================");
            logger.info("单页地块数量： {}", sizeOfPageLandArea);
            logger.info("地块类型配置列表: ");
            if (landAreaTypeConfigList != null && landAreaTypeConfigList.size() > 0) {
                for (int i=0; i<landAreaTypeConfigList.size(); i++) {
                    landAreaTypeConfigList.get(i).printConfigInfo();
                }
            }
            logger.info("地块随机池: ");
            if (landAreaPools != null && landAreaPools.size() > 0) {
                for (int i=0; i<landAreaPools.size(); i++) {
                    logger.info("level: {}, 数量: {}", i, landAreaPools.get(i).size());
                }
            }
        }
    }

    public static class LandAreaTypeConfig implements ConfigInfo {

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

        @Override
        public void printConfigInfo() {
            logger.info("=========================================== land-area-type-config ===========================================");
            logger.info("land area type: {}", landAreaType);
            logger.info("每级地块配比数量: {}", totalList);
        }
    }

    public static class EnergyConfig implements ConfigInfo {

        /**
         * 体力上限
         */
        private int max;

        /**
         * 回复单位体力所需时间
         */
        private int timeInSecondIncreaseUnitEnergy;

        /**
         * 用户每天可购买体力最大值
         */
        private int userBuyMaxEnergyEachDay;

        /**
         * 每时间段领取体力最大值
         * */
        private int maxReceiveEnergyInEachTimeRange;

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

        public int getUserBuyMaxEnergyEachDay() {
            return userBuyMaxEnergyEachDay;
        }

        public void setUserBuyMaxEnergyEachDay(int userBuyMaxEnergyEachDay) {
            this.userBuyMaxEnergyEachDay = userBuyMaxEnergyEachDay;
        }

        public int getMaxReceiveEnergyInEachTimeRange() {
            return maxReceiveEnergyInEachTimeRange;
        }

        public void setMaxReceiveEnergyInEachTimeRange(int maxReceiveEnergyInEachTimeRange) {
            this.maxReceiveEnergyInEachTimeRange = maxReceiveEnergyInEachTimeRange;
        }

        @Override
        public void printConfigInfo() {
            logger.info("=========================================== energy-config ===========================================");
            logger.info("体力上限: {}", max);
            logger.info("单位体力回复时间: {}, 单位: 秒", timeInSecondIncreaseUnitEnergy);
            logger.info("用户每天购买体力上限: {}", userBuyMaxEnergyEachDay);
            logger.info("用户每个时间段领取体力上限: {}", maxReceiveEnergyInEachTimeRange);
        }
    }

    public static class VrCoinConfig implements ConfigInfo {

        /**
         * 无限币上限
         */
        private int max;

        /**
         * 无限币掉落天基数
         */
        private int baseDayCount;

        /**
         * 玩家基数
         */
        private int basePlayerCount;

        /**
         * 每日平均无限币产量
         */
        private int averageByDay;

        /**
         * 开放日期
         */
        private long startTime;

        /**
         * VR币配置
         * subtype <-----> 随机可选项
         */
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

        @Override
        public void printConfigInfo() {
            logger.info("=========================================== vr-coin-config ===========================================");
            logger.info("无限币上限: {}, 单位: 分", max);
            logger.info("无限币掉落天基数: {}天", baseDayCount);
            logger.info("玩家基数: {}天", basePlayerCount);
            logger.info("每日平均无限币产量: {}", averageByDay);
            logger.info("开服时间: {}", new Date(startTime));
            logger.info("VR币配置:");
            if (subtypeRandomConfig != null && subtypeRandomConfig.size() > 0) {
                for (int i=0; i<subtypeRandomConfig.size(); i++) {
                    logger.info("无限币level: {}, 产出随机配置: {}, 单位: 分", i, subtypeRandomConfig.get(i));
                }
            }
        }
    }

    interface ConfigInfo {
       void printConfigInfo();
    }

}
