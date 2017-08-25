import com.wxsk.vr.mine.MineGame;
import com.wxsk.vr.mine.config.GameConfig;
import com.wxsk.vr.mine.model.Student;
import com.wxsk.vr.mine.service.StudentService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineGame.class})
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;
    @Autowired
    private GameConfig gameConfig;

    @Test
    public void testInsert() {
        Student student = new Student();
        student.setName("whj");
        student.setAge(18);
        student.setBirthday(new Date());
        studentService.insert(student);
        System.out.println(student);
    }

    @Test
    public void testQueryById() {
        Student student = studentService.queryById(new ObjectId("596c75955f96605d0e102bb5"));
    }


}
