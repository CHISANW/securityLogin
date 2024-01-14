package login.security.service;

import login.security.Entity.EmailVerified;
import login.security.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    public String generateVerification(String username){
        if(!emailRepository.existsByUsername(username)){
            EmailVerified emailVerification = new EmailVerified(username);
            emailVerification = emailRepository.save(emailVerification);
            return emailVerification.getEmailVerifiedId();
        }
        return getVerificationIdByUsername(username);
    }

    public String getVerificationIdByUsername(String username){
        EmailVerified byUsername = emailRepository.findByUsername(username);
        if(byUsername!=null){
            return byUsername.getEmailVerifiedId();
        }
        return null;
    }

    public String getUsernameForVerificationId(String username) {
        Optional<EmailVerified> verification = emailRepository.findById(username);
        if(verification.isPresent()){
            return verification.get().getUsername();
        }
        return null;
    }
}
