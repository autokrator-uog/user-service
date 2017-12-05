public class MyCommand extends Command {
    public MyCommand() {
        // "Prints a greeting", with command hello
        super("hello", "Prints a greeting");
    }

    @Override
    public void configure(Subparser subparser) {
        // Add a command line option
        subparser.addArgument("-u", "--user")
                .dest("user")
                .type(String.class)
                .required(true)
                .help("The user of the program");
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        System.out.println("Hello " + namespace.getString("user"));
    }
}