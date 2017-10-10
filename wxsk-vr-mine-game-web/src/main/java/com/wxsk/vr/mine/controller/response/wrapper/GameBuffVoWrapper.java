package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.common.constant.enums.GameBuff;
import com.wxsk.vr.mine.controller.response.vo.GameBuffVo;
import com.wxsk.vr.mine.controller.response.vo.constants.VoType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameBuffVoWrapper {

    public GameBuffVo buildGameBuffVo(GameBuff gameBuff) {
        if (gameBuff == null) {
            return null;
        }
        GameBuffVo gameBuffVo = new GameBuffVo();
        gameBuffVo.setStructureType(VoType.USER_GAME_BUFF.getValue());
        gameBuffVo.setType(gameBuff.getType());
        return gameBuffVo;
    }

    public List<GameBuffVo> buildGameBuffVo(List<GameBuff> gameBuffList) {
        if (gameBuffList == null || gameBuffList.isEmpty()) {
            return Collections.emptyList();
        }
        List<GameBuffVo> gameBuffVoList = new ArrayList<>(gameBuffList.size());
        gameBuffList.forEach(buff -> gameBuffVoList.add(buildGameBuffVo(buff)));
        return gameBuffVoList;
    }
}
