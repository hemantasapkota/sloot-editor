package com.laex.cg2d.render.util;

import com.laex.cg2d.render.MyGdxGameDesktop;

public final class AppExceptionUtil {

  public static void handle(Throwable t) {
    MyGdxGameDesktop.lwjglApp().error("Error Details", t.getMessage());
    MyGdxGameDesktop.lwjglApp().exit();
    System.exit(0); // terrible hack. This is needed because the above call to
                    // lwjglApp().exit() does not work.
  }

}
