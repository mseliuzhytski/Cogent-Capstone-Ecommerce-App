package com.cogent.ecommerce.security;

import com.cogent.ecommerce.User.CustomUser;
import com.cogent.ecommerce.User.Role;
import com.cogent.ecommerce.User.UserRepository;
import com.cogent.ecommerce.controller.ContactDTO;
import com.cogent.ecommerce.service.EmailService;
import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

//service to create users for authorization

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountService accountService;

    //boolean since it is just registering not creating a token
    public boolean register(RegisterRequest request, Role role) {
        if(userRepository.existsByUsername(request.getUsername())){
            return false;
        }
        if(userRepository.existsByEmail(request.getEmail())){
            return false;
        }

        var user = CustomUser.builder().email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        boolean isUser = user.getRole().equals(Role.USER);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        Account account = Account.builder().id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isUser(isUser)
                .isAdmin(isAdmin).build();

        Account newAccount = accountService.addAccount(account);
        if (newAccount == null) {
            System.err.println("Could not create account class from CustomUser");
            return false;
        }
//        var jwtToken = jwtService.generateToken(user);

        return true;
    }


    public boolean registerUser(RegisterRequest request) {
        return register(request, Role.USER);
    }

    public boolean registerAdmin(RegisterRequest request) {
        return register(request, Role.ADMIN);
    }

    public TokenResponse authenticate(Authentication auth){

        return new TokenResponse(jwtService.createToken(auth));

    }

    public Optional<CustomUser> getUser(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<CustomUser> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public int getAccountIdFromUsername(String username){
        CustomUser user = userRepository.findByUsername(username).orElse(null);
        if(user==null)  return -1;
        return user.getId();
    }

    public boolean checkIfAdmin(String token){
       String role = jwtService.getRoleFromToken(token);
        //System.out.println("ROLE ::::"+role);
        if(role.equals("ADMIN")){
            return true;
        }
        return false;
    }

    //create reset token and expiry time for 30 mins after calling reset
    public void createResetToken(CustomUser user){
        user.setResetToken(UUID.randomUUID().toString());
        user.setResetExpiryTime(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);
    }
    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    private EmailService emailService;
    //send reset email
    public void createEmail(String emailOfUser,String resetToken){
        SimpleMailMessage passResetEmail = new SimpleMailMessage();
        passResetEmail.setFrom(email);
        passResetEmail.setTo(emailOfUser);
        passResetEmail.setSubject("Password Reset Request");
        passResetEmail.setText("You requested a password reset request! To continue, " +
                "click the link below to change your password: (This link is only valid for 30 minutes!)\n" +
                "http://localhost:4200/resetPassword/"+resetToken+":"+emailOfUser);
        emailService.sendEmail(passResetEmail);
    }

    public boolean changePassword(String token, String email, ResetPasswordRequest resetPasswordRequest) {
        CustomUser userToUpdate = userRepository.findByEmailAndResetToken(email,token).orElse(null);
        if(userToUpdate!=null){
            if(LocalDateTime.now().isAfter(userToUpdate.getResetExpiryTime())){
                return false;
            }else{
                userToUpdate.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
                userToUpdate.setResetToken(null);
                userToUpdate.setResetExpiryTime(null);
                userRepository.save(userToUpdate);
                return true;
            }
        }
        return false;
    }

    public void sendDiscountEmail(Account account){
        SimpleMailMessage discountEmail = new SimpleMailMessage();
        discountEmail.setFrom(email);
        discountEmail.setTo(account.getEmail());
        discountEmail.setSubject("You received a discount!");
        discountEmail.setText("A discount code has been added to your account!\n" +
                "Use the following code on your next visit to get "+account.getDiscount().getDiscountPercent()
                +"% off of your whole order!\n" +
                "Discount Code: "+account.getDiscount().getDiscountCode());
        emailService.sendEmail(discountEmail);


    }


    public boolean sendEmailToManagement(ContactDTO contactDTO) {
        SimpleMailMessage issueEmail = new SimpleMailMessage();
        issueEmail.setFrom(email);
        issueEmail.setTo(email);
        issueEmail.setSubject("Issue: "+contactDTO.getIssueType());
        issueEmail.setText("User Email: "+contactDTO.getEmail()+"\n" +
                "User Name: "+contactDTO.getName()+"\n"
                +"User Message: "+contactDTO.getMessage());
        emailService.sendEmail(issueEmail);
        return true;
    }
}
