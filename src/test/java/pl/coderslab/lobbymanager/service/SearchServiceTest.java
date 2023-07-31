package pl.coderslab.lobbymanager.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.entity.Search;
import pl.coderslab.lobbymanager.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SearchServiceTest {
    @Mock
    private MailService mailService;
    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void startUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void shouldSendMailIfRoomFound() throws MessagingException {
        Room room = new Room();
        room.setName("CS:GO");

        Search search = new Search();
        search.setSearchName("CS:GO");
        search.setUser(new User());
        search.getUser().setUserName("Karol");
        search.getUser().setEmail("example@gmail.com");

        Search searchSecond = new Search();
        searchSecond.setSearchName("Apex Legends");
        searchSecond.setUser(new User());
        searchSecond.getUser().setUserName("Marek");
        searchSecond.getUser().setEmail("exampleSecond@gmail.com");

        List<Search> searches = new ArrayList<>();
        searches.add(search);
        searches.add(searchSecond);

        //When use method

        searchService.sendMailIfRoomFound(searches, room);

        //Then

        verify(mailService, times(1)).sendMail("example@gmail.com", "Karol room found!",
                "Hello Karol You must check CS:GO.", true);


        verify(mailService, times(0)).sendMail("exampleSecond@gmail.com",
                "Marek we have found room for you!", "Hello Marek You must check Apex Legends.", true);

    }
}