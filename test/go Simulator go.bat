@ECHO OFF
:BEGIN
CLS

echo play

CHOICE /c:"12345678" /m "select any number to run corresponding test case."

if errorlevel==8 goto eight
if errorlevel==7 goto seven
if errorlevel==6 goto six
if errorlevel==5 goto five
if errorlevel==4 goto four
if errorlevel==3 goto three
if errorlevel==2 goto two
if errorlevel==1 goto one
goto end

:one
echo you pressed one 
cd ..\src
echo ready to run..
java simStart ..\test\1_testCase\ ..\test\1_testCase\FilePath
goto BEGIN

:two
echo you pressed two
cd ..\src
echo ready to run..
java simStart ..\test\2_testCase\ ..\test\2_testCase\FilePath
goto BEGIN

:three
echo three pressed!!
cd ..\src
echo ready to run..
java simStart ..\test\3_testCase\ ..\test\3_testCase\FilePath
goto BEGIN

:four
echo four pressed!!
cd ..\src
echo ready to run..
java simStart ..\test\4_testCase\ ..\test\4_testCase\FilePath
goto BEGIN

:five
echo five pressed!!
cd ..\src
echo ready to run..
java simStart ..\test\5_testCase\ ..\test\5_testCase\FilePath
goto BEGIN

:six
echo six pressed!!
cd ..\src
echo ready to run..
java simStart ..\test\6_testCase\ ..\test\6_testCase\FilePath
goto BEGIN

:seven
echo seven pressed!!
cd ..\src
echo ready to run..
java simStart ..\test\7_testCase\ ..\test\7_testCase\FilePath
goto BEGIN

:eight
echo eight pressed!!
cd ..\src
echo ready to run..
java simStart ..\test\8_testCase\ ..\test\8_testCase\FilePath
goto BEGIN 


:end
