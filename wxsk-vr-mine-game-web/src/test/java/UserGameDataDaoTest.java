import com.mongodb.WriteResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.MineGame;
import com.wxsk.vr.mine.dao.UserGameDataDao;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineGame.class})
public class UserGameDataDaoTest {

    @Autowired
    private UserGameDataDao userGameDataDao;

    @Before
    public void init() {
        System.out.println("==============================================start==============================================");
    }

    @Test
    public void testQueryDigRecordById() {
        ObjectId id = new ObjectId("59ba2aa9106cf27261f7f6c6");
        UserGameData userGameData = userGameDataDao.queryById(id);
        System.out.println(userGameData);
    }

    @Test
    public void testQueryByParam() {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(151L);
        List<UserGameData> userGameDataList = userGameDataDao.queryByParam(param);
        System.out.println(userGameDataList);
    }

    @Test
    public void testQueryUserCurrentPageLandArea() {
        User user = new User();
        user.setId(151L);
        PageLandArea pageLandArea = userGameDataDao.queryUserCurrentPageLandArea(user);
        System.out.println(pageLandArea);
    }

    @Test
    public void testQueryUserCurrentLandArea() {
        User user = new User();
        user.setId(151L);
        LandArea landArea = userGameDataDao.queryUserCurrentLandArea(user);
        System.out.println(landArea);
    }

    @Test
    public void testUpdateUserCurrentPageLandArea() {
        User user = new User();
        user.setId(151L);
        PageLandArea pageLandArea = new PageLandArea();
        pageLandArea.setIndex(100);
        pageLandArea.setUserId(151L);
        int landAreaListSize = 3;
        List<LandArea> landAreaList = new ArrayList<>(landAreaListSize);
        for (int i=0; i<landAreaListSize; i++) {
            LandArea landArea = new LandArea();
            landArea.setIndex(100+i);
            landAreaList.add(landArea);
        }
        pageLandArea.setLandAreaList(landAreaList);
        WriteResult result = userGameDataDao.updateUserCurrentPageLandArea(user, pageLandArea);
        System.out.println(result);
    }

    @Test
    public void testUpdateUserCurrentLandArea() {
        User user = new User();
        user.setId(151L);
        LandArea currentLandArea = new LandArea();
        currentLandArea.setIndex(1000);
        currentLandArea.setContainEnergy(1000);
        WriteResult result = userGameDataDao.updateUserCurrentLandArea(user, currentLandArea);
        System.out.println(result);
    }
    @Test
    public void testUpdateUserCurrentPageLandAreaByLandArea() {
        User user = new User();
        user.setId(151L);
        LandArea newLandArea = new LandArea();
        newLandArea.setIndex(100);
        newLandArea.setContainEnergy(999);
        WriteResult result = userGameDataDao.updateUserCurrentPageLandArea(user, newLandArea);
        System.out.println(result);
    }

    @Test
    public void testQueryUserCurrentLandAreaByIndex() {
        User user = new User();
        user.setId(151L);
        LandArea landArea = userGameDataDao.queryUserCurrentPageLandArea(user, 100);
        System.out.println(landArea);
    }


    @After
    public void destroy() {
        System.out.println("==============================================end==============================================");
    }
}
