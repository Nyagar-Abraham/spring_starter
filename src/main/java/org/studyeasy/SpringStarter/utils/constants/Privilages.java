package org.studyeasy.SpringStarter.utils.constants;

public enum Privilages {
  RESET_ANY_USER_PASSWORD(1l,"RESET_ANY_USER_PASSWORD"),
  ACCESS_TO_ADMIN_PANEL(2l,"ACCESS_ADMIN_PANEL");

  private Long id;
  private String privilage;
  private Privilages(Long id,String privilage){
    this.id = id;
    this.privilage = privilage;

  }

  public Long getId(){
    return id;
  }

  public String getPrivilage(){
    return privilage;
  }
}
