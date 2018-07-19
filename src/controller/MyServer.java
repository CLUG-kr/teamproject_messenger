package controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

public class MyServer {
    private String id;
    String sUrl = "http://192.168.43.206:12345/getServer";
    URL url = new URL(sUrl);

    JSONParser parser = new JSONParser();
    Iterator<String> roomName;
    Iterator<String> roomId;
    HttpURLConnection conn1;
    public MyServer(String id) throws IOException {
        System.out.println("asdfasdf");
        conn1 = (HttpURLConnection) url.openConnection();
        conn1.setDoOutput(true);
        conn1.setRequestMethod("POST"); // 보내는 타입
        conn1.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
        System.out.println(conn1.getURL());
        this.id = id;
    }

    public void initialize()  throws IOException, ParseException{

        JSONObject object = new JSONObject();
        // 데이터
        object.put("username",id);

        String param = object.toString();
        // 전송
        OutputStreamWriter osw = new OutputStreamWriter(
                conn1.getOutputStream());

        try {

            osw.write(param);
            osw.flush();


            // 응답
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn1.getInputStream(), "UTF-8"));

            String line = null;
            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray arrayRoomName = (JSONArray) jsonObject.get("serverName");
                roomName = arrayRoomName.iterator();
                JSONArray arrayRoomId = (JSONArray) jsonObject.get("serverCode");
                roomId = arrayRoomId.iterator();

                osw.close();
                br.close();
                conn1.disconnect();


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


    }

/*


        JSONParser parser = new JSONParser();
        JSONObject pass = new JSONObject();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST"); // 보내는 타입
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");

        JSONObject id = new JSONObject();
        id.put("username",id);
        // 데이터
        String param = id.toString();
        // 전송
        OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream());


        try {
            osw.write(param);
            osw.flush();

            String line = null;
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            if ((line = br.readLine()) != null) {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                //JSONArray arrayRoomName = (JSONArray) jsonObject.get("serverName");
                //roomName = arrayRoomName.iterator();
                //JSONArray arrayRoomId = (JSONArray) jsonObject.get("serverName");
                //roomId = arrayRoomId.iterator();
            }



            // 닫기

            osw.close();
          //  br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */



    public String getRoomId() {

        return roomId.next();
    }

    public boolean isAvailable() {
        if(roomId.hasNext()){
            return true;
        }else
        return false;
    }

    public String getRoomName() {
        return roomName.next();
    }

    //key:serverName



}
