package com.crispysnippets.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * simple Logger Class wrapping java.util.logging.Logger in case I want to switch to something else
 * @author PruvostC
 */
public class CrispyLogger extends Logger {
  //It's enough to instantiate a factory once and for all.
  //private static MyLoggerFactory myFactory = new MyLoggerFactory();
 
  public CrispyLogger(String name, String resourceBundleName) {
    super(name, resourceBundleName);
  }
  
  protected Logger newInstance(String name, String resourceBundleName) {
    return new CrispyLogger(name, resourceBundleName); 
  }
  
  public void error(String msg) {
    super.log(Level.SEVERE, msg);
  }
  
  public void info(String msg) {
    super.log(Level.INFO, msg);
  }
  
  public void debug(String msg) {
    super.log(Level.FINER,msg);
  }

}

