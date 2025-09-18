@echo off

rem -------------------------------------------------------------------------
rem
rem 使用说明：
rem
rem 1: 该脚本用于别的项目时只需要修改 MAIN_CLASS 即可运行
rem
rem 2: 启动方式:
rem     jfinal.bat start           - 使用默认端口8099启动
rem     jfinal.bat start 端口号     - 使用指定端口启动
rem     jfinal.bat stop            - 停止应用
rem     jfinal.bat restart         - 重启应用(使用默认端口)
rem     jfinal.bat restart 端口号   - 重启应用(使用指定端口)
rem
rem 3: JAVA_OPTS 可通过 -D 传入 undertow.port 与 undertow.host 这类参数覆盖
rem    配置文件中的相同值此外还有 undertow.resourcePath, undertow.ioThreads
rem    undertow.workerThreads 共五个参数可通过 -D 进行传入
rem
rem 4: JAVA_OPTS 可传入标准的 java 命令行参数,例如 -Xms256m -Xmx1024m 这类常用参数
rem
rem
rem -------------------------------------------------------------------------

setlocal & pushd


rem 启动入口类,该脚本文件用于别的项目时要改这里
set MAIN_CLASS=com.sjzu.edu.common.DemoConfig

rem 默认Java 命令行参数
set "DEFAULT_OPTS=-Xms512m -Xmx1024m -Dundertow.host=0.0.0.0"

rem 检查是否提供了端口参数
set PORT=%2
if "%PORT%"=="" (
    rem 没有提供端口参数，使用默认端口8099
    set "JAVA_OPTS=%DEFAULT_OPTS% -Dundertow.port=8099"
) else (
    rem 提供了端口参数，使用指定端口
    set "JAVA_OPTS=%DEFAULT_OPTS% -Dundertow.port=%PORT%"
)

if "%1"=="start" goto normal
if "%1"=="stop" goto normal
if "%1"=="restart" goto normal

goto error


:error
echo Usage:
echo   jfinal.bat start ^[port^]       - 启动应用(可选指定端口)
echo   jfinal.bat stop                - 停止应用
echo   jfinal.bat restart ^[port^]     - 重启应用(可选指定端口)
echo.
echo 示例:
echo   jfinal.bat start         使用默认端口8099启动
echo   jfinal.bat start 8080    使用端口8080启动
echo   jfinal.bat stop          停止应用
echo   jfinal.bat restart       使用默认端口重启
echo   jfinal.bat restart 8080  使用端口8080重启
goto :eof


:normal
if "%1"=="start" goto start
if "%1"=="stop" goto stop
if "%1"=="restart" goto restart
goto :eof


:start
set APP_BASE_PATH=%~dp0
set CP=%APP_BASE_PATH%config;%APP_BASE_PATH%lib\*
echo starting jfinal undertow on port %PORT%
java -Xverify:none %JAVA_OPTS% -cp %CP% %MAIN_CLASS%
goto :eof


:stop
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo stopping jfinal undertow
for /f "tokens=1" %%i in ('jps -l ^| find "%MAIN_CLASS%"') do ( taskkill /F /PID %%i )
goto :eof


:restart
call :stop
rem 为restart命令也支持端口参数
set RESTART_PORT=%2
if "%RESTART_PORT%"=="" (
    set "JAVA_OPTS=%DEFAULT_OPTS% -Dundertow.port=8099"
) else (
    set "JAVA_OPTS=%DEFAULT_OPTS% -Dundertow.port=%RESTART_PORT%"
)
set PORT=%RESTART_PORT%
goto start


endlocal & popd
pause