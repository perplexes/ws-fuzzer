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
    
    public final static int INPUT_FROM_FIXED_VALUE = 0;
    public final static int INPUT_FROM_DICTIONARY = 1;
	
    private int inputSourceType;
    private boolean isEnd;

    private String defaultValue;

    private String dictionaryName;
    private ArrayList<String> words;
    
    private int counter;
    
    private WSFInputSource(){
        super();
    }
    
    public int getInputSourceType(){
        return this.inputSourceType;
    }
    
    public static WSFInputSource createSourceFromDefaultValue(String defaultValue){
		
        WSFInputSource is = new WSFInputSource();

        is.inputSourceType = WSFInputSource.INPUT_FROM_FIXED_VALUE;
        is.defaultValue = defaultValue;

        is.isEnd = false;
        return is;
    }
    
    public static WSFInputSource createInputSourceFromDictionary(WSFDictionary dictionary){

        WSFInputSource is = new WSFInputSource();

        is.dictionaryName = dictionary.getName();
        is.inputSourceType = WSFInputSource.INPUT_FROM_DICTIONARY;
        is.words = dictionary.getWords();
        is.counter = 0;

        is.isEnd = false;

        return is;
    }
    
    public String getNextValue(){
		
        switch(inputSourceType){

            case WSFInputSource.INPUT_FROM_FIXED_VALUE: 
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
    
    @Override
    public String toString(){
        if(this.inputSourceType == WSFInputSource.INPUT_FROM_FIXED_VALUE)
            return this.defaultValue;
        
        if(this.inputSourceType == WSFInputSource.INPUT_FROM_DICTIONARY)
            return this.dictionaryName;
        
        return null;
    }
    
    public void reset(){
        this.counter = 0;
        this.isEnd = false;
    }
}
