package controller;

import model.Friend;

public class WebServer {

    private Friend member[] = new Friend[100];

    private int memberCount;
    private String charRooomId;


    public int getMemberCount() {
        return memberCount;
    }

    public boolean memberIsAvailable() {

        return true;
    }

    public Friend member(){
        Friend returnFriend = new Friend();


        return returnFriend;
    }

    public WebServer(String chatRoomId){
        this.charRooomId = chatRoomId;

    }

}
