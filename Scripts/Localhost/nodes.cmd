@echo off
title Starting Nodes...
echo.
echo 1: Start multiple nodes
echo 2: Start only 1 node

:op
echo.
set/p option=Option:
if %option%==1 (goto op1)
if %option%==2 (goto op2)
echo Invalid Option
goto op



:op1
echo.
set/p nodes=Nodes: 

if %nodes%==1 (goto 1)
if %nodes%==2 (goto 2)
if %nodes%==3 (goto 3)
if %nodes%==4 (goto 4)
if %nodes%==5 (goto 5)
if %nodes%==6 (goto 6)
if %nodes%==7 (goto 7)
if %nodes%==8 (goto 8)
echo Only from nodes 1 to 8
goto op1

:8
start "Node 8" frascati run NodeComposite -libpath node8.jar

:7
start "Node 7" frascati run NodeComposite -libpath node7.jar

:6
start "Node 6" frascati run NodeComposite -libpath node6.jar

:5
start "Node 5" frascati run NodeComposite -libpath node5.jar

:4
start "Node 4" frascati run NodeComposite -libpath node4.jar

:3
start "Node 3" frascati run NodeComposite -libpath node3.jar

:2
start "Node 2" frascati run NodeComposite -libpath node2.jar

:1
start "Node 1" frascati run NodeComposite -libpath node1.jar
goto end




:op2
echo.
set/p node=Node: 

if %node%==1 (goto 1n)
if %node%==2 (goto 2n)
if %node%==3 (goto 3n)
if %node%==4 (goto 4n)
if %node%==5 (goto 5n)
if %node%==6 (goto 6n)
if %node%==7 (goto 7n)
if %node%==8 (goto 8n)
echo Only from nodes 1 to 8
goto op2

:8n
start "Node 8" frascati run NodeComposite -libpath node8.jar
goto end

:7n
start "Node 7" frascati run NodeComposite -libpath node7.jar
goto end

:6n
start "Node 6" frascati run NodeComposite -libpath node6.jar
goto end

:5n
start "Node 5" frascati run NodeComposite -libpath node5.jar
goto end

:4n
start "Node 4" frascati run NodeComposite -libpath node4.jar
goto end

:3n
start "Node 3" frascati run NodeComposite -libpath node3.jar
goto end

:2n
start "Node 2" frascati run NodeComposite -libpath node2.jar
goto end

:1n
start "Node 1" frascati run NodeComposite -libpath node1.jar
goto end


:end