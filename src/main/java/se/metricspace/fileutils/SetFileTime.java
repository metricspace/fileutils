package se.metricspace.fileutils;

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
