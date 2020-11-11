package sample;

import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadServer implements  Runnable{
    Thread thr;
    NetworkUtil networkUtil;
    public static HashMap<String, NetworkUtil> map = new HashMap<>();           // < name, networkUtil >
    public static HashMap<String , Boolean> loggedIn = new HashMap<>();         // < name, true/false >
    public static Boolean garbage=true;

    public ReadServer(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = networkUtil.read();
                if(o instanceof String) {
                    String text = (String) o;
                    String[] splitText = text.split("#");
                    if (splitText[0].equals("C")) {
                        // C#name#email#pass
                        try {
                            boolean flag = true;
                            BufferedReader bufferedReader = new BufferedReader(new FileReader("users_info.txt"));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                String[] textFromFile = line.split(" ");
                                if (splitText[1].equals(textFromFile[0]) && splitText[2].equals(textFromFile[1])) {
                                    flag = false;
                                    networkUtil.write("Account exists");
                                    break;
                                } else if (splitText[1].equals(textFromFile[0])) {
                                    flag = false;
                                    networkUtil.write("Username taken");
                                    break;
                                } else if (splitText[2].equals(textFromFile[1])) {
                                    flag = false;
                                    networkUtil.write("Email taken");
                                    break;
                                }
                            }
                            if (flag) {
                                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("users_info.txt", true));
                                bufferedWriter.write(splitText[1] + " " + splitText[2] + " " + splitText[3] + "\n");
                                bufferedWriter.close();
                                networkUtil.write("SUCCESS");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (splitText[0].equals("L")) {
                        //L#name#pass
                        try {
                            BufferedReader bufferedReader = new BufferedReader(new FileReader("users_info.txt"));
                            String line;
                            boolean flag = false;
                            while ((line = bufferedReader.readLine()) != null) {
                                String[] textFromFile = line.split(" ");
                                if (splitText[1].equals(textFromFile[0]) && splitText[2].equals(textFromFile[2])) {
                                    if (loggedIn.containsKey(splitText[1])) {
                                        if(!loggedIn.get(splitText[1])) {
                                            map.put(splitText[1], networkUtil);
                                            loggedIn.put(splitText[1], true);
                                            networkUtil.write("SUCCESS");
                                            flag = true;
                                        }else {
                                            networkUtil.write("ALREADY LOGGED IN");
                                        }
                                    } else {
                                        map.put(splitText[1], networkUtil);
                                        loggedIn.put(splitText[1], true);
                                        networkUtil.write("SUCCESS");
                                        flag = true;
                                    }
                                }
                            }
                            if (!flag) {
                                networkUtil.write("FAILURE");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if(splitText[0].equals("Logout")) {
                        String sender = "";
                        for(String name : loggedIn.keySet()) {
                            if (networkUtil.equals(map.get(name))) {
                                sender = name;
                                break;
                            }
                        }
                        loggedIn.put(sender,false);
                    }else if(splitText[0].equals("P")){
                        ArrayList<String> myList = new ArrayList<>(loggedIn.keySet());
                        networkUtil.write(myList);
                    }else if(splitText[0].equals("M")){
                        //M#receiver's name#text
                        String sender = "";
                        for(String name : loggedIn.keySet()) {
                            if (networkUtil.equals(map.get(name))) {
                                sender = name;
                                break;
                            }
                        }
                        if (splitText[1].equals("Group")) {
                            for(String receiver : loggedIn.keySet()) {
                                if (!map.get(receiver).equals(networkUtil)) {
                                    map.get(receiver).write(sender + " : " + splitText[2] + "\n");
                                }
                            }
                            networkUtil.write("Me : " + splitText[2] + "\n");
                        }else{
                            if(splitText[1].equals(sender)) {
                                networkUtil.write("Me : " + splitText[2] + "\n");
                            }else {
                                for (String receiver : loggedIn.keySet()) {
                                    if (splitText[1].equals(receiver)) {
                                        map.get(receiver).write(sender + " : " + splitText[2] + "\n");
                                        networkUtil.write("Me : " + splitText[2] + "\n");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }else if(o instanceof Boolean){
                    Boolean b = (Boolean)o;
                    if(b.equals(false)) {
                        networkUtil.write(garbage);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            networkUtil.closeConnection();
        }
    }
}
