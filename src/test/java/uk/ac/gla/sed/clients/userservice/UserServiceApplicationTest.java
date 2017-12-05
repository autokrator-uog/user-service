public class UserServiceApplicationTest {
    private final Environment environment = mock(Environment.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);
    private final UserServiceApplication application = new UserServiceApplication();
    private final UserServiceConfiguration config = new UserServiceConfiguration();

    @Before
    public void setup() throws Exception {
        config.setMyParam("yay");
        when(environment.jersey()).thenReturn(jersey);
    }

    @Test
    public void buildsAThingResource() throws Exception {
        application.run(config, environment);

        verify(jersey).register(isA(UserResource.class));
    }
}