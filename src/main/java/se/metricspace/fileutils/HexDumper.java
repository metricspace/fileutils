package se.metricspace.fileutils;

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
