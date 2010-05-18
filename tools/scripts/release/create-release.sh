#!/bin/bash

HOMEDIR=$(cd $(dirname $0); pwd)
TARGETDIR=$HOMEDIR/target
HOMEDIR=$HOMEDIR/../../..

# options
VERSION=""
BUILD="true"
MVNPARAM=""
MVNPROFILES="release,mac-bundle,win-bundle,jsw-bundle"
CREATETAG="false"
UPDATE="true"
PLUGINS="false"
PUSH="true"
OFFLINE="false"
SOURCE="true"
SERVER="true"

USAGE="usage create-release.sh --version VERSION"
USAGE="$USAGE [--offline|--disable-build|--create-tag|--disable-update|--disable-server|--with-plugins|--disable-push|--help]"

PCOUNT=$#
for (( i=1; $i<=$PCOUNT; i++ ))
do

  case "$1" in
    "--offline")
    	OFFLINE="true"
      MVNPARAM="$MVNPARAM -o"
    ;;
    "--version")
      i=$[$i+1]
      shift
      VERSION=$1
    "--disable-build")
      BUILD="false"
    ;;
    "--create-tag")
      CREATETAG="true"
    ;;
    "--disable-update")
      UPDATE="false"
    ;;
    "--with-plugins")
      PLUGINS="true"
    ;;
    "--disable-push")
      PUSH="false"
    ;;
    "--disable-source")
      SOURCE="false"
    ;;
    "--disable-server")
      SERVER="false"
    ;;
    "--help")
      echo "$USAGE"
      echo ""
      echo "--version VERSION   The version of the release"
    	echo "--offline           Work offline"
    	echo "--create-tag        Create a mercurial tag with the given version"
    	echo "--disable-update    Disables the mercurial update"
    	echo "--with-plugins      Build plugins for the release"
    	echo "--disable-push      Disables the mercurial push after the tag"
    	echo "--disable-server    Create no server release"
    	echo "--disable-source    Create no source release"
    	echo "--help              Shows this help"
    	exit 0
    ;;
    *)
      echo "[ERROR] unknown parameter $1"
      echo $USAGE
      exit 1
    ;;
  esac
  
  shift
  
done

if [ "$VERSION" == "" ]; then
  echo "error: version parameter is required"
  echo $USAGE
  exit 2
fi

# update
if [ "$OFFLINE" != "true" ]; then
  if [ "$UPDATE" == "true" ]; then
    echo "[INFO] update repository"
    hg -R $HOMEDIR pull -u
  fi
else
  echo "[WARNING] update is disabled in offline mode"
fi

# for j2ee appservers
echo "[INFO] build release"

TARGETDIR=$TARGETDIR"/"$VERSION
rm -rf $TARGETDIR

if [ "$BUILD" == "true" ]; then
  mvn clean install -P $MVNPROFILES -f $HOMEDIR/pom.xml $MVNPARAM
fi

mkdir -p $TARGETDIR/webapp
cp -v $HOMEDIR/JAB-WebApp/target/*.war $TARGETDIR/webapp

# server
if [ "$SERVER" == "true" ]; then
  echo "[INFO] copy server bundles"
  mkdir -p $TARGETDIR/server/win $TARGETDIR/server/mac $TARGETDIR/server/jsw $TARGETDIR/server/gen
  # copy win version
  cp -v $HOMEDIR/JAB-Server/target/jab-server.exe $TARGETDIR/server/win
  # copy mac version
  cp -v $HOMEDIR/JAB-Server/target/JAB-Server-*.dmg $TARGETDIR/server/mac/jab-server.dmg
  # copy gen version
  cp -v $HOMEDIR/JAB-Server/target/jab-server.jar $TARGETDIR/server/gen
  # create jsw version 
  cp -v $HOMEDIR/JAB-Server/src/main/assembly/default-config.properties $HOMEDIR/JAB-Server/target/appassembler/jsw/jab-server/conf/jab-server.conf
  chmod +x $HOMEDIR/JAB-Server/target/appassembler/jsw/jab-server/bin/*
  mkdir -p $HOMEDIR/JAB-Server/target/appassembler/jsw/jab-server/logs
  cd $HOMEDIR/JAB-Server/target/appassembler/jsw/
  zip -qr $TARGETDIR/server/jsw/jab-server.zip jab-server
  tar -cf $TARGETDIR/server/jsw/jab-server.tar jab-server
  bzip2 -c $TARGETDIR/server/jsw/jab-server.tar > $TARGETDIR/server/jsw/jab-server.tar.bz2
  gzip $TARGETDIR/server/jsw/jab-server.tar
  cd -
fi

# source
if [ "$SOURCE" == "true" ]; then

  #echo "[INFO] build SoniaUtil source release"	
  #mvn clean package assembly:assembly -f $HOMEDIR/SoniaUtil/pom.xml $MVNPARAM -DdescriptorId=src
  #mkdir -p $TARGETDIR/source
  #cp $HOMEDIR/SoniaUtil/target/*-src.zip $TARGETDIR/source
  
  #echo "[INFO] build api source release"	
  #mvn clean package assembly:assembly -f $HOMEDIR/SoniaWebUtil/pom.xml $MVNPARAM -DdescriptorId=src
  #cp $HOMEDIR/SoniaWebUtil/target/*-src.zip $TARGETDIR/source
  
  echo "[INFO] build api source release"	
  mvn clean package assembly:assembly -f $HOMEDIR/JAB-API/pom.xml $MVNPARAM -DdescriptorId=src
  mkdir -p $TARGETDIR/source
  cp $HOMEDIR/JAB-API/target/*-src.zip $TARGETDIR/source
	
  echo "[INFO] build webapp source release"
  mvn clean package assembly:assembly -f $HOMEDIR/JAB-WebApp/pom.xml $MVNPARAM -DdescriptorId=src
  cp $HOMEDIR/JAB-WebApp/target/*-src.zip $TARGETDIR/source
fi

# plugins
if [ "$PLUGINS" == "true" ]; then
  echo "[INFO] copy plugins"

  mkdir $TARGETDIR/plugins

  for DIR in $(find $HOMEDIR/plugins -type d -depth 1); do
    NAME=$(basename $DIR)
    mkdir $TARGETDIR/plugins/$NAME
    cp $HOMEDIR/plugins/$NAME/target/*.jar $TARGETDIR/plugins/$NAME
    
    if [ "$SOURCE" == "true" ]; then
      mvn clean package assembly:assembly -f $HOMEDIR/plugins/$NAME/pom.xml $MVNPARAM -DdescriptorId=src
      cp $HOMEDIR/plugins/$NAME/target/*-src.zip $TARGETDIR/plugins/$NAME
    fi
    
  done
fi

# create tag
if [ "$CREATETAG" == "true" ]; then
  echo "[INFO] tag release"
	hg -R $HOMEDIR tag "$TAG"
	if [ "$OFFLINE" != "true" ]; then
	  if [ "$PUSH" == "true" ]; then
	    echo "[INFO] push release"
	    hg -R $HOMEDIR push
	  fi
	else
	  echo "[WARNING] push is disabled in offline mode"
	fi
fi

echo ""
echo "-- release can be found in $TARGETDIR --"