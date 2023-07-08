package pl.coderslab.lobbymanager.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.entity.Search;
import pl.coderslab.lobbymanager.repository.SearchRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public void saveSearch(String name, Authentication authentication) {
        Search search = new Search();
        search.setSearchName(name);
        search.setUser(userRepository.findByUserName(authentication.getName()).get());
        search.setCreated(LocalDateTime.now());
        search.setExpires(LocalDateTime.now().plusDays(2));
        searchRepository.save(search);
    }


    public void sendMailIfRoomFound(List<Search> searchList, Room room) {
        for (Search search :
                searchList) {
            String searchName = search.getSearchName();
            String roomName = room.getName();
            if (roomName.contains(searchName)) {
                String to = search.getUser().getEmail();
                String text = "Hello " + search.getUser().getUserName() + " " + "You must check " + room.getName() + ".";
                String subject = search.getUser().getUserName() + " " + "room found!";
                try {
                    mailService.sendMail(to, subject, text, true);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void cleanSearches(List<Search> searches) {
        searches.removeIf(search -> search.getExpires().isBefore(LocalDateTime.now()));
    }
}
