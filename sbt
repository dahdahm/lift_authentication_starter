java -Xmx1024M -Xss2M -XX:MaxPermSize=1024m -XX:+CMSClassUnloadingEnabled -noverify -jar `dirname $0`/sbt-launch-0.12.jar "$@"
