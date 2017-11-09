#!/usr/bin/env groovy

//ssh dev1 'grep `hostname` /etc/hosts'

def exec(shell, String... host) {

    def commands = shell.split() as List;

    //for remote shell
    if (host) {
        commands.add(0, host[0])
        commands.add(0, "ssh")
    }

//    println "[Command ] >> : ${commands} "

    def processBuilder = new ProcessBuilder(commands);
    def process = processBuilder.redirectErrorStream(true).start();
    def rt = [] as List;
    process.inputStream.eachLine {
        rt << it
    }
    process.waitFor();

    ["code": process.exitValue(), "msg":rt]
}

