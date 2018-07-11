package model;

public class Friend {



   private String id;
   private String profileImage;

   public Friend(String id){
        this.id = id;
   }

   public Friend(){
       this.id = "000-000-0000";
   }

   public String getId(){

       return id;
   }


}
