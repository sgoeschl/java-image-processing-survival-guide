# Java Advanced Imaging (JAI)

mvn install:install-file -Dfile=jai_core-1.1.3.jar -Dversion=1.1.3 -DgroupId=com.sun.media -DartifactId=jai_core -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
mvn install:install-file -Dfile=jai_codec-1.1.2_01.jar -Dversion=1.1.2_01 -DgroupId=com.sun.media -DartifactId=jai_codec -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
mvn install:install-file -Dfile=jai_imageio-1.1.jar -Dversion=1.1 -DgroupId=com.sun.media -DartifactId=jai_imageio -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
mvn install:install-file -Dfile=commons-imaging-1.0-20160915.001141-89.jar -Dversion=1.0-SNAPSHOT -DgroupId=org.apache.commons -DartifactId=commons-imaging -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
