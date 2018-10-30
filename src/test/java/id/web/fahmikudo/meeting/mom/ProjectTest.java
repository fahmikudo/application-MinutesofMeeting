package id.web.fahmikudo.meeting.mom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import id.web.fahmikudo.meeting.mom.dao.ProjectDao;
import id.web.fahmikudo.meeting.mom.model.Project;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest {

    @Autowired
    private ProjectDao pd;

    @Test
    public void testInsert(){
        Project p = new Project();
        p.setNamaProject("Otoritas Jasa keuangan");

        pd.save(p);
    }



}
