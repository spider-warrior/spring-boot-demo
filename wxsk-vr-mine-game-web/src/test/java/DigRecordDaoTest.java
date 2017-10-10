import com.wxsk.vr.mine.MineGame;
import com.wxsk.vr.mine.dao.DigRecordDao;
import com.wxsk.vr.mine.model.DigRecord;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineGame.class})
public class DigRecordDaoTest {

    @Autowired
    private DigRecordDao digRecordDao;

    @Before
    public void init() {
        System.out.println("==============================================start==============================================");
    }

    @Test
    public void testQueryDigRecordById() {
        ObjectId id = new ObjectId("59bb8b685f96603c47ca9ee2");
        DigRecord digRecord = digRecordDao.queryById(id);
        System.out.println(digRecord);
    }

    @Test
    public void testQueryByParam() {
        DigRecordDao.DigRecordParam param = new DigRecordDao.DigRecordParam();
        param.setUserId(151L);
        List<DigRecord> digRecordList = digRecordDao.queryByParam(param);
        System.out.println(digRecordList);
    }

    @After
    public void destroy() {
        System.out.println("==============================================end==============================================");
    }
}
