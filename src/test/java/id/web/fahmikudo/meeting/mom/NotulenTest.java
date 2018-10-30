package id.web.fahmikudo.meeting.mom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import id.web.fahmikudo.meeting.mom.dao.NotulenDao;
import id.web.fahmikudo.meeting.mom.model.Notulen;



@RunWith(SpringRunner.class)
@SpringBootTest
public class NotulenTest {

	@Autowired
	private NotulenDao nd;

	@Test
	public void testInsert(){
		Notulen notulen = new Notulen();
		notulen.setNama("Tresna Gumelar");
		notulen.setNoHp("08787212873");
		notulen.setJabatan("Project Coordinator");
		nd.save(notulen);
	}



}
