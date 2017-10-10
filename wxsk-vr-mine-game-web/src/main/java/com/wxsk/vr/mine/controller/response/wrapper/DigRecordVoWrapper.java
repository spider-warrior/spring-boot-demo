package com.wxsk.vr.mine.controller.response.wrapper;


import com.wxsk.vr.mine.common.util.DateUtil;
import com.wxsk.vr.mine.controller.response.vo.DigRecordVo;
import com.wxsk.vr.mine.controller.response.vo.constants.VoType;
import com.wxsk.vr.mine.model.DigRecord;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DigRecordVoWrapper {

    public DigRecordVo buildDigRecordVo(DigRecord digRecord) {
        if (digRecord == null) {
            return null;
        }
        DigRecordVo digRecordVo = new DigRecordVo();
        digRecordVo.setStructureType(VoType.DIG_RECORD.getValue());
        digRecordVo.setStartTimeStr(new Date(digRecord.getStartTime()));
        digRecordVo.setEndTimeStr(new Date(digRecord.getEndTime()));
        digRecordVo.setStartTime(digRecord.getStartTime());
        digRecordVo.setEndTime(digRecord.getEndTime());
        digRecordVo.setTotalTime(digRecord.getEndTime() - digRecord.getStartTime());
        digRecordVo.setTotalTimeStr(DateUtil.getDistance(digRecord.getEndTime() - digRecord.getStartTime()));
        return digRecordVo;
    }

}
