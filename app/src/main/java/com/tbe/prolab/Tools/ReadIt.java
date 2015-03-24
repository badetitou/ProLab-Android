package com.tbe.prolab.Tools;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by badetitou on 24/03/15.
 */
public class ReadIt {

    public static String ReadIt(InputStream is){
        return new Scanner(is,"UTF-8").useDelimiter("\\A").next();
    }
}
