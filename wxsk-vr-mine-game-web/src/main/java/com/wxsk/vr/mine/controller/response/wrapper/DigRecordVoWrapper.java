package com.wxsk.vr.mine.controller.response.wrapper;


import com.wxsk.vr.mine.common.util.DateUtil;
import com.wxsk.vr.mine.controller.response.vo.DigRecordVo;
import com.wxsk.vr.mine.model.DigRecord;
import org.springframework.stereotype.Service;

@Service
public class DigRecordVoWrapper {

    private static final DigRecordVo empty = new DigRecordVo();

    public DigRecordVo buildDigRecordVo(DigRecord digRecord) {
        if (digRecord == null) {
            return empty;
        }
        DigRecordVo digRecordVo = new DigRecordVo();
        digRecordVo.setEndTime(digRecord.getEndTime());
        digRecordVo.setTotalTime(digRecord.getTotalDigTime());
        digRecordVo.setTotalTimeStr(DateUtil.getDistance(digRecord.getTotalDigTime()));
        return digRecordVo;
    }

}
