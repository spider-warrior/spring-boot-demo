package com.wxsk.vr.mine.model;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.wxsk.vr.mine.common.constant.enums.ConsumeType;
import com.wxsk.vr.mine.common.constant.enums.LandAreaAwardTypeValue;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Random;

@Document
public class MagicBox extends BaseModel {

	private static final long serialVersionUID = 1L;
	// 用户id
	private Long userId;
	// 创建时间
	private long createTime;
	// 矿区号
	private Integer pageLandAreaIndex; 
	// 地块编号
	private Integer landAreaIndex;
	// 开箱时间
	private long useTime;
	// 开箱货币类型
	private Byte payType;
	// 奖励类型
	private Byte awardType;
	// 奖励数量
	private Integer awardCnt;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Integer getPageLandAreaIndex() {
		return pageLandAreaIndex;
	}

	public void setPageLandAreaIndex(Integer pageLandAreaIndex) {
		this.pageLandAreaIndex = pageLandAreaIndex;
	}

	public Integer getLandAreaIndex() {
		return landAreaIndex;
	}

	public void setLandAreaIndex(Integer landAreaIndex) {
		this.landAreaIndex = landAreaIndex;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	public Byte getAwardType() {
		return awardType;
	}

	public void setAwardType(Byte awardType) {
		this.awardType = awardType;
	}

	public Integer getAwardCnt() {
		return awardCnt;
	}

	public void setAwardCnt(Integer awardCnt) {
		this.awardCnt = awardCnt;
	}

	@Override
	public String toString() {
		return "MagicBox{" +
				"userId=" + userId +
				", createTime=" + createTime +
				", pageLandAreaIndex=" + pageLandAreaIndex +
				", LandAreaIndex=" + landAreaIndex +
				", useTime=" + useTime +
				", payType=" + payType +
				", awardType=" + awardType +
				", awardCnt=" + awardCnt +
				'}';
	}

	/**
	 * 获取随机奖品
	 * @param type 开奖类型 0 金币，1钻石
	 * @return Pair<奖励类型,奖励数量> 
	 * 奖励类型：0体力，1经验，2金币，3钻石，4无限币
	 */
	public static final Pair<LandAreaAwardTypeValue,Integer> randomAward(ConsumeType type){
		int result=awardRandom.nextInt(1000);
		if(type == ConsumeType.GOLD_COIN){
			return goldCoinRangeMap.get(result);
		}else if(type == ConsumeType.DIAMOND){
			return diamondRangeMap.get(result);
		}
		return null;
	}
	
	private static final RangeMap<Integer,Pair<LandAreaAwardTypeValue,Integer>> goldCoinRangeMap = TreeRangeMap.create();
	private static final RangeMap<Integer,Pair<LandAreaAwardTypeValue,Integer>> diamondRangeMap = TreeRangeMap.create();
	private static final Random awardRandom = new Random();
	static{   
		int goldRatio = 0;
		goldCoinRangeMap.put(Range.closedOpen(goldRatio, goldRatio+=200), new ImmutablePair<>(LandAreaAwardTypeValue.ENERGY,20)); // 体力20
		goldCoinRangeMap.put(Range.closedOpen(goldRatio, goldRatio+=100), new ImmutablePair<>(LandAreaAwardTypeValue.ENERGY,50)); // 体力50
		goldCoinRangeMap.put(Range.closedOpen(goldRatio, goldRatio+=100), new ImmutablePair<>(LandAreaAwardTypeValue.EXP,1)); // 经验1
		goldCoinRangeMap.put(Range.closedOpen(goldRatio, goldRatio+=100), new ImmutablePair<>(LandAreaAwardTypeValue.EXP,10)); // 经验10
		goldCoinRangeMap.put(Range.closedOpen(goldRatio, goldRatio+=350), new ImmutablePair<>(LandAreaAwardTypeValue.GOLD_COIN,100)); // 金币100
		goldCoinRangeMap.put(Range.closedOpen(goldRatio, goldRatio+=100), new ImmutablePair<>(LandAreaAwardTypeValue.GOLD_COIN,300)); // 金币300
		goldCoinRangeMap.put(Range.closedOpen(goldRatio, goldRatio+=50), new ImmutablePair<>(LandAreaAwardTypeValue.DIAMOND,20)); // 钻石20

		int diamondRatio = 0;
		diamondRangeMap.put(Range.closedOpen(diamondRatio, diamondRatio+=100), new ImmutablePair<>(LandAreaAwardTypeValue.ENERGY,100)); // 体力100
		diamondRangeMap.put(Range.closedOpen(diamondRatio, diamondRatio+=100), new ImmutablePair<>(LandAreaAwardTypeValue.ENERGY,200)); // 体力200
		diamondRangeMap.put(Range.closedOpen(diamondRatio, diamondRatio+=350), new ImmutablePair<>(LandAreaAwardTypeValue.GOLD_COIN,300)); // 金币300
		diamondRangeMap.put(Range.closedOpen(diamondRatio, diamondRatio+=350), new ImmutablePair<>(LandAreaAwardTypeValue.GOLD_COIN,500)); // 金币500
		diamondRangeMap.put(Range.closedOpen(diamondRatio, diamondRatio+=90), new ImmutablePair<>(LandAreaAwardTypeValue.DIAMOND,100)); // 钻石 100
		diamondRangeMap.put(Range.closedOpen(diamondRatio, diamondRatio+=5), new ImmutablePair<>(LandAreaAwardTypeValue.DIAMOND,300)); // 钻石 300
		diamondRangeMap.put(Range.closedOpen(diamondRatio, diamondRatio+=5), new ImmutablePair<>(LandAreaAwardTypeValue.VR_COIN,10)); // 无限币 10
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < 100; i++){
			Pair p = randomAward(ConsumeType.GOLD_COIN);
			System.out.println(p.getKey() + "," + p.getValue());
		}
	}
}
