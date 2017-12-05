import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

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

	@Override
	public void configure(Subparser arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(Bootstrap<?> arg0, Namespace arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
}