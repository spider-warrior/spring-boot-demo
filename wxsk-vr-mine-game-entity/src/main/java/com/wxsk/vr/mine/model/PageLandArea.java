package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 页面数据
 */
@CompoundIndexes({
        @CompoundIndex(name = "page_land_area_userId_pageIndex_index", def = "{'userId': 1, 'index': 1}")
})
@Document
public class PageLandArea extends BaseModel implements Iterable<LandArea>{

    /**
     * 用户id
     * */
    private Long userId;
    /**
     * 页面页码
     * */
    private int index;
    
    /**
     * 刷新次数
     * */
    private int flushCnt;
    
	/**
     * 页面数据
     * */
    private List<LandArea> landAreaList = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<LandArea> getLandAreaList() {
        return landAreaList;
    }

    public void setLandAreaList(List<LandArea> landAreaList) {
        this.landAreaList = landAreaList;
    }

    @Override
    public Iterator<LandArea> iterator() {
        return landAreaList.iterator();
    }
    
    /**
     * 获取刷新次数
     * */
    public int getFlushCnt() {
		return flushCnt;
	}
    
    /**
     * 设置刷新次数
     * */
	public void setFlushCnt(int flushCnt) {
		this.flushCnt = flushCnt;
	}

}

