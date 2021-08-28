package com.orbis.ssgascraper.webscraper;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import java.util.logging.Level;

public class BrowserDriver {

  public static JBrowserDriver buildDriver() {
    return new JBrowserDriver(
        Settings.builder()
            .timezone(Timezone.UTC)
            .maxConnections(200)
            .socketTimeout(10000)
            .connectionReqTimeout(10000)
            .connectTimeout(10000)
            .loggerLevel(Level.SEVERE)
            .build());
  }

}
