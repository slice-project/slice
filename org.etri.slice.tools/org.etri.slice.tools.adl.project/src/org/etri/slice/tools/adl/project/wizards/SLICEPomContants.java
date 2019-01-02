package org.etri.slice.tools.adl.project.wizards;

public class SLICEPomContants {
	public static final String POM_XML_CONTENTS = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\r\n" + 
			"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
			"	xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\r\n" + 
			"	\r\n" + 
			"	<modelVersion>4.0.0</modelVersion>\r\n" + 
			"	\r\n" + 
			"  <groupId>{0}</groupId>\r\n" + 
			"  <artifactId>{1}</artifactId>\r\n" + 
			"	<version>0.0.1-SNAPSHOT</version>\r\n" + 
			"\r\n" + 
			"	<properties>\r\n" + 
			"		<slice.home>/Users/yhsuh/development/slice-project/git/slice/org.etri.slice.distribution</slice.home>\r\n" + 
			"	</properties>\r\n" + 
			"\r\n" + 
			"	<repositories>\r\n" + 
			"		<repository>\r\n" + 
			"			<id>local-repo</id>\r\n" + 
			"			<url>file://$'{'slice.home'}'/repository</url>\r\n" + 
			"			<releases>\r\n" + 
			"				<enabled>true</enabled>\r\n" + 
			"				<checksumPolicy>ignore</checksumPolicy>\r\n" + 
			"				<updatePolicy>always</updatePolicy>\r\n" + 
			"			</releases>\r\n" + 
			"			<snapshots>\r\n" + 
			"				<enabled>true</enabled>\r\n" + 
			"				<checksumPolicy>ignore</checksumPolicy>\r\n" + 
			"				<updatePolicy>always</updatePolicy>\r\n" + 
			"			</snapshots>\r\n" + 
			"		</repository>\r\n" + 
			"		<repository>\r\n" + 
			"			<id>central</id>\r\n" + 
			"			<url>https://repo.maven.apache.org/maven2/</url>\r\n" + 
			"		</repository>\r\n" + 
			"	</repositories>\r\n" + 
			"\r\n" + 
			"	<dependencies>\r\n" + 
			"		<dependency>\r\n" + 
			"			<groupId>org.etri.slice</groupId>\r\n" + 
			"			<artifactId>org.etri.slice.commons</artifactId>\r\n" + 
			"			<version>1.1.0</version>\r\n" + 
			"		</dependency>\r\n" + 
			"		<dependency>\r\n" + 
			"			<groupId>org.eclipse.xtend</groupId>\r\n" + 
			"			<artifactId>org.eclipse.xtend.lib</artifactId>\r\n" + 
			"			<version>2.13.0</version>\r\n" + 
			"		</dependency>\r\n" + 
			"	</dependencies>\r\n" + 
			"\r\n" + 
			"	<build>\r\n" + 
			"		<sourceDirectory>src</sourceDirectory>\r\n" + 
			"		<resources>\r\n" + 
			"			<resource>\r\n" + 
			"				<directory>org.etri.slice.generated</directory>\r\n" + 
			"				<excludes>\r\n" + 
			"					<exclude>**/*.java</exclude>\r\n" + 
			"				</excludes>\r\n" + 
			"			</resource>\r\n" + 
			"		</resources>\r\n" + 
			"		<plugins>\r\n" + 
			"			<plugin>\r\n" + 
			"				<artifactId>maven-compiler-plugin</artifactId>\r\n" + 
			"				<version>3.7.0</version>\r\n" + 
			"				<configuration>\r\n" + 
			"					<source>1.8</source>\r\n" + 
			"					<target>1.8</target>\r\n" + 
			"				</configuration>\r\n" + 
			"			</plugin>\r\n" + 
			"		</plugins>\r\n" + 
			"	</build>\r\n" + 
			"</project>";
}
