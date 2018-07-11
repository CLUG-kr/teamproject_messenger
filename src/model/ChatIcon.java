package model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URI;
import java.net.URISyntaxException;

public class ChatIcon extends Button {
   Friend friend;

   int centerX = 218;
   int centerY = 185;

    public ChatIcon(Friend friend, String name){
       super(name);
       this.friend = friend;
       this.setId(friend.getId());
       this.setMinSize(30,30);
       this.setMaxSize(30,30);
       this.setPrefSize(30,30);
      // this.setGraphic(new ImageView(img));
   }
   public ChatIcon(Friend friend){
       super();
       this.friend = friend;
       this.setId(friend.getId());
       this.setMinSize(30,30);
       this.setMaxSize(30,30);
       this.setPrefSize(30,30);
   }

   String getFriendId(){
       return friend.getId();
   }




}
