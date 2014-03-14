import database.AccountServiceTest;
import database.HibernateUtil;
import frontend.Constants;
import frontend.FrontendTest;
import frontend.PageGeneratorTest;
import frontend.RoutingTest;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/*
 * Created by maxim on 11.03.14.
 */
public class TestRunner {
    public static void main(String[] args ) throws Exception
    {
        JUnitCore core = new JUnitCore();
        core.addListener(new CalcListener());
        if (core.run(RegisterTest.class, AuthTest.class, FrontendTest.class, PageGeneratorTest.class, AccountServiceTest.class).wasSuccessful()
                && RoutingTest.performRoutingTest(Constants.Url.AUTHFORM)
                && !RoutingTest.performRoutingTest("/something"))
            System.out.println("All tests were passed");
        else
            System.out.println("Some tests were failed");
        HibernateUtil.getSessionFactory(true).close();
        System.exit(0);
    }
}

class CalcListener extends RunListener {
    @Override
    public void testStarted(Description desc) {
        System.out.println("Started:" + desc.getDisplayName());
    }

    @Override
    public void testFinished(Description desc) {
        System.out.println("Finished:" + desc.getDisplayName());
    }

    @Override
    public void testFailure(Failure fail) {
        System.out.println("Failed:" + fail.getDescription().getDisplayName() + " [" + fail.getMessage() + "]");
    }
}