package org.studyeasy.SpringStarter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.studyeasy.SpringStarter.models.Account;
import org.studyeasy.SpringStarter.models.Authority;
import org.studyeasy.SpringStarter.repositories.AccountRepository;
import org.studyeasy.SpringStarter.utils.constants.Roles;

@Service
public class AccountService implements UserDetailsService{
//  readinf from application.properties
  @Value("${spring.mvc.static-path-pattern}")
  private String photo_prefix;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  public Account save(Account account) {
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    if(account.getRole() == null){
      account.setRole(Roles.USER.getRole());
    }

    if(account.getPhoto() == null){
      String path =photo_prefix.replace("**","images/user-photo.png" ) ;

      System.out.println("path =" + path);
      account.setPhoto(path);
    }
    
    return accountRepository.save(account);
  }


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(email);

    if(!optionalAccount.isPresent()){
      throw new UsernameNotFoundException("Account not found" );
    }

    System.out.println("optionalAccount"+optionalAccount);
    Account account = optionalAccount.get();

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(account.getRole()));

    for(Authority _auth: account.getAuthorities()){
      grantedAuthorities.add(new SimpleGrantedAuthority(_auth.getName()));
    }


    System.out.println("grantedAuthorities"+grantedAuthorities);
   return new User(account.getEmail(),account.getPassword(),grantedAuthorities);
  }

  public Optional<Account> findOneByEmail(String email){
    return accountRepository.findOneByEmailIgnoreCase(email);
  }

  public Optional<Account> findById(Long id){
    return accountRepository.findById(id);
  }
}
