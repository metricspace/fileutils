/*
BSD 3-Clause License

Copyright (c) 2017, Magnus Norden
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package se.metricspace.fileutils;

/**
 * @author Magnus Norden ( http://metricspace.se )
 */

public class SetFileTime {
  public static void main(String[] args) {
    if(null!=args && 2==args.length) {
      java.nio.file.Path path = java.nio.file.Paths.get(args[0]);
      if(java.nio.file.Files.exists(path) && java.nio.file.Files.isReadable(path) && (java.nio.file.Files.isDirectory(path) || java.nio.file.Files.isRegularFile(path))) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try {
          java.util.Date date = format.parse(args[1]);
          java.nio.file.attribute.FileTime fileTime = java.nio.file.attribute.FileTime.fromMillis(date.getTime());
          if(java.nio.file.Files.isRegularFile(path)) {
            setFileTime(fileTime, path);
          } else {
            try (java.nio.file.DirectoryStream<java.nio.file.Path> stream = java.nio.file.Files.newDirectoryStream(path)) {
              for (java.nio.file.Path file: stream) {
                if(java.nio.file.Files.exists(file) && java.nio.file.Files.isRegularFile(file) && java.nio.file.Files.isReadable(file)) {
                  setFileTime(fileTime, file);
                }
              }
            } catch (java.io.IOException exception) {
              System.out.println("IOException: "+exception.getMessage());
              exception.printStackTrace(System.out);
            } 
          }
        } catch (java.io.IOException exception) {
          System.out.println("IOExceptionwhen processing files: "+exception.getMessage());
        } catch (java.text.ParseException exception) {
          System.out.println("ParseException, expected time on the format 'yy-MM-dd HH:mm:ss', but got problem when parsing '"+args[1]+"': "+exception.getMessage());
        } catch (Throwable exception) {
          System.out.println("Problem ("+exception.getClass().getName()+") processing times for file "+exception.getMessage());
        }
      } else {
        System.out.println(args[0]+" doesnt seem to be a readable file or directory!");
      }
    } else {
      System.out.println("se.metricspace.fileutils.SetFileTime expects two arguments: <path> <timestamp (yy-MM-dd HH:mm:ss)>");
    }
  }

  private static void setFileTime(java.nio.file.attribute.FileTime time, java.nio.file.Path file) throws java.io.IOException {
    java.nio.file.Files.setAttribute(file, "basic:creationTime", time, java.nio.file.LinkOption.NOFOLLOW_LINKS);             
    java.nio.file.Files.setAttribute(file, "basic:lastAccessTime", time, java.nio.file.LinkOption.NOFOLLOW_LINKS);                               
    java.nio.file.Files.setAttribute(file, "basic:lastModifiedTime", time, java.nio.file.LinkOption.NOFOLLOW_LINKS);                               
    System.out.println("Setting timestamp for "+file+" to "+time);
  }
}
