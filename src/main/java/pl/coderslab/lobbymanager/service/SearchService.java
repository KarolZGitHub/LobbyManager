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

    public void sendMailIfFoundRoom(List<Search> searches, Room room) throws MessagingException {
        for (Search search :
                searches) {
            if (room.getName().contains(search.getSearchName())) {
                String user = search.getUser().getUserName();
                String email = search.getUser().getEmail();
                String subject = user + " we have found room for you!";
                String text = "You must check " + room.getName();
                mailService.sendMail(email, subject, text, true);
            }
        }
    }

    public void cleanSearches(List<Search> searches) {
        searches.removeIf(search -> search.getExpires().isAfter(LocalDateTime.now()));
    }
}
