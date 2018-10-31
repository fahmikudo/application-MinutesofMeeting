package id.web.fahmikudo.meeting.mom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import id.web.fahmikudo.meeting.mom.dao.NotulenDao;
import id.web.fahmikudo.meeting.mom.model.User;



@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

	@Autowired
	private NotulenDao nd;

	@Test
	public void testInsert(){
		User user = new User();
		user.setNama("Tresna Gumelar");
		user.setNoHp("08787212873");
		user.setJabatan("Project Coordinator");
		user.setUsername("kudo");
		user.setPassword("kudo12");
		nd.save(user);
	}



}
