package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户地块
 */
@Document
public class UserLandArea extends BaseModel{

    /**
     * userId
     * */
    @Indexed(sparse = true)
    private Long userId;

    /**
     * 当前挖掘地块
     * */
    private PageLandArea currentLandAreas;

    /**
     * 下次挖掘地块
     * */
    private PageLandArea nextLandAreas;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PageLandArea getCurrentLandAreas() {
        return currentLandAreas;
    }

    public void setCurrentLandAreas(PageLandArea currentLandAreas) {
        this.currentLandAreas = currentLandAreas;
    }

    public PageLandArea getNextLandAreas() {
        return nextLandAreas;
    }

    public void setNextLandAreas(PageLandArea nextLandAreas) {
        this.nextLandAreas = nextLandAreas;
    }
}
