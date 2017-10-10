package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.controller.response.vo.UserGameDataVo;
import com.wxsk.vr.mine.controller.response.vo.constants.VoType;
import com.wxsk.vr.mine.model.UserGameData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGameDataVoWrapper {

    private static final UserGameDataVo empty = new UserGameDataVo();

    @Autowired
    private DigRecordVoWrapper digRecordVoWrapper;
    @Autowired
    private GameBuffVoWrapper gameBuffVoWrapper;

    public UserGameDataVo buildUserGameDataVo(UserGameData userGameData) {
        if (userGameData == null) {
            return empty;
        }
        UserGameDataVo userGameDataVo = new UserGameDataVo();
        userGameDataVo.setStructureType(VoType.USER_GAME_DATA.getValue());
        userGameDataVo.setGameBuffVoList(gameBuffVoWrapper.buildGameBuffVo(userGameData.getBuffs()));
        userGameDataVo.setDigRecordVo(digRecordVoWrapper.buildDigRecordVo(userGameData.getDigRecord()));
        return userGameDataVo;
    }
}
