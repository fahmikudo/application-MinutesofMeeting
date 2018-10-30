package id.web.fahmikudo.meeting.mom.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import id.web.fahmikudo.meeting.mom.model.Meeting;


public interface MeetingDao extends PagingAndSortingRepository<Meeting, String> {
    Meeting getMeetingById(String id);
}