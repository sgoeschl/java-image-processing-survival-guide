# Java Advanced Imaging (JAI)

call mvn install:install-file -Dfile=jai_core-1.1.3.jar -Dversion=1.1.3 -DgroupId=com.sun.media -DartifactId=jai_core -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=jai_codec-1.1.2_01.jar -Dversion=1.1.2_01 -DgroupId=com.sun.media -DartifactId=jai_codec -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=jai_imageio-1.1.jar -Dversion=1.1 -DgroupId=com.sun.media -DartifactId=jai_imageio -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true

# TwelveMonkeys ImageIO plugins

call mvn install:install-file -Dfile=imageio-tiff-3.0.20140131.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.imageio -DartifactId=imageio-tiff -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=imageio-jpeg-3.0.20140131.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.imageio -DartifactId=imageio-jpeg -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=imageio-jpeg-3.0.20140131-sources.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.imageio -DartifactId=imageio-jpeg -Dclassifier=sources -Dpackaging=jar -DcreateChecksum=true
call mvn install:install-file -Dfile=imageio-core-3.0.20140131.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.imageio -DartifactId=imageio-core -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=imageio-core-3.0.20140131-sources.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.imageio -DartifactId=imageio-core -Dclassifier=sources -Dpackaging=jar -DcreateChecksum=true
call mvn install:install-file -Dfile=imageio-metadata-3.0.20140131.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.imageio -DartifactId=imageio-metadata -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=imageio-metadata-3.0.20140131-sources.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.imageio -DartifactId=imageio-metadata -Dclassifier=sources -Dpackaging=jar -DcreateChecksum=true
call mvn install:install-file -Dfile=common-image-3.0.20140131.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.common -DartifactId=common-image -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=common-image-3.0.20140131-sources.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.common -DartifactId=common-image -Dclassifier=sources -Dpackaging=jar -DcreateChecksum=true
call mvn install:install-file -Dfile=common-io-3.0.20140131.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.common -DartifactId=common-io -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=common-io-3.0.20140131-sources.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.common -DartifactId=common-io -Dclassifier=sources -Dpackaging=jar -DcreateChecksum=true
call mvn install:install-file -Dfile=common-lang-3.0.20140131.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.common -DartifactId=common-lang -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile=common-lang-3.0.20140131-sources.jar -Dversion=3.0.20140131 -DgroupId=com.twelvemonkeys.common -DartifactId=common-lang -Dclassifier=sources -Dpackaging=jar -DcreateChecksum=true