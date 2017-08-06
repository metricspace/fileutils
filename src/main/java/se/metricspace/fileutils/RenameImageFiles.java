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

public class RenameImageFiles {
  public static void main(String[] args) {
    if(null!=args && 1==args.length) {
      java.nio.file.Path dir = java.nio.file.Paths.get(args[0]);
      if(java.nio.file.Files.exists(dir)) {
        if(java.nio.file.Files.isDirectory(dir)) {
          if(java.nio.file.Files.isReadable(dir)) {
            java.util.Map<String,java.util.Map<String,java.util.List<String>>> groupedFiles = new java.util.HashMap<>();
            try (java.nio.file.DirectoryStream<java.nio.file.Path> stream = java.nio.file.Files.newDirectoryStream(dir)) {
              java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyMMdd");
              for (java.nio.file.Path file: stream) {
                java.nio.file.attribute.BasicFileAttributes attrs = java.nio.file.Files.readAttributes(file, java.nio.file.attribute.BasicFileAttributes.class);
                if(java.nio.file.Files.exists(file) && java.nio.file.Files.isRegularFile(file) && java.nio.file.Files.isReadable(file)) {
                  String filename = file.getFileName().toString();
                  String creationdate = formatter.format(new java.util.Date(attrs.creationTime().toMillis()));
                  String suffix = getSuffix(filename);
                  System.out.println(filename+"/"+creationdate+"/"+suffix);
                  if(null!=suffix) {
                    java.util.Map<String,java.util.List<String>> filesByDateForSuffix = groupedFiles.getOrDefault(suffix, new java.util.HashMap<>());
                    java.util.List<String> files = filesByDateForSuffix.getOrDefault(creationdate, new java.util.ArrayList<>());
                    files.add(filename);
                    filesByDateForSuffix.put(creationdate, files);
                    groupedFiles.put(suffix, filesByDateForSuffix);
                  }
                }
              }
            } catch (java.io.IOException exception) {
              System.out.println("IOException: "+exception.getMessage());
              exception.printStackTrace(System.out);
            } 
            for(String suffix:groupedFiles.keySet()) {
              java.util.Map<String,java.util.List<String>> filesbydate = groupedFiles.get(suffix);
              for(String date:filesbydate.keySet()) {
                java.util.List<String> filenames = filesbydate.get(date);
                java.util.Collections.sort(filenames);
                int counter = 1;
                for(String fn:filenames) {
                  String newname=date+((counter<100)?"0":"")+((counter<10)?"0":"")+counter+"."+suffix;
                  System.out.println(newname+":"+fn);
                  java.nio.file.Path source = java.nio.file.Paths.get(args[0],fn);
                  java.nio.file.Path target = java.nio.file.Paths.get(args[0],newname);
                  try {
                    java.nio.file.Files.move(source, target);
                  } catch (java.io.IOException exception) {
                    System.out.println("IOException: "+exception.getMessage());
                    exception.printStackTrace(System.out);
                  }
                  counter++;
                }
              }
            }
          } else {
            System.out.println("se.metricspace.fileutils.RenameImageFiles: "+args[0]+" doesnt seem to be readable!");
          }
        } else {
          System.out.println("se.metricspace.fileutils.RenameImageFiles: "+args[0]+" doesnt seem to be a directory!");
        }
      } else {
        System.out.println("se.metricspace.fileutils.RenameImageFiles: "+args[0]+" doesnt seem to exist!");
      }
    } else {
      System.out.println("se.metricspace.fileutils.RenameImageFiles expects one argument (path to directory) but got "+args.length);
    }
  }

  static String getSuffix(String theFileName) {
    String suffix = null;
    if(null!=theFileName && theFileName.trim().length()>0) {
      // probably a silly check, should crash if the developer supplies a crappy filename
      String lowercasename = theFileName.toLowerCase();
      if(lowercasename.endsWith(".nef")) {
        suffix = "nef";
      } else if(lowercasename.endsWith(".tif")||lowercasename.endsWith(".tiff")) {
        suffix = "tif";
      } else if(lowercasename.endsWith(".jpg")||lowercasename.endsWith(".jpeg")) {
        suffix = "jpg";
      } else if(lowercasename.endsWith(".bmp")) {
        suffix = "bmp";
      } else if(lowercasename.endsWith(".png")) {
        suffix = "png";
      }
    }
    return suffix;
  }
}

