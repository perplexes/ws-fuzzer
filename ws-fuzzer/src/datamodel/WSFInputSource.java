/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.util.ArrayList;

/**
 *
 * @author chang
 */
public class WSFInputSource {
    
    public final static int INPUT_FROM_DEFAULT_VALUE = 0;
    public final static int INPUT_FROM_DICTIONARY = 1;
	
    private int inputSource;
    private boolean isEnd;

    private String defaultValue;

    private ArrayList<String> words;
    
    private int counter;
    
    private WSFInputSource(){
        super();
    }
    
    public static WSFInputSource createSourceFromDefaultValue(String defaultValue){
		
        WSFInputSource is = new WSFInputSource();

        is.inputSource = WSFInputSource.INPUT_FROM_DEFAULT_VALUE;
        is.defaultValue = defaultValue;

        is.isEnd = false;
        return is;
    }
    
    public static WSFInputSource createInputSourceFromDictionary(WSFDictionary dictionary){

        WSFInputSource is = new WSFInputSource();

        is.inputSource = WSFInputSource.INPUT_FROM_DICTIONARY;
        is.words = dictionary.getWords();
        is.counter = 0;

        is.isEnd = false;

        return is;
    }
    
    public String getNextValue(){
		
        switch(inputSource){

            case WSFInputSource.INPUT_FROM_DEFAULT_VALUE: 
                this.isEnd = true;
                return defaultValue;

            case WSFInputSource.INPUT_FROM_DICTIONARY: 
                if(this.counter < this.words.size()-1){
                    return words.get(this.counter++);
                }else {
                    this.isEnd = true;
                    return words.get(words.size()-1);
                }

            default:
                //TODO: check, if inputSource is false
                return null;
        }
    }
    
    public boolean isEnd(){
        return this.isEnd;
    }
    
}
