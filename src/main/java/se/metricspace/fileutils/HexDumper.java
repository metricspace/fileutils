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

public class HexDumper {
  public static void main(String [] args) {
    if(null!=args && args.length>0) {
      for(String filename: args) {
        java.io.FileInputStream source = null;
        try {
          source = new java.io.FileInputStream(filename);
          dump(source);
        } catch (java.io.IOException exception) {
          System.out.println("IOException while reading input: "+exception.getMessage());
          exception.printStackTrace(System.out);
        } finally {
          if(null!=source) {
            try {
              source.close();
            } catch (Throwable exception) {
            }
            source = null;
          }
        }
      }
    } else {
      try {
        dump(System.in);
      } catch (java.io.IOException exception) {
        System.out.println("IOException while reading input: "+exception.getMessage());
        exception.printStackTrace(System.out);
      }
    }
  }

  private static void dump(java.io.InputStream theSource) throws java.io.IOException {
    int size = 0;
    byte[] buffer = new byte[16];
    char[] map = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    while((size=theSource.read(buffer))>0) {
      StringBuilder builder = new StringBuilder(": ");
      for(int index=0;index<size;index++) {
        int data = (256 + (int) buffer[index]) % 256;
        System.out.print(" ");
        System.out.print(map[data/16]);
        System.out.print(map[data%16]);
        if(data>31 && data<127) {
          builder.append((char)buffer[index]);
        } else {
          builder.append('*');
        }
        builder.append(' ');
      }
      for(int index=size;index<16;index++) {
        System.out.print("   ");
      }
      System.out.println(builder.toString());
    }
  }
}
