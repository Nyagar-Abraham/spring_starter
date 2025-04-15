package org.studyeasy.SpringStarter.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.studyeasy.SpringStarter.models.Account;
import org.studyeasy.SpringStarter.models.Authority;
import org.studyeasy.SpringStarter.models.Post;
import org.studyeasy.SpringStarter.services.AccountService;
import org.studyeasy.SpringStarter.services.AuthorityService;
import org.studyeasy.SpringStarter.services.PostService;
import org.studyeasy.SpringStarter.utils.constants.Privilages;
import org.studyeasy.SpringStarter.utils.constants.Roles;

@Component
public class SeedData implements CommandLineRunner {
  @Autowired
  private PostService postService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private AuthorityService authorityService;


  @Override
  public void run(String... args) throws Exception {

    for(Privilages auth: Privilages.values()){
      Authority authority = new Authority();
      authority.setId(auth.getId());
      authority.setName(auth.getPrivilage());

      authorityService.save(authority);
    }

    Account account01 = new Account();
    Account account02 = new Account();
    Account account03 = new Account();
    Account account04 = new Account();

    account01.setEmail("account01@studyeasy.org");
    account01.setPassword("password");
    account01.setFirstname("user");
    account01.setLastname("lastname");
    account01.setAge(25);
    account01.setDate_of_birth(LocalDate.parse("2000-01-01"));
    account01.setGender("Male");

    account02.setEmail("nyagar76@gmail.com");
    account02.setPassword("password");
    account02.setFirstname("admin");
    account02.setLastname("lastname");
    account02.setAge(28);
    account02.setDate_of_birth(LocalDate.parse("1995-01-01"));
    account02.setGender("Female");
    account02.setRole(Roles.ADMIN.getRole());

    account03.setEmail("account03@studyeasy.org");
    account03.setPassword("password");
    account03.setFirstname("editor");
    account03.setLastname("lastname");
    account03.setAge(20);
    account03.setDate_of_birth(LocalDate.parse("2003-01-01"));
    account03.setGender("Male");
    account03.setRole(Roles.EDITOR.getRole());

    account04.setEmail("account04@studyeasy.org");
    account04.setPassword("password");
    account04.setFirstname("super_editor");
    account04.setLastname("lastname");
    account04.setAge(30);
    account04.setDate_of_birth(LocalDate.parse("2002-01-01"));
    account04.setGender("Female");
    account04.setRole(Roles.EDITOR.getRole());


    Set<Authority> authorities = new HashSet<>();
    authorityService.findById(Privilages.ACCESS_TO_ADMIN_PANEL.getId()).ifPresent(authorities::add);

    // account02.setAuthorities(authorities);

    authorityService.findById(Privilages.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
   
    account04.setAuthorities(authorities);
  


    accountService.save(account01);
    accountService.save(account02);
    accountService.save(account03);
    accountService.save(account04);

    List<Post> posts = postService.findAll();

    if (posts.size() == 0) {
      Post post01 = new Post();
      Post post02 = new Post();
      Post post03 = new Post();
      Post post04 = new Post();
      Post post05 = new Post();
      Post post06 = new Post();

      post01.setTitle("Post 01");
      post01.setBody("post 01 body.....");
      post01.setAccount(account01);

      post02.setTitle("Post 02");
      post02.setBody("post 02 body.....");
      post02.setAccount(account02);

      post03.setTitle("Post 03");
      post03.setBody("post 03 body.....");
      post03.setAccount(account03);

      post04.setTitle("Post 04");
      post04.setBody("post 04 body.....");
      post04.setAccount(account04);

      post05.setTitle("Post 05");
      post05.setBody("post 05 body.....");
      post05.setAccount(account02);

      post06.setTitle("Post 06");
      post06.setBody("post 06 body.....");
      post06.setAccount(account04);

      postService.save(post01);
      postService.save(post02);
    }
  }

}
