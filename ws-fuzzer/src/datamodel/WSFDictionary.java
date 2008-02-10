/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author chang
 */
public class WSFDictionary {

    private String name;
    private String path;
    private ArrayList<String> words;
    
    public WSFDictionary(String name, String path) throws FileNotFoundException, IOException{
        this.name = name;
        this.path = path;
        this.words = new ArrayList<String>();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        
        String word = br.readLine();
        while(word != null){
            if(!word.equals(""))
                this.words.add(word);
            word = br.readLine();
        }
        br.close();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<String> getWords() {
        return words;
    }
    
    public void print(){
        System.out.println("Words in " + this.name + ": ");
        for(String word : words){
            System.out.println(word);
        }
        System.out.println("\n");
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        String dict1 = "test/dict1.txt";
        String dict2 = "test/dict2.txt";
        
        WSFDictionary d1 = new WSFDictionary("d1", dict1);
        WSFDictionary d2 = new WSFDictionary("d2", dict2);
        
        d1.print();
        d2.print();
    }
}
