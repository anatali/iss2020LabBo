@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  it.unibo.springbootIntro startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and IT_UNIBO_SPRINGBOOT_INTRO_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\it.unibo.springbootIntro-0.0.1.jar;%APP_HOME%\lib\jssc-2.8.0.jar;%APP_HOME%\lib\californium-proxy-2.0.0-M12.jar;%APP_HOME%\lib\californium-core-2.0.0-M12.jar;%APP_HOME%\lib\httpasyncclient-4.1.4.jar;%APP_HOME%\lib\httpclient-4.5.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\json-20090211.jar;%APP_HOME%\lib\uniboInterfaces.jar;%APP_HOME%\lib\unibonoawtsupports.jar;%APP_HOME%\lib\it.unibo.robotPojo-1.0.jar;%APP_HOME%\lib\spring-boot-starter-actuator-2.2.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-webflux-2.2.4.RELEASE.jar;%APP_HOME%\lib\element-connector-2.0.0-M12.jar;%APP_HOME%\lib\nio-multipart-parser-1.1.0.jar;%APP_HOME%\lib\nio-stream-storage-1.1.3.jar;%APP_HOME%\lib\spring-boot-starter-json-2.2.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-validation-2.2.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-2.2.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-logging-2.2.4.RELEASE.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\log4j-to-slf4j-2.12.1.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.30.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\guava-15.0.jar;%APP_HOME%\lib\httpcore-nio-4.4.13.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\commons-codec-1.13.jar;%APP_HOME%\lib\spring-boot-actuator-autoconfigure-2.2.4.RELEASE.jar;%APP_HOME%\lib\micrometer-core-1.3.2.jar;%APP_HOME%\lib\spring-boot-starter-reactor-netty-2.2.4.RELEASE.jar;%APP_HOME%\lib\spring-webflux-5.2.3.RELEASE.jar;%APP_HOME%\lib\spring-web-5.2.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-autoconfigure-2.2.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-actuator-2.2.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-2.2.4.RELEASE.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\spring-context-5.2.3.RELEASE.jar;%APP_HOME%\lib\spring-aop-5.2.3.RELEASE.jar;%APP_HOME%\lib\spring-beans-5.2.3.RELEASE.jar;%APP_HOME%\lib\spring-expression-5.2.3.RELEASE.jar;%APP_HOME%\lib\spring-core-5.2.3.RELEASE.jar;%APP_HOME%\lib\snakeyaml-1.25.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.10.2.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.10.2.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.10.2.jar;%APP_HOME%\lib\jackson-databind-2.10.2.jar;%APP_HOME%\lib\HdrHistogram-2.1.11.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\reactor-netty-0.9.4.RELEASE.jar;%APP_HOME%\lib\jakarta.el-3.0.3.jar;%APP_HOME%\lib\jakarta.validation-api-2.0.2.jar;%APP_HOME%\lib\hibernate-validator-6.0.18.Final.jar;%APP_HOME%\lib\reactor-core-3.3.2.RELEASE.jar;%APP_HOME%\lib\spring-jcl-5.2.3.RELEASE.jar;%APP_HOME%\lib\jackson-annotations-2.10.2.jar;%APP_HOME%\lib\jackson-core-2.10.2.jar;%APP_HOME%\lib\netty-codec-http2-4.1.45.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.45.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.45.Final.jar;%APP_HOME%\lib\netty-handler-4.1.45.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.45.Final-linux-x86_64.jar;%APP_HOME%\lib\jboss-logging-3.4.1.Final.jar;%APP_HOME%\lib\classmate-1.5.1.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\log4j-api-2.12.1.jar;%APP_HOME%\lib\netty-codec-socks-4.1.45.Final.jar;%APP_HOME%\lib\netty-codec-4.1.45.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.45.Final.jar;%APP_HOME%\lib\netty-transport-4.1.45.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.45.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.45.Final.jar;%APP_HOME%\lib\netty-common-4.1.45.Final.jar

@rem Execute it.unibo.springbootIntro
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %IT_UNIBO_SPRINGBOOT_INTRO_OPTS%  -classpath "%CLASSPATH%" it.unibo.iss.it.unibo.springbootIntro.Application %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable IT_UNIBO_SPRINGBOOT_INTRO_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%IT_UNIBO_SPRINGBOOT_INTRO_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
