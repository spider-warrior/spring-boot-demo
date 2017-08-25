import com.wxsk.vr.mine.MineGame;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineGame.class})
public class UserGameDataServiceTest {

    @Autowired
    private UserGameDataService userGameDataService;

    @Test
    public void testQueryUserGameDataByParam() {
        UserGameDataService.UserGameDataParam userGameDataParam = new UserGameDataService.UserGameDataParam();
//        userGameDataParam.setId(new ObjectId("5982d3d5106cf25c08cdf51e"));
//        userGameDataParam.setSkip(8);
//        userGameDataParam.setLimit(1);
        long now = System.currentTimeMillis();
        userGameDataParam.setDigEndTimeStart(now - TimeUnit.DAYS.toMillis(10));
//        userGameDataParam.setDigEndTimeEnd(now - TimeUnit.DAYS.toMillis(10));
        System.out.println(userGameDataService.queryUserGameDataByParam(userGameDataParam).size());
    }

}
