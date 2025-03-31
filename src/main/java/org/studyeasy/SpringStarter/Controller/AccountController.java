package org.studyeasy.SpringStarter.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.studyeasy.SpringStarter.models.Account;
import org.studyeasy.SpringStarter.services.AccountService;
import org.studyeasy.SpringStarter.utils.AppUtil;

import jakarta.validation.Valid;

@Controller
public class AccountController {

@Value("${spring.mvc.static-path-pattern}")
private String photo_prefix;

  @Autowired
  private AccountService accountService;

  @GetMapping("/register")
  public String register(Model model) {
      System.out.println("✅ Register page accessed");
      Account account = new Account();
      model.addAttribute("account", account);
      return "account_views/register";
  }
  

  @PostMapping("/register")
  public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {
  if(result.hasErrors()){
    return "account_views/register";
  }

    accountService.save(account);

    return "redirect:/";
  }

  @GetMapping("/login")
  public String login(Model model) {
    return "account_views/login";
  }

  @GetMapping("/profile")
  @PreAuthorize("isAuthenticated()")
  public String profile(Model model, Principal principal) {
   String authUser = "email";

   if(principal != null){
    authUser = principal.getName();
   }

   Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

   if(optionalAccount.isPresent()){
    Account account = optionalAccount.get();
    model.addAttribute("account",account);
    model.addAttribute("photo", account.getPhoto());
    return "account_views/profile";
   }else{
    return "redirect:/?error";
   }

  }


  @PostMapping("/profile")
  @PreAuthorize("isAuthenticated()")
  public String updateProfile(@Valid @ModelAttribute Account account, BindingResult result , Principal principal){
    if(result.hasErrors() ){
      return "account_views/profile";
    }
    String authUser = "email";

    if(principal != null){
      authUser = principal.getName();
    }

    Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

    if(optionalAccount.isPresent()){
      Account account_by_id = accountService.findById(account.getId()).get();
      account_by_id.setEmail(account.getEmail());
      account_by_id.setAge(account.getAge());
      account_by_id.setDate_of_birth(account.getDate_of_birth());
      account_by_id.setFirstname(account.getFirstname());
      account_by_id.setGender(account.getGender());
      account_by_id.setLastname(account.getLastname());
      account_by_id.setPassword(account.getPassword());

      accountService.save(account_by_id);
      SecurityContextHolder.clearContext();
      return "redirect:/";
    }else {
      return "redirect:/?error";
    }

  }


  @PostMapping("/update_photo")
  @PreAuthorize("isAuthenticated()")
  public String update_photo(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Principal principal){
    if(file.isEmpty()){
      attributes.addFlashAttribute("error","No file uploaded");
      return "redirect:/profile";
    }else{
      String fileName = StringUtils.cleanPath(file.getOriginalFilename());
      System.out.println("filename ="+ fileName);

      try {
        int length = 18;
        boolean useletters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(length,useletters,useNumbers);
        System.out.println("generatedString = "+ generatedString);
        String final_photo_name = generatedString + fileName;

        String absolute_fileLocation = AppUtil.getUploadPath(final_photo_name);

        System.out.println("fileLocation = "+ absolute_fileLocation);

        Path path = Paths.get(absolute_fileLocation);
        System.out.println("path = "+path);
        Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);

        attributes.addFlashAttribute("message", "successful upload");

        String authUser = "email";
        if(principal != null){
          authUser = principal.getName();
        }

        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

        if(optionalAccount.isPresent()){
          Account account = optionalAccount.get();
          Account account_by_id = accountService.findById(account.getId()).get();
          String  relative_fileLocation = photo_prefix.replace("**","uploads/"+final_photo_name ) ;
          account_by_id.setPhoto(relative_fileLocation);
          accountService.save(account_by_id);
        }

        try {
          TimeUnit.SECONDS.sleep(1);

        } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        }

        return "redirect:/profile";

      } catch (Exception e) {
        // TODO: handle exception
      }
    }

    return "redirect:/profile/?error";
  }



  @GetMapping("/forgot-password")
  public String forgotPassword(Model model){
    return "account_views/forgot_password";
  }

}
