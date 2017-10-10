import com.wxsk.vr.mine.MineGame;
import com.wxsk.vr.mine.dao.StudentDao;
import com.wxsk.vr.mine.model.Student;
import com.wxsk.vr.mine.service.StudentService;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineGame.class})
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Before
    public void init() {
        System.out.println("==============================================start==============================================");
    }

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
    public void testDeleteByParam() {
        StudentDao.StudentDaoParam param = new StudentDao.StudentDaoParam();
        param.setName("whj");
        studentService.deleteByParam(param);
    }

    @Test
    public void testCountByParam() {
        StudentDao.StudentDaoParam param = new StudentDao.StudentDaoParam();
        param.setName("whj");
        long count = studentService.countByParam(param);
        System.out.println("count: " + count);
    }

    @Test
    public void testQuery() {
        StudentDao.StudentDaoParam param = new StudentDao.StudentDaoParam();
        param.setName("whj");
        List<Student> studentList = studentService.queryByParam(param);
        System.out.println(studentList);
    }

    @Test
    public void testUpdate() {
        StudentDao.StudentDaoParam param = new StudentDao.StudentDaoParam();
        param.setName("whj");
        List<Student> studentList = studentService.queryByParam(param);
        if (studentList.size() == 0) {
            System.out.println("no db record found");
        }
        else {
            Student student = studentList.get(0);
            student.setAge(10);
            boolean success = studentService.update(student);
            if (success) {
                System.out.println("更新成功");
            }
            else {
                System.out.println("更新失败");
            }
        }
    }

    @Test
    public void testQueryById() {
        Student student = studentService.queryById(new ObjectId("596c75955f96605d0e102bb5"));
    }

    @After
    public void destroy() {
        System.out.println("==============================================end==============================================");
    }

}
