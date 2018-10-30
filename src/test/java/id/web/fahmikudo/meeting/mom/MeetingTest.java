package id.web.fahmikudo.meeting.mom;

import id.web.fahmikudo.meeting.mom.dao.MeetingDao;
import id.web.fahmikudo.meeting.mom.model.Meeting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetingTest {

    @Autowired
    private MeetingDao md;

    @Test
    public void testInsert() {
        Meeting meeting = new Meeting();
        meeting.setAgenda("Dokumen Teknis SIAI");
        meeting.setTanggalWaktu(new Date());
        meeting.setLokasi("Bandung");
        meeting.setKesimpulan("Revisi DokTek Halaman 10");
        md.save(meeting);
    }


}
