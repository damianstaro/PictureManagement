package picsmgmt;

import java.util.*;
import java.io.*;

public class Props extends Properties {

    private static final String SEP = System.getProperty("file.separator");

    private static Props instance = new Props();

    public static Props instance() {
	return instance;
    }

    private Props() {
	super();
	doLoad();
    }

    private void doMacDefaults() {
	if (getProperty("inputDir") == null)
	    setProperty("inputDir", System.getProperty("user.home"));
	if (getProperty("outputDir") == null)
	    setProperty("outputDir", System.getProperty("user.home"));
	if (getProperty("imageMagick") == null)
	    setProperty("imageMagick", "/usr/local/bin/convert");
    }

    private void doWinDefaults() {
	if (getProperty("inputDir") == null)
	    setProperty("inputDir", System.getProperty("user.home"));
	if (getProperty("outputDir") == null)
	    setProperty("outputDir", System.getProperty("user.home"));
	if (getProperty("imageMagick") == null)
	    setProperty("imageMagick", "C:\\ImageMagick\\convert");
    }

    private void doLoad() {
	try {
	    load(new FileInputStream(System.getProperty("user.home") + SEP + "picsmgmt.properties"));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (SEP.equals("/"))
	    doMacDefaults();
	else
	    doWinDefaults();
	System.out.println(this);
	doSave();
    }

    public void doSave() {
	try {
	    store(new FileOutputStream(System.getProperty("user.home") + SEP + "picsmgmt.properties"), "");
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

}