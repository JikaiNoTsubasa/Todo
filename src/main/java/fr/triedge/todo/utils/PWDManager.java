package fr.triedge.todo.utils;

import java.util.Base64;

public class PWDManager {

    public String encode(String pwd){
        return new String(Base64.getEncoder().encode(pwd.getBytes()));
    }

    public String decode(String pwd){
        return new String(Base64.getDecoder().decode(pwd));
    }

    public static void main(String[] args) {
        if (args.length > 1){
            PWDManager pwd = new PWDManager();
            if (args[0].equals("-encode")){
                System.out.println(pwd.encode(args[1]));
            }else if(args[0].equals("-decode")){
                System.out.println(pwd.decode(args[1]));
            }
        }
    }
}
