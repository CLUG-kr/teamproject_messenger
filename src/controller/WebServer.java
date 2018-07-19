package controller;

import model.Friend;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.util.Iterator;

public class WebServer extends Thread{

    private Friend member[] = new Friend[100];

    private int memberCount = 10;
    private String chatRooomId;
    private String userId;
    JSONParser parser = new JSONParser();
    JSONArray arrayMember;
    Iterator<String> memberId;
    private int tempCount = 0;

    BufferedReader reader; // 서버로부터 온 메시지를 읽어드릴 읽기버퍼
    PrintWriter writer; // 메시지를 소켓 스트림에 써줄 쓰기 스트림
    Socket sock; // 소켓




    public WebServer() throws IOException {


    }

    public void chatRoomInitialize() throws IOException {

            loadingMember();

        //    chatServer.run();

    }

    private void loadingMember() throws IOException {
        String sUrl = "http://192.168.43.206:12345/chatServer";
        URL url = new URL(sUrl);
        JSONObject sendJSON = new JSONObject();
        System.out.println(chatRooomId);
        System.out.println(userId);
        sendJSON.put("serverCode",chatRooomId);
        sendJSON.put("username",userId);
        communicateServer(url, sendJSON.toString());
    }

    private void communicateServer(URL url, String sendString) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST"); // 보내는 타입
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");

        OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream());

        try {
            osw.write(sendString);
            osw.flush();

            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                memberCount = Integer.parseInt((String)jsonObject.get("numOfPeople"));
                System.out.println(memberCount);
                arrayMember = (JSONArray) jsonObject.get("username");
                memberId = arrayMember.iterator();
            }

            // 닫기

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public void chatInitialize(){




    }



    public int getMemberCount() {
        return memberCount;
    }

    public boolean memberIsAvailable() {
        return memberId.hasNext();

    }

    public Friend member(){

            Friend returnFriend = new Friend((String)memberId.next());
            System.out.println(returnFriend.getId());
            return returnFriend;

    }



    public String makeChatRoomId(String name) throws IOException {
        String sUrl = "http://192.168.43.206:12345/chatServer";
        URL url = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST"); // 보내는 타입
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");

        JSONObject sendJson = new JSONObject();
        sendJson.put("serverName",name);
        sendJson.put("username",userId);
        String sendString = sendJson.toString();
        OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream());

        try {
            osw.write(sendString);
            osw.flush();

            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                return (String)jsonObject.get("serverCode");
            }

            // 닫기

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setChatRoomId(String s) {
        this.chatRooomId = s;
    }

    @Override
    public void run(){

    }

    public boolean chatIsAvailable() {


        return false;
    }

    public String getChat() throws IOException {
        JSONObject id = new JSONObject();
        id.put("serverCode",chatRooomId);

        String stringChat = new String();
        String sUrl = "http://192.168.43.206:12345/message";
        URL url = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST"); // 보내는 타입
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
        OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream());
        String param = id.toString();
        // 전송
        try {
            osw.write(param);
            osw.flush();

            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                stringChat = (String) jsonObject.get("O");
                System.out.println("readMessage="+stringChat);
                br.close();
                osw.close();
                conn.disconnect();

            }



            // 닫기

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringChat;
    }

    public boolean Login(String username, String password) throws IOException, ParseException {
        JSONObject id = new JSONObject();
        id.put("username",username);
        id.put("password",password);


        // 데이터
        String param = id.toString();
        String sUrl = "http://192.168.43.206:12345/login";
        URL url = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST"); // 보내는 타입
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
        // 전송
        OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream());

        try {
            osw.write(param);
            osw.flush();

            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                String strPass = (String) jsonObject.get("OX");
                if(strPass.equals("O")){
                    osw.close();
                    br.close();
                    conn.disconnect();
                    return true;
                } else {
                    osw.close();
                    br.close();
                    conn.disconnect();
                    return false;
                }
            }

            // 닫기

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }
    public boolean Register(String username, String password) throws IOException, ParseException {
        JSONObject id = new JSONObject();
        id.put("username",username);
        id.put("password",password);
        id.put("name",username);

        JSONParser parser = new JSONParser();
        JSONObject pass = new JSONObject();

        // 데이터
        String param = id.toString();
        String sUrl = "http://192.168.43.206:12345/register";
        URL url = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST"); // 보내는 타입
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
        // 전송
        OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream());

        try {
            osw.write(param);
            osw.flush();

            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                String strPass = (String) jsonObject.get("OX");
                if(strPass.equals("O")){
                    osw.close();
                    br.close();
                    conn.disconnect();
                    return true;
                } else {
                    osw.close();
                    br.close();
                    conn.disconnect();
                    return false;
                }
            }

            // 닫기

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String sendMessage(String text) throws IOException {
        String returnMessage = "";
        JSONObject id = new JSONObject();
        id.put("username",userId);
        id.put("message",text);
        id.put("serverCode",chatRooomId);



        JSONParser parser = new JSONParser();
        JSONObject pass = new JSONObject();

        // 데이터
        String param = id.toString();
        String sUrl = "http://192.168.43.206:12345/message";
        URL url = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST"); // 보내는 타입
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
        // 전송
        OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream());

        try {
            osw.write(param);
            osw.flush();

            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                returnMessage = (String) jsonObject.get("O");
                osw.close();
                br.close();
                conn.disconnect();

            }

            // 닫기

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return returnMessage;

    }
}


   /*
            JSONArray phoneNum = (JSONArray) jsonObject.get("phoneNumbers");
            Iterator<String> iterator = phoneNum.iterator();
            while (iterator.hasNext()) {
                System.out.println("phoneNumbers :: " + iterator.next());
            }
            */