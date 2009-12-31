#!/bin/bash

HOMEDIR=$(dirname $0)
TARGETDIR=$HOMEDIR/target
HOMEDIR=$HOMEDIR/../../..

rm -rf $TARGETDIR

# options
VERSION=""
MVNPARAM=""
CREATETAG="false"
UPDATE="true"
PLUGINS="false"
PUSH="true"
OFFLINE="false"
TOMCAT="true"
SOURCE="true"

USAGE="usage create-release.sh --version VERSION"
USAGE="$USAGE [--offline|--create-tag|--disable-update|--with-plugins|--disable-push|--disable-tomcat|--help]"

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
    "--disable-tomcat")
      TOMCAT="false"
    ;;
    "--disable-source")
      SOURCE="false"
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
    	echo "--disable-tomcat    Create no tomcat release"
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
mvn clean install -P release -f $HOMEDIR/pom.xml $MVNPARAM
mkdir -p $TARGETDIR/webapp
cp $HOMEDIR/JAB-WebApp/target/*.war $TARGETDIR/webapp

# for tomcat
if [ "$TOMCAT" == "true" ]; then
  echo "[INFO] build tomcat release"
  mvn clean package -P release,tomcat -f $HOMEDIR/JAB-WebApp/pom.xml $MVNPARAM
  mkdir -p $TARGETDIR/webapp-tomcat
  cp $HOMEDIR/JAB-WebApp/target/*.war $TARGETDIR/webapp-tomcat
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