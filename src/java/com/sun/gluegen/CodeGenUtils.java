/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 * 
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package com.sun.gluegen;

import java.io.*;
import java.util.*;

public class CodeGenUtils {
  /**
   * Given a java package name (e.g., "java.lang"), return the package as a
   * directory path (i.e., "java/lang").
   */
  public static String packageAsPath(String packageName) {
    String path = packageName.replace('.', File.separatorChar);
    //System.out.println("Converted package [" + packageName + "] to path [" + path +"]");
    return path;
  }

  /**
   * @param generator the object that is emitting the autogenerated code. If
   * null, the generator will not be mentioned in the warning message.
   */
  public static void emitAutogeneratedWarning(PrintWriter w, Object generator) {
    w.print("/* !---- DO NOT EDIT: This file autogenerated ");
    if (generator != null)  {
      w.print("by ");
      w.print(packageAsPath(generator.getClass().getName()));
      w.print(".java ");
    }
    w.print("on ");
    w.print((new Date()).toString());
    w.println(" ----! */");
    w.println();
  }

  /**
   * Emit the opening headers for one java class/interface file.
   */
  public static void emitJavaHeaders(   PrintWriter w,
                                        String packageName,
                                        String className,
                                        String gluegenRuntimePackage,
                                        boolean isClassNotInterface,
                                        String[] imports,
                                        String[] accessModifiers,
                                        String[] interfaces,
                                        String classExtended,
                                        EmissionCallback classDocComment) throws IOException  {
    w.println("package " + packageName + ";");
    w.println();

    for (int i = 0; imports != null && i < imports.length; ++i) {
      w.print("import ");
      w.print(imports[i]);
      w.println(';');
    }
    w.println("import " + gluegenRuntimePackage + ".*;");

    w.println();

    if (classDocComment != null)
    {
      classDocComment.emit(w);
    }
      
    for (int i = 0; accessModifiers != null && i < accessModifiers.length; ++i) {
      w.print(accessModifiers[i]);
      w.print(' ');
    }

    if (isClassNotInterface) {
      w.print("class ");
      w.print(className);
      w.print(' ');
      if (classExtended != null) {
        w.print("extends ");
        w.print(classExtended);
      }
    }
    else {
      if (classExtended != null) {
        throw new IllegalArgumentException(
          "Autogenerated interface class " + className +
          " cannot extend class " + classExtended);
      }
      w.print("interface ");
      w.print(className);
      w.print(' ');
    }

    for (int i = 0; interfaces != null && i < interfaces.length; ++i) {
      if (i == 0) { w.print(isClassNotInterface ? "implements " : "extends "); }
      w.print(interfaces[i]);
      if (i < interfaces.length-1) { w.print(", "); }
    }

    w.println();
    w.println('{');
  }

  //-----------------------------------------

  /** A class that emits source code of some time when activated. */
  public interface EmissionCallback  {
    /** Emit appropriate source code through the given writer. */
    public void emit(PrintWriter output);
  }
}
