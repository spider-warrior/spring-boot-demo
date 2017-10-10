import com.wxsk.vr.mine.MineGame;
import com.wxsk.vr.mine.dao.MagicBoxDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineGame.class})
public class MagicBoxDaoTest {
    @Autowired
    private MagicBoxDao magicBoxDao;

    @Before
    public void init() {
        System.out.println("==============================================start==============================================");
    }

    @Test
    public void testQueryDigRecordById() {
        MagicBoxDao.MagicBoxParam param = new MagicBoxDao.MagicBoxParam();
        param.setUserId(151L);
        param.setUsed(false);
        long count = magicBoxDao.countMagicBoxByParam(param);
        System.out.println("count magic box: " + count);
    }


    @After
    public void destroy() {
        System.out.println("==============================================end==============================================");
    }
}
