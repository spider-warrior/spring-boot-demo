import com.mongodb.WriteResult;
import com.wxsk.vr.mine.MineGame;
import com.wxsk.vr.mine.model.Book;
import com.wxsk.vr.mine.model.Student;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MineGame.class})
public class MongoDbTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsert(){
        Student s = new Student();
        s.setName("xiaoming");
        s.setAge(18);
        s.setBirthday(new Date());
        mongoTemplate.save(s);
        System.out.println(s);
    }

    @Test
    public void testQueryList() {
        List<Student> studentList = mongoTemplate.findAll(Student.class);
        System.out.println(studentList);
    }

    @Test
    public void testQueryById() {
        ObjectId id = new ObjectId("59683bd65f96607181bcdd22");
        Student student = mongoTemplate.findById(id, Student.class);
        System.out.println(student);
    }

    @Test
    public void testQueryByParam() {
        String name = "xiaoming";
        int age = 18;
        Query query = new Query();
        query.addCriteria(new Criteria("name").is(name));
        query.addCriteria(new Criteria("age").is(age));
        List<Student> studentList = mongoTemplate.find(query, Student.class);
        System.out.println(studentList);
    }

    @Test
    public void testUpdate() {
        String nameNew = "xiaoming_new";
        int ageNew = 16;
        ObjectId id = new ObjectId("596601985f96604e64ad7711");
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(id));
        Update update = Update.update("name", nameNew);
        update.set("age", ageNew);
        WriteResult result = mongoTemplate.updateFirst(query, update, Student.class);
        if (result.getN() == 0) {
            System.out.println("update 0");
        }
        else {
            Student student = mongoTemplate.findById(id, Student.class);
            System.out.println(student);
        }
    }

    @Test
    public void testDelete() {
        ObjectId id = new ObjectId("596601985f96604e64ad7711");
        mongoTemplate.remove(new Query(new Criteria("_id").is(id)), Student.class);
        Student student = mongoTemplate.findById(id, Student.class);
        System.out.println(student);
    }

    @Test
    public void testInsertEmbeddedModel() {
        Student s = new Student();
        s.setBookList(new ArrayList<>());
        s.setAge(15);
        s.setName("xiaoming");
        s.setBirthday(new Date());

        Book b1 = new Book();
        b1.setName("语文");
        b1.setType(1);
        mongoTemplate.insert(b1);
        s.getBookList().add(b1);

        Book b2 = new Book();
        b2.setName("数学");
        b2.setType(2);
        mongoTemplate.insert(b2);
        s.getBookList().add(b2);

        Book b3 = new Book();
        b3.setName("英语");
        b3.setType(3);
        mongoTemplate.insert(b3);
        s.getBookList().add(b3);

        mongoTemplate.insert(s);
    }


    @Test
    public void testProjection() {
        Query query = new Query();
        query.fields().include("name").include("birthday");
        List<String> strings = mongoTemplate.find(query, String.class,"student");
        System.out.println(strings);
    }
}
