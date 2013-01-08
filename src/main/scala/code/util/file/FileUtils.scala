package code
package util
package file

import java.nio._
import java.nio.file._
import java.nio.file.attribute._
import java.io.File
import java.util.UUID
import net.liftweb.util.Props

object FileUtils {

  /**
   * Converts String path to Path object for non-null strings.
   */
  def getPath(path: String): Path = {
    require(path != null)
    Paths.get(path)
  }

  /**
   * Moves/renames file or directory. Source file must exist, but
   * destination will be created if not present.
   * Regular restrictions apply.
   * For more info and semantics check 'java.nio.file.Files.move'
   * method documentation.
   */
  def moveFile(from: String, to: String): Unit = {
    val source = getPath(from)
    if (Files.notExists(source))
      throw new IllegalArgumentException("Path [" + from + "] does not exist")
    val dest = getPath(to)
    Files.move(source, dest)
  }

  /**
   * Returns size of directory in bytes
   */
  def getDirectorySize(path: String): Long = {
    var size = 0L
    Files.walkFileTree(getPath(path), new SimpleFileVisitor[Path]() {
      override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        size += attrs.size()
        super.visitFile(file, attrs)
      }
    })
    size
  }

  /**
   * Returns list of all files filtered by predicate.
   * TODO: consider changing it to lazy streams or remove this todo.
   */
  def getFiles(path: String, filter: BasicFileAttributes => Boolean): List[File] = {
    var files = List[File]()
    Files.walkFileTree(getPath(path), new SimpleFileVisitor[Path]() {
      override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        if (filter(attrs))
          files = files :+ new File(file.getParent.toString, file.getFileName.toString)
        super.visitFile(file, attrs)
      }
    })
    files
  }

  /**
   * Takes a path and appends current date, time and GUID
   * to make it unique.
   * It does not check any directory existence.
   * UUID part is required because time/date adjustments
   * might potentially case problems with uniqueness.
   * Time part is useful for telling when directory was
   * created/moved.
   * 
   * Example input:
   *   "/tmp/alex"
   * example output:
   *   "/tmp/alex_06-12-2012_14:31:27_39cc9379-2d51-4216-b93f-f374d51c28cb"
   */
  def getUniqueDirName(path: String): String = {
    val format = new java.text.SimpleDateFormat("dd-MM-yyyy_H:m:s")
    val datePart = format.format(new java.util.Date())
    val uid = UUID.randomUUID().toString
    path + "_" + datePart + "_" + uid;
  }
  
  /**
   * Returns default users home dir from properties file.
   * This directory might have different requirements as a home dir
   * or for disk usage checks, so we keep it as a string.
   */
  val defaultHomeDir: String = Props.get("user.home.dir").get
  
  /**
   * Reads properties file and returns path to user home
   * directory in default user home.
   * This method is OS agnostic.
   */
  def getUserHomeDir(username: String): String =
    (new File(defaultHomeDir, username)).getAbsolutePath

  /**
   * Returns total space available in bytes on disk or partition
   * where path directory/file/link resides.
   * It follows symlinks and thus finds disk space for
   * corresponding target path, not of a symlink.
   * Remember that hard links can exist within single
   * file system only.
   * Requires a valid path, or an exception is thrown.
   */
  def getTotalDiskSpace(pathOnDisk: String = defaultHomeDir): Long =
    getValidPath(pathOnDisk) getTotalSpace

  /**
   * {@link #getTotalDiskSpace}
   */
  def getFreeDiskSpace(pathOnDisk: String = defaultHomeDir): Long =
    getValidPath(pathOnDisk) getFreeSpace

  /**
   * {@link #getTotalDiskSpace}
   */
  def getUsedDiskSpace(pathOnDisk: String = defaultHomeDir): Long =
    getTotalDiskSpace(pathOnDisk) - getFreeDiskSpace(pathOnDisk)

  def getValidPath(pathOnDisk: String, p: File => Boolean = _ => true): File = {
    val path = new File(pathOnDisk)
    require(path.exists && p(path), "Directory [" + pathOnDisk + "] must exist and have correct permissions.")
    path
  }

}
