package turingmachine;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            var exceptionStr = "filename as first argument required.\ninput to turing-machine as second argument required";
            throw new Exception(exceptionStr);
        }

        var tm = new TuringMachine(args[0]);

        var output = tm.simulate(args[1]);

        System.out.println(output);
    }
}
