package id.web.fahmikudo.meeting.mom.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import id.web.fahmikudo.meeting.mom.model.Meeting;


public interface MeetingRepo extends PagingAndSortingRepository<Meeting, String> {
    Meeting getMeetingById(String id);
}