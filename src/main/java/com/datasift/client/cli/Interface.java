package com.datasift.client.cli;

import java.util.ArrayList;
import java.util.List;

import static com.datasift.client.cli.Parser.CliArguments;
import static com.datasift.client.cli.Parser.CliSwitch;

public class Interface {
    public static void main(String[] args) {
        List<CliSwitch> switches = new ArrayList<>();
        switches.add(new CliSwitch("a", "auth"));
        switches.add(new CliSwitch("e", "endpoint"));
        switches.add(new CliSwitch("c", "command"));
        switches.add(new CliSwitch("p", "param"));
        CliArguments parsedArgs = Parser.parse(args, switches);
        System.out.println(parsedArgs);
    }
}
