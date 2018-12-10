package id.web.fahmikudo.meeting.mom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import id.web.fahmikudo.meeting.mom.repository.ProjectRepo;
import id.web.fahmikudo.meeting.mom.model.Project;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest {

    @Autowired
    private ProjectRepo pd;

    @Test
    public void testInsert(){
        Project p = new Project();
        p.setNamaProject("Idis");

        pd.save(p);
    }



}
